
package com.cleiton.gerenciar.presenter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.cleiton.gerenciar.dao.UsuarioDAO;
import com.cleiton.gerenciar.factory.ILogger;
import com.cleiton.gerenciar.factory.PasswordEncryptor;
import com.cleiton.gerenciar.model.Usuario;
import com.cleiton.gerenciar.model.interfaces.IObservable;
import com.cleiton.gerenciar.model.interfaces.IObserver;
import com.cleiton.gerenciar.model.UserModel;
import com.cleiton.gerenciar.model.LogModel;
import com.cleiton.gerenciar.view.CadastrarUsuarioView;
import com.pss.senha.validacao.ValidadorSenha;

public class CadastrarUsuarioPresenter implements IObservable {

    // ATTRIBUTES
    private final CadastrarUsuarioView view;
    private final List<IObserver> observers;
    private final UsuarioDAO userDAO;
    private final ILogger log;

    // CONSTRUCTORS
    public CadastrarUsuarioPresenter(JDesktopPane desktop, ILogger log) {
        view = new CadastrarUsuarioView();
        userDAO = new UsuarioDAO();
        observers = null;
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

        view.getBtnRemoveRegister().setVisible(false);

        desktop.add(view);
        view.setVisible(true);
    }

    public CadastrarUsuarioPresenter(JDesktopPane desktop, ILogger log, UserModel user) {
        view = new CadastrarUsuarioView();
        userDAO = new UsuarioDAO();
        observers = new ArrayList<>();
        this.log = log;

        view.getBtnClose().addActionListener(l -> {
            view.dispose();
        });

        view.getBtnRegister().addActionListener(l -> {
            saveChanges(user);
        });

        view.getCheckShowPassword().addActionListener(l -> {
            if (view.getCheckShowPassword().isSelected()) {
                view.getTxtPassword().setEchoChar((char) 0);
            } else {
                view.getTxtPassword().setEchoChar('*');
            }
        });

        view.getBtnRemoveRegister().addActionListener(l -> {
            remove(user);
        });

        view.setTitle("Atualizar Dados");
        view.getBtnRegister().setText("Salvar Alterações");

        view.getTxtName().setText(user.getName());
        view.getTxtEmail().setText(user.getEmail());
        view.getTxtUsername().setText(user.getUsername());

        desktop.add(view);
        view.setVisible(true);
    }

    // METHODS
    private void saveChanges(UserModel user) {
        var name = view.getTxtName().getText();
        var email = view.getTxtEmail().getText();
        var username = view.getTxtUsername().getText();
        var password = String.valueOf(view.getTxtPassword().getPassword());

        List<String> result = new ArrayList<>();

        if (password.isEmpty() || password.isBlank()) {

            password = user.getPassword();

        } else {

            result = new ValidadorSenha().validar(password);

            password = PasswordEncryptor.encrypt(password);
        }

        if (!result.isEmpty()) {

            JOptionPane.showMessageDialog(view, result.get(0));

        } else {

            try {
                userDAO.updateUser(user.getId(), name, email, username, password);

                JOptionPane.showMessageDialog(view, "Informações alteradas com sucesso.");

                log.logUsuarioCRUD(
                        new LogModel("edição de usuario", name, LocalDate.now(), LocalTime.now(), username, ""));

                user = userDAO.getUserById(user.getId());

                notifyObservers(user);

            } catch (RuntimeException e) {

                JOptionPane.showMessageDialog(view, e.getMessage());

                log.logFalha(new LogModel("edição de usuário", user.getName(), LocalDate.now(), LocalTime.now(),
                        user.getUsername(), e.getMessage()));
            }
        }

    }

    private void register() {
        final var name = view.getTxtName().getText();
        final var email = view.getTxtEmail().getText();
        final var username = view.getTxtUsername().getText();
        final var password = String.valueOf(view.getTxtPassword().getPassword());
        final var data = LocalDate.now();

        var result = new ValidadorSenha().validar(password);

        if (!userDAO.verifyEmail(email)) {

            JOptionPane.showMessageDialog(view, "Endereço de e-mail já cadastrado.");

        } else if (!userDAO.verifyUsername(username)) {

            JOptionPane.showMessageDialog(view, "Nome de usuário já em uso.");

        } else if (!result.isEmpty()) {

            JOptionPane.showMessageDialog(view, result.get(0));

        } else {

            try {
                var newUser = new Usuario(name, email, username, PasswordEncryptor.encrypt(password), data, false);

                userDAO.insert(newUser, false);

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

    private void remove(UserModel user) {
        String[] options = { "Sim", "Não" };

        int resposta = JOptionPane.showOptionDialog(
                view,
                "Tem certeza que deseja deletar sua conta?",
                "Deletar conta",
                JOptionPane.YES_OPTION,
                JOptionPane.NO_OPTION,
                null,
                options,
                options[1]);

        if (resposta == 0) {

            try {
                userDAO.removeUser(user.getId());

                log.logUsuarioCRUD(
                        new LogModel("remoção", user.getName(), LocalDate.now(), LocalTime.now(), user.getUsername(),
                                ""));

            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(view, e.getMessage());

                log.logFalha(
                        new LogModel("remoção", user.getName(), LocalDate.now(), LocalTime.now(), user.getUsername(),
                                e.getMessage()));
            }
        }
    }

    @Override
    public void registerObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObeserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Object obj) {
        observers.forEach(o -> {
            o.update((UserModel) obj);
        });
    }

}
