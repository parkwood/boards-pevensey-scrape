
	$(function() {

$.ajax({type:'GET', url: '/words', success: function(response) {
                 
       for(idx in response){
         pair = response[idx]; 
        $("#info").append("<div>"+pair[0]+" <a onclick='$(\"#x"+idx+"\")[0].style.visibility=\"visible\"'>show</a><span id='x"+idx+"' style='visibility:hidden'>"+ pair[1]+"</span></div>")
       }
               }});
});
