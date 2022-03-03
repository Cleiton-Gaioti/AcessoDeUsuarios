
package com.cleiton.gerenciar.presenter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.cleiton.gerenciar.dao.UsuarioDAO;
import com.cleiton.gerenciar.factory.ILogger;
import com.cleiton.gerenciar.model.Usuario;
import com.cleiton.gerenciar.model.UserModel;
import com.cleiton.gerenciar.view.CadastrarUsuarioView;

public class CadastrarUsuarioPresenter {

    // ATTRIBUTES
    private final CadastrarUsuarioView view;
    private final UsuarioDAO userDAO;
    private final ILogger log;

    // CONSTRUCTOR
    public CadastrarUsuarioPresenter(JDesktopPane desktop, ILogger log) {
        view = new CadastrarUsuarioView();
        userDAO = new UsuarioDAO();
        this.log = log;

        view.getBtnClose().addActionListener(l -> {
            view.dispose();
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

        desktop.add(view);
        view.setVisible(true);
    }

    // METHODS
    private void register() {
        final var name = view.getTxtName().getText();
        final var email = view.getTxtEmail().getText();
        final var username = view.getTxtUsername().getText();
        final var password = String.valueOf(view.getTxtPassword().getPassword());
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

                log.logUsuarioCRUD(newUser, "realizou cadastro", LocalDateTime.now());

                view.dispose();
            } catch (RuntimeException e) {

                JOptionPane.showMessageDialog(view, "Erro ao realizar o cadastro: " + e.getMessage());

                log.logFalha(null, "realizar cadastro", LocalDateTime.now(), e.getMessage());
            }
        }
    }
}
