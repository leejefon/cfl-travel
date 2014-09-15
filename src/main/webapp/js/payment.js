/*
 *  Document   : index.js
 *  Created on : 2011/10/27
 *  Author     : Jeff Lee
 *  Description: JavaScript for the payment form
 */

$(document).ready(function(){
	$('select[name=expMonth]').append(getMonthOptions());
	$('select[name=expYear]').append(getYearOptions());
	$('input[type=submit]').button();

	$('input#planNewTrip, input#viewHistory').click(function(e){
		e.preventDefault();
		window.location.href = $(this).attr("href");
	});

	$('#paymentForm').submit(function(){
		return validateInput();
	});
});

// Month options and Year options are dynamically generated, too lazy..
function getMonthOptions() {
	var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
	var options = "";

	var i = 1;
	for (var index in months) {
		options += "<option value='" + i++ + "'>" + months[index] + "</option>";
	}

	return options;
}

function getYearOptions() {
	var start = 2012, end = 2020;
	var options = "";

	for (var i = start; i <= end; i++) {
		options += "<option>" + i + "</option>";
	}

	return options;
}

// Validate the input before submit
// for Credit Card, just simply check the number of digits, the number will be validated when making payment
function validateInput() {
	if ($('input[name=cardHolder]').val() == "") {
		$('.errorMsg').html("Please enter the name appears on the card.");
		return false;
	}

	var cardnum = $('input[name=cardNumber]').val();
	if (isNaN(cardnum)) {
		$('.errorMsg').html("Credit card not number");
		return false;
	} else if (cardnum.length < 15) {
		$('.errorMsg').html("Credit card number too short");
		return false;
	}

	if ($('input[name=address]').val() == "") {
		$('.errorMsg').html("Please enter your address.");
		return false;
	}

	if ($('input[name=postcode]').val() == "") {
		$('.errorMsg').html("Please enter your postcode.");
		return false;
	}

	return true;
}