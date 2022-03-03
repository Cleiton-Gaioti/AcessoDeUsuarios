package com.cleiton.gerenciar.factory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.cleiton.gerenciar.model.UserModel;
import com.opencsv.CSVWriter;

// TODO: consertar log.

public class LoggerCSV implements ILogger {

    // ATTRIBUTES
    private File file;
    private final String path = "logs/log.csv";

    // CONSTRUCTOR
    public LoggerCSV() {
        createFile();
    }

    @Override
    public void createFile() {
        File diretorio = new File("logs/");

        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }

        file = new File(path);

        try {
            
             CSVWriter writer = new CSVWriter(new FileWriter(file));
             
             String header[] = { "OPERACAO", "NOME", "DATA", "HORA", "USUARIO", "FALHA" };
             
             writer.writeNext(header);
             writer.close();
            
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gravar log: " + e.getMessage());
        }

    }

    @Override
    public void logUsuarioCRUD(UserModel user, String operation, LocalDateTime dateTime) {
        try {
            String header[] = {
                    operation,
                    user.getName(),
                    dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    user.getUsername(),
                    ""
            };

            CSVWriter writer = new CSVWriter(
                    new FileWriter(file),
                    ';',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            writer.writeNext(header);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gravar log: " + e.getMessage());
        }
    }

    @Override
    public void logFalha(UserModel user, String operation, LocalDateTime dateTime, String exception) {
        try {
            String header[] = {
                    operation,
                    user.getName(),
                    dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    user.getUsername(),
                    exception
            };

            CSVWriter writer = new CSVWriter(
                    new FileWriter(file),
                    ';',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            writer.writeNext(header);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gravar log: " + e.getMessage());
        }
    }

}
