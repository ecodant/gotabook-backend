package library.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import library.models.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, ObjectId> {
	List<Message> findBySenderId(ObjectId senderId);

	List<Message> findByReceiverId(ObjectId receiverId);

	List<Message> findBySenderIdAndReceiverId(ObjectId senderId, ObjectId receiverId);

	List<Message> findByReceiverIdAndReadFalse(ObjectId receiverId);

	List<Message> findAllByOrderByDateDesc();

	long countByReceiverIdAndReadFalse(ObjectId receiverId);
}