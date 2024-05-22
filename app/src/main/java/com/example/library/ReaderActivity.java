package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ReaderActivity extends AppCompatActivity {

    private static final String TAG = "ReaderActivity";

    private ImageView imageView;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView yearTextView;
    private TextView descriptionTextView;
    private TextView bookTextView;
    private Button readButton;
    private String bookTextUrl;  // Добавлено для хранения URL текста книги

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        imageView = findViewById(R.id.readerImageView);
        titleTextView = findViewById(R.id.readerTitleTextView);
        authorTextView = findViewById(R.id.readerAuthorTextView);
        yearTextView = findViewById(R.id.readerYearTextView);
        descriptionTextView = findViewById(R.id.readerDescriptionTextView);
//        bookTextView = findViewById(R.id.readerBookTextView);
        readButton = findViewById(R.id.readButton);

        // Получаем данные из Intent
        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String year = getIntent().getStringExtra("year");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String description = getIntent().getStringExtra("description");
        bookTextUrl = getIntent().getStringExtra("bookTextUrl");

        if (title != null && author != null && year != null && imageUrl != null && description != null && bookTextUrl != null) {
            Log.d(TAG, "Title: " + title);
            Log.d(TAG, "Author: " + author);
            Log.d(TAG, "Year: " + year);
            Log.d(TAG, "ImageUrl: " + imageUrl);
            Log.d(TAG, "Description: " + description);
            Log.d(TAG, "BookTextUrl: " + bookTextUrl);

            titleTextView.setText(title);
            authorTextView.setText(author);
            yearTextView.setText(year);
            descriptionTextView.setText(description);

            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);

            readButton.setOnClickListener(v -> {
                Intent intent = new Intent(ReaderActivity.this, ReadBookActivity.class);
                intent.putExtra("bookTextUrl", bookTextUrl);
                startActivity(intent);
            });
        } else {
            Log.e(TAG, "Missing data in intent");
        }
    }
}
