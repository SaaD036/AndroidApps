package com.example.elvideos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ShowAudio extends AppCompatActivity {
    private ListView listView;
    private DatabaseReference databaseReference;
    private List<AudioInfo> audioInfoList;
    private ShowAllAudio showAllAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_audio);

        listView = findViewById(R.id.showAudio_listView);
        databaseReference = FirebaseDatabase.getInstance().getReference("AudioInfo");
        audioInfoList = new ArrayList<>();
        showAllAudio = new ShowAllAudio(ShowAudio.this, audioInfoList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowAudio.this, AudioPage.class);
                intent.putExtra("url", audioInfoList.get(position).getUrl());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                audioInfoList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    AudioInfo audioInfo = snapshot.getValue(AudioInfo.class);
                    audioInfoList.add(audioInfo);
                }

                listView.setAdapter(showAllAudio);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
