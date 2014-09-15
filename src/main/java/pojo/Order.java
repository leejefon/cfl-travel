/*
 * Order.java
 *   - Includes all the information of an order
 *
 * Date Created: 2011/10/27
 * Created By: Jeff Lee
 */
package pojo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import pojo.CreditCards.AmericanExpress;
import pojo.CreditCards.MasterCard;
import pojo.CreditCards.VISA;

/**
 *
 * @author Jeff Lee
 */
public class Order {

	/**
	 * Enum for two different trip type
	 * There could actually be more types like multiple cities, etc., but that'd be too much for the assignment.
	 */
	public enum TripType {
		ONE_WAY, ROUND_TRIP
	}

	// Reference ID of the order
	public int orderId = -1;

	// Type of trip, default to round trip
	public TripType tripType = TripType.ROUND_TRIP;

	// Number of passengers
	public int numberOfPassengers = 0;

	// Departure city
	public String departureCity;

	// Destination city
	public String destinationCity;

	// Departure date
	public Date departureDate = null;

	// Return date
	public Date returnDate = null;

	// Seats in "x-y" format in a collection
	public Collection<String> seats = null;

	// Credit card type
	public String paymentType;

	// Credit card info
	public CreditCard creditCard = null;

	// Total amount = (# of Passengers * (ONE_WAY ? 1 : 2) * $100)
	public int amount;


	/**
	 *
	 * @return
	 */
	public int getOrderId() {
		return orderId;
	}

	/**
	 *
	 * @return
	 */
	public TripType getTripType() {
		return tripType;
	}

	/**
	 *
	 * @return
	 */
	public int getNumberOfPassengers() {
		return numberOfPassengers;
	}

	/**
	 *
	 * @return
	 */
	public String getDeptCity() {
		return departureCity;
	}

	/**
	 *
	 * @return
	 */
	public String getDestCity() {
		return destinationCity;
	}

	/**
	 *
	 * @return
	 */
	public Date getDeptDate() {
		return departureDate;
	}

	/**
	 *
	 * @return
	 */
	public Date getRetDate() {
		return returnDate;
	}

	/**
	 * Get the departure date in the format specified in the passed in parameter
	 *
	 * @param format
	 * @return
	 */
	public String getDeptDateFormatted(String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(departureDate);
	}

	/**
	 * Get the return date in the format specified in the passed in parameter
	 * @param format
	 * @return
	 */
	public String getRetDateFormatted(String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(returnDate);
	}

	/**
	 * Convert seats in string collection to integer array
	 *
	 * @return Seats in array
	 */
	public int[][] getSeats() {
		int [][] seats = new int[this.seats.size()][2];

		int i = 0;
		for (String seat : this.seats) {
			seats[i][0] = Integer.parseInt(seat.split("-")[0]);
			seats[i][1] = Integer.parseInt(seat.split("-")[1]);
			i++;
		}

		return seats;
	}

	/**
	 * Get the credit card type
	 *
	 * @return Full name of the credit card type
	 */
	public String getPaymentType() {
		if (paymentType.equals("visa")) {
			return "VISA";
		} else if (paymentType.equals("master")) {
			return "Master Card";
		} else if (paymentType.equals("amex")) {
			return "American Express";
		}

		return paymentType;
	}

	/**
	 * Get credit card info including card number and address, etc
	 *
	 * @return CreditCard object that encapsulate all the data.
	 */
	public CreditCard getPaymentInfo() {
		return creditCard;
	}

	/**
	 *
	 * @return
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 *
	 * @param id
	 */
	public void setOrderId(int id) {
		orderId = id;
	}

	/**
	 *
	 * @param tripType
	 */
	public void setTripType(String tripType) {
		if (tripType.equals("oneway")) {
			this.tripType = TripType.ONE_WAY;
		} else {
			this.tripType = TripType.ROUND_TRIP;
		}
	}

	/**
	 *
	 * @param num
	 */
	public void setNumberOfPassengers(int num) {
		numberOfPassengers = num;
	}

	/**
	 *
	 * @param city
	 */
	public void setDeptCity(String city) {
		departureCity = city;
	}

	/**
	 *
	 * @param city
	 */
	public void setDestCity(String city) {
		destinationCity = city;
	}

	/**
	 *
	 * @param date
	 */
	public void setDeptDate(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			departureDate = df.parse(date);
		} catch (ParseException ex) {
			System.out.println(ex);
		}
	}

	/**
	 *
	 * @param date
	 */
	public void setRetDate(String date) {
		if (date != null) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				returnDate = df.parse(date);
			} catch (ParseException ex) {
				System.out.println(ex);
			}
		}
	}

	/**
	 *
	 * @param seats
	 */
	public void setSeats(String seats) {
		String[] seatArray = seats.split(",");
		this.seats = new ArrayList<String>();
		this.seats.addAll(Arrays.asList(seatArray));
	}

	/**
	 *
	 * @param paymentInfo
	 */
	public void setPaymentInfo(Map<String, String> paymentInfo) {
		if (paymentInfo == null) {
			creditCard = null;
			return;
		}

		paymentType = paymentInfo.get("paymentType");

		if (paymentType.equals("master")) {
			creditCard = new MasterCard();
		} else if (paymentType.equals("amex")) {
			creditCard = new AmericanExpress();
		} else {
			creditCard = new VISA();
		}

		creditCard.setHolderName(paymentInfo.get("cardHolder"));
		creditCard.setCardNumber(paymentInfo.get("cardNumber"));
		creditCard.setExpiryDate(paymentInfo.get("expMonth"), paymentInfo.get("expYear"));
		creditCard.setAddress(paymentInfo.get("address"));
		creditCard.setPostcode(paymentInfo.get("postcode"));
	}

	/**
	 * Calculate the total amount to pay
	 */
	public void calculateAmount() {
		amount = this.getNumberOfPassengers() * 100 * (this.getTripType() == TripType.ONE_WAY ? 1 : 2);
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public boolean compareDeptDate(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (departureDate.equals(df.parse(date))) {
				return true;
			}
		} catch (ParseException ex) {
			System.out.println(ex);
		}
		return false;
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public boolean compareRetDate(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (returnDate.equals(df.parse(date))) {
				return true;
			}
		} catch (ParseException ex) {
			System.out.println(ex);
		}
		return false;
	}
}
