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
import org.springframework.web.bind.annotation.RestController;

import library.models.Rating;
import library.services.RatingService;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

	private final RatingService ratingService;

	@Autowired
	public RatingController(RatingService ratingService) {
		this.ratingService = ratingService;
	}

	// Create a new rating
	@PostMapping
	public ResponseEntity<Rating> createRating(@RequestBody Rating rating) {
		Rating createdRating = ratingService.createRating(rating);
		return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
	}

	// Get all ratings
	@GetMapping("/")
	public ResponseEntity<List<Rating>> getAllRatings() {
		List<Rating> ratings = ratingService.getAllRatings();
		return new ResponseEntity<>(ratings, HttpStatus.OK);
	}

	// Get rating by ID
	@GetMapping("/{id}")
	public ResponseEntity<Rating> getRatingById(@PathVariable String id) {
		Optional<Rating> rating = ratingService.getRatingById(id);
		return rating.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// Get ratings by book ID
	@GetMapping("/book/{bookId}")
	public ResponseEntity<List<Rating>> getRatingsByBookId(@PathVariable String bookId) {
		List<Rating> ratings = ratingService.getRatingsByBookId(bookId);
		return new ResponseEntity<>(ratings, HttpStatus.OK);
	}

	// Get ratings by user ID
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Rating>> getRatingsByUserId(@PathVariable String userId) {
		List<Rating> ratings = ratingService.getRatingsByUserId(userId);
		return new ResponseEntity<>(ratings, HttpStatus.OK);
	}

	// Update a rating
	@PutMapping("/{id}")
	public ResponseEntity<Rating> updateRating(@PathVariable String id, @RequestBody Rating rating) {
		rating.setId(id);
		Rating updatedRating = ratingService.updateRating(rating.getId(), rating.getRating(), rating.getComment());
		return new ResponseEntity<>(updatedRating, HttpStatus.OK);
	}

	// Delete a rating
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRating(@PathVariable String id) {
		ratingService.deleteRating(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}