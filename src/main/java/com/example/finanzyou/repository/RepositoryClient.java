package com.example.finanzyou.repository;


import com.example.finanzyou.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RepositoryClient extends JpaRepository<Client, Integer>, JpaSpecificationExecutor<Client> {
    Optional<Client> findClientByUsername (String username);
    Optional<Client> findClientById(int idUser);
    Optional<Client> findClientByDni(String dni);
}
