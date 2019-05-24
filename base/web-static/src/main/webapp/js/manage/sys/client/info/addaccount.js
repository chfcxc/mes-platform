$(function(){
	var relLen=$(".relation label").length;
	if(relLen>40){
		$(".relation").css({"height": "148px","overflow": "auto" });
	}
	getBusinessList();
	

})
function ajaxAdd(){
	var id=$("#detailId").val();
	var userName=$("#userName").val();
	var mobile=$("#phone").val();
	var linkman=$("#linkman").val();
	var email=$("#email").val();
	/*var smsServiceCodeIds="";*/
	var roleIds="";
	/*var platformCodeVals="";
	$('#smsServiceCode').find('input[type=checkbox]:checked').each(function(index,element){
		smsServiceCodeIds += $(element).val()+',';
	});*/
	$('#roleId').find('input[type=checkbox]:checked').each(function(index,element){
		roleIds += $(element).val()+',';
	});
	/*$('#platformCode').find('input[type=checkbox]:checked').each(function(index,element){
		platformCodeVals += $(element).parent().text()+',';
	});*/
	if( userName==""){
		$.fn.tipAlert('用户名不能为空', 1.5, 0);
		return;
	}
	var userzz=/^[a-zA-Z][a-zA-Z0-9]{3,15}$/;
	if (!userzz.test(userName)) {
		$.fn.tipAlert("用户名请输入4-16位英文字母和数字，首位不能为数字", 1.5, 0);
		return false;
	}
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
	if(roleIds==""){
		$.fn.tipAlert('角色不能为空', 1.5, 0);
		return;
	}
	$.fn.tipLodding();
	$.ajax({
			url:  WEB_SERVER_PATH + '/sys/client/info/ajax/addAccount',
			type : 'post',
			dataType : 'json',
			async:false,
			data : {
				id : id,
				userName : userName,
			/*	smsServiceCodeIds:smsServiceCodeIds,
				platformCodes : platformCodeVals,*/
				roleIds:roleIds,
				email:email,
				linkman:linkman,
				mobile:mobile
			},
			success : function(data) {
				if (data.success) {
					$.fn.tipLoddingEnd(true);
					/*$.fn.tipAlert('子帐号创建成功',1.5,1);*/
					var password=data.result.password;
					var userId=data.result.userId;
					$("#userId").val(userId);
					var html ="";
					html = "<div>生成的随机6位密码是："+password+"</div>";
					html+='<div class="tipFoot tipFootMp20">';
					html+='<button onclick="seeClosec('+id+')" type="button" class="tipBtn">关 闭</button>';
					html+='</div>';
					$.fn.tipOpen({
						title : "密码提示",
						width : '300',
						concent :html,
						btn : []				
					});
					
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
function seeClosec(id){
	$.fn.tipShut();
	/*goPage('/sys/client/info/to/detail?id='+id);*/
	//window.location.reload();
	$(".baseDetail .tipFoot").css("display","none");
	$(".secand").css("display","block");
}
//获取list
var businessList = [];
function getBusinessList() {
	var id=$("#detailId").val();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/info/ajax/getBusinessList",
		type:'post',
		dataType:'json',
		data:{
			id : id
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
// 关联服务
function smsGetserver(){
	var userId=$("#userId").val();
	var id=$("#detailId").val();//企业id
	$.fn.tipLodding();
	for(var i=0; i<businessList.length;i++) {
		var dhtml='';
		dhtml += '<div class="sBox" id="'+businessList[i].businessCode+'box"  style="display: none">'+
					'<div class="accountInformation childAcc">'+
					'<div class="aTitle system-on" onclick="collect(this)"><span>关联'+businessList[i].businessNameSimple+'服务号：</span><i></i></div>'+
//					'<div class=""><span>关联'+businessList[i].businessNameSimple+'服务号：</span></div>'+
					'<div class="manageAccount">'+
						'<div id="'+businessList[i].businessCode+'ServiceCode" class="temple_content"></div>'+
					'</div>'+
				'</div><div id="'+businessList[i].businessCode+'platCode"></div>'+
				'<div class="tipFoot secand smsTipFoot">'+
					'<button type="button" class="tipBtn" id="saveBtn" onclick="smsAjaxAdd()">保 存</button>'+
				'</div>'+
			'</div>'
		$('#wrapBox').append(dhtml);
		
		$.ajax({
			url:  WEB_SERVER_PATH + businessList[i].businessCode+'/basesupport/subaccount/serviceCode',
			type : 'post',
			dataType : 'json',
			cache:false,
			async:false,
			data : {
				id : id
			},
			success : function(data) {
				if (data.success) {
					var type = data.result.type;
					$.fn.tipLoddingEnd(false);
					var smsServiceCodeList=data.result.serviceCodeList;
					var html='';
					for(var k in smsServiceCodeList){
						html+='<label><input value="'+smsServiceCodeList[k].id+'" type="checkbox">'+smsServiceCodeList[k].serviceCode+'</label>'
					}
					$("#"+businessList[i].businessCode+"ServiceCode").append(html);
					var platformCodeList=null
					if (type == 1) {
						platformCodeList = data.result.platformCodeList;
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
								console.log(platformCodeList[a].platformCode)
								labelHtml+='<label><input value="'+platformCodeList[a].id+'" type="checkbox">'+platformCodeList[a].platformCode+'</label>'
							}
							$("#"+businessList[i].businessCode+"platCode").html(platHtml);
							$("#platformCode").html(labelHtml);
						}
					}
					if((smsServiceCodeList!=null&&smsServiceCodeList.length>0)||(platformCodeList!=null&&platformCodeList.length>0)){
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

function smsAjaxAdd(){
	var userId=$("#userId").val();
	var id=$("#detailId").val();//企业id
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
			url:  WEB_SERVER_PATH + businessList[i].businessCode+ '/basesupport/subaccount/assignServiceCodeDo',
			type : 'post',
			dataType : 'json',
			async:false,
			data : {
				userId : userId,
				enterpriseId : id,//企业id
				serviceCodeIds : serviceCodeIds,
				platformCodes : platformCodeVals,
			},
			success : function(data) {
				if (data.success) {
					$.fn.tipLoddingEnd(true);
					$.fn.tipAlert(businessList[i].businessName + '号添加成功',1.5,1);
					//window.location.reload();
					goPage('/sys/client/info/to/detail?id='+id);
					
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