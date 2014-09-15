/*
 *  Document   : index.js
 *  Created on : 2011/10/26
 *  Author     : Jeff Lee
 *  Description: JavaScript for the form in index.html
 */

$(document).ready(function(){
	// Dynamically add the coords, instead of hard coded the coords (even tho it is a bit hard coded)
	seatCoord();
	cityCoord();

	// A jQuery plugin that highlights the map area when hovered
	$('.flightmap').maphilight({
		fillColor: 'FF0026',
		fillOpacity: 0.6,
		stroke: false
	});
	$('.seatmap').maphilight({
		fillColor: '3B83CC',
		fillOpacity: 0.6,
		stroke: false
	});

	// jQuery UI for the datepicker, a way to enforce the format of the date
	$('#deptDate').datepicker({
		dateFormat: "yy-mm-dd",
		minDate: new Date()
	}).change(function(){
		markTakenSeats();
		$('#returnDate').datepicker("option", "minDate", new Date((new Date($('#deptDate').val())).valueOf() + 1000 * 3600 * 24));
	});
	$('#returnDate').datepicker({
		dateFormat: "yy-mm-dd"
	});

	// Style the form
	$('#tripType td:first-child').buttonset();
	$('input[name=viewHistory]').button().click(function(){
		window.location.href = "orders.jsp";
	});
	$('input[type=text]').addClass("floatRight");
	$('.floatRight').parent("div").css("overflow", "auto");

	// When trip type changes, show/hide the return date
	$('input[name=tripType]').change(function(){
		if ($(this).val() == "oneway") {
			$('#return').slideUp();
		} else {
			$('#return').slideDown();
		}
	});

	$('#steps').hide();
	$('#part1').hide();
	$('#confirmBooking').button().hide().click(bookTicket);

});

// When a city is clicked, show the form
function showForm(fromCity) {
	$('#intro').hide();
	$('#steps').fadeIn();
	$('#content h2').html("Plan New Trip");

	$('#part1').slideDown();
	$('#numPassengers').focus();
	$('#confirmBooking').show();
	$('#deptCity').val(fromCity);
}

// Input validation and submit
function bookTicket() {
	var numPassenger = $('#numPassengers').val();

	if (parseFloat(numPassenger) != parseInt(numPassenger) || isNaN(numPassenger)) {
		$('.errorMsg').html("Number of Passenger not an integer");
		$('#numPassengers').focus();
		return false;
	}

	// TODO: 18 - seat taken
	if (parseInt(numPassenger) > 18) {
		$('.errorMsg').html("Number of Passengers too many");
		$('#numPassengers').focus();
		return false;
	}

	var deptDate = $('#deptDate').val();
	if (deptDate == "" || deptDate.match(/\d{4}-\d{2}-\d{2}/) == null) {
		$('.errorMsg').html("Depature Date in wrong format");
		$('#deptDate').focus();
		return false;
	}

	// if trip type is one way, then return date is not required
	var retDate = $('#returnDate').val();
	if ($('#tripType :checked').val() == "roundtrip" && (retDate == "" || retDate.match(/\d{4}-\d{2}-\d{2}/) == null)) {
		$('.errorMsg').html("Return Date in wrong format");
		$('#returnDate').focus();
		return false;
	}

	var deptCity = $('#deptCity').val();
	if (deptCity == "") {
		$('.errorMsg').html("Where are you departing from?  Choose a city.");
		return false;
	}

	var destCity = $('#destCity').val();
	if (destCity == "") {
		$('.errorMsg').html("Where are you going?  Choose a city.");
		return false;
	}

	if (getHilighted(".seats") != parseInt(numPassenger)) {
		$('.errorMsg').html("Choose your seat(s)");
		return false;
	}

	$('input[disabled=disabled]').removeAttr("disabled");
	$('#reservationForm').submit();
}
