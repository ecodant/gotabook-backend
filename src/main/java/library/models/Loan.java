package library.models;

import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "loans")
public class Loan {
	@Id
	private String id;
	private String bookId;
	private String bookTitle;
	private String userId;
	private Date loanDate;
	private Date returnDate;
	private LoanStatus status;

	public enum LoanStatus {
		ACTIVE, RETURNED, WAITING
	}

	public Loan() {
		this.loanDate = new Date();
		this.status = LoanStatus.ACTIVE;
		this.returnDate = calculateDefaultReturnDate();
	}

	public Loan(String bookId, String userId) {
		this.bookId = bookId;
		this.userId = userId;
		this.loanDate = new Date();
		this.status = LoanStatus.ACTIVE;
		this.returnDate = calculateDefaultReturnDate();
	}

	// Helper method to calculate default return date
	private Date calculateDefaultReturnDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.loanDate);
		calendar.add(Calendar.DAY_OF_YEAR, 14);
		return calendar.getTime();
	}

	// Getters and Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
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

	public Date getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(Date loanDate) {
		this.loanDate = loanDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public LoanStatus getStatus() {
		return status;
	}

	public void setStatus(LoanStatus status) {
		this.status = status;
	}
}
