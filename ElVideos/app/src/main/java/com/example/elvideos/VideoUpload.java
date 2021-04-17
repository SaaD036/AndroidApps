package com.example.elvideos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class VideoUpload extends AppCompatActivity {
    private static final int PICK_VIDEO = 1;
    private VideoView uploadedVideo;
    private Button uploadButton, chooseButton, showButton, statusButton;
    private EditText videoNameText;
    private Uri videoURI;
    private MediaController mediaController;
    private DatabaseReference databaseReference, paid_databaseReference;
    private StorageReference storageReference;
    private boolean videoType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);

        initComp();

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadVideo();
            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoUpload.this, ShowVideos.class);
                startActivity(intent);
            }
        });
        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoType){
                    statusButton.setText("This is paid video.\nClick here to make it free");
                    videoType = true;
                }
                else{
                    statusButton.setText("This is paid video.\nClick here to make it free");
                    videoType = true;
                }
            }
        });
    }

    //Initialize all the element
    private void initComp(){
        uploadedVideo = findViewById(R.id.uploaded_videoView);
        uploadButton = findViewById(R.id.upload_videoButton);
        chooseButton = findViewById(R.id.chooseVideoButton);
        showButton = findViewById(R.id.showVideoButton);
        statusButton = findViewById(R.id.videoUpload_statusButton);

        videoNameText = findViewById(R.id.videoNameText);

        mediaController = new MediaController(this);
        uploadedVideo.setMediaController(mediaController);
        videoType = false;
        uploadedVideo.start();

        storageReference = FirebaseStorage.getInstance().getReference("Video");
        databaseReference = FirebaseDatabase.getInstance().getReference("Video_info");
        paid_databaseReference = FirebaseDatabase.getInstance().getReference("Paid_video_info");
    }

    ///Choose video and load in the app
    private void chooseVideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO);
    }

    ///Getting the video from device storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO && data!=null){
            videoURI = data.getData();
            uploadedVideo.setVideoURI(videoURI);
        }
    }

    //Get the video extension
    private String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //Uploading video to database
    private void uploadVideo(){
        if (videoNameText.getText().toString().trim().isEmpty()){
            Toast.makeText(VideoUpload.this, "Enter a video name", Toast.LENGTH_LONG).show();
        }
        else if(videoURI == null){
            Toast.makeText(VideoUpload.this, "No video to upload", Toast.LENGTH_LONG).show();
        }

        else{
            final StorageReference tmp = storageReference.child(videoNameText.getText().toString().trim()+"."+getExtension(videoURI));
            UploadTask uploadTask = tmp.putFile(videoURI);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return tmp.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        Toast.makeText( VideoUpload.this, "Video uploaded", Toast.LENGTH_SHORT).show();

                        VideoInfo videoInfo = new VideoInfo();
                        videoInfo.setVideoName(videoNameText.getText().toString().trim());
                        videoInfo.setVideoURL(downloadUri.toString());
                        if(videoType){
                            paid_databaseReference.push().setValue(videoInfo);
                        }
                        else{
                            databaseReference.push().setValue(videoInfo);
                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(VideoUpload.this, ""+e, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    ///Searching and showing video
    private void showVIdeo() throws IOException {

        if(videoNameText.getText().toString().trim().equals("")){
            Toast.makeText( VideoUpload.this, "Enter a video name", Toast.LENGTH_LONG).show();
        }
        else{

        }

    }
}
