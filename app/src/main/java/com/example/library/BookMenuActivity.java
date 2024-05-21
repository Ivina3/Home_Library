package com.example.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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
    private boolean isAdmin;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_menu);

        // Получаем данные из Intent
        Intent intent = getIntent();
        String userId = intent.getStringExtra("user_id");
        isAdmin = intent.getBooleanExtra("isAdmin", false);

        // Показать приветственное сообщение
        showWelcomeMessage(isAdmin);

        db = FirebaseFirestore.getInstance();
        bookList = new ArrayList<>();
        adapter = new BookListAdapter(this, R.layout.custom_list_item, bookList);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        fetchBooksFromFirestore();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        setupDrawerContent(navigationView);
    }

    private void showWelcomeMessage(boolean isAdmin) {
        String role = isAdmin ? "admin" : "читатель";
        Toast.makeText(this, "Добро пожаловать! Ваша роль: " + role, Toast.LENGTH_LONG).show();
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

        // Закрываем навигационное меню
        drawerLayout.closeDrawers();
    }
}
