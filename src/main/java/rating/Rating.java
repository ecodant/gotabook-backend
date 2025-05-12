package rating;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ratings")
public class Rating {
	@Id
	private ObjectId id;

	private ObjectId bookId;
	private ObjectId userId;
	private int rating; // 1-5 stars, at least that is the idea
	private String comment;
	private Date date;
}