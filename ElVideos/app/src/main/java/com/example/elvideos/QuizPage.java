package com.example.elvideos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizPage extends AppCompatActivity {
    private TextView ques, stat;
    private Button op1, op2, op3, op4;
    private String question, answer, option1, option2, option3, option4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

        question = getIntent().getStringExtra("q");
        answer = getIntent().getStringExtra("a");
        option1 = getIntent().getStringExtra("o1");
        option2 = getIntent().getStringExtra("o2");
        option3 = getIntent().getStringExtra("o3");
        option4 = getIntent().getStringExtra("o4");

        initComp();

        ques.setText(question);
        op1.setText(option1);
        op2.setText(option2);
        op3.setText(option3);
        op4.setText(option4);

        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = op1.getText().toString().trim();

                if(answer.equals(tmp)){
                    stat.setText("Ok");

                    op1.setBackgroundResource(R.drawable.button_right);
                    op2.setBackgroundResource(R.drawable.input);
                    op3.setBackgroundResource(R.drawable.input);
                    op4.setBackgroundResource(R.drawable.input);
                }
                else{
                    op1.setBackgroundResource(R.drawable.button_wrong);
                    stat.setText(answer);

                    op2.setBackgroundResource(R.drawable.input);
                    op3.setBackgroundResource(R.drawable.input);
                    op4.setBackgroundResource(R.drawable.input);
                }
            }
        });
        op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = op2.getText().toString().trim();

                if(answer.equals(tmp)){
                    stat.setText("Ok");

                    op1.setBackgroundResource(R.drawable.input);
                    op2.setBackgroundResource(R.drawable.button_right);
                    op3.setBackgroundResource(R.drawable.input);
                    op4.setBackgroundResource(R.drawable.input);
                }
                else{
                    op2.setBackgroundResource(R.drawable.button_wrong);
                    stat.setText(answer);

                    op1.setBackgroundResource(R.drawable.input);
                    op3.setBackgroundResource(R.drawable.input);
                    op4.setBackgroundResource(R.drawable.input);
                }
            }
        });
        op3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = op3.getText().toString().trim();

                if(answer.equals(tmp)){
                    stat.setText("Ok");

                    op1.setBackgroundResource(R.drawable.input);
                    op2.setBackgroundResource(R.drawable.input);
                    op3.setBackgroundResource(R.drawable.button_right);
                    op4.setBackgroundResource(R.drawable.input);
                }
                else{
                    op3.setBackgroundResource(R.drawable.button_wrong);
                    stat.setText(answer);

                    op1.setBackgroundResource(R.drawable.input);
                    op2.setBackgroundResource(R.drawable.input);
                    op4.setBackgroundResource(R.drawable.input);

                }
            }
        });
        op4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = op4.getText().toString().trim();

                if(answer.equals(tmp)){
                    stat.setText("Ok");

                    op1.setBackgroundResource(R.drawable.input);
                    op2.setBackgroundResource(R.drawable.input);
                    op3.setBackgroundResource(R.drawable.input);
                    op4.setBackgroundResource(R.drawable.button_right);
                }
                else{
                    op4.setBackgroundResource(R.drawable.button_wrong);
                    stat.setText(answer);

                    op1.setBackgroundResource(R.drawable.input);
                    op2.setBackgroundResource(R.drawable.input);
                    op3.setBackgroundResource(R.drawable.input);
                }
            }
        });
    }

    private void initComp(){
        ques = findViewById(R.id.quizPage_quesText);
        stat = findViewById(R.id.quizPage_statText);

        op1 = findViewById(R.id.quizPage_op1Button);
        op2 = findViewById(R.id.quizPage_op2Button);
        op3 = findViewById(R.id.quizPage_op3Button);
        op4 = findViewById(R.id.quizPage_op4Button);
    }
}
