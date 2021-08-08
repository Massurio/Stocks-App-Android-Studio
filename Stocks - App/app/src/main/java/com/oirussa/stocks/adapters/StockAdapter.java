package com.oirussa.stocks.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oirussa.stocks.R;
import com.oirussa.stocks.models.Stock;
import com.oirussa.stocks.util.SharedPreferenceManager;

import java.util.ArrayList;

public class StockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Stock> stocks;

    public StockAdapter(Context context, ArrayList<Stock> stocks) {
        this.context = context;
        this.stocks = stocks;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == 0)
        {
            view = LayoutInflater.from(context).inflate(R.layout.stock_item, parent, false);
            return new StockViewHolder(view);
        }else
        {
            view = LayoutInflater.from(context).inflate(R.layout.top_header_layout, parent, false);
            return new HeaderView(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(stocks.get(position).getName().equals("actives"))
        {
            ((HeaderView) holder).title.setText(R.string.most_actives);
        }else if(stocks.get(position).getName().equals("losers"))
        {
            ((HeaderView) holder).title.setText(R.string.top_losers);
        }else if(stocks.get(position).getName().equals("gainers"))
        {
            ((HeaderView) holder).title.setText(R.string.top_gainers);
        }else
        {
            StockViewHolder viewHolder  = ((StockViewHolder) holder);

            viewHolder.symbol.setText(stocks.get(position).getName());
            viewHolder.price.setText(String.format("%,.2f", Float.parseFloat(stocks.get(position).getPrice())));

            char checkOne = stocks.get(position).getChange().charAt(0);

            if(checkOne == '-')
            {
                viewHolder.change.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }else
            {
                viewHolder.change.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            }

            viewHolder.change.setText(stocks.get(position).getChange());

            char checkTwo = stocks.get(position).getpChange().charAt(1);

            if(checkTwo == '+')
            {
                viewHolder.pChange.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            }else
            {
                viewHolder.pChange.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }

            viewHolder.pChange.setText(stocks.get(position).getpChange());


            viewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to add " + stocks.get(position).getName() + " to favourites?");
                    builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferenceManager.saveToFavorites(context, stocks.get(position).getName());
                            Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT).show();
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


        }

    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    @Override
    public int getItemViewType(int position) {
        String type = stocks.get(position).getName();

        switch (type)
        {
            case "actives":
                return 1;
            case "gainers":
                return 2;
            case "losers":
                return 3;
        }

        return 0;
    }
}

class StockViewHolder extends RecyclerView.ViewHolder{

    TextView symbol, price, change, pChange;
    LinearLayout container;

    public StockViewHolder(@NonNull View itemView) {
        super(itemView);

        symbol = itemView.findViewById(R.id.symbol);
        price = itemView.findViewById(R.id.price);
        change = itemView.findViewById(R.id.change);
        pChange = itemView.findViewById(R.id.perc_change);
        container = itemView.findViewById(R.id.container);
    }

}

class HeaderView extends RecyclerView.ViewHolder{

    TextView title;

    public HeaderView(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
    }

}