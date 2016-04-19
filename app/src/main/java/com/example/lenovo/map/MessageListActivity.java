package com.example.lenovo.map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MessageListActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView tv_nickname;
    TextView tv_title;
    TextView tv_tag;
    TextView tv_message;
    TextView tv_time;
    String nickname;
    String title;
    String tag;
    String message;
    String time;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar_detail_message);
        tv_nickname = (TextView) this.findViewById(R.id.tv_nick_name);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_tag = (TextView) this.findViewById(R.id.tv_tag);
        tv_message = (TextView) this.findViewById(R.id.tv_message);
        tv_time = (TextView) this.findViewById(R.id.tv_time);

        settings = getSharedPreferences("setting", 0);
        editor = settings.edit();

        nickname = settings.getString("nickname", "default");
        title = settings.getString("title", "default");
        tag = settings.getString("tag","default");
        message = settings.getString("message","default");
        time = settings.getString("time","default");

        tv_nickname.setText(nickname);
        tv_title.setText(getIntent().getExtras().getString("title"));
        tv_tag.setText(tag);
        tv_message.setText(message);
        tv_time.setText(time);

        View.OnClickListener toolbar_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        toolbar.setNavigationOnClickListener(toolbar_listener);

    }
}
