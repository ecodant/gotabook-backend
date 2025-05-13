package library.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import library.models.Book;
import library.models.BookStatus;
import library.services.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private final BookService bookService;

	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	// Create a new book
	@PostMapping
	public ResponseEntity<Book> createBook(@RequestBody Book book) {
		Book createdBook = bookService.createBook(book);
		return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
	}

	// Get all books
	@GetMapping
	public ResponseEntity<List<Book>> getAllBooks() {
		List<Book> books = bookService.getAllBooks();
		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	// Get book by ID
	@GetMapping("/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable String id) {
		Optional<Book> book = bookService.getBookById(id);
		return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// Update a book
	@PutMapping("/{id}")
	public ResponseEntity<Book> updateBook(@PathVariable String id, @RequestBody Book book) {
		book.setId(id);
		Book updatedBook = bookService.updateBook(book);
		return new ResponseEntity<>(updatedBook, HttpStatus.OK);
	}

	// Delete a book
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable String id) {
		bookService.deleteBook(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// Search books by title
	@GetMapping("/search/title")
	public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String title) {
		List<Book> books = bookService.searchBooksByTitle(title);
		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	// Search books by author
	@GetMapping("/search/author")
	public ResponseEntity<List<Book>> searchBooksByAuthor(@RequestParam String author) {
		List<Book> books = bookService.searchBooksByAuthor(author);
		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	// Search books by category
	@GetMapping("/search/category")
	public ResponseEntity<List<Book>> searchBooksByCategory(@RequestParam String category) {
		List<Book> books = bookService.searchBooksByCategory(category);
		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	// Get books by status
	@GetMapping("/status/{status}")
	public ResponseEntity<List<Book>> getBooksByStatus(@PathVariable BookStatus status) {
		List<Book> books = bookService.getBooksByStatus(status);
		return new ResponseEntity<>(books, HttpStatus.OK);
	}
}