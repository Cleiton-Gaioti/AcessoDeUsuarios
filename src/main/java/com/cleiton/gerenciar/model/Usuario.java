package com.cleiton.gerenciar.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Usuario extends UserModel {

    // ATTRIBUTES
    private boolean authorized;

    // CONSTRUCTORS
    public Usuario(int id, String name, String email, String username, String password, LocalDate data,
            boolean authorized, List<Notification> notifications) {
        super(id, name, email, username, password, data, notifications);
        setAuthorized(authorized);
    }

    public Usuario(int id, String name, String email, String username, String password, LocalDate data, boolean authorized) {
        this(id, name, email, username, password, data, authorized, new ArrayList<>());
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
