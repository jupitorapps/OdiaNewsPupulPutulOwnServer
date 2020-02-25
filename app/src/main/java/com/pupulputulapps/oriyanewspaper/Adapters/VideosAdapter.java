package com.pupulputulapps.oriyanewspaper.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pupulputulapps.oriyanewspaper.Models.VideosModel;
import com.pupulputulapps.oriyanewspaper.R;
import com.pupulputulapps.oriyanewspaper.Utils.ClickListenerInterface;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {

    private final ClickListenerInterface clickListener;


    private static final String TAG = "VideosAdapter TAGG";
    private final ArrayList<VideosModel> videosList = new ArrayList<>();

    public VideosAdapter(ClickListenerInterface clickListener) {
        this.clickListener = clickListener;
    }

    public void loadVideos(ArrayList<VideosModel> data) {

        if (data != null){
            this.videosList.addAll(data);
            notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public VideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.video_item, parent, false);
        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosViewHolder holder, final int position) {
        VideosModel currentVideoItem = videosList.get(position);
        final String thubnailUrl = "https://i.ytimg.com/vi/" + currentVideoItem.getYt_video_id() + "/0.jpg";
        String publishedAt = currentVideoItem.getPub_date();

        holder.titleTextView.setText(currentVideoItem.getTitle());
        holder.publishedAtTextView.setText(publishedAt);
        holder.channelNameTextView.setText(currentVideoItem.getSource_channel_name());

        Picasso.get()
                .load(thubnailUrl)
                .placeholder(R.drawable.placeholder_video)
                .error(R.drawable.placeholder_video)
                .into(holder.thumbnailsImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        if (videosList != null) {
            return videosList.size();
        }
        return 0;
    }


    class VideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView thumbnailsImageView;
        private final TextView titleTextView;
        private final TextView publishedAtTextView;
        private final TextView channelNameTextView;

        VideosViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailsImageView = itemView.findViewById(R.id.video_thumbnail);
            titleTextView = itemView.findViewById(R.id.video_title);
            publishedAtTextView = itemView.findViewById(R.id.publish_date);
            channelNameTextView = itemView.findViewById(R.id.channel_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            clickListener.onVideoItemClick(videosList,adapterPosition);

        }
    }


}
