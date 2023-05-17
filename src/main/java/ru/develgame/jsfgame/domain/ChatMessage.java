package ru.develgame.jsfgame.domain;

public class ChatMessage {
    private String header;

    private String message;

    public ChatMessage(String header, String message) {
        this.header = header;
        this.message = message;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
