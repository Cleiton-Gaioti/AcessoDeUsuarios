package com.cleiton.gerenciar.factory;

import com.cleiton.gerenciar.model.LogModel;

public interface ILogger {
    public void createFile();
    public void logUsuarioCRUD(LogModel log);
    public void logFalha(LogModel log);
}