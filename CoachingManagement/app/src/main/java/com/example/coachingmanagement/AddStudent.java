package com.example.coachingmanagement;

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

public class AddStudent extends AppCompatActivity {
    private EditText nameText, phoneText, batchText, subjectText, feesText;
    private Button addStudent;
    private int fees, totalPayment;
    private String name, phone, batch, subject, fee, key;
    private DatabaseReference databaseReference, batch_databaseReference;
    private List<Student> studentList;
    private List<Batch> batchList;
    private List<String> keyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        initComp();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Student student = snapshot.getValue(Student.class);
                    studentList.add(student);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        batch_databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Batch tmp = snapshot.getValue(Batch.class);
                    batchList.add(tmp);
                    keyList.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameText.getText().toString().trim();
                phone = phoneText.getText().toString().trim();
                batch = batchText.getText().toString().trim();
                subject = subjectText.getText().toString().trim();
                fee = feesText.getText().toString().trim();
                boolean flag = true;

                if(name.isEmpty() || phone.isEmpty() || batch.isEmpty() || subject.isEmpty() || fee.isEmpty()){
                    flag = false;
                    Toast.makeText(AddStudent.this, "You can't leave any field empty", Toast.LENGTH_LONG).show();
                }
                if(flag){
                    for(int i=0; i<studentList.size(); i++){
                        Student student = studentList.get(i);

                        if(student.getPhone().equals(phone)){
                            flag = false;
                            break;
                        }
                    }
                    if (!flag){
                        Toast.makeText(AddStudent.this, "Two student can't have same phone number", Toast.LENGTH_LONG).show();
                    }
                }
                if (flag){
                    flag = false;
                    for (int i=0; i<batchList.size(); i++){
                        Batch tmp = batchList.get(i);

                        if(batch.equals(tmp.getName())){
                            totalPayment = tmp.getTotalStudent();
                            key = keyList.get(i);
                            flag = true;
                            break;
                        }
                    }

                    if (!flag){
                        Toast.makeText(AddStudent.this, "There is no batch", Toast.LENGTH_LONG).show();
                    }
                }
                if (flag){
                    Student student = new Student();

                    student.setName(name);
                    student.setPhone(phone);
                    student.setBatch(batch);
                    student.setSubject(subject);
                    student.setFee(Integer.parseInt(fee));
                    student.setAdmissionDate(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                    student.setTotalPayment(0);
                    student.setDue(Integer.parseInt(fee));

                    totalPayment = totalPayment + 1;
                    Batch tmp = new Batch();

                    tmp.setName(batch);
                    tmp.setTotalStudent(totalPayment);

                    databaseReference.push().setValue(student);
                    batch_databaseReference.child(key).setValue(tmp);
                    Toast.makeText(AddStudent.this, "Information updated successfully", Toast.LENGTH_LONG).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                            sendSMS(student);
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
        nameText = findViewById(R.id.addStudent_nameEdittext);
        phoneText = findViewById(R.id.addStudent_phoneEdittext);
        batchText = findViewById(R.id.addStudent_batchEdittext);
        subjectText = findViewById(R.id.addStudent_subjectEdittext);
        feesText = findViewById(R.id.addStudent_feesEdittext);
        addStudent = findViewById(R.id.addStudent_addStudentButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("Student");
        batch_databaseReference = FirebaseDatabase.getInstance().getReference("Batch");
        studentList = new ArrayList<>();
        batchList = new ArrayList<>();
        keyList = new ArrayList<>();
    }

    private void sendSMS(Student student) {
        String SMS = student.getName() + ", you are registered successfully with coaching name on "+
                student.getAdmissionDate();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(student.getPhone(), null, SMS, null, null);
        }
        catch (Exception e){
            Toast.makeText(AddStudent.this, "Error "+e+" occurred", Toast.LENGTH_LONG).show();
        }
    }
}
