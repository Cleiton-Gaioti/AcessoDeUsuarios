package com.cleiton.gerenciar.factory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import com.cleiton.gerenciar.model.LogModel;

import org.json.simple.JSONObject;

public class LoggerJSON implements ILogger {

    // ATTRIBUTES
    private File file;

    // CONSTRUCTOR
    public LoggerJSON() {
        createFile();
    }

    @Override
    public void createFile() {
        File diretorio = new File("logs/");

        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }

        file = new File("logs/log.json");
    }

    @Override
    public void logUsuarioCRUD(LogModel log) {
        HashMap<String, Object> hashObject = new HashMap<>();

        hashObject.put("OPERACAO", log.getOperation());
        hashObject.put("NOME", log.getName());
        hashObject.put("DATA", log.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        hashObject.put("HORA", log.getHour().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        hashObject.put("USUARIO", log.getUsername());

        JSONObject jsonObject = new JSONObject(hashObject);

        try {
            FileWriter writer = new FileWriter(file, true);

            writer.write(jsonObject.toJSONString() + ",\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gravar log: " + e.getMessage());
        }
    }

    @Override
    public void logFalha(LogModel log) {
        HashMap<String, Object> hashObject = new HashMap<>();

        hashObject.put("EXCESSAO", log.getException());
        hashObject.put("OPERACAO", log.getOperation());
        hashObject.put("NOME", log.getName());
        hashObject.put("DATA", log.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        hashObject.put("HORA", log.getHour().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        hashObject.put("USUARIO", log.getUsername());

        JSONObject jsonObject = new JSONObject(hashObject);

        try {
            FileWriter writer = new FileWriter(file, true);

            writer.write(jsonObject.toJSONString() + "\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gravar log: " + e.getMessage());
        }
    }

}
