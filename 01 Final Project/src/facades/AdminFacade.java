/**
 * The AdminFacade class provides an access to the methods that are used for managing 
 * companies and customers accounts.
 * 
 * @author Alexandra Shapira
 */

package facades;

import java.util.ArrayList;
import java.util.List;

import dbdao.ParameterType;
import exceptions.NotFoundException;
import exceptions.DAOException;
import exceptions.FailedToException;
import exceptions.NoRightsException;
import javaBeans.Company;
import javaBeans.Customer;

public class AdminFacade extends CouponClientFacadeSuperClass implements CouponClientFacade {
	
	public AdminFacade() {
		super();
	}

	/**
	 * The method converts a Company object into a database record.
	 * 
	 * @throws NoRightsException
	 * @throws FailedToException 
	 */
	
	@Override
	public void createCompany(Company company) throws NoRightsException, FailedToException {

			companyDAO.createCompany(company);
		
	}

	/**
	 * The method sets the state of Company, all it's coupons and the links
	 * between Customer's and Company's Coupons and between Company and Coupon
	 * as inactive.
	 * @throws NoRightsException
	 * @throws FailedToException 
	 * @throws NotFoundException 
	 */

	@Override
	public void removeCompany(Company company) throws NoRightsException, NotFoundException, FailedToException {
		
			companyDAO.removeCompany(company);
		
	}
	
	/**
	 * The method updates all the Company's attributes except of ID and name.
	 * 
	 * @param Company
	 *            Company to update
	 * @throws NoRightsException
	 * @throws FailedToException 
	 * @throws NotFoundException 
	 */

	@Override
	public void updateCompany(Company company) throws NoRightsException, NotFoundException, FailedToException {
		Company original;
		original = companyDAO.getCompanyByParameter(ParameterType.ID, company.getId());
		original.setEmail(company.getEmail());
		original.setPassword(company.getPassword());
		companyDAO.updateCompany(original);

	}

	/**
	 * The method gets Company by its' ID.
	 * 
	 * @param id
	 *            The Company's ID
	 * @return Company Company object
	 * @throws NoRightsException
	 * @throws FailedToException 
	 * @throws NotFoundException 
	 */
	
	@Override
	public Company getCompany(String id) throws NoRightsException, NotFoundException, FailedToException {
		Company company = new Company();
		company = companyDAO.getCompanyByParameter(ParameterType.ID, id);
		return company;
	}

	/**
	 * This method gets the List of all active Companies.
	 * 
	 * @return ArrayList of Companies
	 * @throws NoRightsException
	 * @throws DAOException 
	 */
	
	@Override
	public List<Company> getAllCompanies() throws NoRightsException, DAOException {
		List <Company> companies = new ArrayList <Company>();
		companies = companyDAO.getAllCompanies();
		return companies;
	}
	
	/**
	 * This method converts a Customer object into a database record.
	 * 
	 * @param customer
	 *            The Customer object
	 * @throws NoRightsException
	 * @throws FailedToException 
	 */

	@Override
	public void createCustomer(Customer customer) throws NoRightsException, FailedToException {
			customerDAO.createCustomer(customer);
	}
	
	/**
	 * The method doesn't remove the Customer from the database but sets the
	 * state of the Customer and records of purchased coupons in the table
	 * customer_coupon as inactive (false) in order to keep a history of
	 * actions.
	 * 
	 * @param customer
	 *            The Customer object
	 * @throws NoRightsException
	 * @throws FailedToException 
	 * @throws NotFoundException 
	 */

	@Override
	public void removeCustomer(Customer customer) throws NoRightsException, NotFoundException, FailedToException {
			customerDAO.removeCustomer(customer);
	}
	
	/**
	 * The method updates Customer's data in the database according to changes
	 * that were made in the Customer object (except of ID and name).
	 * 
	 * @param customer
	 *            The Customer object
	 * @throws NoRightsException
	 * @throws FailedToException 
	 * @throws NotFoundException 
	 */

	@Override
	public void updateCustomer(Customer customer) throws NoRightsException, NotFoundException, FailedToException {
		Customer original;
			original = customerDAO.getCustomerByParameter(ParameterType.ID, customer.getId());
			original.setPassword(customer.getPassword());
			customerDAO.updateCustomer(customer);
	}

	/**
	 * This method gets Customer object from the database record.
	 * 
	 * @param id
	 *            Customer's ID
	 * @return Customer Customer object
	 * @throws NoRightsException
	 * @throws FailedToException 
	 * @throws NotFoundException 
	 */
	
	@Override
	public Customer getCustomer(String id) throws NoRightsException, NotFoundException, FailedToException {
		Customer customer = new Customer();
		customer = customerDAO.getCustomerByParameter(ParameterType.ID, id);
		return customer;
	}

	/**
	 * The method gets the list of all active companies from the database.
	 * 
	 * @return ArrayList of Companies.
	 * @throws DAOException 
	 */
	
	@Override
	public List<Customer> getAllCustomers() throws NoRightsException, DAOException {
		List <Customer> customers = new ArrayList <Customer>();
		customers = customerDAO.getAllCustomers();
		return customers;
	}

}
