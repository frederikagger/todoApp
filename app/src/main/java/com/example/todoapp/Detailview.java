package com.example.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class Detailview extends AppCompatActivity {
    private TextView textView;
    private AlertDialog alertDialog;
    private EditText editText;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailview);
        setActivity();
        setAlertDialog();


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(textView.getText());
                alertDialog.show();
            }
        });
    }

    public void setActivity() {
        this.textView = (TextView) findViewById(R.id.textView);
        String passedText = getIntent().getExtras().getString("text");
        this.textView.setText(passedText);
        this.alertDialog = new AlertDialog.Builder(this).create();
        this.editText = new EditText(this);
        this.alertDialog.setTitle("Edit text");
        this.alertDialog.setView(editText);
    }

    public void setAlertDialog(){
        this.alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "SAVE TEXT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(editText.getText());
                saveToFirebase();
            }
        });
    }

    public void saveToFirebase(){
        String title = getIntent().getExtras().getString("title");
        System.out.println(title);
        this.db.collection("todos").document(title).set(new Todo(title, this.textView.getText().toString()));
    }
}