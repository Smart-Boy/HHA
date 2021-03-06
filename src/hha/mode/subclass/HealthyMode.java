package hha.mode.subclass;

import hha.main.MainActivity;
import hha.mode.Mode;
import hha.mode.PArray;
import hha.util.DataFileReader;
import hha.util.DateCal;
import hha.util.RandomSequence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bitoflife.chatterbean.util.UserData;

import com.lilele.automatic.AuTomatic;

public class HealthyMode extends Mode {

	Random rand = new Random();
	JSONObject root;
	private PArray user = new PArray();
	private PArray relationship = new PArray();
	private PArray element = new PArray();
	private PArray senior = new PArray();

	public PArray getUser() {
		return user;
	}

	public void setUser(PArray user) {
		this.user = user;
	}

	public PArray getRelationship() {
		return relationship;
	}

	public void setRelationship(PArray relationship) {
		this.relationship = relationship;
	}

	public PArray getElement() {
		return element;
	}

	public void setElement(PArray element) {
		this.element = element;
	}

	public PArray getSenior() {
		return senior;
	}

	public void setSenior(PArray senior) {
		this.senior = senior;
	}

	public PArray getHistory() {
		return history;
	}

	public void setHistory(PArray history) {
		this.history = history;
	}

	PArray history = new PArray();

	public HealthyMode(MainActivity mainActivity, AuTomatic auto) {
		super(mainActivity, auto);
		// TODO Auto-generated constructor stub

		i_waitTime = (int) N_rand(10, 5, 5, 20);

		String jsondata = DataFileReader.ReadFile("healthy.json");
		if (jsondata == null) {
			jsondata = DataFileReader.ReadAsset("healthy.json");
			DataFileReader.WriteFile("healthy.json", jsondata);
		}

		mainActivity.ShowTextOnUIThread(jsondata);

		try {
			root = new JSONObject(jsondata);
			Init();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void Init() {
		try {
			user.list = JSONArrayToStringArray(root.getJSONArray("user"),
					"name");
			relationship.list = JSONArrayToStringArray(
					root.getJSONArray("relationship"), "name");
			element.list = JSONArrayToStringArray(root.getJSONArray("element"),
					"name");
			senior.list = JSONArrayToStringArray(root.getJSONArray("senior"),
					"name");
			history.list = JSONArrayToStringArray(root.getJSONArray("history"),
					"name");
			
			user.unit = JSONArrayToStringArray(root.getJSONArray("user"),
					"unit");
			relationship.unit = JSONArrayToStringArray(
					root.getJSONArray("relationship"), "unit");
			element.unit = JSONArrayToStringArray(root.getJSONArray("element"),
					"unit");
			senior.unit = JSONArrayToStringArray(root.getJSONArray("senior"),
					"unit");
			history.unit = JSONArrayToStringArray(root.getJSONArray("history"),
					"unit");
			
			user.fre = JSONArrayToIntArray(root.getJSONArray("user"),
					"fre");
			relationship.fre = JSONArrayToIntArray(
					root.getJSONArray("relationship"), "fre");
			element.fre = JSONArrayToIntArray(root.getJSONArray("element"),
					"fre");
			senior.fre = JSONArrayToIntArray(root.getJSONArray("senior"),
					"fre");
			history.fre = JSONArrayToIntArray(root.getJSONArray("history"),
					"fre");
			
			if ("true".equals(bot.getProperty("unordered_mode_on"))) {
				// user.list = unordered_seq(user.list);
				unordered_seq(relationship);
				unordered_seq(element);
				unordered_seq(senior);
				unordered_seq(history);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void unordered_seq(PArray str) {
		int[] sx = RandomSequence.random_serial(str.list.length);
		String[] newList = new String[str.list.length];
		int[] newFre = new int[str.list.length];
		String[] newUnit = new String[str.list.length];
		for (int i = 0; i < str.list.length; i++) {
			newList[sx[i]] = str.list[i];
			newUnit[sx[i]] = str.unit[i];
			newFre[sx[i]] = str.fre[i];
		}
		str.list = newList;
		str.unit = newUnit;
		str.fre = newFre;
	}
	private int[] JSONArrayToIntArray(JSONArray array, String att)
			throws JSONException {
		int[] strings = new int[array.length()];
		for (int i = 0; i < array.length(); i++) {
			JSONObject jobj = array.getJSONObject(i);
			strings[i] = jobj.getInt(att);
		}
		return strings;
	}
	
	private String[] JSONArrayToStringArray(JSONArray array, String att)
			throws JSONException {
		String[] strings = new String[array.length()];
		for (int i = 0; i < array.length(); i++) {
			JSONObject jobj = array.getJSONObject(i);
			strings[i] = jobj.getString(att);
		}
		return strings;
	}

	private List<String> element_ask = new ArrayList<String>();

	private boolean FindNullProperty(PArray data,String pclass) {
		for (int i = data.p; i < data.list.length; i++) {
			String prop = bot.getProperty(pclass+"_"+data.list[i]);
			if (prop == null) {
				String ans = bot.Respond("ask_" + data.list[i]);
				mainActivity.Show(null, ans);
				data.p = i;
				return true;
			}
		}
		return false;
	}
	
	public boolean isTimeToAsk(PArray data,String pclass)
	{
		for (int i = data.p; i < data.list.length; i++) {
			List<UserData> userdata = bot.getUserData(pclass+"_"+data.list[i]);
			if ((userdata == null) || (userdata.size()==0))
			{
				String ans = bot.Respond("ask_" + data.list[i]);
				mainActivity.Show(null, ans);
				data.p = i;
				return true;
			}
			UserData lastData = userdata.get(userdata.size()-1);
			if (DateCal.getDay(lastData.date,new Date())>data.fre[i])
			{
				String ans = bot.Respond("ask_" + data.list[i]);
				mainActivity.Show(null, ans);
				data.p = i;
				return true;
			}
		}
		return false;
	}

	public void CheckItem() {
		if (FindNullProperty(user,"user")) {
			return;
		}

		int t = rand.nextInt(2);
		switch (t) {
		case 0:
			if (isTimeToAsk(element,"element")) {
				return;
			}
		case 1:
			if (isTimeToAsk(senior,"senior")) {
				return;
			}
		default:
			if (isTimeToAsk(element,"element")) {
				return;
			}
		}
		
		bot.setProperty("mode", "normal");
	}

	@Override
	public void Run(int UserCount, int RobotCount) {
		// TODO Auto-generated method stub
		if ((i_waitTime == RobotCount)||(6 == UserCount)) {
			mainActivity.ShowTextOnUIThread("CheckItem");
			CheckItem();

			i_waitTime = (int) N_rand(30, 6, 25, 35);
		}
		if (UserCount == 90) {
			mainActivity.ShowTextOnUIThread("免打扰");
			mainActivity.Show(null, bot.Respond("AUTO_确认听见"));
		}
		
		if (UserCount == 180) {
			mainActivity.ShowTextOnUIThread("免打扰");
			mainActivity.Show(null, bot.Respond("AUTO_免打扰"));
			auto.setB_exit(true);
		}
	}

	@Override
	protected String getModeName() {
		// TODO Auto-generated method stub
		return "healthy";
	}

}
