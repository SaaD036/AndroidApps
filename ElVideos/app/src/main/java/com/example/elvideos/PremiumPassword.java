package com.example.elvideos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PremiumPassword extends AppCompatActivity {
    private Button submit;
    private EditText pass;
    private String password;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_password);

        initComp();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = pass.getText().toString();

                if(password.trim().isEmpty()){
                    Toast.makeText(PremiumPassword.this, "Enter a valid password", Toast.LENGTH_LONG).show();
                }
                else{
                    databaseReference.setValue(password);
                    Toast.makeText(PremiumPassword.this, "Password updated successfully", Toast.LENGTH_LONG).show();
                    pass.setText("");
                }
            }
        });
    }

    private void initComp(){
        submit = findViewById(R.id.premiumPassword_submitButton);
        pass = findViewById(R.id.premiumPassword_passwordText);

        databaseReference = FirebaseDatabase.getInstance().getReference("PremiumPassword");
    }
}
