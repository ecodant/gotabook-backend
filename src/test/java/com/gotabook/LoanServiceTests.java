package com.gotabook;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import library.models.Book;
import library.models.BookStatus;
import library.models.Loan;
import library.models.LoanQueue;
import library.repositories.BookRepository;
import library.repositories.LoanQueueRepository;
import library.repositories.LoanRepository;
import library.services.LoanService;

@ExtendWith(MockitoExtension.class)
class LoanServiceTests {

    @Mock
    private LoanRepository loanRepository;
    
    @Mock
    private BookRepository bookRepository;
    
    @Mock
    private LoanQueueRepository loanQueueRepository;
    
    @InjectMocks
    private LoanService loanService;
    
    @Test
    @DisplayName("Test Create Loan for Available Book")
    void testCreateLoanForAvailableBook() {

        String bookId = "book123";
        String userId = "user123";
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setStatus(BookStatus.AVAILABLE.toString());
        
        Loan loan = new Loan(bookId, userId);
        loan.setId("loan123");
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        
        Loan createdLoan = loanService.createLoan(loan);
        
        assertNotNull(createdLoan);
        assertEquals(Loan.LoanStatus.ACTIVE, createdLoan.getStatus());
        assertEquals("Test Book", createdLoan.getBookTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }
    
    @Test
    @DisplayName("Test Create Loan for Unavailable Book")
    void testCreateLoanForUnavailableBook() {

        String bookId = "book123";
        String userId = "user123";
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setStatus(BookStatus.BORROWED.toString());
        
        Loan loan = new Loan(bookId, userId);
        loan.setId("loan123");
        
        LoanQueue loanQueue = new LoanQueue(bookId);
        loanQueue.setQueue(new ArrayList<>());
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(loanQueueRepository.findById(bookId)).thenReturn(Optional.of(loanQueue));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(loanQueueRepository.save(any(LoanQueue.class))).thenReturn(loanQueue);
        
        Loan createdLoan = loanService.createLoan(loan);
        
        assertNotNull(createdLoan);
        assertEquals(Loan.LoanStatus.WAITING, createdLoan.getStatus());
        verify(loanQueueRepository, times(1)).save(any(LoanQueue.class));
    }
    
    @Test
    @DisplayName("Test Book Return with Empty Queue")
    void testBookReturnWithEmptyQueue() {
        String loanId = "loan123";
        String bookId = "book123";
        
        Loan loan = new Loan(bookId, "user123");
        loan.setId(loanId);
        loan.setStatus(Loan.LoanStatus.ACTIVE);
        
        Book book = new Book();
        book.setId(bookId);
        book.setStatus(BookStatus.BORROWED.toString());
        
        LoanQueue loanQueue = new LoanQueue(bookId);
        loanQueue.setQueue(new ArrayList<>());
        
        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(loanQueueRepository.findById(bookId)).thenReturn(Optional.of(loanQueue));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        
        Loan returnedLoan = loanService.returnBook(loanId);
        
        assertNotNull(returnedLoan);
        assertEquals(Loan.LoanStatus.RETURNED, returnedLoan.getStatus());
        assertNotNull(returnedLoan.getReturnDate());
        verify(bookRepository, times(1)).save(any(Book.class));
    }
}