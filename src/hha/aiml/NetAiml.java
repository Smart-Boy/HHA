package hha.aiml;

import hha.main.AppData;
import hha.main.MainActivity;
import hha.util.HttpUtil;
import hha.util.UpdateUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import bitoflife.chatterbean.util.UserData;

public class NetAiml {

	String ip;
	String aiml_url;
	
	String data_url;
	
	int port = 10010;
	Socket socket;

	PrintStream ps;
	BufferedReader br;
	InputStream input;
	OutputStream output;
	Set<Integer> learned = new HashSet<Integer>();
	

	public NetAiml(String _aiml_url,String _data_url) {
		aiml_url = _aiml_url;
		data_url = _data_url;
		learned.add(0);
		util = new UpdateUtil();
	}

	public NetAiml(String _ip, int _port) {
		ip = _ip;
		port = _port;
		learned.add(0);
	}
	public void Close()
	{
		try {
			ps.println("END");
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean Connect() {
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(ip,port), 500);
			input = socket.getInputStream();
			output = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(input,"gbk"));
			ps = new PrintStream(output);
			ps.println("BEGIN");
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public void SendCommand(String str) {
		ps.println(str);
	}

	public void SendArg(String arg) {
		ps.println("BEGIN{");
		ps.println(arg);
		ps.println("}END");
	}

	
	
	public String GetNetAiml(String match) {

		StringBuilder sb = new StringBuilder();
		String is = null;
		String k;
		try {
			SendCommand("FindByString");
			SendArg(match);
			
			
			while (!(k = br.readLine()).equals("END")) {
				sb.append(k);
			}
			
			int i = Integer.parseInt(sb.toString());
			if (!learned.contains(i))
			{
				SendCommand("Find");
				SendArg(String.valueOf(i));
				
				sb.deleteCharAt(0);
				while (!(k = br.readLine()).equals("END")) {
					sb.append(k);
				}
				learned.add(i);
				is = sb.toString();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}
	
	public String send(String BaseUrl,String action,String method,String data) {
		Thread t = new Thread();
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		String result = null;
		URL url = null;
		try {
			url = new URL(BaseUrl+action);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
			connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
          //Post方式不能缓存数据，则需要手动设置使用缓存的值为false  
            connection.setUseCaches(false);  
            DataOutputStream dop = new DataOutputStream(
                    connection.getOutputStream());
            dop.writeBytes(data);
            dop.flush();
            dop.close();
            in = new InputStreamReader(connection.getInputStream(),"utf-8");
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer strBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
 
        }
		return result;
	}
	
	public String post(String BaseUrl,String action,String key,String value){
		try {
			return send(BaseUrl,action,"POST",URLEncoder.encode(key,"utf-8")+"="+URLEncoder.encode(value,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public String post(String BaseUrl,String action,String key,String value,String key2,String value2){
		try {
			return send(BaseUrl,action,"POST",URLEncoder.encode(key,"utf-8")+"="+URLEncoder.encode(value,"utf-8")+
					"&"+URLEncoder.encode(key2,"utf-8")+"="+URLEncoder.encode(value2,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public String post(String BaseUrl,String action,String method,Map<String,String> map){
		StringBuilder sb = new StringBuilder();
		try {
		boolean f = true;
		for (Entry<String,String> e :map.entrySet()) {
			if (f) f = false;
			else sb.append("&");
			sb.append(URLEncoder.encode(e.getKey(),"utf-8")+"="+URLEncoder.encode(e.getValue(),"utf-8"));
		}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return send(BaseUrl,action,method,sb.toString());
	}
	
	public String getAiml(String match) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("text", match);
		MainActivity.main.ShowTextOnUIThread("match:"+match);
//		String id = post(aiml_url,"findbystring","text",match);
		String id = HttpUtil.http(aiml_url+"findbystring", map, "post", "");
		MainActivity.main.ShowTextOnUIThread("return:"+id);
		int i = Integer.parseInt(id.toString());
		if (!learned.contains(i))
		{
			String aiml = post(aiml_url,"find","id",id);
			MainActivity.main.ShowTextOnUIThread("aiml:"+aiml);
			learned.add(i);
			return aiml;
		}
		return null;
	}

//	public static boolean isLogin = false;
	public static String ret = null;
    /**  
     * post方式登录  
     * @param username  
     * @param password  
     * @param loginAction  
     * @return  
     * @throws Exception  
     */  
    public static String getCookie(String username,String password,String loginAction) throws Exception{  
        //登录  
        URL url = new URL(loginAction);  
        String param = "username="+username+"&password="+password;  
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
        conn.setDoInput(true);  
        conn.setDoOutput(true);  
        conn.setRequestMethod("POST");  
        OutputStream out = conn.getOutputStream();  
        out.write(param.getBytes());  
        out.flush();  
        out.close();
        
     // 读取返回内容
 		StringBuffer buffer = new StringBuffer();
 		
		BufferedReader br = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));
		String temp;
		while ((temp = br.readLine()) != null) {
			buffer.append(temp);
			buffer.append("\n");
		}
 		
 		ret = buffer.toString();
 		
        String sessionId = "";  
        String cookieVal = "";  
        String key = null;  
        //取cookie  
        for(int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++){  
            if(key.equalsIgnoreCase("set-cookie")){  
                cookieVal = conn.getHeaderField(i);  
                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));  
                sessionId = sessionId + cookieVal + ";";  
            }  
        }  
        return sessionId;  
    }  
    String cookie = "";
    public static UpdateUtil util;
    
    public boolean Login(String username,String password) {
    	try {
    		MainActivity.main.ShowTextOnUIThread("username:"+username+"\npassword:"+password+"\nurl:"+data_url+"AccountApi/login");
//			cookie = getCookie(username,password,data_url+"AccountApi/login");
    		util.Login(data_url+"AccountApi/login", username, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MainActivity.main.ShowTextOnUIThread("Error:"+e.getMessage());
		}
//    	MainActivity.main.ShowTextOnUIThread("cookie:"+cookie);
//		MainActivity.main.ShowTextOnUIThread("return:"+ret);
    	return true;
    }
	
	public void AddUserData(String name, UserData ud) {
		if (AppData.isLogined) {
			util.Update(data_url+"UserData/SurveyName",name,ud.str);
		}
	}

    
    
//	public void AddUserData(String name, UserData ud) {
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("name", name);
//		map.put("value", ud.str);
//		// TODO Auto-generated method stub
//		if (AppData.isLogined) {
//			HttpUtil http = new HttpUtil(data_url+"UserData/Survey",map,"POST",cookie){
//				@Override
//				public void Callback(String ret) {
//					// TODO Auto-generated method stub
//					MainActivity.main.showTip("数据已同步");
//				}
//			};
//			http.Start();
//		}
//	}

    
    
}
