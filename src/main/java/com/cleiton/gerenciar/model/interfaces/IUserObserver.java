package com.cleiton.gerenciar.model.interfaces;

import com.cleiton.gerenciar.factory.ILogger;
import com.cleiton.gerenciar.model.UserModel;

public interface IUserObserver {
    public void update(ILogger log);

    public void update(UserModel user);
}
