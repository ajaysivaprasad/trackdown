package com.example.jithin.trackdown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent in = new Intent(Splash.this, MainActivity.class);;
                startActivity(in);
                finish();

            }
        }, 1000);

    }
}