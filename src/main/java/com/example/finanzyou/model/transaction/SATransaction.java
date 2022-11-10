package com.example.finanzyou.model.transaction;

import java.util.List;

public interface SATransaction {

    TTransaction addTransaction(TTransaction tTransaction) throws Exception;
    List<TTransaction> listTransactionsByClient(int clientID) throws Exception;
    void deleteTransaction(int id);
}
