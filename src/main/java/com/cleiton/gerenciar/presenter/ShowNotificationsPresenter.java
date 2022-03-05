package com.cleiton.gerenciar.presenter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

import com.cleiton.gerenciar.dao.NotificationDAO;
import com.cleiton.gerenciar.factory.ILogger;
import com.cleiton.gerenciar.model.LogModel;
import com.cleiton.gerenciar.model.UserModel;
import com.cleiton.gerenciar.model.interfaces.IObservable;
import com.cleiton.gerenciar.model.interfaces.IUserObserver;
import com.cleiton.gerenciar.view.ShowNotificationsView;

public class ShowNotificationsPresenter implements IObservable {

    /* ATTRIBUTES */
    private final List<IUserObserver> observers;
    private final ShowNotificationsView view;
    private final NotificationDAO dao;
    private final UserModel user;
    private final ILogger log;
    private DefaultTableModel tableModel;

    /* CONSTRUCTOR */
    public ShowNotificationsPresenter(JDesktopPane desktop, ILogger log, UserModel user) {
        tableModel = new DefaultTableModel();
        view = new ShowNotificationsView();
        observers = new ArrayList<>();
        dao = new NotificationDAO();
        this.user = user;
        this.log = log;

        tableModel = new DefaultTableModel(
                new Object[][] {}, new String[] { "Id", "Notificação" }) {
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return false;
            }
        };

        loadList();

        view.getBtnClose().addActionListener(l -> {
            view.dispose();
        });

        view.getBtnSetAllRead().addActionListener(l -> {
            try {
                user.getNotifications().getUnreadNotifications().forEach(n -> {
                    dao.setReadNotification(n.getId());
                });

            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(view, e.getMessage());

                log.logFalha(new LogModel("carga de notificação", user.getEmail(), LocalDate.now(), LocalTime.now(),
                        user.getUsername(), e.getMessage()));
            }
        });

        view.getListNotifications().getSelectionModel().addListSelectionListener((ListSelectionEvent l) -> {
            setRead();
        });

        desktop.add(view);
        view.setVisible(true);
    }

    private void setRead() {
        var row = view.getListNotifications().getSelectedRow();

        try {
            var notification = dao
                    .getNotificationsById(Integer.valueOf(view.getListNotifications().getValueAt(row, 0).toString()));

            view.getTxtTitle().setText(notification.getTitle());
            view.getTxtContent().setText(notification.getContent());

            view.getListNotifications().setValueAt(notification.getTitle(), row, 0);

            dao.setReadNotification(notification.getId());

            notifyObservers(user);

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(view, e.getMessage());

            log.logFalha(new LogModel("carga de notificação", user.getEmail(), LocalDate.now(), LocalTime.now(),
                    user.getUsername(), e.getMessage()));
        }
    }

    /* METHODS */
    private void loadList() {
        tableModel.setNumRows(0);

        view.getListNotifications().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        user.getNotifications().getUnreadNotifications().forEach(n -> {
            tableModel.addRow(
                    new String[] {
                            String.valueOf(n.getId()),
                            "<html><b>" + n.getTitle() + "</b></html>"
                    });
        });

        view.getListNotifications().setModel(tableModel);
    }

    @Override
    public void registerObserver(Object observer) {
        observers.add((IUserObserver) observer);
    }

    @Override
    public void removeObeserver(Object observer) {
        observers.remove((IUserObserver) observer);
    }

    @Override
    public void notifyObservers(Object obj) {
        observers.forEach(o -> {
            o.update(user);
        });
    }

}