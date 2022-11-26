package com.example.testchat.storage;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.shared.Registration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class Storage {
    @Getter
    private final Queue<ChatMessage> messages = new ConcurrentLinkedQueue<>();
    private final ComponentEventBus eventBus = new ComponentEventBus(new Div());

    public void addRecord(String user, String message) {
        messages.add(new ChatMessage(user, LocalDateTime.now(), message));
        eventBus.fireEvent(new ChatEvent());
    }

    public int size() {
        return messages.size();
    }

    public void addRecordJoined(String user) {
        messages.add(new ChatMessage("",LocalDateTime.now(), user));
        eventBus.fireEvent(new ChatEvent());
    }

    @Getter
    @AllArgsConstructor
    public static class ChatMessage {
        private String name;
        private LocalDateTime localDateTime;
        private String message;

    }

    public static class ChatEvent extends ComponentEvent<Div> {
        public ChatEvent() {
            super(new Div(), false);
        }
    }
    public Registration attachListener(ComponentEventListener<ChatEvent> eventListener) {
        return eventBus.addListener(ChatEvent.class, eventListener);
    }


}
