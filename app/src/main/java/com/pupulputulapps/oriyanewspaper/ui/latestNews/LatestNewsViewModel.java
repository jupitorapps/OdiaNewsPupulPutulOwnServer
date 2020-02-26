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

    LiveData<List<LatestNewsModel>> getLatestNewsModelData(String keyword) {

        if (latestNewsDataModel == null) {
            latestNewsDataModel = new MutableLiveData<>();
        }

        if (keyword.isEmpty()) {

            loadLatestNewsFromServer();

        } else {
            searchLatestNews(keyword);
        }

        return latestNewsDataModel;

    }

    private void loadLatestNewsFromServer() {
        ApiServiceWeb apiService = ApiClientWeb.getRetrofit().create(ApiServiceWeb.class);
        Call<ArrayList<LatestNewsModel>> call = apiService.getLatestNews("news_odia", 0);
        call.enqueue(new Callback<ArrayList<LatestNewsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<LatestNewsModel>> call, Response<ArrayList<LatestNewsModel>> response) {
                Log.d(TAG, "onResponse: Latest News: " + response.body().get(0).getTitle());

                if (!latestNewsList.isEmpty()){
                    latestNewsList.clear();
                }

                latestNewsList.addAll(response.body());
                latestNewsDataModel.postValue(latestNewsList);

            }

            @Override
            public void onFailure(Call<ArrayList<LatestNewsModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: Latest News: " + t.getMessage());
            }
        });

    }

    private void searchLatestNews(String keyword) {
        ApiServiceWeb apiService = ApiClientWeb.getRetrofit().create(ApiServiceWeb.class);
        Call<ArrayList<LatestNewsModel>> call = apiService.searchLatestNews("news_odia", 0, keyword);
        call.enqueue(new Callback<ArrayList<LatestNewsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<LatestNewsModel>> call, Response<ArrayList<LatestNewsModel>> response) {

                if (!latestNewsList.isEmpty()){
                    latestNewsList.clear();
                }

                if (response.body() != null && response.body().isEmpty()) {
                    latestNewsList.add(new LatestNewsModel(1, "No News Found, please try with different search term", "", "pleases", "", "empty.jpg", "", ""));

                } else if (response.body() != null) {

                    latestNewsList.addAll(response.body());
                }

                latestNewsDataModel.postValue(latestNewsList);
            }

            @Override
            public void onFailure(Call<ArrayList<LatestNewsModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: to search latest news: " + t.getMessage());
            }
        });

    }


}