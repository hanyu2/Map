package com.example.lenovo.map;

/**
 * Created by Administrator on 2016/4/10.
 */

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetUtils {
    private static final String TAG = "netUtils";
    public static String logIn(String email, String pwd){
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL murl = new URL("http://52.26.192.7/login.php");
            conn = (HttpURLConnection)murl.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);

            //post request data
            String data = "email=" + email + "&password=" + pwd;
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode == 200){
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                return sb.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "nofucking message";
    }

    public static String register(String email, String pwd){
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL murl = new URL("http://52.26.192.7/register.php");
            conn = (HttpURLConnection)murl.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);

            //post request data
            String data = "email=" + email + "&password=" + pwd;
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode == 200){
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                return sb.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "nofucking message";
    }

    public static String postMessage(JSONObject message){
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL murl = new URL("http://52.26.192.7/postm.php");
            conn = (HttpURLConnection)murl.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);

            //post request data
            String data = message.toString();
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode == 200){
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                return sb.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "nofucking message";
    }

    public static List<MessageData> getMsgs(JSONObject message){
        HttpURLConnection conn = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL murl = new URL("http://52.26.192.7/getms.php");
            conn = (HttpURLConnection)murl.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true);

            //post request data
            String data = message.toString();
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode == 200){
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                String jsonString = sb.toString();
                JSONArray jsonArray = new JSONArray(jsonString);
                List<MessageData> datas = new ArrayList<MessageData>();
                for(int i = 0;i < jsonArray.length(); ++i){
                    JSONObject object = jsonArray.getJSONObject(i);
                    MessageData item = new MessageData();
                    item.setEmail(object.getString("email"));
                    item.setTime(object.getString("time"));
                    item.setDead_time(object.getString("deadtime"));
                    item.setLocation(new double[]{object.getDouble("lat"), object.getDouble("lng")});
                    item.setTitle(object.getString("title"));
                    item.setMessage(object.getString("desc"));
                    item.setType(object.getInt("type"));
                    item.setTag(object.getString("tag"));
                    //System.out.println(item.MToString());
                    datas.add(item);
                }
                return datas;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
