package com.example.lenovo.map;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    Button btn_create;
    EditText edt_username;
    EditText edt_password;
    EditText edt_re_password;
    Toolbar toolbar;
    MyHandler handler;
    RegisterThread registerThread;
    Thread thread;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_create = (Button) this.findViewById(R.id.btn_create);
        edt_username = (EditText) this.findViewById(R.id.edt_username);
        edt_password = (EditText) this.findViewById(R.id.edt_password);
        edt_re_password = (EditText) this.findViewById(R.id.edt_reenter_password);
        handler = new MyHandler();
        toolbar = (Toolbar) this.findViewById(R.id.tool_bar);
        imageView = (ImageView) this.findViewById(R.id.register_map);
        imageView.setImageDrawable(getDrawable(R.drawable.mapimage));

        View.OnClickListener toolbar_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edt_username.getText().toString();
                String password = edt_password.getText().toString();
                String re_password = edt_re_password.getText().toString();
                if(username.isEmpty()){
                    edt_username.setError("please fill your email");
                }
                else if(password.isEmpty()){
                    edt_password.setError("please set your password");
                }
                else if(re_password.isEmpty()){
                    edt_re_password.setError("please reenter your password");
                }
                else if(!password.equals(re_password)){
                    Toast.makeText(RegisterActivity.this,"incorrect password",Toast.LENGTH_SHORT).show();
                }
                else{
                    registerThread = new RegisterThread(username,password);
                    thread = new Thread(registerThread);
                    thread.start();
                }
            }
        };
        TextWatcher textWatcher = new TextWatcher() {
            private CharSequence re_pwd;
            private String pwd;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                pwd = edt_password.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                re_pwd = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!re_pwd.equals(pwd)){

                }
            }
        };
        toolbar.setNavigationOnClickListener(toolbar_listener);
        btn_create.setOnClickListener(myListener);
        edt_re_password.addTextChangedListener(textWatcher);
    }

    class MyHandler extends android.os.Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            if(msgId==1){
                String data = msg.getData().getString("data");
                if(data.equals("s")){
                    Toast.makeText(RegisterActivity.this, "create successfully", Toast.LENGTH_SHORT).show();
                    RegisterActivity.this.finish();
                }
            }
        }
    }

    class RegisterThread implements Runnable{
        private String username;
        private String password;
        public RegisterThread(String username,String password){
            this.username = username;
            this.password = password;
        }
        public void run(){
            final String data = NetUtils.register(username,password);
            System.out.println("============="+data+"=================\n");
            Message message = new Message();
            message.what = 1;
            Bundle bundle = new Bundle();
            bundle.putString("data", data);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}
