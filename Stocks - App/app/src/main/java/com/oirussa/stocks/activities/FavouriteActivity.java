package com.oirussa.stocks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oirussa.stocks.R;
import com.oirussa.stocks.adapters.AssetAdapter;
import com.oirussa.stocks.api.FMClient;
import com.oirussa.stocks.models.Stock;
import com.oirussa.stocks.util.SharedPreferenceManager;
import com.oirussa.stocks.util.Variables;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FavouriteActivity extends AppCompatActivity {

    String delimiterString = "";
    private TextView noAssets;
    private ListView assetList;
    private ProgressBar progressBar;
    AssetAdapter assetAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem fav = menu.findItem(R.id.favoriteBtn);
        fav.setVisible(false);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.refreshBtn)
        {
            getData();
        }

        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        initViews();
        getData();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void getData() {

        ArrayList<String> assets = SharedPreferenceManager.getAllFavoriteStocks(this);

        for (int i = 0; i < assets.size(); i++)
        {
            if(i < assets.size()-1)
            {
                delimiterString = delimiterString.concat(assets.get(i) + ",");
            }
            else
            {
                delimiterString = delimiterString.concat(assets.get(i));
            }
        }

        if(assets.isEmpty())
        {
            progressBar.setVisibility(View.GONE);
            assetList.setVisibility(View.GONE);
            noAssets.setVisibility(View.VISIBLE);
        }else
        {
            getPrices(delimiterString);
        }

    }


    private void getPrices(String delimiterString) {

        hideViews();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Variables.FM_URL)
                .build();

        Call<ResponseBody> call = retrofit.create(FMClient.class).getPrice(delimiterString, getString(R.string.fm_api_key));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ArrayList<Stock> assetModels = new ArrayList<>();

                if(response.isSuccessful())
                {
                    try {
                        String res = response.body().string();
                        JSONArray array = new JSONArray(res);


                        Log.d("TYR", "RESPONSE:  " + res);

                        for(int i = 0; i < array.length(); i++)
                        {
                            String price = array.getJSONObject(i).getString("price");
                            String symbol = array.getJSONObject(i).getString("symbol");
                            String percentage = array.getJSONObject(i).getString("changesPercentage");
                            String change = array.getJSONObject(i).getString("change");

                            assetModels.add(new Stock(symbol, price, change, percentage));
                        }

                        if(assetModels.size() > 0)
                        {
                            showViews();
                            assetAdapter = new AssetAdapter(FavouriteActivity.this, assetModels);
                            assetList.setAdapter(assetAdapter);

                            assetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    boolean status = SharedPreferenceManager.removeFavorite(FavouriteActivity.this, assetModels.get(i).getName());

                                    if(status)
                                    {
                                        assetModels.remove(i);
                                        assetAdapter.notifyDataSetChanged();
                                        Toast.makeText(FavouriteActivity.this, "Removed from favourites", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }else
                        {
                            progressBar.setVisibility(View.GONE);
                            noAssets.setVisibility(View.VISIBLE);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else
                {
                    Log.d("HYT", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    private void showViews() {

        assetList.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        noAssets.setVisibility(View.GONE);


    }

    private void hideViews() {

        assetList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        noAssets.setVisibility(View.GONE);

    }

    private void initViews() {
        assetList = findViewById(R.id.assetList);
        progressBar = findViewById(R.id.progressBar);
        noAssets = findViewById(R.id.noAsset);
    }
}