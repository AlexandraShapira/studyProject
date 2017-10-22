/**
 * This is the superclass Exception for facade exceptions.
 * 
 * @author Alexandra Shapira
 */

package exceptions;

public class FacadeException extends CouponSystemException {

	private static final long serialVersionUID = -6001751288110830437L;

	public FacadeException(String message) {
		super(message);
	}

}
