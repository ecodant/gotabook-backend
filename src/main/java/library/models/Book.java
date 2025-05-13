package library.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
public class Book {
	@Id
	private String id;
	private String title;
	private String author;
	private int year;
	private String category;
	private BookStatus status;
	private double averageRating;

	// Default constructor
	public Book() {
		this.status = BookStatus.AVAILABLE;
		this.averageRating = 0.0;
	}

	// Constructor with fields
	public Book(String title, String author, int year, String category) {
		this.title = title;
		this.author = author;
		this.year = year;
		this.category = category;
		this.status = BookStatus.AVAILABLE;
		this.averageRating = 0.0;
	}

	// Getters and Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}
}