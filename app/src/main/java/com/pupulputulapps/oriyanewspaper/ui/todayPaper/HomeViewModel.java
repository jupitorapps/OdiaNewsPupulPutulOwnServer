package com.pupulputulapps.oriyanewspaper.ui.todayPaper;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pupulputulapps.oriyanewspaper.Models.NewsPaperWebModel;
import com.pupulputulapps.oriyanewspaper.Utils.ApiClientWeb;
import com.pupulputulapps.oriyanewspaper.Utils.ApiServiceWeb;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private final String TAG = "HomeViewModel TAGG";
    private MutableLiveData<List<NewsPaperWebModel>> newsPaperModelData;
    private List<NewsPaperWebModel> newsList = new ArrayList<>();

    private List<NewsPaperWebModel> searchNewsList = new ArrayList<>();
    private MutableLiveData<List<NewsPaperWebModel>> searchNewsPaperModelData;

    private List<NewsPaperWebModel> favNewsPaperList = new ArrayList<>();
    private MutableLiveData<List<NewsPaperWebModel>> favNewsPaperModelData;


    LiveData<List<NewsPaperWebModel>> getNewsPaperModelData(String keyword, String user_id) {

        if (newsPaperModelData == null && keyword.isEmpty()) {
            newsPaperModelData = new MutableLiveData<>();
            loadNewsPaperModelData();
            Log.d(TAG, "getNewsPaperModelData: here at line 38");
            return newsPaperModelData;
        }

        if (!keyword.isEmpty() && !TextUtils.isDigitsOnly(keyword)) {
            Log.d(TAG, "getNewsPaperModelData: here at line 42");
            searchNewsPaperModelData = new MutableLiveData<>();
            searchNewsPapers(keyword);
            return searchNewsPaperModelData;
        }

        if (!user_id.isEmpty()) {
            Log.d(TAG, "getNewsPaperModelData: get fav news for: " + keyword);
            favNewsPaperModelData = new MutableLiveData<>();
            getFavNewsPapers(user_id);
            return favNewsPaperModelData;

        }

        return newsPaperModelData;
    }


    private void loadNewsPaperModelData() {

        ApiServiceWeb apiService = ApiClientWeb.getRetrofit().create(ApiServiceWeb.class);
        Call<ArrayList<NewsPaperWebModel>> call = apiService.getNewsPapers("news_odia", 0);
        call.enqueue(new Callback<ArrayList<NewsPaperWebModel>>() {
            @Override
            public void onResponse(Call<ArrayList<NewsPaperWebModel>> call, Response<ArrayList<NewsPaperWebModel>> response) {
                newsList.addAll(response.body());
                newsPaperModelData.postValue(newsList);
            }

            @Override
            public void onFailure(Call<ArrayList<NewsPaperWebModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: OC: " + t.getMessage());

            }
        });

    }

    private void searchNewsPapers(String keyword) {

        ApiServiceWeb apiService = ApiClientWeb.getRetrofit().create(ApiServiceWeb.class);
        Call<ArrayList<NewsPaperWebModel>> call = apiService.searchNewsPapers("news_odia", 0, keyword);
        call.enqueue(new Callback<ArrayList<NewsPaperWebModel>>() {
            @Override
            public void onResponse(Call<ArrayList<NewsPaperWebModel>> call, Response<ArrayList<NewsPaperWebModel>> response) {

                if (!searchNewsList.isEmpty()) {
                    searchNewsList.clear();
                }

                if (response.body() != null && response.body().isEmpty()) {
                    searchNewsList.add(new NewsPaperWebModel(1, 1, "Please Try Again", "No Paper Found", "", "", "empty.jpg", 0));
                } else {
                    if (response.body() != null) {
                        searchNewsList.addAll(response.body());
                    }
                }

                searchNewsPaperModelData.postValue(searchNewsList);

            }

            @Override
            public void onFailure(Call<ArrayList<NewsPaperWebModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: OC: " + t.getMessage());
            }
        });

    }


    private void getFavNewsPapers(String user_id) {

        ApiServiceWeb apiService = ApiClientWeb.getRetrofit().create(ApiServiceWeb.class);
        Call<ArrayList<NewsPaperWebModel>> call = apiService.getFavNewsPapers("news_odia", user_id, 0);
        call.enqueue(new Callback<ArrayList<NewsPaperWebModel>>() {
            @Override
            public void onResponse(Call<ArrayList<NewsPaperWebModel>> call, Response<ArrayList<NewsPaperWebModel>> response) {

                Log.d(TAG, "onResponse: Fav news: " + response);

                if (!favNewsPaperList.isEmpty()) {
                    favNewsPaperList.clear();
                }
                if (response.body() != null && response.body().isEmpty()) {
                    favNewsPaperList.add(new NewsPaperWebModel(1, 1, "Please Try Again", "No Paper Found", "", "", "empty.jpg", 0));
                } else {
                    if (response.body() != null) {
                        favNewsPaperList.addAll(response.body());
                    }
                }

                favNewsPaperModelData.postValue(favNewsPaperList);


            }

            @Override
            public void onFailure(Call<ArrayList<NewsPaperWebModel>> call, Throwable t) {

            }
        });


    }


}