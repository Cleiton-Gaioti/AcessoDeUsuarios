package com.cleiton.gerenciar.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Administrador extends UserModel {

    public Administrador(int id, String name, String email, String username, String password, LocalDate data,
            List<Notification> notifications) {
        super(id, name, email, username, password, data);
    }

    public Administrador(int id, String name, String email, String username, String password, LocalDate data) {
        this(id, name, email, username, password, data, new ArrayList<>());
    }

    public Administrador(String name, String email, String username, String password, LocalDate data) {
        this(-1, name, email, username, password, data);
    }
}
