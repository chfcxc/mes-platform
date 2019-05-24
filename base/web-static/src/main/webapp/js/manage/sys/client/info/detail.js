$(function(){
	var enterpriseId = $('#enterpriseId').val()
	baseInfo(enterpriseId);//账号基本信息
	getBusinessList(enterpriseId); //获取list
	/*smsServiceCode(enterpriseId);//关联短信服务号
	flowServiceCode(enterpriseId);//关联流量服务号
	imsServiceCode(enterpriseId);//关联国际短信服务号
	voiceServiceCode(enterpriseId);//关联语音服务号
	*/

})
function baseInfo(enterpriseId){
	$.fn.tipLodding();
 	 $.ajax({
   		url : WEB_SERVER_PATH + '/sys/client/info/ajax/accountInfo', 
   		type : "post",
   		dataType : "json",
   		async:false,
   		data : {
   			id : enterpriseId
   		},
   		success:function(data) {
   			if (data.success) {
					$.fn.tipLoddingEnd(true);
					var list=data.result;
					var clientName=list.clientName;
					var clientNumber=list.clientNumber;
					var clientid=list.id;
					var id=list.manageAccount.id;
					var state=list.manageAccount.state;
					var username=list.manageAccount.username;
					var identity=list.manageAccount.identity;
					var type=list.type;
					$("#clientName").text(clientName);
					$("#clientNumber").text(clientNumber);
					$("#detailId").val(clientid);
					$("#manageAccountState").val(state);
					var html='<tr><td>'+username+'</td>';
					if(identity==1){
						html+='<td>管理账号</td>';
					}else{
						html+='<td>子账号</td>';
					}
					if(state==1){
						html+='<td>停用</td>';
					}else if(state==2){
						html+='<td>启用</td>';
					}else if(state==3){
						html+='<td>锁定</td>';
					}
					html+='<td>';
					if(state==1 || state==3){
						html+='<div class="btn_box rs"  title="启用" onclick="enable('+id+',\''+username+'\',\''+type+'\')"><i class="iCon grey-lock"></i></div>&nbsp;';
					}else{
						html+='<div class="btn_box rs"  title="停用" onclick="disable('+id+',\''+username+'\',\''+type+'\')"><i class="iCon grey-unlock"></i></div>&nbsp;';
					}
					html+='<div class="btn_box rs"  title="重置密码" onclick="resetPw('+id+',\''+username+'\')"><i class="iCon grey-reset"></i></div>';
					html+='</td></tr>';
					var childAccountList=list.childAccountList;
					var len=childAccountList.length;
					if(len!=0){
						var childHtml='';
						for(var i in childAccountList){
							var usernameC=childAccountList[i].username;
							var identityC=childAccountList[i].identity;
							var stateC=childAccountList[i].state;
							var idC=childAccountList[i].id;
							childHtml+='<tr><td>'+usernameC+'</td>';
							if(identityC==1){
								childHtml+='<td>管理账号</td>';
							}else{
								childHtml+='<td>子账号</td>';
							}
							if(stateC==1){
								childHtml+='<td>停用</td>';
							}else if(stateC==2){
								childHtml+='<td>启用</td>';
							}else if(stateC==3){
								childHtml+='<td>锁定</td>';
							}
							childHtml+='<td>';
							if(stateC==1 || stateC==3){
								childHtml+='<div class="btn_box rs"  title="启用" onclick="enable('+idC+',\''+usernameC+'\',\''+type+'\')"><i class="iCon grey-lock"></i></div>&nbsp;';
							}else{
								childHtml+='<div class="btn_box rs"  title="停用" onclick="disable('+idC+',\''+usernameC+'\',\''+type+'\')"><i class="iCon grey-unlock"></i></div>&nbsp;';
							}
							childHtml+='<div class="btn_box rs" title="查看" onclick="seeChildAccount('+idC+',\''+clientName+'\',\''+usernameC+'\','+clientid+')"><i class="iCon grey-see"></i></div>&nbsp;';
							if(stateC==1){
								childHtml+='<div class="btn_box rs" title="修改"  style="background:#ccc" onclick="modifyChildAccount('+idC+',\''+clientName+'\',\''+usernameC+'\','+clientid+','+stateC+')"><i class="iCon grey-write"></i></div>&nbsp;';
							}else{
								childHtml+='<div class="btn_box rs" title="修改" onclick="modifyChildAccount('+idC+',\''+clientName+'\',\''+usernameC+'\','+clientid+','+stateC+')"><i class="iCon grey-write"></i></div>&nbsp;';
							}
							childHtml+='<div class="btn_box rs" title="重置密码" onclick="resetChildPw('+idC+',\''+usernameC+'\')"><i class="iCon grey-reset"></i></div>';
							childHtml+='</td></tr>';
						}
						html+=childHtml;
					}
					$("#manageAccountWrap").html(html);
				} else {
					$.fn.tipLoddingEnd(false);
					$.fn.tipAlert(data.message,1.5,0);
				}
   		},
   		error:function() {
   			$.fn.tipLoddingEnd(false);
   			$.fn.tipAlert(data.message,1.5,0);
   		}
   	});
}
//获取list
var businessList = [];
function getBusinessList(enterpriseId) {
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/info/ajax/getBusinessList",
		type:'post',
		dataType:'json',
		data: {
   			id: enterpriseId
   		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				businessList = data.result
				ServiceCode(enterpriseId);//关联服务号
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}

//关联服务号
function ServiceCode(enterpriseId){
	$.fn.tipLodding();
	for(var i=0; i<businessList.length;i++) {
		var dhtml = "";
		dhtml+="<div class='managingNumber' id='"+businessList[i].businessCode+"Wrap' style='display: none'>";
		dhtml+="<div class='aTitle'><span>关联"+businessList[i].businessName+"号</span></div>";
		dhtml+="</div>";
		$("#smsFlowWrap").append(dhtml);
		$.ajax({
	   		url : WEB_SERVER_PATH + businessList[i].businessCode+'/basesupport/getServiceCode',
	   		type : "post",
	   		dataType : "json", // 数据类型为json 
	   		data: {
	   			id: enterpriseId
	   		},
	   		async:false,
	   		success:function(data) {
	   			if (data.success) {
	   				$.fn.tipLoddingEnd(true);
	   				var serviceCodeList=data.result;
	   				var html = "";
	   				var ipHtml = "";
					html+="<table class='emtable_table_table'><thead><tr>";
					if (data.result.length > 0) {
						for (var j=0;j<data.result[0].length;j++) {
							html+="<th>"+data.result[0][j].name+"</th>";
						}
						$("#"+businessList[i].businessCode+"Wrap").show();
					}
					html+="</tr></thead><tbody>";
					for (var k=0;k<serviceCodeList.length;k++) {
						html+="<tr>";
						for(var a in serviceCodeList[k]) {
							if(serviceCodeList[k][a].value=="" || serviceCodeList[k][a].value==null){
								html+="<td></td>";
							} else {
								html+="<td class='"+serviceCodeList[k][a].key+"'>"+serviceCodeList[k][a].value+"</td>";
							}
							if (serviceCodeList[k][a].key == 'requestIps' && serviceCodeList[k][a].value!="" && serviceCodeList[k][a].value!=null) {
							ipHtml ="<span class='ellips' style='width:160px;' title='点击查看详情' onclick='seeDetails(this)' _text='"+serviceCodeList[k][a].value+"'>"+serviceCodeList[k][a].value+"</span>";
							}
						}
						/*html+="<td>"+serviceCodeList[k][0].value+"</td>";
						html+="<td>"+serviceCodeList[k][1].value+"</td>";
						html+="<td>"+serviceCodeList[k][2].value+"</td>";
						html+="<td>"+serviceCodeList[k][3].value+"</td>";
						html+="<td>";
						html+="<div class='ellipsBox' style='width:160px;'>";
						if(serviceCodeList[k][4].value=="" || serviceCodeList[k][4].value==null){
							html+="<span class='ellips'></span>";
						}else{
							html+="<span class='ellips' style='width:160px;' title='点击查看详情' onclick='seeDetails(this)' _text='"+serviceCodeList[k][4].value+"'>"+serviceCodeList[k][4].value+"</span>";
						}
						
						html+="</div>";
						html+="</td>";
						html+="<td>"+serviceCodeList[k][5].value+"</td>";*/
						html+="</tr>"
					}
					html+="</tbody></table>";
					$("#"+businessList[i].businessCode+"Wrap").append(html);
					$("#"+businessList[i].businessCode+"Wrap .requestIps").html(ipHtml);
					
		   		} else {
					$.fn.tipLoddingEnd(false);
					$.fn.tipAlert(data.message,1.5,0);
				}
	   		},
	   		error:function() {
	   			$.fn.tipLoddingEnd(false);
	   			$("#"+businessList[i].businessCode+"Wrap").hide();
	   		}
	   	});
	}
}

function createAccount(){
	var clientName=$("#clientName").text();
	var id=$("#detailId").val();
	var state=$("#manageAccountState").val();
	if(state==1){
		$.fn.tipAlert('管理账号已经停用，请先启用管理账号',1.5,0);
		return false;
	}else{
		window.location.href=WEB_SERVER_PATH + '/sys/client/info/to/addAccount?id='+id+'&clientName='+encodeURI(encodeURI(clientName));//跳转到创建子账号
	}
}
/*管理账号*/
function enable(id,user,type){
	var addHtml ="";
 	addHtml = "<div>确定要启用【"+user+"】吗?</div>";
	$.fn.tipOpen({
			title : "启用确认",// 弹框标题
			width : '300',// 弹框内容宽度
			concent :addHtml,//弹框内容
			btn : [ {
		 		label : '确定',
		 		onClickFunction : 'enab('+id+','+type+')'
		 	}]
		});

}
function enab(id,type){
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/info/ajax/on",
		type:'post',
		dataType:'json',
		data:{
			userId:id,
			userType:type,
			enterpriseId: enterpriseId
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('启用成功',1,1);
				window.location.reload();
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
function disable(id,user,type){
	var addHtml ="";
 	addHtml = "<div>确定要停用【"+user+"】吗?</div>";
	$.fn.tipOpen({
			title : "停用确认",// 弹框标题
			width : '300',// 弹框内容宽度
			concent :addHtml,//弹框内容
			btn : [ {
		 		label : '确定',
		 		onClickFunction : 'Dis('+id+','+type+')'
		 	}]
		});

}
function Dis(id,user,type){
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/info/ajax/off",
		type:'post',
		dataType:'json',
		data:{
			userId:id,
			userType:type,
			enterpriseId: enterpriseId
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('停用成功',1,1);
				window.location.reload();
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
function resetPw(id,username){
	var addHtml ="";
 	addHtml = "<div>【"+username+"】确定要重置吗？</div>";
	$.fn.tipOpen({
			title : "重置密码确认",
			width : '300',
			height : '27',
			concent :addHtml,
			btn : [ {
		 		label : '确定',
		 		onClickFunction : 'resetPwS('+id+')'
		 	}]
		});

}
function resetPwS(id){
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/info/ajax/reset",
		type:'post',
		dataType:'json',
		data:{
			userId:id
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				var password=data.result.password;
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
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message, 3, 0);
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});

}
/*子账号*/
function seeChildAccount(id,clientName,username,parentId){
	window.location.href=WEB_SERVER_PATH + '/sys/client/info/to/childAccountInfo?userId='+id+'&clientName='+encodeURI(encodeURI(clientName))+'&userName='+encodeURI(encodeURI(username))+'&parentId='+parentId;//跳转到查看创建子账号
}
function modifyChildAccount(id,clientName,username,parentId,state){
	if(state==1){
		$.fn.tipAlert('子账号停用，不能进行修改',1.5,0);
	}else{
		window.location.href=WEB_SERVER_PATH + '/sys/client/info/to/modifyAccount?userId='+id+'&clientName='+encodeURI(encodeURI(clientName))+'&userName='+encodeURI(encodeURI(username))+'&parentId='+parentId;//跳转到修改子账号
	}

}
function resetChildPw(id,username){
	var addHtml ="";
 	addHtml = "<div>【"+username+"】确定要重置密码吗？</div>";
	$.fn.tipOpen({
			title : "重置密码确认",// 弹框标题
			width : '300',// 弹框内容宽度
			height : '27',
			concent :addHtml,//弹框内容
			btn : [ {
		 		label : '确定',
		 		onClickFunction : 'resetChildPwsave('+id+')'
		 	}]
		});

}
function resetChildPwsave(id){
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/info/ajax/reset",
		type:'post',
		dataType:'json',
		data:{
			userId:id
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				var password=data.result.password;
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
				/*$.fn.tipAlert('操作成功',3,1);*/
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message, 1, 0);
			}

		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});

}
//点击查看详情信息
function seeDetails(obj){
	var text=$(obj).attr("_text");
	$.fn.tipOpen({
		title : '详情',
		width : '500',
		cancel : [{
			label : '关闭',
			onClickFunction : 'seeClose()'
		}],
		tipClose : false,//禁止关闭按钮和点击罩层关闭(默认是true)
		concent : '<div class="seeDetails">'+cleanXss(text)+'</div>'//弹框内容
	});
}
//xss转义
function cleanXss(obj){
	obj = obj.replace(/</g, "&lt;");
	obj = obj.replace(/'/g, "&#39;");
	return obj; 
}
function seeClose(){
	$.fn.tipShut();
}
/*function resetChildPw(id){
	var addHtml ="";
	addHtml +='<form method="post" id="changepw" class="changepw" style="padding-top: 34px;">';
	addHtml +='<span class="tishi">6-16个字母和数字，区分大小写</span>';
	addHtml +='<div class="item">';
	addHtml +='<label class="item-label"><span class="xing">*</span>新密码:</label>';
	addHtml +='<input id="newpass" name="newpass" class="item-text"  type="password">';
	addHtml +='</div>';
	addHtml +='<div class="item">';
	addHtml +='<label class="item-label"><span class="xing">*</span>再次输入新密码:</label>';
	addHtml +='<input id="affirm" name="affirm" class="item-text"  type="password">';
	addHtml +='</div>';
	addHtml +='<div class="tipFoot">';
	addHtml +='<button id="saveBtn" class="tipBtn" type="submit" onclick="resetChildPwSave('+id+')">确 定</button>';
	addHtml +='</div>';
	addHtml +='</form>';
	$.fn.tipOpen({
			title : "设置新密码",// 弹框标题
			width : '600',// 弹框内容宽度
			height : '240',
			cancel:false,
			concent :addHtml//弹框内容
		});

}
function resetChildPwSave(id){
	var $formCheckOut = $('#changepw');
	   var validator= $formCheckOut.validate({
			rules:{
				newpass:{
					required : true,
					newp:true
				},
				affirm:{
					required : true,
					equalTo: "#newpass"
				}
			},
			messages:{
				newpass:{
					required:"请输入新密码",
				},
				affirm:{
					required:"请重新输入新密码",
				}
			},
		submitHandler: function() {
				var affirm= $('#affirm').val();
				$.fn.tipLodding();
				$.ajax({
					url:  WEB_SERVER_PATH + "/sys/client/info/ajax/modifyPassword?randomdata="+Date.parse(new Date()),
					type:'post',
					data:{
						userId : id,
						password : affirm
					},
					dataType:'json',
					success:function(data){
						if(data.success){
							$.fn.tipLoddingEnd(true);
							$.fn.tipAlert('修改成功',1.5,1);
							window.location.reload();
						}else{
							$.fn.tipLoddingEnd(false);
							$.fn.tipAlert(data.message, 1.5, 0);
							validator.resetForm();
						}
					},
					error:function(){
						$.fn.tipLoddingEnd(false);
						$.fn.tipAlert('系统异常',1.5,0);
					}
				})
		    }
	     })
}
*/
