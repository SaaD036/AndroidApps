package com.example.elvideos;

import android.app.Activity;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;

import java.util.List;

public class ShowAllText extends ArrayAdapter<TextInfo> {
    private Activity context;
    private List<TextInfo> textInfoList;

    public ShowAllText(Activity context, List<TextInfo> textInfoList) {
        super(context, R.layout.text_item, textInfoList);

        this.context = context;
        this.textInfoList = textInfoList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.text_item, null, true);

        final TextInfo textInfo = textInfoList.get(position);
        TextView name = view.findViewById(R.id.textItem_nameText);

        name.setText(textInfo.getName());

        return view;
    }
}
