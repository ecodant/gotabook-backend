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
	private String status; // AVAILABLE, BORROWED
	private double averageRating;

	// Standard getters and setters

	// Constructor
	public Book() {
		// Default constructor required by MongoDB
		this.status = "AVAILABLE";
	}

	public Book(String title, String author, int year, String category) {
		this.title = title;
		this.author = author;
		this.year = year;
		this.category = category;
		this.status = "AVAILABLE";
		this.averageRating = 0.0;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

}