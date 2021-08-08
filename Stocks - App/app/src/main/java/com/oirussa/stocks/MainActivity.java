package com.oirussa.stocks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.oirussa.stocks.activities.FavouriteActivity;
import com.oirussa.stocks.activities.SearchActivity;
import com.oirussa.stocks.adapters.StockAdapter;
import com.oirussa.stocks.helpers.DataHelper;
import com.oirussa.stocks.models.Stock;
import com.oirussa.stocks.util.TaskCompleteListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private RecyclerView stocks;
    ProgressBar progressBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.favoriteBtn)
        {
            Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.refreshBtn)
        {
            loadData();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadData();

    }

    private void downloadData() {
        stocks = findViewById(R.id.stockList);

        findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(stocks.getContext(),
                layoutManager.getOrientation());
        stocks.addItemDecoration(dividerItemDecoration);
        stocks.setLayoutManager(layoutManager);

        progressBar = findViewById(R.id.progressBar);

        loadData();

    }

    private void loadData() {
        hideViews();

        DataHelper.getDataForHomeStocks(this, new TaskCompleteListener() {
            @Override
            public void onDownloadComplete(ArrayList<Stock> stockList) {
                Log.d("JH_CHECK_HOME", "Came");
                showViews();
                stocks.setAdapter(new StockAdapter(MainActivity.this, stockList));
            }
        });

    }


    private void hideViews() {
        progressBar.setVisibility(View.VISIBLE);
        stocks.setVisibility(View.GONE);
    }

    private void showViews()
    {
        progressBar.setVisibility(View.GONE);
        stocks.setVisibility(View.VISIBLE);
    }

}