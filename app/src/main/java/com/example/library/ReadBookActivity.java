package com.example.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.StandardCharsets;

public class ReadBookActivity extends AppCompatActivity {

    private static final String TAG = "ReadBookActivity";
    private TextView bookTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);

        bookTextView = findViewById(R.id.bookTextView);

        // Получаем URL текста книги из Intent
        String bookTextUrl = getIntent().getStringExtra("bookTextUrl");
        if (bookTextUrl != null) {
            loadBookText(bookTextUrl);
        } else {
            Toast.makeText(this, "Ошибка загрузки текста книги", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "bookTextUrl is missing in intent");
        }
    }

    private void loadBookText(String bookTextUrl) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(bookTextUrl);

        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    String bookText = new String(bytes, StandardCharsets.UTF_8);  // Используем UTF-8
                    bookTextView.setText(bookText);
                } catch (Exception e) {
                    Log.e(TAG, "Error reading book text", e);
                    Toast.makeText(ReadBookActivity.this, "Ошибка чтения текста книги", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ReadBookActivity.this, "Ошибка загрузки файла", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error loading book text", exception);
            }
        });
    }
}
