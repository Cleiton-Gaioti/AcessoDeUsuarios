package com.cleiton.gerenciar.presenter;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.cleiton.gerenciar.dao.UsuarioDAO;
import com.cleiton.gerenciar.factory.ILogger;
import com.cleiton.gerenciar.model.Administrador;
import com.cleiton.gerenciar.model.LogModel;
import com.cleiton.gerenciar.model.Usuario;
import com.cleiton.gerenciar.model.UserModel;
import com.cleiton.gerenciar.view.CadastrarUsuarioAdministradorView;
import com.pss.senha.validacao.ValidadorSenha;

public class CadastrarUsuarioAdministradorPresenter {

    // ATTRIBUTES
    private final CadastrarUsuarioAdministradorView view;
    private final UsuarioDAO userDAO;
    private final ILogger log;

    // CONSTRUCTOR
    public CadastrarUsuarioAdministradorPresenter(JDesktopPane desktop, ILogger log) {
        this(desktop, log, false);
    }

    public CadastrarUsuarioAdministradorPresenter(JDesktopPane desktop, ILogger log, boolean firstUser) {
        view = new CadastrarUsuarioAdministradorView();
        userDAO = new UsuarioDAO();
        this.log = log;

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
        final var administrador = view.getCheckAdministrador().isSelected();
        final var data = LocalDate.now();

        UserModel newUser;

        ValidadorSenha validadorSenha = new ValidadorSenha();
        var result = validadorSenha.validar(password);

        if (!userDAO.verifyEmail(email)) {

            JOptionPane.showMessageDialog(view, "Endereço de e-mail já cadastrado.");

        } else if (!userDAO.verifyUsername(username)) {

            JOptionPane.showMessageDialog(view, "Nome de usuário já em uso.");

        } else if (!result.isEmpty()) {

            JOptionPane.showMessageDialog(view, result.get(0));
        } else {

            try {

                if (administrador) {
                    newUser = new Administrador(name, email, username, password, data);
                } else {
                    newUser = new Usuario(name, email, username, password, data, true);
                }

                userDAO.insert(newUser);

                JOptionPane.showMessageDialog(view, "Usuário cadastrado com sucesso.");
                log.logUsuarioCRUD(new LogModel("entrou na conta", newUser.getName(), LocalDate.now(), LocalTime.now(),
                        newUser.getUsername(), ""));

                view.dispose();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(view, "Erro ao realizar o cadastro: " + e.getMessage());

                log.logFalha(new LogModel("entrou na conta", "", LocalDate.now(), LocalTime.now(), "", ""));
            }
        }
    }

}
