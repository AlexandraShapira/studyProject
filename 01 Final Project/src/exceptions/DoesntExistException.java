package exceptions;

public class DoesntExistException extends DBDAOException {
	
	private long id;
	
	public DoesntExistException (EntryType entryType, long id){
		super(entryType.toString() + " with id " + id + " not found");
	}
	
	public enum EntryType {
		COMPANY, COUPON, CUSTOMER
	}
}
