package hr.fer.oprpp1.hw04.db;

/**
 * Class FieldValueGetters offers implementations of IFieldValueGetter strategies for each StudentRecord field: JMBAG, first name or last name.
 * @author anace
 *
 */
public class FieldValueGetters {
	public static final IFieldValueGetter FIRST_NAME = (record) -> record.getFirstName();
	public static final IFieldValueGetter LAST_NAME = (record) -> record.getLastName();
	public static final IFieldValueGetter JMBAG = (record) -> record.getJmbag();
}
