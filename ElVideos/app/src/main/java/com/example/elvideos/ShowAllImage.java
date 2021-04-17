package com.example.elvideos;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ShowAllImage extends ArrayAdapter<ImageInfo> {
    private Activity context;
    private TextView textView;
    private ImageView imageView;
    private List<ImageInfo> imageInfoList;

    public ShowAllImage(Activity context, List<ImageInfo> imageInfoList) {
        super(context, R.layout.image_item, imageInfoList);

        this.context = context;
        this.imageInfoList = imageInfoList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.image_item, null, true);
        textView = view.findViewById(R.id.imageItem_imageNameText);
        imageView = view.findViewById(R.id.imageItem_imageView);

        ImageInfo imageInfo = imageInfoList.get(position);
        textView.setText(imageInfo.getName());
        //imageView.setImageURI(Uri.parse(imageInfo.getUrl()));

        return  view;
    }
}
