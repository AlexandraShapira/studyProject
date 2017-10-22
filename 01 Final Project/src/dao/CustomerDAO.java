/**
 * This interface proclaims the methods that should be used in CustomerDBDAO class.
 * @author Alexandra Shapira
 */

package dao;

import java.util.List;
import java.util.Set;

import dbdao.ParameterType;
import exceptions.DAOException;
import exceptions.FailedToException;
import exceptions.FailedToGetListOfCouponsException;
import exceptions.NotFoundException;
import javaBeans.Coupon;
import javaBeans.Customer;

public interface CustomerDAO {

	void createCustomer(Customer customer) throws FailedToException;

	void removeCustomer(Customer customer) throws NotFoundException, FailedToException;

	void updateCustomer(Customer customer) throws NotFoundException, FailedToException;

	Customer getCustomerByParameter(ParameterType parameter, String value) throws NotFoundException, FailedToException;

	List<Customer> getAllCustomers() throws DAOException;

	Set<Coupon> getCoupons(String id) throws FailedToGetListOfCouponsException;

	boolean login(String custName, String password);


}
