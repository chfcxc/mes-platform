//三角箭头
function role(obj) {
	if ($(obj).hasClass('role-on')) {
		$(obj).removeClass('role-on')
		$(obj).next().slideUp();
	} else {
		$(obj).addClass('role-on')
		$(obj).next().slideDown();
	}

}
//角色权限选择
function roleNav(obj){
	if($(obj).prop('checked')){
		$(obj).parents('.roleAdd').find('.roleNavFind').each(function(ind,ele){
			$(ele).find('input[type=checkbox]').each(function(index,element){
				if(!$(element).prop('checked')){
					$(element).prop('checked',true);
				}
			});
		});
	}else{
		$(obj).parents('.roleAdd').find('.roleNavFind').each(function(ind,ele){
			$(ele).find('input[type=checkbox]').each(function(index,element){
				if($(element).prop('checked')){
					$(element).prop('checked',false);
				}
			});
		});
	}
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

function checkedParent(obj,pageId){
	if($(obj).is(':checked')){
		$("input[parentid='"+pageId+"']").prop("checked",true);
	}else{
		$("input[parentid='"+pageId+"']").prop("checked",false);
	}
	roleNavAll(obj);
}
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
		if($(obj).parent().text()=='短信审核操作'){
			$("input[stype='page'][id='"+pageId+"']").prop("checked",flag);
		}else{
			$("input[stype='page'][id='"+pageId+"']").prop("checked",true);
		}
	}
	roleNavAll(obj);
}

function getSystemName(code){
	if(code == 'MANAGE'){
		return '管理系统';
	} else if(code == 'CLIENT'){
		return '客户系统';
	} else if(code == 'OPERS'){
		return '运维系统';
	} else if(code == 'SALES'){
		return '销售系统';
	} else if(code == 'AGENT'){
		return '代理商系统';
	} else {
		return '管理系统';
	}
}

function addCancel(){
	goPage("/sys/auth/role?system=" + SYSTEM_NOW);
}

$("#card_" + SYSTEM_NOW).addClass("li-on");
