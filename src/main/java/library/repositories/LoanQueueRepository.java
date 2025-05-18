package library.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import library.models.LoanQueue;

@Repository
public interface LoanQueueRepository extends MongoRepository<LoanQueue, String> {
	LoanQueue findByBookId(String bookId);

	void deleteByBookId(String bookId);
}