package hr.fer.oprpp2.dao.jpa;

import hr.fer.oprpp2.dao.DAOException;

import javax.persistence.EntityManager;

public class JPAEMProvider {
	private static ThreadLocal<EntityManager> locals = new ThreadLocal<>();
	public static EntityManager getEntityManager() {
		EntityManager em = locals.get();
		if(em==null) {
			em = JPAEMFProvider.getEmf().createEntityManager();
			em.getTransaction().begin();
			locals.set(em);
		}
		return em;
	}

	public static void close() {
		EntityManager em = locals.get();
		if(em==null) {
			return;
		}
		DAOException dex = null;
		try {
			em.getTransaction().commit();
		} catch(Exception e) {
			dex = new DAOException("Unable to commit transaction.", e);
		}
		try{
			em.close();
		} catch(Exception e) {
			if(dex!=null) {
				dex = new DAOException("Unable to close entity manager", e);
			}
		}
		locals.remove();

		if(dex!=null) throw dex;
	}
}
