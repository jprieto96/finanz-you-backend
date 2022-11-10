package com.example.finanzyou.model.client.imp;

import com.example.finanzyou.model.client.Client;
import com.example.finanzyou.model.client.SAClient;
import com.example.finanzyou.model.client.TClient;
import com.example.finanzyou.repository.RepositoryClient;
import com.example.finanzyou.validation.InvalidDniExistsException;
import com.example.finanzyou.validation.InvalidNameException;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SAClientImpTest {

    @Mock
    private RepositoryClient repositoryClient;

    @InjectMocks
    private SAClient saClient = new SAClientImp();

    private AutoCloseable autoCloseable;
    private Client clientIn;
    private Client clientOut;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        clientIn = new Client();
        clientIn.setActive(true);
        clientIn.setDni("47836822L");
        clientIn.setUsername("test");
        clientIn.setPassword("$2a$12$hIY8cgdl5GMIiwJ35FwWRe7.HMFQATHhi5rBJfkD8rQ1raKafCfEW"); // testtfgPROT01

        clientOut = new Client();
        clientOut.setId(1);
        clientOut.setActive(true);
        clientOut.setDni("47836822L");
        clientOut.setUsername("test");
        clientOut.setPassword("$2a$12$hIY8cgdl5GMIiwJ35FwWRe7.HMFQATHhi5rBJfkD8rQ1raKafCfEW"); // testtfgPROT01
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void createClientOK() {
        when(repositoryClient.save(clientIn)).thenReturn(clientOut);
        when(repositoryClient.findClientByDni(clientIn.getDni())).thenReturn(Optional.ofNullable(null));
        when(repositoryClient.findClientByUsername(clientIn.getUsername())).thenReturn(Optional.ofNullable(null));

        try {
            TClient testEscapeRoom = saClient.createClient(clientIn.toTransfer());
            Assertions.assertEquals(testEscapeRoom.getId(), 1);
            Assertions.assertEquals(testEscapeRoom.getUsername(), clientOut.getUsername());
            Assertions.assertTrue(testEscapeRoom.isActive());
            Assertions.assertEquals(testEscapeRoom.getDni(), clientOut.getDni());
            Assertions.assertEquals(testEscapeRoom.getPassword(), clientOut.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void createClientDNIalreadyExists() {
        when(repositoryClient.save(clientIn)).thenReturn(clientOut);
        when(repositoryClient.findClientByDni(clientIn.getDni())).thenReturn(Optional.ofNullable(clientOut));
        when(repositoryClient.findClientByUsername(clientIn.getUsername())).thenReturn(Optional.ofNullable(null));

        try {
            saClient.createClient(clientIn.toTransfer());
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), InvalidDniExistsException.class);
            Assertions.assertEquals(e.getMessage(), "DNI already exists in BBDD");
        }
    }

    @Test
    void createClientUsernameAlreadyExists() {
        when(repositoryClient.save(clientIn)).thenReturn(clientOut);
        when(repositoryClient.findClientByDni(clientIn.getDni())).thenReturn(Optional.ofNullable(null));
        when(repositoryClient.findClientByUsername(clientIn.getUsername())).thenReturn(Optional.ofNullable(clientOut));

        try {
            saClient.createClient(clientIn.toTransfer());
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), InvalidNameException.class);
            Assertions.assertEquals(e.getMessage(), "Username already exists in BBDD");
        }
    }

    @Test
    void removeClient() {
    }

    @Test
    void login() {
    }

    @Test
    void updateClient() {
    }

    @Test
    void showDetails() {
    }

    @Test
    void calculatePortfolio() {
    }

    @Test
    void editClient() {
    }

    @Test
    void checkOldPassword() {
    }
}