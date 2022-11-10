package com.example.finanzyou.model.stock;

import java.util.List;

public interface SAStock {

    TStock createStock(TStock tStock) throws Exception;
    TStock showDetails(int id) throws Exception;
    TStock removeStock(int id) throws Exception;
    List<TStock> listStocks();

}
