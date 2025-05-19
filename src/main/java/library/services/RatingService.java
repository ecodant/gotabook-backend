package library.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import library.models.Book;
import library.models.Rating;
import library.repositories.BookRepository;
import library.repositories.RatingRepository;

@Service
public class RatingService {

	private final RatingRepository ratingRepository;
	private final BookRepository bookRepository;

	@Autowired
	public RatingService(RatingRepository ratingRepository, BookRepository bookRepository) {
		this.ratingRepository = ratingRepository;
		this.bookRepository = bookRepository;
	}

	// Create a new rating
	@Transactional
	public Rating createRating(Rating rating) {
		// Check if user already rated this book
		Optional<Rating> existingRating = ratingRepository.findByBookIdAndUserId(rating.getBookId(),
				rating.getUserId());

		if (existingRating.isPresent()) {
			// Update existing rating
			Rating updatedRating = existingRating.get();
			updatedRating.setRating(rating.getRating());
			updatedRating.setComment(rating.getComment());
			rating = updatedRating;
		}

		// Save the rating
		Rating savedRating = ratingRepository.save(rating);

		// Update book's average rating
		updateBookAverageRating(rating.getBookId());

		return savedRating;
	}

	// Get all ratings
	public List<Rating> getAllRatings() {
		return ratingRepository.findAll();
	}

	// Get rating by ID
	public Optional<Rating> getRatingById(String id) {
		return ratingRepository.findById(id);
	}

	// Get ratings by book ID
	public List<Rating> getRatingsByBookId(String bookId) {
		return ratingRepository.findByBookId(bookId);
	}

	// Get ratings by user ID
	public List<Rating> getRatingsByUserId(String userId) {
		return ratingRepository.findByUserId(userId);
	}

	@Transactional
	public Rating updateRating(String ratingId, int newRatingValue, String newComment) {
		// Fetch the existing rating
		Optional<Rating> existingRatingOpt = ratingRepository.findById(ratingId);
		if (existingRatingOpt.isEmpty()) {
			throw new IllegalArgumentException("Rating with ID " + ratingId + " not found.");
		}

		Rating existingRating = existingRatingOpt.get();

		// Update only the rating value and comment
		existingRating.setRating(newRatingValue);
		existingRating.setComment(newComment);

		// Save the updated rating
		Rating updatedRating = ratingRepository.save(existingRating);

		// Update the book's average rating
		updateBookAverageRating(existingRating.getBookId());

		return updatedRating;
	}

	// Delete a rating
	@Transactional
	public void deleteRating(String id) {
		Optional<Rating> ratingOpt = ratingRepository.findById(id);
		if (ratingOpt.isPresent()) {
			String bookId = ratingOpt.get().getBookId();
			ratingRepository.deleteById(id);
			updateBookAverageRating(bookId);
		}
	}

	// Helper method to update a book's average rating
	private void updateBookAverageRating(String bookId) {
		List<Rating> bookRatings = ratingRepository.findByBookId(bookId);

		double averageRating = 0.0;
		if (!bookRatings.isEmpty()) {
			double sum = bookRatings.stream().mapToDouble(Rating::getRating).sum();
			averageRating = sum / bookRatings.size();
		}

		Optional<Book> bookOpt = bookRepository.findById(bookId);
		System.out.println("Book ID TO REE CALCULATE: " + bookOpt.get().getTitle());
		if (bookOpt.isPresent()) {
			Book book = bookOpt.get();
			System.out.println("Updating average rating for book: " + book.getTitle() + " to " + averageRating);
			book.setAverageRating(averageRating);
			bookRepository.save(book); // Persist the updated book in MongoDB
		}
	}
}