/**
 * ConnectionPoolSingleton is a singleton class which allows to establish connection between MySQL database and the 
 * CouponSystem application. 
 * @author Alexandra Shapira
 */

package connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class ConnectionPoolSingleton {

	private int maxConnections;
	private List<Connection> connections = new ArrayList<Connection>(maxConnections);
	private String connectionString;
	private String userName;
	private String password;
	private List<Connection> givenConnections = new ArrayList<Connection>(maxConnections);

	/**
	 * The inner class ConnectionPoolSingletonHolder was designed as a part of
	 * getInstance() method in order to allow multithread work.
	 *
	 */

	private static class ConnectionPoolSingletonHolder {
		private static final ConnectionPoolSingleton INSTANCE = new ConnectionPoolSingleton();
	}

	public static ConnectionPoolSingleton getInstance() {
		return ConnectionPoolSingletonHolder.INSTANCE;
	}

	/**
	 * Class constructor.
	 */

	private ConnectionPoolSingleton() {
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("db.properties");
			Properties props = new Properties();
			props.load(is);
			maxConnections = Integer.parseInt(props.getProperty("maxConnection", "10"));
			connectionString = props.getProperty("connectionUrl");
			userName = props.getProperty("userName");
			password = props.getProperty("password");
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		for (int i = 0; i < maxConnections; i++) {
			try {
				connections.add(DriverManager.getConnection(connectionString, userName, password));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * The method gives the connection from the pool of connections and adds it
	 * to the givenConnections list.
	 * 
	 * @return Connection from the pool of Connections.
	 */

	public synchronized Connection getConnection() {
		while (connections.size() == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		Connection givenConnection = connections.remove(0);
		givenConnections.add(givenConnection);
		return givenConnection;
	}

	/**
	 * The method returns given connection to the pool.
	 * 
	 * @param returnedConnection
	 *            is the Connection that must be returned.
	 */

	public synchronized void returnConnection(Connection returnedConnection) {
		boolean removed = givenConnections.remove(returnedConnection);
		if (removed) {
			connections.add(returnedConnection);
		}
		this.notifyAll();
	}

	/**
	 * The method closes all connections.
	 */

	public void closeAllConnections() {
		for (Connection connection : connections) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		for (Connection connection : givenConnections) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
}
