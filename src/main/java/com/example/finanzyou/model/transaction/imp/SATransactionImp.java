package com.example.finanzyou.model.transaction.imp;

import com.example.finanzyou.model.client.Client;
import com.example.finanzyou.model.stock.Stock;
import com.example.finanzyou.model.transaction.SATransaction;
import com.example.finanzyou.model.transaction.TTransaction;
import com.example.finanzyou.model.transaction.Transaction;
import com.example.finanzyou.repository.RepositoryClient;
import com.example.finanzyou.repository.RepositoryStock;
import com.example.finanzyou.repository.RepositoryTransaction;
import com.example.finanzyou.validation.InvalidUserNotExistsException;
import com.example.finanzyou.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SATransactionImp implements SATransaction {

    private static final Logger log = LoggerFactory.getLogger(SATransaction.class);

    @Autowired
    private RepositoryClient repositoryClient;

    @Autowired
    private RepositoryStock repositoryStock;

    @Autowired
    private RepositoryTransaction repositoryTransaction;

    @Override
    public TTransaction addTransaction(TTransaction tTransaction) throws Exception {
        Optional<Client> auxUser = repositoryClient.findClientById(tTransaction.getIdClient());
        Optional<Stock> auxStock = repositoryStock.findStockById(tTransaction.getStockID());

        ValidationException e = null;
        Transaction transaction = tTransaction.toEntity();
        if(auxUser.isPresent() && auxUser.get().isActive() && auxStock.isPresent()) {
            transaction.setClient(auxUser.get());
            transaction.setStock(auxStock.get());
        }
        else {
            if(!auxUser.isPresent()) {
                e = new InvalidUserNotExistsException();
            }
            else if(!auxStock.isPresent()) {
                Stock stock = new Stock();
                stock.setId(tTransaction.getStockID());
                stock.setName(tTransaction.getStockName());
                stock.setSector(tTransaction.getStockSector());
                repositoryStock.save(stock);
                transaction.setStock(stock);
                transaction.setClient(auxUser.get());
            }
        }

        if (Optional.ofNullable(e).isPresent()) {
            log.warn("El login del cliente ha fallado: {}",
                    e.getMessage());
            throw e;
        }

        Transaction transactionSaved = repositoryTransaction.save(transaction);

        return transactionSaved.toTransfer();

    }

    @Override
    public List<TTransaction> listTransactionsByClient(int clientID) throws Exception {
        List<Transaction> transactionList = repositoryTransaction.findByIdClient(clientID);
        List<TTransaction> listTransactions = new ArrayList<>();
        for (Transaction t : transactionList) {
            listTransactions.add(t.toTransfer());
        }
        return listTransactions;
    }

    @Override
    public void deleteTransaction(int id) {
        repositoryTransaction.deleteAllById(id);
    }

}
