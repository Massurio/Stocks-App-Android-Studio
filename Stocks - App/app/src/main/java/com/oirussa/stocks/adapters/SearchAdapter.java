package com.oirussa.stocks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.oirussa.stocks.R;
import com.oirussa.stocks.models.SearchStock;

import java.util.ArrayList;

public class SearchAdapter extends ArrayAdapter<SearchStock> implements Filterable {

    private ArrayList<SearchStock> stocks;
    private Context context;

    public SearchAdapter(@NonNull Context context, ArrayList<SearchStock> stocks) {
        super(context, R.layout.search_item, stocks);
        this.context = context;
        this.stocks = stocks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        }

        TextView symbol = view.findViewById(R.id.symbol);
        TextView companyName = view.findViewById(R.id.companyName);

        symbol.setText(stocks.get(position).getSymbol());
        companyName.setText(stocks.get(position).getCompanyName());

        return view;
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults r = new FilterResults();
                r.values = stocks;
                r.count = stocks.size();
                return r;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

}

