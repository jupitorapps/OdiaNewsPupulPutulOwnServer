package com.pupulputulapps.oriyanewspaper;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pupulputulapps.oriyanewspaper.Utils.Utils;
import com.pupulputulapps.oriyanewspaper.ui.latestNews.LatestNewsFragment;
import com.pupulputulapps.oriyanewspaper.ui.todayPaper.HomeFragment;
import com.pupulputulapps.oriyanewspaper.ui.videos.VideosFragment;

import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity TAGG";
    boolean doubleBackToExitPressedOnce = false;

    private InterstitialAd interstitialAd;
    private String fragmentName;

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    private Boolean nowShowingFavNews = false;
    private String launchedFrom;

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

        interstitialAd.loadAd();

        if (Utils.getUserId(getApplicationContext()).isEmpty()) {
            getUniqueID(getApplicationContext());

        }
        Log.d(TAG, "onCreate: User id is: " + Utils.getUserId(getApplicationContext()));

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

    public synchronized static void getUniqueID(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                Utils.saveUserId(context, uniqueID);
            }
        }

        Log.d("Main Activity TAGG", "Unique id: " + uniqueID);
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

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPapers(query);
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

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (id == R.id.action_about_app) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

if (id ==R.id.action_favourite){
    if (item.getIcon().getConstantState() == getResources().getDrawable(R.drawable.star_fill).getConstantState()) {
        item.setIcon(getResources().getDrawable(R.drawable.action_home));
        if (fragmentName.contains("Home")) {
            HomeFragment homeFragment = (HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
            homeFragment.getNewsPapers("", Utils.getUserId(getApplicationContext()));

        }
    } else {
        item.setIcon(getResources().getDrawable(R.drawable.star_fill));
        if (fragmentName.contains("Home")) {
            HomeFragment homeFragment = (HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
            homeFragment.getNewsPapers("", "");

        }
    }

}
//        if (id == R.id.action_favourite) {
//            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//
//            if (navHostFragment != null) {
//
//                if (fragmentName.contains("Home")) {
//                    HomeFragment homeFragment = (HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
//                    homeFragment.getNewsPapers("",Utils.getUserId(getApplicationContext()));
//
//                }
//            } else {
//                Log.d(TAG, "onQueryTextSubmit: Nav Host fragment is null");
//            }
//
//        }

        return true;
    }

    private void searchPapers(String searchQuery) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);


        if (navHostFragment != null) {

            if (fragmentName.contains("Home")) {
                HomeFragment homeFragment = (HomeFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
                homeFragment.getNewsPapers(searchQuery, "");
            } else if (fragmentName.contains("Latest")) {
                LatestNewsFragment latestNewsFragment = (LatestNewsFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
                latestNewsFragment.loadNewsFromRss(searchQuery);
            } else {
                VideosFragment videosFragment = (VideosFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
                videosFragment.loadVideosFromServer(searchQuery);
            }

        } else {
            Log.d(TAG, "onQueryTextSubmit: Nav Host fragment is null");
        }
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        super.onDestroy();
    }

}