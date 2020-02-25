package com.pupulputulapps.oriyanewspaper.Utils;

import com.pupulputulapps.oriyanewspaper.Models.LatestNewsModel;
import com.pupulputulapps.oriyanewspaper.Models.NewsPaperWebModel;
import com.pupulputulapps.oriyanewspaper.Models.VideosModel;

import java.util.ArrayList;

public interface ClickListenerInterface {
    void onNewsPaperClick(NewsPaperWebModel newsPaperModel);
    void onVideoItemClick(ArrayList<VideosModel> videoItemClassArrayList, int clickedPosition);
    void onRssNewsItemClick(LatestNewsModel article);
}