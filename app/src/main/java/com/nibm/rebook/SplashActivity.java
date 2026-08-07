package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private TextView txtTagline;
    private final CharSequence sloganText = "Read. Repeat. ReBook.";
    private int index = 0;
    private final long delay = 80; // Typewriter speed

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        View cardLogo = findViewById(R.id.cardLogo);
        View txtTitle = findViewById(R.id.txtTitle);
        txtTagline = findViewById(R.id.txtTagline);

        // Apply bouncing animation to the logo card container
        try {
            Animation bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
            cardLogo.startAnimation(bounceAnimation);
        } catch (Exception e) {
            // Fallback if animation resource is missing
        }

        // Fade in title smoothly
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(600);
        txtTitle.startAnimation(fadeIn);

        // Start typewriter text effect for slogan
        handler.postDelayed(typewriterRunnable, 400);
    }

    private final Runnable typewriterRunnable = new Runnable() {
        @Override
        public void run() {
            if (index <= sloganText.length()) {
                txtTagline.setText(sloganText.subSequence(0, index));
                index++;
                handler.postDelayed(this, delay);
            } else {
                // Wait 1.2 seconds after finishing text typing, then open the main dashboard
                handler.postDelayed(() -> {
                    Intent intent = new Intent(SplashActivity.this, SellerDashboardActivity.class);
                    startActivity(intent);
                    finish();
                }, 1200);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}