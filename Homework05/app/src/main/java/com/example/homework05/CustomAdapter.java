package com.example.homework05;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<News> {

    private final Context context;
    private final List<News> news;


    CustomAdapter(Context context, List<News> news) {
        super(context, R.layout.row_layout, news);
        this.context = context;
        this.news = news;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        ImageView imageView = rowView.findViewById(R.id.image_view);
        if (news.get(position).getUrlToImage() != null || news.get(position).getUrlToImage().isEmpty()) {
            Picasso.get().load(news.get(position).getUrlToImage()).into(imageView);
        }
        TextView title = rowView.findViewById(R.id.title);
        TextView author = rowView.findViewById(R.id.author);
        TextView publishedAt = rowView.findViewById(R.id.published_at);
        title.setText(news.get(position).getTitle());
        publishedAt.setText(news.get(position).getPublishedAt());
        if(news.get(position).getAuthor() != "null" && !news.get(position).getAuthor().isEmpty()) {
            author.setText(news.get(position).getAuthor());
        }
        return rowView;
    }
}


