package com.cleiton.gerenciar;

import javax.swing.JOptionPane;

import com.cleiton.gerenciar.dao.NotificationDAO;
import com.cleiton.gerenciar.dao.UsuarioDAO;
import com.cleiton.gerenciar.factory.ConnectionSQLite;
import com.cleiton.gerenciar.presenter.PrincipalPresenter;

public class Main {

    public static void main(String[] args) {
        ConnectionSQLite.checkDiretorioDb();

        try {
            UsuarioDAO.createTableUsuarios();
            NotificationDAO.createTableNotification();
        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }

        new PrincipalPresenter();
    }
}
