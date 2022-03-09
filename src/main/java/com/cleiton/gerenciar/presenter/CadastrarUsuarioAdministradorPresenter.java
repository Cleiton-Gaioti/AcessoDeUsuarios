package com.cleiton.gerenciar.presenter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.cleiton.gerenciar.dao.UsuarioDAO;
import com.cleiton.gerenciar.factory.ILogger;
import com.cleiton.gerenciar.factory.PasswordEncryptor;
import com.cleiton.gerenciar.model.Administrador;
import com.cleiton.gerenciar.model.LogModel;
import com.cleiton.gerenciar.model.Usuario;
import com.cleiton.gerenciar.model.interfaces.IObservable;
import com.cleiton.gerenciar.model.interfaces.IObserver;
import com.cleiton.gerenciar.model.UserModel;
import com.cleiton.gerenciar.view.CadastrarUsuarioAdministradorView;
import com.pss.senha.validacao.ValidadorSenha;

public class CadastrarUsuarioAdministradorPresenter implements IObservable {

    // ATTRIBUTES
    private final CadastrarUsuarioAdministradorView view;
    private final UsuarioDAO userDAO;
    private final ILogger log;
    private final List<IObserver> observers;

    // CONSTRUCTOR
    public CadastrarUsuarioAdministradorPresenter(JDesktopPane desktop, ILogger log) {
        this(desktop, log, false);
    }

    public CadastrarUsuarioAdministradorPresenter(JDesktopPane desktop, ILogger log, boolean firstUser) {
        view = new CadastrarUsuarioAdministradorView();
        userDAO = new UsuarioDAO();
        observers = new ArrayList<>();
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

        view.setLocation((desktop.getWidth() - view.getWidth())/2, (desktop.getHeight() - view.getHeight())/2);
        desktop.add(view);
        view.setVisible(true);
    }

    public CadastrarUsuarioAdministradorPresenter(JDesktopPane desktop, ILogger log, UserModel user) {
        /* Construtor chamado no modo de edição de um usuário */
        view = new CadastrarUsuarioAdministradorView();
        userDAO = new UsuarioDAO();
        observers = new ArrayList<>();
        this.log = log;

        view.getBtnClose().addActionListener(l -> {
            view.dispose();
        });

        view.getBtnRegister().addActionListener(l -> {
            saveChanges(user.getId(), user.getUsername());
        });

        view.setTitle("Atualizar Dados");
        view.getBtnRegister().setText("Salvar Alterações");

        view.getTxtName().setText(user.getName());
        view.getTxtEmail().setText(user.getEmail());
        view.getTxtUsername().setText(user.getUsername());
        view.getCheckAdministrador().setSelected(Administrador.class.isInstance(user));

        view.getTxtUsername().setEditable(false);
        view.getLblPassword().setVisible(false);
        view.getTxtPassword().setVisible(false);
        view.getCheckShowPassword().setVisible(false);

        view.setLocation((desktop.getWidth() - view.getWidth())/2, (desktop.getHeight() - view.getHeight())/2);
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

        var result = new ValidadorSenha().validar(password);

        if (!userDAO.verifyEmail(email)) {

            JOptionPane.showMessageDialog(view, "Endereço de e-mail já cadastrado.");

        } else if (!userDAO.verifyUsername(username)) {

            JOptionPane.showMessageDialog(view, "Nome de usuário já em uso.");

        } else if (!result.isEmpty()) {

            JOptionPane.showMessageDialog(view, result.get(0));

        } else {

            try {

                if (administrador) {
                    newUser = new Administrador(name, email, username, PasswordEncryptor.encrypt(password), data);
                } else {
                    newUser = new Usuario(name, email, username, PasswordEncryptor.encrypt(password), data, true);
                }

                userDAO.insert(newUser, true);

                JOptionPane.showMessageDialog(view, "Usuário cadastrado com sucesso.");

                log.logUsuarioCRUD(new LogModel("entrou na conta", newUser.getName(), LocalDate.now(), LocalTime.now(),
                        newUser.getUsername(), ""));

                notifyObservers(null);

                view.dispose();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(view, "Erro ao realizar o cadastro: " + e.getMessage());

                log.logFalha(new LogModel("entrou na conta", "", LocalDate.now(), LocalTime.now(), "", ""));
            }
        }
    }

    public void saveChanges(int idUser, String username) {
        var name = view.getTxtName().getText();
        var email = view.getTxtEmail().getText();
        var admin = view.getCheckAdministrador().isSelected();

        if (name == null || name.isBlank() || name.isEmpty()) {

            JOptionPane.showMessageDialog(view, "Nome inválido.");

        } else if (email == null || email.isBlank() || email.isEmpty()) {

            JOptionPane.showMessageDialog(view, "Email inválido");

        } else {

            try {

                InternetAddress emailAddr = new InternetAddress(email);
                emailAddr.validate();

                userDAO.updateUser(idUser, name, email, admin);

                JOptionPane.showMessageDialog(view, "Informações alteradas com sucesso.");

                log.logUsuarioCRUD(
                        new LogModel("edição de usuario", name, LocalDate.now(), LocalTime.now(), username, ""));

                notifyObservers(null);

            } catch (AddressException | RuntimeException e) {

                if (AddressException.class.isInstance(e)) {

                    JOptionPane.showMessageDialog(view, "Email inválido.");

                } else {

                    JOptionPane.showMessageDialog(view, e.getMessage());

                }

                log.logFalha(new LogModel("edição de usuario", name, LocalDate.now(), LocalTime.now(), "username",
                        e.getMessage()));
            }
        }
    }

    @Override
    public void registerObserver(IObserver observer) {
        observers.add((IObserver) observer);
    }

    @Override
    public void removeObeserver(IObserver observer) {
        observers.remove((IObserver) observer);
    }

    @Override
    public void notifyObservers(Object obj) {
        observers.forEach(o -> {
            o.update(obj);
        });
    }

}
