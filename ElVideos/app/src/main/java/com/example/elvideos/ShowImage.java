package com.example.elvideos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowImage extends AppCompatActivity {
    private ListView listView;
    private DatabaseReference databaseReference;
    private List<ImageInfo> imageInfoList;
    ShowAllImage showAllImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        initComp();

    }

    private  void initComp(){
        listView = findViewById(R.id.showImage_listview);
        databaseReference = FirebaseDatabase.getInstance().getReference("Image_info");
        imageInfoList = new ArrayList<>();
        showAllImage = new ShowAllImage(ShowImage.this, imageInfoList);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageInfoList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ImageInfo imageInfo = snapshot.getValue(ImageInfo.class);
                    imageInfoList.add(imageInfo);
                }

                listView.setAdapter(showAllImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
