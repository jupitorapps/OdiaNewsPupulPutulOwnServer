package com.pupulputulapps.oriyanewspaper.ui.videos;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pupulputulapps.oriyanewspaper.Models.VideosModel;
import com.pupulputulapps.oriyanewspaper.Utils.ApiClientWeb;
import com.pupulputulapps.oriyanewspaper.Utils.ApiServiceWeb;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosViewModel extends ViewModel {

    private static final String TAG = "VideosViewModel TAGG";

    private MutableLiveData<List<VideosModel>> videosDataModel;
    private List<VideosModel> videosList = new ArrayList<>();

    private MutableLiveData<List<VideosModel>> searchVideosDataModel;
    private List<VideosModel> searchVideosList = new ArrayList<>();

    LiveData<List<VideosModel>> getVideos(String keyword) {

        if (videosDataModel == null && keyword.isEmpty()) {
            videosDataModel = new MutableLiveData<>();
            loadVideosFromServer();
            return videosDataModel;
        } else {
            if (!keyword.isEmpty()) {
                searchVideosDataModel = new MutableLiveData<>();
                searchVideos(keyword);
                return searchVideosDataModel;
            }
        }

        return videosDataModel;

    }

    private void loadVideosFromServer() {
        ApiServiceWeb apiService = ApiClientWeb.getRetrofit().create(ApiServiceWeb.class);
        Call<ArrayList<VideosModel>> call = apiService.getNewsVideos("news_odia", 0);
        call.enqueue(new Callback<ArrayList<VideosModel>>() {
            @Override
            public void onResponse(Call<ArrayList<VideosModel>> call, Response<ArrayList<VideosModel>> response) {
                Log.d(TAG, "onResponse: Videos: " + response.body().get(0).getTitle());
                videosList.addAll(response.body());
                videosDataModel.postValue(videosList);
            }

            @Override
            public void onFailure(Call<ArrayList<VideosModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: Videos: " + t.getMessage());
            }
        });

    }




    private void searchVideos(String keyword) {
    }


}