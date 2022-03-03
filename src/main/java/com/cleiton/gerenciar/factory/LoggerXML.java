package com.cleiton.gerenciar.factory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.cleiton.gerenciar.model.UserModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

// TODO: consertar log.

public class LoggerXML implements ILogger {

    // ATTRIBUTES
    private FileOutputStream output;
    private Document doc;
    private Element log;

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

        try {
            output = new FileOutputStream(new File("logs/log.xml"));

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("usuarios");
            doc.appendChild(rootElement);

            Element log = doc.createElement("log");
            rootElement.appendChild(log);

            writeXML();

        } catch (ParserConfigurationException | TransformerException | IOException e) {
            throw new RuntimeException("Erro ao gravar log: " + e.getMessage());
        }

    }

    @Override
    public void logUsuarioCRUD(UserModel user, String operation, LocalDateTime dateTime) {
        Element operacao = doc.createElement("OPERACAO");
        operacao.setTextContent(operation);
        log.appendChild(operacao);

        Element name = doc.createElement("NOME");
        name.setTextContent(user.getName());
        log.appendChild(name);

        Element date = doc.createElement("DATA");
        date.setTextContent(dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        log.appendChild(date);

        Element hour = doc.createElement("HORA");
        hour.setTextContent(dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        log.appendChild(hour);

        Element username = doc.createElement("USUARIO");
        username.setTextContent(user.getUsername());
        log.appendChild(username);

        try {
            writeXML();
        } catch (TransformerException e) {
            throw new RuntimeException("Erro ao gravar log: " + e.getMessage());
        }
    }

    @Override
    public void logFalha(UserModel user, String operation, LocalDateTime dateTime, String exception) {
        Element fail = doc.createElement("FALHA");
        fail.setTextContent(exception);
        log.appendChild(fail);

        Element operacao = doc.createElement("OPERACAO");
        operacao.setTextContent(operation);
        log.appendChild(operacao);

        Element name = doc.createElement("NOME");
        name.setTextContent(user.getName());
        log.appendChild(name);

        Element date = doc.createElement("DATA");
        date.setTextContent(dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        log.appendChild(date);

        Element hour = doc.createElement("HORA");
        hour.setTextContent(dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        log.appendChild(hour);

        Element username = doc.createElement("USUARIO");
        username.setTextContent(user.getUsername());
        log.appendChild(username);

        try {
            writeXML();
        } catch (TransformerException e) {
            throw new RuntimeException("Erro ao gravar log: " + e.getMessage());
        }
    }

    private void writeXML() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }
}
