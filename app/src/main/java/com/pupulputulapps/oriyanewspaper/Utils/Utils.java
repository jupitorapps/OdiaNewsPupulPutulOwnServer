package com.pupulputulapps.oriyanewspaper.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {

    private static final String TAG = "Utils TAGG";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USER_ID = "user_id";

    public static synchronized void saveUserId(final Context context, final String user_id) {

        final SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        ApiServiceWeb apiService = ApiClientWeb.getRetrofit().create(ApiServiceWeb.class);
        Call<String> call = apiService.createUser("news_odia", user_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "User id created in DB : " + response.body());

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(USER_ID, response.body());
                editor.apply();
                Log.d("TAGG", "User id saved to SharedPref " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {


            }
        });

    }

    public static String getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        return preferences.getString(USER_ID, "");
    }



}
