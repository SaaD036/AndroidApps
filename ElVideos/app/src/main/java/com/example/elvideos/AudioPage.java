package com.example.elvideos;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class AudioPage extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;
    private Button button;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_page);

        String url = getIntent().getStringExtra("url");

        imageView = findViewById(R.id.audioPage_imageView);
        textView = findViewById(R.id.audioPage_textView);
        button = findViewById(R.id.audioPage_Button);
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(AudioPage.this, Uri.parse(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    button.setText("Play");
                    textView.setText("Pause");
                    imageView.setImageResource(R.mipmap.audio_pausing);
                } else {
                    mediaPlayer.start();
                    button.setText("Pause");
                    textView.setText("Playing...");
                    imageView.setImageResource(R.mipmap.audio_playing);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mediaPlayer.stop();
    }
}
