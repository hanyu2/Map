package com.example.lenovo.map;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class HomePageActivity extends Activity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        imageView = (ImageView) this.findViewById(R.id.iv_homepage);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.homepage));

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
                startActivity(intent);
                HomePageActivity.this.finish();
            }
        }, 3000);
    }
}
