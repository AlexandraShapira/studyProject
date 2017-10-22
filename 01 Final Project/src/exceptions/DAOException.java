/**
 * The superclass Exception for DAO exceptions.
 * 
 * @author Alexandra Shapira
 */

package exceptions;

public class DAOException extends CouponSystemException {

	private static final long serialVersionUID = 1493543104636069221L;

	public DAOException(String message) {
		super(message);
	}

}
