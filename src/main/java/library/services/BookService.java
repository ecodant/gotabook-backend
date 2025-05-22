package library.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import datastructures.BookBST;
import datastructures.ArraycitaList;
import datastructures.LinkedListSimple;
import jakarta.annotation.PostConstruct;
import library.exceptions.ResourceNotFoundException;
import library.models.Book;
import library.repositories.BookRepository;

@Service
public class BookService {
	private final BookRepository bookRepository;
	private final BookBST bookBST;
	private static final Logger logger = LoggerFactory.getLogger(BookService.class);

	@Autowired
	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
		this.bookBST = new BookBST();
		initializeBSTFromDatabase();
	}

	@PostConstruct
	private void initializeBSTFromDatabase() {
		logger.info("Loading books from database into BST...");
		java.util.List<Book> allBooks = bookRepository.findAll();
		for (Book book : allBooks) {
			bookBST.insert(book);
		}
		logger.info("Loaded {} books into BST", allBooks.size());
	}


	public Book addBook(Book book) {
		// First save to MongoDB
		Book savedBook = bookRepository.save(book);
		// Then add to BST
		bookBST.insert(savedBook);
		return savedBook;
	}

	// Search by title (using BST for efficiency)
	public Book findByTitle(String title) {
		return bookBST.search(title);
	}

	public ArraycitaList<Book> findByTitlePrefix(String prefix) {
		ArraycitaList<Book> result = new ArraycitaList<>();
		java.util.List<Book> allBooks = bookBST.getAllBooks();

		for (Book book : allBooks) {
			if (book.getTitle().toLowerCase().startsWith(prefix.toLowerCase())) {
				result.add(book);
			}
		}

		return result;
	}

	
	public LinkedListSimple<Book> getAllBooksSorted() {
		java.util.List<Book> books = bookRepository.findAll();
		LinkedListSimple<Book> linkedBooks = new LinkedListSimple<>();
		
		for (Book book : books) {
			linkedBooks.add(book);
		}
		
		return linkedBooks;
	}


	public Book updateBook(String id, Book updatedBook) {
		// First find the book in MongoDB
		Book existingBook = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

		// Update fields only if they are not null
		if (updatedBook.getTitle() != null) {
			existingBook.setTitle(updatedBook.getTitle());
		}
		if (updatedBook.getAuthor() != null) {
			existingBook.setAuthor(updatedBook.getAuthor());
		}
		if (updatedBook.getYear() > 0) {
			existingBook.setYear(updatedBook.getYear());
		}
		if (updatedBook.getCategory() != null) {
			existingBook.setCategory(updatedBook.getCategory());
		}
		if (updatedBook.getStatus() != null) {
			existingBook.setStatus(updatedBook.getStatus());
		}

		// Save to MongoDB
		Book savedBook = bookRepository.save(existingBook);

		// Update BST 
		bookBST.delete(existingBook.getTitle());
		bookBST.insert(savedBook);

		return savedBook;
	}

	// Delete a book
	public void deleteBook(String id) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

		// Delete from MongoDB
		bookRepository.deleteById(id);

		// Delete from BST
		bookBST.delete(book.getTitle());
	}


	public ArraycitaList<Book> findByAuthor(String author) {
		// Since we're searching by author (not optimized in BST),
		// we'll use MongoDB directly and convert to our custom data structure
		java.util.List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);
		return convertToCustomArrayList(books);
	}

	public ArraycitaList<Book> findByCategory(String category) {
		// For the bear requirement of use a custom Data Structure
		java.util.List<Book> books = bookRepository.findByCategory(category);
		return convertToCustomArrayList(books);
	}

	public Optional<Book> findById(String id) {
		return bookRepository.findById(id);
	}
	
	// Helper method to convert standard Java List to our ArraycitaList
	private ArraycitaList<Book> convertToCustomArrayList(java.util.List<Book> books) {
		ArraycitaList<Book> customList = new ArraycitaList<>(books.size());
		for (Book book : books) {
			customList.add(book);
		}
		return customList;
	}
}