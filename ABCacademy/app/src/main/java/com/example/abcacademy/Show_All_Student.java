package com.example.abcacademy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class Show_All_Student extends ArrayAdapter <Student> {
    private Activity context;
    private TextView name, phone, batchName, subject, due, totalPayment, admissionDate;
    private ImageView imageView;
    private LinearLayout phoneLayout, paymentLayout, subjectLayout, dateLayout;
    private List<Student> studentList;
    private boolean flag;


    public Show_All_Student(Activity context, boolean flag, List<Student> studentList) {
        super(context, R.layout.item_student, studentList);

        this.context = context;
        this.studentList = studentList;
        this.flag = flag;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_student, null, true);

        name = view.findViewById(R.id.itemStudent_nameTextview);
        phone = view.findViewById(R.id.itemStudent_phoneTextview);
        batchName = view.findViewById(R.id.itemStudent_batchTextview);
        subject = view.findViewById(R.id.itemStudent_subjectTextview);
        due = view.findViewById(R.id.itemStudent_dueTextview);
        totalPayment = view.findViewById(R.id.itemStudent_totalPaymentTextview);
        admissionDate = view.findViewById(R.id.itemStudent_dateTextview);
        imageView = view.findViewById(R.id.itemStudent_unpaidImageview);
        phoneLayout = view.findViewById(R.id.itemStudent_phoneLayout);
        subjectLayout = view.findViewById(R.id.itemStudent_subjectLayout);
        paymentLayout = view.findViewById(R.id.itemStudent_paymentLayout);
        dateLayout = view.findViewById(R.id.itemStudent_dateLayout);

        Student student = studentList.get(position);

        name.setText(student.getName());
        batchName.setText(student.getBatch());
        due.setText(""+student.getDue());


        if (student.getDue() > 0){
            imageView.setImageResource(R.drawable.unpaid);
            due.setTextColor(Color.parseColor("#F40303"));
        }

        if (flag){
            phoneLayout.setVisibility(View.VISIBLE);
            subjectLayout.setVisibility(View.VISIBLE);
            paymentLayout.setVisibility(View.VISIBLE);
            dateLayout.setVisibility(View.VISIBLE);

            phone.setText(student.getPhone());
            subject.setText(student.getSubject());
            totalPayment.setText(""+student.getTotalPayment());
            admissionDate.setText(student.getAdmissionDate());
        }

        return view;
    }
}
