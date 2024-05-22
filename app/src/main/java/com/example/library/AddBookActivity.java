package com.example.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddBookActivity extends AppCompatActivity {

    private static final String TAG = "AddBookActivity";
    private static final int PICK_FILE_REQUEST = 1;

    private EditText titleEditText;
    private EditText authorEditText;
    private EditText yearEditText;
    private EditText imageUrlEditText;
    private EditText descriptionEditText;
    private Button addButton;
    private Button uploadFileButton;
    private Uri fileUri;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;

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
        uploadFileButton = findViewById(R.id.uploadFileButton);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        uploadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileUri != null) {
                    uploadFileAndAddBook();
                } else {
                    Toast.makeText(AddBookActivity.this, "Пожалуйста, загрузите файл книги", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*"); // Можно выбрать любой тип файла, если необходимо
        startActivityForResult(Intent.createChooser(intent, "Выберите файл"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            Toast.makeText(this, "Файл выбран: " + fileUri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFileAndAddBook() {
        final String title = titleEditText.getText().toString().trim();
        final String author = authorEditText.getText().toString().trim();
        final String year = yearEditText.getText().toString().trim();
        final String imageUrl = imageUrlEditText.getText().toString().trim();
        final String description = descriptionEditText.getText().toString().trim();

        if (!title.isEmpty() && !author.isEmpty() && !year.isEmpty() && !imageUrl.isEmpty() && !description.isEmpty()) {
            final StorageReference fileRef = storageReference.child("books/" + System.currentTimeMillis() + "_" + fileUri.getLastPathSegment());

            fileRef.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String fileUrl = uri.toString();

                                    Map<String, Object> book = new HashMap<>();
                                    book.put("title", title);
                                    book.put("author", author);
                                    book.put("year", year);
                                    book.put("imageUrl", imageUrl);
                                    book.put("description", description);
                                    book.put("bookTextUrl", fileUrl);  // Сохраняем URL файла

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
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {  // Исправлено
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(AddBookActivity.this, "Ошибка загрузки файла", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }
}
