function showItem(){
	var flag = false;
	if ($("#businessType").val() == "" && $("#saveType").val() == "" && $("#businessTypeId").val() == "") {
		flag = true;
	} else if ($("#businessType").val() != "" && $("#saveType").val() != "" && $("#businessTypeId").val() != ""){
		flag = true;
	} else {
		flag = false;
	}
	if (!flag) {
		$.fn.tipAlert('请统一选择业务类型，保存类型，内容类别',1.5,0);
	} else {
		window['emTableConfig'].ajaxConfig.data.serviceCode=$("#serviceCode").val();
		window['emTableConfig'].ajaxConfig.data.appId=$("#appId").val();
		window['emTableConfig'].ajaxConfig.data.saveType=$("#saveType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.businessTypeId=$("#businessTypeId option:selected").val();
		window['emTableConfig'].ajaxConfig.data.businessType=$("#businessType option:selected").val(); 
		emTable('emTableConfig');
		$("#serviceCode").val(window['emTableConfig'].ajaxConfig.data.serviceCode); 
		$("#appId").val(window['emTableConfig'].ajaxConfig.data.appId); 
		$("#businessType").val(window['emTableConfig'].ajaxConfig.data.businessType);
		$("#saveType").val(window['emTableConfig'].ajaxConfig.data.saveType); 
		$("#businessTypeId").val(window['emTableConfig'].ajaxConfig.data.businessTypeId); 
	}
}
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true,
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [ {
			label : '服务号名称',
			id : 'serviceCode',
			type : 'input'
		},{
			label : 'APPID',
			id : 'appId',
			type : 'input'
		},{
			isShow : true,
			label : '业务类型',
			id : 'businessType',
			type :"include"
		},{
			label : '<span class="searchspan">保存类型</span>',
			id : 'saveType',
			type : 'include',
		},{
			isShow : true,
			label : '内容类型',
			id : 'businessTypeId',
			type :"include"
		}],
		buttonItems : [{
			isShow: true,
			label : '查询',
			id : 'addBtn',
			onClickFunction : 'showItem()'
		}],
		searchButton : false,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/fms/client/servicecode/ajax/list',
		method : 'POST',
		data : {
			serviceCode : '#serviceCode',
			appId : '#appId',
			savetype : '#savetype',
			businessType : '#businessType',
			businessTypeId : '#businessTypeId',
			
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
			title : "服务号ID",
			width : "5%",
			context : "@{id}"
		},{
			isShow : true,
			title : "服务号名称",
			width : "10%",
			align :'left',
			context : '@{serviceCode}'
		},{
			isShow : true,
			title : "保存类型",
			width : "9%",
			selectors : [ {
				isShow : true,
				term : "@{saveType}",
				select : [ {
					value : 1,
					context : "可保存"
				}, {
					value : 2,
					context : "不可保存"
				}]
			} ]
			
		},{
			isShow : true,
			title : "业务类型",
			width : "9%",
			context : "@{businessType}"
			
		},{
			isShow : true,
			title : "内容类别",
			width : "9%",
			context : "@{contentType}"
			
		},{
			isShow : true,
			title : "APPID",
			width : "9%",
			context : "@{appId}"
			
		},{
			isShow : true,
			title : "秘钥",
			width : "9%",
			context : "@{secretKey}"
			
		},{
			//0-激活，1-停用
			isShow : true,
			title : "状态",
			width : "9%",
			selectors : [ {
				isShow : true,
				term : "@{state}",
				select : [ {
					value : 0,
					context : "启用"
				}, {
					value : 1,
					context : "停用"
				}]
			} ]
		},{
			width : "8%",
			title : "操作",
			selectors : [
				{
					isShow : true, //hasAuth("OPER_SMS_SERVICECODE_PRICE_UPDATE")==true
					term : "@{id}",
					select : [ {
						value :"@{id}",
						context :  "<div class='btn_box rs' alt='编辑服务号名称' title='编辑服务号名称' onClick='modifyConfirm(\"@{id}\")'><i class='iCon grey-write'></i></div>&nbsp;"
					} ]
				}]
		}]
	}
}
emTable('emTableConfig');


//编辑服务号名称
function modifyConfirm(id){
	var html='<div class="tipcen">';
	html+='<div class="item"><label class="item-label"><span class="xing">*</span>服务号：</label>';
	html+='<input type="text" id="serviceCodeName"   class="item-text" name="serviceCodeName"  /> </div>';
	html+='</div>';
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/client/servicecode/ajax/info',
		type : 'post',
		dataType : 'json',
		data : {
			id : id
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				var serviceCodeName=data.result.serviceCode;
				$.fn.tipOpen({
					title : "编辑服务号名称",
					width : '400',
					concent :html,
					btn : [ {
				 		label : '确定',
				 		onClickFunction : 'modifyTure('+id+')'
				 	}]				
				});
				$("#serviceCodeName").val(serviceCodeName);
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
function addCancel(){
	$.fn.tipShut();
}
//编辑确定事件
function modifyTure(id){
	var serviceCodeName=$("#serviceCodeName").val();
	$.fn.tipLodding();
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/client/servicecode/ajax/update',
		type : 'post',
		dataType : 'json',
		data : {
			id: id,
			serviceCode : serviceCodeName
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('服务号名称编辑成功',1,1);
				var limit=$('.limitSelect').val();
				emrefulsh("emTableConfig", 0, limit);
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error : function() {
			$.fn.tipLoddingEnd(false);
			validateor.resetForm();
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
}
