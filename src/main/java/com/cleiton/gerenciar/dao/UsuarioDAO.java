package com.cleiton.gerenciar.dao;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cleiton.gerenciar.collection.NotificationCollection;
import com.cleiton.gerenciar.factory.ConnectionSQLite;
import com.cleiton.gerenciar.model.Administrador;
import com.cleiton.gerenciar.model.UserModel;
import com.cleiton.gerenciar.model.Usuario;

public class UsuarioDAO {

    public static void createTableUsuarios() {
        var query = "CREATE TABLE IF NOT EXISTS usuario("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name VARCHAR NOT NULL, "
                + "email VARCHAR NOT NULL UNIQUE, "
                + "username VARCHAR NOT NULL UNIQUE, "
                + "password VARCHAR NOT NULL, "
                + "dateRegister DATE NOT NULL, "
                + "administrator INT DEFAULT 0, "
                + "authorized INT DEFAULT 0 "
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

    public void insert(UserModel newUser, boolean authorized) {
        var query = "INSERT INTO usuario (name, email, username, password, dateRegister, administrator, authorized) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            var isAdmin = false;

            if (Administrador.class.isInstance(newUser)) {
                isAdmin = true;
                authorized = true;
            }

            ps.setString(1, newUser.getName());
            ps.setString(2, newUser.getEmail());
            ps.setString(3, newUser.getUsername());
            ps.setString(4, newUser.getPassword());
            ps.setDate(5, Date.valueOf(newUser.getDataCadastro()));
            ps.setInt(6, isAdmin ? 1 : 0);
            ps.setInt(7, authorized ? 1 : 0);

            ps.executeUpdate();

            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usu??rio: " + e.getMessage());
        }
    }

    public UserModel login(String username, String password) {
        var query = "SELECT * FROM usuario WHERE LOWER(username) = LOWER(?) AND password = ?";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            UserModel user = null;

            if (rs.next()) {
                var id = rs.getInt("id");
                var name = rs.getString("name");
                var email = rs.getString("email");
                username = rs.getString("username");
                password = rs.getString("password");
                var dataRegister = rs.getDate("dateRegister").toLocalDate();
                var administrator = rs.getInt("administrator") == 1;
                var authorized = rs.getInt("authorized") == 1;

                var notifications = new NotificationDAO(conn).getNotificationsByUser(id);

                if (!authorized) {
                    throw new RuntimeException(
                            "Usu??rio n??o autorizado. Espere at?? que um administrador autorize sua cria????o de usu??rio.");

                } else if (administrator) {
                    user = new Administrador(id, name, email, username, password, dataRegister, notifications);
                } else {
                    user = new Usuario(id, name, email, username, password, dataRegister, authorized, notifications);
                }
            }

            rs.close();
            ps.close();
            conn.close();

            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao realizar login.");
        }
    }

    public List<UserModel> getAllUsers() {
        var query = "SELECT * FROM usuario";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            List<UserModel> users = new ArrayList<>();

            NotificationDAO notificationDAO = new NotificationDAO(conn);

            while (rs.next()) {
                var id = rs.getInt("id");
                var name = rs.getString("name");
                var email = rs.getString("email");
                var username = rs.getString("username");
                var password = rs.getString("password");
                var dataRegister = rs.getDate("dateRegister").toLocalDate();
                var administrator = rs.getInt("administrator") == 1;
                var authorized = rs.getInt("authorized") == 1;

                NotificationCollection notifications = notificationDAO.getNotificationsByUser(id);

                if (administrator) {
                    users.add(new Administrador(id, name, email, username, password, dataRegister, notifications));
                } else {
                    users.add(
                            new Usuario(id, name, email, username, password, dataRegister, authorized, notifications));
                }
            }

            rs.close();
            ps.close();
            conn.close();

            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usu??rios.");
        }
    }

    public List<Usuario> getUsersUnauthorizeds() {
        var query = "SELECT * FROM usuario WHERE authorized = 0";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            List<Usuario> users = new ArrayList<>();

            NotificationDAO notificationDAO = new NotificationDAO(conn);

            while (rs.next()) {
                var id = rs.getInt("id");
                var name = rs.getString("name");
                var email = rs.getString("email");
                var username = rs.getString("username");
                var password = rs.getString("password");
                var dataRegister = rs.getDate("dateRegister").toLocalDate();
                var authorized = rs.getInt("authorized") == 1;

                NotificationCollection notifications = notificationDAO.getNotificationsByUser(id);

                users.add(
                        new Usuario(id, name, email, username, password, dataRegister, authorized, notifications));
            }

            rs.close();
            ps.close();
            conn.close();

            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usu??rios.");
        }
    }

    public List<UserModel> searchUsers(String text) {
        var query = "SELECT * FROM usuario "
                + "WHERE CAST(id AS VARCHAR) LIKE ? OR name LIKE ? OR username LIKE ? ORDER BY name";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, "%" + text + "%");
            ps.setString(2, "%" + text + "%");
            ps.setString(3, "%" + text + "%");

            ResultSet rs = ps.executeQuery();

            List<UserModel> users = new ArrayList<>();

            NotificationDAO notificationDAO = new NotificationDAO(conn);

            while (rs.next()) {
                var id = rs.getInt("id");
                var name = rs.getString("name");
                var email = rs.getString("email");
                var username = rs.getString("username");
                var password = rs.getString("password");
                var dataRegister = rs.getDate("dateRegister").toLocalDate();
                var administrator = rs.getInt("administrator") == 1;
                var authorized = rs.getInt("authorized") == 1;

                NotificationCollection notifications = notificationDAO.getNotificationsByUser(id);

                if (administrator) {
                    users.add(new Administrador(id, name, email, username, password, dataRegister, notifications));
                } else {
                    users.add(
                            new Usuario(id, name, email, username, password, dataRegister, authorized, notifications));
                }
            }

            rs.close();
            ps.close();
            conn.close();

            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usu??rio.");
        }
    }

    public void removeUser(int id) {
        var query = "DELETE FROM usuario WHERE id = ?";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, id);

            ps.executeUpdate();

            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usu??rio.");
        }
    }

    public UserModel getUserById(int id) {
        var query = "SELECT * FROM usuario WHERE id = ?";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            UserModel user = null;

            if (rs.next()) {
                var name = rs.getString("name");
                var email = rs.getString("email");
                var username = rs.getString("username");
                var password = rs.getString("password");
                var dataRegister = rs.getDate("dateRegister").toLocalDate();
                var administrator = rs.getInt("administrator") == 1;
                var authorized = rs.getInt("authorized") == 1;

                var notifications = new NotificationDAO(conn).getNotificationsByUser(id);

                if (administrator) {

                    user = new Administrador(id, name, email, username, password, dataRegister, notifications);

                } else {

                    user = new Usuario(id, name, email, username, password, dataRegister, authorized, notifications);

                }
            }

            rs.close();
            ps.close();
            conn.close();

            return user;
        } catch (SQLException e) {

            throw new RuntimeException("Erro ao buscar usu??rio.");

        }
    }

    public void updateUser(int idUser, String name, String email, boolean admin) {
        var query = "UPDATE usuario set name = ?, email = ?, administrator = ? WHERE id = ?";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, admin ? 1 : 0);
            ps.setInt(4, idUser);

            ps.executeUpdate();

            ps.close();
            conn.close();
        } catch (SQLException e) {

            throw new RuntimeException("Erro ao editar usu??rio.");

        }
    }

    public void updateUser(int userId, String name, String email, String username, String password) {
        var query = "UPDATE usuario set name = ?, email = ?, username = ?, password = ? WHERE id = ?";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setInt(5, userId);

            ps.executeUpdate();

            ps.close();
            conn.close();
        } catch (SQLException e) {

            throw new RuntimeException("Erro ao editar usu??rio.");

        }
    }

    public void approveSolicitation(String username) {
        var query = "UPDATE usuario SET authorized = 1 WHERE username = ?";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, username);

            ps.executeUpdate();

            ps.close();
            conn.close();
        } catch (SQLException e) {

            throw new RuntimeException("Erro ao editar usu??rio: " + e.getMessage());

        }
    }

    public int countAdmins() {
        var query = "SELECT COUNT(1) AS qtd FROM usuario WHERE administrator = 1";

        try {
            Connection conn = ConnectionSQLite.connect();
            Statement ps = conn.createStatement();

            ResultSet rs = ps.executeQuery(query);

            var admins = 0;

            if (rs.next()) {
                admins = rs.getInt("qtd");
            }

            rs.close();
            ps.close();
            conn.close();

            return admins;
        } catch (SQLException e) {

            throw new RuntimeException("Erro ao contar admins: " + e.getMessage());

        }
    }

    public void selectAdmin() {
        var query = "UPDATE usuario SET administrator = 1 WHERE id = (SELECT MIN(id) FROM usuario)";

        try {
            Connection conn = ConnectionSQLite.connect();
            Statement ps = conn.createStatement();

            ps.executeUpdate(query);

            ps.close();
            conn.close();
        } catch (SQLException e) {

            throw new RuntimeException("Erro ao selecionar novo administrador: " + e.getMessage());

        }

    }
}
