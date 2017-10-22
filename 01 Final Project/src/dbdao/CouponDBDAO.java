/**
 * This class provides access to the database and connects Coupon objects with it.
 * @author Alexandra Shapira
 */

package dbdao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import connection.ConnectionPoolSingleton;
import dao.CouponDAO;
import exceptions.ActionType;
import exceptions.DBDAOException;
import exceptions.EntryType;
import exceptions.FailedToException;
import exceptions.FailedToGetListOfCouponsException;
import exceptions.NotFoundException;
import facades.ClientType;
import javaBeans.Coupon;
import javaBeans.CouponType;

public class CouponDBDAO implements CouponDAO {

	private ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();

	/**
	 * The method converts a Coupon object into a database record. 
	 * 
	 * @param coupon
	 *            The Coupon object
	 * @throws FailedToException
	 */

	@Override
	public void createCoupon(Coupon coupon) throws FailedToException {
		String query = "INSERT INTO coupon "
				+ "(ID, TITLE, START_DATE, END_DATE, AMOUNT, TYPE, MESSAGE, PRICE, IMAGE) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection connection = pool.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, coupon.getId());
			preparedStatement.setString(2, coupon.getTitle());
			preparedStatement.setDate(3, coupon.getStartDate());
			preparedStatement.setDate(4, coupon.getEndDate());
			preparedStatement.setInt(5, coupon.getAmount());
			preparedStatement.setString(6, coupon.getType().toString());
			preparedStatement.setString(7, coupon.getMessage());
			preparedStatement.setDouble(8, coupon.getPrice());
			preparedStatement.setString(9, coupon.getImage());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.COUPON, ActionType.CREATE, coupon.getId(), e);
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * This method links the Coupon to the specific Company.
	 * 
	 * @param coupon
	 *            The Coupon object
	 * @param compId
	 *            The ID of the Company releases the Coupon
	 * @throws FailedToException 
	 */

	@Override
	public void joinCouponCompany (Coupon coupon, String compId) throws FailedToException{

		String query = "INSERT INTO company_coupon "
				+ "(COMP_ID, COUPON_ID) "
				+ "VALUES (?, ?)";
		Connection connection = pool.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, compId);
			preparedStatement.setString(2, coupon.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.COUPON, ActionType.JOIN, coupon.getId(), e);
		} finally {
			pool.returnConnection(connection);
		}

	}
	
	/**
	 * This method links the Coupon to the specific Customer.
	 * 
	 * @param coupon
	 *            The Coupon object
	 * @param compId
	 *            The ID of the Customer purchasing the Coupon
	 * @throws FailedToException 
	 */

	@Override
	public void joinCouponCustomer (Coupon coupon, String custId) throws FailedToException{

		String query = "INSERT INTO customer_coupon "
				+ "(CUST_ID, COUPON_ID) "
				+ "VALUES (?, ?)";
		Connection connection = pool.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, custId);
			preparedStatement.setString(2, coupon.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.COUPON, ActionType.JOIN, coupon.getId(), e);
		} finally {
			pool.returnConnection(connection);
		}

	}

	/**
	 * The method doesn't remove the Coupon from the database but sets the state
	 * of the Coupon and records in the join tables (customer_coupon,
	 * company_coupon) as inactive (false) in order to keep a history of
	 * actions.
	 * 
	 * @param coupon
	 *            The Coupon that should be removed
	 * 
	 * @throws NotFoundException
	 * @throws FailedToException
	 */

	@Override
	public void removeCoupon(Coupon coupon) throws NotFoundException, FailedToException {
		coupon.setActive(false);
		String query = "UPDATE coupon cp "
				+ "JOIN company_coupon cc on cp.ID = cc.COUPON_ID "
				+ "JOIN customer_coupon ccp on ccp.COUPON_ID = cp.ID "
				+ "SET cc.IS_ACTIVE = false, "
				+ "cp.IS_ACTIVE = false, "
				+ "ccp.IS_ACTIVE = false "
				+ "WHERE cp.ID = ?;";
		Connection connection = pool.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, coupon.getId());
			preparedStatement.executeUpdate();
			if (preparedStatement.getUpdateCount() == 0) {
				throw new NotFoundException(EntryType.COUPON, coupon.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.COUPON, ActionType.REMOVE, coupon.getId(), e);
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * The method updates Coupon's data in the database according to changes
	 * that were made in the Coupon object.
	 * 
	 * @param coupon
	 *            The Coupon object
	 * @throws NotFoundException
	 * @throws FailedToException
	 */

	@Override
	public void updateCoupon(Coupon coupon) throws NotFoundException, FailedToException {
		String query = "UPDATE coupon "
				+ "SET TITLE = ?, START_DATE =?, END_DATE = ?, "
				+ "AMOUNT = ?, TYPE = ?, MESSAGE = ?, PRICE = ?, IMAGE = ? "
				+ "WHERE ID = ?";
		Connection connection = pool.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, coupon.getTitle());
			preparedStatement.setDate(2, (Date) coupon.getStartDate());
			preparedStatement.setDate(3, (Date) coupon.getEndDate());
			preparedStatement.setInt(4, coupon.getAmount());
			preparedStatement.setString(5, coupon.getType().toString());
			preparedStatement.setString(6, coupon.getMessage());
			preparedStatement.setDouble(7, coupon.getPrice());
			preparedStatement.setString(8, coupon.getImage());
			preparedStatement.setString(9, coupon.getId());
			preparedStatement.executeUpdate();
			if (preparedStatement.getUpdateCount() == 0) {
				throw new NotFoundException(exceptions.EntryType.COUPON, coupon.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.COUPON, ActionType.UPDATE, coupon.getId(), e);
		} finally {
			pool.returnConnection(connection);
		}

	}

	/**
	 * This is the auxiliary method that helps to avoid code duplication. The
	 * method creates the Coupon object from the ResultSet received from the
	 * database. Before using the method you should check if the resultset is
	 * not null.
	 * 
	 * @param rs
	 *            ResultSet received from the database that includes one Coupon
	 *            record
	 * @return Coupon Coupon object
	 * @throws SQLException
	 */

	static Coupon parseDBCoupon(ResultSet rs) throws SQLException {
		Coupon coupon = new Coupon();
		String id = rs.getString("ID");
		String title = rs.getString("TITLE");
		Date startDate = rs.getDate("START_DATE");
		Date endDate = rs.getDate("END_DATE");
		int amount = rs.getInt("AMOUNT");
		String type = rs.getString("TYPE");
		String message = rs.getString("MESSAGE");
		double price = rs.getDouble("PRICE");
		String image = rs.getString("IMAGE");
		boolean isActive = rs.getBoolean("IS_ACTIVE");
		coupon = new Coupon(id, title, startDate, endDate, amount, CouponType.valueOf(type), message, price, image,
				isActive);
		return coupon;
	}

	/**
	 * The method gets the Coupon object from the database record by Coupon ID.
	 * 
	 * @param id
	 *            The Coupon id
	 * @throws NotFoundException
	 * @throws FailedToGetListOfCouponsException 
	 */

	@Override
	public Coupon getCoupon(String id) throws NotFoundException, FailedToGetListOfCouponsException {
		String query = "SELECT * FROM coupon WHERE ID = ?";
		Connection connection = pool.getConnection();
		Coupon coupon = null;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.next()) {
				throw new NotFoundException(EntryType.COUPON, id);
			}
			coupon = parseDBCoupon(rs);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToGetListOfCouponsException(e);
		} finally {
			pool.returnConnection(connection);
		}

		return coupon;

	}

	/**
	 * The method gets the list of all active coupons from the database.
	 * 
	 * @return HashSet of Coupons.
	 * @throws FailedToGetListOfCouponsException 
	 */

	@Override
	public Set<Coupon> getAllCoupons() throws FailedToGetListOfCouponsException {
		String query = "SELECT * FROM coupon WHERE IS_ACTIVE = TRUE";
		Connection connection = pool.getConnection();
		Coupon coupon = null;
		Set<Coupon> activeCoupons = new HashSet<Coupon>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				coupon = parseDBCoupon(rs);
				activeCoupons.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToGetListOfCouponsException(e);
		} finally {
			pool.returnConnection(connection);
		}
		return activeCoupons;
	}

	/**
	 * The method gets the list of all active coupons by coupon type from the
	 * database that were released by specific Company or were purchased by
	 * specific Client.
	 * 
	 * @param type
	 *            Enum CouponType
	 * @param clientType
	 *            Enum ClientType
	 * @param clientId
	 *            The ID of Company or Customer
	 * @return HashSet of Coupons.
	 * @throws FailedToGetListOfCouponsException
	 */

	@Override
	public Set<Coupon> getCouponByType(CouponType type, ClientType clientType, String clientId) throws FailedToGetListOfCouponsException {
		String query = "";
		String conditions = "WHERE c.ID = ? "
				+ "AND cp.TYPE = ? "
				+ "AND cp.IS_ACTIVE = TRUE";
		switch (clientType){
		case COMPANY: query = "SELECT * FROM company c "
				+ "INNER JOIN company_coupon cc ON c.ID = cc.COMP_ID "
				+ "INNER JOIN coupon cp ON cc.COUPON_ID = cp.ID "
				+ conditions;
		break;
		case CUSTOMER: query = "SELECT * FROM customer c "
				+ "INNER JOIN customer_coupon cc ON c.ID = cc.CUST_ID "
				+ "INNER JOIN coupon cp ON cc.COUPON_ID = cp.ID "
				+ conditions;
		break;
		default:
			break;
		}
		Connection connection = pool.getConnection();
		Coupon coupon = null;
		Set<Coupon> couponsByType = new HashSet<Coupon>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, clientId);
			preparedStatement.setString(2, type.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				coupon = parseDBCoupon(rs);
				couponsByType.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToGetListOfCouponsException(e);
		} finally {
			pool.returnConnection(connection);
		}
		return couponsByType;
	}
	
	/**
	 * The method gets the list of all active coupons under the definite price
	 * from the database that were released by specific Company or were
	 * purchased by specific Client.
	 * 
	 * @param price
	 *            The Coupon price
	 * @param clientType
	 *            Enum ClientType
	 * @param clientId
	 *            The ID of the Company or the Customer
	 * @return HashSet of Coupons
	 * @throws FailedToGetListOfCouponsException
	 */
	
	public Set<Coupon> getCouponByPrice(double price, ClientType clientType, String clientId) throws FailedToGetListOfCouponsException {
		String query = "";
		String conditions = "WHERE c.ID = ? "
				+ "AND cp.PRICE < ? "
				+ "AND cp.IS_ACTIVE = TRUE";
		switch (clientType){
		case COMPANY: query = "SELECT * FROM company c "
				+ "INNER JOIN company_coupon cc ON c.ID = cc.COMP_ID "
				+ "INNER JOIN coupon cp ON cc.COUPON_ID = cp.ID "
				+ conditions;
		break;
		case CUSTOMER: query = "SELECT * FROM customer c "
				+ "INNER JOIN customer_coupon cc ON c.ID = cc.CUST_ID "
				+ "INNER JOIN coupon cp ON cc.COUPON_ID = cp.ID "
				+ conditions;
		break;
		default:
			break;
		}
		Connection connection = pool.getConnection();
		Coupon coupon = null;
		Set<Coupon> couponsByPrice = new HashSet<Coupon>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, clientId);
			preparedStatement.setDouble(2, price);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				coupon = parseDBCoupon(rs);
				couponsByPrice.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToGetListOfCouponsException(e);
		} finally {
			pool.returnConnection(connection);
		}
		return couponsByPrice;
	}
	
	/**
	 * This method gets all the Coupons released by specific Company that will
	 * end before definite date.
	 * 
	 * @param date
	 *            The Date
	 * @param compId
	 *            The ID of the Company
	 * @return HashSet of Coupons
	 * @throws FailedToGetListOfCouponsException
	 */
	
	public Set<Coupon> getCouponByDate (Date date, String compId) throws FailedToGetListOfCouponsException{
		
		String query = "SELECT * FROM company c "
				+ "INNER JOIN company_coupon cc ON c.ID = cc.COMP_ID "
				+ "INNER JOIN coupon cp ON cc.COUPON_ID = cp.ID "
				+ "WHERE c.ID = ? "
				+ "AND cp.END_DATE < ? "
				+ "AND cp.IS_ACTIVE = TRUE "
				+ "AND cc.IS_ACTIVE = TRUE";
		Connection connection = pool.getConnection();
		Coupon coupon = null;
		Set<Coupon> couponsByType = new HashSet<Coupon>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, compId);
			preparedStatement.setDate(2, date);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				coupon = parseDBCoupon(rs);
				couponsByType.add(coupon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToGetListOfCouponsException(e);
		} finally {
			pool.returnConnection(connection);
		}
		return couponsByType;
		
	}

	/**
	 * This auxiliary method returns if the Coupon released by specific Company.
	 * 
	 * @param coupon
	 *            The Coupon object
	 * @param compId
	 *            The ID of the Company that we want to check
	 * @return boolean
	 * @throws NotFoundException
	 * @throws FailedToException
	 */

	public boolean releasedByCompany (Coupon coupon, String compId) throws NotFoundException, FailedToException{

		String query = "SELECT * FROM coupon cp "
				+ "INNER JOIN company_coupon cc on cp.ID = cc.COUPON_ID "
				+ "WHERE cp.ID = ?";
		Connection connection = pool.getConnection();
		String originalCompanyId = "";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, coupon.getId());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()){
				originalCompanyId = rs.getString("COMP_ID");
			} else {
				throw new NotFoundException(EntryType.COUPON, coupon.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.COUPON, ActionType.GET, coupon.getId());
		} finally {
			pool.returnConnection(connection);
		}
		return originalCompanyId.equals(compId);
	}

		
	/**
	 * The method checks if Customer may purchase specific Coupon.
	 * 
	 * @param coupon
	 *            The Coupon object
	 * @param custId
	 *            The ID of the Customer who wants to purchase the Coupon.
	 * @return boolean if it is allowed to purchase a Coupon.
	 * @throws FailedToException
	 */

	public boolean allowedToPurchase (Coupon coupon, String custId) throws FailedToException{
		
		String query = "SELECT * FROM customer_coupon "
				+ "WHERE CUST_ID = ? "
				+ "AND COUPON_ID = ? "
				+ "AND IS_ACTIVE = TRUE";
		Connection connection = pool.getConnection();
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, custId);
			preparedStatement.setString(2, coupon.getId());
			ResultSet rs = preparedStatement.executeQuery();
			return (!rs.next());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.COUPON, ActionType.GET, coupon.getId());
		}

	}
	
	/**
	 * The method checks if there are coupons to purchase in specific deal and
	 * if the deal is active.
	 * 
	 * @param coupon
	 *            The Coupon object
	 * @return boolean if there are coupons to purchase
	 * @throws FailedToException
	 */
	
	public boolean enoughCouponsToPurchase (Coupon coupon) throws FailedToException{
		
		String query = "SELECT * FROM coupon "
				+ "WHERE ID = ? "
				+ "AND IS_ACTIVE = TRUE "
				+ "FOR UPDATE";
		Connection connection = pool.getConnection();
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, coupon.getId());
			ResultSet rs = preparedStatement.executeQuery();
			return (rs.next() && rs.getInt("AMOUNT") > 0);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.COUPON, ActionType.GET, coupon.getId());
		}
	}

	@Override
	public void deleteExpiredCoupons(Date date) throws DBDAOException {
		String query = "UPDATE coupon c "
				+ "INNER JOIN company_coupon cc ON c.ID = cc.COUPON_ID "
				+ "SET c.IS_ACTIVE = false, "
				+ "cc.IS_ACTIVE = false "
				+ "WHERE c.END_DATE < ?";
		String query01 = "UPDATE coupon c "
				+ "LEFT JOIN customer_coupon cc ON c.ID = cc.COUPON_ID "
				+ "SET c.IS_ACTIVE = false, "
				+ "cc.IS_ACTIVE = false WHERE c.END_DATE < ?";
		Connection connection = pool.getConnection();
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setDate(1, date);
			preparedStatement.executeUpdate();
			PreparedStatement preparedStatement01 = connection.prepareStatement(query01);
			preparedStatement01.setDate(1, date);
			preparedStatement01.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBDAOException("Failed to delete expired coupon");
		}
	}
}
