/**
 * This class describes the DailyCouponExpiration task that runs once a 24 hours and 
 * removes all the coupons with the previous days as end day.  
 * @author Alexandra Shapira
 */

package main;

import dao.CouponDAO;
import dbdao.CouponDBDAO;
import exceptions.DAOException;

public class DailyCouponExpirationTask implements Runnable {
	
	private CouponDAO couponDAO = new CouponDBDAO();
	private boolean quit = false;

	@Override
	public void run() {
		if (!quit) {
			java.util.Date utilDate = new java.util.Date();
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			try {
				couponDAO.deleteExpiredCoupons(sqlDate);
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopTask (){
		
		quit = true;
	}

}
