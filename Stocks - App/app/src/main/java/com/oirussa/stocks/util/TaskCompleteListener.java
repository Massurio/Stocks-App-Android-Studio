package com.oirussa.stocks.util;

import com.oirussa.stocks.models.Stock;

import java.util.ArrayList;

public interface TaskCompleteListener {

    void onDownloadComplete(ArrayList<Stock> stocks) ;

}
