package com.example.todoapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ArrayList<String> todos = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setArrayToView();
        listener();
    }

    private void setArrayToView() {
        ListView mylistview = findViewById(R.id.listview);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todos);
        mylistview.setAdapter(arrayAdapter);
    }


    public void listener(){
        db.collection("todos").addSnapshotListener((values, error) -> {
            for(DocumentSnapshot snap : values.getDocuments()){
                String text1= snap.get("text").toString();

                //Todo todo = new Todo(snap.get("text").toString(), snap.getId());
                todos.add(text1);
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }



    public void changeActivity(AdapterView<?> parent, View view, int position, long id){
        Intent intent = new Intent(this, Detailview.class);
        //intent.putExtra("text", this.notes[position].getText());
        startActivity(intent);
    }
}