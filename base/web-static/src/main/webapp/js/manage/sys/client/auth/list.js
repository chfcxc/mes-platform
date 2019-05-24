$(function(){
	clientAuthInfoList();
	var authLevel=$("#typesss").val();
	if(authLevel=="0" || authLevel==""){
		$("#card_base").addClass("li-on");
		$("#card_pro,#card_premium").removeClass("li-on");
	}else if(authLevel=="1"){
		$("#card_pro").addClass("li-on");
		$("#card_base,#card_premium").removeClass("li-on");
	}else{
		$("#card_premium").addClass("li-on");
		$("#card_base,#card_pro").removeClass("li-on");
	}
});
function clientAuthInfoList(){
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/auth/ajax/info",
		type:'post',
		dataType:'json',
		data : {
			authLevel :$("#typesss").val()
		},
		success:function(data){	
			if(data.success){
				var roleauth=data.result; //客户权限信息
				var arr=[];
				for(var i in roleauth){
					var resourceId = roleauth[i];
					arr.push(resourceId);
				}
				$.ajax({
					url:  WEB_SERVER_PATH + "/sys/client/auth/ajax/authTree",
					type:'post',
					dataType:'json',
					data : {
						authLevel :$("#typesss").val()
					},
					success:function(data){	
						var html='<div class="clear role_div" id="role_div">';
						html +='<table class="alert-cen">';
						var auth = data.result.authTree;
						var ngvs = auth.ngvs;
						for(var i in ngvs){ 
							var ngv = ngvs[i]; 
							var ngvId = ngv.id;
							var ngvName = ngv.resourceName; 
							var ngvCode = ngv.resourceCode;
							html +='<thead class="role-title role-on light_blue_bg"><tr><td colspan="3" class="tdTitle" >'+ngvName+'<input type="hidden" id="'+ngvId+'" code="'+ngvCode+'" /></td></tr></thead>';
							html +='<tbody><tr class="role-cen authDivBox"><td class="leftDiv">一级</td><td class="centerDiv">二级</td><td class="rightDiv">权限配置细则</td></tr>';
							var pagesNew=ngv.resource;	
							for(var j in pagesNew){ //一级菜单
								var page = pagesNew[j];
								var pageIdSecond = page.id;
								var pageNameSecond = page.resourceName;
								var pageCodeSecond = page.resourceCode;
								var pages=page.resource;
								html += "<tr class='roleAdd'>"; 
								html += "<td class='roleNav roleLeft' rowspan='"+pages.length+"' _pcode='"+pageCodeSecond+"'>"+pageNameSecond+"</label></td>" ;  
								for(var k in pages){  //二级菜单
									var lastPages=pages[k];
									var resourceName=pages[k].resourceName;
									var resourceId=pages[k].id;
									var resourceCode=pages[k].resourceCode;
									if(k>0){
										html+='<tr>';
									}
									html+='<td class="roleCenter">';
									html+='<div class="roleNavFind">';
									html+='<label><input type="checkbox" class="checkboxdo" onclick="checkedParent(this,'+resourceId+')" stype="page" _childCode="'+resourceCode+'" id="'+resourceId+'" stype="page"';	
									if($.inArray(resourceCode, arr)!=-1){
										html += ' checked="checked" ';
									}
									html += '>'+resourceName+'';
									html += '</label>';
									html+='</div>';
									html+='</td><td class="roleRight">'; //加第三列内容
									if(resourceCode=="SMS_CLIENT_PROSPECTUS"){  //短信概览
										html+='<label><input type="checkbox" parentid="'+resourceId+'" onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_SMS_SEND_NUMBER" stype="page">短信发送量</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'" onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_SMS_SEND_SUCCESS_RATE" stype="page">短信发送成功率</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_VOICE_SEND_NUMBER" stype="page">语音发送量</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_VOICE_SEND_SUCCESS_RATE" stype="page">语音发送成功率</label>';
										
									}
									if(resourceCode=="SMS_CLIENT_MESSAGE_RESULT"){  //发送查询
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_SEND_STATE" stype="page">发送状态</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_REPORT" stype="page">状态报告</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_STATE_CODE" stype="page">状态码</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_FAIL_REASON" stype="page">失败原因</label>';
										
									}
									if(resourceCode=="SMS_CLIENT_ES_MESSAGE_RESULT"){  //发送实时查询
										html+='<label><input type="checkbox" parentid="'+resourceId+'"   onclick="checkedPage(this,'+resourceId+')" _lastCode="COLUMN_ES_SEND_STATE" stype="page">发送状态</label>';
										html+='<label><input type="checkbox"  parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')" _lastCode="COLUMN_ES_REPORT" stype="page">状态报告</label>';
										html+='<label><input type="checkbox"  parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')" _lastCode="COLUMN_ES_STATE_CODE" stype="page">状态码</label>';
										html+='<label><input type="checkbox"  parentid="'+resourceId+'" onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_ES_FAIL_REASON" stype="page">失败原因</label>';
										
									}
									if(resourceCode=="SMS_CLIENT_SEND_STSTISTICS"){  //短信发送报表
										html+='<label><input type="checkbox"  parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')" _lastCode="COLUMN_SMS_STATISTICS_SUBMIT_TOTAL" stype="page">提交总数</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_SMS_STATISTICS_SPLIT_TOTAL" stype="page">拆分总数</label>';
										html+='<label><input type="checkbox"  parentid="'+resourceId+'" onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_SMS_STATISTICS_SUCCESS_NUMBER" stype="page">发送成功数</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"   onclick="checkedPage(this,'+resourceId+')" _lastCode="COLUMN_SMS_STATISTICS_TIMEOUT_NUMBER" stype="page">发送超时数</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_SMS_STATISTICS_SUCCESS_RATE" stype="page">发送成功率</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_SMS_STATISTICS_FAIL_RATE" stype="page">发送失败率</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_SMS_STATISTICS_TIMEOUT_RATE" stype="page">超时率</label>';
										
									}	
									if(resourceCode=="SMS_CLIENT_SEND_VOICE_STSTISTICS"){  //语音发送报表
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_VOICE_STATISTICS_SUBMIT_TOTAL" stype="page">提交总数</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_VOICE_STATISTICS_SUCCESS_NUMBER" stype="page">发送成功数</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_VOICE_STATISTICS_TIMEOUT_NUMBER" stype="page">发送超时数</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_VOICE_STATISTICS_SUCCESS_RATE" stype="page">发送成功率</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_VOICE_STATISTICS_FAIL_RATE" stype="page">发送失败率</label>';
										html+='<label><input type="checkbox" parentid="'+resourceId+'"  onclick="checkedPage(this,'+resourceId+')"  _lastCode="COLUMN_VOICE_STATISTICS_TIMEOUT_RATE" stype="page">超时率</label>';
										
									}	
									html+='</td></tr>';//加第三列内容结束
								}
								
						}
							html += "</tbody>";
					}
						html += '</table>';
						html+='<div class="tipFoot">';
				        html+='<button type="button" class="tipBtn" id="saveBtn" onclick="ajaxmodify()">更 新</button>';
						html +='</div>';
						$("#clientAuthBox").html(html);
						//三级菜单回显
						var lastResourceCode="";
						$("#role_div table tbody tr td.roleRight").find("input[type='checkbox']").each(function(index,ele){
							lastResourceCode=$(ele).attr("_lastCode");
							if($.inArray(lastResourceCode, arr)!=-1){
								$(ele).attr("checked",true);
							}
						});
						
					},
					error:function(){
						$.fn.tipAlert('系统异常',1.5,0);
					}
				});
			}else{
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
}
//更新
function ajaxmodify(){
	var authLevel = $("#typesss").val(); //版本
	var menuParams=""; //二级菜单
	var columnParams="";//三级菜单
	$("#role_div table tbody tr td.roleCenter").find("input[type='checkbox']:checked").each(function(index,obj){
		menuParams+=$(obj).attr("_childcode")+",";
	});
	$("#role_div table tbody tr td.roleRight").find("input[type='checkbox']:checked").each(function(index,obj){
		columnParams+=$(obj).parent().parent().parent().find("td.roleCenter input[type='checkbox']").attr("_childcode")+"#"+$(obj).attr("_lastcode")+",";
	});
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + '/sys/client/auth/ajax/modify',
		type : 'post',
		dataType : 'json',
		data : {
			authLevel : authLevel,
			menuParams : menuParams, //二级菜单的code用逗号拼接
			columnParams : columnParams //二级菜单的code#三级菜单的code,二级菜单的code#三级菜单的 
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('客户权限更新成功',1.5,1);
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
