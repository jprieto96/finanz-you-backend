package com.example.finanzyou.repository;

import com.example.finanzyou.model.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RepositoryStock extends JpaRepository<Stock, Integer>, JpaSpecificationExecutor<Stock> {
    Optional<Stock> findStockById(String idStock);
}
