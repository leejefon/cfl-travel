/*
 * VISA.java
 *   - VISA implementation of CreditCard
 *
 * Date Created: 2011/10/28
 * Created By: Jeff Lee
 *
 */
package pojo.CreditCards;

import pojo.CreditCard;

/**
 *
 * @author Jeff Lee
 */
public class VISA extends CreditCard {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean serverConnect() {
		// Connect to VISA company server
		return true;
	}

	/**
	 * Validate the visa card specific rules, and calls the parent validation function
	 *
	 * @return
	 */
	@Override
	public boolean validateCardNumber() {
		if (cardNumber.startsWith("4")) {
			if (cardNumber.length() == 13 || cardNumber.length() == 16) {
				return super.validateCardNumber();
			}
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean processPayment() {
		if (!this.serverConnect()) {
			return false;
		}

		if (this.validateCardNumber()) {
			// process payment using VISA company's API
			return true;
		}

		return false;
	}
}
