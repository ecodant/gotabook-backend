package library.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import library.models.Loan;

@Repository
public interface LoanRepository extends MongoRepository<Loan, String> {
	List<Loan> findByUserId(String userId);

	List<Loan> findByBookId(String bookId);

	List<Loan> findByStatus(Loan.LoanStatus status);

	Loan findActiveLoanByBookId(String bookId);

	List<Loan> findByUserIdAndStatus(String userId, Loan.LoanStatus status);
}