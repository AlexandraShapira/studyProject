/**
 * This DBDAO Exception is used when the user can't get the list of the coupons.
 * @author Alexandra Shapira
 */

package exceptions;

public class FailedToGetListOfCouponsException extends DBDAOException {

	private static final long serialVersionUID = -1307471949081585197L;
	private static final String MESSAGE = "Failed to get list of coupons";

	public FailedToGetListOfCouponsException() {
		super(MESSAGE);
	}
	
	public FailedToGetListOfCouponsException(Throwable cause) {
		super(MESSAGE);
	}

}
