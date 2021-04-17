package com.example.abcacademy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    private EditText nameText, phoneText, batchText, subjectText, feesText, dateText;
    private Button addStudent;
    private ListView listView;
    private int totalStudent;
    private String name, phone, batch, subject, fee, key, date;
    private DatabaseReference databaseReference, batch_databaseReference;
    private List<Student> studentList;
    private List<Batch> batchList, newBatchList;
    private List<String> keyList;
    private Show_All_Batch show_all_batch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        initComp();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Student tmp = snapshot.getValue(Student.class);
                    studentList.add(tmp);
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
                batch = batchText.getText().toString().trim().toUpperCase();
                subject = subjectText.getText().toString().trim();
                fee = feesText.getText().toString().trim();
                date = dateText.getText().toString().trim();
                boolean flag = true;

                if(name.isEmpty() || phone.isEmpty() || batch.isEmpty() || subject.isEmpty() ||
                        fee.isEmpty() || date.isEmpty()){
                    flag = false;
                    Toast.makeText(AddStudent.this, "You can't leave any field empty", Toast.LENGTH_LONG).show();
                }
                if (flag){
                    for (int i=0; i<studentList.size(); i++){
                        Student tmp = studentList.get(i);

                        if(phone.equals(tmp.getPhone())){
                            flag = false;
                            break;
                        }
                    }
                    if(!flag){
                        Toast.makeText(AddStudent.this, "Two student can't have same phone number", Toast.LENGTH_LONG).show();
                    }
                }
                if(flag){
                    flag = false;

                    for (int i=0; i<batchList.size(); i++){
                        Batch tmp = batchList.get(i);

                        if (tmp.getName().equals(batch)){
                            flag = true;
                            key = keyList.get(i);
                            totalStudent = tmp.getTotalStudent()+1;
                            break;
                        }
                    }
                }
                if (flag){
                    Student student = new Student();

                    student.setName(name);
                    student.setPhone(phone);
                    student.setBatch(batch);
                    student.setSubject(subject);
                    student.setFee(Integer.parseInt(fee));
                    student.setAdmissionDate(date);
                    student.setTotalPayment(0);
                    student.setDue(Integer.parseInt(fee));
                    student.setAvailable(true);

                    databaseReference.push().setValue(student);
                    batch_databaseReference.child(key).child("totalStudent").setValue(totalStudent);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Batch tmp = newBatchList.get(position);

                batchText.setText(tmp.getName());
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
        dateText = findViewById(R.id.addStudent_dateEdittext);
        listView = findViewById(R.id.addStudent_listview);

        databaseReference = FirebaseDatabase.getInstance("https://abcacademy-c4ae3-default-rtdb.firebaseio.com/").getReference("Student");
        batch_databaseReference = FirebaseDatabase.getInstance("https://abcacademy-c4ae3-default-rtdb.firebaseio.com/").getReference("Batch");
        studentList = new ArrayList<>();
        batchList = new ArrayList<>();
        newBatchList = new ArrayList<>();
        keyList = new ArrayList<>();

        show_all_batch = new Show_All_Batch(AddStudent.this, newBatchList);
    }

    private void sendSMS(Student student) {
        String SMS = "Hi, "+student.getName() + ", Subject-"+student.getSubject()+", you are registered successfully on "+
                student.getAdmissionDate()+". Helpgroup: www.facebook.com/onlineschoolinghelp. YouTube Name: online schooling";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(student.getPhone(), null, SMS, null, null);
            Toast.makeText(AddStudent.this, student.getName()+" added to ABC Academy successfully and message sent", Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(AddStudent.this, "Error "+e+" occurred", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        batch_databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newBatchList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Batch tmp = snapshot.getValue(Batch.class);
                    newBatchList.add(tmp);
                }

                listView.setAdapter(show_all_batch);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
