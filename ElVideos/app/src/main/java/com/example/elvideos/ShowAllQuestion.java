package com.example.elvideos;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ShowAllQuestion extends ArrayAdapter<Question> {
    private Activity context;
    private List<Question> questionList;
    private TextView ques;
    private Button op1, op2, op3, op4;

    public ShowAllQuestion(Activity context, List<Question> questionList) {
        super(context, R.layout.question_item, questionList);

        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.question_item, null, true);

        ques = view.findViewById(R.id.quesItem_quesText);
        op1 = view.findViewById(R.id.quesItem_op1Button);
        op2 = view.findViewById(R.id.quesItem_op2Button);
        op3 = view.findViewById(R.id.quesItem_op3Button);
        op4 = view.findViewById(R.id.quesItem_op4Button);

        Question question = questionList.get(position);

        ques.setText(question.getQuestion());
        op1.setText(question.getOption1());
        op2.setText(question.getOption2());
        op3.setText(question.getOption3());
        op4.setText(question.getOption4());

        return view;
    }
}
