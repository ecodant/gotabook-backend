package library.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ratings")
public class Rating {
	@Id
	private String id;
	private String bookId;
	private String userId;
	private int rating;
	private String comment;
	private Date date;

	public Rating() {
		this.date = new Date();
	}

	public Rating(String bookId, String userId, int rating, String comment) {
		this.bookId = bookId;
		this.userId = userId;
		this.rating = rating;
		this.comment = comment;
		this.date = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}