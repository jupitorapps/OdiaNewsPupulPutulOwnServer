package com.pupulputulapps.oriyanewspaper.ui.latestNews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdExtendedListener;
import com.pupulputulapps.oriyanewspaper.Adapters.LatestNewsAdapter;
import com.pupulputulapps.oriyanewspaper.Models.LatestNewsModel;
import com.pupulputulapps.oriyanewspaper.Models.NewsPaperWebModel;
import com.pupulputulapps.oriyanewspaper.Models.VideosModel;
import com.pupulputulapps.oriyanewspaper.NewsAdvancedWebViewActivity;
import com.pupulputulapps.oriyanewspaper.R;
import com.pupulputulapps.oriyanewspaper.Utils.ClickListenerInterface;

import java.util.ArrayList;
import java.util.List;

public class LatestNewsFragment extends Fragment implements ClickListenerInterface {

    private LatestNewsViewModel latestNewsViewModel;
    private static final String TAG = "LatestNewsFragment TAGG";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LatestNewsAdapter mAdapter;


    private InterstitialAd interstitialAd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        latestNewsViewModel =
                ViewModelProviders.of(this).get(LatestNewsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_latestnews, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
        progressBar = root.findViewById(R.id.progress_bar);


       // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.hasFixedSize();
        mAdapter = new LatestNewsAdapter(this);

        loadNewsFromRss();

        AudienceNetworkAds.initialize(getContext());
        interstitialAd = new InterstitialAd(getContext(), getString(R.string.FAN_Placement_other_fragments));

        interstitialAd.setAdListener(new InterstitialAdExtendedListener() {
            @Override
            public void onInterstitialActivityDestroyed() {

            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d(TAG, "onError: " + adError.getErrorMessage());

            }

            @Override
            public void onAdLoaded(Ad ad) {

                Log.d(TAG, "onAdLoaded: " + ad.getPlacementId());

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onRewardedAdCompleted() {

            }

            @Override
            public void onRewardedAdServerSucceeded() {

            }

            @Override
            public void onRewardedAdServerFailed() {

            }
        });

        interstitialAd.loadAd();

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.findItem(R.id.action_search).setVisible(false);
        Log.d(TAG, "onCreateOptionsMenu: Latest News Fragment");
    }

    private void loadNewsFromRss() {

       // latestNewsViewModel.getLatestNewsModelData("");

        latestNewsViewModel.getLatestNewsModelData("").observe(getViewLifecycleOwner(), new Observer<List<LatestNewsModel>>() {
            @Override
            public void onChanged(List<LatestNewsModel> latestNewsModels) {
                Log.d(TAG, "onChanged: Latest News: " + latestNewsModels.get(0).getTitle());


                ArrayList<LatestNewsModel> articlesArrayList = new ArrayList<>(latestNewsModels);

                recyclerView.setAdapter(mAdapter);
                mAdapter.loadArticlesFromRssFeed(articlesArrayList);
                mAdapter.notifyDataSetChanged();
                Log.d(TAG, "onChanged ArrayList: "+articlesArrayList.get(0).getTitle());
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public void onNewsPaperClick(NewsPaperWebModel newsPaperModel) {
        //to be implemented in NewsPaper Fragment
    }

    @Override
    public void onVideoItemClick(ArrayList<VideosModel> videoItemClassArrayList, int clickedPosition) {
        //for video fragment
    }

    @Override
    public void onRssNewsItemClick(LatestNewsModel article) {

        if (interstitialAd != null && interstitialAd.isAdLoaded()) {
            interstitialAd.show();

        }

        Intent intent = new Intent(getActivity(), NewsAdvancedWebViewActivity.class);
        intent.putExtra("url", article.getNews_link());
        intent.putExtra("paper_name", article.getTitle());
        startActivity(intent);

    }


}