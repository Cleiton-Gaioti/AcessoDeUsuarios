package com.cleiton.gerenciar.model;

import java.time.LocalDate;

public class Administrador extends UserModel {

    public Administrador(int id, String name, String email, String username, String password, LocalDate data) {
        super(id, name, email, username, password, data);
    }

    public Administrador(String name, String email, String username, String password, LocalDate data) {
        super(-1, name, email, username, password, data);
    }
    
}
