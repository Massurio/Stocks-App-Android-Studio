package com.oirussa.stocks.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FMClient {

    @GET("actives")
    public Call<ResponseBody> getMostActives(
            @Query("apikey") String apiKey
    );


    @GET("gainers")
    public Call<ResponseBody> getMostGainers(
            @Query("apikey") String apiKey
    );


    @GET("losers")
    public Call<ResponseBody> getMostLosers(
            @Query("apikey") String apiKey
    );

    @GET("quote/{stock}")
    Call<ResponseBody> getPrice(
            @Path("stock") String stocks,
            @Query("apikey") String apiKey
    );

}
