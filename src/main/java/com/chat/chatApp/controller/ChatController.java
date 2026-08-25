package com.chat.chatApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.chat.chatApp.model.ChatMessage;
import com.chat.chatApp.repository.ChatMessageRepository;
import com.chat.chatApp.service.ChatMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ChatController {

    private final ChatMessageService chatMessageService;
    @Autowired
    ChatMessageRepository chatMessageRepository;

    public ChatController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        return chatMessageService.saveMessage(message);
    }

    @GetMapping("/history")
    public String getChatHistory(Model model) {
        List<ChatMessage> messages = chatMessageService.getAllMessages(); // Assuming this service fetches the messages
        model.addAttribute("messages", messages);
        return "history"; // This will resolve to /templates/history.html
    }

    @MessageMapping("/deleteMessage")
    @SendTo("/topic/messages")
    public ChatMessage deleteMessage(ChatMessage deleteRequest) {
        try {
            // Fetch the message from the repository
            ChatMessage message = chatMessageRepository.findById(deleteRequest.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Message not found"));

            // Ensure that only the sender can delete their message
            if (!message.getSender().equals(deleteRequest.getSender())) {
                throw new IllegalArgumentException("You can only delete your own messages.");
            }

            // Delete the message from the database
            chatMessageRepository.deleteById(message.getId());

            // Return the deleted message's ID to the frontend
            ChatMessage response = new ChatMessage();
            response.setId(message.getId()); // Return only the ID for frontend to identify the deleted message
            return response;
        } catch (IllegalArgumentException e) {
            // Return an error response to the client
            ChatMessage errorMessage = new ChatMessage();
            errorMessage.setContent("Error: " + e.getMessage()); // Include error details in the message
            return errorMessage; // This can be interpreted on the client-side as an error
        }
    }

    // New editMessage method for updating a message
    @MessageMapping("/editMessage")
    @SendTo("/topic/messages")
    public ChatMessage editMessage(ChatMessage editRequest) {
        try {
            // Fetch the message from the repository
            ChatMessage message = chatMessageRepository.findById(editRequest.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Message not found"));

            // Ensure that only the sender can edit their message
            if (!message.getSender().equals(editRequest.getSender())) {
                throw new IllegalArgumentException("You can only edit your own messages.");
            }

            // Update the message content
            message.setContent(editRequest.getContent());
            chatMessageRepository.save(message); // Save the updated message

            // Return the updated message to be broadcasted to the clients
            return message;

        } catch (IllegalArgumentException e) {
            // Handle the error and return an error message to the client
            ChatMessage errorMessage = new ChatMessage();
            errorMessage.setContent("Error: " + e.getMessage());
            return errorMessage; // Send error back as a message
        }
    }
    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }
}
