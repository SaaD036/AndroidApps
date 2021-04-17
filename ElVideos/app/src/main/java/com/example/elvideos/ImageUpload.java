package com.example.elvideos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.*;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.*;

import com.google.android.gms.tasks.*;
import com.google.firebase.database.*;
import com.google.firebase.storage.*;

public class ImageUpload extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private ImageView uploadedImage;
    private Button uploadButton, chooseButton, showButton;
    private EditText imageNameText;
    private Uri imageURI;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        initComp();

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();;
            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageUpload.this, ShowImage.class);
                startActivity(intent);
            }
        });
    }

    ///Initializing all the variables
    private void initComp(){
        uploadedImage = findViewById(R.id.uploaded_imageView);
        uploadButton = findViewById(R.id.upload_imageButton);
        chooseButton = findViewById(R.id.chooseImageButton);
        showButton = findViewById(R.id.showImageButton);
        imageNameText = findViewById(R.id.imageNameText);

        databaseReference = FirebaseDatabase.getInstance().getReference("Image_info");
        storageReference = FirebaseStorage.getInstance().getReference("Image");
    }

    ///Choose image and load in the app
    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);
    }

    ///Getting the video from device storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && data!=null){
            imageURI = data.getData();
            uploadedImage.setImageURI(imageURI);
        }
    }

    //Get the video extension
    private String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //Uploading image to database
    private void uploadImage(){
        if (imageNameText.getText().toString().trim().isEmpty()){
            Toast.makeText(ImageUpload.this, "Enter an image name", Toast.LENGTH_LONG).show();
        }
        else if(imageURI == null){
            Toast.makeText(ImageUpload.this, "No video to upload", Toast.LENGTH_LONG).show();
        }

        else{
            final StorageReference tmp = storageReference.child(imageNameText.getText().toString().trim()+"."+getExtension(imageURI));
            UploadTask uploadTask = tmp.putFile(imageURI);

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
                        Toast.makeText(ImageUpload.this, "Image uploaded", Toast.LENGTH_SHORT).show();

                        VideoInfo videoInfo = new VideoInfo();
                        videoInfo.setVideoName(imageNameText.getText().toString().trim());
                        videoInfo.setVideoURL(downloadUri.toString());
                        databaseReference.push().setValue(videoInfo);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ImageUpload.this, ""+e, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
