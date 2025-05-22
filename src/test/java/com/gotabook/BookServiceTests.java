package com.gotabook;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import datastructures.BookBST;

import library.repositories.BookRepository;
import library.services.BookService;

@ExtendWith(MockitoExtension.class)
class BookServiceTests {

    @Mock
    private BookRepository bookRepository;
    
    @Mock
    private BookBST bookBST;
    
    @InjectMocks
    private BookService bookService;
    


}