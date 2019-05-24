$(function(){
	getBusinessList();
	var arrSms=[];
	var arrRole=[];
	var arrPlatForm=[];
	$("#platformCode input[type='checkbox']").each(function(j,element){
		var platformCodeval=$(element).val();
		arrPlatForm.push(platformCodeval);
		$("#platformlist input[type='hidden']").each(function(i,ele){
			var platformId=$(ele).val();
			if($.inArray(platformId,arrPlatForm)!=-1){
				$("#platformCode input[value='"+platformId+"']").attr("checked",true);
			}
		});
	});
	$("#smsServiceCode input[type='checkbox']").each(function(j,element){
		var smsService=$(element).val();
		arrSms.push(smsService);
		$("#userAssignlist input[type='hidden']").each(function(i,ele){
			var assignId=$(ele).val();
			if($.inArray(assignId,arrSms)!=-1){
				$("#smsServiceCode input[value='"+assignId+"']").attr("checked",true);
			}
		});
	});
	$("#roleId input[type='checkbox']").each(function(j,element){
		var roleid=$(element).val();
		arrRole.push(roleid);
		$("#userRoleAssignlist input[type='hidden']").each(function(i,ele){
			var roleAssignId=$(ele).val();
			if($.inArray(roleAssignId,arrRole)!=-1){
				$("#roleId input[value='"+roleAssignId+"']").attr("checked",true);
			}	
		});
	});
	
	/*smsGetserver();
	flowGetserver();
	imsGetserver();
	voiceGetserver();*/
})



function ajaxModify(){
	var id=$("#detailId").val();
	var mobile=$("#phone").val();
	var linkman=$("#linkman").val();
	var email=$("#email").val();
	var roleIds="";
	if( mobile==""){
		$.fn.tipAlert('手机号不能为空', 1.5, 0);
		return;
	}
	if(!validate.phone(mobile)) {
		$.fn.tipAlert("手机号格式不正确", 1.5, 0);
		return false;
	}
	
	if(linkman==""){
		$.fn.tipAlert('联系人不能为空', 1.5, 0);
		return;
	}
	var text=/^[\u2E80-\u9FFFa-zA-Z]+$/;
	if (!text.test(linkman)) {
		$.fn.tipAlert("联系人请输入中文或英文 ", 1.5, 0);
		return false;
	}
	$('#roleId').find('input[type=checkbox]:checked').each(function(index,element){
		roleIds += $(element).val()+',';
	});
	if(roleIds==""){
		$.fn.tipAlert('角色不能为空', 1.5, 0);
		return;
	}
	$.fn.tipLodding();
	$.ajax({
			url:  WEB_SERVER_PATH + '/sys/client/info/ajax/modifyAccount',
			type : 'post',
			dataType : 'json',
			data : {
				userId : id,
				roleIds:roleIds,
				email:email,
				linkman:linkman,
				mobile:mobile
			},
			success : function(data) {
				if (data.success) {
					$.fn.tipLoddingEnd(true);
					$.fn.tipAlert('更新角色成功',1.5,1);
					window.location.reload();
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

//获取list
var businessList = [];
function getBusinessList() {
	var enterpriseId = $('#enterpriseId').val();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/info/ajax/getBusinessList",
		type:'post',
		dataType:'json',
		data:{
			id:enterpriseId
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				businessList = data.result
				smsGetserver();//关联服务号
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}

function smsGetserver(){
	var id=$("#detailId").val();//用户Id
	$.fn.tipLodding();
	for(var i=0; i<businessList.length;i++) {
		var dhtml='';
		dhtml += '<div class="fBox" id="'+businessList[i].businessCode+'box" style="display: none">'+
					'<div class="accountInformation childAcc">'+
					'<div class="aTitle"><span>关联'+businessList[i].businessNameSimple+'服务号：</span></div>'+
					'<div class="manageAccount">'+
						'<div class="temple_content" id="'+businessList[i].businessCode+'ServiceCode"></div>'+
					'</div>'+
				'</div><div id="'+businessList[i].businessCode+'platCode"></div>'+
				'<div class="tipFoot smsTipFoot">'+
					'<button type="button" class="tipBtn" id="saveBtn" onclick="smsAjaxModify()">更新</button>'+
				'</div>'+
				'</div>'+
			'</div>'
		$("#allbox").append(dhtml);
		
		
		$.ajax({
			url:  WEB_SERVER_PATH + businessList[i].businessCode + '/basesupport/subaccount/edit/selectServiceCode',
			type : 'post',
			dataType : 'json',
			async:false,
			data : {
				userId : id
			},
			success : function(data) {
				if (data.success) {
					var type = data.result.type;
					$.fn.tipLoddingEnd(false);
					var allEffectiveList=data.result.userServiceCodeAssignMap.allEffectiveList;
					var bindingList=data.result.userServiceCodeAssignMap.bindingList;
					var arr=[];
					var html='';
					for(var k in allEffectiveList){
						arr.push(allEffectiveList[k].id);
						if (allEffectiveList[k].serviceCode == undefined) {
							html+='<label><input value="'+allEffectiveList[k].id+'" type="checkbox" value='+allEffectiveList[k].appId+'>'+allEffectiveList[k].appId+'</label>';
						} else {
							html+='<label><input value="'+allEffectiveList[k].id+'" type="checkbox" value='+allEffectiveList[k].serviceCode+'>'+allEffectiveList[k].serviceCode+'</label>';
						}
					}
					$("#"+ businessList[i].businessCode+"ServiceCode").html(html);
					if(bindingList.length!=0){
						for(var t in bindingList){
							var id=bindingList[t].id;
							if($.inArray(id,arr)!=-1){
								$("#"+ businessList[i].businessCode+"ServiceCode input[value="+id+"]").attr("checked",true);
							}
						}
					}
					var platformCodeList = null;
					if (type == 1) {
						platformCodeList = data.result.userPlatformCodeAssignMap.allEffectiveList
						var platbindingList=data.result.userPlatformCodeAssignMap.bindingList;
						var platarr=[];
						if (platformCodeList.length > 0) {
							var platHtml = '';
							var labelHtml = '';
							platHtml+='<div class="accountInformation childAcc">	'+
									'<div class=""><span>关联平台代码：</span></div>'+
									'<div class="manageAccount">'+
										'<div class="temple_content relation" id="platformCode">'+
											
										'</div>'+
									'</div>'+
								'</div>'
							for(var a in platformCodeList){
								platarr.push(platformCodeList[a].id);
								labelHtml+='<label><input value="'+platformCodeList[a].id+'" type="checkbox">'+platformCodeList[a].platformCode+'</label>'
							}
							$("#"+businessList[i].businessCode+"platCode").html(platHtml);
							$("#platformCode").html(labelHtml);
							if(platbindingList.length!=0){
								for(var t in platbindingList){
									var id=platbindingList[t].id;
									if($.inArray(id,platarr)!=-1){
										$("#platformCode input[value="+id+"]").attr("checked",true);
									}
								}
							}
						}
					}
					
					if((allEffectiveList!=null&&allEffectiveList.length>0)||(platformCodeList!=null&&platformCodeList.length>0)){
						$("#"+businessList[i].businessCode+"box").show();
					}
					
				} else {
					$.fn.tipLoddingEnd(false);
					$.fn.tipAlert(data.message,1.5,0);
				}
			},
			error : function() {
				$.fn.tipLoddingEnd(false);
				$("#"+businessList[i].businessCode+"box").hide();
			}
		});
	}
}
// 更新服务号 平台代码
function smsAjaxModify(){
	var id=$("#detailId").val();
	var parentId=$("#parentId").val();
	$.fn.tipLodding();
	for(var i=0; i<businessList.length;i++) {
		var serviceCodeIds="";
		var platformCodeVals="";
		$('#'+businessList[i].businessCode+'ServiceCode').find('input[type=checkbox]:checked').each(function(index,element){
			serviceCodeIds += $(element).val()+',';
		});
		$('#platformCode').find('input[type=checkbox]:checked').each(function(index,element){
			platformCodeVals += $(element).parent().text()+',';
		});
		serviceCodeIds=serviceCodeIds.substring(0,serviceCodeIds.length-1);
		platformCodeVals=platformCodeVals.substring(0,platformCodeVals.length-1);
			$.ajax({
				url:  WEB_SERVER_PATH + businessList[i].businessCode + '/basesupport/subaccount/edit/modifyServiceCodeDo',
				type : 'post',
				dataType : 'json',
				async:false,
				data : {
					userId : id,
					serviceCodeIds : serviceCodeIds,
					platformCodes : platformCodeVals,
				},
				success : function(data) {
					if (data.success) {
						$.fn.tipLoddingEnd(true);
						$.fn.tipAlert(businessList[i].businessName + '号修改成功',1.5,1);
						window.location.reload();
						//goPage('/sys/client/info/to/detail?id='+parentId);
						
					} else {
						$.fn.tipLoddingEnd(false);
						$.fn.tipAlert(data.message,1.5,0);
					}
				},
				/*error : function() {
					$.fn.tipLoddingEnd(false);
					$.fn.tipAlert('系统异常',1.5,2);
				}*/
			});
	}
}

//三角箭头
function collect(obj) {
	if ($(obj).hasClass('system-on')) {
		$(obj).removeClass('system-on')
		$(obj).next().slideUp();
	} else {
		$(obj).addClass('system-on')
		$(obj).next().slideDown();
	}
}