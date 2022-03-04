package com.cleiton.gerenciar.presenter;

import com.cleiton.gerenciar.model.UsuarioLogadoState;
import com.cleiton.gerenciar.model.LoginState;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

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

        userDeslogadoLayout();

        /* Menu de usuários */
        view.getMenuUpdate().addActionListener(l -> {
            updateUser();
        });

        view.getMenuLogin().addActionListener(l -> {
            login();
        });

        view.getMenuLogout().addActionListener(l -> {
            logout();
        });

        /* Menu de administrador */
        view.getMenuListarUsuarios().addActionListener(l -> {
            new ListarUsuariosPresenter(view.getDesktop(), log);
        });

        /* Menu de configurações */
        view.getMenuSettings().addActionListener(l -> {
            var settingsView = new SettingsPresenter(view.getDesktop());
            settingsView.registerObserver(this);
        });

        view.setSize(900, 600);
        view.setVisible(true);
        view.setLocationRelativeTo(view.getParent());
    }

    // METHODS

    private void updateUser() {
        // TODO: implementar
    }

    private void login() {
        var loginPresenter = new LoginPresenter(view.getDesktop(), log);
        loginPresenter.registerObserver(this);
    }

    private void logout() {
        String[] options = { "Sim", "Não" };

        int resposta = JOptionPane.showOptionDialog(
                view,
                "Tem certeza que deseja sair da sua conta?",
                "Sair da conta",
                JOptionPane.YES_OPTION,
                JOptionPane.NO_OPTION,
                null,
                options,
                options[1]);

        if (resposta == 0) {
            closeAllTabs();
            setEstado(new UsuarioDeslogadoState(this));
            user = null;
            view.getTxtUser().setText("");
            userDeslogadoLayout();
            login();
        }
    }

    private void updateFooter(boolean isAdmin, String name) {

        if (isAdmin) {
            view.getTxtUser().setText("Administrador - " + name);
        } else {
            view.getTxtUser().setText("Usuário - " + name);
        }
    }

    private void userDeslogadoLayout() {
        view.getMenuLogout().setVisible(false);
        view.getMenuUpdate().setVisible(false);
        view.getjMenuUsuario().setVisible(false);
        view.getjMenuAdministrador().setVisible(false);
    }

    private void userLayout() {
        /*
         * Caso o usuário logado seja um usuário normal, adapta a tela principal para o
         * modo de usuário
         */
        view.getjMenuAdministrador().setVisible(false);
        view.getjMenuUsuario().setVisible(true);
        view.getMenuLogin().setVisible(false);
        view.getMenuLogout().setVisible(true);
        view.getMenuUpdate().setVisible(true);
    }

    private void adminLayout() {
        /*
         * Caso o usuário logado seja um administrador, adapta a tela principal para o
         * modo de administrador
         */
        view.getjMenuAdministrador().setVisible(true);
        view.getjMenuUsuario().setVisible(true);
        view.getMenuLogin().setVisible(false);
        view.getMenuLogout().setVisible(true);
        view.getMenuUpdate().setVisible(true);
    }

    @Override
    public void update(ILogger log) {
        /* ATUALIZA O MÉTODO DE LOG */
        this.log = log;

        closeAllTabs();

        var deslogado = UsuarioDeslogadoState.class.isInstance(state);

        if (deslogado) {
            login();
        }
    }

    public void update(UserModel user) {
        /* ATUALIZA A TELA PRINCIPAL QUANDO UM USUÁRIO REALIZA LOGIN */
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

    private void closeAllTabs() {
        for (JInternalFrame f : view.getDesktop().getAllFrames()) {
            f.dispose();
        }
    }

    // GETTERS AND SETTERS
    public void setEstado(LoginState newState) {
        state = newState;
    }
}
