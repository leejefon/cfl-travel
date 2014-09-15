/*
 * PutOrderServlet.java
 *   - Process the order information, and redirect to payment
 *
 * Date Created: 2011/10/27
 * Created By: Jeff Lee
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pojo.Order;

/**
 *
 * @author Jeff Lee
 */
public class PutOrderServlet extends HttpServlet {

	/**
	 * Handles the payment with the oid in URL, used in order summary
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int orderId;

		try {
			orderId = Integer.parseInt(request.getParameter("oid"));
		} catch (NumberFormatException e) {
			// redirect to home page
			response.sendRedirect(response.encodeRedirectURL("/AirlineReservationSystem"));
			return;
		}

		HttpSession session = request.getSession();
		Order order = null;

		if (session.getAttribute("orders") != null) {
			for (Order o : (Collection<Order>) session.getAttribute("orders")) {
				if (o.getOrderId() == orderId) {
					order = o;
				}
			}
		}

		request.setAttribute("order", order);
		request.setAttribute("firstTime", true);
		RequestDispatcher view = request.getRequestDispatcher("payment.jsp");
		view.forward(request, response);
	}

	/**
	 * Handles the payment when that are dispatched from the form
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		Collection<Order> orders;

		Order order = new Order();

		// handle number format exception
		int num = Integer.parseInt(request.getParameter("numPassengers"));
		String tripType = request.getParameter("tripType");
		String deptCity = request.getParameter("deptCity");
		String destCity = request.getParameter("destCity");
		String deptDate = request.getParameter("deptDate");
		String retDate = tripType.equals("roundtrip") ? request.getParameter("returnDate") : null;
		String seats = request.getParameter("seats");

		order.setTripType(tripType);
		order.setNumberOfPassengers(num);
		order.setDeptCity(deptCity);
		order.setDestCity(destCity);
		order.setDeptDate(deptDate);
		order.setRetDate(retDate);
		order.setSeats(seats);

		order.calculateAmount();

		if (session.isNew() || session.getAttribute("orders") == null) {
			orders = new ArrayList<Order>();
			order.setOrderId(1);
			orders.add(order);
			session.setAttribute("orders", orders);
		} else {
			orders = (Collection<Order>) session.getAttribute("orders");
			// loop through all of them to check
			order.setOrderId(orders.size() + 1);
			orders.add(order);
			session.setAttribute("orders", orders);
		}

		request.setAttribute("order", order);
		request.setAttribute("firstTime", true);
		RequestDispatcher view = request.getRequestDispatcher("payment.jsp");
		view.forward(request, response);
	}
}
