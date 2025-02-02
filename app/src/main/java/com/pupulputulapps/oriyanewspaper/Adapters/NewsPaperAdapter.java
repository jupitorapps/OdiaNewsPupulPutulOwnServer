package com.pupulputulapps.oriyanewspaper.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pupulputulapps.oriyanewspaper.Models.NewsPaperWebModel;
import com.pupulputulapps.oriyanewspaper.R;
import com.pupulputulapps.oriyanewspaper.Utils.ClickListenerInterface;
import com.pupulputulapps.oriyanewspaper.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsPaperAdapter extends RecyclerView.Adapter<NewsPaperAdapter.NewsPaperViewHolder> {

    private final ClickListenerInterface clickListener;
    private static final String TAG = "NewsPaperAdapter TAGG";
    private Boolean isFavourite = false;
    private Context context;

    private ArrayList<NewsPaperWebModel> newsPaperModelArrayList = new ArrayList<>();

    public NewsPaperAdapter(ClickListenerInterface clickListener, Context context) {
        this.clickListener = clickListener;
        this.context = context;
    }


    public void loadNewsPapers(ArrayList<NewsPaperWebModel> data) {
        if (newsPaperModelArrayList.size() > 0) {
            newsPaperModelArrayList.clear();
            notifyItemRangeRemoved(0, newsPaperModelArrayList.size());
        }
        this.newsPaperModelArrayList.addAll(data);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public NewsPaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.news_paper_item, parent, false);
        return new NewsPaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsPaperViewHolder holder, int position) {

        final NewsPaperWebModel currentNewsPaper = newsPaperModelArrayList.get(position);

        String paper_logo_link = "http://odiacalendar.co.in/news_apps/images/odia/" + currentNewsPaper.getImage_name();
        //   Log.d(TAG, "onBindViewHolder: "+paper_logo_link);
        Picasso.get()
                .load(paper_logo_link)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.paper_logo);

//        holder.fav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: Fav Icon clicked");
//
//                //change icon
//                //save/remove from fav
//
//
//                if ((holder.fav.getDrawable().getConstantState()) == context.getResources().getDrawable(R.drawable.star_blank).getConstantState() ) {
//                    holder.fav.setImageDrawable(context.getResources().getDrawable(R.drawable.star_fill));
//                    Log.d(TAG, "onClick: line 77");
//                } else {
//                    holder.fav.setImageDrawable(context.getResources().getDrawable(R.drawable.star_blank));
//                    Log.d(TAG, "onClick: line 80");
//                }
//
//                Utils.updateFavInServer(Utils.getUserId(context),currentNewsPaper.getId());
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        if (newsPaperModelArrayList != null) {
            return newsPaperModelArrayList.size();
        }

        return 0;
    }

    class NewsPaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView paper_logo;
      //  private final ImageView fav;
        //  private final TextView news_type;


        NewsPaperViewHolder(@NonNull View itemView) {
            super(itemView);
            paper_logo = itemView.findViewById(R.id.paper_logo);
     //       fav = itemView.findViewById(R.id.fav_icon);

            // news_type = itemView.findViewById(R.id.news_type_tv);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            NewsPaperWebModel newsPaperModel = newsPaperModelArrayList.get(adapterPosition);
            clickListener.onNewsPaperClick(newsPaperModel);
        }
    }

}
