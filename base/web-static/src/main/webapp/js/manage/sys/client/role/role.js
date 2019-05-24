var tmp ;
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true,
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [ {
			isShow : true,
			label : '角色名称',
			id : 'roleName',
			type : 'input'
		} ],
		buttonItems : [ {
			isShow : hasAuth("OPER_SYS_CLIENT_ROLE_ADD")==true,
			label : '添加角色',
			id : 'addBtn',
			onClickFunction : 'addrole()'
		}],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/client/role/ajax/list',
		method : 'POST',
		data : {
			roleName : '#roleName'
		},
		startType : "startNum",
		startParams : "start",
		limitParams : "limit", 
		defaultLimit : 20,
		supportLimit : [ 20, 50, 100 ],
		result : {
			dataArray : "result.list",
			totalCount : "result.totalCount",
			totalPageNum : "result.totalPage",
			currentPageNum : "result.currentPageNum"
		}
	},
	tableConfig : {
		isNeedIndexRow : true,
		rowItems : [{
					isShow : true,
					title : "角色名称",
					width : "30%",
					align :'left',
					context : '<a href="javascript:void(0);" style=" text-decoration:none;" onclick="seeDetail(@{id})">@{roleName}</a>'
				},{
					isShow : true,
					title : "角色描述",
					width : "51%",
					align :'left',
					context : '<div class="ellipsBox" style="width:460px;"><span class="ellips" style="width:460px;" title="@{remark}">@{remark}</span></div>'	
				},{
					isShow : hasAuths("OPER_SYS_CLIENT_ROLE_UPDATE","OPER_SYS_CLIENT_ROLE_DELETE")==true,
					title : "操作",
					width : "14%",
					selectors : [ {
							isShow:hasAuths("OPER_SYS_CLIENT_ROLE_UPDATE")==true,
							term : "@{id}",
							select : [ {
								value : "@{id}",
								context : "<div class='btn_box rs' alt='修改' title='修改' onClick='modify(@{id})'><i class='iCon grey-write'></i></div>"
							} ]
						},{
							isShow:hasAuths("OPER_SYS_CLIENT_ROLE_DELETE")==true,
							term : "@{id}",
							select : [ {
								value : "@{id}",
								context : "&nbsp;&nbsp;<div class='btn_box rs' alt='删除' title='删除'  onClick='deleteRole(@{id},\"@{roleName}\")'><i class='iCon grey-delete'></i></div>"
							} ]
						} ]
				}]
	}
}

emTable('emTableConfig');
function addrole(){
	 window.location.href=WEB_SERVER_PATH + '/sys/client/role/add';
}
function seeDetail(id){
	 window.location.href=WEB_SERVER_PATH + '/sys/client/role/ajax/to/detail?roleId='+id;
}
//新增角色
$(function(){
	var html='<div class="clear role_div" id="role_div">';
	html +='<form method="post" id="addRoleForm" class="">';
	html += '<dl class="alert-cen">';
	html += '<dt class="item itemNew"><label class="item-label"><span class="xing">*</span>角色名称 :</label><input type="text" id="addroleName" value="" class="item-text" name="addroleName" maxlength="20" /></dt>';
	html +='<dt class="item"><label class="item-label">角色描述 :</label><textarea maxlength="50" name="remark" id="remark" ></textarea></dt>';
	html +='<dd><div class="b_gradient role_auth">角色权限</div></dd>';
	html += '<dd class="authority">';
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/role/ajax/getTree",
		type:'post',
		dataType:'json',
		success:function(data){	
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
					html += "<div class='roleNav'><label><input type='checkbox' onclick='roleNav(this)' class='checkboxdo' id='"+pageIdSecond +"' stype='page' />"+pageNameSecond+"</label></div>" ;
					var pages=page.resource;
					for(var k in pages){
						var lastPages=pages[k];
						var resourceName=pages[k].resourceName;
						var resourceId=pages[k].id;
						html+='<div class="roleNavFind">';
						html+='<label><input type="checkbox" class="checkboxdo" onclick="checkedParent(this,'+resourceId+')"  stype="page" id="'+resourceId+'" stype="page">'+resourceName+'</label><div class="threebox">';
						var aa=lastPages.resource; 
						for(var m in aa){
							var operName=aa[m].resourceName; 
							var operId=aa[m].id; 
							html += '<div class="roleNavFind-cen">';
							html +='<label><input type="checkbox"  parentid="'+resourceId+'"  id="'+operId+'" stype="page" class="checkboxdo" onclick="checkedPage(this,'+resourceId+')"/>'+operName+'</label>';
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
	        html+='<button type="submit" class="tipBtn" id="saveBtn" onclick="ajaxAdd()">保 存</button>';
	        html+='<button onclick="addCancel()" type="button" class="tipBtn tip-cancel">取 消</button></div>';
			html +='</form></div>';
			$("#addRolebox").html(html);
		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
});

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
//新增确定
function ajaxAdd(){
	if($("#remark").val()==''|| $("#remark").val()==undefined || $("#remark").val()==null){
		$("#remark").val("");
	}
	var $formCheckOut = $('#addRoleForm');
    $formCheckOut.validate({
    	rules:{     
    		addroleName:{ 
				required:true,
				allroleName:true
			},
			remark:{
				maxlength:50
			}
    	},
		messages:{
			addroleName:{
				required:"角色名称不可为空"
			},
			remark:{
				rangelength:$.validator.format("角色描述不可超过50个字符")
			},
		},
		submitHandler: function() {
			var name = $('#addroleName').val();
			var remark=$("#remark").val();
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
        		url:  WEB_SERVER_PATH + '/sys/client/role/ajax/add?randomdata='+Date.parse(new Date()),
        		type : 'post',
        		dataType : 'json',
        		data : {
        			auths : tmp,
        			roleName : name,
        			remark : remark
        		},
        		success : function(data) {
        			if (data.success) {
        				$.fn.tipLoddingEnd(true);
        				$.fn.tipAlert('客户角色添加成功',1.5,1);
        				goPage("/sys/client/role");
        			} else {
        				$.fn.tipLoddingEnd(false);
        				$.fn.tipAlert(data.message,1.5,0);
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
//删除
function deleteRole(id,roleName){
	var addHtml = "<div>确定要删除角色【"+roleName+"】吗?</div>";
	$.fn.tipOpen({
		title : '删除确认',
		width : '300',
		btn : [{
			label : '确定',
			onClickFunction : 'ajaxDelete('+id+',"'+roleName+'")'
		}],
		concent : addHtml
	});
}

function ajaxDelete(id,name){
	$.fn.tipLodding();
	$.ajax({
		url :  WEB_SERVER_PATH + "/sys/client/role/ajax/delete",
		type : 'post',
		dataType : 'json',
		data : {
			roleId : id,
			roleName:name
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert("删除成功",1.5,1);
				emrefulsh('emTableConfig');
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error : function() {
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
}

function hasAuths(a,b){
	return (hasAuth(a)||hasAuth(b));
}
function modify(id){
	window.location.href=WEB_SERVER_PATH + '/sys/client/role/modify?roleId='+id;//跳转到修改页面
}
function addCancel(){
	goPage("/sys/client/role");
}
