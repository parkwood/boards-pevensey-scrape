

var initialize = function() {

    // bind to the marker detail tap event
    //  $('a[href="#marker-detail"]').live(supportsTouch ? 'tap' : 'click', some fn);
    if(window.location.hash !== ""){ window.location.hash = ""}

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

    $('#searchBox')[0].value = positionConfig.useBrowserLocation? "Current Location" : "";

    $("#searchBox").autocomplete({
        target: $('#suggestions'),
        source: function(text, callback){
            placesService.textSearch({query:text},
                                     function(results, status){
                                         if (status == google.maps.places.PlacesServiceStatus.OK) {

                                             for (var i = 0; i < results.length; i++) {
                                                 var place = results[i];
                                                 place.label = place.name;
                                                 place.value = place.reference;

                                             }

                                         }
                                         callback(results)
                                     });
        },
        link: 'target.html?term=',
        minLength: 4,
callback: function(e){
            var $a = $(e.currentTarget); // access the selected item
            $('#searchBox').val($a.text()); // place the value of the selection into the search box
            $("#searchBox").autocomplete('clear'); // clear the
    // listview
            var place = jQuery.parseJSON($a.attr('data-autocomplete'));
            positionConfig.positionInfo = {coords: {latitude: place.geometry.location.Xa, longitude:place.geometry.location.Ya }}
        }
        
    });

    $('#searchButton').click(
        function(event){
            event.preventDefault();
            generateMapAndListResults(positionConfig);
        });

};

var generateMapAndListResults = function(positionConfig){

    $.mobile.showPageLoadingMsg ("Loading")

    var mapCenter = new google.maps.LatLng(positionConfig.positionInfo.coords.latitude,positionConfig.positionInfo.coords.longitude);

    map = map || new google.maps.Map($('#map_canvas')[0], {
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        center: mapCenter,
        zoom: 15
    });

    var request = {
        location: mapCenter,
        //radius: '500',
        types: ['store'],
        rankBy : google.maps.places.RankBy.DISTANCE
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
        /*results = $(results).sort(function(a,b){
          var aOpen = valOrNull(a,"opening_hours.open_now");
          //var bOpen = valOrNull(b,"opening_hours.open_now");
          if(aOpen){return -1;}
          else{return 1;}
          });*/
        var listElement = ["<ul data-role='listview' data-inset='true' data-theme='d' data-split-icon='gear' data-split-theme='d'>"];
        for (var i = 0; i < results.length; i++) {
            var place = results[i];
            var marker = createMarker(place);
            var hasOpeningHours = place.opening_hours;
            var isOpen =  valOrNull(place,"opening_hours.open_now");
            /* main link on open takes you to map. side link takes you to map
               main link on closed adds info about opening. side link. shows*/
            /* opening times and shows map after

               how to set background color for list item?
            */
            listElement.push("<li id='list_"+place.reference +"' ");
            if(isOpen){
                listElement.push("data-theme='e' ><a href='#' onclick='showOnMap(event,\""+place.reference+"\")'><img src='img/map.png' class='ul-li-icon'/><h1>"+place.name+"</h1><p class='ui-li-aside'>Open</p></a>");
            }
            else if(hasOpeningHours){
                listElement.push("data-theme='b' ><a href='#' onclick='lookupOpening(event,\""+place.reference+"\")'><img src='img/question.png' class='ul-li-icon'/><h1>"+place.name+"</h1> <p class='ui-li-aside'>Closed</p></a>");
            }
            else{
                listElement.push("<a href='#' onclick='showOnMap(event,\""+place.reference+"\")'><img src='img/map.png' class='ul-li-icon'/><h1>"+place.name+"</h1><p class='ui-li-aside'>No Opening Times</p></a>");
            }

            listElement.push("</li>");

        }

        var previouslyRendered = $('#listHolder').attr('class');

        $('#listHolder').html(listElement.join('')+"</ul>");
        previouslyRendered && $('#list_results').trigger("create");
    }

    $.mobile.hidePageLoadingMsg ()
}

var lookupOpening = function(event,placeRef){
    $.mobile.showPageLoadingMsg ("Loading")
    event.preventDefault();
    placesService.getDetails({reference:placeRef},detailsResponse);
}

var detailsResponse = function(place,status){
    if (status == google.maps.places.PlacesServiceStatus.OK) {
        var periods = valOrNull(place,"opening_hours.periods");
        var listItem = "#list_"+place.reference;
        var date = new Date();
        var todaysOpeningTimes = periods[date.getDay()];
        var nowHoursAndMinutesAsNumber = Number(""+date.getHours()+date.getMinutes());
        var todayOpeningTime = Number(todaysOpeningTimes.open.time);
        //alert(todayOpeningTime+" "+nowHoursAndMinutesAsNumber)


        var openingMessage = null;
        if(todayOpeningTime > nowHoursAndMinutesAsNumber){
            openingMessage = "opening at "+todaysOpeningTimes.open.time;

        }
        else{
            var tomorrowsOpeningTimes = periods[(date.getDay()+1) % 7];
            openingMessage = "unlucky, it closed at "+todaysOpeningTimes.close.time+" but opens again at "+tomorrowsOpeningTimes.open.time;
        }

        $(listItem+" img").attr("src","map.png");
        $(listItem+" p").text(openingMessage);

    }
    else
    {
        alert("unable to load opening times");
    }
    $.mobile.hidePageLoadingMsg ()
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


   var google = {maps: {places:{PlacesServiceStatus:{OK :'OK'}} }}

   var generateMapAndListResults = function(positionConfig){
   processServiceResults([{reference:0,opening_hours:{open_now:false},name:'pemberly'},
   {reference:1,opening_hours:{open_now:true},name:'gosford'}],'OK');

   placesService = {getDetails:function(){
   detailsResponse({reference:0, opening_hours: {periods :[
   {
   "close" : {
   "day" : 0,
   "time" : "1400"
   },
   "open" : {
   "day" : 0,
   "time" : "0900"
   }
   },
   {
   "close" : {
   "day" : 1,
   "time" : "1800"
   },
   "open" : {
   "day" : 1,
   "time" : "0800"
   }
   },
   {
   "close" : {
   "day" : 2,
   "time" : "1800"
   },
   "open" : {
   "day" : 2,
   "time" : "0800"
   }
   },
   {
   "close" : {
   "day" : 3,
   "time" : "1800"
   },
   "open" : {
   "day" : 3,
   "time" : "0800"
   }
   },
   {
   "close" : {
   "day" : 4,
   "time" : "1800"
   },
   "open" : {
   "day" : 4,
   "time" : "0800"
   }
   },
   {
   "close" : {
   "day" : 5,
   "time" : "1800"
   },
   "open" : {
   "day" : 5,
   "time" : "0800"
   }
   },
   {
   "close" : {
   "day" : 6,
   "time" : "1400"
   },
   "open" : {
   "day" : 6,
   "time" : "0900"
   }
   }
   ]}},'OK');
   }};

   }

   var createMarker = function(){}

   var initialize = function(){
   if(window.location.hash !== ""){ window.location.hash = ""}
   browserPosition({latitude:50,longitude:0});
   }


   var findLatLongForChosenPlace = function(searchTerm){
   return {positionInfo:{latitude:1, longitude:1}}
   };
*/
