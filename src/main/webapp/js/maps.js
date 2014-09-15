/*
 *  Document   : maps.js
 *  Created on : 2011/10/26
 *  Author     : Jeff Lee
 *  Description: JavaScript for the maps
 */

/* Functions for Flight Map Init */

var cityCoords = {
	toronto : [219, 155, "Toronto, Canada"],
	vancouver : [107, 138, "Vancouver, Canada"],
	la : [124, 186, "Los Angeles, US"],
	london : [419, 134, "London, England"],
	madrid : [412, 172, "Madrid, Spain"],
	sydney : [800, 368, "Sydney, Australia"],
	taipei : [727, 222, "Taipei, Taiwan"],
	tokyo : [779, 187, "Tokyo, Japan"],
	beijing : [717, 174, "Beijing, China"]
};

// Adds areas in the map
function cityCoord() {
	for (var city in cityCoords) {
		var area = $('<area>').attr({
			shape: 'poly',
			coords: starCoord(cityCoords[city][0], cityCoords[city][1]),
			href: '#',
			alt: cityCoords[city][2],
			id: city,
			'class': 'cities'
		});

		$("map[name=destinations]").append(area);
	}

	$('.cities').click(function(e){
		e.preventDefault();
		var data = $(this).mouseout().data('maphilight') || {};
		data.alwaysOn = !data.alwaysOn;
		$(this).data('maphilight', data).trigger('alwaysOn.maphilight');

		if ($('#part1').is(":hidden")) {
			showForm($(this).attr('alt'));
		} else {
			var hilighted = getHilighted(".cities");
			if (hilighted == 0) {
				// clean all textbox
				$('#deptCity').val("");
				$('#destCity').val("");
			} else if (hilighted == 1) {
				// check if textboxes has value, if does, remove city, else from = city
				if ($('#deptCity').val() == $(this).attr("alt")) {
					$('#deptCity').val("");
				} else if ($('#destCity').val() == $(this).attr("alt")) {
					$('#destCity').val("");
				} else {
					$('#deptCity').val($(this).attr("alt"));
				}
			} else if (hilighted == 2) {
				// check which is empty, and make the empty = city
				if ($('#destCity').val() == "") {
					$('#destCity').val($(this).attr("alt"));
				} else {
					$('#deptCity').val($(this).attr("alt"));
				}
			} else {
				data.alwaysOn = !data.alwaysOn;
				$(this).data('maphilight', data).trigger('alwaysOn.maphilight');
			}
		}

		markTakenSeats();
	});
}

function starCoord(topX, topY) {
	var str = topX + "," + topY + ",";
	str += (topX - 3) + "," + (topY + 9) + ",";
	str += (topX - 13) + "," + (topY + 9) + ",";
	str += (topX - 6) + "," + (topY + 18) + ",";
	str += (topX - 9) + "," + (topY + 27) + ",";
	str += topX + "," + (topY + 21) + ",";
	str += (topX + 9) + "," + (topY + 27) + ",";
	str += (topX + 6) + "," + (topY + 18) + ",";
	str += (topX + 13) + "," + (topY + 9) + ",";
	str += (topX + 3) + "," + (topY + 9);

	return str;
}

/* Functions for Seat Map Init */

// Adding area elements in map
function seatCoord() {
	var rows = 6, cols = 3;
	var seat1A = [265, 28, 288, 49];
	var colOffset = [0, 58, 83];

	for (var i = 0; i < rows; i++) {
		for (var j = 0; j < cols; j++) {
			var topLeftX = seat1A[0] - i * 34;
			var topLeftY = seat1A[1] + colOffset[j];
			var bottomRightX = seat1A[2] - i * 34;
			var bottomRightY = seat1A[3] + colOffset[j];
			var area = $('<area>').attr({
				shape: 'rect',
				coords: topLeftX + "," + topLeftY + "," + bottomRightX + "," + bottomRightY,
				href: '#',
				alt: i + "-" + j,
				id: "s" + i + "-" + j,
				'class': 'seats'
			});

			$("map[name=seats]").append(area);
		}
	}

	$('.seats').click(function(e){
		e.preventDefault();
		if ($(this).hasClass("taken") == false) {
			var data = $(this).mouseout().data('maphilight') || {};
			data.alwaysOn = !data.alwaysOn;
			$(this).data('maphilight', data).trigger('alwaysOn.maphilight');

			var hilighted = getHilighted(".seats");
			if (hilighted > $('#numPassengers').val()) {
				data.alwaysOn = !data.alwaysOn;
				$(this).data('maphilight', data).trigger('alwaysOn.maphilight');
			}
		}
		updateHiddenField();
	});
}

// used for summary page, to show which seats are booked
function markSeatsBooked(oid, s) {
	var rows = 6, cols = 3;
	var seat1A = [265, 28, 288, 49];
	var colOffset = [0, 58, 83];

	var seats = eval(s);

	for (var i = 0; i < rows; i++) {
		for (var j = 0; j < cols; j++) {
			for (var index in seats) {
				if (seats[index][0] == i && seats[index][1] == j) {
					var topLeftX = seat1A[0] - i * 34;
					var topLeftY = seat1A[1] + colOffset[j];
					var bottomRightX = seat1A[2] - i * 34;
					var bottomRightY = seat1A[3] + colOffset[j];
					var area = $('<area>').attr({
						shape: 'rect',
						coords: topLeftX + "," + topLeftY + "," + bottomRightX + "," + bottomRightY,
						href: '#',
						alt: i + "-" + j,
						id: oid + "-" + i + "-" + j,
						'class': 'seats'
					});

					$("map[name=seats" + oid + "]").append(area);
				}
			}
		}
	}

	$('.seats').click(function(e){
		e.preventDefault();
	}).each(function(){
		var data = $(this).mouseout().data('maphilight') || {};
		data.alwaysOn = true;
		data.fillColor = 'FFB2BD';
		data.stroke = false;
		$(this).data('maphilight', data).trigger('fillColor.maphilight');
		$(this).data('maphilight', data).trigger('stroke.maphilight');
		$(this).data('maphilight', data).trigger('alwaysOn.maphilight');
	});
}

// When dest/dept cities or the dept date is changed, check if there is orders booked, and mark the taken seats
function markTakenSeats() {
	var taken = getSeatInfo();

	if (taken == null) {
		return;
	}

	// loop through area
	$('.seats').each(function(){
		var i = $(this).attr("alt").split("-")[0];
		var j = $(this).attr("alt").split("-")[1];

		for (var index in taken) {
			if (taken[index][0] == i && taken[index][1] == j)	{
				$(this).addClass('taken');
			}
		}
	});

	$('.taken').each(function(index){
		var data = $(this).mouseout().data('maphilight') || {};
		data.alwaysOn = true;
		data.fillColor = 'FFB2BD';
		data.stroke = false;
		$(this).data('maphilight', data).trigger('fillColor.maphilight');
		$(this).data('maphilight', data).trigger('stroke.maphilight');
		$(this).data('maphilight', data).trigger('alwaysOn.maphilight');
	});

}

// Returns the taken seats in 2D array
function getSeatInfo() {
	if ($('#deptCity').val() == "" || $('#destCity').val() == "" || $('#deptDate').val() == "") {
		return null;
	}

	var result;
	$.ajax({
		url: "GetSeats",
		data: {
			deptCity: $('#deptCity').val(),
			destCity: $('#destCity').val(),
			deptDate: $('#deptDate').val()
		},
		type: "POST",
		async: false,
		success: function(data){
			result = eval(data);
		}
	});
	return result;
}

// When seats are sleected, the row and col of the seats are updated in a hidden field
function updateHiddenField() {
	var result = "";
	$('.seats').each(function(index){
		for (var key in $(this).data('maphilight')) {
			if ($(this).data('maphilight')[key] == true && $(this).hasClass("taken") == false) {
				result += $(this).attr("alt") + ",";
			}
		}
	});
	$('input[name=seats]').val(result);
}

/* Functions shared for the maps */

// Count the number of highlighted elements
function getHilighted(map) {
	var count = 0;
	$(map).each(function(index){
		for (var key in $(this).data('maphilight')) {
			if ($(this).data('maphilight')[key] == true && $(this).hasClass("taken") == false) {
				count++;
			}
		}
	});
	return count;
}