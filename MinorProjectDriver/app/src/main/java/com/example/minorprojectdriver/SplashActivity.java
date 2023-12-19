package com.example.minorprojectdriver;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import android.app.Activity;

import com.airbnb.lottie.LottieAnimationView;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity {

    TextView appname;
    LottieAnimationView lottie;
    LottieAnimationView lott2;
    ImageView bus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appname=findViewById(R.id.app_name);
        // lottie=findViewById(R.id.lottie);
        lott2=findViewById(R.id.lottie2);
        //bus = findViewById(R.id.splash_bus);

        appname.animate().translationY(-700).setDuration(1000).setStartDelay(0);
        // lottie.animate().translationX(2000).setDuration(2000).setStartDelay(2900);
        //bus.animate().translationX(1400).setDuration(2700).setStartDelay(2900);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(intent);
            }
        },3000);
    }
}
