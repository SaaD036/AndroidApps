package com.example.elvideos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.*;

public class ShowVideos extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_videos);
        type = getIntent().getStringExtra("type");

        initComp();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<VideoInfo> options = new FirebaseRecyclerOptions.Builder<VideoInfo>().
                setQuery(databaseReference, VideoInfo.class).build();

        FirebaseRecyclerAdapter<VideoInfo, ShowAllVideo> firebaseRecyclerAdapter =  new FirebaseRecyclerAdapter<VideoInfo, ShowAllVideo>(options){
            @NonNull
            @Override
            public ShowAllVideo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);

                return new ShowAllVideo(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ShowAllVideo showAllVideo, int i, @NonNull VideoInfo videoInfo) {
                showAllVideo.setVideo(ShowVideos.this, videoInfo.getVideoName(), videoInfo.getVideoURL());
            }
        };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void initComp(){
        recyclerView = findViewById(R.id.showVideo_RecyclerView);

        if(type.equals("paid")){
            databaseReference = FirebaseDatabase.getInstance().getReference("Paid_video_info");
        }
        else{
            databaseReference = FirebaseDatabase.getInstance().getReference("Video_info");
        }
    }
}
