package com.example.abcacademy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private DatabaseReference databaseReference, enable_databaseReference, payDate_databaseReference;
    private List<Student> studentListList;
    private List<String> keyList;
    private String prevDate;
    boolean runORnot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComp();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Student tmp = snapshot.getValue(Student.class);
                    studentListList.add(tmp);
                    keyList.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        payDate_databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prevDate = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        enable_databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                runORnot = dataSnapshot.getValue(boolean.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (runORnot){
                    Intent intent = new Intent(MainActivity.this, AddStudent.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Hey man, you haven't paid my money", Toast.LENGTH_LONG).show();
                }
            }
        });
        createBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (runORnot){
                    Intent intent = new Intent(MainActivity.this, CreateBatch.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Hey man, you haven't paid my money", Toast.LENGTH_LONG).show();
                }
            }
        });
        conPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(runORnot){
                    Intent intent = new Intent(MainActivity.this, ConfirmPayment.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Hey man, you haven't paid my money", Toast.LENGTH_LONG).show();
                }
            }
        });
        createBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (runORnot){
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    //payDate_databaseReference.setValue(currentDate);

                    if (prevDate.isEmpty() || prevDate.equals(null)){
                        for (int i=0; i<studentListList.size(); i++){
                            Student tmp = studentListList.get(i);

                            if (tmp.isAvailable()){
                                int due = tmp.getDue() + tmp.getFee();
                                databaseReference.child(keyList.get(i)).child("due").setValue(due);
                            }
                        }

                        payDate_databaseReference.setValue(currentDate);
                        Toast.makeText(MainActivity.this, "Bill successfully added", Toast.LENGTH_LONG).show();
                    }
                    else{
                        String s1 = prevDate.substring(0, 7), s2 = currentDate.substring(0, 7);

                        if (s1.equals(s2)){
                            Toast.makeText(MainActivity.this, "Bill for "+s2+" has already been added by a user", Toast.LENGTH_LONG).show();
                        }
                        else{
                            for (int i=0; i<studentListList.size(); i++){
                                Student tmp = studentListList.get(i);

                                if (tmp.isAvailable()){
                                    int due = tmp.getDue() + tmp.getFee();
                                    databaseReference.child(keyList.get(i)).child("due").setValue(due);
                                }
                            }

                            payDate_databaseReference.setValue(currentDate);
                            Toast.makeText(MainActivity.this, "Bill successfully added", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Hey man, you haven't paid my money", Toast.LENGTH_LONG).show();
                }
            }
        });
        studentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowStudent.class);

                startActivity(intent);
            }
        });
    }

    private void initComp(){
        addStudent = findViewById(R.id.main_addStudent_layout);
        createBatch = findViewById(R.id.main_createBatch_layout);
        conPay = findViewById(R.id.main_confirmPayment_layout);
        studentList = findViewById(R.id.main_studentList_layout);
        createBill = findViewById(R.id.main_createBillLayout);

        databaseReference = FirebaseDatabase.getInstance("https://abcacademy-c4ae3-default-rtdb.firebaseio.com/").getReference("Student");
        enable_databaseReference = FirebaseDatabase.getInstance("https://abcacademy-c4ae3-default-rtdb.firebaseio.com/").getReference("Enable");
        payDate_databaseReference = FirebaseDatabase.getInstance("https://abcacademy-c4ae3-default-rtdb.firebaseio.com/").getReference("Payment_Date");

        studentListList = new ArrayList<>();
        keyList = new ArrayList<>();

        prevDate = "";
        runORnot = true;
    }
}
