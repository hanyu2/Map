package com.example.lenovo.map;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SendMessageActivity extends AppCompatActivity {

    EditText edt_title;
    EditText edt_nick_name;
    EditText edt_tag;
    EditText edt_message;
    Toolbar toolbar;
    Button btn_send;
    MyHandler handler;
    Thread thread;
    SendMsgThread sendMsgThread;
    String email;
    String postTime;
    String deadTime;
    String nickname;
    String title;
    String message;
    String[] tags;
    int type;
    double[] location;
    MessageData messageData;
    TextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        final Bundle bundle = this.getIntent().getExtras();
        handler = new MyHandler();
        edt_title = (EditText) this.findViewById(R.id.edt_title);
        edt_nick_name = (EditText) this.findViewById(R.id.edt_nick_name);
        edt_tag = (EditText) this.findViewById(R.id.edt_tag);
        edt_message = (EditText) this.findViewById(R.id.edt_message);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar_send_message);
        btn_send = (Button) this.findViewById(R.id.btn_send);

        View.OnClickListener toolbar_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        toolbar.setNavigationOnClickListener(toolbar_listener);

        View.OnClickListener sendClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = edt_title.getText().toString();
                nickname = edt_nick_name.getText().toString();
                tags = edt_tag.getText().toString().split(" ");
                message = edt_message.getText().toString();
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                postTime = sDateFormat.format(new Date());
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);//one day
                deadTime = sDateFormat.format(calendar.getTime());
                location = new double[]{bundle.getDouble("lat"), bundle.getDouble("lng")};
                type = 1;

                SharedPreferences settings = getSharedPreferences("setting",0);
                email = settings.getString("email","");

                if(title.isEmpty()){
                    edt_title.setError("title cannot be empty");
                }
                else if(nickname.isEmpty()){
                    edt_nick_name.setError("nickname cannot be empty");
                }
                else if(message.isEmpty()){
                    Toast.makeText(SendMessageActivity.this, "please enter your message", Toast.LENGTH_SHORT).show();
                }
                else{
                    messageData = new MessageData(email,postTime,deadTime,nickname,location,title,message,type,tags);
                    String str_tag = messageData.tagsToString(tags);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email",messageData.getEmail());
                        jsonObject.put("nickname",messageData.getNick_name());
                        jsonObject.put("time",messageData.getTime());
                        jsonObject.put("dead_time", messageData.getDead_time());
                        jsonObject.put("lat",messageData.getLocation()[0]);
                        jsonObject.put("lng",messageData.getLocation()[1]);
                        jsonObject.put("title",messageData.getTitle());
                        jsonObject.put("desc",messageData.getMessage());
                        jsonObject.put("type",messageData.getType());
                        jsonObject.put("tags",str_tag);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sendMsgThread = new SendMsgThread(jsonObject);
                    thread = new Thread(sendMsgThread);
                    thread.start();
                }
            }
        };
        btn_send.setOnClickListener(sendClickListener);
    }

    class MyHandler extends android.os.Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            if(msgId==1){
                String data = msg.getData().getString("data");
                if(data.equals("s")){
                    Toast.makeText(SendMessageActivity.this, "send successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    class SendMsgThread implements Runnable{
        JSONObject jsonObject;

        public SendMsgThread(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }
        public void run(){
            String data = NetUtils.postMessage(jsonObject);
            System.out.println("==============="+data+"=================\n");
            Message message = new Message();
            message.what = 1;
            Bundle bundle = new Bundle();
            bundle.putString("data", data);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}
