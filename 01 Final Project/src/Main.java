import java.sql.*;


public class Main {

	public static void main(String[] args) throws SQLException {

		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coupons", "root", "sasha1987");
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
