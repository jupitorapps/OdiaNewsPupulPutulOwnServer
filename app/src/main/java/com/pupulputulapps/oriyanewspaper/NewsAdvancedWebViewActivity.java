package com.pupulputulapps.oriyanewspaper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import im.delight.android.webview.AdvancedWebView;

public class NewsAdvancedWebViewActivity extends AppCompatActivity implements AdvancedWebView.Listener{

    private AdvancedWebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_advanced_web_view);

        FloatingActionButton fab = findViewById(R.id.fab_share);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.getOriginalUrl() !=null && !webView.getOriginalUrl().isEmpty()){

                    final Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT,webView.getOriginalUrl());
                    startActivity(Intent.createChooser(intent,"Select App to Share"));

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
        String paperName = intent.getStringExtra("paper_name");

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);

        super.onBackPressed();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
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
        webView.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

        if (!progressBar.isShown()){
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
        if (!webView.onBackPressed()){
            return;
        }
        super.onBackPressed();
    }
}
