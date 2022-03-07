package com.cleiton.gerenciar.presenter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.cleiton.gerenciar.dao.UsuarioDAO;
import com.cleiton.gerenciar.factory.ILogger;
import com.cleiton.gerenciar.model.Administrador;
import com.cleiton.gerenciar.model.LogModel;
import com.cleiton.gerenciar.model.UserModel;
import com.cleiton.gerenciar.model.interfaces.IObserver;
import com.cleiton.gerenciar.view.ListarUsuariosView;

public class ListarUsuariosPresenter implements IObserver {

    // ATTRIBUTES
    private final ListarUsuariosView view;
    private final DefaultTableModel tableModel;
    private final UsuarioDAO userDAO;
    private final Administrador admin;
    private final ILogger log;
    private final JDesktopPane desktop;
    private List<UserModel> usersList;

    // CONSTRUCTOR
    public ListarUsuariosPresenter(JDesktopPane desktop, ILogger log, Administrador admin) {
        view = new ListarUsuariosView();
        userDAO = new UsuarioDAO();
        this.desktop = desktop;
        this.admin = admin;
        this.log = log;

        reloadUsersList();

        tableModel = new DefaultTableModel(
                new Object[][] {}, new String[] { "Id", "Tipo", "Nome", "Usuário", "Email", "Data de Cadastro",
                        "Notificações Enviadas", "Notificações Lidas" }) {
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return false;
            }
        };

        loadTable();

        view.getBtnSearchUser().addActionListener(l -> {
            searchUser();
        });

        view.getBtnSendNotification().addActionListener(l -> {
            sendNotification(desktop, log);
        });

        view.getBtnClose().addActionListener(l -> {
            view.dispose();
        });

        view.getBtnAddUser().addActionListener(l -> {
            new CadastrarUsuarioAdministradorPresenter(desktop, log).registerObserver(this);
        });

        view.getBtnUpdateUser().addActionListener(l -> {
            updateUser();
        });

        view.getBtnRemoveUser().addActionListener(l -> {
            remove();
        });

        view.getCheckSelectAll().addActionListener(l -> {
            if (view.getCheckSelectAll().isSelected())
                view.getTblUsuarios().selectAll();
            else
                view.getTblUsuarios().clearSelection();
        });

        desktop.add(view);
        view.setVisible(true);
    }

    /* METHODS */
    private void reloadUsersList() {
        try {

            usersList = userDAO.getAllUsers();

        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(view, e.getMessage());

            log.logFalha(new LogModel("listar usuários", "", LocalDate.now(), LocalTime.now(), "", e.getMessage()));
        }
    }

    private void searchUser() {
        var text = view.getTxtSearch().getText();

        try {
            if (text.isBlank() || text.isEmpty()) {

                usersList = userDAO.getAllUsers();
            } else {

                usersList = userDAO.searchUsers(text);

            }

            loadTable();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(view, e.getMessage());

            log.logFalha(new LogModel("busca de usuário", "", LocalDate.now(), LocalTime.now(), "", e.getMessage()));
        }
    }

    private void updateUser() {
        if (view.getTblUsuarios().getSelectedRows().length > 1) {

            JOptionPane.showMessageDialog(view, "Só é possível alterar um usuário por vez.");

        } else {
            var row = view.getTblUsuarios().getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(view, "Selecione uma linha.");

            } else {
                var idUser = Integer.valueOf(view.getTblUsuarios().getValueAt(row, 0).toString());

                try {
                    var user = userDAO.getUserById(idUser);

                    new CadastrarUsuarioAdministradorPresenter(desktop, log, user).registerObserver(this);

                } catch (RuntimeException e) {

                    JOptionPane.showMessageDialog(view, e.getMessage());

                    log.logFalha(new LogModel("atualização de usuário", "", LocalDate.now(), LocalTime.now(), "",
                            e.getMessage()));
                }
            }
        }
    }

    private void sendNotification(JDesktopPane desktop, ILogger log) {

        List<Integer> idsList = new ArrayList<>();

        var rows = view.getTblUsuarios().getSelectedRows();

        if (rows.length == 0) {

            JOptionPane.showMessageDialog(view, "Nenhum usuário selecionado.");

        } else {

            for (int row : rows) {
                var id = Integer.valueOf(view.getTblUsuarios().getValueAt(row, 0).toString());

                idsList.add(id);
            }

            new SendNotificationPresenter(desktop, log, admin, idsList).registerObserver(this);
            ;
        }

    }

    private void remove() {

        if (view.getTblUsuarios().getSelectedRows().length > 1) {

            JOptionPane.showMessageDialog(view, "Só é possível remover um usuário por vez.");

        } else {

            var row = view.getTblUsuarios().getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(view, "Selecione uma linha");

            } else {

                var id = Integer.valueOf(view.getTblUsuarios().getValueAt(row, 0).toString());
                var name = view.getTblUsuarios().getValueAt(row, 2).toString();
                var username = view.getTblUsuarios().getValueAt(row, 3).toString();

                if (username.equals(admin.getUsername())) {

                    JOptionPane.showMessageDialog(view,
                            "Para excluir sua conta, vá no menu Usuário -> Alterar Cadastro.");

                } else {

                    String[] options = { "Sim", "Não" };

                    int resposta = JOptionPane.showOptionDialog(
                            view,
                            "Tem certeza que deseja remover o usuário " + name + "?",
                            "Remover usuário",
                            JOptionPane.YES_OPTION,
                            JOptionPane.NO_OPTION,
                            null,
                            options,
                            options[1]);

                    if (resposta == 0) {

                        try {
                            userDAO.removeUser(id);

                            log.logUsuarioCRUD(
                                    new LogModel("remoção", name, LocalDate.now(), LocalTime.now(), username, ""));

                            reloadUsersList();

                            loadTable();
                        } catch (RuntimeException e) {
                            JOptionPane.showMessageDialog(view, e.getMessage());

                            log.logFalha(new LogModel("remoção", name, LocalDate.now(), LocalTime.now(), username,
                                    e.getMessage()));
                        }
                    }
                }
            }
        }

    }

    private void loadTable() {
        tableModel.setNumRows(0);

        var dataFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (UserModel u : usersList) {
            var tipo = Administrador.class.isInstance(u) ? "Administrador" : "Usuário";

            if (admin.getId() != u.getId()) {
                tableModel.addRow(
                        new Object[] {
                                u.getId(),
                                tipo,
                                u.getName(),
                                u.getUsername(),
                                u.getEmail(),
                                u.getDataCadastro().format(dataFormat),
                                u.getNotifications().countNotifications(),
                                u.getNotifications().countReadNotifications()
                        });
            }
        }

        view.getTblUsuarios().setModel(tableModel);

    }

    @Override
    public void update(Object obj) {
        reloadUsersList();
        loadTable();
        view.getCheckSelectAll().setSelected(false);
    }

}
