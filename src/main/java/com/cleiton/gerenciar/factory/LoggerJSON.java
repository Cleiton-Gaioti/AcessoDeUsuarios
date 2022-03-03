package com.cleiton.gerenciar.factory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import com.cleiton.gerenciar.model.UserModel;

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
    public void logUsuarioCRUD(UserModel user, String operation, LocalDateTime dateTime) {
        HashMap<String, Object> hashObject = new HashMap<>();

        hashObject.put("OPERACAO", operation);
        hashObject.put("NOME", user.getName());
        hashObject.put("DATA", dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        hashObject.put("HORA", dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        hashObject.put("USUARIO", user.getUsername());

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
    public void logFalha(UserModel user, String operation, LocalDateTime dateTime, String exception) {
        HashMap<String, Object> hashObject = new HashMap<>();

        hashObject.put("EXCESSAO", exception);
        hashObject.put("OPERACAO", operation);
        hashObject.put("NOME", user.getName());
        hashObject.put("DATA", dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        hashObject.put("HORA", dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        hashObject.put("USUARIO", user.getUsername());

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
