package com.example.library;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AboutAuthorFragment extends Fragment {

    public AboutAuthorFragment() {
        // Обязательный пустой конструктор
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_author, container, false);

        ImageView authorImageView = view.findViewById(R.id.authorImageView);
        TextView authorNameTextView = view.findViewById(R.id.authorNameTextView);
        TextView authorGroupTextView = view.findViewById(R.id.authorGroupTextView);
        TextView authorYearTextView = view.findViewById(R.id.authorYearTextView);
        TextView authorEmailTextView = view.findViewById(R.id.authorEmailTextView);

        // Установите данные о разработчике
        authorNameTextView.setText("Николай Гиль");
        authorGroupTextView.setText("Группа: ИКБО-02");
        authorYearTextView.setText("Год разработки: 2024");
        authorEmailTextView.setText("email@example.com");

        // Используйте Glide для загрузки изображения
        Glide.with(this)
                .load("https://funik.ru/wp-content/uploads/2018/10/17478da42271207e1d86.jpg") // Замените на URL вашей аватарки

                .into(authorImageView);

        return view;
    }
}
