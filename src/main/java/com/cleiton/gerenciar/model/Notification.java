package com.cleiton.gerenciar.model;

import java.time.LocalDate;

public final class Notification {
    /* ATTRIBUTES */
    private int id;
    private int idAdminWhoSent;
    private int idUser;
    private String title;
    private String content;
    private boolean read;
    private LocalDate date;

    /* CONSTRUCTOR */
    public Notification(int id, int idAdminWhoSent, int idUser, String title, String content, boolean read,
            LocalDate date) {
        setId(id);
        setIdAdminWhoSent(idAdminWhoSent);
        setIdUser(idUser);
        setTitle(title);
        setContent(content);
        setRead(read);
        setDate(date);
    }

    public Notification(int idAdminWhoSent, int idUser, String title, String content, boolean read,
            LocalDate date) {
        this(-1, idAdminWhoSent, idUser, title, content, read, date);
    }

    /* GETTERS AND SETTERS */
    public int getId() {
        return this.id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getIdAdminWhoSent() {
        return this.idAdminWhoSent;
    }

    private void setIdAdminWhoSent(int idAdminWhoSent) {
        this.idAdminWhoSent = idAdminWhoSent;
    }

    public int getIdUser() {
        return this.idUser;
    }

    private void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getTitle() {
        return this.title;
    }

    private void setTitle(String title) {
        if (title == null) {
            throw new RuntimeException("Título nulo.");
        } else if (title.isBlank() || title.isEmpty()) {
            throw new RuntimeException("Informe um título.");
        } else {
            this.title = title;
        }
    }

    public String getContent() {
        return this.content;
    }

    private void setContent(String content) {
        if (content == null) {
            throw new RuntimeException("Mensagem nulo.");
        } else if (content.isEmpty() || content.isBlank()) {
            throw new RuntimeException("Entre com uma mensagem.");
        } else {
            this.content = content;
        }
    }

    public boolean wasRead() {
        return this.read;
    }

    private void setRead(boolean read) {
        this.read = read;
    }

    public boolean getRead() {
        return this.read;
    }

    public LocalDate getDate() {
        return this.date;
    }

    private void setDate(LocalDate date) {
        this.date = date;
    }

}
