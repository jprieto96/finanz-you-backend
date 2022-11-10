package com.example.finanzyou.model.transaction;

import com.example.finanzyou.model.client.Client;
import com.example.finanzyou.model.stock.Stock;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Stock stock;

    private double buyPrice;
    private int quantity;
    private Date date;
    private String currency;
   

    public Transaction() {}

    public Transaction(int id, int idClient, String idStock, double buyPrice, int quantity, Date date, String currency) {
        this.id = id;
        this.buyPrice = buyPrice;
        this.quantity = quantity;
        this.date = date;
        this.currency = currency;
    }

    public TTransaction toTransfer() {
        return new TTransaction(id, client.getId(), stock.getId(), stock.getName(), stock.getSector(), buyPrice, quantity, date, currency);
    }

}
