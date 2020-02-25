package com.pupulputulapps.oriyanewspaper.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientWeb {

    private static Retrofit retrofit;
    private static final String BASE_URL_SERVER = "http://odiacalendar.co.in/news_apps/";

    public static Retrofit getRetrofit() {

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL_SERVER)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofit;


    }


}
