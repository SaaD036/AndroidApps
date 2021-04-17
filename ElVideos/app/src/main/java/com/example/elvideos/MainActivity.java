package com.example.elvideos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.*;


import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{
    private LinearLayout video, image, text, audio, quiz;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComp();

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        //Activate all the button
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoPage.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowImage.class);
                startActivity(intent);
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowText.class);
                startActivity(intent);
            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowAudio.class);
                startActivity(intent);
            }
        });
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowQuestion.class);
                startActivity(intent);
            }
        });
    }

    //Initializing all the component
    private void initComp(){
        video = findViewById(R.id.main_videoLayout);
        image = findViewById(R.id.main_imageLayout);
        text = findViewById(R.id.main_textLayout);
        audio = findViewById(R.id.main_audioLayout);
        quiz = findViewById(R.id.main_quizLayout);
        bottomNavigationView = findViewById(R.id.main_bottomNavigation);
    }

    BottomNavigationView .OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.main_bottomNav_user:

                    break;
                case R.id.main_bottomNav_admin:
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                    break;
            }

            return true;
        }
    };
}