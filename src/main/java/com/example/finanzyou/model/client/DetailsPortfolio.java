package com.example.finanzyou.model.client;

import lombok.Data;

@Data
public class DetailsPortfolio {

    private int quantity;
    private double buyPrice;
    private String sector;

    public DetailsPortfolio() {}

    public DetailsPortfolio(int quantity, double buyPrice, String sector) {
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.sector = sector;
    }
}
