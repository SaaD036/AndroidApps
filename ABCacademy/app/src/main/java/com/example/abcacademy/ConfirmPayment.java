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

import java.util.ArrayList;
import java.util.List;

public class ConfirmPayment extends AppCompatActivity {
    private EditText nameText, phoneText, amountText;
    private Button add, delete;
    private ListView listView;
    private DatabaseReference databaseReference, batch_database;
    private List<Student> studentList;
    private List<Batch> batchList;
    private List<String> keyList, batchKeyList;
    private String key, batchKey;
    private String name, phone, batch;
    private int amount, totalPayment, due;
    private Student student;
    private Show_All_Student show_all_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);

        initComp();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Student tmp = snapshot.getValue(Student.class);
                    studentList.add(tmp);
                    keyList.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        batch_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Batch tmp = snapshot.getValue(Batch.class);
                    batchList.add(tmp);
                    batchKeyList.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameText.getText().toString().trim();
                phone = phoneText.getText().toString().trim();
                boolean flag = true;

                if(phone.isEmpty() || name.isEmpty() || amountText.toString().trim().isEmpty()){
                    flag = false;
                    Toast.makeText(ConfirmPayment.this, "Tou can't leave any field empty", Toast.LENGTH_LONG).show();
                }
                if (flag){
                    flag = false;
                    amount = Integer.parseInt(amountText.getText().toString().trim()+"0");
                    amount = amount/10;

                    for (int i=0; i<studentList.size(); i++){
                        Student tmp = studentList.get(i);

                        if (tmp.getPhone().equals(phone)){
                            flag = true;
                            key = keyList.get(i);
                            totalPayment = tmp.getTotalPayment() + 1;
                            due = tmp.getDue() - amount;
                            student = tmp;
                            break;
                        }
                    }

                    if (!flag){
                        Toast.makeText(ConfirmPayment.this, "No student found with this phone number", Toast.LENGTH_LONG).show();
                    }
                }
                if (flag){
                    databaseReference.child(key).child("totalPayment").setValue(totalPayment);
                    databaseReference.child(key).child("due").setValue(due);

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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameText.getText().toString().trim();
                phone = phoneText.getText().toString().trim();
                boolean flag = true;

                if(phone.isEmpty() || name.isEmpty()){
                    flag = false;
                    Toast.makeText(ConfirmPayment.this, "Tou can't leave any field empty", Toast.LENGTH_LONG).show();
                }
                if (flag){
                    flag = false;

                    for (int i=0; i<studentList.size(); i++){
                        Student tmp = studentList.get(i);

                        if (tmp.getPhone().equals(phone)){
                            flag = true;
                            batch = tmp.getBatch();
                            key = keyList.get(i);
                            break;
                        }
                    }

                    if (!flag){
                        Toast.makeText(ConfirmPayment.this, "No student found with this phone number", Toast.LENGTH_LONG).show();
                    }
                }
                if (flag){
                    int totalStudent=0;

                    for (int i=0; i<batchList.size(); i++){
                        Batch tmp = batchList.get(i);

                        if (tmp.getName().equals(batch)){
                            totalStudent = tmp.getTotalStudent() - 1;
                            batchKey = batchKeyList.get(i);
                            break;
                        }
                    }

                    databaseReference.child(key).removeValue();
                    batch_database.child(batchKey).child("totalStudent").setValue(totalStudent);
                    Toast.makeText(ConfirmPayment.this, "Student is deleted", Toast.LENGTH_LONG).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student tmp = studentList.get(position);

                name = tmp.getName();
                phone = tmp.getPhone();
                batch = tmp.getBatch();

                nameText.setText(name);
                phoneText.setText(phone);

                add.setEnabled(true);
                delete.setEnabled(true);
            }
        });
    }

    private void initComp(){
        nameText = findViewById(R.id.confirmPament_nameEdittext);
        phoneText = findViewById(R.id.confirmPament_phoneEdittext);
        amountText = findViewById(R.id.confirmPament_amountEdittext);

        add = findViewById(R.id.confirmPament_conPayButton);
        delete = findViewById(R.id.confirmPament_deleteButton);

        listView = findViewById(R.id.confirmPament_listview);

        databaseReference = FirebaseDatabase.getInstance("https://abcacademy-c4ae3-default-rtdb.firebaseio.com/").getReference("Student");
        batch_database = FirebaseDatabase.getInstance("https://abcacademy-c4ae3-default-rtdb.firebaseio.com/").getReference("Batch");
        studentList = new ArrayList<>();
        batchList= new ArrayList<>();
        keyList = new ArrayList<>();
        batchKeyList = new ArrayList<>();

        show_all_student = new Show_All_Student(ConfirmPayment.this, false, studentList);
    }

    private void sendSMS(Student student) {
        String SMS = "Hi, " + student.getName() + ", Subject- "+student.getSubject()+". Your fee tk " + amount +
                " payment is successfully received. Thank you";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(student.getPhone(), null, SMS, null, null);
            Toast.makeText(ConfirmPayment.this, "Message sent succesfully to "+student.getName(), Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(ConfirmPayment.this, "Error "+e+" occurred", Toast.LENGTH_LONG).show();
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
                    Student tmp = snapshot.getValue(Student.class);
                    studentList.add(tmp);
                    keyList.add(snapshot.getKey());
                }

                listView.setAdapter(show_all_student);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
