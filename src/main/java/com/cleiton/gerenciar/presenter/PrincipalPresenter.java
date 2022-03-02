package com.cleiton.gerenciar.presenter;

import com.cleiton.gerenciar.model.UsuarioLogadoState;
import com.cleiton.gerenciar.model.LoginState;

import java.time.LocalDateTime;

import javax.swing.JInternalFrame;

import com.cleiton.gerenciar.factory.ILogger;
import com.cleiton.gerenciar.factory.LoggerCSV;
import com.cleiton.gerenciar.model.Administrador;
import com.cleiton.gerenciar.model.UserModel;
import com.cleiton.gerenciar.model.interfaces.IUserObserver;
import com.cleiton.gerenciar.view.PrincipalView;

public class PrincipalPresenter implements IUserObserver {

    // ATTRIBUTES
    private final PrincipalView view;
    private UserModel user;
    private LoginState state;
    private ILogger log;

    // CONSTRUCTOR
    public PrincipalPresenter(UserModel user) {
        view = new PrincipalView();
        log = new LoggerCSV();
        this.user = user;

        log.logUsuarioCRUD(user, "realizou login", LocalDateTime.now());

        setEstado(new UsuarioLogadoState(this));
        updateFooter(user);

        view.getMenuLogin().addActionListener(l -> {
            new LoginPresenter();
        });

        view.getMenuSettings().addActionListener(l -> {
            var settingsView = new SettingsPresenter(view.getDesktop());
            settingsView.registerObserver(this);
        });

        view.setSize(900, 600);
        view.setVisible(true);
        view.setLocationRelativeTo(view.getParent());
    }

    private void updateFooter(UserModel u) {
        var isAdmin = Administrador.class.isInstance(u);

        if (isAdmin) {
            view.getTxtUser().setText("Administrador - " + u.getName());
        } else {
            view.getTxtUser().setText("Usuário - " + u.getName());
        }
    }

    @Override
    public void update(ILogger log) {
        this.log = log;

        for (JInternalFrame f : view.getDesktop().getAllFrames()) {
            f.dispose();
        }
    }

    // GETTERS AND SETTERS
    public void setEstado(LoginState newState) {
        state = newState;
    }
}
