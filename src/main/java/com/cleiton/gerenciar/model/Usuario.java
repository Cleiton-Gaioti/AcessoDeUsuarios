package com.cleiton.gerenciar.model;

import java.time.LocalDate;

public class Usuario extends UserModel {

    // ATTRIBUTES
    private boolean authorized;

    // CONSTRUCTORS
    public Usuario(int id, String name, String email, String username, String password, LocalDate data,
            boolean authorized) {
        super(id, name, email, username, password, data);
        setAuthorized(authorized);
    }

    public Usuario(String name, String email, String username, String password, LocalDate data, boolean authorized) {
        this(-1, name, email, username, password, data, authorized);
    }

    // GETTERS AND SETTERS
    public boolean getAuthorized() {
        return this.authorized;
    }

    private void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }
}
