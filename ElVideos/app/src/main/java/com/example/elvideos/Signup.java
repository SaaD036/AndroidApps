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

public class Signup extends AppCompatActivity {
    private EditText mail, u_name, pass, conPass;
    private Button login, signup;
    private DatabaseReference member_database;
    private List<Member> memberList;
    String email, name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initComp();

        member_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Member tmp = snapshot.getValue(Member.class);
                    memberList.add(tmp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mail.getText().toString().trim();
                name = u_name.getText().toString().trim();
                password = pass.getText().toString();
                boolean flag = true;

                if(email.isEmpty() || name.isEmpty() || password.trim().isEmpty()){
                    flag = false;
                    Toast.makeText(Signup.this, "You can't leave any field empty", Toast.LENGTH_LONG).show();
                }

                if (flag){
                    for (int i=0; i<memberList.size(); i++){
                        Member member = memberList.get(i);

                        if(member.getEmail().equals(email)){
                            flag = false;
                            break;
                        }
                    }
                }
                if(!flag){
                    Toast.makeText(Signup.this, "Account already exists", Toast.LENGTH_LONG).show();
                }
                if (flag){
                    if (!password.equals(conPass.getText().toString())){
                        Toast.makeText(Signup.this, "Please re enter correct password", Toast.LENGTH_LONG).show();
                        flag = false;
                    }
                }
                if (flag){
                    Member member = new Member();
                    member.setEmail(email);
                    member.setName(name);
                    member.setPassword(password);
                    member.setAdmin(true);

                    member_database.push().setValue(member);

                    Toast.makeText(Signup.this, "Successfully signed up", Toast.LENGTH_LONG).show();

                    mail.setText("");
                    u_name.setText("");
                    pass.setText("");
                    conPass.setText("");
                }
            }
        });
    }

    private void initComp(){
        mail = findViewById(R.id.signup_emailText);
        u_name = findViewById(R.id.signup_nameText);
        pass = findViewById(R.id.signup_passwordText);
        conPass = findViewById(R.id.signup_conpasswordText);

        login = findViewById(R.id.signup_loginButton);
        signup = findViewById(R.id.signup_signupButton);

        member_database = FirebaseDatabase.getInstance().getReference("Member");
        memberList = new ArrayList<>();
    }
}
