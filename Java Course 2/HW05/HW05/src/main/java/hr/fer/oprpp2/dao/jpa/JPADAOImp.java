package hr.fer.oprpp2.dao.jpa;

import hr.fer.oprpp2.dao.DAO;
import hr.fer.oprpp2.model.BlogEntry;
import hr.fer.oprpp2.model.BlogUser;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class JPADAOImp implements DAO {
	@Override
	public List<BlogUser> getAuthors() {
		EntityManager em = JPAEMProvider.getEntityManager();

		List<BlogUser> authors = (List<BlogUser>) em.createQuery("select a from BlogUser as a").getResultList();

		return authors;
	}

	@Override
	public BlogUser findUser(String nick) {
		EntityManager em = JPAEMProvider.getEntityManager();
		BlogUser bu;
		try {
			bu = (BlogUser) em.createQuery("select bu from BlogUser as bu where nick=:n")
					.setParameter("n", nick)
					.getSingleResult();
		} catch(NoResultException e) {
			return null;
		}
		return bu;
	}

	@Override
	public BlogEntry getEntry(Long entryId) {
		return JPAEMProvider.getEntityManager().find(BlogEntry.class, entryId);
	}
}
