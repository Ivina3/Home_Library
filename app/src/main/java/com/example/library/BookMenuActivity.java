package com.example.library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BookMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_menu);
        String[] authors = {"Author 1", "Author 2", "Author 3"};
        String[] years = {"Year 1", "Year 2", "Year 3"};
        int[] images = {R.drawable.mm, R.drawable.mm, R.drawable.mm};

        // Создание списка элементов
        List<Book> bookList = new ArrayList<>();
        for (int i = 0; i < authors.length; i++) {
            bookList.add(new Book(authors[i], years[i], images[i]));
        }

        // Создание адаптера
        BookListAdapter adapter = new BookListAdapter(this, R.layout.custom_list_item, bookList);

        // Привязка адаптера к ListView
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}