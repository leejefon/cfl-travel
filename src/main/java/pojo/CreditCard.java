/*
 * CreditCard.java
 *   - Abstract class for credit cards
 *
 * Date Created: 2011/10/28
 * Created By: Jeff Lee
 */
package pojo;

/**
 *
 * @author Jeff Lee
 */
public abstract class CreditCard {

	// Name of the card holder
	public String cardHolder;

	// Credit card number
	public String cardNumber;

	// Credit card expiry date
	public String expiryDate;

	// Address of the card holder
	public String address;

	// Post code for the address
	public String postcode;

	/**
	 * Get the name of the card holder
	 *
	 * @return card holder name
	 */
	public String getHolderName() {
		return cardHolder;
	}

	/**
	 * Get card number
	 *
	 * @return card number
	 */
	public String getCardNumber() {
		return cardNumber.substring(0,4) + "XXXXXXXX" + cardNumber.substring(cardNumber.length() - 4);
	}

	/**
	 * Get expiry date
	 *
	 * @return expiry date
	 */
	public String getExpiryDate() {
		return expiryDate;
	}

	/**
	 * Get the address of the card holder
	 *
	 * @return address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Get post code
	 *
	 * @return postcode
	 */
	public String getPostcode() {
		return postcode;
	}

	/**
	 * Set the card holder's name
	 *
	 * @param cardHolder
	 */
	public void setHolderName(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	/**
	 * Set the card number
	 *
	 * @param cardNumber
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	 * Set the expiry date in "MM/yy" format
	 *
	 * @param Month
	 * @param Year
	 */
	public void setExpiryDate(String Month, String Year) {
		this.expiryDate = String.format("%02d", Integer.parseInt(Month)) + "/" + Year.substring(Year.length() - 2);
	}

	/**
	 * Set the address
	 *
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Set the postcode
	 *
	 * @param postcode
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	/**
	 * Validate the credit card number using the algorithm known as the LUHN Formula (mod10)
	 * Algorithm from (http://www.rgagnon.com/javadetails/java-0034.html)
	 *
	 * @return
	 */
	public boolean validateCardNumber() {
		try {
			int j = cardNumber.length();

			String[] s1 = new String[j];
			for (int i = 0; i < j; i++) {
				s1[i] = "" + cardNumber.charAt(i);
			}

			int checksum = 0;

			for (int i = j - 1; i >= 0; i -= 2) {
				int k = 0;

				if (i > 0) {
					k = Integer.valueOf(s1[i - 1]).intValue() * 2;
					if (k > 9) {
						String s = "" + k;
						k = Integer.valueOf(s.substring(0, 1)).intValue() + Integer.valueOf(s.substring(1)).intValue();
					}
					checksum += Integer.valueOf(s1[i]).intValue() + k;
				} else {
					checksum += Integer.valueOf(s1[0]).intValue();
				}
			}
			return ((checksum % 10) == 0);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Mock function to connect to the credit card service
	 *
	 * @return true if connection is successful, or otherwise false
	 */
	protected abstract boolean serverConnect();

	/**
	 * Mock function to Process the payment.
	 *
	 * @return true if payment was successful, or otherwise false
	 */
	public abstract boolean processPayment();
}