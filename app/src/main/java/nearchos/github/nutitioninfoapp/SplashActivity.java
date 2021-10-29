package nearchos.github.nutitioninfoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity
{
    private static boolean splashLoaded = false;
   LottieAnimationView animationView;
   TextView splash_title, splash_txt;
   ImageView backgroundSplash;
   View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!splashLoaded) {
            setContentView(R.layout.splash_screen);

            splash_title = findViewById(R.id.title_splash);
            splash_txt = findViewById(R.id.txt_splash);
            animationView = findViewById(R.id.animationView);
            backgroundSplash = findViewById(R.id.backgroudSplash);
            view = findViewById(R.id.view);
            animate();

            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, secondsDelayed * 4500);

            splashLoaded = true;
        }
        else {
            Intent goToMainActivity = new Intent(SplashActivity.this, MainActivity.class);
            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToMainActivity);
            finish();
        }
    }

    void animate(){

        view.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
        animationView.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
        splash_title.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
        splash_txt.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
    }

}
