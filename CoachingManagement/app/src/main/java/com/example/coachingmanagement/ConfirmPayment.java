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

import java.util.ArrayList;
import java.util.List;

public class ConfirmPayment extends AppCompatActivity {
    private EditText nameText, phoneText, amountText;
    private Button conPayment;
    private String name, phone, key;
    private int amount, totalPayment, due;
    private DatabaseReference databaseReference;
    private List<Student> studentList;
    private List<String> keyList;
    Student tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);

        initComp();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Student student = snapshot.getValue(Student.class);
                    studentList.add(student);
                    keyList.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        conPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameText.getText().toString().trim();
                phone = phoneText.getText().toString().trim();
                amount = Integer.parseInt(amountText.getText().toString().trim());
                boolean flag = true;

                if(name.isEmpty() || phone.isEmpty() || amountText.getText().toString().trim().isEmpty()){
                    flag = false;
                    Toast.makeText(ConfirmPayment.this, "You can't leave any field empty", Toast.LENGTH_LONG).show();
                }
                if (flag){
                    flag = false;

                    for(int i=0; i<studentList.size(); i++){
                        Student student = studentList.get(i);

                        if (student.getPhone().equals(phone)){
                            flag = true;

                            totalPayment = student.getTotalPayment() + 1;
                            due = student.getDue() - amount;
                            key = keyList.get(i);
                            tmp = student;
                            break;
                        }
                    }

                    if (!flag){
                        Toast.makeText(ConfirmPayment.this, "No student with this phone", Toast.LENGTH_LONG).show();
                    }
                }
                if(flag){
                    databaseReference.child(key).child("due").setValue(due);
                    databaseReference.child(key).child("totalPayment").setValue(totalPayment);

                    //Toast.makeText(ConfirmPayment.this, "Payment made successfully", Toast.LENGTH_LONG).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                            sendSMS(tmp);
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
        nameText = findViewById(R.id.confirmPament_nameEdittext);
        phoneText = findViewById(R.id.confirmPament_phoneEdittext);
        amountText = findViewById(R.id.confirmPament_amountEdittext);
        conPayment = findViewById(R.id.confirmPament_conPayButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("Student");
        studentList = new ArrayList<>();
        keyList = new ArrayList<>();
    }

    private void sendSMS(Student student) {
        String SMS = "Hi, " + student.getName() + ", your fee tk " + amount +
                " is successfully received. Your current due is " + due + ". \n\nThank you";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(student.getPhone(), null, SMS, null, null);
            Toast.makeText(ConfirmPayment.this, "Message sent succesfully to "+student.getName(), Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(ConfirmPayment.this, "Error "+e+" occurred", Toast.LENGTH_LONG).show();
        }
    }
}
