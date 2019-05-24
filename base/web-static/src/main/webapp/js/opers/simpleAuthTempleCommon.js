$(function(){
	$(".nav_list li").eq(0).addClass("active");
	//移除二级菜单
	$("nav").remove();
	//默认展开一级菜单
  $("#navList").find('a').show();
  $(".nav_list_div,.navbar-side,.clientLogo,.nav_list_div .nav_list li,.container").css({"width":"200px"});
  $(".clientLogo #logo").addClass("disno").css({"background-size":"49px 40px","background-position":"10px 6px","display":"none"});
  $(".clientLogo h2").show();
  $(".container h2 a").css("margin-left","16px");
  $(".open_box").remove();
  $(".nav_list_div").css({"top":"60px"});
  $("#navList").unbind('mouseenter').unbind('mouseleave');
})