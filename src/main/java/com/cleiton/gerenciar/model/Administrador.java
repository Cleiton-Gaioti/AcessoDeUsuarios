package com.cleiton.gerenciar.model;

import java.time.LocalDate;

import com.cleiton.gerenciar.collection.NotificationCollection;

public class Administrador extends UserModel {

    public Administrador(int id, String name, String email, String username, String password, LocalDate data,
            NotificationCollection notifications) {
        super(id, name, email, username, password, data, notifications);
    }

    public Administrador(int id, String name, String email, String username, String password, LocalDate data) {
        this(id, name, email, username, password, data, new NotificationCollection());
    }

    public Administrador(String name, String email, String username, String password, LocalDate data) {
        this(-1, name, email, username, password, data);
    }
}
