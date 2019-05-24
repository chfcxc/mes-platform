//判断目标id是否存在
function exist(id){
	 if($("#"+id).length>0){
	  return true;
	 }
	 else
		 return false;
 } 
$(function(){
	//username
	$("#usernameFor").html(CURRENT_USER_REALNAME);
	//业务script
	var scriptForHtml = $("#scriptFor").html();
	$("head").append('<script type="text/javascript" id="scriptForNew">'+scriptForHtml+'</script>');
	$("#scriptFor").remove();
	//head
	$("head").append($("#headFor").html());
	$("#headFor").remove();
	//title
	var titleForHtml = $("#titleFor").html();
	$("head").append('<title id="titleForNew">'+titleForHtml+'</title>');
	$("#titleFor").remove();
	//顶级导航 
	$("#navList li").each(function(index,ele){
		var code = $(ele).attr("code");
		if(MOUDLES_URLS[code]){ //url存在，改变url
			$(ele).find("a").attr("href",MOUDLES_URLS[code]);
		}else{ //url不存在，移除顶级导航
			$(ele).remove();
		}
		//顶级导航高亮
		if(code == CURRENT_MOUDLE_CODE){
			$(ele).addClass("active");
		}
	});
	//左侧菜单
	$(".left_nav_menu li").each(function(index, element) {
		//二级菜单权限判断
		var thisCode = $(element).attr("code");
		if(AUTHS[thisCode]==false || AUTHS[thisCode]==undefined){ 
			$(element).attr("removeMenu",'false'); 
			var a = $(element).parent().find("li");
			var ll = 0;
			var ii = 0;
			a.each(function(index,obj){
				ll++;
				if($(obj).attr("removeMenu")=='false'){
					ii++;
				}
			});
			if(ii==ll){
			  $(element).parent().prev().remove();
			  $(element).parent().remove();
			}else{
				$(element).remove();
			}
		}else{
			$(element).show();
		}
	});
	
	//面包屑
	if(exist("crumbsThis")){
		if(exist("crumbsmoudle")){ //一级
			if ($("#crumbsForMoudle").html()!="") {
				$("#crumbsmoudle").append($("#crumbsForMoudle").html()).append("&gt;");
			}
			$("#crumbsForMoudle").remove();	
		}
		if(exist("crumbsnav")){ //二级
			if ($("#crumbsForNav").html()!="") {
				$("#crumbsnav").append($("#crumbsForNav").html()).append("&gt;");
			}
			$("#crumbsForNav").remove();	
		}
		if(exist("crumbspageurl")){ //三级跳转
			$("#crumbspageurl").attr("href",$("#crumbsForPageUrl").html());
			$("#crumbsForPageUrl").remove();	
		}
		if(exist("crumbspage")){ //三级
			$("#crumbspage").append($("#crumbsForPage").html());
			$("#crumbsForPage").remove();	
		}
		
		if(exist("crumboperurl")){ //四级跳转
			$("#crumboperurl").attr("href",$("#crumbsForOperUrl").html());
			$("#crumbsForOperUrl").remove();	
		}
		if(exist("crumbsoper")){ //四级
			if ($("#crumbsForOper").html()!="") {
				$("#crumbsForOper").prepend("&gt;");
				$("#crumbsoper").append($("#crumbsForOper").html());
				
			}
			$("#crumbsForOper").remove();	
		}
		if(exist("crumbssupport")){ //增删改
			$("#crumbssupport").append($("#crumbsForSupport").html());
			$("#crumbsForSupport").remove();	
		}
	}
	//右侧业务内容
	if(exist("contextThis")){
		$("#contextThis").append($("#contextFor").html());
		$("#contextFor").remove();
	}
	
});
