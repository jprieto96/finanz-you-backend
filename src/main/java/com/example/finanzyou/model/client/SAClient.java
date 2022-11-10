package com.example.finanzyou.model.client;

import com.example.finanzyou.validation.ValidationException;

import java.util.HashMap;

public interface SAClient {

    TClient createClient(TClient tClient) throws Exception;
    TClient removeClient(int id) throws Exception;
    TClient login(TClient tClient) throws Exception;
    TClient updateClient(TClient tClient) throws Exception;
    TClient showDetails(int id) throws Exception;
    HashMap<String, DetailsPortfolio> calculatePortfolio(int id) throws Exception;

    TClient editClient(TClient tClient) throws ValidationException;

    void checkOldPassword(TClient tClient) throws ValidationException;
}
