/**
 * The interface proclaims the methods for facade classes.
 * 
 * @author Alexandra Shapira
 */

package facades;

import exceptions.FailedToException;
import exceptions.NotFoundException;

public interface CouponClientFacade {

	public CouponClientFacade login (String name, String password, ClientType type) throws NotFoundException, FailedToException;

}
