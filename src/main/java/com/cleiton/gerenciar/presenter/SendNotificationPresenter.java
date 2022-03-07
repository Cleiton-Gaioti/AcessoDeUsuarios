package com.cleiton.gerenciar.presenter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import com.cleiton.gerenciar.dao.NotificationDAO;
import com.cleiton.gerenciar.factory.ILogger;
import com.cleiton.gerenciar.model.Administrador;
import com.cleiton.gerenciar.model.LogModel;
import com.cleiton.gerenciar.model.Notification;
import com.cleiton.gerenciar.model.interfaces.IObservable;
import com.cleiton.gerenciar.model.interfaces.IObserver;
import com.cleiton.gerenciar.view.SendNotificationsView;

public class SendNotificationPresenter implements IObservable {

    /* ATTRIBUTES */
    private final SendNotificationsView view;
    private final List<IObserver> observers;
    private final NotificationDAO dao;
    private final ILogger log;

    /* CONSTRUCTOR */
    public SendNotificationPresenter(JDesktopPane desktop, ILogger log, Administrador admin, List<Integer> idUsers) {
        view = new SendNotificationsView();
        observers = new ArrayList<>();
        dao = new NotificationDAO();
        this.log = log;

        view.getBtnClose().addActionListener(l -> {
            view.dispose();
        });

        view.getBtnSend().addActionListener(l -> {
            send(admin, idUsers);
        });

        desktop.add(view);
        view.setVisible(true);
    }

    private void send(Administrador admin, List<Integer> idUsers) {
        var title = view.getTxtTitle().getText();
        var content = view.getTxtNotification().getText();

        try {
            idUsers.stream().filter(id -> (id != admin.getId())).forEachOrdered(id -> {
                dao.insert(new Notification(admin.getId(), id, title, content, false, LocalDate.now()));
            });

            JOptionPane.showMessageDialog(view, "Notificação enviada com sucesso.");

            log.logUsuarioCRUD(new LogModel("envio de notificação", admin.getName(), LocalDate.now(), LocalTime.now(),
                    admin.getUsername(), ""));

            notifyObservers(null);

            view.dispose();
        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(view, e.getMessage());

            log.logFalha(
                    new LogModel("envio de notificação", admin.getName(), LocalDate.now(), LocalTime.now(),
                            admin.getUsername(), e.getMessage()));
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
