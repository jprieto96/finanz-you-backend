package com.example.finanzyou.model.stock;

import com.example.finanzyou.model.transaction.Transaction;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class Stock {

    @Id
    private String id;
    private String name;
    private String sector;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;


    public Stock(TStock tStock){
        this.id = tStock.getId();
        this.name = tStock.getName();
        this.sector = tStock.getSector();
    }

    public Stock() {}

    public TStock toTransfer() {
        return new TStock(id, name, sector);
    }

}
