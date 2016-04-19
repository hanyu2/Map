package com.example.lenovo.map;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    Button btn_register;
    EditText edt_email;
    EditText edt_password;
    MyHandler handler;
    LoginThread loginThread;
    Thread thread;
    ImageView imageView;
    String email;
    String password;
    CheckBox checkBox;
    boolean remember;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = (Button) this.findViewById(R.id.btn_login);
        btn_register = (Button) this.findViewById(R.id.btn_register);
        edt_email = (EditText) this.findViewById(R.id.edt_login_email);
        edt_password = (EditText) this.findViewById(R.id.edt_login_password);
        imageView = (ImageView) this.findViewById(R.id.login__map);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.mapimage));
        checkBox = (CheckBox) this.findViewById(R.id.chbox_login_remember);
        handler = new MyHandler();
        settings = getSharedPreferences("setting",0);
        remember = settings.getBoolean("remember",false);

        if(remember){
            settings = getSharedPreferences("setting",0);
            email = settings.getString("email","");
            edt_email.setText(email);
        }
        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_login:
                        email = edt_email.getText().toString();
                        password = edt_password.getText().toString();
                        if(email.isEmpty()){
                            edt_email.setError("please enter your email");
                        }
                        else if(!checkEmail(email)){
                            edt_email.setError("invalid email");
                        }
                        else if(password.isEmpty()){
                            edt_password.setError("please enter your password");
                        }
                        else{
                            loginThread = new LoginThread(email,password);
                            thread = new Thread(loginThread);
                            thread.start();
                        }
                        break;
                    case R.id.btn_register:
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, RegisterActivity.class);
                        LoginActivity.this.startActivity(intent);
                        break;
                }
            }
        };
        btn_login.setOnClickListener(myListener);
        btn_register.setOnClickListener(myListener);
    }

    public boolean checkEmail(String email) {
        String format = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        if (email.matches(format)) {
            return true;
        } else {
            return false;
        }
    }

    class MyHandler extends android.os.Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            if(msgId==1){
                String data = msg.getData().getString("data");
                if(data.equals("nouser")){
                    Toast.makeText(LoginActivity.this,"user not exist",Toast.LENGTH_SHORT).show();
                }
                else if(data.equals("wpwd")){
                    Toast.makeText(LoginActivity.this,"incorrect password",Toast.LENGTH_SHORT).show();
                }
                else if(data.equals("s")){
                    Toast.makeText(LoginActivity.this,"login successfully",Toast.LENGTH_SHORT).show();
                    try {
                        remember = (checkBox.isChecked()) ? true : false;
                        settings = getSharedPreferences("setting",0);
                        editor = settings.edit();
                        editor.putBoolean("remember",remember);
                        editor.putString("email",email);
                        editor.commit();
                        Thread.sleep(1);
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MapsActivity.class);
                        LoginActivity.this.startActivity(intent);
                    } catch (InterruptedException e) {
                        Toast.makeText(LoginActivity.this,"something error!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    class LoginThread implements Runnable{
        private String username;
        private String password;
        public LoginThread(String username,String password){
            this.username = username;
            this.password = password;
        }
        public void run(){
            final String data = NetUtils.logIn(username,password);
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
