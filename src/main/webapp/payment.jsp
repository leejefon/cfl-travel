<%--
    Document    : payment.jsp
    Created on  : 2011/10/28
    Author      : Jeff Lee
    Description : The payment form for user to enter credit card and address.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="pojo.Order"%>
<%@page import="pojo.Order.TripType"%>

<!DOCTYPE html>
<html>
    <head>
		<title>CFL Travel Agency</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href='http://fonts.googleapis.com/css?family=Irish+Grover' rel='stylesheet' type='text/css' />
		<link href='http://fonts.googleapis.com/css?family=Terminal+Dosis' rel='stylesheet' type='text/css' />
		<link href="css/overcast/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="css/base.css" rel="stylesheet" type="text/css" />
		<link href="css/payment.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
		<script type="text/javascript" src="http://code.jquery.com/ui/1.8.16/jquery-ui.min.js"></script>
		<script type="text/javascript" src="js/payment.js"></script>
	</head>
	<body>
		<div id="header">
			<h1>CFL Travel Agency</h1>
		</div>

		<div class="clear"></div>

		<div id="content">
			<h2>Payment</h2>

			<div id="infobox">
				<div id="steps">
					<span class="unfocus">Step 1: Plan</span>
					<span><b>Step 2: Payment</b></span>
					<span class="unfocus">Step 3: Done</span>
				</div>
				<div class='errorMsg'>
				<%
					boolean firstTime = (Boolean) request.getAttribute("firstTime");
					if (!firstTime) {
						out.print("Unable to process payment.  Please try again.");
					}

					Order order = (Order) request.getAttribute("order");
					if (order == null) {
						response.sendRedirect(response.encodeRedirectURL("/"));
						return;
					}
				%>
				</div>
			</div>

			<div class="clear"></div>

			<div id="orderSummary">
				<h3>Order Summary</h3>
				<table>
					<tr>
						<td>Trip Type:</td>
						<td><%= order.getTripType().toString() %></td>
					</tr>
					<tr>
						<td>Passengers:</td>
						<td><%= order.getNumberOfPassengers() %></td>
					</tr>
					<tr>
						<td>Departure City:</td>
						<td><%= order.getDeptCity() %></td>
					</tr>
					<tr>
						<td>Destination City:</td>
						<td><%= order.getDestCity() %></td>
					</tr>
					<tr>
						<td>Departure Date:</td>
						<td><%= order.getDeptDateFormatted("MMM dd, yyyy") %></td>
					</tr>
					<%
						if (order.getTripType().toString() == "ROUND_TRIP") {
					%>
					<tr>
						<td>Return Date:</td>
						<td><%= order.getRetDateFormatted("MMM dd, yyyy") %></td>
					</tr>
					<%
						}
					%>
					<tr>
						<td>Seats:</td>
						<td>
						<%
							int i = 0;
							for (int[] seat : order.getSeats())  {
								if (i != 0) {
									out.print(", ");
								} if (i++ % 5 == 4) {
									out.print("<br />");
								}

								out.print(seat[0] + "-" + seat[1]);
							}
						%>
						</td>
					</tr>
					<tr class="topBorder">
						<td>Total:</td>
						<td>$ <%= order.getAmount() %></td>
					</tr>
				</table>
			</div>

			<% if (order.getPaymentInfo() == null) { %>
			<form action="MakePayment" method="post" id="paymentForm">
				<table>
					<tr>
						<td>Amount:</td>
						<td>$ <%= order.getAmount() %></td>
					</tr>
					<tr>
						<td>Payment Type:</td>
						<td>
							<div id="paymentType">
								<input type="radio" name="paymentType" value="visa" id="visa" checked="checked" />
								<label for="visa"><img src="img/VisaCard.gif" alt="VISA" /></label>
								<input type="radio" name="paymentType" value="master" id="master" />
								<label for="master"><img src="img/MasterCard.png" alt="Master Card" /></label>
								<input type="radio" name="paymentType" value="amex" id="amex" />
								<label for="amex"><img src="img/AmericanExpress.png" alt="American Express" /></label>
							</div>
						</td>
					</tr>
					<tr>
						<td>Card Holder:</td>
						<td>
							<input type="text" name="cardHolder" />
						</td>
					</tr>
					<tr>
						<td>Card Number:</td>
						<td>
							<input type="text" name="cardNumber" />
						</td>
					</tr>
					<tr>
						<td>Expiry Date:</td>
						<td>
							<select name="expMonth"></select>
							<select name="expYear"></select>
						</td>
					</tr>
					<tr>
						<td>Address:</td>
						<td>
							<input type="text" name="address" />
						</td>
					</tr>
					<tr>
						<td>Post Code:</td>
						<td>
							<input type="text" name="postcode" />
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<input type="hidden" name="orderId" value="<%= order.getOrderId() %>" />
							<input type="submit" value="Submit" />
							<input type="submit" value="Plan More Trip" href="/" id="planNewTrip" />
						</td>
					</tr>
				</table>
				<% } else { %>
				<div id="paidMessage">
					Paid
					<div>
						<input type="submit" value="Plan New Trip" href="/" id="planNewTrip" />&nbsp;&nbsp;
						<input type="submit" value="View Order History" href="orders.jsp" id="viewHistory" />
					</div>
				</div>
				<% } %>
			</form>
		</div>

		<div class="clear"></div>

		<div id="footer">
			<p>University of Toronto CSC309 Assignment 3</p>
			<p>Chieh-Feng (Jeff) Lee &copy; 2011</p>
		</div>
	</body>
</html>
