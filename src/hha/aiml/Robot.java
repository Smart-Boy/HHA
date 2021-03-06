package hha.aiml;

import hha.main.AppData;
import hha.main.MainActivity;
import hha.mode.Database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.res.AssetManager;
import bitoflife.chatterbean.AliceBot;
import bitoflife.chatterbean.BotEmotion;
import bitoflife.chatterbean.Context;
import bitoflife.chatterbean.Graphmaster;
import bitoflife.chatterbean.Match;
import bitoflife.chatterbean.aiml.AIMLParser;
import bitoflife.chatterbean.aiml.AIMLParserConfigurationException;
import bitoflife.chatterbean.aiml.AIMLParserException;
import bitoflife.chatterbean.parser.AliceBotParser;
import bitoflife.chatterbean.parser.AliceBotParserConfigurationException;
import bitoflife.chatterbean.parser.AliceBotParserException;
import bitoflife.chatterbean.util.UserData;

public class Robot implements Runnable {

	AliceBot bot = null;
	Context context = null;
	private Database db = null;

	public Database getDb() {
		return db;
	}

	Graphmaster graphmaster = null;
	BotEmotion emotion = null;
	AssetManager am = null;
	boolean canfind = false;
	String command = null;
	MainActivity main = null;
	NetAiml net;
	private ByteArrayOutputStream gossip;

	public Robot(AssetManager am, MainActivity main) {
		// TODO Auto-generated constructor stub
		this.am = am;
		this.main = main;
	}

	public void InitDataBase(MainActivity main) {
		db = new Database(main, context);
		db.InitDatabase();
	}

	public void InitRobot() {
		// /初始化分词系统
		try {
			
			InputStream prop = am.open("jcseg.properties",
					AssetManager.ACCESS_BUFFER);

			Jcseg.init(prop);
			prop.close();

			String[] strings = null;
			strings = am.list("lexicon");

			// InputStream[] inputStreams = new InputStream[strings.length];
			for (int i = 0; i < strings.length; i++) {
				InputStream is = am.open("lexicon/" + strings[i],
						AssetManager.ACCESS_BUFFER);
				Jcseg.initDic(is);
				is.close();
			}
			Jcseg.initSeg();

			// 初始化机器人
			gossip = new ByteArrayOutputStream();

			String[] aimls = am.list("aiml");
			InputStream[] aimlstreams = new InputStream[aimls.length];
			for (int i = 0; i < aimls.length; i++) {
				aimlstreams[i] = am.open("aiml/" + aimls[i],
						AssetManager.ACCESS_BUFFER);
			}
			AliceBotParser parser = new AliceBotParser();

			bot = parser.parse(
					am.open("context.xml", AssetManager.ACCESS_BUFFER),
					am.open("splitters.xml", AssetManager.ACCESS_BUFFER),
					am.open("substitutions.xml", AssetManager.ACCESS_BUFFER),
					aimlstreams);

			context = bot.getContext();
			graphmaster = bot.getGraphmaster();
			emotion = bot.getEmotion();
			emotion.main = main;
			emotion.init();
			// setProperty("mode", "healthy");
			if (AppData.NetworkMode) {
				net = new NetAiml((String) context.property("bot.aiml_url"),
					(String) context.property("bot.data_url"));
			}
			InitDataBase(main);
			context.outputStream(gossip);
			// int _port = Integer.parseInt((String)
			// context.property("bot.port"));
			// net = new NetAiml((String) context.property("bot.ip"), _port);
			
			main.showTip("Bot Bootup");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			main.showTip("Bot IOException");
			e.printStackTrace();
		} catch (AliceBotParserConfigurationException e) {
			// TODO Auto-generated catch block
			main.showTip("Bot AliceBotParserConfigurationException");
			e.printStackTrace();
		} catch (AliceBotParserException e) {
			main.Show("Error", e.getMessage());
			// TODO Auto-generated catch block
			main.showTip("Bot AliceBotParserException");
			e.printStackTrace();
		}
	}

	public boolean isInitDone() {
		return bot != null;
	}

	public void BeginInit() {
		new Thread(this).start();
	}

	public static InputStream StringTOInputStream(String in) {

		ByteArrayInputStream is = null;
		try {
			is = new ByteArrayInputStream(in.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}

	public String Respond(String str) {

		if (!isInitDone()) {
			return "";
		}

		String output = "";
		String ansString = bot.respond(str);

		bitoflife.chatterbean.Context context = bot.getContext();

		output = (String) context.property("predicate.CanNotFind");
		context.property("predicate.CanNotFind", "null");
		command = (String) context.property("predicate.Command");
		context.property("predicate.Command", "null");

		if ("True".equals(output)) {
			canfind = false;
		} else
			canfind = true;

		if ((AppData.NetworkMode) && (canfind == false)) {
			Match[] matchs = bot.getMatchdata();
			for (Match match : matchs) {
				String[] strings = match.getMatchPath();
				StringBuilder sb = new StringBuilder();
				for (String string : strings) {
					sb.append(string + " ");
				}
				// if (net.Connect()) {
				// String st = net.GetNetAiml(sb.toString());
				// net.Close();
				// if (st != null) {
				// LearnFromStream(StringTOInputStream(st));
				// return Respond(str);
				// }
				// }
				try {
					String st = net.getAiml(sb.toString());
					if (st != null) {
						LearnFromStream(StringTOInputStream(st));
						return Respond(str);
					}
				} catch (Exception e) {
					MainActivity.main.ShowTextOnUIThread("Error:"+match);
				}
			}
		}
		return ansString;
	}

	public boolean CanFindAnswer() {
		return canfind;
	}

	public String getCommand() {
		if (command.equals("null"))
			return null;
		return command;
	}

	public String getProperty(String str) {
		if (!isInitDone()) {
			return null;
		}
		return (String) context.property("predicate." + str);
	}

	public void setProperty(String str, String data) {
		if (!isInitDone()) {
			return;
		}
		context.property("predicate." + str, data);
	}

	public List<UserData> getUserData(String str) {
		if (!isInitDone()) {
			return null;
		}
		return (List<UserData>) context.property("userdata." + str);
	}

	public void addUserData(String name, String data, Date date) {
		if (!isInitDone()) {
			return;
		}
		List<UserData> UserDatalist = (List<UserData>) context
				.property("userdata." + name);
		UserData ud = new UserData(data, date);
		if (UserDatalist == null) {
			UserDatalist = new ArrayList<UserData>();
			UserDatalist.add(ud);
			context.property("userdata." + name, UserDatalist);
		} else {
			UserDatalist.add(ud);
		}
		db.AddUserData(name, ud);
		if (AppData.NetworkMode)
			net.AddUserData(name, ud);
	}

	public void LearnFromStream(InputStream stream) {
		try {
			AIMLParser parser = new AIMLParser();
			parser.parse(graphmaster, stream);
		} catch (AIMLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AIMLParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void LearnFromUrl(String address) {
		URL url = null;
		AIMLParser parser = null;
		try {
			url = new URL(address);
			parser = new AIMLParser();
			parser.parse(graphmaster, url.openStream());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (AIMLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AIMLParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AliceBot getBot() {
		return bot;
	}

	public AssetManager getAm() {
		return am;
	}

	public NetAiml getNet() {
		return net;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		InitRobot();
	}
}
