/**
 * This is the facade superclass that includes all the methods that are used 
 * in all type of facades. On this stage all these methods (except login and 
 * some auxiliary methods) throw NoRightsException. The methods that are used 
 * in specific facades are overrided in proper facade.
 * 
 * @author Alexandra Shapira
 */

package facades;

import java.awt.HeadlessException;
import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import dao.CompanyDAO;
import dao.CouponDAO;
import dao.CustomerDAO;
import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import dbdao.CustomerDBDAO;
import exceptions.DAOException;
import exceptions.FailedToException;
import exceptions.FailedToGetListOfCouponsException;
import exceptions.NoRightsException;
import exceptions.NotFoundException;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.CouponType;
import javaBeans.Customer;

public class CouponClientFacadeSuperClass implements CouponClientFacade {

	protected CompanyDAO companyDAO;
	protected CustomerDAO customerDAO;
	protected CouponDAO couponDAO;
	
	/**
	 * The class constructor that initiates DAO layer.
	 */

	public CouponClientFacadeSuperClass() {

		companyDAO = new CompanyDBDAO();
		customerDAO = new CustomerDBDAO();
		couponDAO = new CouponDBDAO();
	}

	public void createCompany(Company company) throws NoRightsException, FailedToException {
		throw new NoRightsException();
	}

	public void removeCompany(Company company) throws NoRightsException, NotFoundException, FailedToException {
		throw new NoRightsException();
	}

	public void updateCompany(Company company) throws NoRightsException, NotFoundException, FailedToException {
		throw new NoRightsException();
	}

	public Company getCompany(String id) throws NoRightsException, NotFoundException, FailedToException {
		throw new NoRightsException();
	}
	
	public Company getCompany() throws NoRightsException, NotFoundException, FailedToException {
		throw new NoRightsException();
	}

	public List<Company> getAllCompanies() throws NoRightsException, DAOException {
		throw new NoRightsException();
	}

	public void createCustomer(Customer customer) throws NoRightsException, FailedToException {
		throw new NoRightsException();
	}

	public void removeCustomer(Customer customer) throws NoRightsException, NotFoundException, FailedToException {
		throw new NoRightsException();
	}

	public void updateCustomer(Customer customer) throws NoRightsException, NotFoundException, FailedToException {
		throw new NoRightsException();
	}

	public Customer getCustomer(String id) throws NoRightsException, NotFoundException, FailedToException {
		throw new NoRightsException();
	}

	public List<Customer> getAllCustomers() throws NoRightsException, DAOException {
		throw new NoRightsException();
	}

	public void createCoupon(Coupon coupon) throws NoRightsException, FailedToException {
		throw new NoRightsException();
	}

	public void removeCoupon(Coupon coupon) throws NoRightsException, NotFoundException, FailedToException {
		throw new NoRightsException();
	}

	public void updateCoupon(Coupon coupon) throws NoRightsException, NotFoundException, FailedToException, FailedToGetListOfCouponsException {
		throw new NoRightsException();
	}

	public Coupon getCoupon(String id) throws NoRightsException, NotFoundException, FailedToGetListOfCouponsException {
		throw new NoRightsException();
	}

	public Set<Coupon> getAllCoupons() throws NoRightsException, NotFoundException, FailedToGetListOfCouponsException {
		throw new NoRightsException();
	}

	public Set<Coupon> getCouponsByType(CouponType type) throws NoRightsException, FailedToGetListOfCouponsException {
		throw new NoRightsException();
	}
	
	public Set<Coupon> getCouponsByPrice(double price) throws NoRightsException, FailedToGetListOfCouponsException{
		throw new NoRightsException();
	}
	
	public Set<Coupon> getCouponsByDate(Date date) throws NoRightsException, FailedToGetListOfCouponsException{
		throw new NoRightsException();
	}

	public void purchaseCoupon(Coupon coupon) throws NoRightsException, FailedToException, NotFoundException, HeadlessException {
		throw new NoRightsException();
	}

	public Set<Coupon> getAllPurchasedCoupons() throws NoRightsException, FailedToGetListOfCouponsException {
		throw new NoRightsException();
	}

	public Set<Coupon> getAllPurchasedCouponsByType(CouponType type) throws NoRightsException, FailedToGetListOfCouponsException {
		throw new NoRightsException();
	}

	public Set<Coupon> getAllPurchasedCouponsByPrice(double price) throws NoRightsException, FailedToGetListOfCouponsException {
		throw new NoRightsException();
	}
	
	/**
	 * The method receive username, password and usertype and initiates the
	 * proper facade.
	 * 
	 * @param name
	 *            Username
	 * @param password
	 *            Password
	 * @param type
	 *            The type of the client
	 * @return proper type of facade
	 * @throws FailedToException 
	 * @throws NotFoundException 
	 */

	public CouponClientFacadeSuperClass login(String name, String password, ClientType type) throws NotFoundException, FailedToException {
		CouponClientFacadeSuperClass cf = new CouponClientFacadeSuperClass();
		switch (type) {
		case ADMIN:
			if (name.equals("admin") && password.equals("1234"))
				cf = new AdminFacade();
			break;
		case COMPANY:
			if (companyDAO.login(name, password)){
					cf = new CompanyFacade(name);
					} else {
						JOptionPane.showMessageDialog(null, "The username or the password is wrong");
					}
			break;
		case CUSTOMER:
			if (customerDAO.login(name, password)){
				cf = new CustomerFacade(name);
			}else {
				JOptionPane.showMessageDialog(null, "The username or the password is wrong");
			}
			break;
		}
		return cf;
	}
	
	/**
	 * This is an auxiliary method that helps to avoid code duplicating in
	 * handling by exception in facades.
	 * 
	 * @param e
	 *            Exception that was caught
	 */

	protected static void facadeHandleException(Exception e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(null, e.getMessage());
	}

}
