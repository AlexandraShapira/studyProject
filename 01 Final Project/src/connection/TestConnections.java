package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestConnections {

	public static void main(String[] args) throws SQLException {
		Connection connection = null;
		ConnectionPoolSingleton pool = ConnectionPoolSingleton.getInstance();
		try {
			connection = pool.getConnection();
			// connection = (Connection) new CouponConnection();
			Statement myStatement = connection.createStatement();
			ResultSet myRS = myStatement.executeQuery("select * from company");
			while (myRS.next()){
				System.out.println(myRS.getString(2));
			}
			pool.returnConnection(connection);
			
		}  catch (Throwable t ) {
			t.printStackTrace();
		} finally {
			pool.closeAllConnections();
		}

	}

}
