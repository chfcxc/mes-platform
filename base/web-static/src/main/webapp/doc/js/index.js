$(function() {
    if (window.PIE) {
        $('.rounded').each(function() {
            PIE.attach(this);
        });
    };
 /*顶部导航*/
  var $head=$('.listDiv h3'),$ul=$('.listInfo'),$lis=$ul.find('li'),$li=$ul.find('li .twoList'),$promise=$('.prosmore'),$span=$promise.find('span'),inter=false;
    $head.mouseenter(function(){
		$(this).addClass('hover');
		$(this).siblings().slideDown(400);
	});
	$('.listDiv').mouseleave(function(){
		$(this).find('h3').removeClass('hover');
		$(this).find('ul').slideUp(400);
	});
	
	$li.mouseenter(function(){
		$(this).css('background','rgba(7,51,100,.7)')
		$(this).siblings('.prosmore').slideDown(400);
	});
	$lis.mouseleave(function(){
		$(this).find('.twoList').css('background','rgba(0,78,163,.7)')
//		$(this).find('.prosmore').slideUp(400);
	});
	
	$span.hover(function(){
		$(this).addClass('spanBg');
		$(this).find('em').addClass('emStyle');
	},function(){
		$(this).removeClass('spanBg');
		$(this).find('em').removeClass('emStyle');
	});
    
    
    
    /*客服*/
   $('.custom_service li').hover(function(){
   		$(this).addClass('hoverBg');
   		if($(this).hasClass('liA')){
   			$('.call_phone').show();
   		}else{
   			$('.call_phone').hide();
   		}
   		if($(this).hasClass('wx')){
   			$('.twocode').show();
   		}else{
   			$('.twocode').hide();
   		}
   },function(){
   		$(this).removeClass('hoverBg');
   		$('.call_phone').hide();
   });
   
   $('.twocode').mouseleave(function(){
   	$(this).hide();
   });
   
   /*底部*/
  var footLi=$('.footL ul li');
  footLi.hover(function(){
  	$(this).find('a').addClass('contentC');
  },function(){
  	$(this).find('a').removeClass('contentC');
  });
   
   
   /*返回顶部*/
   $(window).scroll(function(){
	   var sc=$(window).scrollTop();
	   if(sc>200){
	   		$('.custom_service').show();
	   }else{
	   		$('.custom_service').hide();
	   }
 	});
	 $('#scrollT').click(function(){
	 	$('body,html').animate({scrollTop:0},300);
	 });
 
 
});