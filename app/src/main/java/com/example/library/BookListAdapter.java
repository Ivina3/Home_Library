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

import com.bumptech.glide.Glide;

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
        Book currentBook = getItem(position);
        if (currentBook == null) return convertView;

        String title = currentBook.getTitle();
        String author = currentBook.getAuthor();
        String year = currentBook.getYear();
        String imageUrl = currentBook.getImageUrl();

        // Создание объекта ViewHolder
        ViewHolder viewHolder;

        // Проверка возможности использовать существующий объект View
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.titleTextView = convertView.findViewById(R.id.titleTextView);
            viewHolder.authorTextView = convertView.findViewById(R.id.authorTextView);
            viewHolder.yearTextView = convertView.findViewById(R.id.yearTextView);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Заполнение данных в объект ViewHolder
        viewHolder.titleTextView.setText(title);
        viewHolder.authorTextView.setText(author);
        viewHolder.yearTextView.setText(year);
        Glide.with(mContext).load(imageUrl).into(viewHolder.imageView);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReaderActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("author", author);
                intent.putExtra("year", year);
                intent.putExtra("imageUrl", imageUrl);
                intent.putExtra("description", currentBook.getDescription());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        TextView yearTextView;
        ImageView imageView;
    }
}
