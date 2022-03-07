package com.cleiton.gerenciar.presenter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.cleiton.gerenciar.dao.UsuarioDAO;
import com.cleiton.gerenciar.factory.ILogger;
import com.cleiton.gerenciar.model.Administrador;
import com.cleiton.gerenciar.model.LogModel;
import com.cleiton.gerenciar.model.Usuario;
import com.cleiton.gerenciar.view.AutorizarUsuarioView;

public class AutorizarUsuarioPresenter {

    /* ATTRIBUTES */
    private final DefaultTableModel tableModel;
    private final AutorizarUsuarioView view;
    private final Administrador admin;
    private final UsuarioDAO userDAO;
    private final ILogger log;
    private List<Usuario> users;

    /* CONSTRUCTOR */
    public AutorizarUsuarioPresenter(JDesktopPane desktop, ILogger log, Administrador admin) {
        view = new AutorizarUsuarioView();
        userDAO = new UsuarioDAO();
        this.admin = admin;
        this.log = log;

        tableModel = new DefaultTableModel(
                new Object[][] {}, new String[] { "Nome", "Usuário", "Email" }) {
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return false;
            }
        };

        loadTable();

        view.getBtnClose().addActionListener(l -> {
            view.dispose();
        });

        view.getBtnAuthorize().addActionListener(l -> {
            authorize();
        });

        desktop.add(view);
        view.setVisible(true);
    }

    /* METHODS */
    private void loadTable() {

        tableModel.setNumRows(0);

        try {

            users = userDAO.getUsersUnauthorizeds();

            users.forEach(u -> {
                tableModel.addRow(
                        new String[] {
                                u.getName(),
                                u.getUsername(),
                                u.getEmail()
                        });
            });

            view.getTblUsers().setModel(tableModel);
        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(view, "Erro ao carregar solicitações: " + e.getMessage());

            log.logFalha(new LogModel("Listar solicitações", admin.getName(), LocalDate.now(), LocalTime.now(),
                    admin.getUsername(), e.getMessage()));
        }
    }

    private void authorize() {
        var rows = view.getTblUsers().getSelectedRows();

        if (rows.length == 0) {

            JOptionPane.showMessageDialog(view, "Selecione uma linha.");

        } else {

            for (int row : rows) {
                var username = view.getTblUsers().getValueAt(row, 1).toString();

                try {
                    userDAO.approveSolicitation(username);

                    loadTable();
                } catch (RuntimeException e) {
                    JOptionPane.showMessageDialog(view, "Erro ao aprovar solicitações: " + e.getMessage());

                    log.logFalha(new LogModel("Aprovar solicitações", admin.getName(), LocalDate.now(), LocalTime.now(),
                            admin.getUsername(), e.getMessage()));
                }
            }
        }
    }

}
