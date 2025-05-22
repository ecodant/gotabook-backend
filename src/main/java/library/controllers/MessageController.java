package library.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import library.models.Message;
import library.services.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

	private final MessageService messageService;

	@Autowired
	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@PostMapping("/")
	public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
		return ResponseEntity.ok(messageService.sendMessage(message));
	}

	@GetMapping("/")
	public ResponseEntity<List<Message>> getAllMessages() {
		return ResponseEntity.ok(messageService.getAllMessages());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Message> getMessage(@PathVariable String id) {
		return ResponseEntity.ok(messageService.getMessageById(id));
	}

	@GetMapping("/sender/{senderId}")
	public ResponseEntity<List<Message>> getMessagesBySender(@PathVariable String senderId) {
		return ResponseEntity.ok(messageService.getMessagesBySender(senderId));
	}

	@GetMapping("/receiver/{receiverId}")
	public ResponseEntity<List<Message>> getMessagesByReceiver(@PathVariable String receiverId) {
		return ResponseEntity.ok(messageService.getMessagesByReceiver(receiverId));
	}

	@GetMapping("/unread/{receiverId}")
	public ResponseEntity<List<Message>> getUnreadMessages(@PathVariable String receiverId) {
		return ResponseEntity.ok(messageService.getUnreadMessages(receiverId));
	}

	@GetMapping("/unread/count/{receiverId}")
	public ResponseEntity<Long> countUnreadMessages(@PathVariable String receiverId) {
		return ResponseEntity.ok(messageService.countUnreadMessages(receiverId));
	}

	@PutMapping("/read/{messageId}")
	public ResponseEntity<Message> markAsRead(@PathVariable String messageId) {
		return ResponseEntity.ok(messageService.markAsRead(messageId));
	}

	@DeleteMapping("/{messageId}")
	public ResponseEntity<Void> deleteMessage(@PathVariable String messageId) {
		messageService.deleteMessage(messageId);
		return ResponseEntity.noContent().build();
	}
}