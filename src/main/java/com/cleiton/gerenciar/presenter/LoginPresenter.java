
package com.cleiton.gerenciar.presenter;

import javax.swing.JOptionPane;

import com.cleiton.gerenciar.dao.UsuarioDAO;
import com.cleiton.gerenciar.factory.PasswordEncryptor;
import com.cleiton.gerenciar.view.LoginView;

public class LoginPresenter {

    // ATTRIBUTES
    private final LoginView view;
    private final int countUsers;
    private final UsuarioDAO userDAO;

    // CONSTRUCTOR
    public LoginPresenter() {
        view = new LoginView();
        userDAO = new UsuarioDAO();
        countUsers = UsuarioDAO.countUsers();

        view.getBtnLogin().addActionListener(l -> {
            login();
        });

        view.getBtnRegister().addActionListener(l -> {
            register();
        });

        view.getCheckShowPassword().addActionListener(l -> {
            if (view.getCheckShowPassword().isSelected()) {
                view.getTxtPassword().setEchoChar((char) 0);
            } else {
                view.getTxtPassword().setEchoChar('*');
            }
        });

        view.setLocationRelativeTo(view.getParent());
        view.setVisible(true);
    }

    // METHODS
    private void register() {
        if (countUsers == 0) {
            new CadastrarUsuarioAdministradorPresenter(true);
        } else {
            new CadastrarUsuarioPresenter();
        }
    }

    private void login() {
        var username = view.getTxtUser().getText();
        var password = String.valueOf(view.getTxtPassword().getPassword());

        if (username.isBlank() || username.isEmpty()) {

            JOptionPane.showMessageDialog(view, "Informe o nome de usuário.");

        } else if (password.isBlank() || password.isEmpty()) {

            JOptionPane.showMessageDialog(view, "Informe uma senha.");

        } else {

            try {
                var user = userDAO.login(username, PasswordEncryptor.encrypt(password));

                if (user == null) {
                    JOptionPane.showMessageDialog(view, "Credenciais incorretas. Verifique sua senha e email.");
                } else {

                    new PrincipalPresenter(user);
                    view.dispose();
                }
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(view, "Erro ao realizar login." + e.getMessage());

            }
        }

    }

}
