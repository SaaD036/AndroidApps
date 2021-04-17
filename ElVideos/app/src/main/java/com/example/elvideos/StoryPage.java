package com.example.elvideos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class StoryPage extends AppCompatActivity {
    private TextView story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_page);

        story = findViewById(R.id.storyPage_storyText);
        String s = getIntent().getStringExtra("s");
        story.setText(s);
    }
}
