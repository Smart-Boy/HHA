package hha.util;

import hha.main.AppData;
import hha.main.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateUtil{
	private HttpClient httpClient;
	
	public UpdateUtil() {
		super();
		httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
	}

	public void Login(String url,String username, String password) {
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        formparams.add(new BasicNameValuePair("username", username));  
        formparams.add(new BasicNameValuePair("password", password));  
        PostUtil util = new PostUtil(httpPost,formparams,httpClient) {

			@Override
			public void Callback(String ret) {
				// TODO Auto-generated method stub
				int id = -1;
				try {
					JSONObject jsonObject = new JSONObject(ret);
					id = jsonObject.getInt("id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (id != -1)
					AppData.isLogined = true;
				MainActivity.main.ShowTextOnUIThread(ret);
			}
        };
        Thread t = new Thread(util);
        t.setDaemon(true);
        t.start();
	}

	public void Update(String url, String name, String value) {
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        formparams.add(new BasicNameValuePair("name", name));  
        formparams.add(new BasicNameValuePair("value", value));  
        
        PostUtil util = new PostUtil(httpPost,formparams,httpClient) {

			@Override
			public void Callback(String ret) {
				// TODO Auto-generated method stub
				int id = -1;
				try {
					JSONObject jsonObject = new JSONObject(ret);
					id = jsonObject.getInt("ret");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MainActivity.main.ShowTextOnUIThread(ret);
			}
        };
        Thread t = new Thread(util);
        t.setDaemon(true);
        t.start();

	}

	
}

abstract class PostUtil  implements Runnable {
	private HttpPost httpPost;
	private List<? extends NameValuePair> formparams;
	private HttpClient httpClient;

	public PostUtil(HttpPost httpPost,
			List<? extends NameValuePair> formparams, HttpClient httpClient) {
		super();
		this.httpPost = httpPost;
		this.formparams = formparams;
		this.httpClient = httpClient;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httpPost.setEntity(uefEntity);  
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();  
				String ret = EntityUtils.toString(entity, "UTF-8");
				Callback(ret);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public abstract void Callback(String ret);
}
