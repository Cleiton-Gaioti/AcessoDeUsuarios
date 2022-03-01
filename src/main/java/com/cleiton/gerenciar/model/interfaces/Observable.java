package com.cleiton.gerenciar.model.interfaces;

public interface Observable {
    
    public void registerObserver();
    public void removeObeserver();
    public void notifyObservers();
}
