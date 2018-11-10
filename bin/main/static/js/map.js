var cit = {lat: 51.88640744, lng: -8.53339883};
	
function initMap() {
	
	var map = new google.maps.Map(
	document.getElementById('map'), {zoom: 15, center: cit});
	//var marker = new google.maps.Marker({position: cit, map: map});
}

function placeMarker(element){
	var longitude = parseFloat($(element).find('td').eq(1).text());
	var latitude = parseFloat($(element).find('td').eq(2).text());
	var mark = {lat: longitude, lng: latitude};
	
	var map = new google.maps.Map(
		document.getElementById('map'), 
		{zoom: 15, center: cit});
	
	new google.maps.Marker({position: mark, map: map});
}