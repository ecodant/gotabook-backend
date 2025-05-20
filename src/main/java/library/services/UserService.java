package library.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import library.models.User;
import library.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User createUser(User user) {
		user.setFriends(new HashSet<>());
		return userRepository.save(user);
	}

	public Optional<User> login(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> getUserById(String id) {
		return userRepository.findById(id);
	}

	public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User updateUser(User newUser) {
		// Fetch the original user data
		Optional<User> originalUserOpt = userRepository.findById(newUser.getId());
		if (originalUserOpt.isEmpty()) {
			throw new IllegalArgumentException("User with ID " + newUser.getId() + " not found.");
		}

		User originalUser = originalUserOpt.get();

		// Validate some fields for avoid nulls values
		if (newUser.getUsername() != null) {
			originalUser.setUsername(newUser.getUsername());
		}
		if (newUser.getEmail() != null) {
			originalUser.setEmail(newUser.getEmail());
		}
		if (newUser.getPassword() != null) {
			originalUser.setPassword(newUser.getPassword());
		}
		if (newUser.getRole() != null) {
			originalUser.setRole(newUser.getRole());
		}
		if (newUser.getFriends() != null) {
			originalUser.setFriends(newUser.getFriends());
		}

		// Save the updated user
		return userRepository.save(originalUser);
	}

	public void deleteUser(String id) {
		userRepository.deleteById(id);
	}

	public List<User> getUsersByRole(User.UserRole role) {
		return userRepository.findByRole(role);
	}
}