package com.example.finanzyou.model.client.imp;

import com.example.finanzyou.model.client.Client;
import com.example.finanzyou.model.client.DetailsPortfolio;
import com.example.finanzyou.model.client.SAClient;
import com.example.finanzyou.model.client.TClient;
import com.example.finanzyou.model.transaction.Transaction;
import com.example.finanzyou.repository.RepositoryClient;
import com.example.finanzyou.repository.RepositoryTransaction;
import com.example.finanzyou.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class SAClientImp implements SAClient {

    private static final Logger log = LoggerFactory.getLogger(SAClientImp.class);

    @Autowired
    private RepositoryClient repositoryClient;

    @Autowired
    private RepositoryTransaction repositoryTransaction;

    @Override
    public TClient createClient(TClient tClient) throws Exception {
        Optional<Client> auxClient = repositoryClient.findClientByDni(tClient.getDni());
        Optional<Client> auxClient2 = repositoryClient.findClientByUsername(tClient.getUsername());

        ValidationException e = null;
        if(tClient.getUsername().equals("")) {
            e = new InvalidEmptyNameException();
        } else if (auxClient.isPresent() && auxClient.get().isActive()) {
            e = new InvalidDniExistsException();
        } else if (auxClient2.isPresent() && auxClient2.get().isActive()) {
            e = new InvalidNameException();
        } else if (!validateDni(tClient.getDni())) {
            e = new InvalidDniException();
        } else if (validateUser(tClient.getUsername())) {
            e = new InvalidUserFormatException();
        }else if (!validatePassword(tClient.getPassword())) {
            e = new InvalidPasswordFormatException();
        }

        if (Optional.ofNullable(e).isPresent()) {
            log.warn("User creation has not passed validation rules: {}",
                    e.getMessage());
            throw e;
        }

        log.debug("User: {} has passed validation rules", tClient.getUsername());

        Client client;

        if(auxClient.isPresent()) {
            client = auxClient.get();
            client.setActive(true);
            client.setDni(tClient.getDni());
            client.setPassword(tClient.getPassword());
            client.setUsername(tClient.getUsername());
        } else {
            tClient.setActive(true);
            client = new Client(tClient);
        }

        Client clientSaved = repositoryClient.save(client);

        return clientSaved.toTransfer();
    }

    @Override
    public TClient removeClient(int id) throws Exception {
        Optional<Client> auxClient = repositoryClient.findClientById(id);

        ValidationException e = null;
        if(!auxClient.isPresent() || (auxClient.isPresent() && !auxClient.get().isActive())) {
            e = new InvalidUserNotExistsException();
        }

        if (Optional.ofNullable(e).isPresent()) {
            log.warn("Client deletion has not passed validation rules: {}",
                    e.getMessage());
            throw e;
        }

        auxClient.get().setActive(false);
        Client clientSaved = repositoryClient.save(auxClient.get());
        return clientSaved.toTransfer();
    }

    @Override
    public TClient login(TClient tClient) throws Exception {
        Optional<Client> auxUser = repositoryClient.findClientByUsername(tClient.getUsername());

        ValidationException e = null;
        if(!auxUser.isPresent() || (auxUser.isPresent() && !auxUser.get().isActive())){
            e = new InvalidUserNotExistsException();
        } else if (!auxUser.get().getPassword().equals(tClient.getPassword())){
            e = new InvalidPasswordException();
        }

        if (Optional.ofNullable(e).isPresent()) {
            log.warn("Client login failed: {}",
                    e.getMessage());
            throw e;
        }

        return auxUser.get().toTransfer();
    }

    @Override
    public TClient updateClient(TClient tClient) throws Exception {
        return null;
    }

    @Override
    public TClient showDetails(int id) throws Exception {
        Optional<Client> auxClient = repositoryClient.findClientById(id);

        ValidationException e = null;
        if(!auxClient.isPresent() || (auxClient.isPresent() && !auxClient.get().isActive())) {
            e = new InvalidUserNotExistsException();
        }

        if (Optional.ofNullable(e).isPresent()) {
            log.warn("Show details of client has not passed validation rules: {}",
                    e.getMessage());
            throw e;
        }

        return auxClient.get().toTransfer();
    }

    @Override
    public HashMap<String, DetailsPortfolio> calculatePortfolio(int id) throws Exception {
        Optional<Client> auxClient = repositoryClient.findClientById(id);

        ValidationException e = null;
        if(!auxClient.isPresent() || (auxClient.isPresent() && !auxClient.get().isActive())) {
            e = new InvalidUserNotExistsException();
        }

        if (Optional.ofNullable(e).isPresent()) {
            log.warn("Calculate Portfolio of client has not passed validation rules: {}",
                    e.getMessage());
            throw e;
        }

        List<Transaction> transactionList = repositoryTransaction.findByIdClient(id);
        HashMap<String, DetailsPortfolio> portfolio = new HashMap<String, DetailsPortfolio>();
        for(Transaction t : transactionList) {
            if(portfolio.containsKey(t.getStock().getId())) {
                DetailsPortfolio detailsPortfolio = portfolio.get(t.getStock().getId());

                // Calculating the average price of the asset
                int totalQuantity = detailsPortfolio.getQuantity() + t.getQuantity();
                double newBuyPrice = ((detailsPortfolio.getBuyPrice() * detailsPortfolio.getQuantity()) + (t.getBuyPrice() * t.getQuantity())) / totalQuantity;
                detailsPortfolio.setBuyPrice(newBuyPrice);

                detailsPortfolio.setQuantity(totalQuantity);

                portfolio.put(t.getStock().getId(), detailsPortfolio);
            }
            else {
                portfolio.put(t.getStock().getId(), new DetailsPortfolio(t.getQuantity(), t.getBuyPrice(), t.getStock().getSector()));
            }
        }

        return portfolio;

    }

    public boolean validateDni(String dni) {
        if (dni == null)
            return false;
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        if (dni.length() != 9)
            return false;
        if (!Pattern.matches("^\\d{1,8}[" + letras + "]$", dni))
            return false;
        int num;
        try {
            num = Integer.parseInt(dni.substring(0, 8));
        } catch (NumberFormatException nfe) {
            return false;
        }
        return dni.charAt(8) == letras.charAt(num % 23);
    }

    public boolean validateUser(String user) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9á-üÁ-Ü-_. ]");
        Matcher matcher = pattern.matcher(user);
        return matcher.find();
    }

    public boolean validatePassword(String password) {
        if(password.isEmpty()) {
            return false;
        }
        byte[] decodedBytes = Base64.getDecoder().decode(password);
        String decodedString = new String(decodedBytes);
        String decryptedPassword = decodedString.substring(0, decodedString.length() - 9);
        if (decryptedPassword.length() < 8) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).{8,}$");
        Matcher matcher = pattern.matcher(decryptedPassword);
        return matcher.find();
    }

    @Override
    public TClient editClient(TClient tClient) throws ValidationException {
        Optional<Client> auxClient = repositoryClient.findClientById(tClient.getId());
        ValidationException e = null;

        if(!auxClient.isPresent() || (auxClient.isPresent() && !auxClient.get().isActive())) {
            e = new InvalidUserNotExistsException();
        }

        if(tClient.getUsername().equals("") && tClient.getPassword().equals("")) {
            e = new InvalidEmptyNameException();
        } else if (validateUser(tClient.getUsername()) && !validatePassword(tClient.getPassword())) {
            e = new InvalidUserFormatException();
        }

        if (Optional.ofNullable(e).isPresent()) {
            log.warn("User creation has not passed validation rules: {}",
                    e.getMessage());
            throw e;
        }

        log.debug("User: {} has passed validation rules", tClient.getUsername());

        Client client = auxClient.get().editAtributes(tClient.getUsername(), tClient.getPassword());
        Client clientSaved = repositoryClient.save(client);

        return clientSaved.toTransfer();
    }

    @Override
    public void checkOldPassword(TClient tClient) throws ValidationException {
        Optional<Client> auxClient = repositoryClient.findClientById(tClient.getId());

        ValidationException e = null;

        if(!auxClient.isPresent() || (auxClient.isPresent() && !auxClient.get().isActive())) {
            e = new InvalidUserNotExistsException();
        }

        TClient auxTClient = auxClient.get().toTransfer();

        if(tClient.getPassword().equals("")) {
            e = new InvalidEmptyNameException();
        } else if (!validatePassword(tClient.getPassword())) {
            e = new InvalidPasswordFormatException();
        } else if (!tClient.getPassword().equals(auxTClient.getPassword())) {
            e = new InvalidPasswordException();
        }

        if (Optional.ofNullable(e).isPresent()) {
            log.warn("User check old password has not passed validation rules: {}",
                    e.getMessage());
            throw e;
        }

        log.debug("User: {} has passed validation rules", tClient.getUsername());
    }

}
