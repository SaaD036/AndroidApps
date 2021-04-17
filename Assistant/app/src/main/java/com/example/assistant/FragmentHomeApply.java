package com.example.assistant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class FragmentHomeApply extends Fragment{
    private EditText emailText, subjectText, bodyText;
    private Button attach, send;
    private View view;
    private Uri uri;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.home_apply_fragment, container, false);

        initComp();

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 10);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, subject, body;

                email = emailText.getText().toString().trim();
                subject = subjectText.getText().toString().trim();
                body = bodyText.getText().toString().trim();

                if(email.isEmpty()){
                    Toast.makeText(getContext(), "Provide an email", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("mailto:"));
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL, email);
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_TEXT, body);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(intent, ""));
                }
            }
        });

        return view;
    }

    private void initComp(){
        emailText = view.findViewById(R.id.applyFragment_emailEditText);
        subjectText = view.findViewById(R.id.applyFragment_subjectEditText);
        bodyText = view.findViewById(R.id.applyFragment_bodyEditText);

        send = view.findViewById(R.id.applyFragment_sendButton);
        attach = view.findViewById(R.id.applyFragment_attachFileButton);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 10 && resultCode==RESULT_OK){
            uri = data.getData();
        }
    }
}
