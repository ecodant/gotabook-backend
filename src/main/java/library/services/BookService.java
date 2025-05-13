package library.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import library.models.Book;
import library.models.BookStatus;
import library.repositories.BookRepository;

@Service
public class BookService {

	private final BookRepository bookRepository;

	@Autowired
	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	// Create a new book
	public Book createBook(Book book) {
		return bookRepository.save(book);
	}

	// Get all books
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	// Get book by ID
	public Optional<Book> getBookById(String id) {
		return bookRepository.findById(id);
	}

	// Update a book
	public Book updateBook(Book book) {
		return bookRepository.save(book);
	}

	// Delete a book
	public void deleteBook(String id) {
		bookRepository.deleteById(id);
	}

	// Search books by title
	public List<Book> searchBooksByTitle(String title) {
		return bookRepository.findByTitleContainingIgnoreCase(title);
	}

	// Search books by author
	public List<Book> searchBooksByAuthor(String author) {
		return bookRepository.findByAuthorContainingIgnoreCase(author);
	}

	// Search books by category
	public List<Book> searchBooksByCategory(String category) {
		return bookRepository.findByCategoryIgnoreCase(category);
	}

	// Get books by status
	public List<Book> getBooksByStatus(BookStatus status) {
		return bookRepository.findByStatus(status);
	}

	// Update book status
	public Book updateBookStatus(String id, BookStatus status) {
		Optional<Book> bookOpt = bookRepository.findById(id);
		if (bookOpt.isPresent()) {
			Book book = bookOpt.get();
			book.setStatus(status);
			return bookRepository.save(book);
		}
		return null;
	}

	// Update book rating
	public Book updateBookRating(String id, double averageRating) {
		Optional<Book> bookOpt = bookRepository.findById(id);
		if (bookOpt.isPresent()) {
			Book book = bookOpt.get();
			book.setAverageRating(averageRating);
			return bookRepository.save(book);
		}
		return null;
	}
}