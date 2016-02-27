package hha.main;

import hha.util.DataFileReader;

import org.json.JSONException;
import org.json.JSONObject;

public class AppData {
	//login
	public static String username;
	public static int id;
	public static boolean isLogined = false;
	
	//robot
	public static int Speed = 50; //语速
	public static int Pitch = 50; //语调
	public static int Volume = 5; //音量
	
	//setting
	public static int SystemAudio = 50; //100为最大
	public static int RecognizableLanguage = 0; //0为汉语 1为粤语
	public static String Speaker = "xiaoyan"; //发音人设置
	public static boolean DebugMode = false;
	public static boolean NetworkMode = true;
	
	public static void SaveData()
	{
		JSONObject root = new JSONObject();
		try {
			root.put("username", username)
			.put("id",id)
			.put("Speed",Speed)
			.put("Pitch",Pitch)
			.put("Volume",Volume)
			.put("SystemAudio",SystemAudio)
			.put("RecognizableLanguage",RecognizableLanguage)
			.put("Speaker",Speaker)
			.put("DebugMode",DebugMode)
			.put("NetworkMode", NetworkMode);
			
			String data = root.toString();
			DataFileReader.WriteFile("Setting.json",data);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void LoadData()
	{
		String data = DataFileReader.ReadFile("Setting.json");
		if (data == null) 
			return;
		try {
			JSONObject root = new JSONObject(data);
			username = root.optString("username");
			id = root.optInt("id");
			Speed = root.optInt("Speed");
			Pitch = root.optInt("Pitch");
			Volume = root.optInt("Volume");
			SystemAudio = root.optInt("SystemAudio");
			RecognizableLanguage = root.optInt("RecognizableLanguage");
			Speaker = root.optString("Speaker");
			DebugMode = root.optBoolean("DebugMode");
			NetworkMode = root.optBoolean("NetworkMode");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
/*
class LoginData{
	String username;
	int id;
	boolean isLogined = false;
}

class SettingData{
	int SystemAudio = 50; //100为最大
	int RecognizableLanguage = 0; //0为汉语 1为粤语
	String Speaker = "xiaoyan"; //发音人设置
	boolean DebugMode = false;
	boolean NetworkMode = true;
}

class RobotParams{
	int Speed = 50; //语速
	int Pitch = 50; //语调
	int Volume = 5; //音量
}
*/
