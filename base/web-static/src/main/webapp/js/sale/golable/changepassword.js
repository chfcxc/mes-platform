/* 中间高度 */
$(function(){
	var centerHeight= $(window).height() - $('.header').height()-$('#footer').height()-19;
	$(".main").height(centerHeight);
	var cenheight=$(".main").height()-$(".changepw").height();
	$(".changepw").css("padding-top",cenheight/2);
	if(count==1){
		$(".tipword").hide();
	}else{
		$(".tipword").show();
	}
});
