package com.example.elvideos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VideoPage extends AppCompatActivity {
    private LinearLayout free, premium;
    private Button go;
    private EditText pass;
    private DatabaseReference databaseReference;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_page);

        initComp();

        free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go.setVisibility(View.INVISIBLE);
                go.setEnabled(false);
                pass.setVisibility(View.INVISIBLE);

                Intent intent = new Intent(VideoPage.this, ShowVideos.class);
                intent.putExtra("type", "free");
                startActivity(intent);
            }
        });
        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go.setVisibility(View.VISIBLE);
                go.setEnabled(true);
                pass.setVisibility(View.VISIBLE);
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        password = dataSnapshot.getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                if (!pass.getText().toString().equals(password)){
                    flag = false;
                    Toast.makeText(VideoPage.this, "Wrong password", Toast.LENGTH_LONG).show();
                }

                if (flag){
                    pass.setText("");
                    Intent intent = new Intent(VideoPage.this, ShowVideos.class);
                    intent.putExtra("type", "paid");
                    startActivity(intent);
                }
            }
        });
    }

    private void initComp(){
        free = findViewById(R.id.videoPage_freeLayout);
        premium = findViewById(R.id.videoPage_premiumLayout);
        go = findViewById(R.id.videoPage_goButton);
        pass = findViewById(R.id.videoPage_passwordText);
        databaseReference = FirebaseDatabase.getInstance().getReference("PremiumPassword");
    }
}
