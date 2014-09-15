/*
 * AmericanExpress.java
 *   - AMEX implementation of CreditCard
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
public class AmericanExpress extends CreditCard {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean serverConnect() {
		// Connect to AMEX company server
		return true;
	}

	/**
	 * Validate the American express specific rules, and calls the parent validation function
	 *
	 * @return
	 */
	@Override
	public boolean validateCardNumber() {
		if (cardNumber.startsWith("34") || cardNumber.startsWith("37")) {
			if (cardNumber.length() == 15) {
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
			// process payment using American Express company's API
			return true;
		}

		return false;
	}
}