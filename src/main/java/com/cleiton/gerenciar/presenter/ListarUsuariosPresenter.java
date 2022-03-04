package com.cleiton.gerenciar.presenter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.cleiton.gerenciar.dao.UsuarioDAO;
import com.cleiton.gerenciar.factory.ILogger;
import com.cleiton.gerenciar.model.Administrador;
import com.cleiton.gerenciar.model.LogModel;
import com.cleiton.gerenciar.model.Notification;
import com.cleiton.gerenciar.model.UserModel;
import com.cleiton.gerenciar.view.ListarUsuariosView;

public class ListarUsuariosPresenter {

    // ATTRIBUTES
    private final ListarUsuariosView view;
    private final JDesktopPane desktop;
    private final ILogger log;
    private final DefaultTableModel tableModel;
    private final UsuarioDAO userDAO;
    private List<UserModel> users;

    // CONSTRUCTOR
    public ListarUsuariosPresenter(JDesktopPane desktop, ILogger log) {
        view = new ListarUsuariosView();
        userDAO = new UsuarioDAO();
        this.desktop = desktop;
        this.log = log;

        try {

            users = userDAO.getAllUsers();

        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(view, e.getMessage());

            log.logFalha(new LogModel("listar usuários", "", LocalDate.now(), LocalTime.now(), "", e.getMessage()));
        }

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
            sendNotification();
        });

        view.getBtnClose().addActionListener(l -> {
            view.dispose();
        });

        view.getBtnAddUser().addActionListener(l -> {
            new CadastrarUsuarioAdministradorPresenter(desktop, log);
            // TODO: add obeserver
        });

        view.getBtnUpdateUser().addActionListener(l -> {
            // TODO: criar tela de update
        });

        view.getBtnRemoveUser().addActionListener(l -> {
            remove();
        });

        view.getCheckSelectAll().addActionListener(l -> {
            view.getTblUsuarios().selectAll();
        });

        desktop.add(view);
        view.setVisible(true);
    }

    private void searchUser() {
        var text = view.getTxtSearch().getText();

        try {
            if (text.isBlank() || text.isEmpty()) {

                users = userDAO.getAllUsers();
            } else {

                users = userDAO.searchUsers(text);

            }

            loadTable();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(view, e.getMessage());

            log.logFalha(new LogModel("busca de usuário", "", LocalDate.now(), LocalTime.now(), "", e.getMessage()));
        }
    }

    private void sendNotification() {
        // TODO: implementar
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
                        UsuarioDAO.removeUser(id);

                        log.logUsuarioCRUD(
                                new LogModel("remoção", name, LocalDate.now(), LocalTime.now(), username, ""));

                        users = userDAO.getAllUsers();

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

    // METHODS
    private void loadTable() {
        tableModel.setNumRows(0);

        var dataFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (UserModel u : users) {
            var tipo = Administrador.class.isInstance(u) ? "Administrador" : "Usuário";
            var notifications = u.getNotifications().size();

            tableModel.addRow(
                    new Object[] {
                            u.getId(),
                            tipo,
                            u.getName(),
                            u.getUsername(),
                            u.getEmail(),
                            u.getDataCadastro().format(dataFormat),
                            notifications,
                            countNotificationsRead(u.getNotifications())
                    });
        }

        view.getTblUsuarios().setModel(tableModel);

    }

    private int countNotificationsRead(List<Notification> notifications) {
        var count = 0;

        for (Notification n : notifications) {
            if (n.wasRead()) {
                count++;
            }
        }

        return count;
    }
}
