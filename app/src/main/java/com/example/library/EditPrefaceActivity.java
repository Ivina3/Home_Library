package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditPrefaceActivity extends AppCompatActivity {

    private EditText editTextPreface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_preface);


        editTextPreface = findViewById(R.id.editTextPreface);


        String currentPreface = getCurrentPreface();
        editTextPreface.setText(currentPreface);
    }


    private String getCurrentPreface() {

        return "Текст предисловия книги";
    }


    public void savePreface(View view) {

        String editedPreface = editTextPreface.getText().toString();

        saveEditedPrefaceToDatabase(editedPreface);
    }


    private void saveEditedPrefaceToDatabase(String editedPreface) {
        // Здесь должна быть ваша логика сохранения изменений в предисловии, например, в базе данных
    }
}
