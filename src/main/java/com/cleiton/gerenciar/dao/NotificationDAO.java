package com.cleiton.gerenciar.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cleiton.gerenciar.collection.NotificationCollection;
import com.cleiton.gerenciar.factory.ConnectionSQLite;
import com.cleiton.gerenciar.model.Notification;

public class NotificationDAO {

    public static void createTableNotification() {
        var query = "CREATE TABLE IF NOT EXISTS notification("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "idAdminWhoSent INTEGER NOT NULL REFERENCES usuario (id), "
                + "idUser INTEGER NOT NULL REFERENCES usuario (id), "
                + "title VARCHAR NOT NULL, "
                + "content VARCHAR NOT NULL, "
                + "read INTEGER NOT NULL DEFAULT 0, "
                + "date DATE NOT NULL "
                + ")";

        try {
            Connection conn = ConnectionSQLite.connect();
            Statement stmt = conn.createStatement();

            stmt.execute(query);

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela notificações: " + e.getMessage());
        }
    }

    public void insert(Notification notification) {
        var query = "INSERT INTO notification (idAdminWhoSent, idUser, title, content, read, date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, notification.getIdAdminWhoSent());
            ps.setInt(2, notification.getIdUser());
            ps.setString(3, notification.getTitle());
            ps.setString(4, notification.getContent());
            ps.setInt(5, notification.getRead() ? 1 : 0);
            ps.setDate(6, Date.valueOf(notification.getDate()));

            ps.executeUpdate();

            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir notificação: " + e.getMessage());
        }
    }

    public NotificationCollection getNotificationsByUser(int idUser) {
        var query = "SELECT * FROM notification WHERE idUser = ?";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, idUser);

            ResultSet rs = ps.executeQuery();

            NotificationCollection notifications = new NotificationCollection();

            while (rs.next()) {
                var id = rs.getInt("id");
                var idAdminWhoSent = rs.getInt("idAdminWhoSent");
                idUser = rs.getInt("idUser");
                var title = rs.getString("title");
                var content = rs.getString("content");
                var read = rs.getInt("read") == 1;
                var date = rs.getDate("date").toLocalDate();

                notifications.add(new Notification(id, idAdminWhoSent, idUser, title, content, read, date));
            }

            rs.close();
            ps.close();
            conn.close();

            return notifications;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar notificações: " + e.getMessage());
        }
    }

    public Notification getNotificationsById(int id) {
        var query = "SELECT * FROM notification WHERE id = ?";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            Notification notification = null;

            if (rs.next()) {
                var idAdminWhoSent = rs.getInt("idAdminWhoSent");
                var idUser = rs.getInt("idUser");
                var title = rs.getString("title");
                var content = rs.getString("content");
                var read = rs.getInt("read") == 1;
                var date = rs.getDate("date").toLocalDate();

                notification = new Notification(id, idAdminWhoSent, idUser, title, content, read, date);
            }

            rs.close();
            ps.close();
            conn.close();

            return notification;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar notificação: " + e.getMessage());
        }
    }

    public void setReadNotification(int id) {
        var query = "UPDATE notification set read = 1 WHERE id = ?";

        try {
            Connection conn = ConnectionSQLite.connect();
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, id);

            ps.executeUpdate();

            ps.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar notificação: " + e.getMessage());
        }
    }
}
