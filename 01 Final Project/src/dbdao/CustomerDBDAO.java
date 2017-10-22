/**
 * This class provides access to the database and connects Customer objects with it.
 * @author Alexandra Shapira
 */

package dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import connection.ConnectionPoolSingleton;
import dao.CustomerDAO;
import exceptions.ActionType;
import exceptions.DBDAOException;
import exceptions.EntryType;
import exceptions.FailedToException;
import exceptions.FailedToGetListOfCouponsException;
import exceptions.NotFoundException;
import javaBeans.Coupon;
import javaBeans.Customer;

public class CustomerDBDAO implements CustomerDAO {

	private static ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();

	/**
	 * The method converts a Customer object into a database record.
	 * 
	 * @param customer
	 *            The Customer object
	 * @throws FailedToException
	 */

	@Override
	public void createCustomer(Customer customer) throws FailedToException {
		String query = "INSERT INTO customer "
				+ "(ID, CUST_NAME, PASSWORD) "
				+ "VALUES (?, ?, ?)";
		Connection connection = pool.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, customer.getId());
			preparedStatement.setString(2, customer.getCustName());
			preparedStatement.setString(3, customer.getPassword());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.CUSTOMER, ActionType.CREATE, customer.getId(), e);
		} finally {
			pool.returnConnection(connection);
		}

	}

	/**
	 * The method doesn't remove the Customer from the database but sets the
	 * state of the Customer and records of purchased coupons in the table
	 * customer_coupon as inactive (false) in order to keep a history of
	 * actions.
	 * 
	 * @param customer
	 *            The Customer that should be removed
	 * 
	 * @throws NotFoundException
	 * @throws FailedToException
	 */

	@Override
	public void removeCustomer(Customer customer) throws NotFoundException, FailedToException {
		String query = "UPDATE customer c "
						+ "INNER JOIN customer_coupon cc on c.ID = cc.CUST_ID "
						+ "SET cc.IS_ACTIVE = false, "
							+ "c.IS_ACTIVE = false "
						+ "WHERE c.ID = ?";
		Connection connection = pool.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, customer.getId());
			preparedStatement.executeUpdate();
			if (preparedStatement.getUpdateCount() == 0) {
				throw new NotFoundException(EntryType.CUSTOMER, customer.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.CUSTOMER, ActionType.REMOVE, customer.getId(), e);
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * The method updates Customer's data in the database according to changes
	 * that were made in the Customer object.
	 * 
	 * @param company
	 *            The Customer object
	 * @throws NotFoundException
	 * @throws FailedToException
	 * @throws FailedToUpdateException
	 */

	
	public void updateCustomer(Customer customer) throws NotFoundException, FailedToException {
		String query = "UPDATE customer "
				+ "SET CUST_NAME = ?, PASSWORD = ?, IS_ACTIVE = ? "
				+ "WHERE ID = ?";
		Connection connection = pool.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, customer.getCustName());
			preparedStatement.setString(2, customer.getPassword());
			preparedStatement.setBoolean(3, customer.isActive());
			preparedStatement.setString(4, customer.getId());
			preparedStatement.executeUpdate();
			if (preparedStatement.getUpdateCount() == 0) {
				throw new NotFoundException(exceptions.EntryType.CUSTOMER, customer.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.CUSTOMER, ActionType.UPDATE, customer.getId(), e);
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * This is the auxiliary method that helps to avoid code duplication. The
	 * method creates the Customer object from the ResultSet received from the
	 * database. Before using the method you should check if the resultset is
	 * not null.
	 * 
	 * @param rs
	 *            ResultSet received from the database that includes one
	 *            Customer record
	 * @return Customer Customer object
	 * @throws SQLException
	 * @throws FailedToGetListOfCouponsException 
	 */

	private Customer parseDBCustomer(ResultSet rs) throws SQLException, FailedToGetListOfCouponsException {
		Customer customer = new Customer();
		String id = rs.getString("ID");
		String custName = rs.getString("CUST_NAME");
		String password = rs.getString("PASSWORD");
		Set<Coupon> couponsOfCustomer = getCoupons(id);
		boolean isActive = rs.getBoolean("IS_ACTIVE");
		customer = new Customer(id, custName, password, couponsOfCustomer, isActive);
		return customer;
	}

	/**
	 * The method gets the Customer object from the database record by Customer
	 * ID or name.
	 * 
	 * @param parameter
	 *            What the method will use for searching the database record (ID
	 *            or
	 * @param value
	 *            The Customer id
	 * @throws NotFoundException
	 * @throws FailedToException
	 */

	@Override
	public Customer getCustomerByParameter(ParameterType parameter, String value) throws NotFoundException, FailedToException {
		String query = "SELECT * FROM customer WHERE "  + parameter + " = ?";
		Connection connection = pool.getConnection();
		Customer customer = null;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, value);
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.next()) {
				throw new NotFoundException(EntryType.CUSTOMER, value);
			}
			customer = parseDBCustomer(rs);

		} catch (SQLException | FailedToGetListOfCouponsException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.CUSTOMER, ActionType.GET, value, e);
		} finally {
			pool.returnConnection(connection);
		}

		return customer;

	}

	/**
	 * The method gets the list of all active customers from the database.
	 * 
	 * @return ArrayList of Customers.
	 * @throws DBDAOException 
	 */

	@Override
	public List<Customer> getAllCustomers() throws DBDAOException {
		String query = "SELECT * FROM customer WHERE IS_ACTIVE = TRUE";
		Connection connection = pool.getConnection();
		Customer customer = null;
		List<Customer> activeCustomers = new ArrayList<Customer>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				customer = parseDBCustomer(rs);
				activeCustomers.add(customer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBDAOException ("Failed to get list of customers", e);
		} finally {
			pool.returnConnection(connection);
		}
		return activeCustomers;
	}

	/**
	 * The method gets the set of coupons (active and inactive) purchased by
	 * Customer (by Customer's id).
	 * 
	 * @param id
	 *            The Customer id
	 * @return Set of Coupons
	 * @throws FailedToGetListOfCouponsException 
	 */

	@Override
	public Set<Coupon> getCoupons(String id) throws FailedToGetListOfCouponsException {
		Coupon coupon = new Coupon ();
		Set<Coupon> couponsOfCustomer = new HashSet<Coupon>();
		String query = "SELECT * FROM customer_coupon cc " 
						+ "INNER JOIN coupon cp ON cc.COUPON_ID = cp.ID "
						+ "WHERE cc.CUST_ID = ? AND cc.IS_ACTIVE=true";
		Connection connection = pool.getConnection();
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				coupon = CouponDBDAO.parseDBCoupon (rs);
				couponsOfCustomer.add(coupon);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToGetListOfCouponsException (e);
		} finally {
			pool.returnConnection(connection);
		}
		return couponsOfCustomer;

	}

	/**
	 * The method compares received parameters with the database and return
	 * boolean if these parameters are suitable.
	 * 
	 * @param custName
	 *            Customer's name
	 * @param password
	 *            The password
	 * @return boolean
	 * @throws DBDAOException 
	 */

	@Override
	public boolean login(String custName, String password) {
		String query = "SELECT * FROM customer "
				+ "WHERE CUST_NAME = ? AND PASSWORD = ?";
		Connection connection = pool.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, custName);
			preparedStatement.setString(2, password);
			ResultSet rs = preparedStatement.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			pool.returnConnection(connection);
		}
	}

}
