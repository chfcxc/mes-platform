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
		window['emTableConfig'].ajaxConfig.data.startTime=$("#startTime").val(); 
		window['emTableConfig'].ajaxConfig.data.endTime=$("#endTime").val(); 
		window['emTableConfig'].ajaxConfig.data.title=$("#title").val();
		window['emTableConfig'].ajaxConfig.data.content=$("#content").val();
		window['emTableConfig'].ajaxConfig.data.businessType=$("#businessType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.saveType=$("#saveType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.contentTypeId=$("#businessTypeId option:selected").val();
		window['emTableConfig'].ajaxConfig.data.messageType=$("#messageType option:selected").val(); 
		window['emTableConfig'].ajaxConfig.data.submitType=$("#submitType option:selected").val(); 
		emTable('emTableConfig');
		$("#startTime").val(window['emTableConfig'].ajaxConfig.data.startTime);
		$("#endTime").val(window['emTableConfig'].ajaxConfig.data.endTime); 
		$("#title").val(window['emTableConfig'].ajaxConfig.data.title); 
		$("#content").val(window['emTableConfig'].ajaxConfig.data.content); 
		$("#businessType").val(window['emTableConfig'].ajaxConfig.data.businessType);
		$("#saveType").val(window['emTableConfig'].ajaxConfig.data.saveType); 
		$("#businessTypeId").val(window['emTableConfig'].ajaxConfig.data.contentTypeId); 
		$("#messageType").val(window['emTableConfig'].ajaxConfig.data.messageType); 
		$("#submitType").val(window['emTableConfig'].ajaxConfig.data.submitType);
	}
}

var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true,
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [ {
			label : '模板名称',
			id : 'title',
			type : 'input'
		},
		{
	    	  isShow : true,
	          label : '提交时间',
	          id : 'startTime',
	          type : 'date',// 时间，
		      dateFmt:'yyyy-MM-dd HH:mm:ss',
		      startDate:'%y-%M-%d 00:00:00',
		      maxDateId : 'endTime'   
		},{
			label : '&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;&nbsp;',
			id : 'endTime',
			type : 'date',// 时间，
	        dateFmt:'yyyy-MM-dd HH:mm:ss',
	        startDate:'%y-%M-%d 23:59:59',
	        minDateId : 'startTime'
		 },{
			label : '闪推内容',
			id : 'content',
			type : 'input'
		},{
			isShow : true,
			label : '业务类型',
			id : 'businessType',
			type :"include"
		},{
			isShow : true,
			label : '<span class="searchspan">保存类型</span>',
			id : 'saveType',
			type : 'include'
		},{
			isShow : true,
			label : '内容类型',
			id : 'businessTypeId',
			type :"include"
		},{
			label : '<span class="searchspan">信息类型</span>',
			id : 'messageType',
			type : 'select',
			options : [ {
				context : '--请选择--',
				value : ''
			}, {
				context : '普通',
				value : '0'  
			}, {
				context : '个性',
				value : '1'
			}]
		},{
			label : '<span class="searchspan">提交方式</span>',
			id : 'submitType',
			type : 'select',
			options : [ {
				context : '--请选择--',
				value : ''
			}, {
				context : '接口',
				value : '1'  
			}, {
				context : '页面',
				value : '2'
			}]
		}
		],
		buttonItems : [ {
			isShow: true,
			label : '查询',
			id : 'addBtn',
			onClickFunction : 'showItem()'
		}],
		searchButton : false,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/fms/reported/query/list',
		method : 'POST',
		data : {
			title:'#title',
			content:'#content',
			serviceCodeId:'#serviceCodeId',
			saveType:'#saveType',
			messageType:'#messageType',
			submitType:'#submitType',
			startTime:'#startTime',
			endTime:'#endTime',
			contentTypeId:'#businessTypeId',
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
			title : "模板ID",
			width : "7%",
			context : "@{templateId}"
		},{
			isShow : true,
			title : "服务号ID",
			width : "6%",
			context : "@{serviceCodeId}"
		},{
			isShow : true,
			title : "AppId",
			width : "9%",
			context : '@{appId}',
		},{
			isShow : true,
			title : "模板名称",
			width : "6%",
			align:'left',
			context : '@{templateName}' //'<a href="jacascript:;">@{templateName}</a>'
		},{
			isShow : true,
			title : "报备内容",
			width : "7%",
			align:'left',
			context : '@{content}'
		},{
			isShow : true,
			title : "业务类型",
			align:'left',
			width : "6%",
			context : '@{businessType}'
		},{
			isShow : true,
			title : "保存类型",
			width : "6%",
			selectors : [ {
				isShow : true,
				term : "@{saveType}",
				select : [ {
					value : 1,
					context : "可以保存"
				}, {
					value : 2,
					context : "不可保存"
				}]
			} ]
		},{
			isShow : true,
			title : "内容类别",
			width : "6%",
			align:'left',
			context : '@{contentType}'
		},{
			isShow : true,
			title : "信息类型",
			width : "5%",
			selectors : [ {
				isShow : true,
				term : "@{templateType}",
				select : [ {
					value : 0,
					context : "普通"
				}, {
					value : 1,
					context : "个性"
				}]
			} ]
		},{
			isShow : true,
			title : "提交方式",
			width : "5%",
			selectors : [ {
				isShow : true,
				term : "@{sendType}",
				select : [ {
					value : 2,
					context : "页面"
				}, {
					value : 1,
					context : "接口"
				}]
			} ]
		},{
			isShow : true,
			title : "提交时间",
			width : "7%",
			context : "@{submitTime}"
			
		},{
			isShow : true,
			title : "报备通道",
			width : "7%",
			align:'left',
			context : '<div><p>移动：@{cmccChannelName}</p><p>联通：@{cuccChannelName}</p><p>电信：@{ctccChannelName}</p></div>',
		},{
			isShow : true,
			title : "审核结果",
			width : "6%",
			align:'left',
			context : '<div><p>移动：@{cmccAuditStateDesc}</p><p>联通：@{cuccAuditStateDesc}</p><p>电信：@{ctccAuditStateDesc}</p></div>',
		}]
	}
}
emTable('emTableConfig');
