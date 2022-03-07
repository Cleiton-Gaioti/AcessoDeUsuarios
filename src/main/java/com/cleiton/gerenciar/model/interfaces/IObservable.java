package com.cleiton.gerenciar.model.interfaces;

public interface IObservable {
    
    public void registerObserver(IObserver observer);
    public void removeObeserver(IObserver observer);
    public void notifyObservers(Object obj);
}
