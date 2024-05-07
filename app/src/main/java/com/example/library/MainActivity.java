package com.example.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
EditText email;
EditText password;
Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email=findViewById(R.id.editTextTextEmailAddress);
        password=findViewById(R.id.editTextTextPassword);
        b1=findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                if (!userEmail.isEmpty() && !userPassword.isEmpty()) {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();


                    db.collection("user")
                            .whereEqualTo("email", userEmail)
                            .whereEqualTo("password", userPassword)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                                        if (!queryDocumentSnapshots.isEmpty()) {

                                            startActivity(new Intent(MainActivity.this, BookMenuActivity.class));
                                        } else {

                                            Toast.makeText(MainActivity.this, "Неверный email или пароль", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
//                                        // Ошибка при выполнении запроса к базе данных
//                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                        Toast.makeText(MainActivity.this, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void onRegister(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }
    public void onLogin1(View view){
//        startActivity(new Intent(this, BookMenuActivity.class));
    }

}