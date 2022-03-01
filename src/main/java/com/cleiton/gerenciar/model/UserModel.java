package com.cleiton.gerenciar.model;

import java.time.LocalDate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.cleiton.gerenciar.factory.PasswordEncryptor;

public abstract class UserModel {
    
    // ATTRIBUTES
    private int id;
    private String name;
    private String email;
    private String username;
    private String password;
    private LocalDate dataCadastro;

    // CONSTRUCTOR
    public UserModel(int id, String name, String email, String username, String password, LocalDate data) {
        setId(id);
        setName(name);
        setEmail(email);
        setUsername(username);
        setPassword(password);
        setDataCadastro(data);
    }

    public UserModel(String name, String email, String username, String password, LocalDate data) {
        this(-1, name, email, username, password, data);
    }

    // GETTERS AND SETTERS

    public int getId() {
        return this.id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    private void setName(String name) {
        
        if(name == null || name.isBlank() || name.isEmpty()) {
            throw new RuntimeException("Nome inválido.");
        } else {
            this.name = name;
        }
    }

    public String getEmail() {
        return this.email;
    }

    private void setEmail(String email) {
        if(email == null || email.isBlank() || email.isEmpty()) {
            throw new RuntimeException("Email inválido");
        } else {
            try {
                InternetAddress emailAddr = new InternetAddress(email);
                emailAddr.validate();

                this.email = email;
            } catch (AddressException e) {
                throw new RuntimeException("Email inválido.");
            }
        }
    }

    public String getUsername() {
        return this.username;
    }

    private void setUsername(String username) {
        if(username == null || username.isBlank() || username.isEmpty()) {
            throw new RuntimeException("Nome de usuário inválido.");
        } else {
            this.username = username;
        }
    }

    public String getPassword() {
        return this.password;
    }

    private void setPassword(String password) {
        if(password == null || password.isBlank() || password.isEmpty()) {
            throw new RuntimeException("Senha inválida. A senha deve conter de 6 a 10 caracteres e conter letras minúsculas e maiúscula,pelo menos um dígito e um caractere especial");
        } else {
            this.password = PasswordEncryptor.encrypt(password);
        }

        // TODO: add verificação de senha.
    }

    public LocalDate getDataCadastro() {
        return this.dataCadastro;
    }

    private void setDataCadastro(LocalDate data) {
        if(data == null) {
            throw new RuntimeException("Data de cadastro inválida.");
        } else {

            this.dataCadastro = data;
        }
    }
}
