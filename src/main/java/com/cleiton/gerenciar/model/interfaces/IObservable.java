package com.cleiton.gerenciar.model.interfaces;

public interface IObservable {
    
    public void registerObserver(IUserObserver observer);
    public void removeObeserver(IUserObserver observer);
    public void notifyObservers(Object obj);
}
