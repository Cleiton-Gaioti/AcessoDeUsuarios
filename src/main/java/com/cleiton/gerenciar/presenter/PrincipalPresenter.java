package com.cleiton.gerenciar.presenter;

import com.cleiton.gerenciar.model.UsuarioLogadoState;
import com.cleiton.gerenciar.model.LoginState;

import javax.swing.JInternalFrame;

import com.cleiton.gerenciar.factory.ILogger;
import com.cleiton.gerenciar.factory.LoggerCSV;
import com.cleiton.gerenciar.model.Administrador;
import com.cleiton.gerenciar.model.AdministradorLogadoState;
import com.cleiton.gerenciar.model.UserModel;
import com.cleiton.gerenciar.model.UsuarioDeslogadoState;
import com.cleiton.gerenciar.model.interfaces.IUserObserver;
import com.cleiton.gerenciar.view.PrincipalView;

public class PrincipalPresenter implements IUserObserver {

    // ATTRIBUTES
    private final PrincipalView view;
    private UserModel user;
    private LoginState state;
    private ILogger log;

    // CONSTRUCTOR
    public PrincipalPresenter() {
        view = new PrincipalView();
        log = new LoggerCSV();

        setEstado(new UsuarioDeslogadoState(this));
        
        login();
        
        userDeslogado();

        view.getMenuLogin().addActionListener(l -> {
            login();
        });

        view.getMenuSettings().addActionListener(l -> {
            var settingsView = new SettingsPresenter(view.getDesktop());
            settingsView.registerObserver(this);
        });

        view.getMenuUpdate().addActionListener(l -> {
            updateUser();
        });

        view.setSize(900, 600);
        view.setVisible(true);
        view.setLocationRelativeTo(view.getParent());
    }
    
    // METHODS

    private void userDeslogado() {
        view.getMenuLogout().setVisible(false);
        view.getMenuUpdate().setVisible(false);
        view.getjMenuAdministrador().setVisible(false);
    }

    private void login() {
        var loginPresenter = new LoginPresenter(view.getDesktop(), log);
        loginPresenter.registerObserver(this);
    }

    private void updateUser() {
        // TODO: implementar
    }

    private void updateFooter(boolean isAdmin, String name) {

        if (isAdmin) {
            view.getTxtUser().setText("Administrador - " + name);
        } else {
            view.getTxtUser().setText("Usuário - " + name);
        }
    }

    private void userLayout() {
        view.getjMenuAdministrador().setVisible(false);
    }

    private void adminLayout() {
    }

    @Override
    public void update(ILogger log) {
        this.log = log;

        for (JInternalFrame f : view.getDesktop().getAllFrames()) {
            f.dispose();
        }

        var deslogado = UsuarioDeslogadoState.class.isInstance(state);

        if(deslogado) {
            login();
        }
    }

    public void update(UserModel user) {
        this.user = user;

        var isAdmin = Administrador.class.isInstance(user);

        updateFooter(isAdmin, user.getName());

        if (isAdmin) {
            setEstado(new AdministradorLogadoState(this));
            adminLayout();
        } else {
            setEstado(new UsuarioLogadoState(this));
            userLayout();
        }
    }

    // GETTERS AND SETTERS
    public void setEstado(LoginState newState) {
        state = newState;
    }
}
