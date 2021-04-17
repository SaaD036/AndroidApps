package com.example.elvideos;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShowAllVideo extends RecyclerView.ViewHolder {
    private TextView textView;
    private VideoView playerView;
    private MediaController mediaController;

    public ShowAllVideo(@NonNull View itemView) {
        super(itemView);
    }

    public void setVideo(Context context, /*Application application,*/ String videoName, String videoURL){
        textView = itemView.findViewById(R.id.videoItem_videoNameText);
        playerView = itemView.findViewById(R.id.videoItem_PlayerView);
        mediaController = new MediaController(context);
        playerView.setMediaController(mediaController);
        textView.setText(videoName);

        try{
            playerView.setVideoURI(Uri.parse(videoURL));
            playerView.pause();
        }
        catch (Exception e){

        }
    }
}
