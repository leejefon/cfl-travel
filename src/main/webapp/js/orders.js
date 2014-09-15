/*
 *  Document   : order.js
 *  Created on : 2011/10/29
 *  Author     : Jeff Lee
 *  Description: Handles the summary page including the seat maps and the show/hide effect
 */

$(document).ready(function(){
	$('input[name=planNewTrip], input[name=payNow]').button().click(function(){
		window.location.href = $(this).attr("href");
	});

	$('input[name=printReceipt]').button().click(function(){
		$('iframe#printReceipt').attr("src", $(this).attr("href"));
	});

	$('.seatmap').each(function(){
		$(this).maphilight({
			fillColor: 'FF0026',
			fillOpacity: 0.6,
			stroke: false
		});
		markSeatsBooked($(this).attr("id"), $(this).attr("seats"));
	});

	$('.orderContent').hide();

	// When click on a past order, show/hide the detailed info
	$('.orderTitle').click(function(){
		$(this).next(".orderContent").slideToggle();
	});
});