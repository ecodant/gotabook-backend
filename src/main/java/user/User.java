package user;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
	@Id
	private ObjectId id;

	@Indexed(unique = true)
	private String username;

	private String password;

	@Indexed(unique = true)
	private String email;

	private UserRole role;
	private Date registrationDate;
	private List<ObjectId> connections; // References (for affinity graph)

	public enum UserRole {
		READER, ADMIN
	}
}