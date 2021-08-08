package com.oirussa.stocks.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

public class SharedPreferenceManager {

    private static final SharedPreferenceManager manager = new SharedPreferenceManager();

    private SharedPreferenceManager() {}

    public static SharedPreferenceManager getInstance()
    {
        return manager;
    }

    public static SharedPreferences getSharedPreferences(Context context)
    {
        return context.getSharedPreferences("stocks_pref", Context.MODE_PRIVATE);
    }

    public static void addDataToSharedPreferences(Context context, String key, String value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveToFavorites(Context context, String stock)
    {
        int movieNumber = getMovieNumber(context);
        Log.d("HRS", stock + " Saved at " + movieNumber);
        addDataToSharedPreferences(context, String.valueOf(movieNumber), stock);
        addDataToSharedPreferences(context, "total", movieNumber + "");
    }

    public static ArrayList<String> getAllFavoriteStocks(Context context)
    {
        String totalStr = getDataFromSharedPreferences(context, "total");

        ArrayList<String> movies = new ArrayList<>();

        if(totalStr.isEmpty())
            return movies;

        int total = Integer.parseInt(totalStr);

        for(int i = 0; i <= total; i++)
        {
            String title = getDataFromSharedPreferences(context, String.valueOf(i));

            movies.add(title);
        }

        return movies;
    }

    public static boolean removeFavorite(Context context, String stock)
    {
        String totalStr = getDataFromSharedPreferences(context, "total");

        if(totalStr.isEmpty())
            return false;

        int total = Integer.parseInt(totalStr);

        for(int i = 0; i <= total; i++)
        {
            String title = getDataFromSharedPreferences(context, String.valueOf(i));

            if(title.equals(stock))
            {
                addDataToSharedPreferences(context, "" + i, "");
                return true;
            }
        }

        return false;
    }


    public static String getDataFromSharedPreferences(Context context, String key)
    {
        return getSharedPreferences(context).getString(key,"");
    }

    public static int getMovieNumber(Context context)
    {
        String num = getSharedPreferences(context).getString("movie_number", "");

        assert num != null;
        if(num.equals(""))
        {
            propagateNumber("0", context);
            return 0;
        }else
        {
            propagateNumber(num, context);
        }

        return Integer.parseInt(num);

    }

    private static void propagateNumber(String num, Context context) {
        int numToAdd = Integer.parseInt(num);
        numToAdd++;
        addDataToSharedPreferences(context, "movie_number", String.valueOf(numToAdd));
    }

}

