/* 中间高度 */
$(function(){
	var centerHeight= $(window).height() - $('.header').height()-$('#footer').height()-9;
	$(".center_box").height(centerHeight);
	var cenheight=$(".center_box").height()-$(".changepw").height();
	$(".changepw").css("padding-top",cenheight/2);
	if(count==1){
		$(".tipword").hide();
	}else{
		$(".tipword").show();
	}
});
