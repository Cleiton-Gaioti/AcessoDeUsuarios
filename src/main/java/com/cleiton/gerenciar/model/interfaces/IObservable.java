package com.cleiton.gerenciar.model.interfaces;

public interface IObservable {
    
    public void registerObserver(Object observer);
    public void removeObeserver(Object observer);
    public void notifyObservers(Object obj);
}
