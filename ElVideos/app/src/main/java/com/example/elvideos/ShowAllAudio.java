package com.example.elvideos;

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

public class ShowAllAudio extends ArrayAdapter<AudioInfo> {
    private Activity context;
    private List<AudioInfo> audioInfoList;

    public ShowAllAudio(Activity context, List<AudioInfo> objects) {
        super(context, R.layout.audio_item, objects);

        this.context = context;
        this.audioInfoList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.audio_item, null, true);

        TextView textView = view.findViewById(R.id.audioItem_playTextview);
        AudioInfo audioInfo = audioInfoList.get(position);
        textView.setText(audioInfo.getName());

        return view;
    }
}
