/**
 * This class was designed as JavaBean for coupons' records.
 * @author Alexandra Shapira
 */

package javaBeans;

import java.sql.Date;
import java.util.UUID;

public class Coupon {

	private String id;
	private String title;
	private Date startDate;
	private Date endDate;
	private int amount;
	private CouponType type;
	private String message;
	private double price;
	private String image;
	private boolean isActive = true;

	/**
	 * The class constructor that is used for creating a new coupon. It sets the
	 * unique id and isActive parameter as true. Although the task includes an
	 * ID parameter of the long type it was decided to use the String type for
	 * this parameter in order to simplify getting the unique value.
	 * 
	 * @param title
	 *            Coupon's title
	 * @param startDate
	 *            The date when the deal starts
	 * @param endDate
	 *            The deal's end date
	 * @param amount
	 *            Amount of coupons released by company
	 * @param type
	 *            The type of the coupon
	 * @param message
	 *            The description of the deal
	 * @param price
	 *            Coupon's price
	 * @param image
	 *            The link to the image
	 */

	public Coupon(String title, Date startDate, Date endDate, int amount, CouponType type, String message, double price,
			String image) {
		this.id = UUID.randomUUID().toString();
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.type = type;
		this.message = message;
		this.price = price;
		this.image = image;
		this.isActive = true;
	}

	/**
	 * The class constructor that is used to creating a new Coupon object from
	 * the database record, hence includes all Company's parameters.
	 * 
	 * @param id
	 *            Coupon's id
	 * @param title
	 *            Coupon's title
	 * @param startDate
	 *            The date when the deal starts
	 * @param endDate
	 *            The deal's end date
	 * @param amount
	 *            Amount of coupons released by company
	 * @param type
	 *            The type of the coupon
	 * @param message
	 *            The description of the deal
	 * @param price
	 *            Coupon's price
	 * @param image
	 *            The link to the image
	 * @param isActive
	 *            Coupon's state
	 */

	public Coupon(String id, String title, Date startDate, Date endDate, int amount, CouponType type, String message,
			double price, String image, boolean isActive) {
		this.id = id;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.type = type;
		this.message = message;
		this.price = price;
		this.image = image;
		this.isActive = isActive;
	}
	
	/**
	 * This constructor creates a Coupon object with a unique id and empty
	 * attributes.
	 */

	public Coupon() {
		this.id = UUID.randomUUID().toString();
		this.title = "";
		this.startDate = null;
		this.endDate = null;
		this.amount = 0;
		this.type = null;
		this.message = "";
		this.price = 0.0;
		this.image = "";
		this.isActive = true;
	}

	/**
	 * The method gets Coupon's id.
	 * 
	 * @return Coupon's id
	 */

	public String getId() {
		return id;
	}

	/**
	 * The method sets Coupon's id.
	 * 
	 * @param id
	 *            Coupon's id
	 */

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * The method gets Coupon's title.
	 * 
	 * @return Coupon's title
	 */

	public String getTitle() {
		return title;
	}

	/**
	 * The method sets Coupon's title.
	 * 
	 * @param title
	 *            Coupon's title
	 */

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * The method gets Coupon's start date.
	 * 
	 * @return Coupon's start date
	 */

	public Date getStartDate() {
		return startDate;
	}

	/**
	 * The method sets Coupon's start date.
	 * 
	 * @param startDate
	 *            Coupon's start date
	 */

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * The method gets Coupon's end date.
	 * 
	 * @return Coupon's end date
	 */

	public Date getEndDate() {
		return endDate;
	}

	/**
	 * The method sets Coupon's end date.
	 * 
	 * @param endDate
	 *            Coupon's end date
	 */

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * The method gets amount of Coupons released by Company.
	 * 
	 * @return amount of Coupons
	 */

	public int getAmount() {
		return amount;
	}

	/**
	 * The method sets amount of Coupons released by Company.
	 * 
	 * @param amount
	 *            amount of Coupons
	 */

	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * The method gets Enum type of Coupon.
	 * 
	 * @return CouponType
	 */

	public CouponType getType() {
		return type;
	}

	/**
	 * The method sets Enum type of Coupon.
	 * 
	 * @param type
	 *            Enum type of Coupon
	 */

	public void setType(CouponType type) {
		this.type = type;
	}

	/**
	 * The method gets description of the Coupon
	 * 
	 * @return String message
	 */

	public String getMessage() {
		return message;
	}

	/**
	 * The method sets description of the Coupon into the object.
	 * 
	 * @param message
	 *            Description
	 */

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * The method gets price of the deal.
	 * 
	 * @return price
	 */

	public double getPrice() {
		return price;
	}

	/**
	 * The method sets the price of the deal.
	 * 
	 * @param price
	 */

	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * The method gets the link to the image related to Coupon.
	 * 
	 * @return link
	 */

	public String getImage() {
		return image;
	}

	/**
	 * The method sets the link of the image.
	 * 
	 * @param image
	 *            link to the image
	 */

	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * The method gets the Coupon's state.
	 * 
	 * @return if coupon is active
	 */

	public boolean isActive() {
		return isActive;
	}

	/**
	 * The method sets the Coupon's state.
	 * 
	 * @param isActive
	 *            if Coupon is active
	 */

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * The method returns a string with all Coupon's parameters
	 */

	@Override
	public String toString() {
		return "Coupon: " + "\n" + "ID: " + id + "\n" + "Title: " + title + "\n" + "Start Date: " + startDate + "\n"
				+ "End Date: " + endDate + "\n" + "Amount: " + amount + "\n" + "Type: " + type + "\n" + "Message: "
				+ message + "\n" + "Price: " + price + "\n" + "Link to image: " + image + "\n" + "Is active: "
				+ isActive;
	}

	/**
	 * The autogenerated method that returns a hash code value for the object.
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	/**
	 * The method that indicates whether some other object is equal to this one.
	 */

	@Override
	public boolean equals(Object otherObject) {

		if (otherObject == null) {
			return false;
		}

		if (this == otherObject) {
			return true;
		}

		if (!(otherObject instanceof Coupon)) {
			return false;
		}

		Coupon otherCoupon = (Coupon) otherObject;

		return (otherCoupon.getId() == this.id && otherCoupon.getTitle().equals(this.title));

	}

}
