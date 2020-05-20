package com.swap.newsdesk.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swap.newsdesk.activities.NewsDetails;
import com.swap.newsdesk.R;
import com.swap.newsdesk.models.NewsDataModel;

import java.util.List;

public class NewsDataAdapter extends RecyclerView.Adapter<NewsDataAdapter.HeadlineHolder> {

    private Context context;
    private List<NewsDataModel> headlineModelList;

    public NewsDataAdapter(Context context, List<NewsDataModel> headlineModelList) {
        this.context = context;
        this.headlineModelList = headlineModelList;
    }

    @NonNull
    @Override
    public HeadlineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_layout, parent, false);
        return new HeadlineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HeadlineHolder holder, int position) {
        final NewsDataModel model = headlineModelList.get(position);

        Picasso.get().load(model.getImage_url()).error(R.drawable.ic_broken_image).into(holder.imageView);
        holder.textView.setText(model.getTitle());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetails.class);
                /*intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("NEWS_IMAGE_PATH", model.getImage_url());
                intent.putExtra("NEWS_PUBLISHED_DATE", model.getPublished_date());
                intent.putExtra("NEWS_DESC", model.getDescription());*/
                intent.putExtra("NEWS_TITLE", model.getTitle());
                intent.putExtra("NEWS_URL", model.getUrl());
//                intent.putExtra("NEWS_CONTENT", model.getContent());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return headlineModelList.size();
    }

    public void clear() {
        headlineModelList.clear();
        notifyDataSetChanged();
    }

    public class HeadlineHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;
        protected CardView cardView;

        public HeadlineHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.news_image);
            this.textView = itemView.findViewById(R.id.news_title);
            this.cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
