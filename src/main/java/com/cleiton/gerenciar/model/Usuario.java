package com.cleiton.gerenciar.model;

import java.time.LocalDate;

public class Usuario extends UserModel {

    public Usuario(String name, String email, String username, String password, LocalDate data) {
        super(name, email, username, password, data);
    }
    
}
