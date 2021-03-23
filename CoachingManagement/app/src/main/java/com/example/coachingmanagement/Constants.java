package com.example.coachingmanagement;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants {
    public final DatabaseReference studentDatabase = FirebaseDatabase.getInstance().getReference("Students");
    public final DatabaseReference batchDatabase = FirebaseDatabase.getInstance().getReference("Batch");
}
