package com.example.testchat.service;

import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import org.springframework.stereotype.Component;


@Component
public class ChatService {
    @Getter
    private final ComponentEventBus eventBus;

    public ChatService() {
        this.eventBus = new ComponentEventBus(new Div());
    }

//    public void addRecord(String user, String message) {
//        messagesStorage.save(new ChatMessage(user, LocalDateTime.now(), message));
//        eventBus.fireEvent(new ChatEvent());
//    }
//
//    public long size() {
//        return messagesStorage.count();
//    }
//
//    public void addRecordJoined(String user) {
//        messagesStorage.save(new ChatMessage("",LocalDateTime.now(), user));
//        eventBus.fireEvent(new ChatEvent());
//    }

//    @Getter
//    @AllArgsConstructor
//    public static class ChatMessage {
//        private String name;
//        private LocalDateTime localDateTime;
//        private String message;
//
//    }

    //    public static class ChatEvent extends ComponentEvent<Div> {
//        public ChatEvent() {
//            super(new Div(), false);
//        }
//    }
    public Registration attachListener(ComponentEventListener<ChatEvent> eventListener) {
        return eventBus.addListener(ChatEvent.class, eventListener);
    }
}
