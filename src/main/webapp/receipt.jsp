<%--
    Document    : receipt.jsp
    Created on  : 2011/10/29
    Author      : Jeff Lee
    Description : Prints the receipt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Collection"%>
<%@page import="pojo.CreditCard"%>
<%@page import="pojo.Order"%>

<%
	Collection<Order> orders = (Collection<Order>) session.getAttribute("orders");
	Order order = null;

	String oid = request.getParameter("oid");

	if (oid != null) {
		for (Order o : orders) {
			if (o.getOrderId() == Integer.parseInt(oid)) {
				order = o;
			}
		}
	} else {
		return;
	}
%>

<!DOCTYPE html>
<html>
    <head>
		<title>CFL Travel Agency - Order Summary (Ref#: <%= order.getOrderId() %>)</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href='http://fonts.googleapis.com/css?family=Irish+Grover' rel='stylesheet' type='text/css' />
		<link href='http://fonts.googleapis.com/css?family=Terminal+Dosis' rel='stylesheet' type='text/css' />
		<link rel="stylesheet" media="print" href="css/receipt.css" type="text/css" />
	</head>
    <body onload="window.print()">
		<div id="header">
			<h1>CFL Travel Agency</h1>
		</div>

		<h2>Order Summary</h2>

		<div class="flightInfo">
			<h3>Flight Info:</h3>
			<% if (order.getTripType().toString() == "ONE_WAY") { %>
			<div class="flight"><%= order.getDeptDateFormatted("yyyy/MM/dd") %> (One Way)</div>
			<% } else { %>
			<div class="flight"><%= order.getDeptDateFormatted("yyyy/MM/dd") %> ~ <%= order.getRetDateFormatted("yyyy/MM/dd") %></div>
			<% } %>
			<div class="flight"><%= order.getDeptCity() %> -> <%= order.getDestCity() %></div>
			<div class="flight">Seat(s):&nbsp;
			<%
				for (int[] seat : order.getSeats()) {
					out.print("<br />&nbsp;&nbsp;Row " + (seat[0] + 1) + " Col " + (seat[1] + 1));
				}
			%>
			</div>
		</div>
		<div class="paymentInfo">
			<% CreditCard creditCard = order.getPaymentInfo(); %>
			<h3>Payment Info:</h3>
			<table>
				<tr>
					<td>Amount:</td>
					<td>$ <%= order.getAmount() %> (Paid by: <%= order.getPaymentType() %> Card)</td>
				</tr>
				<tr>
					<td>Card Holder:</td>
					<td><%= creditCard.getHolderName() %></td>
				</tr>
				<tr>
					<td>Card Number:</td>
					<td><%= creditCard.getCardNumber() %> (exp: <%= creditCard.getExpiryDate() %>)</td>
				</tr>
				<tr>
					<td>Address:</td>
					<td><%= creditCard.getAddress() + ", " + creditCard.getPostcode() %></td>
				</tr>
			</table>
		</div>

		<br />

		<div id="footer">
			University of Toronto CSC309 Assignment 3<br />
			Chieh-Feng (Jeff) Lee &copy; 2011
		</div>
    </body>
</html>
