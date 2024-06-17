package hr.fer.oprpp2.dao;

import hr.fer.oprpp2.dao.jpa.JPADAOImp;

public class DAOProvider {
	private static DAO dao = new JPADAOImp();

	public static DAO getDAO() {
		return dao;
	}
}
