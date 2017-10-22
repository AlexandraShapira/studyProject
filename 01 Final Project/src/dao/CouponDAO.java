/**
 * This interface proclaims the methods that should be used in CouponDBDAO class.
 * @author Alexandra Shapira
 */

package dao;

import java.sql.Date;
import java.util.Set;

import exceptions.DAOException;
import exceptions.FailedToException;
import exceptions.FailedToGetListOfCouponsException;
import exceptions.NotFoundException;
import facades.ClientType;
import javaBeans.Coupon;
import javaBeans.CouponType;

public interface CouponDAO {

	void createCoupon(Coupon coupon) throws FailedToException;

	void removeCoupon(Coupon coupon) throws NotFoundException, FailedToException;

	void updateCoupon(Coupon coupon) throws NotFoundException, FailedToException;

	Coupon getCoupon(String id) throws NotFoundException, FailedToGetListOfCouponsException;

	Set<Coupon> getAllCoupons() throws FailedToGetListOfCouponsException;

	Set<Coupon> getCouponByType(CouponType type, ClientType clientType, String clientId) throws FailedToGetListOfCouponsException;

	Set<Coupon> getCouponByPrice(double price, ClientType clientType, String clientId) throws FailedToGetListOfCouponsException;
	
	Set<Coupon> getCouponByDate (Date date, String compName) throws FailedToGetListOfCouponsException;
	
	void joinCouponCompany(Coupon coupon, String id) throws FailedToException;
	
	void joinCouponCustomer (Coupon coupon, String custId) throws FailedToException;

	boolean releasedByCompany(Coupon coupon, String id) throws NotFoundException, FailedToException;

	public boolean enoughCouponsToPurchase (Coupon coupon) throws FailedToException;
	
	public boolean allowedToPurchase (Coupon coupon, String custId) throws FailedToException;
	
	public void deleteExpiredCoupons (Date date) throws DAOException;
	
}
