/**
 * DBDAO Exception
 * 
 * @author Alexandra Shapira
 */

package exceptions;

import dbdao.ParameterType;

public class FailedToException extends DBDAOException {

	private static final long serialVersionUID = 5002425191564279481L;
	
	public FailedToException(EntryType entryType, ActionType type, String id) {
		super("Failed to " + type.toString().toLowerCase() + " the " + entryType.toString().toLowerCase() + " with id "
				+ id);
	}
	
	public FailedToException(EntryType entryType, ActionType type, String id, Throwable cause) {
		super("Failed to " + type.toString().toLowerCase() + " the " + entryType.toString().toLowerCase() + " with id "
				+ id);
	}

	public FailedToException(EntryType entryType, ActionType type, ParameterType parameterType, String value, Throwable cause) {
		super("Failed to " + type.toString().toLowerCase() + " the " + entryType.toString().toLowerCase() + " with " + parameterType 
				+ value);

	}
}