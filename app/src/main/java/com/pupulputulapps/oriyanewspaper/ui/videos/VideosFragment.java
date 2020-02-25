package com.pupulputulapps.oriyanewspaper.ui.videos;

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

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdExtendedListener;
import com.pupulputulapps.oriyanewspaper.Adapters.VideosAdapter;
import com.pupulputulapps.oriyanewspaper.Models.LatestNewsModel;
import com.pupulputulapps.oriyanewspaper.Models.NewsPaperWebModel;
import com.pupulputulapps.oriyanewspaper.Models.RastaModel;
import com.pupulputulapps.oriyanewspaper.Models.VideosModel;
import com.pupulputulapps.oriyanewspaper.R;
import com.pupulputulapps.oriyanewspaper.Utils.ApiClientWeb;
import com.pupulputulapps.oriyanewspaper.Utils.ApiServiceWeb;
import com.pupulputulapps.oriyanewspaper.Utils.ClickListenerInterface;
import com.pupulputulapps.oriyanewspaper.VideoPlayerActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosFragment extends Fragment implements ClickListenerInterface {

    private VideosViewModel videosViewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private VideosAdapter videosAdapter;
    private static final String TAG = "VideosFragment TAGG";

    private ArrayList<RastaModel> rastaList = new ArrayList<>();

    private InterstitialAd interstitialAd;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        videosViewModel =
                ViewModelProviders.of(this).get(VideosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_videos, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
        progressBar = root.findViewById(R.id.progress_bar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();
        videosAdapter = new VideosAdapter(this);

        loadVideosFromServer();

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

        getRasta();

        return root;
    }

    private void loadVideosFromServer() {

        videosViewModel.getVideos("").observe(getViewLifecycleOwner(), new Observer<List<VideosModel>>() {
            @Override
            public void onChanged(List<VideosModel> videosModels) {
                Log.d(TAG, "onChanged: Videos: " + videosModels.get(0).getTitle());

                ArrayList<VideosModel> videosArrayList = new ArrayList<>();
                videosArrayList.addAll(videosModels);

                recyclerView.setAdapter(videosAdapter);
                videosAdapter.loadVideos(videosArrayList);

                progressBar.setVisibility(View.GONE);


            }
        });

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
        Log.d(TAG, "onCreateOptionsMenu: Video Fragment");
    }


    @Override
    public void onNewsPaperClick(NewsPaperWebModel newsPaperModel) {
        //for news paper fragment
    }

    @Override
    public void onVideoItemClick(final ArrayList<VideosModel> videoItemClassArrayList, final int clickedPosition) {
        if (interstitialAd != null && interstitialAd.isAdLoaded()) {
            interstitialAd.show();

        }

        openVideo(videoItemClassArrayList, clickedPosition);


    }

    @Override
    public void onRssNewsItemClick(LatestNewsModel article) {

    }

    private void getRasta() {
        ApiServiceWeb apiService = ApiClientWeb.getRetrofit().create(ApiServiceWeb.class);
        Call<ArrayList<RastaModel>> call = apiService.getRasta("admin_test");
        call.enqueue(new Callback<ArrayList<RastaModel>>() {
            @Override
            public void onResponse(Call<ArrayList<RastaModel>> call, Response<ArrayList<RastaModel>> response) {
                rastaList = response.body();

            }

            @Override
            public void onFailure(Call<ArrayList<RastaModel>> call, Throwable t) {

            }
        });
    }

    private void openVideo(ArrayList<VideosModel> videoItemClassArrayList, int clickedPosition) {
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        intent.putExtra("video_list", videoItemClassArrayList);
        intent.putExtra("clicked_position", clickedPosition);
        intent.putExtra("rasta", rastaList.get(0).getY_key());
        startActivity(intent);
    }
}