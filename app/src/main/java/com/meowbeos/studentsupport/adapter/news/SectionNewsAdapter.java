package com.meowbeos.studentsupport.adapter.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meowbeos.studentsupport.R;
import com.meowbeos.studentsupport.model.GeneralNews;
import com.meowbeos.studentsupport.model.News;

import java.util.List;

/**
 * Project name: StudentSupport
 * Created by MeowBeos
 * Date: 12/1/2021
 */

public class SectionNewsAdapter extends RecyclerView.Adapter<SectionNewsAdapter.ViewHolder> {
    private List<GeneralNews> generalNewsList;
    private Context context;
    private ItemNewsAdapter itemAdapter;
    public SectionNewsAdapter(Context context, List<GeneralNews> generalNewsList) {
        this.generalNewsList = generalNewsList;
        this.context = context;
    }
    @NonNull
    @Override
    public SectionNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_section, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GeneralNews section = generalNewsList.get(position);
        holder.bind(section);
    }
    @Override
    public int getItemCount() {
        return generalNewsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionName;
        private RecyclerView itemRecyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.section_item_text_view);
            itemRecyclerView = itemView.findViewById(R.id.item_recycler_view);
        }
        public void bind(GeneralNews generalNews) {
            sectionName.setText(generalNews.getCategory().getNameCategory());
            // RecyclerView for items
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            itemRecyclerView.setLayoutManager(linearLayoutManager);
            itemAdapter = new ItemNewsAdapter(context, generalNews.getListNews());
            itemRecyclerView.setAdapter(itemAdapter);
        }
    }
    public void moveItem(int toSectionPosition, int fromSectionPosition) {
        List<News> toItemsInSection = generalNewsList.get(toSectionPosition).getListNews();
        List<News> fromItemsInSection = generalNewsList.get(fromSectionPosition).getListNews();
        if (fromItemsInSection.size() > 3) {
            toItemsInSection.add(fromItemsInSection.get(3));
            fromItemsInSection.remove(3);
            // update adapter for items in a section
            itemAdapter.notifyDataSetChanged();
            // update adapter for sections
            this.notifyDataSetChanged();
        }
    }
}
