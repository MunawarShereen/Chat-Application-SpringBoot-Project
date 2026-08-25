package com.chat.chatApp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Entity
@Table(name = "CHAT_MESSAGE")
@Data
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "SENDER", nullable = false)
    private String sender;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public String getFormattedTimestamp() {
        if (this.timestamp != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy hh.mm.ss.SSSSSSSSS a");
            return this.timestamp.format(formatter);
        }
        return null; // Return null or some default value if the timestamp is null
    }
}
