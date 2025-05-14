package library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import library.models.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
	List<Book> findByTitleContainingIgnoreCase(String title);

	List<Book> findByAuthorContainingIgnoreCase(String author);

	List<Book> findByCategory(String category);

	List<Book> findByStatus(String status);

	Optional<Book> findById(String id);

	List<Book> findByYearBetween(int startYear, int endYear);
}