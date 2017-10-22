/**
 * This is singleton class providing the methods for the opening and closing the whole system.
 * @author Alexandra Shapira
 */

package main;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import connection.ConnectionPoolSingleton;
import dao.CompanyDAO;
import dao.CouponDAO;
import dao.CustomerDAO;
import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import dbdao.CustomerDBDAO;
import exceptions.FailedToException;
import exceptions.NotFoundException;
import facades.ClientType;
import facades.CouponClientFacade;
import facades.CouponClientFacadeSuperClass;

public class CouponSystemSingleton {

	private CouponClientFacade facadeSuperClass = new CouponClientFacadeSuperClass();
	private CompanyDAO companyDAO;
	private CustomerDAO customerDAO;
	private CouponDAO couponDAO;
	private DailyCouponExpirationTask task;
	private ScheduledExecutorService scheduledExecutor;
	private ConnectionPoolSingleton pool;

	/**
	 * The inner class CouponSystemSingletonHolder was designed as a part of
	 * getInstance() method in order to allow multithread work.
	 *
	 */

	private static class CouponSystemSingletonHolder {
		private static final CouponSystemSingleton INSTANCE = new CouponSystemSingleton();
	}

	public static CouponSystemSingleton getInstance() {
		return CouponSystemSingletonHolder.INSTANCE;
	}

	/**
	 * Class constructor that initializes DAO layer and launches the
	 * DailyCouponExpirationTask.
	 */

	private CouponSystemSingleton() {
		pool = ConnectionPoolSingleton.getInstance();
		companyDAO = new CompanyDBDAO();
		customerDAO = new CustomerDBDAO();
		couponDAO = new CouponDBDAO();
		task = new DailyCouponExpirationTask();
		scheduledExecutor = new ScheduledThreadPoolExecutor(1);
		scheduledExecutor.scheduleAtFixedRate(task, 1, 24, TimeUnit.HOURS);
	}
	
	/**
	 * The method allows to access to the specific facade by parameters.
	 * 
	 * @param name
	 *            The username (Company's or Customer's name)
	 * @param password
	 *            Password
	 * @param type
	 *            Enum ClientType (ADMIN, COMPANY or CUSTOMER)
	 * @return CouponClientFacade interface
	 * @throws FailedToException 
	 * @throws NotFoundException 
	 */

	public CouponClientFacade login(String name, String password, ClientType type) throws NotFoundException, FailedToException {
		CouponClientFacade cf = facadeSuperClass.login(name, password, type);
		return cf;
	}
	
	/**
	 * This method is used for closing the system.
	 */

	public void shutdown() {
		scheduledExecutor.shutdown();
		pool.closeAllConnections();

	}

}
