package com.cleiton.gerenciar.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JDesktopPane;

import com.cleiton.gerenciar.factory.ILogger;
import com.cleiton.gerenciar.factory.LoggerCSV;
import com.cleiton.gerenciar.factory.LoggerJSON;
import com.cleiton.gerenciar.factory.LoggerXML;
import com.cleiton.gerenciar.model.interfaces.IObservable;
import com.cleiton.gerenciar.model.interfaces.IObserver;
import com.cleiton.gerenciar.view.SettingsView;

public class SettingsPresenter implements IObservable {

    // ATTRIBUTES
    private final SettingsView view;
    private final List<IObserver> observers;

    // CONSTRUCTOR
    public SettingsPresenter(JDesktopPane desktop) {
        view = new SettingsView();
        observers = new ArrayList<>();

        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(view.getRadioButtonCSV());
        btnGroup.add(view.getRadioButtonJSON());
        btnGroup.add(view.getRadioButtonXML());

        view.getBtnCancel().addActionListener(l -> {
            view.dispose();
        });

        view.getBtnApply().addActionListener(l -> {
            apply();
        });

        view.setLocation((desktop.getWidth() - view.getWidth())/2, (desktop.getHeight() - view.getHeight())/2);
        desktop.add(view);
        view.setVisible(true);
    }

    // METHODS
    private void apply() {
        if (view.getRadioButtonCSV().isSelected()) {
            notifyObservers(new LoggerCSV());
        } else if (view.getRadioButtonJSON().isSelected()) {
            notifyObservers(new LoggerJSON());
        } else {
            notifyObservers(new LoggerXML());
        }
    }

    @Override
    public void registerObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObeserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Object obj) {
        observers.forEach(o -> {
            o.update((ILogger) obj);
        });
    }

}
