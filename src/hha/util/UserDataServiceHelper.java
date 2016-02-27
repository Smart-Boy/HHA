package hha.util;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;



public class UserDataServiceHelper {
    /** 时间戳 */
    static long serial = new Date().getTime();
 
    /** KEY */
    static String key = "后台人员会给你个密钥";
 
    /** 网站地址 */
    private static final String HOST_IP = "你的请求地址";
 
    /** 请求服务器 S1,S2,S3等 */
    static String server = "s1";
 
    /** 返回码 1=成功 */
    public static String results = null;
 
    /** 登录UID */
    public static String LoginUid = null;
 
    /** 注册UID */
    public static String RegisterUid = null;
 
    /** 快速注册根据返回得到用户名 */
    public static String FastUserName = null;
 
    /** 快速注册根据返回得到密码 */
    public static String FastPassWord = null;
 
    /** 快速注册根据返回UID */
    public static String Uid = null;
     
     
    /** 充值请求地址*/
    private  static final String HOST_IP_CREDIT = "你的充值请求地址";
     
 
    // 注册
    public static boolean Register(Context context, String action, String username,
            String password, String from) {
        Log.i("TAG", "得到的" + context + action + username + password + from);
        try {
            HttpClient httpClient = new DefaultHttpClient();
            // 请求超时
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
 
            // 读取超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
            StringBuilder uri = new StringBuilder();
            uri.append(HOST_IP + "reg" + "?" + "action=" + action);
            uri.append("&username=");
            uri.append(username);
            uri.append("&password=");
            uri.append(password);
            uri.append("&from=");
            uri.append(from);
            uri.append("&server=");
            uri.append(server);
            uri.append("&apitime=");
            uri.append(serial);
            uri.append("&key=");
            uri.append(getMD5Str(serial + key));
            Log.i("TAG", "请求地址:" + uri.toString());
            HttpPost httpPost = new HttpPost(uri.toString());
            HttpResponse httpResponse = httpClient.execute(httpPost);
            String jsonforString = null;
            String result = null;
            String result2 = null;
            String result3 = null;
            String result4 = null;
            String result5 = null;
            String result6 = null;
            String result7 = null;
            // 返回json报文
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                jsonforString = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(jsonforString);
                Log.i("TAG", "JSONObject对象=" + jsonforString);
                result = jsonObject.getString("result");
                result2 = jsonObject.getString("msg");
                Log.i("TAG", "result2:返回的错误信息"+result2);
                Log.i("TAG", "成功还是失败返回" + result);
                if (httpClient != null) {
                    httpClient.getConnectionManager().shutdown();
                }
                if (result.equalsIgnoreCase("1")) {
                    return true;
                } else {
                    return false;
                }
                 
                 
                 
                 
                 
            }
        } catch (ConnectException e) {
            Toast.makeText(context, "网络连接异常", Toast.LENGTH_LONG).show();
        } catch (SocketTimeoutException e) {
            Toast.makeText(context, "连接超时", Toast.LENGTH_LONG).show();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
 
    // 登录
    public static boolean logins(Context context, String action, String username, String password,
            String from) {
        Log.i("TAG", "得到的" + context + action + username + password);
        try {
            HttpClient httpClient = new DefaultHttpClient();
            // 请求超时
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
 
            // 读取超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
            StringBuilder uri = new StringBuilder();
            uri.append(HOST_IP + action + "?"  );
            uri.append("&username=");
            uri.append(username);
            uri.append("&password=");
            uri.append(password);
            uri.append("&from=");
            uri.append(from);
            uri.append("&server=");
            uri.append(server);
            uri.append("&apitime=");
            uri.append(serial);
            uri.append("&key=");
            uri.append(getMD5Str(serial + key));
            Log.i("TAG", "请求地址:" + uri.toString());
            HttpPost httpPost = new HttpPost(uri.toString());
            HttpResponse httpResponse = httpClient.execute(httpPost);
            String jsonforString = null;
            String result = null;
            String login = null;
            // 返回json报文
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                jsonforString = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(jsonforString);
                Log.i("TAG", "JSONObject对象=" + jsonforString);
                result = jsonObject.getString("result");
                login = jsonObject.getString("uid");
                Log.i("TAG", "成功还是失败返回" + result);
                if (httpClient != null) {
                    httpClient.getConnectionManager().shutdown();
                }
                if (result.equalsIgnoreCase("1")) {
                    Log.i("TAG", "服务器返回码" + result);
                    results = result;
                    LoginUid = login;
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ConnectException e) {
            Toast.makeText(context, "网络连接异常", Toast.LENGTH_LONG).show();
        } catch (SocketTimeoutException e) {
            Toast.makeText(context, "连接超时", Toast.LENGTH_LONG).show();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
 
    // 快速注册
    public static boolean fast(Context context, String action, String from) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            // 请求超时
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
 
            // 读取超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
            StringBuilder uri = new StringBuilder();
            uri.append(HOST_IP + "XXXXXXXX" + "?" + "action=" + action);
            uri.append("&from=");
            uri.append(from);
            uri.append("&server=");
            uri.append(server);
            uri.append("&apitime=");
            uri.append(serial);
            uri.append("&key=");
            uri.append(getMD5Str(serial + key));
            Log.i("TAG", "快速注册请求地址:" + uri.toString());
            HttpPost httpPost = new HttpPost(uri.toString());
            HttpResponse httpResponse = httpClient.execute(httpPost);
            String jsonforString = null;
            String result = null;
            String UserNames = null;
            String PassWords = null;
            String UserId = null;
            // 返回json报文
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                jsonforString = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(jsonforString);
                Log.i("TAG", "JSONObject对象=" + jsonforString);
                result = jsonObject.getString("result");
                Log.i("TAG", "快速注册成功还是失败返回码" + result);
                UserNames = jsonObject.getString("username");
                PassWords = jsonObject.getString("password");
                Log.i("TAG", "快速注册成功返回的密码" + PassWords);
                Log.i("TAG", "快速注册成功返回用户名" + UserNames);
                UserId = jsonObject.getString("uid");
                if (httpClient != null) {
                    httpClient.getConnectionManager().shutdown();
                }
                if (result.equalsIgnoreCase("1")) {
                    Log.i("TAG", "快速注册服务器返回码" + result);
                    results = result;
                    FastUserName = UserNames;
                    FastPassWord = PassWords;
                    Uid = UserId;
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ConnectException e) {
            Toast.makeText(context, "网络连接异常", Toast.LENGTH_LONG).show();
        } catch (SocketTimeoutException e) {
            Toast.makeText(context, "连接超时", Toast.LENGTH_LONG).show();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
 
    // 修改帐号
    public static boolean updateData(Context context, String action, String uid, String username,
            String password, String newname, String newpass) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            // 请求超时
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
 
            // 读取超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
            StringBuilder uri = new StringBuilder();
            uri.append(HOST_IP + "XXXXXXXXX" + "?" + "action=" + action);
            uri.append("&uid=");
            uri.append(uid);
            uri.append("&username=");
            uri.append(username);
            uri.append("&password=");
            uri.append(password);
            uri.append("&newname=");
            uri.append(newname);
            uri.append("&newpass=");
            uri.append(newpass);
            uri.append("&server=");
            uri.append(server);
            uri.append("&apitime=");
            uri.append(serial);
            uri.append("&key=");
            uri.append(getMD5Str(serial + key));
            Log.i("TAG", "提交更新:" + uri.toString());
            HttpPost httpPost = new HttpPost(uri.toString());
            HttpResponse httpResponse = httpClient.execute(httpPost);
            String jsonforString = null;
            String result = null;
            String UserNames = null;
            String PassWords = null;
            String UserId = null;
            // 返回json报文
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                jsonforString = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(jsonforString);
                // Log.i("TAG", "JSONObject对象=" + jsonforString);
                result = jsonObject.getString("result");
                Log.i("TAG", "提交更新返回码" + result);
                // UserNames = jsonObject.getString("username");
                // PassWords = jsonObject.getString("password");
                // Log.i("TAG", "快速注册成功返回的密码" + PassWords);
                // Log.i("TAG", "快速注册成功返回用户名" + UserNames);
                UserId = jsonObject.getString("uid");
                if (httpClient != null) {
                    httpClient.getConnectionManager().shutdown();
                }
                if (result.equalsIgnoreCase("1")) {
                    Log.i("TAG", "提交更新" + result);
                    // results = result;
                    // FastUserName = UserNames;
                    // FastPassWord = PassWords;
                    // Uid = UserId;
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ConnectException e) {
            Toast.makeText(context, "网络连接异常", Toast.LENGTH_LONG).show();
        } catch (SocketTimeoutException e) {
            Toast.makeText(context, "连接超时", Toast.LENGTH_LONG).show();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
 
     
 
    // MD5
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
 
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString();
    }
 
 
}

