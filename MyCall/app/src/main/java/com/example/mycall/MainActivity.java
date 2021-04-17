package com.example.mycall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView contactStatus;
    private ListView contactListview;
    private String p, q;
    private List<ItemContact> itemContactList;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComp();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CONTACTS}, PackageManager.PERMISSION_GRANTED);
    }

    private  void initComp(){
        contactStatus = findViewById(R.id.main_contactStatusTextview);
        contactListview = findViewById(R.id.main_contactListview);

        itemContactList = new ArrayList<>();
        contactAdapter = new ContactAdapter(MainActivity.this, itemContactList);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Cursor cursor = getContentResolver().
                query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
            ItemContact tmp = new ItemContact();

            //getting the name
            p = cursor.getString(33);

            //getting all phone number under a name
            if (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))) {
                Cursor phones = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ p, null, null);

                while (phones.moveToNext()) {
                    q  = q + ", " +phones.getString(phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();
            }

            tmp.setName(p);
            tmp.setNumber(q);
            itemContactList.add(tmp);

            while(cursor.moveToNext()){
                //getting the name
                p = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                //getting all phone number under a name
                if (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))) {
                    Cursor phones = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ p, null, null);

                    while (phones.moveToNext()) {
                        q  = q + ", " +phones.getString(phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                }

                ItemContact tmp1 = new ItemContact();
                tmp1.setName(p);
                tmp1.setNumber(q);
                itemContactList.add(tmp1);
            }
            contactListview.setAdapter(contactAdapter);
        }
    }
}
