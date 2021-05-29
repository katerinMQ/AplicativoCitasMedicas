package tech.abralica.clinicalaluzapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import tech.abralica.clinicalaluzapp.ui.start.LoginFragment;

public class SplashActivity extends AppCompatActivity {

    ProgressBar splashProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash );

        splashProgress=findViewById(R.id.splashProgress);
        ObjectAnimator.ofInt(splashProgress,"progress",100).setDuration(5000).start();
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this, LoginFragment.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }
}