/*
 * GetSeatsServlet.java
 *   - Return an array in JSON format that includes the taken seats
 *
 * Date Created: 2011/10/27
 * Created By: Jeff Lee
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
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
public class GetSeatsServlet extends HttpServlet {

	/**
	 * Used to get the taken seats for particular order.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/plain;charset=UTF-8");

		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();

		String deptCity = request.getParameter("deptCity");
		String destCity = request.getParameter("destCity");
		String deptDate = request.getParameter("deptDate");

		String jsonResult = "[";

		try {
			if (session.isNew() || session.getAttribute("orders") == null) {
				out.print("ALL");
			} else {
				Collection<Order> orders = (Collection<Order>) session.getAttribute("orders");

				for (Order order : orders) {
					if (order.getDeptCity().equals(deptCity) && order.getDestCity().equals(destCity)
							&& order.compareDeptDate(deptDate)) {
						for (int[] seat : order.getSeats()) {
							jsonResult += "[" + seat[0] + "," + seat[1] + "],";
						}
					}
				}

				jsonResult = jsonResult.substring(0, jsonResult.length() - 1) + "]";

				out.print(jsonResult);
			}
		} finally {
			out.close();
		}
	}
}