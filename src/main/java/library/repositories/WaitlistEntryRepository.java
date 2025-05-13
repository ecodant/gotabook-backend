package library.repositories;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import library.models.WaitlistEntry;

@Repository
public interface WaitlistEntryRepository extends MongoRepository<WaitlistEntry, ObjectId> {
	// Find entries by book ID
	List<WaitlistEntry> findByBookIdOrderByPriorityAsc(ObjectId bookId);

	// Find entries by user ID
	List<WaitlistEntry> findByUserId(ObjectId userId);

	// Find entry by book ID and user ID
	WaitlistEntry findByBookIdAndUserId(ObjectId bookId, ObjectId userId);

	// Find entries requested after a specific date
	List<WaitlistEntry> findByRequestDateAfter(Date date);

	// Count entries by book ID
	long countByBookId(ObjectId bookId);

	// Count entries by user ID
	long countByUserId(ObjectId userId);

	// Delete entry by book ID and user ID
	void deleteByBookIdAndUserId(ObjectId bookId, ObjectId userId);

	// Delete all entries for a book
	void deleteByBookId(ObjectId bookId);

	// Delete all entries by a user
	void deleteByUserId(ObjectId userId);
}