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
						html +='<form method="post" id="detailRoleForm" class=""><input type="hidden" value="" id="modifInpVal" />';
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
				        html+='<button onclick="addCancel()" type="button" class="tipBtn tip-cancel">返 回</button></div>';
						html +='</form></div>';
						$("#detailRolebox").html(html);
						$("#detailRoleForm input").attr("disabled",true);
						$("#detailRoleForm textarea").attr("disabled",true);
						//初始化关联父级
						$(".roleNavFind input[type='checkbox']").each(function(i,ele){
							if($(ele).is(":checked")){
								$(ele).parent().parent().parent().find(".roleNav input[type='checkbox']").attr("checked",true);
							}	
						})
						
						$("#modifInpVal").val(id);	
						$("#detailRoleForm input[type='checkbox']").each(function(z,eles){
							var checkval=$(eles).attr("checked");
							if(checkval=="checked"){
								$(eles).parent().show();
							}else{
								$(eles).parent().remove();
							}
						})
				
						$("#detailRoleForm .roleNavFind-cen").each(function(q,elesq){
							if($(elesq).html()==""){
								$(elesq).remove();
							}
						})
						$("#detailRoleForm .threebox").each(function(q,elesq){
							if($(elesq).html()==""){
								$(elesq).remove();
							}
						})
						$("#detailRoleForm .roleNavFind").each(function(q,elesq){
							if($(elesq).html()==""){
								$(elesq).remove();
							}
						})
						$("#detailRoleForm .roleNav").each(function(q,elesq){
							if($(elesq).html()==""){
								$(elesq).remove();
							}
						})
						$("#detailRoleForm .roleAdd").each(function(q,elesq){
							if($(elesq).html()==""){
								$(elesq).remove();
							}
						})
						
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

function addCancel(){
	goPage("/sys/client/role");
}