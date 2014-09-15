/*
 * MasterCard.java
 *   - Master Card implementation of CreditCard
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
public class MasterCard extends CreditCard {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean serverConnect() {
		// Connect to Master Card company server
		return true;
	}

	/**
	 * Validate the master card specific rules, and calls the parent validation function
	 *
	 * @return
	 */
	@Override
	public boolean validateCardNumber() {
		if (cardNumber.substring(0, 2).compareTo("51") >= 0 && cardNumber.substring(0, 2).compareTo("55") <= 0) {
			if (cardNumber.length() == 16) {
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
			// process payment using Master Card company's API
			return true;
		}

		return false;
	}
}
