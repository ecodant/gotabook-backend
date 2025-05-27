package library.services;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import library.models.Book;
import library.models.BookStatus;
import library.models.Loan;
import library.models.Loan.LoanStatus;
import library.models.LoanQueue;
import library.repositories.BookRepository;
import library.repositories.LoanQueueRepository;
import library.repositories.LoanRepository;

@Service
public class LoanService {

	private final LoanRepository loanRepository;
	private final LoanQueueRepository loanQueueRepository;
	private final BookRepository bookRepository;

	@Autowired
	public LoanService(LoanRepository loanRepository, BookRepository bookRepository,
			LoanQueueRepository loanQueueRepository) {
		this.loanRepository = loanRepository;
		this.bookRepository = bookRepository;
		this.loanQueueRepository = loanQueueRepository;
	}

	public Loan createLoan(Loan newLoan) {
		// 1. Fetch the book
		Optional<Book> bookOpt = bookRepository.findById(newLoan.getBookId());
		if (bookOpt.isEmpty()) {
			throw new IllegalArgumentException("Book with ID " + newLoan.getBookId() + " not found.");
		}

		Book book = bookOpt.get();

		// 2. If book is AVAILABLE, loan it out immediately
		if (BookStatus.AVAILABLE.toString().equals(book.getStatus())) {
			// Mark book as borrowed
			book.setStatus(BookStatus.BORROWED.toString());
			bookRepository.save(book);

			// Mark loan as ACTIVE
			newLoan.setStatus(LoanStatus.ACTIVE);
			newLoan.setBookTitle(book.getTitle());

			return loanRepository.save(newLoan);
		}

		// 3. If book is already borrowed will queue the loan
		newLoan.setStatus(LoanStatus.WAITING);
		newLoan.setBookTitle(book.getTitle());
		// Waiting loans have no return date yet
		newLoan.setReturnDate(null); 

		LoanQueue loanQueue = loanQueueRepository.findById(newLoan.getBookId())
				.orElse(new LoanQueue(newLoan.getBookId()));

		// Add to queue and maintain sorting order
		loanQueue.addLoan(newLoan);
		
		// Get queue as list for sorting
		List<Loan> waitingList = loanQueue.getQueue();
		// FIFO 
		waitingList.sort(Comparator.comparing(Loan::getLoanDate)); 
		
		// Set the sorted queue back
		loanQueue.setQueue(waitingList);
		loanQueueRepository.save(loanQueue);

		return loanRepository.save(newLoan);
	}

	// Get all loans
	public List<Loan> getAllLoans() {
		return loanRepository.findAll();
	}

	public List<Loan> getLoanQueueByBookId(String bookId) {
		Optional<LoanQueue> loanQueueOpt = loanQueueRepository.findById(bookId);
		return loanQueueOpt.map(LoanQueue::getQueue).orElse(null);
	}

	// Get loan by ID
	public Optional<Loan> getLoanById(String id) {
		return loanRepository.findById(id);
	}

	// Get loans by user ID
	public List<Loan> getLoansByUserId(String userId) {
		return loanRepository.findByUserId(userId);
	}

	// Get active loans by user ID
	public List<Loan> getActiveLoansByUserId(String userId) {
		return loanRepository.findByUserIdAndStatus(userId, Loan.LoanStatus.ACTIVE);
	}

	// Get loans by book ID
	public List<Loan> getLoansByBookId(String bookId) {
		return loanRepository.findByBookId(bookId);
	}

	// Return a book (update loan status and book status)
	public Loan returnBook(String loanId) {
		Optional<Loan> loanOpt = loanRepository.findById(loanId);
		if (loanOpt.isEmpty()) {
			return null;
		}

		Loan returnedLoan = loanOpt.get();
		returnedLoan.setStatus(LoanStatus.RETURNED);
		returnedLoan.setReturnDate(new Date());
		loanRepository.save(returnedLoan);

		// 1. Fetch and update the Book
		Optional<Book> bookOpt = bookRepository.findById(returnedLoan.getBookId());
		if (bookOpt.isEmpty()) {
			return returnedLoan;
		}

		Book book = bookOpt.get();

		// 2. Fetch loan queue
		Optional<LoanQueue> loanQueueOpt = loanQueueRepository.findById(book.getId());

		if (loanQueueOpt.isPresent()) {
			LoanQueue loanQueue = loanQueueOpt.get();
			
			if (!loanQueue.isEmpty()) {
				// 3. Promote next loan in line
				Loan nextLoan = loanQueue.pollFirst();
				nextLoan.setStatus(LoanStatus.ACTIVE);
				nextLoan.setLoanDate(new Date());
				loanRepository.save(nextLoan);

				// 4. Update book status and save
				book.setStatus(BookStatus.BORROWED.toString());
				bookRepository.save(book);

				// 5. Persist updated queue
				loanQueueRepository.save(loanQueue);
			} else {
				// No one waiting, mark book as available
				book.setStatus(BookStatus.AVAILABLE.toString());
				bookRepository.save(book);

				// Optionally delete the empty queue
				loanQueueRepository.deleteById(book.getId());
			}
		} else {
			// No queue exists, mark book as available
			book.setStatus(BookStatus.AVAILABLE.toString());
			bookRepository.save(book);
		}

		return returnedLoan;
	}


	public void deleteLoan(String id) {
		loanRepository.deleteById(id);
	}
}