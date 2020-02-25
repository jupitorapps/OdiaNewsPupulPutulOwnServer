package com.pupulputulapps.oriyanewspaper.ui.todayPaper;

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

    private MutableLiveData<List<NewsPaperWebModel>> newsPaperModelData;
    private final String TAG = "HomeViewModel TAGG";

    private List<NewsPaperWebModel> newsList = new ArrayList<>();

    private List<NewsPaperWebModel> searchNewsList = new ArrayList<>();
    private MutableLiveData<List<NewsPaperWebModel>> searchNewsPaperModelData;


    LiveData<List<NewsPaperWebModel>> getNewsPaperModelData(String keyword, int offset) {

        if (newsPaperModelData == null && keyword.isEmpty()) {
            newsPaperModelData = new MutableLiveData<>();
            loadNewsPaperModelData(offset);
            return newsPaperModelData;
        }

        if (!keyword.isEmpty()) {

            searchNewsPaperModelData = new MutableLiveData<>();
            searchNewsPapers(keyword);
            return searchNewsPaperModelData;
        }

        return newsPaperModelData;
    }


    private void loadNewsPaperModelData(int offset) {

        ApiServiceWeb apiService = ApiClientWeb.getRetrofit().create(ApiServiceWeb.class);
        Call<ArrayList<NewsPaperWebModel>> call = apiService.getNewsPapers("news_odia", offset);
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

                if (!searchNewsList.isEmpty()){
                    searchNewsList.clear();
                }

                if (response.body()!=null && response.body().isEmpty() ){
                    searchNewsList.add(new NewsPaperWebModel(1,1,"Please Try Again","No Paper Found","","","empty.jpg",0));
                } else{
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


}