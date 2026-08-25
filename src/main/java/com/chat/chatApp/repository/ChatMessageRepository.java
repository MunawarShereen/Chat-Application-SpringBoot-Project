package com.chat.chatApp.repository;


import com.chat.chatApp.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
}

