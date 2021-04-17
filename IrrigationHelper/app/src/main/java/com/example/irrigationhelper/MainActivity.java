package com.example.irrigationhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComp();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                getStarted.setEnabled(true);
            }
        }, 4000);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Hompage.class);
                intent.putExtra("back", true);
                startActivity(intent);
            }
        });
    }

    private void initComp(){
        getStarted = findViewById(R.id.main_getStartedButton);
    }
}