package com.example.finanzyou.repository;

import com.example.finanzyou.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositoryTransaction extends JpaRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction> {

    @Query("SELECT t FROM Transaction t WHERE t.client.id = ?1")
    List<Transaction> findByIdClient(int idClient);
    void deleteAllById(int idTransaction);
}
