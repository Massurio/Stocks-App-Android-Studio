package com.oirussa.stocks.util;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface SectorsApi {

    @GET("query")
    public Call<ResponseBody> requestApi(
            @QueryMap Map<String, String> queryMap
    );

}
