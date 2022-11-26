package com.example.testchat.service;

import com.example.testchat.entity.ChatMessage;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MessagesService {
    @Getter
    private final Queue<ChatMessage> messages = new ConcurrentLinkedQueue<>();
    private final MessagesStorage storage;

    private final ChatService chatService;

    public MessagesService(MessagesStorage storage, ChatService chatService) {
        this.storage = storage;
        this.chatService = chatService;
    }

    public void addRecord(String user, String message) {
        storage.save(new ChatMessage(user, LocalDateTime.now(), message));
        messages.add(new ChatMessage(user, LocalDateTime.now(), message));
        chatService.getEventBus().fireEvent(new ChatEvent());
    }

    public int size() {
        return messages.size();
    }

    public void addRecordJoined(String user) {
        storage.save(new ChatMessage("", LocalDateTime.now(), user));
        messages.add(new ChatMessage("", LocalDateTime.now(), user));
        chatService.getEventBus().fireEvent(new ChatEvent());
    }


}
