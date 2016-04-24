package com.example.lenovo.map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


public class MessageDetailActivity extends AppCompatActivity {


    Bundle bundle;
    private String nick_name;
    private String title;
    private String tag;
    private String message;
    private String time;

    Toolbar toolbar;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    MessageData messageData;
    TextView tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        bundle = this.getIntent().getExtras();
        messageData = bundle.getParcelable("MessageDetail");

        nick_name = messageData.getNick_name();
        title = messageData.getTitle();
        tag = messageData.tagToString(messageData.getTag());
        message = messageData.getMessage();
        time = messageData.getTime();

        toolbar = (Toolbar) this.findViewById(R.id.toolbar_detail_message);

        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerview_message);

        tv_message = (TextView) this.findViewById(R.id.tv_message);
        tv_message.setText(message);

        final View.OnClickListener toolbar_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        toolbar.setNavigationOnClickListener(toolbar_listener);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter(nick_name,title,tag,time,recyclerView);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST));
    }
}
