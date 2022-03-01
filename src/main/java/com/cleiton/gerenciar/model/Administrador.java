package com.cleiton.gerenciar.model;

import java.time.LocalDate;

public class Administrador extends UserModel {

    public Administrador(String name, String email, String username, String password, LocalDate data) {
        super(name, email, username, password, data);
    }
    
}
