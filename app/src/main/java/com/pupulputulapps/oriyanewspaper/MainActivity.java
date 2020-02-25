package com.pupulputulapps.oriyanewspaper;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdExtendedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pupulputulapps.oriyanewspaper.ui.todayPaper.HomeFragment;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity TAGG";
    boolean doubleBackToExitPressedOnce = false;

    private InterstitialAd interstitialAd;
    private String fragmentName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_today_paper, R.id.navigation_latest_news, R.id.navigation_videos)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);



        AudienceNetworkAds.initialize(this);
        interstitialAd = new InterstitialAd(this, getString(R.string.FAN_Placement_SearchResult_HomeFragment));

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
                Log.d(TAG, "onError: "+adError.getErrorMessage());

            }

            @Override
            public void onAdLoaded(Ad ad) {

                Log.d(TAG, "onAdLoaded: "+ad.getPlacementId());

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

        FirebaseMessaging.getInstance().subscribeToTopic("common")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Success";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        // getMenuInflater().inflate(R.menu.actionmenu, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionmenu, menu);


        //for testing
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
         fragmentName = navHostFragment.getChildFragmentManager().getFragments().get(0).toString();

      //  Log.d(TAG, "onCreate: TEST: "+   navHostFragment.getChildFragmentManager().getFragments().get(0).toString());

        if (fragmentName.contains("Home")){
            Log.d(TAG, "onCreateOptionsMenu: This is HomeFragment");

            // Get the SearchView and set the searchable configuration

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            // Assumes current activity is the searchable activity
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Log.d("onQueryTextChange", "called: " + newText);
                    // searchPapers(newText);
                    return false;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    // start acitivy here
                    Log.d(TAG, "onQueryTextSubmit: Search is: " + query);
                    searchPapers(query);

                    if (interstitialAd != null && interstitialAd.isAdLoaded()){
                        interstitialAd.show();
                    }

                    return false;
                }

            });



            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    Log.d(TAG, "onClose: On Closed");
                    searchPapers("");
                    return false;
                }
            });


            
        } else if (fragmentName.contains("Latest")){
            Log.d(TAG, "onCreateOptionsMenu: This is Latest Fragment");

        } else{
            Log.d(TAG, "onCreateOptionsMenu: This is Video Fragment");
        }




        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_about_app) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_search) {
            Log.d(TAG, "onOptionsItemSelected: Search clicked");

        }

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d(TAG, "onNewIntent: ");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // doMySearch(query);
            Log.d(TAG, "onCreate: Search Query: " + query);
        }
    }


    private void searchPapers(String searchQuery){
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {

            Log.d(TAG, "searchPapers: Get Fragment: "+navHostFragment.getChildFragmentManager().getFragments().get(0).getId());
            HomeFragment homeFragment = (HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
            homeFragment.getSearchedNewsPaper(searchQuery);


        } else {
            Log.d(TAG, "onQueryTextSubmit: Nav Host fragment is null");
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

        }

        this.doubleBackToExitPressedOnce = true;
        Toasty.info(getApplicationContext(), "Double Press to Exit").show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }



    @Override
    protected void onDestroy() {
        if (interstitialAd != null){
            interstitialAd.destroy();
        }
        super.onDestroy();
    }


}
