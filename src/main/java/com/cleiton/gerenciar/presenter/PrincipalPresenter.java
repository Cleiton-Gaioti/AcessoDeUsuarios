package com.cleiton.gerenciar.presenter;

import javax.swing.JOptionPane;

import com.cleiton.gerenciar.dao.UsuarioDAO;
import com.cleiton.gerenciar.factory.ConnectionSQLite;
import com.cleiton.gerenciar.model.Administrador;
import com.cleiton.gerenciar.model.UserModel;
import com.cleiton.gerenciar.model.interfaces.Observer;
import com.cleiton.gerenciar.view.PrincipalView;

public class PrincipalPresenter implements Observer {

    // ATTRIBUTES
    private static PrincipalView view;
    private static UserModel user;

    // MAIN
    public static void main(String[] args) {
        view = new PrincipalView();

        init();
    }

    // METHODS
    private static void init() {

        ConnectionSQLite.checkDiretorioDb();

        try {
            UsuarioDAO.createTableUsuarios();
        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(view, e.getMessage());
            System.exit(1);
        }

        new LoginPresenter(view.getDesktop());

        view.setSize(900, 600);
        view.setVisible(true);
        view.setLocationRelativeTo(view.getParent());
    }

    private static void updateFooter(UserModel u) {
        var isAdmin = Administrador.class.isInstance(u);

        if (isAdmin) {
            view.getTxtUser().setText("Administrador - " + u.getName());
        } else {
            view.getTxtUser().setText("Usuário - " + u.getName());
        }
    }

    @Override
    public void update(Object obj) {
        user = (UserModel) obj;

        updateFooter(user);
    }
}
