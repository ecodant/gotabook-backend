package library.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "loan_queues")
public class LoanQueue {
	@Id
	private String bookId; 
	private List<Loan> queue = new ArrayList<>();

	public LoanQueue(String bookId) {
		this.bookId = bookId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public List<Loan> getQueue() {
		return queue;
	}

	public void setQueue(List<Loan> queue) {
		this.queue = queue;
	}
	public List<Loan> pop() {
		if (queue.isEmpty()) {
			return new ArrayList<>();
		}
		List<Loan> loansToReturn = new ArrayList<>(queue);
		queue.clear();
		return loansToReturn;
	}
}
