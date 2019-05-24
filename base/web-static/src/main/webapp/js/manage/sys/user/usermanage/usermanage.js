$(function(){
	getItems('sysusermanage');
});
//查询
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, //true 显示具体的分页数
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [{
			label : '用户名',
			id : 'username',
			type : 'input'
		},{
			label : '姓名 ',
			id : 'realname',
			type : 'input'
		},{
			label : '手机号 ',
			id : 'mobile',
			type : 'input'
		} ],
		buttonItems : [{
			isShow:hasAuth("OPER_SYS_USER_ADD")==true,
			label : '新增',
			id : 'addBtn',
			onClickFunction : 'addUser()'
		} ],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/user/manage/ajax/list',
		method : 'POST',
		data : {
			username : '#username', //用户名
			realname : '#realname', //姓名
			mobile : '#mobile'//手机号
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
		rowItems : [
				{
					isShow : true,
					title : "用户名",
					width : "10%",
					align :'left',
					context : "<div class='leftdiv'>@{username}</div>"
				},{
					isShow : true,
					title : "姓名",
					width : "7%",
					align :'left',
					context : "@{realname}"
				},{
					isShow : true,
					title : "部门",
					width : "15%",
					align :'left',
					context : "<div class='leftdiv'>@{department}</div>"
				},{
					isShow : true,
					title : "手机号",
					width : "10%",
					context : "@{mobile}"
				},{
					isShow : true,
					title : "邮箱",
					width : "20%",
					align :'left',
					context : "<div class='ellipsBox'  style='width:200px;'><span class='ellips' style='width:200px;' title='@{email}' >@{email}</span></div>"
				},{
					isShow : true,
					title : "角色",
					width : "10%",
					align :'left',  
					context : '<div class="ellipsBox"  style="width:80px;"><span class="ellips" style="width:80px;" title="@{rolename}">@{rolename}</span></div>',
				},{
					isShow : true,
					title : "是否领导",
					width : "6%",
					selectors : [ {
						isShow : true,
						term : "@{identity}",
						select : [ {
							value : 1,
							context : "是"
						}, {
							value : 2,
							context : "否"
						}]
					} ]

				},{
					isShow : true,
					title : "状态",
					width : "5%",
					selectors : [ {
						isShow : true,
						term : "@{state}",
						select : [ {
							value : 1,
							context : "停用"
						}, {
							value : 2,
							context : "启用"
						}, {
							value : 3,
							context : "锁定"
						}]
					} ]

				},{
					isShow : hasAuths("OPER_SYS_USER_UPDATE","OPER_SYS_USER_DELETE","OPER_SYS_USER_ON_OFF")==true,
					width : "12%",
					title : "操作",
					selectors : [{
						isShow : hasAuth("OPER_SYS_USER_ON_OFF")==true,
						term : "@{state}",
						select : [{
							value : 1,
							context : 
								  "<div class='btn_box rs' alt='启用' title='启用' onClick='enable(@{id},\"@{username}\")'><i class='iCon grey-lock'></i></div>&nbsp;"
								 
						},{
							value : 3,
							context : 
								  "<div class='btn_box rs' alt='启用' title='启用' onClick='enable(@{id},\"@{username}\")'><i class='iCon grey-lock'></i></div>&nbsp;"
								 
						},{
							value : 2,
							context : 
								 "<div class='btn_box rs' alt='停用' title='停用' onClick='Disable(@{id},\"@{username}\")'><i class='iCon grey-unlock'></i></div>&nbsp;" 
								 
						}]
					},{
						isShow : hasAuth("OPER_SYS_USER_UPDATE")==true,
						term : "@{id}",
						select : [{
							value : "@{id}",
							context : 
								 "<div class='btn_box rs' alt='编辑' title='编辑' onClick='modifyUser(@{id})'><i class='iCon grey-write'></i></div>&nbsp;"
					     }]
		            },{
						isShow : hasAuth("OPER_SYS_USER_DELETE")==true,
						term : "@{id}",
						select : [{
							value : "@{id}",
							context : 
								 "<div class='btn_box rs' alt='删除' title='删除' onclick='delConfirm(@{id},\"@{username}\")'><i class='iCon grey-delete'></i></div>&nbsp;"
					     }]
		            },{
		            	isShow : hasAuth("OPER_SYS_USER_UPDATE")==true,
						term : "@{id}",
						select : [{
							value : "@{id}",
							context : 
								 "<div class='btn_box rs' alt='重置密码' title='重置密码' onclick='resetPw(@{id},\"@{username}\")'><i class='iCon grey-reset'></i></div>"
					     }]
		            }]

			}]
				
		}
}
emTable('emTableConfig');

//新增
function addUser(){
	window.location.href=WEB_SERVER_PATH + '/sys/user/manage/adduser';
}


//编辑
function modifyUser(id){
	if(id!=1){
		window.location.href=WEB_SERVER_PATH + '/sys/user/manage/modify?userId='+id;
		setItems('sysusermanage');
	}else{
		$.fn.tipAlert('不能操作系统管理员',1.5,0);
	}	
}

//停用/启用
function Disable(id,username){
	if(id!=1){
		var addHtml ="";
	 	addHtml = "<div>确定要停用用户【"+username+"】?</div>";
		$.fn.tipOpen({
				title : "停用确认",// 弹框标题
				width : '300',// 弹框内容宽度
				height : '27',
				concent :addHtml,//弹框内容
				btn : [ {
			 		label : '确定',
			 		onClickFunction : 'Dis('+id+')'
			 	}]				
			});
	}else{
		$.fn.tipAlert('不能操作系统管理员',1.5,0);
	}	
}
function Dis(id,username){
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/user/manage/ajax/off",
		type:'post',
		dataType:'json',
		data:{
			userId:id
		},
		success:function(data){

			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('停用成功',1,1);
				emrefulsh('emTableConfig');
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message, 1.5, 0);
			}
		
		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
//停用
function enable(id,user){
	if(id!=1){
		var addHtml ="";
	 	addHtml = "<div>确定要启用用户【"+user+"】?</div>";
		$.fn.tipOpen({
				title : "启用确认",// 弹框标题
				width : '300',// 弹框内容宽度
				height : '27',
				concent :addHtml,//弹框内容
				btn : [ {
			 		label : '确定',
			 		onClickFunction : 'enab('+id+')'
			 	}]				
			});
	}else{
		$.fn.tipAlert('不能操作系统管理员',1.5,0);
	}	
}
function enab(id){
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/user/manage/ajax/on",
		type:'post',
		dataType:'json',
		data:{
			userId:id
		},
		success:function(data){
			if (data.success) {
				if (data.success) {
					$.fn.tipLoddingEnd(true);
					$.fn.tipAlert('启用成功',1,1);
					emrefulsh('emTableConfig');
				} else {
					$.fn.tipLoddingEnd(false);
					$.fn.tipAlert(data.message, 1.5, 0);
				}
			} else {
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
//删除
function delConfirm(id,username){
	if(id!=1){
		var addHtml ="";
	 	addHtml = "<div>确定要删除用户【"+username+"】？</div>";
		$.fn.tipOpen({
				title : "删除确认",// 弹框标题
				width : '300',// 弹框内容宽度
				height : '27',
				concent :addHtml,//弹框内容
				btn : [ {
			 		label : '确定',
			 		onClickFunction : 'delUser('+id+')'
			 	}]				
			});
	}else{
		$.fn.tipAlert('不能操作系统管理员',1.5,0);
	}	
}
function delUser(id){
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/user/manage/ajax/delete",
		type:'post',
		dataType:'json',
		data:{
			userId:id
		},
		success:function(data){
			if (data.success) {
				if (data.success) {
					$.fn.tipLoddingEnd(true);
					$.fn.tipAlert('用户已删除',1,1);
					emrefulsh('emTableConfig');
				} else {
					$.fn.tipLoddingEnd(false);
					$.fn.tipAlert(data.message, 1.5, 0);
				}
			} else {
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});	
}
//重置密码
function resetPw(id,username){
	if(id!=1){
		var addHtml ="";
	 	/*addHtml = "<div>【"+username+"】确定要重置为原始密码"+username+"吗？</div>";*/
		addHtml = "<div>【"+username+"】确定要重置密码吗？</div>";
		$.fn.tipOpen({
				title : "重置密码确认",// 弹框标题
				width : '300',// 弹框内容宽度
				height : '27',
				concent :addHtml,//弹框内容
				btn : [ {
			 		label : '确定',
			 		onClickFunction : 'resetPwS('+id+')'
			 	}]				
			});
	}else{
		$.fn.tipAlert('不能操作系统管理员',1.5,0);
	}	
}
function resetPwS(id){
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/user/manage/ajax/reset",
		type:'post',
		dataType:'json',
		data:{
			userId:id
		},
		success:function(data){
			if (data.success) {
				if (data.success) {
					$.fn.tipLoddingEnd(true);
					var password=data.result;
					var html ="";
					html = "<div>生成的随机6位密码是："+password+"</div>";
					html+='<div class="tipFoot tipFootMp20">';
					html+='<button onclick="seeClose()" type="button" class="tipBtn">关 闭</button>';
					html+='</div>';
					$.fn.tipOpen({
						title : "密码提示",
						width : '300',
						concent :html,
						btn : []				
					});
					/*$.fn.tipAlert('密码重置成功',1,1);
					emrefulsh('emTableConfig');*/
				} else {
					$.fn.tipLoddingEnd(false);
					$.fn.tipAlert(data.message, 1.5, 0);
				}
			} else {
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});	
	
}

function hasAuths(a,b,c){
	return (hasAuth(a)||hasAuth(b)||hasAuth(c));
}
function seeClose(){
	$.fn.tipShut();
	emrefulsh('emTableConfig');
}
