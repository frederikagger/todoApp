package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Detailview extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailview);

        textView = (TextView) findViewById(R.id.textView);
        String passedText = getIntent().getExtras().getString("text");
        System.out.println(passedText);
        textView.setText(passedText);

    }
}