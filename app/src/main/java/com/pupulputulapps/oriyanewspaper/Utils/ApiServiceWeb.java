package com.pupulputulapps.oriyanewspaper.Utils;

import com.pupulputulapps.oriyanewspaper.Models.LatestNewsModel;
import com.pupulputulapps.oriyanewspaper.Models.NewsPaperWebModel;
import com.pupulputulapps.oriyanewspaper.Models.RastaModel;
import com.pupulputulapps.oriyanewspaper.Models.VideosModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServiceWeb {

    @GET("getNewsPapers.php")
    Call<ArrayList<NewsPaperWebModel>> getNewsPapers(@Query("dbname") String dbname, @Query("offset") int offset);

    @GET("searchNewsPapers.php")
    Call<ArrayList<NewsPaperWebModel>> searchNewsPapers(@Query("dbname") String dbname, @Query("offset") int offset, @Query("keyword") String keyword);

    @GET("getLatestNews.php")
    Call<ArrayList<LatestNewsModel>> getLatestNews(@Query("dbname") String dbname, @Query("offset") int offset);

    @GET("searchLatestNews.php")
    Call<ArrayList<LatestNewsModel>> searchLatestNews(@Query("dbname") String dbname, @Query("offset") int offset, @Query("keyword") String keyword);


    @GET("getNewsVideos.php")
    Call<ArrayList<VideosModel>> getNewsVideos(@Query("dbname") String dbname, @Query("offset") int offset);

    @GET("searchVideos.php")
    Call<ArrayList<VideosModel>> searchVideos(@Query("dbname") String dbname, @Query("offset") int offset, @Query("keyword") String keyword);

    @GET("getRasta.php")
    Call<ArrayList<RastaModel>> getRasta(@Query("dbname") String dbname);

    @GET("createUser.php")
    Call<String> createUser (@Query("dbname") String dbname, @Query("device_id") String device_id);

    @GET("updateFav.php")
    Call<String> updateFav (@Query("dbname") String dbname, @Query("user_id") String user_id, @Query("news_paper_id") int news_paper_id);

    @GET("getFavNewsPapers.php")
    Call<ArrayList<NewsPaperWebModel>> getFavNewsPapers(@Query("dbname") String dbname,@Query("user_id") String user_id, @Query("offset") int offset);

    @GET("checkFavNewsPaper.php")
    Call<String> checkFavNewsPaper (@Query("dbname") String dbname, @Query("user_id") String user_id, @Query("news_paper_id") int news_paper_id);

}
