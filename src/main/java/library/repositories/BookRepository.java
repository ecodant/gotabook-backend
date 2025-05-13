package library.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import library.models.Book;
import library.models.BookStatus;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
	List<Book> findByTitleContainingIgnoreCase(String title);

	List<Book> findByAuthorContainingIgnoreCase(String author);

	List<Book> findByCategoryIgnoreCase(String category);

	List<Book> findByStatus(BookStatus status);
}
