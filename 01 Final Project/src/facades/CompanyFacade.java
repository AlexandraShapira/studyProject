/**
 * The CompanyFacade class provides an access to the methods that are used by Company 
 * for managing its account.
 * 
 * @author Alexandra Shapira
 */

package facades;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import dbdao.ParameterType;
import exceptions.FailedToException;
import exceptions.FailedToGetListOfCouponsException;
import exceptions.NoRightsException;
import exceptions.NotFoundException;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.CouponType;

public class CompanyFacade extends CouponClientFacadeSuperClass implements CouponClientFacade {
	
	private Company company;
	
	/**
	 * Class constructor. There is no ability to construct abstract
	 * CompanyFacade, each facade of this type is linked to specific Company in
	 * order to forbid access to other Companies' accounts.
	 * 
	 * @param compName
	 *            The name of the Company
	 * @throws FailedToException 
	 * @throws NotFoundException 
	 */
	
	public CompanyFacade (String compName) throws NotFoundException, FailedToException{
		super();
		this.company = companyDAO.getCompanyByParameter(ParameterType.COMP_NAME, compName);
		
	}
	
	/**
	 * The method converts a Coupon object into a database record and links it
	 * to the specific Company.
	 * 
	 * @param coupon
	 *            The Coupon that Company releases
	 * @throws NoRightsException
	 * @throws FailedToException 
	 */
	
	@Override
	public void createCoupon(Coupon coupon) throws NoRightsException, FailedToException {
			couponDAO.createCoupon(coupon);
			couponDAO.joinCouponCompany(coupon, this.company.getId());
		
	}
	
	/**
	 * This method checks if the Coupon released by the Company that manages the
	 * account and removes the Coupon if the Coupon belongs to the Company.
	 * @param coupon The Coupon object
	 * @throws NoRightsException
	 * @throws FailedToException 
	 * @throws NotFoundException 
	 */

	@Override
	public void removeCoupon(Coupon coupon) throws NoRightsException, NotFoundException, FailedToException {

		if (couponDAO.releasedByCompany(coupon, this.company.getId())) {
			couponDAO.removeCoupon(coupon);
		} else {
			throw new NoRightsException();
		}

	}
	
	/**
	 * This method checks if the Coupon released by the Company that manages the
	 * account and updates the end date and the price of the Coupon if the
	 * Coupon belongs to the Company.
	 * 
	 * @param coupon
	 *            The Coupon object
	 * @throws NoRightsException
	 * @throws FailedToGetListOfCouponsException 
	 * @throws FailedToException 
	 * @throws NotFoundException 
	 */
	
	@Override
	public void updateCoupon(Coupon coupon) throws NoRightsException, NotFoundException, FailedToException, FailedToGetListOfCouponsException {
		Coupon original;
			if (couponDAO.releasedByCompany (coupon, this.company.getId())){
				original = couponDAO.getCoupon(coupon.getId());
				original.setEndDate(coupon.getEndDate());
				original.setPrice(coupon.getPrice());
				couponDAO.updateCoupon(original);
			}
			else {
				throw new NoRightsException();
			}
		
	}
	
	/**
	 * This method gets the Company object of the Company that manages the
	 * account.
	 * 
	 * @return Company Company object
	 * @throws FailedToException 
	 * @throws NotFoundException 
	 */
	
	@Override
	public Company getCompany() throws NotFoundException, FailedToException {
		Company company = new Company();
		company = companyDAO.getCompanyByParameter(ParameterType.ID, this.company.getId());
		return company;
	}
	
	/**
	 * This method gets any Coupon that was released by any Company by its' ID
	 * (for reading only).
	 * 
	 * @param id
	 *            The Coupon's id
	 * @return Coupon The Coupon object
	 * @throws NoRightsException
	 * @throws FailedToGetListOfCouponsException 
	 * @throws NotFoundException 
	 */
	
	
	@Override
	public Coupon getCoupon(String id) throws NoRightsException, NotFoundException, FailedToGetListOfCouponsException {
		Coupon coupon = new Coupon();
			coupon = couponDAO.getCoupon(id);
		return coupon;
	}
	
	/**
	 * This method gets the set of all active coupons released by the Company.
	 * 
	 * @return coupons HashSet of Coupons
	 * @throws NoRightsException
	 * @throws FailedToGetListOfCouponsException 
	 * @throws NotFoundException 
	 */
	
	@Override
	public Set<Coupon> getAllCoupons() throws NoRightsException, NotFoundException, FailedToGetListOfCouponsException {
		Set<Coupon> coupons = new HashSet<Coupon>();
			coupons = companyDAO.getCoupons(this.company.getId());
		return coupons;
	}
	
	/**
	 * This method gets all Coupons released by Company by Coupon type.
	 * 
	 * @param type
	 *            Enum CouponType
	 * @return HashSet of Coupons
	 * @throws NoRightsException
	 * @throws FailedToGetListOfCouponsException 
	 */
	
	@Override
	public Set<Coupon> getCouponsByType(CouponType type) throws NoRightsException, FailedToGetListOfCouponsException {
		Set <Coupon> coupons = new HashSet <Coupon>();
			coupons = couponDAO.getCouponByType(type, ClientType.COMPANY, this.company.getId());
		return coupons;
	}
	
	/**
	 * This method gets all Coupons released by Company under the definite
	 * price.
	 * 
	 * @param price
	 *            The price
	 * @return HashSet of Coupons
	 * @throws NoRightsException
	 * @throws FailedToGetListOfCouponsException 
	 */
	
	@Override
	public Set<Coupon> getCouponsByPrice(double price) throws NoRightsException, FailedToGetListOfCouponsException {
		Set <Coupon> coupons = new HashSet <Coupon>();
			coupons = couponDAO.getCouponByPrice(price, ClientType.COMPANY, this.company.getId());
		return coupons;
	}
	
	/**
	 *This method gets all the Coupons released by specific Company that will
	 * end before definite date.
	 * 
	 * @param date
	 *            The date
	 * @return HashSet of Coupons
	 * @throws NoRightsException
	 * @throws FailedToGetListOfCouponsException 
	 */
	
	@Override
	public Set<Coupon> getCouponsByDate(Date date) throws NoRightsException, FailedToGetListOfCouponsException{
		Set <Coupon> coupons = new HashSet <Coupon>();
			coupons = couponDAO.getCouponByDate(date, this.company.getId());
		return coupons;
	}

}
