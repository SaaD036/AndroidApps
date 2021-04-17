package com.example.elvideos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class Login extends AppCompatActivity {
    private EditText mail, pass;
    private Button login, signup;
    private DatabaseReference databaseReference, p;
    private List<Member> memberList;
    private Long k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComp();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Member member = snapshot.getValue(Member.class);
                    memberList.add(member);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag=false, isAdmin=true;
                String email, password;
                email = mail.getText().toString().trim();
                password = pass.getText().toString();

                if(email.isEmpty()){
                    flag = false;
                    Toast.makeText(Login.this, "Enter an email or phone", Toast.LENGTH_LONG).show();
                }
                if (!flag){
                    for (int i=0; i<memberList.size(); i++) {
                        Member member = memberList.get(i);

                        if (member.getEmail().equals(email) && member.getPassword().equals(password)) {
                            flag = true;
                            isAdmin = member.isAdmin();
                            break;
                        }
                    }
                }
                if (flag){
                    mail.setText("");
                    pass.setText("");
                    Intent intent = new Intent(Login.this, AdminPage.class);
                    startActivity(intent);

                }
                if (!flag){
                    Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
    }

    private void initComp(){
        mail = findViewById(R.id.login_emailText);
        pass = findViewById(R.id.login_passwordText);
        login = findViewById(R.id.login_loginButton);
        signup = findViewById(R.id.login_signupButton);
        databaseReference = FirebaseDatabase.getInstance().getReference("Member");
        memberList = new ArrayList<>();
        p = FirebaseDatabase.getInstance().getReference("Quiz");
    }
}
