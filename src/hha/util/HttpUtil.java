package hha.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author yoUng
 * @description 发送http请求
 * @filename HttpUtil.java
 * @time 2011-6-15 下午05:26:36
 * @version 1.0
 */
public abstract class HttpUtil implements Runnable{

	public static String http(String url, Map<String, String> params,String method,String cookie) {
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (Entry<String, String> e : params.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue());
				sb.append("&");
			}
			sb.substring(0, sb.length() - 1);
		}
		System.out.println("send_url:" + url);
		System.out.println("send_data:" + sb.toString());
		// 尝试发送请求
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod(method);
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			con.setRequestProperty("Cookie",cookie);  
			OutputStreamWriter osw = new OutputStreamWriter(
					con.getOutputStream(), "UTF-8");
			osw.write(sb.toString());
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}

		// 读取返回内容
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}

	String url;
	Map<String, String> params;
	String method;
	String cookie;
	
	public HttpUtil(String url, Map<String, String> params, String method,String cookie) {
		super();
		this.url = url;
		this.params = params;
		this.method = method;
		this.cookie = cookie;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String ret = HttpUtil.http(url, params, method,cookie);
		Callback(ret);
	}
	
	public abstract void Callback(String ret);
	
	public void Start() {
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}
}

