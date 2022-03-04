package com.cleiton.gerenciar.factory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.cleiton.gerenciar.model.LogModel;
import com.thoughtworks.xstream.XStream;

public class LoggerXML implements ILogger {

    // ATTRIBUTES
    private File file;

    // CONSTRUCTOR
    public LoggerXML() {
        createFile();
    }

    @Override
    public void createFile() {
        File diretorio = new File("logs/");

        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }

        file = new File("logs/log.xml");
    }

    @Override
    public void logUsuarioCRUD(LogModel log) {
        XStream stream = new XStream();
        stream.alias("CRUD", LogModel.class);

        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(stream.toXML(log) + "\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gravar log.");
        }
    }

    @Override
    public void logFalha(LogModel log) {
        XStream stream = new XStream();
        stream.alias("FALHA", LogModel.class);

        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(stream.toXML(log) + "\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gravar log.");
        }
    }

}
