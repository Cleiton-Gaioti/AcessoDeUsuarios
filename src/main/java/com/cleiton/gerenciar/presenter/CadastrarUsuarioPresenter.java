
package com.cleiton.gerenciar.presenter;

import java.time.LocalDate;

import javax.swing.JOptionPane;

import com.cleiton.gerenciar.dao.UsuarioDAO;
import com.cleiton.gerenciar.model.Usuario;
import com.cleiton.gerenciar.model.UserModel;
import com.cleiton.gerenciar.view.CadastrarUsuarioView;

public class CadastrarUsuarioPresenter {

    // ATTRIBUTES
    private final CadastrarUsuarioView view;
    private final UsuarioDAO userDAO;

    // CONSTRUCTOR
    public CadastrarUsuarioPresenter() {
        view = new CadastrarUsuarioView();
        userDAO = new UsuarioDAO();

        view.getBtnClose().addActionListener(l -> {
            view.dispose();
        });

        view.getBtnRegister().addActionListener(l -> {
            register();
        });

        view.setLocationRelativeTo(view.getParent());
        view.setVisible(true);
    }

    // METHODS
    private void register() {
        final var name = view.getTxtName().getText();
        final var email = view.getTxtEmail().getText();
        final var username = view.getTxtUsername().getText();
        final var password = view.getTxtPassword().getText();
        final var data = LocalDate.now();

        if (!userDAO.verifyEmail(email)) {

            JOptionPane.showMessageDialog(view, "Endereço de e-mail já cadastrado.");

        } else if (!userDAO.verifyUsername(username)) {

            JOptionPane.showMessageDialog(view, "Nome de usuário já em uso.");

        } else {

            try {
                UserModel newUser = new Usuario(name, email, username, password, data, false);

                userDAO.insert(newUser);

                JOptionPane.showMessageDialog(view, "Usuário cadastrado com sucesso.");
                view.dispose();
            } catch (RuntimeException e) {

                JOptionPane.showMessageDialog(view, "Erro ao realizar o cadastro: " + e.getMessage());

            }
        }
    }
}
