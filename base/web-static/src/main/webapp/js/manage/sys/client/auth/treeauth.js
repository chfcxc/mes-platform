var authLevel='0';
function tabContent(obj,types){
	if(authLevel == null || authLevel== ''){
		authLevel = '0';
	}
	$(obj).addClass("li-on").siblings().removeClass("li-on");
	$("#typesss").val(types);
	authLevel = types;
	clientAuthInfoList();
}
//二级菜单事件
function checkedParent(obj,pageId){
	if($(obj).is(':checked')){
		$("input[parentid='"+pageId+"']").prop("checked",true);
	}else{
		$("input[parentid='"+pageId+"']").prop("checked",false);
	}
	roleNavAll(obj);
}
//三级菜单事件
function checkedPage(obj,pageId){
	if($(obj).is(':checked')){
		$("input[stype='page'][id='"+pageId+"']").prop("checked",true);
	}else{
		var flag = false;
		$("input[parentid='"+pageId+"']").each(function(i){
			  if($(this).is(':checked')){
				  flag = true;
				 return false;
			  }
		 });
		$("input[stype='page'][id='"+pageId+"']").prop("checked",true);
	}
	roleNavAll(obj);
}
function roleNavAll(obj){
	var i = 0 ;
	$(obj).parents('.roleAdd').find('.roleNavFind').each(function(ind,ele){
		if($(ele).children("label").children("input[type=checkbox]").prop('checked')){
			i = 1;
			return false;
		}
	});
	if(i==0){
		$(obj).parents('.roleAdd').find('.roleNav input[type="checkbox"]').prop("checked",false);
	}else if(i==1){
		$(obj).parents('.roleAdd').find('.roleNav input[type="checkbox"]').prop("checked",true);
	}
}
