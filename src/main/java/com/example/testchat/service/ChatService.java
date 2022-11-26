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

    public Registration attachListener(ComponentEventListener<ChatEvent> eventListener) {
        return eventBus.addListener(ChatEvent.class, eventListener);
    }
}
