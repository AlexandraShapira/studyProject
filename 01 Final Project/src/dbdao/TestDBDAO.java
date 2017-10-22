package dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connection.ConnectionPoolSingleton;
import javaBeans.Company;

public class TestDBDAO {
	
	public static void createCompany(Company company) {
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
			String query = "INSERT INTO company (COMP_NAME, PASSWORD, EMAIL) VALUES (?, ?, ?)";
			Connection connection = pool.getConnection();
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
//				if (company.getId() != null) {
//					preparedStatement.setLong(1, company.getId());
//				}
				preparedStatement.setString(1, company.getCompName());
				preparedStatement.setString(2, company.getPassword());
				preparedStatement.setString(3, company.getEmail());
				preparedStatement.executeUpdate();
				ResultSet rs = preparedStatement.getGeneratedKeys();
				rs.next();
				company.setId(rs.getLong(1));
			} catch (SQLException e) {
				// TODO To care about exceptions
				System.out.println(e.getMessage());
				throw new IllegalArgumentException("failed to insert record", e);
			} finally {
				pool.returnConnection(connection);
			}
	}
	
	public static void main(String[] args) throws SQLException {
		
				
//		Company c01 = new Company ("Fox", "fox123", "fox@gmail.com");
//		System.out.println(c01.toString());
//		Company c02 = new Company ("Renuar", "renuar123", "renuar@gmail.com");
//		System.out.println(c02.toString());
//		Company c03 = new Company ("FoxHome", "foxhome", "foxhome@gmail.com");
//		System.out.println(c03.toString());
		Connection connection = null;
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		
		Company c04 = new Company ("Renuar", "renuar123", "renuar@gmail.com");
		System.out.println(c04.toString());
		createCompany (c04);
		System.out.println(c04.toString());
		
		
				try {
			connection = pool.getConnection();
			// connection = (Connection) new CouponConnection();
			Statement myStatement = connection.createStatement();
			
			ResultSet myRS = myStatement.executeQuery("select * from company");
			while (myRS.next()){
				System.out.println(myRS.getString(2));
			}
						
		}  catch (Throwable t ) {
			t.printStackTrace();
		} finally {
			connection.close();
		}

							
	}
}
