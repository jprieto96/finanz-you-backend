package com.example.finanzyou.model.stock.imp;

import com.example.finanzyou.model.stock.SAStock;
import com.example.finanzyou.model.stock.Stock;
import com.example.finanzyou.model.stock.TStock;
import com.example.finanzyou.repository.RepositoryClient;
import com.example.finanzyou.repository.RepositoryStock;
import com.example.finanzyou.repository.RepositoryTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SAStockImp implements SAStock {

    @Autowired
    private RepositoryClient repositoryClient;

    @Autowired
    private RepositoryStock repositoryStock;

    @Autowired
    private RepositoryTransaction repositoryTransaction;

    @Override
    public TStock createStock(TStock tStock) throws Exception {
        return null;
    }

    @Override
    public TStock showDetails(int id) throws Exception {
        return null;
    }

    @Override
    public TStock removeStock(int id) throws Exception {
        return null;
    }

    @Override
    public List<TStock> listStocks() {
        List<TStock> transferStockList = new ArrayList<>();

        List<Stock> stockList = repositoryStock.findAll();
        for(Stock stock : stockList) {
            transferStockList.add(stock.toTransfer());
        }

        return transferStockList;
    }

}
