
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
import com.cleiton.gerenciar.model.UserModel;
import com.cleiton.gerenciar.model.LogModel;
import com.cleiton.gerenciar.model.interfaces.IObservable;
import com.cleiton.gerenciar.model.interfaces.IUserObserver;
import com.cleiton.gerenciar.view.LoginView;

public class LoginPresenter implements IObservable {

    // ATTRIBUTES
    private final LoginView view;
    private final int countUsers;
    private final ILogger log;
    private final List<IUserObserver> observers;
    private final UsuarioDAO userDAO;
    private final JDesktopPane desktop;

    // CONSTRUCTOR
    public LoginPresenter(JDesktopPane desktop, ILogger log) {
        view = new LoginView();
        userDAO = new UsuarioDAO();
        observers = new ArrayList<>();
        countUsers = UsuarioDAO.countUsers();
        this.log = log;
        this.desktop = desktop;

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

        view.setLocation(250, 160);
        desktop.add(view);
        view.setVisible(true);
    }

    // METHODS
    private void register() {
        if (countUsers == 0) {
            new CadastrarUsuarioAdministradorPresenter(desktop, log, true);
        } else {
            new CadastrarUsuarioPresenter(desktop, log);
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

                    log.logUsuarioCRUD(new LogModel("entrou na conta", user.getName(), LocalDate.now(), LocalTime.now(),
                            user.getUsername(), ""));

                    notifyObservers(user);

                    view.dispose();
                }
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(view, "Erro ao realizar login." + e.getMessage());

                log.logFalha(new LogModel("entrou na conta", "", LocalDate.now(), LocalTime.now(), "", ""));

            }
        }

    }

    @Override
    public void registerObserver(IUserObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObeserver(IUserObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Object obj) {
        observers.forEach(o -> {
            o.update((UserModel) obj);
        });
    }

}
