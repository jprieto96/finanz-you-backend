package com.example.finanzyou.model.client;

import com.example.finanzyou.model.transaction.Transaction;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String dni;
    private boolean active;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;


    public Client(TClient tClient){
        this.username = tClient.getUsername();
        this.password = tClient.getPassword();
        this.dni = tClient.getDni();
        this.active = tClient.isActive();
    }

    public Client() {}

    public TClient toTransfer() {
        return new TClient(id, username, password, dni, active);
    }

    public Client editAtributes(String newUsername, String newPassword){

        this.username = newUsername;
        this.password = newPassword;

        return this;
    }

}
