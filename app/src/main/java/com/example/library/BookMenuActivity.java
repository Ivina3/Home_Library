package com.example.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookMenuActivity extends AppCompatActivity {

    private static final String TAG = "BookMenuActivity";
    private FirebaseFirestore db;
    private List<Book> bookList;
    private BookListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_menu);

        db = FirebaseFirestore.getInstance();
        bookList = new ArrayList<>();
        adapter = new BookListAdapter(this, R.layout.custom_list_item, bookList);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        fetchBooksFromFirestore();
    }

    private void fetchBooksFromFirestore() {
        Log.d(TAG, "Fetching books from Firestore...");
        db.collection("book")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Firestore task successful.");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Book book = document.toObject(Book.class);
                                bookList.add(book);
                                Log.d(TAG, "Book added: " + book.getAuthor() + ", " + book.getYear());
                            }
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "Adapter notified.");
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            Toast.makeText(BookMenuActivity.this, "Error fetching data.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
