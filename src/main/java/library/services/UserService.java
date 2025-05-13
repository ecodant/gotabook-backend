package library.services;

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

	// Create a new user
	public User createUser(User user) {
		return userRepository.save(user);
	}

	public Optional<User> login(String username, String password) {
		return userRepository.findByUsernameAndPassword(username, password);
	}

	// Get all users
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	// Get user by ID
	public Optional<User> getUserById(String id) {
		return userRepository.findById(id);
	}

	// Get user by username
	public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	// Get user by email
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	// Update a user
	public User updateUser(User user) {

		return userRepository.save(user);
	}

	// Delete a user
	public void deleteUser(String id) {
		userRepository.deleteById(id);
	}

	// Get users by role
	public List<User> getUsersByRole(User.UserRole role) {
		return userRepository.findByRole(role);
	}
}