package com.gotabook;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import datastructures.BookBST;
import library.models.Book;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookBSTTests {

    private BookBST bookBST;
    
    @BeforeEach
    void setup() {
        bookBST = new BookBST();
    }
    
    @Test
    @DisplayName("Test Book BST Insertion")
    void testBookBSTInsertion() {
        // Arrange
        Book book1 = new Book("Clean Code", "Robert Martin", 2008, "Programming");
        Book book2 = new Book("Design Patterns", "Gang of Four", 1994, "Programming");
        Book book3 = new Book("Effective Java", "Joshua Bloch", 2017, "Programming");
        
        // Act
        bookBST.insert(book1);
        bookBST.insert(book2);
        bookBST.insert(book3);
        
        // Assert
        assertNotNull(bookBST.search("Clean Code"));
        assertNotNull(bookBST.search("Design Patterns"));
        assertNotNull(bookBST.search("Effective Java"));
        assertEquals("Robert Martin", bookBST.search("Clean Code").getAuthor());
    }

    @Test
    @DisplayName("Test Book BST Deletion")
    void testBookBSTDeletion() {
        // Arrange
        Book book1 = new Book("Clean Code", "Robert Martin", 2008, "Programming");
        Book book2 = new Book("Design Patterns", "Gang of Four", 1994, "Programming");
        bookBST.insert(book1);
        bookBST.insert(book2);
        
        // Act
        bookBST.delete("Clean Code");
        
        // Assert
        assertNull(bookBST.search("Clean Code"));
        assertNotNull(bookBST.search("Design Patterns"));
    }
    
    @Test
    @DisplayName("Test Get All Books from BST")
    void testGetAllBooks() {
        // Arrange
        Book book1 = new Book("Clean Code", "Robert Martin", 2008, "Programming");
        Book book2 = new Book("Design Patterns", "Gang of Four", 1994, "Programming");
        Book book3 = new Book("Effective Java", "Joshua Bloch", 2017, "Programming");
        
        bookBST.insert(book1);
        bookBST.insert(book2);
        bookBST.insert(book3);
        
        // Act
        List<Book> allBooks = bookBST.getAllBooks();
        
        // Assert
        assertEquals(3, allBooks.size());
        
        // Books should be in alphabetical order by title
        assertEquals("Clean Code", allBooks.get(0).getTitle());
        assertEquals("Design Patterns", allBooks.get(1).getTitle());
        assertEquals("Effective Java", allBooks.get(2).getTitle());
    }
}
