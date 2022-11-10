package com.example.finanzyou.model.stock;

import lombok.Data;

@Data
public class TStock {

    private String id;
    private String name;
    private String sector;

    public TStock(String id, String name, String sector) {
        this.id = id;
        this.name = name;
        this.sector = sector;
    }
}
