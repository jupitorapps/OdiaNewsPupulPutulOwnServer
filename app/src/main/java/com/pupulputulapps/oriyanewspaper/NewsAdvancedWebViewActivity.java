package com.pupulputulapps.oriyanewspaper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.pupulputulapps.oriyanewspaper.Utils.ApiClientWeb;
import com.pupulputulapps.oriyanewspaper.Utils.ApiServiceWeb;
import com.pupulputulapps.oriyanewspaper.Utils.Utils;

import java.util.Objects;

import im.delight.android.webview.AdvancedWebView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsAdvancedWebViewActivity extends AppCompatActivity implements AdvancedWebView.Listener {

    private AdvancedWebView webView;
    private ProgressBar progressBar;
    private int source_news_id;
    private Boolean isItFromNewsPapersFragment = true;
    private static final String TAG = "WebView TAGG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_advanced_web_view);

        FloatingActionButton fab = findViewById(R.id.fab_share);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.getOriginalUrl() != null && !webView.getOriginalUrl().isEmpty()) {

                    final Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, webView.getOriginalUrl());
                    startActivity(Intent.createChooser(intent, "Select App to Share"));

                } else {
                    Snackbar.make(view, getString(R.string.share_error), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

            }
        });


        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBar);

        webView.setListener(this, this);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        source_news_id = intent.getIntExtra("news_source_id", 0);
        String paperName = intent.getStringExtra("paper_name");
        String sourceFragment = intent.getStringExtra("source");
        if (sourceFragment != null && !sourceFragment.contains("NewsPaperFragment")) {
            isItFromNewsPapersFragment = false;
        }

        //mWebView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setVerticalFadingEdgeEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);

        webView.loadUrl(url);
        progressBar.setVisibility(View.VISIBLE);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(paperName);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_webview_menu, menu);

        if (!isItFromNewsPapersFragment) {
            menu.findItem(R.id.action_fav).setVisible(false);
        }
        checkFavNewsPaper(Utils.getUserId(getApplicationContext()),source_news_id,menu.findItem(R.id.action_fav));
      //  updateFavInServer(Utils.getUserId(getApplicationContext()), source_news_id,menu.findItem(R.id.action_favourite));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.action_fav) {
            updateFavInServer(Utils.getUserId(getApplicationContext()), source_news_id,item);

//            if (item.getIcon().getConstantState() == getResources().getDrawable(R.drawable.star_blank).getConstantState()) {
//                item.setIcon(R.drawable.star_fill);
//            } else {
//                item.setIcon(R.drawable.star_blank);
//            }

        } else {
            super.onBackPressed();
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        }

        return false;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        webView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        webView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

        if (!progressBar.isShown()) {
            progressBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageFinished(String url) {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    @Override
    public void onBackPressed() {
        if (!webView.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }


    private static void updateFavInServer(String user_id, int news_paper_id, final MenuItem item) {

        ApiServiceWeb apiService = ApiClientWeb.getRetrofit().create(ApiServiceWeb.class);
        Call<String> call = apiService.updateFav("news_odia", user_id, news_paper_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.body().contains("Created")){
                    //fav created
                    item.setIcon(R.drawable.star_fill);

                } else {
                    //fav deleted
                    item.setIcon(R.drawable.star_blank);

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });


    }


    private static void checkFavNewsPaper(String user_id, int news_paper_id, final MenuItem item){
        ApiServiceWeb apiService = ApiClientWeb.getRetrofit().create(ApiServiceWeb.class);
        Call<String> call = apiService.checkFavNewsPaper("news_odia", user_id, news_paper_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {

                    Log.d(TAG, "onResponse: "+response.body());
                    if (response.body().contains("0")){
                        item.setIcon(R.drawable.star_fill);
                    } else {
                        item.setIcon(R.drawable.star_blank);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });


    }


}
