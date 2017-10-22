package facades;

import java.awt.HeadlessException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import dbdao.ParameterType;
import exceptions.FailedToException;
import exceptions.FailedToGetListOfCouponsException;
import exceptions.NoRightsException;
import exceptions.NotFoundException;
import javaBeans.Coupon;
import javaBeans.CouponType;
import javaBeans.Customer;

public class CustomerFacade extends CouponClientFacadeSuperClass implements CouponClientFacade {
	
private Customer customer;

/**
 * Class constructor. There is no ability to construct abstract
 * CustomerFacade, each facade of this type is linked to specific Customer in
 * order to forbid access to other Customers' accounts.
 * 
 * @param custName
 *            The name of the Customer
 * @throws FailedToException 
 * @throws NotFoundException 
 */

public CustomerFacade (String custName) throws NotFoundException, FailedToException{
	super();
		this.customer = customerDAO.getCustomerByParameter(ParameterType.CUST_NAME, custName);
	
}

	/**
	 * This method allow to Customer to purchase the Coupon. It checks if the
	 * Customer already bought this Coupon and if there are coupons in stock and
	 * creates a record in the database table customer_coupon.
	 * 
	 * @param coupon
	 *            Coupon to purchase
	 * @throws NoRightsException
	 * @throws HeadlessException 
	 * @throws NotFoundException 
	 * @throws FailedToException 
	 */
	
	@Override
	public void purchaseCoupon(Coupon coupon) throws NoRightsException, FailedToException, NotFoundException, HeadlessException {
			if (couponDAO.allowedToPurchase(coupon, this.customer.getId()) && 
					couponDAO.enoughCouponsToPurchase(coupon)){
				couponDAO.joinCouponCustomer(coupon, this.customer.getId());
				coupon.setAmount(coupon.getAmount() - 1);
				couponDAO.updateCoupon(coupon);
			}
			// it's not the place for this pop up
			else { if (!couponDAO.allowedToPurchase(coupon, this.customer.getId())){
				JOptionPane.showMessageDialog(null, "You've already purchased this coupon");
			} else {
				JOptionPane.showMessageDialog(null, "The coupon is out of stock");
			}
			}
		
	}

	/**
	 * This method gets all purchased coupons (active).
	 * 
	 * @return coupons HashSet of coupons
	 * @throws NoRightsException
	 * @throws FailedToGetListOfCouponsException 
	 */
	
	@Override
	public Set<Coupon> getAllPurchasedCoupons() throws NoRightsException, FailedToGetListOfCouponsException {
		Set<Coupon> coupons = new HashSet <Coupon>();
			coupons = customerDAO.getCoupons(this.customer.getId());
		return coupons;
	}
	
	/**
	 * The method gets all active coupons by type.
	 * @param type ENUM CouponType
	 * @return HashSet of coupons
	 * @throws NoRightsException
	 * @throws FailedToGetListOfCouponsException 
	 */
	
	@Override
	public Set<Coupon> getAllPurchasedCouponsByType(CouponType type) throws NoRightsException, FailedToGetListOfCouponsException {
		Set<Coupon> coupons = new HashSet <Coupon>();
			coupons = couponDAO.getCouponByType(type, ClientType.CUSTOMER, this.customer.getId());
		return coupons;
	}
	
	/**
	 * The method gets all active coupons under the definite price.
	 * @param price Price
	 * @return HashSet of coupons
	 * @throws NoRightsException
	 * @throws FailedToGetListOfCouponsException 
	 */
	
	@Override
	public Set<Coupon> getAllPurchasedCouponsByPrice(double price) throws NoRightsException, FailedToGetListOfCouponsException {
		Set<Coupon> coupons = new HashSet <Coupon>();
			coupons = couponDAO.getCouponByPrice(price, ClientType.CUSTOMER, this.customer.getId());
		return coupons;
	}

}
