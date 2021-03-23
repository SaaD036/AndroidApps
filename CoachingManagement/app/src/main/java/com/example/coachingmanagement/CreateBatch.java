package com.example.coachingmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateBatch extends AppCompatActivity {
    private EditText nameText;
    private Button addBatch;
    private DatabaseReference databaseReference;
    private List<Batch> batchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_batch);

        initComp();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Batch batch = snapshot.getValue(Batch.class);
                    batchList.add(batch);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        addBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String batchName = nameText.getText().toString().trim();
                boolean flag = true;


                if (batchName.isEmpty()) {
                    flag = false;
                    Toast.makeText(CreateBatch.this, "You can't leave any field empty", Toast.LENGTH_LONG).show();
                }
                if (flag) {
                    for (int i = 0; i < batchList.size(); i++) {
                        Batch batch = batchList.get(i);

                        if (batch.getName().equals(batchName)) {
                            flag = false;
                            break;
                        }
                    }
                    if (!flag) {
                        Toast.makeText(CreateBatch.this, "Two batch can't have same name", Toast.LENGTH_LONG).show();
                    }
                }
                if (flag) {
                    Batch batch = new Batch();

                    batch.setName(batchName);

                    databaseReference.push().setValue(batch);
                    Toast.makeText(CreateBatch.this, "Batch created successfully", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initComp(){
        nameText = findViewById(R.id.createBatch_batchNameEdittext);
        addBatch = findViewById(R.id.createBatch_addBatchButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("Batch");
        batchList = new ArrayList<>();
    }
}
