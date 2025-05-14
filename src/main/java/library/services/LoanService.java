package library.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import library.models.Book;
import library.models.BookStatus;
import library.models.Loan;
import library.repositories.BookRepository;
import library.repositories.LoanRepository;

@Service
public class LoanService {

	private final LoanRepository loanRepository;
	private final BookRepository bookRepository;

	@Autowired
	public LoanService(LoanRepository loanRepository, BookRepository bookRepository) {
		this.loanRepository = loanRepository;
		this.bookRepository = bookRepository;
	}

	// Create a new loan
	public Loan createLoan(Loan loan) {
		// Update book status if it's available
		Optional<Book> bookOpt = bookRepository.findById(loan.getBookId());
		if (bookOpt.isPresent()) {
			Book book = bookOpt.get();
			if (book.getStatus().equals(BookStatus.AVAILABLE.toString())) {
				book.setStatus(BookStatus.BORROWED.toString());
				bookRepository.save(book);
//				System.out.println("Book found: " + bookOpt.get());
				return loanRepository.save(loan);
			}
		}
		return null;
	}

	// Get all loans
	public List<Loan> getAllLoans() {
		return loanRepository.findAll();
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
		if (loanOpt.isPresent()) {
			Loan loan = loanOpt.get();
			loan.setStatus(Loan.LoanStatus.RETURNED);
			loan.setReturnDate(new Date());

			// Update book status
			Optional<Book> bookOpt = bookRepository.findById(loan.getBookId());
			if (bookOpt.isPresent()) {
				Book book = bookOpt.get();
				book.setStatus(BookStatus.AVAILABLE.toString());
				bookRepository.save(book);
			}

			return loanRepository.save(loan);
		}
		return null;
	}

	// Delete a loan
	public void deleteLoan(String id) {
		loanRepository.deleteById(id);
	}
}