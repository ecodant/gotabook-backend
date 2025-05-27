package library.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import datastructures.ArraycitaList;

@Document(collection = "loan_queues")
public class LoanQueue {
	@Id
	private String bookId; 
	private ArraycitaList<Loan> queue;

	public LoanQueue(String bookId) {
		this.bookId = bookId;
		this.queue = new ArraycitaList<>();
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	// Convert to List for compatibility with existing code
	public List<Loan> getQueue() {
		return queue.toList();
	}

	// Accept regular List but store as ArraycitaList
	public void setQueue(List<Loan> queue) {
		this.queue = ArraycitaList.fromList(queue);
	}
	
	// Add a loan to the queue
	public void addLoan(Loan loan) {
		this.queue.add(loan);
	}
	
	// Remove a loan at specific index
	public Loan removeLoan(int index) {
		return this.queue.remove(index);
	}
	
	// Get the first loan in the queue (without removing)
	public Loan peekFirst() {
		if (queue.isEmpty()) {
			return null;
		}
		return queue.get(0);
	}
	
	// Remove and return the first loan in the queue
	public Loan pollFirst() {
		if (queue.isEmpty()) {
			return null;
		}
		return queue.remove(0);
	}
	
	// Get queue size
	public int size() {
		return queue.size();
	}
	
	// Check if queue is empty
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	// Return all loans and clear the queue
	public List<Loan> pop() {
		if (queue.isEmpty()) {
			return new ArrayList<>();
		}
		List<Loan> loansToReturn = queue.toList();
		queue.clear();
		return loansToReturn;
	}
}
