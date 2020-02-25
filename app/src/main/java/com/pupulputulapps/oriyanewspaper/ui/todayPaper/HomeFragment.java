package com.pupulputulapps.oriyanewspaper.ui.todayPaper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.pupulputulapps.oriyanewspaper.Adapters.NewsPaperAdapter;
import com.pupulputulapps.oriyanewspaper.Models.LatestNewsModel;
import com.pupulputulapps.oriyanewspaper.Models.NewsPaperWebModel;
import com.pupulputulapps.oriyanewspaper.Models.VideosModel;
import com.pupulputulapps.oriyanewspaper.NewsAdvancedWebViewActivity;
import com.pupulputulapps.oriyanewspaper.R;
import com.pupulputulapps.oriyanewspaper.Utils.ClickListenerInterface;
import com.pupulputulapps.oriyanewspaper.Utils.CustomDialogClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class HomeFragment extends Fragment implements ClickListenerInterface {

    private static final String TAG = "HomeFragment TAGG";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private NewsPaperAdapter newsPaperAdapter;
    private InterstitialAd interstitialAd;
    private HomeViewModel homeViewModel;

    private ArrayList<NewsPaperWebModel> newsListData = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_todaypaper, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
        progressBar = root.findViewById(R.id.progress_bar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();
        newsPaperAdapter = new NewsPaperAdapter(this);

        homeViewModel.getNewsPaperModelData("").observe(getViewLifecycleOwner(), new Observer<List<NewsPaperWebModel>>() {
            @Override
            public void onChanged(List<NewsPaperWebModel> newsPaperModels) {

                progressBar.setVisibility(View.GONE);

                newsListData = new ArrayList<>();
                newsListData.addAll(newsPaperModels);
                recyclerView.setAdapter(newsPaperAdapter);
                newsPaperAdapter.loadNewsPapers(newsListData);

            }
        });

        AudienceNetworkAds.initialize(Objects.requireNonNull(getContext()));
        interstitialAd = new InterstitialAd(getContext(), getString(R.string.FAN_Placement_SearchResult_HomeFragment));

//        interstitialAd.setAdListener(new InterstitialAdExtendedListener() {
//            @Override
//            public void onInterstitialActivityDestroyed() {
//
//            }
//
//            @Override
//            public void onInterstitialDisplayed(Ad ad) {
//
//            }
//
//            @Override
//            public void onInterstitialDismissed(Ad ad) {
//
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                Log.d(TAG, "onError: " + adError.getErrorMessage());
//
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//
//                Log.d(TAG, "onAdLoaded: " + ad.getPlacementId());
//
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//
//            @Override
//            public void onRewardedAdCompleted() {
//
//            }
//
//            @Override
//            public void onRewardedAdServerSucceeded() {
//
//            }
//
//            @Override
//            public void onRewardedAdServerFailed() {
//
//            }
//        });

        interstitialAd.loadAd();


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onNewsPaperClick(final NewsPaperWebModel newsPaperModel) {

        if (interstitialAd != null && interstitialAd.isAdLoaded()) {
            interstitialAd.show();

        }

        loadPapers(newsPaperModel);
    }

    private void loadPapers(NewsPaperWebModel newsPaperModel) {
        String ePaperLink = newsPaperModel.getEpaper_link();
        String websiteLink = newsPaperModel.getWebsite_link();
        String newsPaperName = newsPaperModel.getName();

        if (ePaperLink.isEmpty()) {
            openPaper(websiteLink, newsPaperName);
        } else if (websiteLink.isEmpty()) {
            openPaper(ePaperLink, newsPaperName);
        } else {
            newsSelectionDialog(ePaperLink, websiteLink, newsPaperName);
        }
    }

    private void newsSelectionDialog(final String ePaper, final String website, final String paperName) {
        final CustomDialogClass cdd = new CustomDialogClass(getActivity());
        cdd.show();
        Window window = cdd.getWindow();
        Objects.requireNonNull(window).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        cdd.ePaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPaper(ePaper, paperName);
                //   Log.d(TAG, "onClick: Epaper Clicked");
                cdd.dismiss();

            }
        });

        cdd.website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPaper(website, paperName);
                //  Log.d(TAG, "onClick: Website clicked");
                cdd.dismiss();
            }
        });
    }

    private void openPaper(String paperLink, String paperName) {

        if (!paperLink.isEmpty()) {

            Intent intent = new Intent(getActivity(), NewsAdvancedWebViewActivity.class);
            intent.putExtra("url", paperLink);
            intent.putExtra("paper_name", paperName);
            startActivity(intent);
        } else {
            Toasty.error(Objects.requireNonNull(getContext()), "No Newspaper found, Please search again with different term", Toast.LENGTH_LONG, true).show();

        }

    }

    @Override
    public void onVideoItemClick(ArrayList<VideosModel> videoItemClassArrayList, int clickedPosition) {
        //for video fragment
    }

    @Override
    public void onRssNewsItemClick(LatestNewsModel article) {
        //for latest news fragment
    }

    public void getSearchedNewsPaper(String searchQuery) {

        homeViewModel.getNewsPaperModelData(searchQuery).observe(getViewLifecycleOwner(), new Observer<List<NewsPaperWebModel>>() {
            @Override
            public void onChanged(List<NewsPaperWebModel> newsPaperModels) {

                progressBar.setVisibility(View.GONE);
                newsListData.clear();
                newsListData.addAll(newsPaperModels);
                recyclerView.setAdapter(newsPaperAdapter);
                newsPaperAdapter.loadNewsPapers(newsListData);
                newsPaperAdapter.notifyDataSetChanged();

            }
        });

    }


}