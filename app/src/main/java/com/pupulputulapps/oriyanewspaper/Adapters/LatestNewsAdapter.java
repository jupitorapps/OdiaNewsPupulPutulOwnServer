package com.pupulputulapps.oriyanewspaper.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pupulputulapps.oriyanewspaper.Models.LatestNewsModel;
import com.squareup.picasso.Picasso;
import com.pupulputulapps.oriyanewspaper.R;
import com.pupulputulapps.oriyanewspaper.Utils.ClickListenerInterface;

import java.util.ArrayList;
import java.util.Objects;

public class LatestNewsAdapter extends RecyclerView.Adapter<LatestNewsAdapter.ViewHolder> {

    private final ClickListenerInterface clickListener;

    public LatestNewsAdapter(ClickListenerInterface clickListener) {
        this.clickListener = clickListener;
    }

    private final ArrayList<LatestNewsModel> articleArrayList = new ArrayList<>();

    public void loadArticlesFromRssFeed(ArrayList<LatestNewsModel> data) {
        if (!articleArrayList.isEmpty()){
            articleArrayList.clear();
        }
        this.articleArrayList.addAll(data);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.news_rss_items, parent, false);
        return new LatestNewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LatestNewsModel currentArticle = articleArrayList.get(position);
        holder.title.setText(currentArticle.getTitle());
        holder.description.setText(currentArticle.getDescription());
        String pubDate = currentArticle.getPub_date().substring(6,currentArticle.getPub_date().length()-1);
        holder.pubDate.setText(pubDate);

        String newsSource = currentArticle.getNews_source();

        holder.newsSource.setText(newsSource);

        String image_link = currentArticle.getImage_link();
        if (image_link.isEmpty()){
            image_link = "no_image_found_in_db";
        }


        Picasso.get()
                .load(image_link)
                .placeholder(R.drawable.placeholder_video)
                .error(R.drawable.error)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView title;
        final TextView pubDate;
        final ImageView image;
        final TextView description;
        final TextView newsSource;

        ViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.rss_news_title);
            pubDate = itemView.findViewById(R.id.rss_news_publish_time);
            image = itemView.findViewById(R.id.rss_news_thumbnail);
            description = itemView.findViewById(R.id.rss_news_description);

            newsSource = itemView.findViewById(R.id.rss_news_author);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            LatestNewsModel article = articleArrayList.get(adapterPosition);

            clickListener.onRssNewsItemClick(article);
        }
    }
}
