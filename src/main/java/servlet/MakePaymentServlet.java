/*
 * MakePaymentServlet.java
 *   - Handles the payment process and redirect to Order Summary page
 *
 * Date Created: 2011/10/27
 * Created By: Jeff Lee
 */
package servlet;


import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pojo.Order;
import pojo.Payment;

/**
 *
 * @author Jeff Lee
 */
public class MakePaymentServlet extends HttpServlet {

	/**
	 * Handles the payment by calling the Payment POLO
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();

		String orderId = request.getParameter("orderId");

		Map<String, String> paymentInfo = new HashMap<String, String>();

		paymentInfo.put("paymentType", request.getParameter("paymentType"));
		paymentInfo.put("cardHolder", request.getParameter("cardHolder"));
		paymentInfo.put("cardNumber", request.getParameter("cardNumber"));
		paymentInfo.put("expMonth", request.getParameter("expMonth"));
		paymentInfo.put("expYear", request.getParameter("expYear"));
		paymentInfo.put("address", request.getParameter("address"));
		paymentInfo.put("postcode", request.getParameter("postcode"));

		Order order = null;

		for (Order o : (Collection<Order>) session.getAttribute("orders")) {
			if (o.getOrderId() == Integer.parseInt(orderId)) {
				o.setPaymentInfo(paymentInfo);
				order = o;
				break;
			}
		}

		Payment payment = new Payment(order);

		if (payment.makePayment()) {
			response.sendRedirect(response.encodeRedirectURL("orders.jsp?oid=" + order.getOrderId()));
		} else {
			// Reset the payment info
			order.setPaymentInfo(null);

			// Redirect to payment page again
			request.setAttribute("order", order);
			request.setAttribute("firstTime", false);
			RequestDispatcher view = request.getRequestDispatcher("payment.jsp");
			view.forward(request, response);
		}
	}
}
