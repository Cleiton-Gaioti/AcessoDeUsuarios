package com.cleiton.gerenciar;

import javax.swing.JOptionPane;

import com.cleiton.gerenciar.dao.UsuarioDAO;
import com.cleiton.gerenciar.factory.ConnectionSQLite;
import com.cleiton.gerenciar.presenter.LoginPresenter;

public class Main {
    
    public static void main(String[] args) {
        ConnectionSQLite.checkDiretorioDb();

        try {
            UsuarioDAO.createTableUsuarios();
        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }

        new LoginPresenter();
    }
}
