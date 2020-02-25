package com.pupulputulapps.oriyanewspaper.ui.latestNews;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pupulputulapps.oriyanewspaper.Models.LatestNewsModel;
import com.pupulputulapps.oriyanewspaper.Utils.ApiClientWeb;
import com.pupulputulapps.oriyanewspaper.Utils.ApiServiceWeb;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LatestNewsViewModel extends ViewModel {

    private static final String TAG = "LatestNews VM TAGG";

    private MutableLiveData<List<LatestNewsModel>> latestNewsDataModel;
    private List<LatestNewsModel> latestNewsList = new ArrayList<>();

    private MutableLiveData<List<LatestNewsModel>> searchLatestNewsDataModel;
    private List<LatestNewsModel> searchLatestNewsList = new ArrayList<>();

    LiveData<List<LatestNewsModel>> getLatestNewsModelData(String keyword) {

        if (latestNewsDataModel == null && keyword.isEmpty()) {
            latestNewsDataModel = new MutableLiveData<>();
            loadLatestNewsFromServer();
            return latestNewsDataModel;
        } else {
            if (!keyword.isEmpty()) {
                searchLatestNewsDataModel = new MutableLiveData<>();
                searchLatestNews(keyword);
                return searchLatestNewsDataModel;
            }
        }

        return latestNewsDataModel;

    }

    private void loadLatestNewsFromServer() {
        ApiServiceWeb apiService = ApiClientWeb.getRetrofit().create(ApiServiceWeb.class);
        Call<ArrayList<LatestNewsModel>> call = apiService.getLatestNews("news_odia", 0);
        call.enqueue(new Callback<ArrayList<LatestNewsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<LatestNewsModel>> call, Response<ArrayList<LatestNewsModel>> response) {
                Log.d(TAG, "onResponse: Latest News: "+response.body().get(0).getTitle());
                latestNewsList.addAll(response.body());
                latestNewsDataModel.postValue(latestNewsList);

            }

            @Override
            public void onFailure(Call<ArrayList<LatestNewsModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: Latest News: "+t.getMessage());
            }
        });

    }

    private void searchLatestNews(String keyword) {
    }


}