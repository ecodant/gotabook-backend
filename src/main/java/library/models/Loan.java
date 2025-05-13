package library.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "loans")
public class Loan {
	@Id
	private String id;
	private String bookId;
	private String userId;
	private Date loanDate;
	private Date returnDate;
	private LoanStatus status;

	public enum LoanStatus {
		ACTIVE, RETURNED, WAITING
	}

	// Default constructor
	public Loan() {
		this.loanDate = new Date();
		this.status = LoanStatus.ACTIVE;
	}

	// Constructor with fields
	public Loan(String bookId, String userId) {
		this.bookId = bookId;
		this.userId = userId;
		this.loanDate = new Date();
		this.status = LoanStatus.ACTIVE;
	}

	// Getters and Setters
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
