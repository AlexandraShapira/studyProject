/**
 * This facade Exception is used when we need to avoid access to specific function.
 * 
 * @author Alexandra Shapira
 */

package exceptions;

public class NoRightsException extends FacadeException {
	
	private static final long serialVersionUID = -4599359141495652469L;
	private static final String MESSAGE = "You have no rights to execute the operation.";
	
	public NoRightsException ()
	{
		super (MESSAGE);
	}

}
