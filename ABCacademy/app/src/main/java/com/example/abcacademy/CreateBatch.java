package com.example.abcacademy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateBatch extends AppCompatActivity {
    private EditText nameText;
    private Button addBatch, deleteBatch;
    private ImageButton addImageButton;
    private ListView listView;
    private LinearLayout linearLayout;
    private DatabaseReference databaseReference, student_databaseReference;
    private List<Batch> batchList;
    private List<Student> studentList;
    private List<String> keyList_batch, keyList_student;
    private Show_All_Batch show_all_batch;
    private Batch batch;
    private String key;
    private boolean showAddBatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_batch);

        initComp();
        showAddBatch = false;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Batch tmp = snapshot.getValue(Batch.class);
                    batchList.add(tmp);
                    keyList_batch.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        student_databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Student tmp = snapshot.getValue(Student.class);
                    studentList.add(tmp);
                    keyList_student.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showAddBatch){
                    showAddBatch = false;
                    linearLayout.setVisibility(View.GONE);
                }
                else{
                    showAddBatch = true;
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        addBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString().trim().toUpperCase();
                boolean flag = true;

                if(name.isEmpty()){
                    flag = false;
                    Toast.makeText(CreateBatch.this, "Batch name must be provided", Toast.LENGTH_LONG).show();
                }
                if (flag){
                    for (int i=0; i<batchList.size(); i++){
                        Batch tmp = batchList.get(i);

                        if (tmp.getName().equals(name)){
                            flag = false;
                            break;
                        }
                    }

                    if(!flag){
                        Toast.makeText(CreateBatch.this, "No two batch can have same name", Toast.LENGTH_LONG).show();
                    }
                }
                if (flag){
                    Batch batch = new Batch();

                    batch.setName(name);
                    batch.setTotalStudent(0);

                    databaseReference.push().setValue(batch);
                    Toast.makeText(CreateBatch.this, "Batch created successfully", Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString().trim().toUpperCase();
                boolean flag = true;

                if(name.isEmpty()){
                    flag = false;
                    Toast.makeText(CreateBatch.this, "Batch name must be provided", Toast.LENGTH_LONG).show();
                }
                if (flag){
                    flag = false;

                    for (int i=0; i<batchList.size(); i++){
                        Batch tmp = batchList.get(i);

                        if (tmp.getName().equals(name)){
                            flag = true;
                            key = keyList_batch.get(i);
                            batch = tmp;
                            break;
                        }
                    }

                    if(!flag){
                        Toast.makeText(CreateBatch.this, "No batch found", Toast.LENGTH_LONG).show();
                    }
                }
                if (flag){
                    databaseReference.child(key).removeValue();

                    for (int i=0; i<studentList.size(); i++){
                        Student tmp = studentList.get(i);

                        if (tmp.getBatch().equals(name)){
                            student_databaseReference.child(keyList_student.get(i)).removeValue();
                        }
                    }

                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Batch tmp = batchList.get(position);

                Intent intent = new Intent(CreateBatch.this, Show_Student_By_Batch.class);
                intent.putExtra("batch", tmp.getName());
                startActivity(intent);
            }
        });
    }

    private void initComp(){
        nameText = findViewById(R.id.createBatch_batchNameEdittext);
        addBatch = findViewById(R.id.createBatch_addBatchButton);
        deleteBatch = findViewById(R.id.createBatch_deleteBatchButton);
        addImageButton = findViewById(R.id.createBatch_addBatchImagebutton);
        listView = findViewById(R.id.createBatch_listview);
        linearLayout = findViewById(R.id.createBatch_linearLayout);

        databaseReference = FirebaseDatabase.getInstance("https://abcacademy-c4ae3-default-rtdb.firebaseio.com/").getReference("Batch");
        student_databaseReference = FirebaseDatabase.getInstance("https://abcacademy-c4ae3-default-rtdb.firebaseio.com/").getReference("Student");
        batchList = new ArrayList<>();
        studentList = new ArrayList<>();
        keyList_batch = new ArrayList<>();
        keyList_student = new ArrayList<>();

        show_all_batch = new Show_All_Batch(CreateBatch.this, batchList);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                batchList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Batch tmp = snapshot.getValue(Batch.class);
                    batchList.add(tmp);
                }

                listView.setAdapter(show_all_batch);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
