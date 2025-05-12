package book;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.convert.ObjectPath;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class Book {
	@Id
	private ObjectPath id;

	private String title;
	private String author;
	private String category;
	private int averageRating;
	private Status status;
	private int yearPublished;

}
