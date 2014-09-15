<%--
    Document    : orders.jsp
    Created on  : 2011/10/29
    Author      : Jeff Lee
    Description : Display all the orders and their summary.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Collection"%>
<%@page import="pojo.CreditCard"%>
<%@page import="pojo.Order"%>

<!DOCTYPE html>
<html>
    <head>
		<title>CFL Travel Agency</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href='http://fonts.googleapis.com/css?family=Irish+Grover' rel='stylesheet' type='text/css' />
		<link href='http://fonts.googleapis.com/css?family=Terminal+Dosis' rel='stylesheet' type='text/css' />
		<link href="css/overcast/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="css/base.css" rel="stylesheet" type="text/css" />
		<link href="css/orders.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="http://code.jquery.com/jquery-1.5.2.min.js"></script>
		<script type="text/javascript" src="http://code.jquery.com/ui/1.8.16/jquery-ui.min.js"></script>
		<script type="text/javascript" src="js/jquery.maphilight.min.js"></script>
		<script type="text/javascript" src="js/maps.js"></script>
		<script type="text/javascript" src="js/orders.js"></script>
	</head>
    <body>
		<div id="header">
			<h1>CFL Travel Agency</h1>
		</div>

		<div class="clear"></div>

		<div id="content">
			<h2>Summary</h2>
			<%
				Collection<Order> orders = (Collection<Order>) session.getAttribute("orders");
				boolean noPastRecords = false;
				Order newOrder = null;
				String oid = request.getParameter("oid");

				if (orders == null) {
					noPastRecords = true;
				} else if (oid != null) {
					int count = 0;
					for (Order order : orders) {
						if (order.getOrderId() == Integer.parseInt(oid)) {
							newOrder = order;
							count--;
						}
						count++;
					}
					if (count == 0)  {
						noPastRecords = true;
					}
				}
			%>

			<% if (newOrder != null) { %>
			<div id="infobox">
				<div id="steps">
					<span class="unfocus">Step 1: Plan</span>
					<span class="unfocus">Step 2: Payment</span>
					<span>Step 3: Done</span>
				</div>
			</div>

			<div class="clear"></div>

			<div id="newOrder">
				<h3>New Order</h3>
				<div class="newOrderTitle">
					<% if (newOrder.getTripType().toString() == "ONE_WAY") { %>
					<div class="orderDate"><%= newOrder.getDeptDateFormatted("yyyy/MM/dd") %> (One Way)</div>
					<% } else { %>
					<div class="orderDate"><%= newOrder.getDeptDateFormatted("yyyy/MM/dd") %> ~ <%= newOrder.getRetDateFormatted("yyyy/MM/dd") %></div>
					<% } %>
					<div class="orderCity"><%= newOrder.getDeptCity() %> -> <%= newOrder.getDestCity() %></div>
					<div class="orderLink">
						<% if (newOrder.getPaymentInfo() == null) { %>
							<input type="button" name="payNow" href="PutOrder?oid=<%= newOrder.getOrderId() %>" value="Pay Now" />
						<% } else { %>
							<input type="button" name="printReceipt" href="receipt.jsp?oid=<%= newOrder.getOrderId() %>" value="Print Receipt" />
						<% } %>
					</div>
				</div>
				<div class="newOrderContent">
					<div class="paymentInfo">
						<% CreditCard creditCard = newOrder.getPaymentInfo(); %>
						Payment Info:
						<% if (creditCard != null) { %>
						<table>
							<tr>
								<td>Amount:</td>
								<td>$ <%= newOrder.getAmount() %> (Paid by: <%= newOrder.getPaymentType() %> Card)</td>
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
						<% } else { %>
						<div class="unpaid">Unpaid</div>
						<% } %>
					</div>
					<div class="seatmapDiv">
						<%
							String result = "[";
							for (int[] seat : newOrder.getSeats()) {
								result += "[" + seat[0] + "," + seat[1] + "],";
							}
							result = result.substring(0, result.length() - 1) + "]";
						%>
						<img src="img/SeatMap.bmp" class="seatmap" usemap="#seats<%= newOrder.getOrderId() %>" id="<%= newOrder.getOrderId() %>" seats="<%= result %>" />
						<map name="seats<%= newOrder.getOrderId() %>"></map>
					</div>
				</div>
			</div>

			<div class="clear"></div>
			<% } %>

			<div class="clear"></div>

			<% if (!noPastRecords) { %>
			<div id="pastOrders">
				<h3>Past Orders</h3>
			<%
				for (Order order : orders) {
					if (oid != null && order.getOrderId() == Integer.parseInt(oid)) {
						continue;
					} else {
			%>
				<div class="orderTitle">
					<% if (order.getTripType().toString() == "ONE_WAY") { %>
					<div class="orderDate"><%= order.getDeptDateFormatted("yyyy/MM/dd") %> (One Way)</div>
					<% } else { %>
					<div class="orderDate"><%= order.getDeptDateFormatted("yyyy/MM/dd") %> ~ <%= order.getRetDateFormatted("yyyy/MM/dd") %></div>
					<% } %>
					<div class="orderCity"><%= order.getDeptCity() %> -> <%= order.getDestCity() %></div>
					<div class="orderLink">
						<% if (order.getPaymentInfo() == null) { %>
							<input type="button" name="payNow" href="PutOrder?oid=<%= order.getOrderId() %>" value="Pay Now" />
						<% } else { %>
							<input type="button" name="printReceipt" href="receipt.jsp?oid=<%= order.getOrderId() %>" value="Print Receipt" />
						<% } %>
					</div>
				</div>
				<div class="orderContent">
					<div class="paymentInfo">
						<% CreditCard creditCard = order.getPaymentInfo(); %>
						Payment Info:
						<% if (creditCard != null) { %>
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
						<% } else { %>
						<div class="unpaid">Unpaid</div>
						<% } %>
					</div>
					<div class="seatmapDiv">
						<%
							String result = "[";
							for (int[] seat : order.getSeats()) {
								result += "[" + seat[0] + "," + seat[1] + "],";
							}
							result = result.substring(0, result.length() - 1) + "]";
						%>
						<img src="img/SeatMap.bmp" class="seatmap" usemap="#seats<%= order.getOrderId() %>" id="<%= order.getOrderId() %>" seats="<%= result %>" />
						<map name="seats<%= order.getOrderId() %>"></map>
					</div>
				</div>
				<div class="clear"></div>
			<%
					}
				}
			%>
			</div>
			<% } else { %>
			<div id="noPastRecords">No Past Records</div>
			<% } %>

			<div class="clear"></div><br />

			<input type="button" name="planNewTrip" href="/" value="Plan a new trip" />
		</div>

		<div class="clear"></div>

		<div id="footer">
			<p>University of Toronto CSC309 Assignment 3</p>
			<p>Chieh-Feng (Jeff) Lee &copy; 2011</p>
		</div>

		<iframe src="" id="printReceipt"></iframe>
    </body>
</html>
