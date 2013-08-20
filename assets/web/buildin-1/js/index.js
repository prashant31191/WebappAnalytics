$(function(){

	
	$('#container').fadeIn(600,function(){
		$('article').css('marginBottom','10px');
	});
	
	$('article').hover(function(){
		var that = this;
		$('article').each(function(){
			if(that != this){
				$(this).stop(false,true);
				$(this).css('opacity',0.2);
			}
		});
	},function(){
		$(this).stop(false,true);
		$('article').each(function(j){
			$(this).css('opacity',1);
		});
	});
	
	$(document.body).on('touchmove', function(e){
	    e.preventDefault();
	});
	
});
