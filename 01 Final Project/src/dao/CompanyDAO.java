/**
 * This interface proclaims the methods that should be used in CompanyDBDAO class.
 * @author Alexandra Shapira
 */

package dao;

import java.util.List;
import java.util.Set;

import dbdao.ParameterType;
import exceptions.NotFoundException;
import exceptions.DAOException;
import exceptions.FailedToException;
import exceptions.FailedToGetListOfCouponsException;
import javaBeans.Company;
import javaBeans.Coupon;

public interface CompanyDAO {

	void createCompany(Company company) throws FailedToException;

	void removeCompany(Company company) throws NotFoundException, FailedToException;

	void updateCompany(Company company) throws NotFoundException, FailedToException;

	Company getCompanyByParameter(ParameterType parameter, String value) throws NotFoundException, FailedToException;

	List<Company> getAllCompanies() throws DAOException;

	Set<Coupon> getCoupons(String id) throws NotFoundException, FailedToGetListOfCouponsException;

	boolean login(String compName, String password);

}
