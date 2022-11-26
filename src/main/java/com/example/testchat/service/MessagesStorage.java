package com.example.testchat.service;

import com.example.testchat.entity.ChatMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesStorage extends CrudRepository<ChatMessage, String> {
}
