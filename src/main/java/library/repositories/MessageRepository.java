package library.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import library.models.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
	List<Message> findBySenderId(String senderId);

	List<Message> findByReceiverId(String receiverId);

	List<Message> findBySenderIdAndReceiverId(String senderId, String receiverId);

	List<Message> findByReceiverIdAndReadFalse(String receiverId);

	List<Message> findAllByOrderByDateDesc();

	long countByReceiverIdAndReadFalse(String receiverId);
}