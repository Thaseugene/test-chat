package com.example.testchat.views;

import com.example.testchat.storage.Storage;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;


@Route("")
public class MainView extends VerticalLayout {
    @Autowired
    private final Storage storage;
    private Registration registration;
    private Grid<Storage.ChatMessage> grid;
    private VerticalLayout chat;
    private VerticalLayout login;
    private String user = "";


    public MainView(Storage storage) {
        this.storage = storage;
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
                            storage.addRecordJoined(user);
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
        grid.setItems(storage.getMessages());
        grid.addColumn(new ComponentRenderer<>(chatMessage -> new Html(printText(chatMessage)))).setAutoWidth(true);
        TextField textField = new TextField();
        chat.add(
                new H3("Chat"),
                grid,
                new HorizontalLayout() {{
                    add(textField,
                            new Button(">") {{
                                addClickListener(click -> {
                                    storage.addRecord(user, textField.getValue());
                                    textField.clear();
                                });
                                addClickShortcut(Key.ENTER);
                            }});
                }}

        );
    }

    public void onMessage(Storage.ChatEvent event) {
        if (getUI().isPresent()) {
            UI ui = getUI().get();
            ui.getSession().lock();
            ui.access(() -> grid.getDataProvider().refreshAll());
            ui.beforeClientResponse(grid, ctx -> grid.scrollToEnd());
            ui.getSession().unlock();
        }
    }

    private String printText(Storage.ChatMessage chatMessage) {
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
        registration = storage.attachListener(this::onMessage);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        registration.remove();
    }
}

