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
		window['emTableConfig'].ajaxConfig.data.appId=$("#appId").val(); 
		window['emTableConfig'].ajaxConfig.data.businessType=$("#businessType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.saveType=$("#saveType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.businessTypeId=$("#businessTypeId option:selected").val();
		emTable('emTableConfig');
		$("#serviceCodeId").val(window['emTableConfig'].ajaxConfig.data.serviceCodeId);
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
			label : '服务号ID',
			id : 'serviceCodeId',
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
			//1不可保存2可以保存
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
		url : WEB_SERVER_PATH + '/fms/client/account/ajax/list',
		method : 'POST',
		data : {
			appId:'#appId',
			serviceCodeId:'#serviceCodeId',
			saveType:'#saveType',
			businessTypeId:'#businessTypeId',
			businessType:'#businessType',
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
			context : "@{serviceCodeId}"
		},{
			isShow : true,
			title : "服务号名称",
			width : "10%",
			align :'left',
			context : '@{serviceCode}'
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
			title : "余量（条）",
			width : "9%",
			context : "@{balance}"
			
		},{
			isShow : true,
			title : "备注",
			width : "9%",
			context : "@{remark}"
		}]
	}
}
emTable('emTableConfig');
