package com.cleiton.gerenciar.presenter;

import java.time.LocalDate;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.cleiton.gerenciar.dao.UsuarioDAO;
import com.cleiton.gerenciar.model.Administrador;
import com.cleiton.gerenciar.model.Usuario;
import com.cleiton.gerenciar.model.UserModel;
import com.cleiton.gerenciar.view.CadastrarUsuarioAdministradorView;

public class CadastrarUsuarioAdministradorPresenter {

    // ATTRIBUTES
    private final CadastrarUsuarioAdministradorView view;
    private final UsuarioDAO userDAO;

    // CONSTRUCTOR
    public CadastrarUsuarioAdministradorPresenter(JDesktopPane desktop) {
        this(desktop, false);
    }

    public CadastrarUsuarioAdministradorPresenter(JDesktopPane desktop, boolean firstUser) {
        view = new CadastrarUsuarioAdministradorView();
        userDAO = new UsuarioDAO();

        if (firstUser) {
            view.getCheckAdministrador().setSelected(true);
            view.getCheckAdministrador().setEnabled(false);
        }

        view.getBtnClose().addActionListener(l -> {
            view.dispose();
        });

        view.getBtnRegister().addActionListener(l -> {
            register();
        });

        desktop.add(view);
        view.setVisible(true);
    }

    // METHODS
    private void register() {
        final var name = view.getTxtName().getText();
        final var email = view.getTxtEmail().getText();
        final var username = view.getTxtUsername().getText();
        final var password = view.getTxtPassword().getText();
        final var administrador = view.getCheckAdministrador().isSelected();
        final var data = LocalDate.now();
        
        if (!userDAO.verifyEmail(email)) {
            JOptionPane.showMessageDialog(view, "Endereço de e-mail já cadastrado.");
        }

        if (!userDAO.verifyUsername(username)) {
            JOptionPane.showMessageDialog(view, "Nome de usuário já em uso.");
        }

        UserModel newUser;

        try {

            if (administrador) {
                newUser = new Administrador(name, email, username, password, data);
            } else {
                newUser = new Usuario(name, email, username, password, data);
            }

            userDAO.insert(newUser);

            view.dispose();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(view, "Erro ao realizar o cadastro: " + e.getMessage());
        }
    }

}
