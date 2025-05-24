package library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import library.models.Rating;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {
	List<Rating> findByBookId(String bookId);

	List<Rating> findByUserId(String userId);

	Optional<Rating> findByBookIdAndUserId(String bookId, String userId);

	// Count ratings for a book
	long countByBookId(String bookId);
}