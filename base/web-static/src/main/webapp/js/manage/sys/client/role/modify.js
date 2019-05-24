var tmp ;
$(function(){
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/role/ajax/roleauth",
		type:'post',
		dataType:'json',
		data : {
			roleId :uid
		},
		success:function(data){	
			if(data.success){
				$.fn.tipLoddingEnd(true);
				var dataResult = data.result.role;
				var id = dataResult.id;
				var roleName = dataResult.roleName;
				var remark = dataResult.remark;
				var roleauth=data.result.list;
				var arr=[];
				for(var i in roleauth){
					var resourceId = roleauth[i].resourceId;
					arr.push(resourceId);
				}
				$.ajax({
					url:  WEB_SERVER_PATH + "/sys/client/role/ajax/getTree",
					type:'post',
					dataType:'json',
					success:function(data){	
						var html='<div class="clear role_div" id="role_div">';
						html +='<form method="post" id="modifyRoleForm" class=""><input type="hidden" value="" id="modifInpVal" />';
						html += '<dl class="alert-cen">';
						html += '<dt class="item itemNew"><label class="item-label"><span class="xing">*</span>角色名称 :</label><input type="text" id="modifyroleName" name="modifyroleName" value="'+roleName+'" class="item-text"  maxlength="20"  /></dt><div class="clear"></div>';
						html +='<dt class="item"><label class="item-label">角色描述 :</label><textarea id="modifyremark"  value="'+(remark==null || remark==""? "" :remark)+'"  class="item-text" name="modifyremark" maxlength="50" >'+(remark==null || remark==""? "" :remark)+'</textarea></dt>';
						html +='<dd><div class="b_gradient role_auth">角色权限</div></dd>';
						html += '<dd class="authority">';
						var auth = data.result.allauth;
						var ngvs = auth.ngvs;
						for(var i in ngvs){
							var ngv = ngvs[i]; 
							var ngvId = ngv.id;
							var ngvName = ngv.resourceName; 
							html +='<div class="role-title role-on light_blue_bg" onclick="role(this)"><i></i>'+ngvName+'<input type="hidden" id="'+ngvId+'" /></div>';
							html +='<div class="role-cen" style="display:block">';
							var pagesNew=ngv.resource;	
							for(var j in pagesNew){
								var page = pagesNew[j];
								var pageIdSecond = page.id;
								var pageNameSecond = page.resourceName;
								html += "<div class='roleAdd'>";
								html += "<div class='roleNav'><label><input type='checkbox' onclick='roleNav(this)' class='checkboxdo' id='"+pageIdSecond +"' stype='page'";
								/*if($.inArray(pageIdSecond, arr)!=-1 ){
									html += ' checked="checked" ';
								}*/
								
								html += "/>"+pageNameSecond+"</label></div>" ;
								var pages=page.resource;
								for(var k in pages){
									var lastPages=pages[k];
									var resourceName=pages[k].resourceName;
									var resourceId=pages[k].id;
									html+='<div class="roleNavFind">';
									html+='<label><input type="checkbox" class="checkboxdo" onclick="checkedParent(this,'+resourceId+')"  stype="page" id="'+resourceId+'" stype="page"';	
									if($.inArray(resourceId, arr)!=-1){
										html += ' checked="checked" ';
									}

									html += '>'+resourceName+'';
									html += '</label><div class="threebox">';
									var aa=lastPages.resource; 
									
									for(var m in aa){
										var operName=aa[m].resourceName;
										var operId=aa[m].id;
										html += '<div class="roleNavFind-cen">';
										html +='<label><input type="checkbox"  parentid="'+resourceId+'"  id="'+operId+'" stype="page" class="checkboxdo" onclick="checkedPage(this,'+resourceId+')" ';
										if($.inArray(operId, arr)!=-1){
											html += ' checked="checked" ';
										}
										html += '/>'+operName+'</label>';
										
										html +='</div>';
									}
									html+='</div></div>';
									
								}

								html +=	"</div>";
								
						}
							html += "</div>";
					}
						html += '</dd>';
						html += '</dl>';
						html+='<div class="tipFoot">';
				        html+='<button type="submit" class="tipBtn" id="saveBtn" onclick="ajaxmodify()">保 存</button>';
				        html+='<button onclick="addCancel()" type="button" class="tipBtn tip-cancel">取 消</button></div>';
						html +='</form></div>';
						$("#updateRolebox").html(html);
						
						//初始化关联父级
						$(".roleNavFind input[type='checkbox']").each(function(i,ele){
							if($(ele).is(":checked")){
								$(ele).parent().parent().parent().find(".roleNav input[type='checkbox']").attr("checked",true);
							}	
						})
						
						$("#modifInpVal").val(id);
					},
					error:function(){
						$.fn.tipAlert('系统异常',1.5,0);
					}
				});
			}else{
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
	
	
})
//修改
function ajaxmodify(){
	if($("#modifyremark").val()==''|| $("#modifyremark").val()==undefined || $("#modifyremark").val()==null){
		$("#modifyremark").val("");
	}
	var $formCheckOut = $('#modifyRoleForm');
	var validateor= $formCheckOut.validate({
    	rules:{     
    		modifyroleName:{
    			required:true,
				allroleName:true
			},
		    modifyremark:{
		    	maxlength:50
			}
    	},
		messages:{
			modifyroleName:{
				required:"角色名称不可为空"
			},
			modifyremark:{
				rangelength:$.validator.format("角色描述不可超过50个字符")
			}
		},
		submitHandler: function() {
			var roleName = $('#modifyroleName').val();
			var remark=$("#modifyremark").val();
			var uid=$("#modifyRoleForm input[type=hidden]").val();
			var ps = '';
			var os = '';
			$('.authority').find('input[type=checkbox]:checked').each(function(index,element){
				var row = $(element).attr('id');
				var type = $(element).attr('stype');
				if(row.length!==3){
					if(type == "page"){
						ps = ps + row + ",";
					}else{
						os = os + row + ",";
					}
				}
			});
			if(!validate.required(ps)){
				$.fn.tipAlert("角色页面权限不允许为空",1.5,0);
				return false;
			}
			tmp = ps;
			$.fn.tipLodding();
        	$.ajax({
        		url:  WEB_SERVER_PATH + '/sys/client/role/ajax/modify?randomdata='+Date.parse(new Date()),
        		type : 'post',
        		dataType : 'json',
        		data : {
        			auths : tmp,
        			roleName : roleName,
        			remark : remark,
        			roleId :uid
        		},
        		success : function(data) {
        			if (data.success) {
        				$.fn.tipLoddingEnd(true);
        				$.fn.tipAlert('客户角色修改成功',1.5,1);
        				goPage("/sys/client/role");
        			} else {
        				$.fn.tipLoddingEnd(false);
        				$.fn.tipAlert(data.message,1.5,0);
        				validateor.resetForm();
        			}
        		},
        		error : function() {
        			$.fn.tipLoddingEnd(false);
        			$.fn.tipAlert('系统异常',1.5,2);
        			
        		}
        	});
        }
    });
    
}

function checkData(roleName){
	if(validate.notNull(roleName)){
		var len = 20;
		if(!validate.outLenght(roleName,len)){
    		$.fn.tipAlert("角色名称长度不可超过"+len+"个字符",1.5,0);
    		return false;
    	}
	}
	return true;
}
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
		$("input[stype='page'][id='"+pageId+"']").prop("checked",true);
	}
	roleNavAll(obj);
}
function addCancel(){
	goPage("/sys/client/role");
}