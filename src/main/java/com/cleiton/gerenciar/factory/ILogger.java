package com.cleiton.gerenciar.factory;

import java.time.LocalDateTime;

import com.cleiton.gerenciar.model.UserModel;

public interface ILogger {
    public void createFile();
    public void logUsuarioCRUD(UserModel user, String operation, LocalDateTime dateTime);
    public void logFalha(UserModel user, String operation, LocalDateTime dateTime, String exception);
}