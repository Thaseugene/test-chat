package com.example.testchat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class ChatMessage {
    @Id
    private String id;
    private String name;
    private LocalDateTime localDateTime;
    private String message;

    public ChatMessage() {
    }

    public ChatMessage(String name, LocalDateTime localDateTime, String message) {
        this(UUID.randomUUID().toString(), name, localDateTime, message);
    }

    public void setId(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return id.equals(that.id) && Objects.equals(name, that.name) && localDateTime.equals(that.localDateTime) && message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, localDateTime, message);
    }
}
