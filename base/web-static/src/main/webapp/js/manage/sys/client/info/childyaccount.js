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

//获取list
var businessList = [];
function getBusinessList() {
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
		var dhtml = '';
		dhtml += '<div class="accountInformation childAcc" id="'+businessList[i].businessCode+'box"  style="display: none">'+
		'<div class="aTitle"><span>关联'+businessList[i].businessNameSimple+'服务号：</span></div>'+
		'<div class="manageAccount">'+
			'<div class="temple_content" id="'+businessList[i].businessCode+'ServiceCode">'+
				
			'</div>'+
			'</div>'+
		'</div><div id="'+businessList[i].businessCode+'platCode"></div>'
		
		$('#allbox').append(dhtml);
	$.ajax({
			url:  WEB_SERVER_PATH + businessList[i].businessCode +'/basesupport/info/getBindServiceCode',
			type : 'post',
			dataType : 'json',
			async:false,
			data : {
				userId : id
			},
			success : function(data) {
				if (data.success) {
					$.fn.tipLoddingEnd(false);
					var type = data.result.type
					var bindingList=data.result.bindingList;
					var html='';
					if(bindingList.length!=0){
						for(var t in bindingList){
							html+='<label>'+bindingList[t].serviceCode+'</label>';
						}
					}
					$("#"+ businessList[i].businessCode+"ServiceCode").html(html);
					var platformCodeList = null;
					if (type == 1) {
						var platformCodeList = data.result.bindPlatformCodelist
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
								labelHtml+='<label>'+platformCodeList[a].platformCode+'</label>'
							}
							$("#"+businessList[i].businessCode+"platCode").html(platHtml);
							$("#platformCode").html(labelHtml);
						}
					}
					
					if((bindingList!=null&&bindingList.length>0)||(platformCodeList!=null&&platformCodeList.length>0)){
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
