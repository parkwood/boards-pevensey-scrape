
   var initialize = function() {

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

 var showSearchPage = function(positionConfig){
         $('#searchButton').click(function(){
                     $('#splash').fadeOut();
		     $('#map_results').fadeIn();
                     if(!positionConfig.useBrowserLocation){
                       positionInfo = findLatLongForChosenPlace($('#searchBox').val());
                     }
                     searchForService(positionInfo);
                 });

        $('#searchBox').setValue(positionConfig.useBrowserLocation? "Current Location" : "");

 };

var searchForService = function(positionInfo){
  centerMap(positionInfo);
}

var findLatLongForChosenPlace = function(searchTerm){};


 var centerMap = function(latLong){
    var mapOptions = {
           center: new google.maps.LatLng(latLong.latitude, latLong.longitude),
           zoom: 8,
           mapTypeId: google.maps.MapTypeId.ROADMAP
         };
         var map = new google.maps.Map(document.getElementById("map_canvas"),
             mapOptions);
 }
