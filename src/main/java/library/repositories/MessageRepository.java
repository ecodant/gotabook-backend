package library.repositories;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import library.models.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, ObjectId> {
	// Find received messages by user ID
	List<Message> findByReceiverIdOrderByDateDesc(ObjectId receiverId);

	// Find sent messages by user ID
	List<Message> findBySenderIdOrderByDateDesc(ObjectId senderId);

	// Find unread messages by receiver ID
	List<Message> findByReceiverIdAndReadFalseOrderByDateDesc(ObjectId receiverId);

	// Find conversation between two users
	@Query("{ $or: [ { 'senderId': ?0, 'receiverId': ?1 }, { 'senderId': ?1, 'receiverId': ?0 } ] }")
	List<Message> findConversation(ObjectId userId1, ObjectId userId2);

	// Find messages sent after a specific date
	List<Message> findByDateAfter(Date date);

	// Count unread messages by receiver ID
	long countByReceiverIdAndReadFalse(ObjectId receiverId);

	// Delete all messages for a user (sent or received)
	@Query("{ $or: [ { 'senderId': ?0 }, { 'receiverId': ?0 } ] }")
	void deleteAllMessagesForUser(ObjectId userId);
}