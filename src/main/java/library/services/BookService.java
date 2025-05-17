package library.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import datastructures.BookBST;
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
		List<Book> allBooks = bookRepository.findAll();
		for (Book book : allBooks) {
			bookBST.insert(book);
		}
		logger.info("Loaded {} books into BST", allBooks.size());
	}

	// Add a new book
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

	// Search by title prefix (e.g., books starting with "The")
	public List<Book> findByTitlePrefix(String prefix) {
		List<Book> result = new ArrayList<>();
		List<Book> allBooks = bookBST.getAllBooks();

		for (Book book : allBooks) {
			if (book.getTitle().toLowerCase().startsWith(prefix.toLowerCase())) {
				result.add(book);
			}
		}

		return result;
	}

	// Get all books in alphabetical order
	public List<Book> getAllBooksSorted() {
		return bookRepository.findAll();
	}

	// Update a book
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
		if (updatedBook.getStatus() != null) { // Assuming 'status' is a field in the Book class
			existingBook.setStatus(updatedBook.getStatus());
		}

		// Save to MongoDB
		Book savedBook = bookRepository.save(existingBook);

		// Update BST (delete and re-insert)
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

	// Additional methods for other search criteria
	public List<Book> findByAuthor(String author) {
		// Since we're searching by author (not optimized in BST),
		// we'll use MongoDB directly
		return bookRepository.findByAuthorContainingIgnoreCase(author);
	}

	public List<Book> findByCategory(String category) {
		// Use MongoDB for category search
		return bookRepository.findByCategory(category);
	}

	public Optional<Book> findById(String id) {
		return bookRepository.findById(id);
	}
}