package com.example.proje;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    private ImageView logo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        logo = (ImageView) findViewById(R.id.logo);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.transition);
        logo.startAnimation(myanim);
        final Intent i = new Intent(this, ChartScreen.class);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(3750);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}
