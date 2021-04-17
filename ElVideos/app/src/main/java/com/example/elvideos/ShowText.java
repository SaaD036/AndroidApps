package com.example.elvideos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowText extends AppCompatActivity {
    private ListView listView;
    private DatabaseReference databaseReference;
    private List<TextInfo> textInfoList;
    private ShowAllText showAllText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);

        listView = findViewById(R.id.showText_listView);
        databaseReference = FirebaseDatabase.getInstance().getReference("TextInfo");
        textInfoList = new ArrayList<>();
        showAllText = new ShowAllText(ShowText.this, textInfoList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowText.this, StoryPage.class);
                intent.putExtra("n", textInfoList.get(position).getName());
                intent.putExtra("s", textInfoList.get(position).getStory());
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
                textInfoList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TextInfo textInfo = snapshot.getValue(TextInfo.class);
                    textInfoList.add(textInfo);
                }

                listView.setAdapter(showAllText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
