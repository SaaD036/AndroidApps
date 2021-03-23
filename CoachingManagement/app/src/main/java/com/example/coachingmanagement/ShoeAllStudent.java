package com.example.coachingmanagement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ShoeAllStudent extends ArrayAdapter<Student> {
    private TextView name, phone, batch, subject, due, totalPayment, admissionDate;
    private ImageView imageView;
    private Activity context;
    private List<Student> studentList;

    public ShoeAllStudent(Activity context, List<Student> studentList) {
        super(context, R.layout.item_student, studentList);

        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_student, null, true);

        name = view.findViewById(R.id.itemStudent_nameTextview);
        phone = view.findViewById(R.id.itemStudent_phoneTextview);
        batch = view.findViewById(R.id.itemStudent_batchTextview);
        subject = view.findViewById(R.id.itemStudent_subjectTextview);
        due = view.findViewById(R.id.itemStudent_dueTextview);
        totalPayment = view.findViewById(R.id.itemStudent_totalPaymentTextview);
        admissionDate = view.findViewById(R.id.itemStudent_dateTextview);
        imageView = view.findViewById(R.id.itemStudent_unpaidImageview);

        Student student = studentList.get(position);

        name.setText(student.getName());
        phone.setText(student.getPhone());
        batch.setText(student.getBatch());
        subject.setText(student.getSubject());
        due.setText(""+student.getDue());
        totalPayment.setText(""+student.getTotalPayment());
        admissionDate.setText(student.getAdmissionDate());

        if (student.getDue() >0 ){
            due.setTextColor(Color.parseColor("#F40303"));
            imageView.setImageResource(R.drawable.unpaid);
        }
        return view;
    }
}
