package com.example.library;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class BookListAdapter extends ArrayAdapter<Book> {

    private Context mContext;
    private int mResource;

    public BookListAdapter(Context context, int resource, List<Book> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Получение данных
        String author = getItem(position).getAuthor();
        String year = getItem(position).getYear();
        int image = getItem(position).getImage();

        // Создание объекта ViewHolder
        ViewHolder viewHolder;

        // Проверка возможности использовать существующий объект View
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.authorTextView = convertView.findViewById(R.id.authorTextView);
            viewHolder.yearTextView = convertView.findViewById(R.id.yearTextView);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Заполнение данных в объект ViewHolder
        viewHolder.authorTextView.setText(author);
        viewHolder.yearTextView.setText(year);
        viewHolder.imageView.setImageResource(image);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // String userRole = getUserRole(); // Здесь нужно получить роль пользователя, например, из базы данных или переменной
                String userRole = "kolya";

                // Переход на другую активность в зависимости от роли пользователя
                if (userRole.equals("admin")) {



                    Intent intent = new Intent(mContext, AdminActivity.class);
                    mContext.startActivity(intent);
                } else {
                    // Переход на активность для читателя
                    Intent intent = new Intent(mContext, ReaderActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });

        return convertView;
    }





    private static class ViewHolder {
        TextView authorTextView;
        TextView yearTextView;
        ImageView imageView;
    }
}
