package library.controllers;

import java.util.ArrayList;
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

import datastructures.ArraycitaList;
import datastructures.LinkedListSimple;
import library.models.Book;
import library.services.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {
	private final BookService bookCatalogService;

	@Autowired
	public BookController(BookService bookCatalogService) {
		this.bookCatalogService = bookCatalogService;
	}

	@GetMapping("/")
	public ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false) String sortBy) {
		LinkedListSimple<Book> books = bookCatalogService.getAllBooksSorted();
		return ResponseEntity.ok(books.toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable String id) {
		Optional<Book> book = bookCatalogService.findById(id);

		if (book.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else
			return ResponseEntity.ok(book.get());
	}

	@GetMapping("/title/{title}")
	public ResponseEntity<Book> getBookByTitle(@PathVariable String title) {
		Book book = bookCatalogService.findByTitle(title);
		if (book == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(book);
	}

	@GetMapping("/search")
	public ResponseEntity<List<Book>> searchBooks(@RequestParam(required = false) String title,
			@RequestParam(required = false) String author, @RequestParam(required = false) String category) {

		List<Book> results = new ArrayList<>();

		if (title != null) {
			// Use BST for efficient title search
			ArraycitaList<Book> booksFound = bookCatalogService.findByTitlePrefix(title);
			for (Book book : booksFound) {
				results.add(book);
			}
		} else if (author != null) {
			// Use custom data structure for author search
			ArraycitaList<Book> booksFound = bookCatalogService.findByAuthor(author);
			for (Book book : booksFound) {
				results.add(book);
			}
		} else if (category != null) {
			// Use custom data structure for category search
			ArraycitaList<Book> booksFound = bookCatalogService.findByCategory(category);
			for (Book book : booksFound) {
				results.add(book);
			}
		} else {
			// Return all books if no parameters
			LinkedListSimple<Book> books = bookCatalogService.getAllBooksSorted();
			results = books.toList();
		}

		return ResponseEntity.ok(results);
	}

	@PostMapping("/")
	public ResponseEntity<Book> createBook(@RequestBody Book book) {
		Book newBook = bookCatalogService.addBook(book);
		return ResponseEntity.status(HttpStatus.CREATED).body(newBook);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Book> updateBook(@PathVariable String id, @RequestBody Book book) {
		Book updatedBook = bookCatalogService.updateBook(id, book);
		return ResponseEntity.ok(updatedBook);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable String id) {
		bookCatalogService.deleteBook(id);
		return ResponseEntity.noContent().build();
	}
}