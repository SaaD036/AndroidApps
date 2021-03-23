package com.example.coachingmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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

public class MainActivity extends AppCompatActivity {
    private LinearLayout addStudent, createBatch, conPay, studentList, createBill;
    private DatabaseReference databaseReference;
    private List<Student> studenList;
    private List<String> keyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComp();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Student student = snapshot.getValue(Student.class);
                    studenList.add(student);
                    keyList.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddStudent.class);
                startActivity(intent);
            }
        });
        createBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateBatch.class);
                startActivity(intent);
            }
        });
        conPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConfirmPayment.class);
                startActivity(intent);
            }
        });
        studentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowStudent.class);
                startActivity(intent);
            }
        });

        createBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0; i<studenList.size(); i++){
                    Student student = studenList.get(i);

                    int due = student.getDue() + student.getFee();

                    databaseReference.child(keyList.get(i)).child("due").setValue(due);
                }

                Toast.makeText(MainActivity.this, "Salary has been added to all studen't account for "+
                        new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initComp(){
        addStudent = findViewById(R.id.main_addStudent_layout);
        createBatch = findViewById(R.id.main_createBatch_layout);
        conPay = findViewById(R.id.main_confirmPayment_layout);
        studentList = findViewById(R.id.main_studentList_layout);
        createBill = findViewById(R.id.main_createBillLayout);

        databaseReference = FirebaseDatabase.getInstance().getReference("Student");
        studenList = new ArrayList<>();
        keyList = new ArrayList<>();
    }
}
