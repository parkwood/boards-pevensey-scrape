
	$(function() {

$.ajax({type:'POST', url: '/words', data:{}, success: function(response) {
                 $('#info').html(response);
               }});
});
