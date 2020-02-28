package com.pupulputulapps.oriyanewspaper;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pupulputulapps.oriyanewspaper.Adapters.VideosAdapter;
import com.pupulputulapps.oriyanewspaper.Models.LatestNewsModel;
import com.pupulputulapps.oriyanewspaper.Models.NewsPaperWebModel;
import com.pupulputulapps.oriyanewspaper.Models.VideosModel;
import com.pupulputulapps.oriyanewspaper.Utils.ClickListenerInterface;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class VideoPlayerActivity extends YouTubeBaseActivity implements ClickListenerInterface {

    private static final String TAG = "VideoPlayerActi TAGG";

    private static YouTubePlayer youTubePlayer1;

    private MyPlaylistEventListener playlistEventListener;
    private MyPlayerStateChangeListener playerStateChangeListener;
    private MyPlaybackEventListener playbackEventListener;
    private YouTubePlayerView youTubePlayerView;
    private TextView videoTitleTextView;
    private TextView videoDescriptionTextView;

    private String videoYtCode;
    private ArrayList<VideosModel> videoItemClassArrayList = new ArrayList<>();
    private int clicked_position;

    String ytk = "test";
    private static int y;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_video_player);
        youTubePlayerView = findViewById(R.id.player);
        videoTitleTextView = findViewById(R.id.video_title_player);
        videoDescriptionTextView = findViewById(R.id.video_description_player);

        if (getIntent() != null) {
            videoItemClassArrayList = getIntent().getParcelableArrayListExtra("video_list");
            clicked_position = getIntent().getIntExtra("clicked_position", 0);
            ytk = getIntent().getStringExtra("rasta");

        }

        Log.d(TAG, "onCreate: YTK is: "+ytk);
        setUpYouTubePlayer(youTubePlayerView, ytk);

        playlistEventListener = new MyPlaylistEventListener();
        playerStateChangeListener = new MyPlayerStateChangeListener();
        playbackEventListener = new MyPlaybackEventListener();


        videoYtCode = videoItemClassArrayList.get(clicked_position).getYt_video_id();
        String videoDescription = videoItemClassArrayList.get(clicked_position).getDescription();
        String videoTitle = videoItemClassArrayList.get(clicked_position).getTitle();
        setTitleDescription(videoTitle, videoDescription);


        // Log.d(TAG, "onCreate: Parcelable: " + videoItemClassArrayList.size());


        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.hasFixedSize();
        VideosAdapter videosAdapter = new VideosAdapter(this);
        recyclerView.setAdapter(videosAdapter);
        videosAdapter.loadVideos(videoItemClassArrayList);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);
                y = dy;
            }


            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    //    Log.d(TAG, "onScrollStateChanged: Scrolling Up line 489");
                    videoTitleTextView.setVisibility(View.VISIBLE);
                    videoDescriptionTextView.setVisibility(View.VISIBLE);

                    if (y <= 0) {
                        //  Log.d(TAG, "onScrollStateChanged: Scrolling DOWN line 491");
                        videoTitleTextView.setVisibility(View.VISIBLE);
                        videoDescriptionTextView.setVisibility(View.VISIBLE);
                    } else {
                        y = 0;
                        // Log.d(TAG, "onScrollStateChanged: Scrolling UP line 494");
                        videoTitleTextView.setVisibility(View.GONE);
                        videoDescriptionTextView.setVisibility(View.GONE);

                    }
                }


            }
        });


    }


    private void setUpYouTubePlayer(YouTubePlayerView youTubePlayerView, String ytk) {

        youTubePlayerView.initialize(ytk, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {

                youTubePlayer.setPlaylistEventListener(playlistEventListener);
                youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
                youTubePlayer.setPlaybackEventListener(playbackEventListener);
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                try {
                    youTubePlayer.loadVideo(videoYtCode);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                youTubePlayer1 = youTubePlayer;

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                //   Log.d(TAG, "YouTube player failed to initialize: " + youTubeInitializationResult);
                if (youTubeInitializationResult.toString().contains("SERVICE_MISSING")) {

                    Toasty.info(getApplicationContext(), "YouTube app is not installed, this app needs YouTube service to work", Toasty.LENGTH_LONG).show();


                } else {
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.youtube_player_initialization_error), Toasty.LENGTH_LONG).show();
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        VideoPlayerActivity.super.onBackPressed();
                    }
                }, 500);
            }

        });
    }

    @Override
    public void onNewsPaperClick(NewsPaperWebModel newsPaperModel) {

    }

    @Override
    public void onVideoItemClick(ArrayList<VideosModel> videoItemClassArrayList, int clickedPosition) {

        //  Log.d(TAG, "onVideoItemClick: " + videoItemClassArrayList.get(clickedPosition).getSnippet().getTitle());
        youTubePlayer1.loadVideo(videoItemClassArrayList.get(clickedPosition).getYt_video_id());
        setTitleDescription(videoItemClassArrayList.get(clickedPosition).getTitle(), videoItemClassArrayList.get(clickedPosition).getDescription());
    }

    @Override
    public void onRssNewsItemClick(LatestNewsModel article) {

    }

    private final class MyPlaylistEventListener implements YouTubePlayer.PlaylistEventListener {
        @Override
        public void onNext() {
            //log("NEXT VIDEO");
        }

        @Override
        public void onPrevious() {
            // log("PREVIOUS VIDEO");
        }

        @Override
        public void onPlaylistEnded() {
            //  Log.d("aaaa===", "Player finished"); //this is different, this si about play list
        }
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {
        String playbackState = "NOT_PLAYING";
        String bufferingState = "";

        @Override
        public void onPlaying() {
            playbackState = "PLAYING";

        }

        @Override
        public void onBuffering(boolean isBuffering) {
            bufferingState = isBuffering ? "(BUFFERING)" : "";

        }

        @Override
        public void onStopped() {
            playbackState = "STOPPED";

        }

        @Override
        public void onPaused() {
            playbackState = "PAUSED";

        }

        @Override
        public void onSeekTo(int endPositionMillis) {

        }
    }

    private final class MyPlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {
        String playerState = "UNINITIALIZED";

        @Override
        public void onLoading() {
            playerState = "LOADING";

        }

        @Override
        public void onLoaded(String videoId) {
            playerState = String.format("LOADED %s", videoId);

        }

        @Override
        public void onAdStarted() {
            playerState = "AD_STARTED";

        }

        @Override
        public void onVideoStarted() {
            playerState = "VIDEO_STARTED";

        }

        @Override
        public void onVideoEnded() {
            playerState = "VIDEO_ENDED";
            //    Log.d(TAG, "onVideoEnded: ");
            if (clicked_position < videoItemClassArrayList.size() - 1) {
                youTubePlayer1.loadVideo(videoItemClassArrayList.get(clicked_position + 1).getYt_video_id());

            } else {
                youTubePlayer1.loadVideo(videoItemClassArrayList.get(1).getYt_video_id());

            }

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason reason) {

            if (clicked_position < videoItemClassArrayList.size() - 1) {
                youTubePlayer1.loadVideo(videoItemClassArrayList.get(clicked_position + 1).getYt_video_id());

            } else {
                youTubePlayer1.loadVideo(videoItemClassArrayList.get(1).getYt_video_id());

            }

        }

    }

    private void setTitleDescription(String videoTitle, String videoDescription) {

        videoTitleTextView.setText(videoTitle);
        videoDescriptionTextView.setText(videoDescription);
    }

    @Override
    protected void onStop() {
        youTubePlayer1.release();
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        youTubePlayer1.release();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        youTubePlayer1.release();
        super.onPause();
    }
}
