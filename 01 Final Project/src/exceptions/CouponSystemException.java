/** 
 * The superclass Exception for all system exceptions.
 * 
 * @author Alexandra Shapira
 */

package exceptions;

public class CouponSystemException extends Exception {

	private static final long serialVersionUID = -5300117368545878L;

	public CouponSystemException(String message) {
		super(message);
	}

}
