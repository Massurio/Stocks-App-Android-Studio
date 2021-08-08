package com.oirussa.stocks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.oirussa.stocks.R;
import com.oirussa.stocks.adapters.SearchAdapter;
import com.oirussa.stocks.models.SearchStock;
import com.oirussa.stocks.util.SectorsApi;
import com.oirussa.stocks.util.SharedPreferenceManager;
import com.oirussa.stocks.util.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity {

    private EditText searchBar;
    private ProgressBar progressBar;
    private ListView searchList;
    ArrayList<SearchStock> searchResults;
    SearchAdapter adapter;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        searchBar = findViewById(R.id.searchBar);
        progressBar = findViewById(R.id.progressBar);
        searchList = findViewById(R.id.searchList);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence query, int i, int i1, int i2) {
                searchQuery(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }



    private void hideViews()
    {
        progressBar.setVisibility(View.VISIBLE);
        searchList.setVisibility(View.GONE);
    }

    private void showViews()
    {
        progressBar.setVisibility(View.GONE);
        searchList.setVisibility(View.VISIBLE);
    }

    private void searchQuery(CharSequence s) {

        hideViews();

        searchResults = new ArrayList<>();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Variables.ALPHA_VANTAGE_URL)
                .build();

        Map<String, String> map = new HashMap<>();

        map.put("function", "SYMBOL_SEARCH");
        map.put("keywords", s.toString());
        map.put("apikey",getString(R.string.alpha_vantage_api_key));

        Call<ResponseBody> call = retrofit.create(SectorsApi.class).requestApi(map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                    try {
                        assert response.body() != null;
                        String searchResponse = response.body().string();
                        Log.d("TAS", searchResponse);
                        JSONObject object = new JSONObject(searchResponse);
                        JSONArray array = object.getJSONArray("bestMatches");

                        for(int i = 0; i < array.length(); i++)
                        {

                            JSONObject searchObject = array.getJSONObject(i);
                            String symbol = searchObject.getString("1. symbol");
                            String name = searchObject.getString("2. name");
                            Log.d("HGS", symbol + " " + name);
                            searchResults.add(new SearchStock(symbol, name));
                        }

                        adapter = new SearchAdapter(SearchActivity.this,searchResults);
                        adapter.setNotifyOnChange(true);
                        searchList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        showViews();

                        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                                builder.setMessage("Do you want to add " + searchResults.get(position).getSymbol() + " to favourites?");
                                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        SharedPreferenceManager.saveToFavorites(SearchActivity.this, searchResults.get(position).getSymbol());
                                        Toast.makeText(SearchActivity.this, "Added to favourites", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();
                            }
                        });


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

}