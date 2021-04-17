package com.example.mycall;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<ItemContact> {
    private Activity context;
    private List<ItemContact> itemContactList;

    public ContactAdapter(Activity context,  List<ItemContact> objects) {
        super(context, R.layout.item_contact, objects);

        this.context = context;
        this.itemContactList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_contact, null, true);

        TextView name = view.findViewById(R.id.itemContact_nameTextview), number = view.findViewById(R.id.itemContact_numberTextiew);
        name.setText("Name : "+itemContactList.get(position).getName());
        number.setText("q : "+itemContactList.get(position).getNumber());

        return view;
    }
}
