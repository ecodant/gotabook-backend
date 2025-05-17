package library.services;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import library.models.Message;
import library.repositories.MessageRepository;

@Service
public class MessageService {

	private final MessageRepository messageRepository;

	@Autowired
	public MessageService(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	public Message sendMessage(Message message) {
		message.setDate(new Date());
		message.setRead(false);
		return messageRepository.save(message);
	}

	public Message getMessageById(ObjectId id) {
		return messageRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
	}

	public List<Message> getMessagesBySender(ObjectId senderId) {
		return messageRepository.findBySenderId(senderId);
	}

	public List<Message> getMessagesByReceiver(ObjectId receiverId) {
		return messageRepository.findByReceiverId(receiverId);
	}

	public List<Message> getConversation(ObjectId senderId, ObjectId receiverId) {
		return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId);
	}

	public List<Message> getUnreadMessages(ObjectId receiverId) {
		return messageRepository.findByReceiverIdAndReadFalse(receiverId);
	}

	public List<Message> getAllMessages() {
		return messageRepository.findAllByOrderByDateDesc();
	}

	public long countUnreadMessages(ObjectId receiverId) {
		return messageRepository.countByReceiverIdAndReadFalse(receiverId);
	}

	public Message markAsRead(ObjectId messageId) {
		Message message = getMessageById(messageId);
		message.setRead(true);
		return messageRepository.save(message);
	}

	public void deleteMessage(ObjectId messageId) {
		messageRepository.deleteById(messageId);
	}
}