package com.rotimijohnson.notetakingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.rotimijohnson.notetakingapp.view.auth.LoginActivity;
import com.rotimijohnson.notetakingapp.view.walkthrough.WalkthroughActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int TIMER = 3000;
    SharedPreferences walkthrough;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                walkthrough = getSharedPreferences("walkthrough", MODE_PRIVATE);
                boolean isFirstTime = walkthrough.getBoolean("firstTime", true);

                if (isFirstTime){
                    SharedPreferences.Editor editor = walkthrough.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    Intent intent = new Intent(SplashActivity.this, WalkthroughActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, TIMER);
    }
}