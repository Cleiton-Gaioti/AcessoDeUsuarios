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
import com.cleiton.gerenciar.model.interfaces.IObserver;
import com.cleiton.gerenciar.view.PrincipalView;

public class PrincipalPresenter implements IObserver {

    // ATTRIBUTES
    private final PrincipalView view;
    private UserModel user;
    private LoginState state;
    private ILogger log;

    // CONSTRUCTOR
    public PrincipalPresenter() {
        view = new PrincipalView();
        log = new LoggerCSV();

        view.setSize(1366, 768);
        
        setEstado(new UsuarioDeslogadoState(this));
        
        login();

        userDeslogadoLayout();

        /* Menu de usuários */
        view.getMenuUpdate().addActionListener(l -> {
            new CadastrarUsuarioPresenter(view.getDesktop(), log, user).registerObserver(this);
        });

        view.getMenuLogin().addActionListener(l -> {
            login();
        });

        view.getMenuLogout().addActionListener(l -> {
            logout(true);
        });

        /* Menu de administrador */
        view.getMenuListarUsuarios().addActionListener(l -> {
            new ListarUsuariosPresenter(view.getDesktop(), log, (Administrador) user);
        });

        /* Menu de configurações */
        view.getMenuSettings().addActionListener(l -> {
            new SettingsPresenter(view.getDesktop()).registerObserver(this);
        });

        /* Botão de notificações */
        view.getBtnNotifications().addActionListener(l -> {
            new ShowNotificationsPresenter(view.getDesktop(), log, user).registerObserver(this);
        });

        view.getBtnSolicitacao().addActionListener(l -> {
            new AutorizarUsuarioPresenter(view.getDesktop(), log, (Administrador) user);
        });

        view.setVisible(true);
        view.setLocationRelativeTo(view.getParent());
    }

    // METHODS
    private void login() {
        new LoginPresenter(view.getDesktop(), log).registerObserver(this);
    }

    private void logout(boolean confirmar) {

        var resposta = 0;

        if (confirmar) {
            String[] options = { "Sim", "Não" };

            resposta = JOptionPane.showOptionDialog(
                    view,
                    "Tem certeza que deseja sair da sua conta?",
                    "Sair da conta",
                    JOptionPane.YES_OPTION,
                    JOptionPane.NO_OPTION,
                    null,
                    options,
                    options[1]);
        }

        if (resposta == 0) {
            closeAllTabs();
            setEstado(new UsuarioDeslogadoState(this));
            user = null;
            view.getTxtUser().setText("");
            view.getBtnNotifications().setText("0 notificações");
            userDeslogadoLayout();
            login();
        }
    }

    private void updateFooter(boolean isAdmin) {

        if (isAdmin) {
            view.getTxtUser().setText("Administrador - " + user.getName());
        } else {
            view.getTxtUser().setText("Usuário - " + user.getName());
        }

        var unread = user.getNotifications().countUnreadNotifications();

        view.getBtnNotifications().setText(unread + " notificações");
    }

    private void userDeslogadoLayout() {
        view.getMenuLogout().setVisible(false);
        view.getMenuUpdate().setVisible(false);
        view.getjMenuUsuario().setVisible(false);
        view.getjMenuAdministrador().setVisible(false);
        view.getBtnSolicitacao().setVisible(false);
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
        view.getBtnSolicitacao().setVisible(false);
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
        view.getBtnSolicitacao().setVisible(true);
    }

    private void update(ILogger log) {
        /* ATUALIZA O MÉTODO DE LOG */
        this.log = log;

        closeAllTabs();

        var deslogado = UsuarioDeslogadoState.class.isInstance(state);

        if (deslogado) {
            login();
        }
    }

    private void update(UserModel user) {
        /* ATUALIZA A TELA PRINCIPAL QUANDO UM USUÁRIO REALIZA LOGIN */
        this.user = user;

        var isAdmin = Administrador.class.isInstance(user);

        updateFooter(isAdmin);

        if (isAdmin) {
            setEstado(new AdministradorLogadoState(this));
            adminLayout();
        } else {
            setEstado(new UsuarioLogadoState(this));
            userLayout();
        }
    }

    @Override
    public void update(Object obj) {
        if (ILogger.class.isInstance(obj)) {
            update((ILogger) obj);
        } else if (UserModel.class.isInstance(obj)) {
            update((UserModel) obj);
        } else {
            logout(false);
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
