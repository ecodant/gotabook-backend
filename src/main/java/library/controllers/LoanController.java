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

import library.models.Loan;
import library.services.LoanService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

	private final LoanService loanService;

	@Autowired
	public LoanController(LoanService loanService) {
		this.loanService = loanService;
	}

	// Create a new loan
	@PostMapping("/")
	public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
		Loan createdLoan = loanService.createLoan(loan);
		if (createdLoan != null) {
			return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// Get all loans
	@GetMapping("/")
	public ResponseEntity<List<Loan>> getAllLoans() {
		List<Loan> loans = loanService.getAllLoans();
		return new ResponseEntity<>(loans, HttpStatus.OK);
	}

	// Get loan by ID
	@GetMapping("/{id}")
	public ResponseEntity<Loan> getLoanById(@PathVariable String id) {
		Optional<Loan> loan = loanService.getLoanById(id);
		return loan.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// Get loans by user ID
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Loan>> getLoansByUserId(@PathVariable String userId) {
		List<Loan> loans = loanService.getLoansByUserId(userId);
		return new ResponseEntity<>(loans, HttpStatus.OK);
	}

	// Get active loans by user ID
	@GetMapping("/user/{userId}/active")
	public ResponseEntity<List<Loan>> getActiveLoansByUserId(@PathVariable String userId) {
		List<Loan> loans = loanService.getActiveLoansByUserId(userId);
		return new ResponseEntity<>(loans, HttpStatus.OK);
	}

	// Get loans by book ID
	@GetMapping("/book/{bookId}")
	public ResponseEntity<List<Loan>> getLoansByBookId(@PathVariable String bookId) {
		List<Loan> loans = loanService.getLoansByBookId(bookId);
		return new ResponseEntity<>(loans, HttpStatus.OK);
	}

	// Return a book
	@PutMapping("/{id}/return")
	public ResponseEntity<Loan> returnBook(@PathVariable String id) {
		Loan returnedLoan = loanService.returnBook(id);
		if (returnedLoan != null) {
			return new ResponseEntity<>(returnedLoan, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// Delete a loan
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLoan(@PathVariable String id) {
		loanService.deleteLoan(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}