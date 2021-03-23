package com.example.coachingmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowStudent extends AppCompatActivity {
    private ListView listView;
    private DatabaseReference databaseReference;
    private List<Student> studentList;
    private ShoeAllStudent shoeAllStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);

        initComp();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student student = studentList.get(position);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                        sendSMS(student);
                    }
                    else{
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                    }
                }
            }
        });
    }

    private void initComp(){
        listView = findViewById(R.id.showAllStudnnt_listview);
        databaseReference = FirebaseDatabase.getInstance().getReference("Student");
        studentList = new ArrayList<>();
        shoeAllStudent = new ShoeAllStudent(ShowStudent.this, studentList);
    }

    private void sendSMS(Student student) {
        String SMS = "Hi, " + student.getName() + ", batch: " + student.getBatch() + ", tuition fee is " + student.getDue() + " for previous month. Please pay the fees";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(student.getPhone(), null, SMS, null, null);
            Toast.makeText(ShowStudent.this, "Message sent succesfully to "+student.getName(), Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(ShowStudent.this, "Error "+e+" occurred", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Student student = snapshot.getValue(Student.class);
                    studentList.add(student);
                }

                listView.setAdapter(shoeAllStudent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
