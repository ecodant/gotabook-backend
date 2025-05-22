package com.gotabook;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import library.models.User;
import library.repositories.UserRepository;
import library.services.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    @DisplayName("Test User Creation")
    void testCreateUser() {
        User user = new User("testuser", "test@example.com", "password123");
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        User createdUser = userService.createUser(user);
        
        assertNotNull(createdUser);
        assertNotNull(createdUser.getFriends());
        assertEquals(0, createdUser.getFriends().size());
        assertEquals("testuser", createdUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    @DisplayName("Test User Login")
    void testUserLogin() {

        String email = "test@example.com";
        String password = "password123";
        User user = new User("testuser", email, password);
        when(userRepository.findByEmailAndPassword(email, password)).thenReturn(Optional.of(user));
        
        Optional<User> loggedInUser = userService.login(email, password);
        
        assertTrue(loggedInUser.isPresent());
        assertEquals("testuser", loggedInUser.get().getUsername());
        verify(userRepository, times(1)).findByEmailAndPassword(email, password);
    }
    
    @Test
    @DisplayName("Test Update User")
    void testUpdateUser() {

        String userId = "user123";
        User originalUser = new User("oldUsername", "old@example.com", "oldpass");
        originalUser.setId(userId);
        originalUser.setFriends(new HashSet<>());
        
        User updatedUserData = new User();
        updatedUserData.setId(userId);
        updatedUserData.setUsername("newUsername");
        updatedUserData.setEmail("new@example.com");
        
        Set<String> friendIds = new HashSet<>();
        friendIds.add("friend1");
        friendIds.add("friend2");
        updatedUserData.setFriends(friendIds);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(originalUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUserData);
        
        User result = userService.updateUser(updatedUserData);
        
        assertEquals("newUsername", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        assertEquals(2, result.getFriends().size());
        verify(userRepository, times(1)).save(any(User.class));
    }
}