package com.example.abcacademy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Show_Student_By_Batch extends AppCompatActivity {
    private TextView batchName, due;
    private EditText message;
    private Button send;
    private ListView listView;
    private DatabaseReference databaseReference;
    private List<Student> studentList, studentList_forSMS;
    private Show_All_Student show_all_student;
    private String batch;
    int totalDue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__student__by__batch);

        batch = getIntent().getStringExtra("batch");
        totalDue = 0;

        initComp();

        batchName.setText(batch);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Student tmp = snapshot.getValue(Student.class);

                    if(tmp.getBatch().equals(batch)){
                        studentList_forSMS.add(tmp);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = message.getText().toString().trim();

                for (int i=0; i<studentList_forSMS.size(); i++){
                    Student student = studentList_forSMS.get(i);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                            if(tmp.isEmpty()){
                                if(student.getDue() > 0) {
                                    sendSMS(student);
                                }
                            }
                            else{
                                sendSMS(student, tmp);
                            }
                        }
                        else{
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                        }
                    }
                }
            }
        });
    }

    private void initComp(){
        batchName = findViewById(R.id.showStudentBatch_batchNameTextview);
        message = findViewById(R.id.showStudentBatch_messageEdittext);
        due = findViewById(R.id.showStudentBatch_dueTextview);
        send = findViewById(R.id.showStudentBatch_sendButton);
        listView = findViewById(R.id.showStudentBatch_listView);

        databaseReference = FirebaseDatabase.getInstance("https://abcacademy-c4ae3-default-rtdb.firebaseio.com/").getReference("Student");
        studentList = new ArrayList<>();
        studentList_forSMS = new ArrayList<>();
        show_all_student = new Show_All_Student(Show_Student_By_Batch.this,true, studentList);
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

                    if(tmp.getBatch().equals(batch)){
                        totalDue = totalDue + tmp.getDue();
                        studentList.add(tmp);
                    }
                }

                listView.setAdapter(show_all_student);
                due.setText("Total due : "+totalDue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void sendSMS(Student student) {
        String SMS = "Dear, " + student.getName() + ", Subject: " + student.getSubject() + ", your monthly fee tk " +
                student.getDue() + " unpaid on previous month. Please pay your monthly fee";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(student.getPhone(), null, SMS, null, null);
            Toast.makeText(Show_Student_By_Batch.this, "Message sent succesfully to "+student.getName(), Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(Show_Student_By_Batch.this, "Error "+e+" occurred", Toast.LENGTH_LONG).show();
        }
    }
    private void sendSMS(Student student, String SMS) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(student.getPhone(), null, SMS, null, null);
            Toast.makeText(Show_Student_By_Batch.this, "Message sent succesfully to "+student.getName(), Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(Show_Student_By_Batch.this, "Error "+e+" occurred", Toast.LENGTH_LONG).show();
        }
    }
}
