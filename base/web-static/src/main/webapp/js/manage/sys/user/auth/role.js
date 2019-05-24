$(function(){
	getItems('sysauthrole');
});
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
			isShow:hasAuth("OPER_SYS_USER_ROLE_ADD")==true,
			label : '添加角色',
			id : 'addBtn',
			onClickFunction : 'addrole()'
		}],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/auth/role/ajax/list',
		method : 'POST',
		data : {
			roleName : '#roleName',
			roleType : '#typesss'
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
					context : "@{roleName}"
				},{
					isShow : true,
					title : "角色描述",
					width : "51%",
					align :'left',
					context : '<div class="ellipsBox" style="width:460px;"><span class="ellips" style="width:466px;" title="@{remark}">@{remark}</span></div>'	
				},{
					isShow : hasAuths("OPER_SYS_USER_ROLE_UPDATE",'OPER_SYS_USER_ROLE_DELETE')==true,
					title : "操作",
					width : "14%",
					selectors : [{
							isShow:hasAuth("OPER_SYS_USER_ROLE_UPDATE")==true,
							term : "@{id}",
							select : [ {
								value : "@{id}",
								context : "<div class='btn_box rs' alt='修改' title='修改' onClick='modify(@{id})'><i class='iCon grey-write'></i></div>"
							} ]
						},{
							isShow:hasAuth("OPER_SYS_USER_ROLE_DELETE")==true,
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
	 window.location.href=WEB_SERVER_PATH + '/sys/auth/role/add?system=' + SYSTEM_NOW;
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
		url :  WEB_SERVER_PATH + "/sys/auth/role/ajax/delete",
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
function modify(id){
	window.location.href=WEB_SERVER_PATH + '/sys/auth/role/modify?roleId='+id;//跳转到修改页面
	setItems('sysauthrole');
}

function hasAuths(a,b){
	return (hasAuth(a)||hasAuth(b));
}

function tabContent(obj,types){
	$(obj).addClass("li-on").siblings().removeClass("li-on");
	$("#typesss").val(types);
	SYSTEM_NOW = types;
	emTable('emTableConfig');
}
