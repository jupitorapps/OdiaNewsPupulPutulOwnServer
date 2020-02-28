package com.pupulputulapps.oriyanewspaper.ui.todayPaper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.pupulputulapps.oriyanewspaper.Utils.EndlessRecyclerViewScrollListener;
import com.pupulputulapps.oriyanewspaper.Utils.Utils;

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
    private int offset = 0;

    private ArrayList<NewsPaperWebModel> newsListData = new ArrayList<>();

    private EndlessRecyclerViewScrollListener scrollListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_todaypaper, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
        progressBar = root.findViewById(R.id.progress_bar);
        // setHasOptionsMenu(true);

        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //  StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        newsPaperAdapter = new NewsPaperAdapter(this,getContext());

        getNewsPapers("","");

        AudienceNetworkAds.initialize(Objects.requireNonNull(getContext()));
        interstitialAd = new InterstitialAd(getContext(), getString(R.string.FAN_Placement_SearchResult_HomeFragment));
        interstitialAd.loadAd();

        return root;
    }

    public void getNewsPapers(String searchQuery, final String user_id) {

        homeViewModel.getNewsPaperModelData(searchQuery, user_id).observe(getViewLifecycleOwner(), new Observer<List<NewsPaperWebModel>>() {
            @Override
            public void onChanged(List<NewsPaperWebModel> newsPaperModels) {
                Log.d(TAG, "onChanged: user id at line 98 of Home Fragment: "+user_id);
                progressBar.setVisibility(View.GONE);
                newsListData.clear();
                newsListData.addAll(newsPaperModels);
                recyclerView.setAdapter(newsPaperAdapter);
                newsPaperAdapter.loadNewsPapers(newsListData);
                newsPaperAdapter.notifyDataSetChanged();
            }
        });

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
        int news_paper_id = newsPaperModel.getId();

        if (ePaperLink.isEmpty()) {
            openPaper(websiteLink, newsPaperName, news_paper_id);
        } else if (websiteLink.isEmpty()) {
            openPaper(ePaperLink, newsPaperName, news_paper_id);
        } else {
            newsSelectionDialog(ePaperLink, websiteLink, newsPaperName, news_paper_id);
        }
    }

    private void newsSelectionDialog(final String ePaper, final String website, final String paperName, final int news_paper_id) {
        final CustomDialogClass cdd = new CustomDialogClass(getActivity());
        cdd.show();
        Window window = cdd.getWindow();
        Objects.requireNonNull(window).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        cdd.ePaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPaper(ePaper, paperName,news_paper_id);
                //   Log.d(TAG, "onClick: Epaper Clicked");
                cdd.dismiss();

            }
        });

        cdd.website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPaper(website, paperName,news_paper_id);
                //  Log.d(TAG, "onClick: Website clicked");
                cdd.dismiss();
            }
        });
    }

    private void openPaper(String paperLink, String paperName, int news_paper_id) {

        if (paperLink.isEmpty()) {
            Toasty.error(Objects.requireNonNull(getContext()), "No Newspaper found, showing all newspapers", Toast.LENGTH_LONG, true).show();
            getNewsPapers("","");
            return;
        }
        Intent intent = new Intent(getActivity(), NewsAdvancedWebViewActivity.class);
        intent.putExtra("url", paperLink);
        intent.putExtra("paper_name", paperName);
        intent.putExtra("news_source_id",news_paper_id);
        intent.putExtra("source","NewsPaperFragment");
        startActivity(intent);
    }

    @Override
    public void onVideoItemClick(ArrayList<VideosModel> videoItemClassArrayList, int clickedPosition) {
        //for video fragment
    }

    @Override
    public void onRssNewsItemClick(LatestNewsModel article) {
        //for latest news fragment
    }

}