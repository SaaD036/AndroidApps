package com.example.elvideos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class AudioUpload extends AppCompatActivity {
    private static final int PICK_AUDIO=1;
    private Button show, choose, upload, play;
    private TextView playText;
    private EditText nameText;
    private Uri audioUri;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_upload);

        initComp();

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAudio();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAudio();;
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioUri != null){
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        play.setText("Play");
                        playText.setText("Pause");
                    }
                    else{
                        mediaPlayer.start();
                        play.setText("Pause");
                        playText.setText("Playing...");
                    }
                }
                else{
                    Toast.makeText(AudioUpload.this, "No audio is selected", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initComp(){
        show = findViewById(R.id.audioUpload_listenButton);
        choose = findViewById(R.id.audioUpload_chooseButton);
        upload = findViewById(R.id.audioUpload_uploadButton);
        nameText = findViewById(R.id.audioUpload_nameText);
        play = findViewById(R.id.audioUpload_playButton);
        playText = findViewById(R.id.audioUpload_playText);

        mediaPlayer = new MediaPlayer();

        databaseReference = FirebaseDatabase.getInstance().getReference("AudioInfo");
        storageReference = FirebaseStorage.getInstance().getReference("Audio");
    }


    ///Choose audio and load in the app
    private void chooseAudio(){
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_AUDIO);
    }

    ///Getting the audio from device storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_AUDIO && data!=null){
            audioUri = data.getData();

            try {
                mediaPlayer.setDataSource(AudioUpload.this, audioUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            play.setText("Pause");
            playText.setText("Playing...");
            //uploadedImage.setImageURI(audioUri);
        }
    }

    //Get the extension
    private String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //Uploading audio to database
    private void uploadAudio(){
        if (nameText.getText().toString().trim().isEmpty()){
            Toast.makeText(AudioUpload.this, "Enter an audio name", Toast.LENGTH_LONG).show();
        }
        else if(audioUri == null){
            Toast.makeText(AudioUpload.this, "No audio to upload", Toast.LENGTH_LONG).show();
        }

        else{
            final StorageReference tmp = storageReference.child(nameText.getText().toString().trim()+"."+getExtension(audioUri));
            UploadTask uploadTask = tmp.putFile(audioUri);

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
                        Toast.makeText(AudioUpload.this, "Image uploaded", Toast.LENGTH_SHORT).show();

                        AudioInfo audioInfo = new AudioInfo();
                        audioInfo.setName(nameText.getText().toString().trim());
                        audioInfo.setUrl(downloadUri.toString());
                        databaseReference.push().setValue(audioInfo);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AudioUpload.this, ""+e, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
