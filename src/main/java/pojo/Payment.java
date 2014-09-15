/*
 * Payment.java
 *   - Make connection to VISA company and make the payment
 *
 * Date Created: 2011/10/28
 * Created By: Jeff Lee
 */
package pojo;

/**
 *
 * @author Jeff Lee
 */
public class Payment {

	//
	Order order;

	/**
	 *
	 * @param order
	 */
	public Payment(Order order) {
		this.order = order;
	}

	/**
	 *
	 * @return
	 */
	public boolean makePayment() {
		CreditCard creditCard = order.getPaymentInfo();

		if (creditCard.processPayment()) {
			return true;
		}

		return false;
	}
}
