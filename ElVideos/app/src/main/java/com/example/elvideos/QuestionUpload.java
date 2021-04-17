package com.example.elvideos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuestionUpload extends AppCompatActivity {
    private EditText ques, ans, op1, op2, op3, op4;
    private Button upload;
    private DatabaseReference databaseReference;
    private int int_ans;
    private String question, answer, option1, option2, option3, option4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_upload);

        initComp();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question = ques.getText().toString().trim();
                answer = ans.getText().toString().trim();
                option1 = op1.getText().toString().trim();
                option2 = op2.getText().toString().trim();
                option3 = op3.getText().toString().trim();
                option4 = op4.getText().toString().trim();
                int_ans = Integer.parseInt(answer);
                boolean flag=true;

                if(question.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || answer.isEmpty()){
                    flag = false;
                    Toast.makeText(QuestionUpload.this, "Invalid inputs", Toast.LENGTH_LONG).show();
                }
                if(flag && (int_ans>4||int_ans<1) ){
                    flag = false;
                    Toast.makeText(QuestionUpload.this, "Invalid answer", Toast.LENGTH_LONG).show();
                }
                if (flag){
                    if (int_ans==1){
                        answer = option1;
                    }
                    else if(int_ans==2){
                        answer = option2;
                    }
                    else if(int_ans==3){
                        answer = option3;
                    }
                    else{
                        answer = option4;
                    }

                    Question tmp = new Question();

                    tmp.setQuestion(question);
                    tmp.setOption1(option1);
                    tmp.setOption2(option2);
                    tmp.setOption3(option3);
                    tmp.setOption4(option4);
                    tmp.setAnswer(answer);

                    databaseReference.push().setValue(tmp);
                    Toast.makeText(QuestionUpload.this, "Quiz added successfully", Toast.LENGTH_LONG).show();

                    ques.setText("");
                    ans.setText("");
                    op1.setText("");
                    op2.setText("");
                    op3.setText("");
                    op4.setText("");
                }
            }
        });
    }

    private void initComp(){
        ques = findViewById(R.id.quesUpload_quesText);
        ans = findViewById(R.id.quesUploadansText);
        op1 = findViewById(R.id.quesUpload_op1Text);
        op2 = findViewById(R.id.quesUpload_op2Text);
        op3 = findViewById(R.id.quesUpload_op3Text);
        op4 = findViewById(R.id.quesUpload_op4Text);

        upload = findViewById(R.id.quesUpload_uploadButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("Question");
    }
}
