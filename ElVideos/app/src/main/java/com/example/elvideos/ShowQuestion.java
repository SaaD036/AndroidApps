package com.example.elvideos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.ShowFirstParty;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowQuestion extends AppCompatActivity {
    private TextView ques, stat;
    private Button op1, op2, op3, op4;
    private ListView listView;
    private DatabaseReference databaseReference;
    private List<Question> questionList;
    private ShowAllQuestion showAllQuestion;
    private int i, mark;
    private String answer;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_question);

        initComp();
        i=0;
        mark=0;
        flag = true;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(flag){
                    Question question = questionList.get(i);

                    ques.setText(question.getQuestion());
                    op1.setText(question.getOption1());
                    op2.setText(question.getOption2());
                    op3.setText(question.getOption3());
                    op4.setText(question.getOption4());
                    answer = question.getAnswer();

                    ques.setVisibility(View.VISIBLE);
                    stat.setVisibility(View.VISIBLE);
                    op1.setVisibility(View.VISIBLE);
                    op2.setVisibility(View.VISIBLE);
                    op3.setVisibility(View.VISIBLE);
                    op4.setVisibility(View.VISIBLE);

                    op1.setEnabled(true);
                    op2.setEnabled(true);
                    op3.setEnabled(true);
                    op4.setEnabled(true);

                    flag=false;
                    listView.setVisibility(View.GONE);
                }
            }
        });

        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = op1.getText().toString().trim();

                if(answer.equals(tmp)){
                    stat.setText("Correct");
                    mark++;

                    op1.setBackgroundResource(R.drawable.button_right);
                    op2.setBackgroundResource(R.drawable.input);
                    op3.setBackgroundResource(R.drawable.input);
                    op4.setBackgroundResource(R.drawable.input);
                }
                else{
                    op1.setBackgroundResource(R.drawable.button_wrong);
                    stat.setText("Correct answer : "+answer);

                    op2.setBackgroundResource(R.drawable.input);
                    op3.setBackgroundResource(R.drawable.input);
                    op4.setBackgroundResource(R.drawable.input);
                }

                loadQuiz();
            }
        });
        op2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = op2.getText().toString().trim();

                if(answer.equals(tmp)){
                    stat.setText("Correct");
                    mark++;

                    op1.setBackgroundResource(R.drawable.input);
                    op2.setBackgroundResource(R.drawable.button_right);
                    op3.setBackgroundResource(R.drawable.input);
                    op4.setBackgroundResource(R.drawable.input);
                }
                else{
                    op2.setBackgroundResource(R.drawable.button_wrong);
                    stat.setText("Correct answer : "+answer);

                    op1.setBackgroundResource(R.drawable.input);
                    op3.setBackgroundResource(R.drawable.input);
                    op4.setBackgroundResource(R.drawable.input);
                }

                loadQuiz();
            }
        });
        op3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = op3.getText().toString().trim();

                if(answer.equals(tmp)){
                    stat.setText("Correct");
                    mark++;

                    op1.setBackgroundResource(R.drawable.input);
                    op2.setBackgroundResource(R.drawable.input);
                    op3.setBackgroundResource(R.drawable.button_right);
                    op4.setBackgroundResource(R.drawable.input);
                }
                else{
                    op3.setBackgroundResource(R.drawable.button_wrong);
                    stat.setText("Correct answer : "+answer);

                    op1.setBackgroundResource(R.drawable.input);
                    op2.setBackgroundResource(R.drawable.input);
                    op4.setBackgroundResource(R.drawable.input);
                }

                loadQuiz();
            }
        });
        op4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = op4.getText().toString().trim();

                if(answer.equals(tmp)){
                    stat.setText("Correct");
                    mark++;

                    op1.setBackgroundResource(R.drawable.input);
                    op2.setBackgroundResource(R.drawable.input);
                    op3.setBackgroundResource(R.drawable.input);
                    op4.setBackgroundResource(R.drawable.button_right);
                }
                else{
                    op4.setBackgroundResource(R.drawable.button_wrong);
                    stat.setText("Correct answer : "+answer);

                    op1.setBackgroundResource(R.drawable.input);
                    op2.setBackgroundResource(R.drawable.input);
                    op3.setBackgroundResource(R.drawable.input);
                }

                loadQuiz();
            }
        });
    }

    private void initComp(){
        ques = findViewById(R.id.showQuestion_quesText);
        stat = findViewById(R.id.showQuestion_statText);

        op1 = findViewById(R.id.showQuestion_op1Button);
        op2 = findViewById(R.id.showQuestion_op2Button);
        op3 = findViewById(R.id.showQuestion_op3Button);
        op4 = findViewById(R.id.showQuestion_op4Button);

        listView = findViewById(R.id.showQues_listview);
        databaseReference = FirebaseDatabase.getInstance().getReference("Question");
        questionList = new ArrayList<>();
        showAllQuestion = new ShowAllQuestion(ShowQuestion.this, questionList);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionList.clear();

                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    Question tmp = snapshot.getValue(Question.class);
                    questionList.add(tmp);
                }

                listView.setAdapter(showAllQuestion);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void loadQuiz(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                i = i+1;

                if(i==questionList.size()){
                    loadResult();
                }
                else {
                    Question question = questionList.get(i);

                    ques.setText(question.getQuestion());
                    op1.setText(question.getOption1());
                    op2.setText(question.getOption2());
                    op3.setText(question.getOption3());
                    op4.setText(question.getOption4());
                    answer = question.getAnswer();

                    op1.setBackgroundResource(R.drawable.input);
                    op2.setBackgroundResource(R.drawable.input);
                    op3.setBackgroundResource(R.drawable.input);
                    op4.setBackgroundResource(R.drawable.input);

                    stat.setText("");
                }
            }
        }, 5000);
    }

    private void loadResult(){
        ques.setText("Congratutations...");

        op1.setVisibility(View.GONE);
        op2.setVisibility(View.GONE);
        op3.setVisibility(View.GONE);
        op4.setVisibility(View.GONE);

        stat.setText("Your score is "+mark+" out of "+questionList.size());
    }
}