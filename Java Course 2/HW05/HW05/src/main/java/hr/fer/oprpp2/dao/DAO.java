package hr.fer.oprpp2.dao;

import hr.fer.oprpp2.model.BlogEntry;
import hr.fer.oprpp2.model.BlogUser;

import java.util.List;

public interface DAO {
	List<BlogUser> getAuthors();

	BlogUser findUser(String nick);

	BlogEntry getEntry(Long entryId);

}
