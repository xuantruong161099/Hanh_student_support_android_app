package com.meowbeos.studentsupport.adapter.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meowbeos.studentsupport.R;
import com.meowbeos.studentsupport.activity.news.NewsDetailActivity;
import com.meowbeos.studentsupport.model.News;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Project name: StudentSupport
 * Created by MeowBeos
 * Date: 12/1/2021
 */

public class ItemNewsAdapter extends RecyclerView.Adapter<ItemNewsAdapter.ViewHolder> {
    List<News> newsList;
    Context context;

    public ItemNewsAdapter(Context context, List<News> newsList) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemNewsAdapter.ViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.bind(news);

        holder.ll_item_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idNews", String.valueOf(news.getIdNews()));
                intent.putExtra("DATA", bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, date;
        private ImageView img;
        private LinearLayout ll_item_news;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_item_news = itemView.findViewById(R.id.ll_item_news);
            title = itemView.findViewById(R.id.txtTitleNews);
            date = itemView.findViewById(R.id.txtDatePostNews);
            img = itemView.findViewById(R.id.imgGeneralNews);
        }

        public void bind(News news) {

            title.setText(news.getTitleNews());
            date.setText(news.getDatePost());
            Picasso.get().load(news.getUrlBanner()).error(R.drawable.error).into(img);
        }
    }
}