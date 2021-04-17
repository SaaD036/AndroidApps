package com.example.abcacademy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class ShowStudent extends AppCompatActivity {
    private ListView listView;
    private DatabaseReference databaseReference, enable_databaseReference;
    private List<Student> studentList;
    private Show_All_Student show_all_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);

        initComp();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                boolean flag = false;
//                enable_databaseReference.setValue(flag);
            }
        });
    }

    private void initComp(){
        listView = findViewById(R.id.showAllStudnnt_listview);
        databaseReference = FirebaseDatabase.getInstance("https://abcacademy-c4ae3-default-rtdb.firebaseio.com/").getReference("Student");
        enable_databaseReference = FirebaseDatabase.getInstance("https://abcacademy-c4ae3-default-rtdb.firebaseio.com/").getReference("Enable");
        studentList = new ArrayList<>();
        show_all_student = new Show_All_Student(ShowStudent.this, false, studentList);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Student tmp = snapshot.getValue(Student.class);
                    studentList.add(tmp);
                }

                listView.setAdapter(show_all_student);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
