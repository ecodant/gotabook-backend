package library.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import library.models.Role;
import library.models.User;

public interface UserRepository extends MongoRepository<User, String> {
	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	Optional<User> findByEmailAndPassword(String email, String password);

	List<User> findByRole(Role role);
}
