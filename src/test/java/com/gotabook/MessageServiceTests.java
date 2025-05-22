package com.gotabook;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import library.models.Message;
import library.repositories.MessageRepository;
import library.services.MessageService;

@ExtendWith(MockitoExtension.class)
class MessageServiceTests {

    @Mock
    private MessageRepository messageRepository;
    
    @InjectMocks
    private MessageService messageService;
    
    @Test
    @DisplayName("Test Send Message")
    void testSendMessage() {
        // Arrange
        Message message = new Message();
        message.setSenderId("user1");
        message.setReceiverId("user2");
        message.setContent("Hello, this is a test message");
        
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message savedMessage = invocation.getArgument(0);
            savedMessage.setId("msg123");
            return savedMessage;
        });
        
        // Act
        Message sentMessage = messageService.sendMessage(message);
        
        // Assert
        assertNotNull(sentMessage);
        assertNotNull(sentMessage.getId());
        assertNotNull(sentMessage.getDate());
        assertFalse(sentMessage.isRead());
        assertEquals("Hello, this is a test message", sentMessage.getContent());
        verify(messageRepository, times(1)).save(any(Message.class));
    }
    
    @Test
    @DisplayName("Test Get Unread Messages")
    void testGetUnreadMessages() {
        // Arrange
        String receiverId = "user123";
        List<Message> unreadMessages = Arrays.asList(
            createTestMessage("msg1", "user1", receiverId, "Unread 1"),
            createTestMessage("msg2", "user2", receiverId, "Unread 2"),
            createTestMessage("msg3", "user3", receiverId, "Unread 3")
        );
        
        when(messageRepository.findByReceiverIdAndReadFalse(receiverId)).thenReturn(unreadMessages);
        
        // Act
        List<Message> result = messageService.getUnreadMessages(receiverId);
        
        // Assert
        assertEquals(3, result.size());
        verify(messageRepository, times(1)).findByReceiverIdAndReadFalse(receiverId);
    }
    
    @Test
    @DisplayName("Test Mark Message as Read")
    void testMarkAsRead() {
        // Arrange
        String messageId = "msg123";
        Message message = new Message();
        message.setId(messageId);
        message.setSenderId("user1");
        message.setReceiverId("user2");
        message.setContent("Test message");
        message.setRead(false);
        
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message savedMessage = invocation.getArgument(0);
            return savedMessage;
        });
        
        // Act
        Message updatedMessage = messageService.markAsRead(messageId);
        
        // Assert
        assertTrue(updatedMessage.isRead());
        verify(messageRepository, times(1)).save(message);
    }
    
    // Helper method to create test messages
    private Message createTestMessage(String id, String senderId, String receiverId, String content) {
        Message message = new Message();
        message.setId(id);
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setDate(new Date());
        message.setRead(false);
        return message;
    }
}