package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);


        Button editPrefaceButton = findViewById(R.id.editPrefaceButton);
        editPrefaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открываем новую активность для редактирования предисловия
                Intent intent = new Intent(AdminActivity.this, EditPrefaceActivity.class);
                startActivity(intent);
            }
        });

    }
}