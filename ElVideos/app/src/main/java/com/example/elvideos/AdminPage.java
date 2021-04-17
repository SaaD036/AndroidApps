package com.example.elvideos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class AdminPage extends AppCompatActivity {
    private LinearLayout video, image, text, audio, quiz, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        initComp();

        //Activate all the button
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPage.this, VideoUpload.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPage.this, ImageUpload.class);
                startActivity(intent);
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPage.this, TextUpload.class);
                startActivity(intent);
            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPage.this, AudioUpload.class);
                startActivity(intent);
            }
        });
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPage.this, QuestionUpload.class);
                startActivity(intent);
            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPage.this, PremiumPassword.class);
                startActivity(intent);
            }
        });
    }

    //Initializing all the component
    private void initComp(){
        video = findViewById(R.id.adminPage_videoLayout);
        image = findViewById(R.id.adminPage_imageLayout);
        text = findViewById(R.id.adminPage_textLayout);
        audio = findViewById(R.id.adminPage_audioLayout);
        quiz = findViewById(R.id.adminPage_quizLayout);
        pass = findViewById(R.id.adminPage_passwordLayout);
    }
}
