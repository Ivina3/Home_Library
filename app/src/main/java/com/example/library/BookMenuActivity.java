package com.example.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookMenuActivity extends AppCompatActivity {

    private static final String TAG = "BookMenuActivity";

    private FirebaseFirestore db;
    private List<Book> bookList;
    private BookListAdapter adapter;
    private boolean isAdmin;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private EditText searchEditText;
    private Button searchButton;
    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_menu);

        // Получаем данные из Intent
        Intent intent = getIntent();
        isAdmin = intent.getBooleanExtra("isAdmin", false);
        Log.d(TAG, "isAdmin: " + isAdmin);

        // Показать приветственное сообщение
        showWelcomeMessage(isAdmin);

        db = FirebaseFirestore.getInstance();
        bookList = new ArrayList<>();
        adapter = new BookListAdapter(this, R.layout.custom_list_item, bookList);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        settingsButton = findViewById(R.id.settingsButton);

        if (isAdmin) {
            settingsButton.setVisibility(View.VISIBLE);
            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSettingsDialog();
                }
            });
        } else {
            settingsButton.setVisibility(View.GONE);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim();
                fetchBooksFromFirestore(query);
            }
        });

        fetchBooksFromFirestore(null);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        setupDrawerContent(navigationView);
    }

    private void showWelcomeMessage(boolean isAdmin) {
        String role = isAdmin ? "admin" : "читатель";
        Toast.makeText(this, "Добро пожаловать! Ваша роль: " + role, Toast.LENGTH_LONG).show();
    }

    private void fetchBooksFromFirestore(String query) {
        Log.d(TAG, "Fetching books from Firestore...");
        Query booksQuery = db.collection("book");
        if (query != null && !query.isEmpty()) {
            booksQuery = booksQuery.whereEqualTo("title", query);
        }

        booksQuery.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Firestore task successful.");
                            bookList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Book book = document.toObject(Book.class);
                                bookList.add(book);
                                Log.d(TAG, "Book added: " + book.getTitle() + ", " + book.getAuthor() + ", " + book.getYear());
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

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem item) {
        Intent intent = new Intent(this, DetailsActivity.class);

        String title = item.getTitle().toString();
        Log.d(TAG, "Selected item title: " + title);

        if (title.equals("Инструкции")) {
            intent.putExtra(DetailsActivity.EXTRA_FRAGMENT_TYPE, "instructions");
        } else if (title.equals("Об авторе")) {
            intent.putExtra(DetailsActivity.EXTRA_FRAGMENT_TYPE, "about_author");
        } else if (title.equals("О приложении")) {
            intent.putExtra(DetailsActivity.EXTRA_FRAGMENT_TYPE, "about_app");
        }

        startActivity(intent);


        drawerLayout.closeDrawers();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Настройки")
                .setItems(new String[]{"Удалить книгу", "Добавить книгу", "Отмена"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Удалить книгу
                                showDeleteBookDialog();
                                break;
                            case 1: // Добавить книгу
                                startActivity(new Intent(BookMenuActivity.this, AddBookActivity.class));
                                break;
                            case 2: // Отмена
                                dialog.dismiss();
                                break;
                        }
                    }
                })
                .create()
                .show();
    }

    private void showDeleteBookDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удалить книгу");

        final EditText input = new EditText(this);
        input.setHint("Название книги");
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String bookTitle = input.getText().toString().trim();
                deleteBookFromFirestore(bookTitle);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void deleteBookFromFirestore(String bookTitle) {
        db.collection("book")
                .whereEqualTo("title", bookTitle)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                            Toast.makeText(BookMenuActivity.this, "Книга удалена", Toast.LENGTH_SHORT).show();
                            fetchBooksFromFirestore(null); // Обновить список книг
                        } else {
                            Toast.makeText(BookMenuActivity.this, "Книга не найдена", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
