package com.cleiton.gerenciar.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cleiton.gerenciar.factory.ConnectionSQLite;
import com.cleiton.gerenciar.model.Administrador;
import com.cleiton.gerenciar.model.UserModel;

public class UsuarioDAO {

    public static void createTableUsuarios() {
        var query = "CREATE TABLE IF NOT EXISTS usuario("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name VARCHAR NOT NULL, "
                + "email VARCHAR NOT NULL UNIQUE, "
                + "username VARCHAR NOT NULL UNIQUE, "
                + "password VARCHAR NOT NULL, "
                + "dateRegister DATE NOT NULL, "
                + "administrator INT DEFAULT 0"
                + ")";

        try {
            Connection conn = ConnectionSQLite.connect();
            Statement stmt = conn.createStatement();

            stmt.execute(query);

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela usuario: " + e.getMessage());
        }
    }

    public static int countUsers() {
        var query = "SELECT COUNT(1) AS count FROM usuario";

        try {
            Connection conn = ConnectionSQLite.connect();
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            var count = 0;

            if (rs.next()) {
                count = rs.getInt("count");
            }

            rs.close();
            stmt.close();
            conn.close();

            return count;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar registros: " + e.getMessage());
        }
    }

    public boolean verifyUsername(String username) {
        var query = "SELECT COUNT(1) AS count FROM usuario WHERE username = ?";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            var count = 0;

            if (rs.next()) {
                count = rs.getInt("count");
            }

            rs.close();
            ps.close();
            conn.close();

            return count == 0;
        } catch (SQLException e) {

            throw new RuntimeException("Erro ao verificar senha: " + e.getMessage());
        }
    }

    public boolean verifyEmail(String email) {
        var query = "SELECT COUNT(1) AS count FROM usuario WHERE email = ?";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            var count = 0;

            if (rs.next()) {
                count = rs.getInt("count");
            }

            rs.close();
            ps.close();
            conn.close();

            return count == 0;
        } catch (SQLException e) {

            throw new RuntimeException("Erro ao verificar email: " + e.getMessage());
        }
    }

    public void insert(UserModel newUser) {
        var query = "INSERT INTO usuario (name, email, username, password, dateRegister, administrator) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            var isAdmin = Administrador.class.isInstance(newUser) ? 1 : 0;

            ps.setString(1, newUser.getName());
            ps.setString(2, newUser.getEmail());
            ps.setString(3, newUser.getUsername());
            ps.setString(4, newUser.getPassword());
            ps.setDate(5, Date.valueOf(newUser.getDataCadastro()));
            ps.setInt(6, isAdmin);

            ps.executeUpdate();

            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário: " + e.getMessage());
        }
    }
}
