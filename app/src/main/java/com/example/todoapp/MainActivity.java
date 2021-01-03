package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Todo> todos = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayAdapter<Todo> arrayAdapter;
    private ListView mylistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setArrayToView();
        listener();
        attachOnClick();
    }

    public void setArrayToView() {
        this.mylistview = findViewById(R.id.listview);
        this.arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todos);
        this.mylistview.setAdapter(arrayAdapter);
    }

    public void listener(){
        this.db.collection("todos").addSnapshotListener((values, error) -> {
            this.todos.clear();
            for(DocumentSnapshot snap : values.getDocuments()){
                String title = snap.getId();
                String text = snap.get("text").toString();
                String path = snap.get("path").toString();
                this.todos.add(new Todo(title, text, path));
                this.arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public void attachOnClick(){
        this.mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeActivity(position);
            }
        });
    }

    public void changeActivity(int position){
        Intent intent = new Intent(this, Detailview.class);
        intent.putExtra("title", this.todos.get(position).getTitle());
        intent.putExtra("text", this.todos.get(position).getText());
        intent.putExtra("path", this.todos.get(position).getPath());
        startActivity(intent);
    }
}