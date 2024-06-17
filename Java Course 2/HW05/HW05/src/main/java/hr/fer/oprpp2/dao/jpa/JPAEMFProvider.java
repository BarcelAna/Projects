package hr.fer.oprpp2.dao.jpa;

import javax.persistence.EntityManagerFactory;

public class JPAEMFProvider {
	private static EntityManagerFactory emf;

	public static EntityManagerFactory getEmf() {
		return emf;
	}
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}
