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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.meowbeos.studentsupport.R;
import com.meowbeos.studentsupport.activity.news.NewsDetailActivity;
import com.meowbeos.studentsupport.model.DetailNews;
import com.squareup.picasso.Picasso;

public class RelatedNewsAdapter extends RecyclerView.Adapter<RelatedNewsAdapter.ViewHolder> {

    Context context;
    DetailNews detailNews;


    public RelatedNewsAdapter(Context context, DetailNews detailNews) {
        this.context = context;
        this.detailNews = detailNews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_related_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(detailNews.getSimilarNews().get(position).getTitleNews());
        Picasso.get().load(detailNews.getSimilarNews().get(position).getUrlBanner()).error(R.drawable.error).into(holder.img);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                Intent intent = new Intent(context, NewsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idNews", String.valueOf(detailNews.getSimilarNews().get(position).getIdNews()));
                intent.putExtra("DATA", bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailNews.getSimilarNews().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView img;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txtTitleNewsRelated);
            img = itemView.findViewById(R.id.imgRelatedNews);
            linearLayout = itemView.findViewById(R.id.linear_layout_RelatedNews);
        }
    }
}
