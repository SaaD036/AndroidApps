package com.example.irrigationhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class ProgressBar {
    private Activity activity;
    private AlertDialog alertDialog;

    ProgressBar(Activity activity) {
        this.activity = activity;
    }

    public void startLoadingProgressBar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.custom_dialouge, null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissProgressBar(){
        alertDialog.dismiss();
    }
}
