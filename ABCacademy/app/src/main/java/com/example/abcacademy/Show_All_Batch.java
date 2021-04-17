package com.example.abcacademy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.util.List;

public class Show_All_Batch extends ArrayAdapter<Batch> {
    private Activity context;
    private List<Batch> batchList;
    private TextView name, total;

    public Show_All_Batch(Activity context,  List<Batch> batchList) {
        super(context, R.layout.item_batch, batchList);

        this.context = context;
        this.batchList = batchList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_batch,  null, true);

        name = view.findViewById(R.id.itemBatch_nameTextview);
        total = view.findViewById(R.id.itemBatch_totalStudentTextview);

        Batch batch = batchList.get(position);

        name.setText(batch.getName());
        total.setText(""+batch.getTotalStudent());

        return view;
    }
}
