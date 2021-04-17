package com.example.elvideos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TextUpload extends AppCompatActivity {
    private EditText name, story;
    private Button save;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_upload);

        initComp();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n=name.getText().toString().trim();
                String s=story.getText().toString().trim();

                if (n.isEmpty() || s.isEmpty()){
                    Toast.makeText(TextUpload.this, "Name or story is empty", Toast.LENGTH_LONG).show();
                }
                else{
                    TextInfo textInfo = new TextInfo();
                    textInfo.setName(n);
                    textInfo.setStory(s);

                    databaseReference.push().setValue(textInfo);

                    Toast.makeText(TextUpload.this, "Story saved successfully", Toast.LENGTH_LONG).show();

                    name.setText("");
                    story.setText("");
                }
            }
        });
    }

    private void initComp(){
        name = findViewById(R.id.textUpload_nameText);
        story = findViewById(R.id.textUpload_storyText);

        save = findViewById(R.id.textUpload_saveButton);
        databaseReference = FirebaseDatabase.getInstance().getReference("TextInfo");
    }
}
