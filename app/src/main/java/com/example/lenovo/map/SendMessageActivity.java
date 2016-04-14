package com.example.lenovo.map;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMessageActivity extends AppCompatActivity {

    Button btn_send;
    EditText edtext_title;
    EditText edtext_message;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        edtext_title = (EditText) this.findViewById(R.id.edt_title);
        edtext_message = (EditText) this.findViewById(R.id.edt_message);
        btn_send = (Button) this.findViewById(R.id.btn_send);
        toolbar = (Toolbar) this.findViewById(R.id.tool_bar_send);
        //setSupportActionBar(toolbar);
        View.OnClickListener toolbar_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        toolbar.setNavigationOnClickListener(toolbar_listener);
        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_send:
                        String title = edtext_title.getText().toString();
                        String message = edtext_message.getText().toString();
                        if(title.isEmpty()){
                            Toast.makeText(SendMessageActivity.this, "please enter the title", Toast.LENGTH_SHORT).show();
                        }
                        else if(message.isEmpty()){
                            Toast.makeText(SendMessageActivity.this, "please enter your message", Toast.LENGTH_SHORT).show();
                        }
                        //MessageData messageData = new MessageData(,,,message,,);
                        //messageData.setMessage(message);
                        break;
                }
            }
        };
        btn_send.setOnClickListener(myListener);
    }
}
