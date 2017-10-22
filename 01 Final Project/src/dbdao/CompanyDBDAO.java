/**
 * This class provides access to the database and connects Company objects with it.
 * @author Alexandra Shapira
 */

package dbdao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.*;
import connection.ConnectionPoolSingleton;
import dao.CompanyDAO;
import exceptions.ActionType;
import exceptions.DBDAOException;
import exceptions.EntryType;
import exceptions.FailedToException;
import exceptions.FailedToGetListOfCouponsException;
import exceptions.NotFoundException;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.CouponType;

public class CompanyDBDAO implements CompanyDAO {

	private static ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();

	/**
	 * The method converts a Company object into a database record. The
	 * uniqueness of the companyName is checking on the database level.
	 * 
	 * @param company
	 *            The Company object
	 * @throws FailedToException
	 */

	@Override
	public void createCompany(Company company) throws FailedToException {
		String query = "INSERT INTO company "
				+ "(ID, COMP_NAME, PASSWORD, EMAIL) "
				+ "VALUES (?, ?, ?, ?)";
		Connection connection = pool.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, company.getId());
			preparedStatement.setString(2, company.getCompName());
			preparedStatement.setString(3, company.getPassword());
			preparedStatement.setString(4, company.getEmail());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.COMPANY, ActionType.CREATE, company.getId(), e);
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * The method updates Company's data in the database according to changes
	 * that were made in the Company object.
	 * 
	 * @param company
	 *            The Company object
	 * @throws NotFoundException
	 * @throws FailedToException
	 */

	@Override
	public void updateCompany(Company company) throws NotFoundException, FailedToException {
		String query = "UPDATE company "
				+ "SET COMP_NAME = ?, PASSWORD = ?, EMAIL = ?, IS_ACTIVE = ? "
				+ "WHERE ID = ?";
		Connection connection = pool.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, company.getCompName());
			preparedStatement.setString(2, company.getPassword());
			preparedStatement.setString(3, company.getEmail());
			preparedStatement.setBoolean(4, company.isActive());
			preparedStatement.setString(5, company.getId());
			preparedStatement.executeUpdate();
			if (preparedStatement.getUpdateCount() == 0) {
				throw new NotFoundException(exceptions.EntryType.COMPANY, company.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.COMPANY, ActionType.UPDATE, company.getId(), e);
		} finally {
			pool.returnConnection(connection);
		}
	}

	/**
	 * The method doesn't remove the Company from the database but sets the
	 * state of the Company, all Coupons released by it and all records in the
	 * tables company_coupon and customer_coupon as inactive (false) in order
	 * to keep a history of actions.
	 * 
	 * @param company
	 *            The Company that should be removed
	 * 
	 * @throws NotFoundException
	 * @throws FailedToException
	 */

	@Override
	public void removeCompany(Company company) throws NotFoundException, FailedToException {
		String query = "UPDATE company cm "
				+ "INNER JOIN company_coupon cc on cm.ID = cc.COMP_ID "
				+ "INNER JOIN coupon cp ON cp.ID = cc.COUPON_ID "
				+ "INNER JOIN customer_coupon ccp on ccp.COUPON_ID = cp.ID "
				+ "SET cc.IS_ACTIVE = false, "
					+ "cm.IS_ACTIVE = false, "
					+ "cp.IS_ACTIVE = false, "
					+ "ccp.IS_ACTIVE = false "
				+ "WHERE cm.ID = ?";
		Connection connection = pool.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, company.getId());
			preparedStatement.executeUpdate();
			if (preparedStatement.getUpdateCount() == 0) {
				throw new NotFoundException(EntryType.COMPANY, company.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.COMPANY, ActionType.REMOVE, company.getId(), e);
		} finally {
			pool.returnConnection(connection);
		}

	}

	/**
	 * This is the auxiliary method that helps to avoid code duplication. The
	 * method creates the Company object from the ResultSet received from the
	 * database. Before using the method you should check if the resultset is
	 * not null.
	 * 
	 * @param rs
	 *            ResultSet received from the database that includes one Company
	 *            record
	 * @return Company Company object
	 * @throws SQLException
	 * @throws DBDAOException 
	 */

	private Company parseDBCompany(ResultSet rs) throws SQLException, DBDAOException {
		Company company = new Company();
		String id = rs.getString("ID");
		String compName = rs.getString("COMP_NAME");
		String password = rs.getString("PASSWORD");
		String email = rs.getString("EMAIL");
		Set<Coupon> couponsOfCompany = getCoupons(id);
		boolean isActive = rs.getBoolean("IS_ACTIVE");
		company = new Company(id, compName, password, email, couponsOfCompany, isActive);
		return company;
	}

	/**
	 * The method gets the Company object from the database record by Company
	 * ID or Company Name.
	 * 
	 * @param parameter
	 *            Enum type of parameter (ID or COMPNAME)
	 * @param value
	 *            The value of the parameter
	 * 
	 * @throws NotFoundException
	 * @throws FailedToException
	 */

	@Override
	public Company getCompanyByParameter(ParameterType parameter, String value)
			throws NotFoundException, FailedToException {
		String query = "SELECT * FROM company WHERE " + parameter + "= ?";
		Connection connection = pool.getConnection();
		Company company = new Company();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, value);
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.next()) {
				throw new NotFoundException(EntryType.COMPANY, value);
			}
			company = parseDBCompany(rs);

		} catch (SQLException | DBDAOException e) {
			e.printStackTrace();
			throw new FailedToException(EntryType.COMPANY, ActionType.GET, parameter, value, e);
		} finally {
			pool.returnConnection(connection);
		}

		return company;

	}

	/**
	 * The method gets the list of all active companies from the database.
	 * 
	 * @return ArrayList of Companies.
	 * @throws DBDAOException 
	 */

	@Override
	public List<Company> getAllCompanies() throws DBDAOException {
		String query = "SELECT * FROM company WHERE IS_ACTIVE = TRUE";
		Connection connection = pool.getConnection();
		Company company = null;
		List<Company> activeCompanies = new ArrayList<Company>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				company = parseDBCompany(rs);
				activeCompanies.add(company);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBDAOException ("Failed to get list of companies", e);
		} finally {
			pool.returnConnection(connection);
		}
		return activeCompanies;
	}

	/**
	 * The method gets the set of active coupons released by Company (by Company's id).
	 * 
	 * @param id
	 *            The Company id
	 * @return Set of Coupons
	 * @throws FailedToGetListOfCouponsException 
	 */

	@Override
	public Set<Coupon> getCoupons(String id) throws FailedToGetListOfCouponsException {
		Set<Coupon> couponsOfCompany = new HashSet<Coupon>();
		String query = "SELECT * FROM company_coupon "
						+ "INNER JOIN coupon ON company_coupon.COUPON_ID = coupon.ID "
						+ "WHERE COMP_ID = ? AND coupon.IS_ACTIVE=true";
		Connection connection = pool.getConnection();
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String couponId = rs.getString("ID");
				String title = rs.getString("TITLE");
				Date startDate = rs.getDate("START_DATE");
				Date endDate = rs.getDate("END_DATE");
				int amount = rs.getInt("AMOUNT");
				String type = rs.getString("TYPE");
				String message = rs.getString("MESSAGE");
				double price = rs.getDouble("PRICE");
				String image = rs.getString("IMAGE");
				boolean isActive = rs.getBoolean("IS_ACTIVE");

				couponsOfCompany.add(new Coupon(couponId, title, startDate, endDate, amount, CouponType.valueOf(type),
						message, price, image, isActive));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FailedToGetListOfCouponsException (e);
		} finally {
			pool.returnConnection(connection);
		}
		return couponsOfCompany;
	}

	/**
	 * The method compares received parameters with the database and return
	 * boolean if these parameters are suitable.
	 * 
	 * @param compName
	 *            The name of Company
	 * @param password
	 *            The password
	 * @return boolean
	 */

	@Override
	public boolean login(String compName, String password) {
		String query = "SELECT * FROM company WHERE COMP_NAME = ? AND PASSWORD = ?";
		Connection connection = pool.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, compName);
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