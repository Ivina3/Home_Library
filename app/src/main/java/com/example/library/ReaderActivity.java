package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ReaderActivity extends AppCompatActivity {

    private static final String TAG = "ReaderActivity";

    private ImageView imageView;
    private TextView authorTextView;
    private TextView yearTextView;
    private TextView descriptionTextView;
    private Button readButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        imageView = findViewById(R.id.readerImageView);
        authorTextView = findViewById(R.id.readerAuthorTextView);
        yearTextView = findViewById(R.id.readerYearTextView);
        descriptionTextView = findViewById(R.id.readerDescriptionTextView);
        readButton = findViewById(R.id.readButton);

        // Получаем данные из Intent
        String author = getIntent().getStringExtra("author");
        String year = getIntent().getStringExtra("year");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String description = getIntent().getStringExtra("description");

        if (author != null && year != null && imageUrl != null && description != null) {
            Log.d(TAG, "Author: " + author);
            Log.d(TAG, "Year: " + year);
            Log.d(TAG, "ImageUrl: " + imageUrl);
            Log.d(TAG, "Description: " + description);

            authorTextView.setText(author);
            yearTextView.setText(year);
            descriptionTextView.setText(description);

            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);
        } else {
            Log.e(TAG, "Missing data in intent");
        }
    }
}
