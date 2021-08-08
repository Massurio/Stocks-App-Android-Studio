package com.oirussa.stocks.helpers;

import android.content.Context;
import android.util.Log;

import com.oirussa.stocks.R;
import com.oirussa.stocks.api.FMClient;
import com.oirussa.stocks.models.Stock;
import com.oirussa.stocks.util.TaskCompleteListener;
import com.oirussa.stocks.util.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DataHelper {

    public static ArrayList<Stock> stocks;

    public static int count = 0;


    public static void getDataForHomeStocks(Context context, TaskCompleteListener listener)
    {
        count = 0;
        stocks = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Variables.FM_URL).build();

        Call<ResponseBody> activesCall = retrofit.create(FMClient.class).getMostActives(context.getString(R.string.fm_api_key));
        Call<ResponseBody> gainersCall = retrofit.create(FMClient.class).getMostGainers(context.getString(R.string.fm_api_key));
        Call<ResponseBody> losersCall = retrofit.create(FMClient.class).getMostLosers(context.getString(R.string.fm_api_key));

        activesCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                    String activesResponse = "";

                    try {
                        assert response.body() != null;
                        activesResponse = response.body().string();
                        Log.d("JH_RES_ACTIVE", activesResponse);
                        parseHomeResponse(activesResponse, "actives");


                        Log.d("JH_CHECK_COUNT", "" + count);

                        if(count == 3)
                        {

                            Log.d("JH_CHECK_HELPER", "CAME" + count);
                            listener.onDownloadComplete(stocks);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else
                {
                    Log.d("JH_RES_ACTIVE", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        gainersCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                    String gainersResponse = "";

                    try {
                        assert response.body() != null;
                        gainersResponse = response.body().string();
                        Log.d("JH_RES_GAINERS", gainersResponse);
                        parseHomeResponse(gainersResponse, "gainers");


                        Log.d("JH_CHECK_COUNT", "" + count);

                        if(count == 3)
                        {
                            Log.d("JH_CHECK_HELPER", "CAME" + count);
                            listener.onDownloadComplete(stocks);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else
                {
                    Log.d("JH_RES_GAINERS", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        losersCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                    String losersResponse = "";

                    try {
                        assert response.body() != null;
                        losersResponse = response.body().string();
                        Log.d("JH_RES_LOSERS", losersResponse);
                        parseHomeResponse(losersResponse, "losers");

                        Log.d("JH_CHECK_COUNT", "" + count);

                        if(count == 3)
                        {
                            Log.d("JH_CHECK_HELPER", "Came");
                            listener.onDownloadComplete(stocks);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else
                {
                    Log.d("JH_RES_LOSERS", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    private static void parseHomeResponse(String response, String type) {
        stocks.add(new Stock(type,"","",""));

        try {
            JSONArray array = new JSONArray(response);

            for(int i = 0; i < array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                String ticker = object.getString("ticker");
                String changes = object.getString("changes");
                String price = object.getString("price");
                String pChange = object.getString("changesPercentage");

                stocks.add(new Stock(ticker, price, changes, pChange));
            }

            count++;

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }





}
