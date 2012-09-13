

var initialize = function() {

     if (navigator.geolocation)
     {
       navigator.geolocation.getCurrentPosition(browserPosition,showError);
     }
     else {
       showSearchPage({useBrowserLocation:false});
     }       
}

var initialize = function(){
    browserPosition({latitude:50,longitude:0});
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
		     $('#list_results').fadeIn();
                     if(!positionConfig.useBrowserLocation){
                      positionInfo = findLatLongForChosenPlace($('#searchBox').val());
                     }
                     myDoAjax("https://maps.googleapis.com/maps/api/place/search/json?location="+positionInfo.latitude+","+positionInfo.longitude+"&radius=2000&sensor=false&key=AIzaSyD17Ud_ZGwVnmYFh3Us_ggKnH-e64_pYpY",processServiceResults);
                     
                 });

        $('#searchBox')[0].value = positionConfig.useBrowserLocation? "Current Location" : "";

 };

var processServiceResults = function(data){
    if(!data.results || data.results.length === 0){
        alert("nothing found");
        //todo- broaden search?
    }
    for(var i = 0; i <10;i++){
        if(data.results[i]){
          myDoAjax("",displayOpenDetails);
        }
        else{
          break;
        }
    }

}

var myDoAjax = function(url, callback){
   doAjax(url,callback);
}

var myDoAjax = function(url, callback){
   if(url.contains("details")){
       displayOpenDetails();
   }
    else{
        processServiceResults();     
    }
   
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
