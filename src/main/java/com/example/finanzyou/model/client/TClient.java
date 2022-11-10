package com.example.finanzyou.model.client;

import lombok.Data;

@Data
public class TClient {

    private int id;
    private String username;
    private String password;
    private String dni;
    private boolean active;

    public TClient(int id, String username, String password, String dni, boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.dni = dni;
        this.active = active;
    }

    public TClient() {}
}
