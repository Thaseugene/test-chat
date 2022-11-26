package com.example.testchat.views;

import com.example.testchat.service.ChatEvent;
import com.example.testchat.entity.ChatMessage;
import com.example.testchat.service.ChatService;
import com.example.testchat.service.MessagesService;
import com.github.rjeschke.txtmark.Processor;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import java.time.format.DateTimeFormatter;


@Route("")
public class MainView extends VerticalLayout {

    private final ChatService chatService;
    private final MessagesService messagesService;
    private Registration registration;
    private Grid<ChatMessage> grid;
    private VerticalLayout chat;
    private VerticalLayout login;
    private String user = "";


    public MainView(ChatService chatService, MessagesService messagesService) {
        this.chatService = chatService;
        this.messagesService = messagesService;
        buildChat();
        buildLogin();
    }

    private void buildLogin() {
        TextField textField = new TextField();
        login = new VerticalLayout() {{
            textField.setPlaceholder("Enter your login name...");
            add(
                    textField,
                    new Button("Login") {{
                        addClickListener(buttonClickEvent -> {
                            login.setVisible(false);
                            chat.setVisible(true);
                            user = textField.getValue();
                            messagesService.addRecordJoined(user);
                        });
                        addClickShortcut(Key.ENTER);
                    }}
            );
        }};
        add(login);
    }

    private void buildChat() {
        chat = new VerticalLayout();
        add(chat);
        chat.setVisible(false);
        grid = new Grid<>();
        grid.setItems(messagesService.getMessages());
        grid.addColumn(new ComponentRenderer<>(chatMessage -> new Html(printText(chatMessage)))).setAutoWidth(true);
        TextField textField = new TextField();
        chat.add(
                new H3("Chat"),
                grid,
                new HorizontalLayout() {{
                    add(textField,
                            new Button(">") {{
                                addClickListener(click -> {
                                    messagesService.addRecord(user, textField.getValue());
                                    textField.clear();
                                });
                                addClickShortcut(Key.ENTER);
                            }});
                }}

        );
    }

    public void onMessage(ChatEvent event) {
        if (getUI().isPresent()) {
            UI ui = getUI().get();
            ui.getSession().lock();
            ui.access(() -> grid.getDataProvider().refreshAll());
            ui.beforeClientResponse(grid, ctx -> grid.scrollToEnd());
            ui.getSession().unlock();
        }
    }

    private String printText(ChatMessage chatMessage) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        if (chatMessage.getName().isEmpty()) {
            return Processor.process(String.format("%s : %s joined chat",
                    chatMessage.getLocalDateTime().format(formatter), chatMessage.getMessage()));
        } else {
            return Processor.process(String.format("%s %s -  %s", chatMessage.getLocalDateTime().format(formatter),
                    chatMessage.getName(), chatMessage.getMessage()));
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        registration = chatService.attachListener(this::onMessage);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        registration.remove();
    }
}

