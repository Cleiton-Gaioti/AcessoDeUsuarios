package com.cleiton.gerenciar.factory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.cleiton.gerenciar.model.LogModel;
import com.opencsv.CSVWriter;

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

        if (!file.exists()) {

            try {

                CSVWriter writer = new CSVWriter(
                        new FileWriter(file),
                        ';',
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);

                String header[] = { "OPERACAO", "NOME", "DATA", "HORA", "USUARIO", "FALHA" };

                writer.writeNext(header, false);
                writer.close();

            } catch (IOException e) {
                throw new RuntimeException("Erro ao gravar log: " + e.getMessage());
            }
        }

    }

    @Override
    public void logUsuarioCRUD(LogModel log) {
        try {
            String line[] = {
                    log.getOperation(),
                    log.getName(),
                    log.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    log.getHour().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    log.getUsername(),
                    log.getException()
            };

            CSVWriter writer = new CSVWriter(
                    new FileWriter(file, true),
                    ';',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            writer.writeNext(line);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gravar log: " + e.getMessage());
        }
    }

    @Override
    public void logFalha(LogModel log) {
        try {
            String header[] = {
                    log.getOperation(),
                    log.getName(),
                    log.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    log.getHour().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    log.getUsername(),
                    log.getException()
            };

            CSVWriter writer = new CSVWriter(
                    new FileWriter(file, true),
                    ';',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            writer.writeNext(header);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gravar log: " + e.getMessage());
        }
    }

}
