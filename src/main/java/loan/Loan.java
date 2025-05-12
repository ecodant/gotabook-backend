package loan;

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
@Document(collection = "loans")
public class Loan {
	@Id
	private ObjectId id;

	private ObjectId bookId;
	private ObjectId userId;
	private Date loanDate;
	private Date returnDate;
	private LoanStatus status;

	public enum LoanStatus {
		ACTIVE, RETURNED, WAITING
	}
}
