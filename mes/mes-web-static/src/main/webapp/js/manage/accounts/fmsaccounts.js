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
		window['emTableConfig'].ajaxConfig.data.serviceCodeId=$("#serviceCodeId").val();
		window['emTableConfig'].ajaxConfig.data.enterpriseName=$("#enterpriseName").val(); 
		window['emTableConfig'].ajaxConfig.data.appId=$("#appId").val();
		window['emTableConfig'].ajaxConfig.data.businessType=$("#businessType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.saveType=$("#saveType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.businessTypeId=$("#businessTypeId option:selected").val();
		emTable('emTableConfig');
		$("#serviceCodeId").val(window['emTableConfig'].ajaxConfig.data.serviceCodeId);
		$("#enterpriseName").val(window['emTableConfig'].ajaxConfig.data.enterpriseName);
		$("#appId").val(window['emTableConfig'].ajaxConfig.data.appId);
		$("#businessTypeId").val(window['emTableConfig'].ajaxConfig.data.businessTypeId);
		$("#businessType").val(window['emTableConfig'].ajaxConfig.data.businessType);
		$("#saveType").val(window['emTableConfig'].ajaxConfig.data.saveType);
	}
}
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true,
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [{
			isShow : true,
			label : '客户名称',
			id : 'enterpriseName',
			type : 'input'
		},{
			isShow : true,
			label : '服务号Id',
			id : 'serviceCodeId',
			type : 'input'
		},{
			isShow : true,
			label : 'APPID',
			id : 'appId',
			type : 'input'
		},{
			isShow : true,
			label : '业务类型',
			id : 'businessType',
			type :"include"
		},{
			isShow : true,
			label : '保存类型',
			id : 'saveType',
			type : 'include'
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
		url : WEB_SERVER_PATH + '/fms/account/balance/ajax/list',
		method : 'POST',
		data : {
			appId:'#appId',
			serviceCodeId:'#serviceCodeId',
			saveType:'#saveType',
			businessType:'#businessType',
			businessTypeId:'#businessTypeId',
			enterpriseName : '#enterpriseName'
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
				title : "客户名称",
				width : "9%",
				align :'left',
				context : "@{enterpriseName}"
			},{
				isShow : true,
				title : "服务号ID",
				width : "9%",
				context : "@{serviceCodeId}"
			},{
				isShow : true,
				title : "服务号名称",
				width : "9%",
				context : "@{serviceCode}"
			},{
				isShow : true,
				title : "业务类型",
				width : "9%",
				context : "@{businessType}"
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
				title : "内容类型",
				width : "9%",
				context : "@{contentType}"
			},{
					isShow : true,
					title : "APPID",
					width : "9%",
					context : "@{appId}"
				},{
					isShow : true,
					title : "余量(条)",
					width : "9%",
					context : "@{balance}"
				},{
					isShow : true,
					title : "备注",
					width : "12%",
					context : "@{remark}"
				},{
					isShow : true,
		        	width : "9%",
					title : "操作",
					 context :
		            	 "<div class='btn_box rs' alt='操作' title='操作' onClick='modify(\"@{id}\",\"@{serviceCodeId}\",\"@{serviceCode}\")'><i class='iCon grey-write'></i></div>"
		        }]
	}
}
emTable('emTableConfig');
function modify(id,serviceCodeId,serviceCode){
	var html='<div class="businesstype serviceCode" ><label><span class="xing">*</span> 服务号ID：</label><input type="text"  value='+serviceCodeId+' disabled="disabled" /></div>';
	html+='<div class="businesstype serviceCode" ><label><span class="xing">*</span> 服务号名称：</label><input type="text" value='+serviceCode+' disabled="disabled" /></div>';
	html+='<div class="businesstype" ><label><span class="xing">*</span>操作类型：</label>';
	html+='<select id="operationType"><option value="0">充值</option><option value="1">扣费</option><option value="2">补款</option></select>';
	html+='</div>';
	html+='<div class="businesstype" ><label><span class="xing">*</span> 额度（条）：</label><input type="text" id="balance" /></div>';
	html+='<div class="businesstype" ><label><span class="xing">*</span> 备注：</label><textarea id="remark" ></textarea></div>';
	$.fn.tipOpen({
		title : "操作类型",
		width : '300',
//		height : '150',
		concent :html,
		btn : [ {
	 		label : '确定',
	 		onClickFunction : 'contantd('+id+')'
	 	}]				
	});
}
function contantd(id){
	var type=$("#operationType").val();
	var balance=$("#balance").val();
	var remark=$("#remark").val();
	if(balance=='' || balance==null){
		$.fn.tipAlert('请添写额度',1,0);
		return false;
	}
	$.fn.tipLodding();
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/account/balance/ajax/operation',
		type : 'post',
		dataType : 'json',
		data : {
			type:type,
			balance:balance,
			remark:remark,
			id:id
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('操作成功',1,1);
				var limit=$('.limitSelect').val();
	        	emrefulsh("emTableConfig", 0, limit);
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
