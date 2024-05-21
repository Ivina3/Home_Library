package com.example.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button y1;
    Switch y2;
    String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        email=findViewById(R.id.editTextTextEmailAddress2);
        password=findViewById(R.id.editTextTextPassword2);
        y1=findViewById(R.id.button3);
        y2=findViewById(R.id.switch1);

        y1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                if (!userEmail.isEmpty() && !userPassword.isEmpty()) {
                    if (y2.isChecked()) {
                        userType = "admin";
                    } else {
                        userType = "reader";
                    }
                    Map<String, Object> user = new HashMap<>();
                    user.put("email", userEmail);
                    user.put("password", userPassword);
                    user.put("role", userType);

                    // Получаем экземпляр Firestore и добавляем данные пользователя в коллекцию "users"
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("user")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

//                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

//                                    Log.w(TAG, "Error adding document", e);
                                    Toast.makeText(RegisterActivity.this, "Пожалуйста, попробуйте еще", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {

                    Toast.makeText(RegisterActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();


            }
        });

    }
//    public void saveUserToDatabase(String email, String password, boolean isAdmin) {
//        // Получаем ссылку на вашу базу данных Firebase
//        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
//
//        // Генерируем уникальный идентификатор пользователя
//        String userId = usersRef.push().getKey();
//
//        // Создаем объект пользователя
//        User user = new User(email, password, isAdmin);
//
//        // Сохраняем пользователя в базу данных
//        usersRef.child(userId).setValue(user);
//    }

    public void onLogin(View view){


    }

}