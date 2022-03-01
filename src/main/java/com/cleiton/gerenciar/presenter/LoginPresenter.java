
package com.cleiton.gerenciar.presenter;

import javax.swing.JDesktopPane;

import com.cleiton.gerenciar.dao.UsuarioDAO;
import com.cleiton.gerenciar.view.LoginView;

public class LoginPresenter {

    // ATTRIBUTES
    private final LoginView view;
    private final JDesktopPane desktop;
    private final int countUsers;

    // CONSTRUCTOR
    public LoginPresenter(JDesktopPane desktop) {
        view = new LoginView();
        this.desktop = desktop;
        countUsers = UsuarioDAO.countUsers();

        view.getBtnLogin().addActionListener(l -> {
            login();
        });

        view.getBtnRegister().addActionListener(l -> {
            register();
        });

        desktop.add(view);
        view.setLocation(250, 160);
        view.setVisible(true);
    }

    // METHODS
    private void register() {
        if(countUsers == 0) {
            new CadastrarUsuarioAdministradorPresenter(desktop, true);
        } else {
            new CadastrarUsuarioPresenter(desktop);
        }
    }

    private void login() {
    }
}
