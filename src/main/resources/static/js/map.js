var cit = {lat: 51.88640744, lng: -8.53339883};
	
function initMap() {
	
	var map = new google.maps.Map(
	document.getElementById('map'), {zoom: 15, center: cit});
	//var marker = new google.maps.Marker({position: cit, map: map});
}

function fcs(element){
		$(".focused").removeClass("focused");
		
		$(".disposed").each(function() {
  			$(this).addClass("hidden");
  			$(this).removeClass("disposed");
		});
		
		$(element).addClass("focused");

		var nextElement = $(element);
		console.log(5);
		while(true){

			nextElement = $(nextElement).next();
			if($(nextElement).hasClass("hidden")){
				$(nextElement).addClass("disposed");
				$(nextElement).removeClass("hidden");
			} else {
				break;
			}
		}	
	}

function placeMarker(element){
	fcs(element);
	var long = $(element).find('td').eq(1).text();
	var lat = $(element).find('td').eq(2).text();
	if(long != null && lat != null){
		var longitude = parseFloat(long);
		var latitude = parseFloat(lat);
		var mark = {lat: longitude, lng: latitude};
		
		var map = new google.maps.Map(
			document.getElementById('map'), 
			{zoom: 15, center: cit});
		
		new google.maps.Marker({position: mark, map: map});
	}
}