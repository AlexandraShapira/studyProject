/** 
 * DBDAO Exception which is used when the record was not found in the database.
 * 
 * @author Alexandra Shapira
 */

package exceptions;

public class NotFoundException extends DBDAOException {
	
	private static final long serialVersionUID = 5704641354739043652L;
	
	public NotFoundException (EntryType entryType, String id){
		super(entryType.toString() + " with id " + id + " not found");
	}
	
}
