package com.oirussa.stocks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.oirussa.stocks.R;
import com.oirussa.stocks.models.Stock;

import java.util.ArrayList;

public class AssetAdapter extends ArrayAdapter<Stock> {

    private ArrayList<Stock> assets;
    private Context context;



    public AssetAdapter(@NonNull Context context, ArrayList<Stock> assets) {
        super(context, R.layout.asset_item, assets);
        this.context = context;
        this.assets = assets;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.asset_item, parent, false);
        }

        TextView symbol = view.findViewById(R.id.symbol);
        TextView price = view.findViewById(R.id.price);
        TextView percentage = view.findViewById(R.id.percent);

        symbol.setText(assets.get(position).getName());
        price.setText("$" + assets.get(position).getPrice());

        String percentageToDisplay = "";

        if(assets.get(position).getChange().charAt(0) == '-')
        {
            percentageToDisplay = "-$"+ assets.get(position).getChange().substring(1) +  "( " + assets.get(position).getpChange() + "% )";
        }else
        {
            percentageToDisplay = "$"+ assets.get(position).getChange() +  "( " + assets.get(position).getpChange() + "% )";
        }


        percentage.setText(percentageToDisplay);
        char status = assets.get(position).getpChange().charAt(0);

        if(status == '-')
        {
            percentage.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        }else
        {
            percentage.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        }


        return view;
    }
}
