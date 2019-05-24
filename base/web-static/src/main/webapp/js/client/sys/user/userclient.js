$(function(){
	getItems('clientAdministrate');
});
//查询
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, 
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
			isShow:hasAuth("OPER_SYS_CLIENT_ADMINISTRA_ADD")==true,
			label : '新增',
			id : 'addBtn',
			onClickFunction : 'addUser()'
		} ],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/client/administrate/findall',
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
					width : "16%",
					align :'left',
					context : "@{username}"
				},{
					isShow : true,
					title : "姓名",
					width : "14%",
					align :'left',
					context : "@{realname}"
				},{
					isShow : true,
					title : "手机号",
					width : "15%",
					context : "@{mobile}"
				},{
					isShow : true,
					title : "邮箱",
					width : "20%",
					align :'left',
					context : "@{email}"
				},{
					isShow : true,
					title : "状态",
					width : "10%",
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
					isShow : hasAuths("OPER_SYS_CLIENT_ADMINISTRA_ON_OFF","OPER_SYS_CLIENT_ADMINISTRA_DETAIL","OPER_SYS_CLIENT_ADMINISTRA_UPDATE","OPER_SYS_CLIENT_ADMINISTRA_DETELE","OPER_SYS_CLIENT_ADMINISTRA_RESET","OPER_SYS_CLIENT_ADMINISTRA_BIND")==true,
					width : "20%",
					title : "操作",
					selectors : [{
						isShow : hasAuth("OPER_SYS_CLIENT_ADMINISTRA_ON_OFF")==true,
						term : "@{state}",
						select : [{
							value : 1,
							context : 
								  "<div class='btn_box rs' alt='启用' title='启用' onClick='enable(@{id},\"@{username}\",@{identity})'><i class='iCon grey-lock'></i></div>&nbsp;"
								 
						},{
							value : 2,
							context : 
								"<div class='btn_box rs' alt='停用' title='停用' onClick='Disable(@{id},\"@{username}\",@{identity})'><i class='iCon grey-unlock'></i></div>&nbsp;"
								 
						},{
							value : 3,
							context : 
								  "<div class='btn_box rs' alt='启用' title='启用' onClick='enable(@{id},\"@{username}\",@{identity})'><i class='iCon grey-lock'></i></div>&nbsp;"
								 
						}]
					},{
						isShow : hasAuth("OPER_SYS_CLIENT_ADMINISTRA_DETAIL")==true,
						term : "@{id}",
						select : [{
							value :"@{id}",
							context : 
								  "<div class='btn_box rs' alt='查看' title='查看' onClick='checkUser(@{id},@{identity})'><i class='iCon grey-see'></i></div>&nbsp;"
								 
						}]
					},{
						isShow : hasAuth("OPER_SYS_CLIENT_ADMINISTRA_UPDATE")==true,
						term : "@{id}",
						select : [{
							value : "@{id}",
							context : 
								  "<div class='btn_box rs' alt='编辑' title='编辑' onClick='modifyUser(@{id},@{identity})'><i class='iCon grey-write'></i></div>&nbsp;"
								 
						}]
					},{
						isShow : hasAuth("OPER_SYS_CLIENT_ADMINISTRA_DETELE")==true,
						term : "@{id}",
						select : [{
							value : "@{id}",
							context : 
								  "<div class='btn_box rs' alt='删除' title='删除' onclick='delConfirm(@{id},\"@{username}\",@{identity})'><i class='iCon grey-delete'></i></div>&nbsp;"
								 
						}]
					},{
						isShow : hasAuth("OPER_SYS_CLIENT_ADMINISTRA_RESET")==true,
						term : "@{id}",
						select : [{
							value : "@{id}",
							context : 
								  "<div class='btn_box rs' alt='重置密码' title='重置密码' onclick='resetPw(@{id},\"@{username}\",@{identity})'><i class='iCon grey-reset'></i></div>&nbsp;"
								 
						}]
					},{
						isShow : hasAuth("OPER_SYS_CLIENT_ADMINISTRA_BIND")==true,
						term : "@{state}",
						select : [{
							value : 1,
							context : 
								  "<div class='btn_box rs' alt='帐号绑定' title='帐号绑定'  style='background:#ccc' onclick='account(@{id},@{identity},@{state},this)'><i class='iCon grey-bind'></i></div>"
								 
						},{
							value : 2,
							context : 
								  "<div class='btn_box rs' alt='帐号绑定' title='帐号绑定' onclick='account(@{id},@{identity},@{state},this)'><i class='iCon grey-bind'></i></div>"
								 
						},{
							value : 3,
							context : 
								  "<div class='btn_box rs' alt='帐号绑定' title='帐号绑定'  onclick='account(@{id},@{identity},@{state},this)'><i class='iCon grey-bind'></i></div>"
								 
						}]
					}]	
				}]
	}
}
emTable('emTableConfig');
function hasAuths(a,b,c,d,e,f){
	return (hasAuth(a)||hasAuth(b)||hasAuth(c)||hasAuth(d)||hasAuth(e)||hasAuth(f));
}

//新增
function addUser(){
	window.location.href=WEB_SERVER_PATH + '/sys/client/administrate/toadd';
}
//查看
function checkUser(id,identity){
	if(identity!=1){
		window.location.href=WEB_SERVER_PATH + '/sys/client/administrate/todetail/'+id;
		setItems('clientAdministrate');
	}else{
		$.fn.tipAlert('不能操作系统管理员',1.5,0);
	}	
	
}
//停用
function Disable(id,username,identity){
	if(id!=1&&identity!=1){
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
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/administrate/updatestate",
		type:'post',
		dataType:'json',
		data:{
			userId:id,
			state:"1"
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
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}

//启用
function enable(id,user,identity){
	if(id!=1&&identity!=1){
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
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/administrate/updatestate",
		type:'post',
		dataType:'json',
		data:{
			userId:id,
			state:"2"
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('启用成功',1,1);
				emrefulsh('emTableConfig');
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message, 1.5, 0);
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
//删除
function delConfirm(id,username,identity){
	if(id!=1&&identity!=1){
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
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/administrate/delete",
		type:'post',
		dataType:'json',
		data:{
			userId:id,
			state:"0"
		},
		success:function(data){
				if (data.success) {
					$.fn.tipLoddingEnd(true);
					$.fn.tipAlert('用户删除成功',1,1);
					location.reload();
				} else {
					$.fn.tipLoddingEnd(false);
					$.fn.tipAlert(data.message, 1.5, 0);
				}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});	
}
//重置密码
function resetPw(id,username,identity){
	if(id!=1&&identity!=1){
		var addHtml ="";
	 	addHtml = "<div>【"+username+"】确定要重置密码吗？</div>";
		$.fn.tipOpen({
				title : "重置密码确认",// 弹框标题
				width : '360',// 弹框内容宽度
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
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/administrate/updatepwd",
		type:'post',
		dataType:'json',
		data:{
			userId:id
		},
		success:function(data){
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
				/*$.fn.tipAlert('重置密码成功',1,1);
				location.reload();*/
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message, 1.5, 0);
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});	
	
}
function seeClose(){
	$.fn.tipShut();
	location.reload();
}
//编辑
function modifyUser(id,identity){
	if(id!=1&&identity!=1){
		window.location.href=WEB_SERVER_PATH + '/sys/client/administrate/toupdate/'+id;
		setItems('clientAdministrate');
	}else{
		$.fn.tipAlert('不能操作系统管理员',1.5,0);
	}	
	
}
//账号绑定
function account(id,identity,state,obj){
	if(id!=1&&identity!=1){
		if(state==1){
			$.fn.tipAlert('用户已停用，不能进行账号绑定',1.5,0);
			$(obj).css("background","#ccc");
		}else{	
			window.location.href=WEB_SERVER_PATH + '/sys/client/administrate/account/'+id;
			setItems('clientAdministrate');
		}
	}else{
		$.fn.tipAlert('不能操作系统管理员',1.5,0);
	}
		
}
