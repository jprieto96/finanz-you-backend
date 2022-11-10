package com.example.finanzyou.controller;

import com.example.finanzyou.model.client.DetailsPortfolio;
import com.example.finanzyou.model.client.SAClient;
import com.example.finanzyou.model.client.TClient;
import com.example.finanzyou.model.stock.SAStock;
import com.example.finanzyou.model.stock.TStock;
import com.example.finanzyou.model.transaction.SATransaction;
import com.example.finanzyou.model.transaction.TTransaction;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/")
public class WebController {

    private static final Logger log = LoggerFactory.getLogger(WebController.class);

    private static final String INTERNAL_SERVER_ERROR = "An unexpected server error has occurred";

    @Autowired
    SAClient saClient;

    @Autowired
    SATransaction saTransaction;

    @Autowired
    SAStock saStock;

    @PostMapping(path = "/client/create", consumes = "application/json")
    public String createClient(@RequestBody TClient tClient, HttpServletResponse response) {

        log.debug("Initiating POST operation: createUser for user: {}", tClient);

        TClient newClient = new TClient();

        Optional<TClient> optional = null;
        try {
            optional = Optional.ofNullable(saClient.createClient(tClient));
        } catch (Exception e) {
            log.error("The service responds with the following error: {}", e.getMessage());
            response.setStatus(400);
            return e.getMessage();
        }

        if (optional.isPresent()) {
            response.setStatus(HttpServletResponse.SC_OK);
            newClient = optional.get();
        } else {
            log.error("The service does not respond correctly");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        log.debug("The user has been created correctly: {}", newClient);

        return new Gson().toJson(newClient);
    }

    @GetMapping(path = "/client/showDetails/{id}")
    public String showDetailsClient(@PathVariable(value = "id") String id, HttpServletResponse response) {
        log.debug("Initiating show details method for client with ID: {}", id);

        int clientId = Integer.valueOf(new String(Base64.getDecoder().decode(id)).split("\\s+")[0]);
        TClient clientToShow;
        try {
            clientToShow = saClient.showDetails(clientId);
        } catch (Exception e) {
            log.error("Service error: {}", e.getMessage());
            response.setStatus(400);
            return e.getMessage();
        }
        if (clientToShow != null) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            log.error("The service has failed");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return INTERNAL_SERVER_ERROR;
        }

        log.debug("Information correctly loaded for the client with ID: {}", clientId);

        return new Gson().toJson(clientToShow);
    }

    @GetMapping(path = "/client/showTransactions/{id}")
    public String showTransactionsByClient(@PathVariable(value = "id") String id, HttpServletResponse response) {
        log.debug("Initiating show transactions method for client with ID: {}", id);

        int clientId = Integer.valueOf(new String(Base64.getDecoder().decode(id)).split("\\s+")[0]);
        List<TTransaction> transactionList;
        try {
            transactionList = saTransaction.listTransactionsByClient(clientId);
        } catch (Exception e) {
            log.error("Service error: {}", e.getMessage());
            response.setStatus(400);
            return e.getMessage();
        }
        response.setStatus(HttpServletResponse.SC_OK);
        log.debug("Information correctly loaded for the client with ID: {}", clientId);
        return new Gson().toJson(transactionList);
    }

    @GetMapping(path = "/getStocks")
    public String getStocks(HttpServletResponse response) {
        log.debug("Initiating getStocks method");
        List<TStock> stockList = saStock.listStocks();

        if (stockList != null) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            log.error("The service has failed");
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return INTERNAL_SERVER_ERROR;
        }
        log.debug("Information correctly loaded");
        return new Gson().toJson(stockList);
    }

    @GetMapping(path = "/client/showPortfolio/{id}")
    public String showPortfolio(@PathVariable(value = "id") String id, HttpServletResponse response) {
        log.debug("Initiating show portfolio method for client with ID: {}", id);

        int clientId = Integer.valueOf(new String(Base64.getDecoder().decode(id)).split("\\s+")[0]);
        HashMap<String, DetailsPortfolio> portfolio;
        try {
            portfolio = saClient.calculatePortfolio(clientId);
        } catch (Exception e) {
            log.error("Service error: {}", e.getMessage());
            response.setStatus(400);
            return e.getMessage();
        }
        if (portfolio != null) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            log.error("The service has failed");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return INTERNAL_SERVER_ERROR;
        }

        log.debug("Information correctly loaded for the client with ID: {}", clientId);

        return new Gson().toJson(portfolio);
    }

    @PostMapping(path = "/login", consumes = "application/json")
    public String login(@RequestBody TClient tClient, HttpServletResponse response, HttpServletRequest request) {
        try {
            TClient client = saClient.login(tClient);
            String data = client.getId() + " " + client.getDni() + " " + client.getUsername();
            return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e){
            log.error("El servicio ha respondido con el siguiente error: {}", e.getMessage());
            response.setStatus(400);
            return e.getMessage();
        }
    }

    @PostMapping(path = "/client/addTransaction", consumes = "application/json")
    public String addTransaction(@RequestBody TTransaction tTransaction, HttpServletResponse response) {

        log.debug("Initiating POST operation: addTransaction for client: {}", tTransaction.getIdClient());

        TTransaction newTransaction = new TTransaction();

        Optional<TTransaction> opt = null;
        try {
            opt = Optional.ofNullable(saTransaction.addTransaction(tTransaction));
        } catch (Exception e) {
            log.error("The service responds with the following error: {}", e.getMessage());
            response.setStatus(400);
            return e.getMessage();
        }

        if (opt.isPresent()) {
            response.setStatus(HttpServletResponse.SC_OK);
            newTransaction = opt.get();
        } else {
            log.error("The service does not respond correctly");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        log.debug("The transaction has been created correctly: {}", newTransaction);

        return new Gson().toJson(newTransaction);
    }

    @PostMapping(path = "/client/deleteTransaction", consumes = "application/json")
    public void deleteTransaction(@RequestBody TTransaction tTransaction, HttpServletResponse response) {

        log.debug("Initiating POST operation: delete transaction");

        try {
            saTransaction.deleteTransaction(tTransaction.getId());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error(e.getMessage());
        }

        log.debug("The transaction has been deleted correctly");
    }

    @PostMapping(path = "/client/editClient", consumes = "application/json")
    public String editUserProfile(@RequestBody TClient tClient, HttpServletResponse response){

        log.debug("Initiating edit user profile method for client: {}", tClient);

        TClient clientToEdit = new TClient();

        Optional<TClient> optional = null;
        try {
            optional = Optional.ofNullable(saClient.editClient(tClient));
        } catch (Exception e) {
            log.error("The service responds with the following error: {}", e.getMessage());
            response.setStatus(400);
            return e.getMessage();
        }

        if (optional.isPresent()) {
            response.setStatus(HttpServletResponse.SC_OK);
            clientToEdit = optional.get();
        } else {
            log.error("The service does not respond correctly");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        log.debug("Information correctly edited for the client: {}", tClient);

        return new Gson().toJson(clientToEdit);
    }

    @PostMapping(path = "/client/oldPasswordCheck", consumes = "application/json")
    public boolean checkOldPassword(@RequestBody TClient tClient, HttpServletResponse response){

        log.debug("Initiating check old password method for client: {}", tClient);

        try {
            saClient.checkOldPassword(tClient);
        } catch (Exception e) {
            log.error("The service responds with the following error: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            return false;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        log.debug("Information correctly edited for the client: {}", tClient);

        return true;
    }
}
