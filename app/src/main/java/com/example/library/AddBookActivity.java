package com.example.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddBookActivity extends AppCompatActivity {

    private static final String TAG = "AddBookActivity";

    private EditText titleEditText;
    private EditText authorEditText;
    private EditText yearEditText;
    private EditText imageUrlEditText;
    private EditText descriptionEditText;
    private Button addButton;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        titleEditText = findViewById(R.id.titleEditText);
        authorEditText = findViewById(R.id.authorEditText);
        yearEditText = findViewById(R.id.yearEditText);
        imageUrlEditText = findViewById(R.id.imageUrlEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        addButton = findViewById(R.id.addButton);

        db = FirebaseFirestore.getInstance();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookToFirestore();
            }
        });
    }

    private void addBookToFirestore() {
        String title = titleEditText.getText().toString().trim();
        String author = authorEditText.getText().toString().trim();
        String year = yearEditText.getText().toString().trim();
        String imageUrl = imageUrlEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (!title.isEmpty() && !author.isEmpty() && !year.isEmpty() && !imageUrl.isEmpty() && !description.isEmpty()) {
            Map<String, Object> book = new HashMap<>();
            book.put("title", title);
            book.put("author", author);
            book.put("year", year);
            book.put("imageUrl", imageUrl);
            book.put("description", description);

            db.collection("book")
                    .add(book)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddBookActivity.this, "Книга добавлена", Toast.LENGTH_SHORT).show();
                                finish(); // Закрыть активность после успешного добавления книги
                            } else {
                                Toast.makeText(AddBookActivity.this, "Ошибка добавления книги", Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "Error adding book", task.getException());
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }
}
