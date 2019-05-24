$(function(){
//	console.log(11111)
	// 获取已经开通的服务
	var businessList = [];
	function getBusinessList() {
		$.ajax({
			url:  WEB_SERVER_PATH + "/ajax/getBusinessList",
			type:'post',
			dataType:'json',
			data:{},
			success:function(data){
				if (data.success) {
					$.fn.tipLoddingEnd(true);
					businessList = data.result;
					bindService();
				}
			},
			error:function(){
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert('系统异常',1.5,0);
			}
		});
	}
	getBusinessList();
	
//	页面显示
	function bindService() {
		for (var t=0;t<businessList.length;t++) {
			var html = '';
			html+='<dl class="alert-cen">'+
			'<dd class="authority">'+
				'<input type="hidden" />'+
					
				'<div class="role_box check_rolebox" id="serviceCodeBox">'+
					'<div class="role-title light_grey_bg mb30">绑定'+businessList[t].businessNameSimple+'服务号</div>'+
					'<div class="role-cen width99" >'+
						'<div class="roleAdd" id="'+businessList[t].businessCode+'roleAdd"></div>'+
					'</div>'+
				'</div>'+
			'</dd>'+
		'</dl>'
			$('#updateRolebox').append(html);
			$.ajax({
				url : WEB_SERVER_PATH +businessList[t].businessCode+ '/basesupport/account/ajax/getBindServiceCode',
				type : 'post',
				dataType : 'json',
				data : {
					userId : userId
				},
				success : function(data) {
					if (data.success) {
						var dataResult = data.result;
						var dataCode = data.result.code;
						// 绑定服务号
						var bindingList = dataResult.bindingList;
						var serviceCodeHtml = '';
						if (bindingList != "") {
							for (i in bindingList) {
								var serviceCode = bindingList[i].serviceCode;
								var id = bindingList[i].id;
								serviceCodeHtml += '<div class="roleNavFind">';
								serviceCodeHtml += '<label><input class="checkboxdo" type="hidden" value="'
										+ id
										+ '" />'
										+ serviceCode
										+ '</label>';
								serviceCodeHtml += '</div>';
							}
						} else {
							serviceCodeHtml += '<label class="nodata">暂无数据</label>';
						}
						$("#"+dataCode+"roleAdd").html(serviceCodeHtml);
						$("#modifInpVal").val(userId);
						// 绑定平台
						if (dataResult.type == 1) {
							var plathtml = '';
							plathtml+='<div class="role_box check_rolebox" id="platFormCodeBox">'+
								'<div class="role-title light_grey_bg mb30">绑定平台</div>'+
								'<div class="role-cen width99" >'+
									'<div class="roleAdd"></div>'+
								'</div>'+
							'</div>'
						$('#serviceCodeBox').before(plathtml);
							var bindPlatformCodelist = dataResult.bindPlatformCodelist;
							var platHtml = '';
							if (bindPlatformCodelist != "") {
								for (i in bindPlatformCodelist) {
									var platformCode = bindPlatformCodelist[i].platformCode;
									platHtml+='<div class="roleNavFind">';
									platHtml+='<label><input class="checkboxdo" type="hidden" value="'+platformCode+'" />'+platformCode+'</label>';
									platHtml+='</div>';
								}
							} else {
								platCodeHtml += '<label class="nodata">暂无数据</label>';
							}
							$("#platFormCodeBox .roleAdd").html(platHtml);
						} else {
							$("#platFormCodeBox").hide();
						}

					} else {
						$.fn.tipAlert(data.message, 1.5, 0);
						$(".role-add").attr("onclick", "");
					}
				}

			});
		}
	}
	
});
//三角箭头
function role(obj) {
	if ($(obj).hasClass('role-on')) {
		$(obj).removeClass('role-on')
		$(obj).parent().find(".role_box").slideUp();
	} else {
		$(obj).addClass('role-on')
		$(obj).parent().find(".role_box").slideDown();
	}

}