/**
 * The superclass Exception for DBDAO Exceptions.
 * 
 * @author Alexandra Shapira
 */

package exceptions;

public class DBDAOException extends DAOException {

	private static final long serialVersionUID = -179649425351255097L;

	public DBDAOException(String message) {
		super(message);
	}

	public DBDAOException(String message, Throwable cause) {
		super(message);
	}

}
