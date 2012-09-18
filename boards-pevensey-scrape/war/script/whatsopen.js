

var initialize = function() {

    // bind to the marker detail tap event
    //  $('a[href="#marker-detail"]').live(supportsTouch ? 'tap' : 'click', some fn);

    if (navigator.geolocation)
    {
        navigator.geolocation.getCurrentPosition(browserPosition,showError);
    }
    else {
        showSearchPage({useBrowserLocation:false});
    }
}

var showError = function(error)
{
    showSearchPage({useBrowserLocation:false});
}

var browserPosition = function(position){
    showSearchPage({useBrowserLocation:true, positionInfo:position});
}



var map;
var placesService;

var showSearchPage = function(positionConfig){

    if(!positionConfig.useBrowserLocation){
        positionConfig = findLatLongForChosenPlace($('#searchBox').val());
    }
    $('#searchBox')[0].value = positionConfig.useBrowserLocation? "Current Location" : "";

    $('#searchButton').click(
        function(event){
            event.preventDefault();
            generateMapAndListResults(positionConfig);
        });

};

var generateMapAndListResults = function(positionConfig){

    var mapCenter = new google.maps.LatLng(positionConfig.positionInfo.coords.latitude,positionConfig.positionInfo.coords.longitude);

    map = map || new google.maps.Map($('#map_canvas')[0], {
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        center: mapCenter,
        zoom: 15
    });

    var request = {
        location: mapCenter,
        radius: '500',
        types: ['store']
    };

    placesService = placesService || new google.maps.places.PlacesService(map);
    placesService.search(request,processServiceResults);

}

var markers = {};

var valOrNull = function(obj,attr){
    var attrs = attr.split('.');
    for(var i =0; i<attrs.length;i++){
        var obj = obj[attrs[i]]
        if(obj === undefined) return null;
    }
    return obj;
}

var processServiceResults = function(results, status){
    if (status == google.maps.places.PlacesServiceStatus.OK) {
        if(!results || results.length === 0){
            alert("nothing found");
            //todo- broaden search?
        }

        //todo... how to sort results? filter?
        results = $(results).sort(function(a,b){
            var aOpen = valOrNull(a,"opening_hours.open_now");
            //var bOpen = valOrNull(b,"opening_hours.open_now");
            if(aOpen){return -1;}
            else{return 1;}
        });
        for (var i = 0; i < results.length; i++) {
            var place = results[i];
            var marker = createMarker(place);

            isOpen = place.opening_hours && place.opening_hours.open_now ? "yes" : "no";
            $('#list').append("<li><a href='#'><h1>"+place.name+"</h1><p>Distance: "+1+"km</p> <p>Open: "+isOpen+"</p></a></li>");
        }
    }
}

var createMarker = function (place) {
    var placeLoc = place.geometry.location;
    markers[place.name] = place;
    var marker  = new google.maps.Marker({
        map: map,
        position: place.geometry.location,
        title: place.name,
    });

    // capture touch click events for the created marker
    google.maps.event.addListener(marker, 'click', function() {
        markers[place.name];

    });
    //iwb
    return marker;
}



var myDoAjax = function(url, callback){
    doAjax(url,callback);
}

/* comment me out for real behaviour
   var myDoAjax = function(url, callback){
   if(url.contains("details")){
   displayOpenDetails();
   }
   else{
   processServiceResults();
   }

   }
*/
var google = {maps: {places:{PlacesServiceStatus:{OK :'OK'}} }}

var generateMapAndListResults = function(positionConfig){
    processServiceResults([{opening_hours:{open_now:false},name:'pemberly'},
                           {opening_hours:{open_now:true},name:'gosford'}],'OK');

}

var createMarker = function(){}

var initialize = function(){
    browserPosition({latitude:50,longitude:0});
}


var findLatLongForChosenPlace = function(searchTerm){
    return {positionInfo:{latitude:1, longitude:1}}
};
