package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Detailview extends AppCompatActivity {
    private TextView textView;
    private AlertDialog alertDialog;
    private EditText editText;
    private ImageView imageView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailview);
        setActivity();
        setAlertDialog();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(textView.getText());
                alertDialog.show();
            }
        });
    }

    public void setActivity() {
        this.imageView = (ImageView) findViewById(R.id.imageView);
        String path = getIntent().getExtras().getString("path");
        // if path exist
        if (!path.equals("")){
            StorageReference pathReference = storageRef.child(path);
            try {
                File localFile = File.createTempFile("images", "jpg");
                pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        imageView.setImageBitmap(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        System.out.println("Failure downloading image");
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        this.button = (Button) findViewById(R.id.button);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageView.setImageURI(data.getData());
            uploadImageToStorage();
        }
        else {
            System.out.println("Failed to set imageview");
        }
    }

    public void uploadImageToStorage(){
        String path = System.currentTimeMillis()+".jpg";
        StorageReference imageRef = storageRef.child(path);
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("Failed to upload");
            }
        }).addOnSuccessListener(e -> {
            String title = getIntent().getExtras().getString("title");
            this.db.collection("todos").document(title).update("path", path);
        });
    }

    public void saveToFirebase(){
        String title = getIntent().getExtras().getString("title");
        this.db.collection("todos").document(title).update("title", title, "text", this.textView.getText().toString());
    }
}