package com.example.irrigationhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class Hompage extends AppCompatActivity {
    private Button fillTank, back;
    private CircularProgressBar circularProgressBar;
    private TextView humidityText, wateringText;
    private LinearLayout main, humidity;
    private int filled=100;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hompage);

        initComp();
        tankCondition();

        fillTank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filled = 200;
                circularProgressBar.setProgressWithAnimation(filled, (long) 2000);
            }
        });
        humidityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.setVisibility(View.GONE);
                humidity.setVisibility(View.VISIBLE);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                humidity.setVisibility(View.GONE);
                main.setVisibility(View.VISIBLE);
            }
        });
        wateringText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.startLoadingProgressBar();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressBar.dismissProgressBar();
                        filled -= 30;
                        circularProgressBar.setProgressWithAnimation(filled, (long) 2000);
                    }
                }, 4000);
            }
        });
    }

    private void initComp(){
        fillTank = findViewById(R.id.tankFill_button);
        back = findViewById(R.id.home_backButton);
        circularProgressBar = findViewById(R.id.yourCircularProgressbar);

        humidityText = findViewById(R.id.home_humidityTextview);
        wateringText = findViewById(R.id.home_wateringTextview);

        main = findViewById(R.id.home_mainLayout);
        humidity = findViewById(R.id.home_humidityLayout);

        progressBar = new ProgressBar(Hompage.this);
    }
    private void tankCondition(){
        circularProgressBar.setProgressWithAnimation(filled, (long) 2000);
        circularProgressBar.setProgressMax(200f);

// Set ProgressBar Color
        circularProgressBar.setProgressBarColor(R.color.violet);
// or with gradient
        circularProgressBar.setProgressBarColorStart(Color.GRAY);
        circularProgressBar.setProgressBarColorEnd(Color.RED);
        circularProgressBar.setProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);

// Set background ProgressBar Color
        circularProgressBar.setBackgroundProgressBarColor(Color.GRAY);
// or with gradient
        circularProgressBar.setBackgroundProgressBarColorStart(Color.WHITE);
        circularProgressBar.setBackgroundProgressBarColorEnd(Color.RED);
        circularProgressBar.setBackgroundProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);

// Set Width
        circularProgressBar.setProgressBarWidth(7f); // in DP
        circularProgressBar.setBackgroundProgressBarWidth(3f); // in DP

// Other
        circularProgressBar.setRoundBorder(true);
        circularProgressBar.setStartAngle(180f);
        circularProgressBar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);
    }
    @Override
    public void onBackPressed() {
        this.finish();
    }
}