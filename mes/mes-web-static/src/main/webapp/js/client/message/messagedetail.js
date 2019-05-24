/*$(function() {
	defaultDayTime("startTime");
});
//报表默认当天时间
function defaultDayTime(str, type) {
	$("#outerId_search_form #" + str + "").val(getNewDay());
	window['emTableConfig'].ajaxConfig.data.startTime = getNewDay();
	emTable('emTableConfig');
	$("#outerId_search_form #" + str + "").val(getNewDay());
}
//获取当前时间
function getNewDay() {
	var nowtime = new Date()
    var year = nowtime.getFullYear();
    var month = padleft0(nowtime.getMonth() + 1);
    var day = padleft0(nowtime.getDate());
    return year + "-" + month + "-" + day + " " + "00:00:00";

}*/
function showItem(){
	var flag = false;
	if ($("#businessType").val() == "" && $("#saveType").val() == "" && $("#contentType").val() == "") {
		flag = true;
	} else if ($("#businessType").val() != "" && $("#saveType").val() != "" && $("#contentType").val() != ""){
		flag = true;
	} else {
		flag = false;
	}
	if (!flag) {
		$.fn.tipAlert('请统一选择业务类型，保存类型，内容类别',1.5,0);
	} else {
		window['emTableConfig'].ajaxConfig.data.batchNumber=$("#batchNumber").val();
		window['emTableConfig'].ajaxConfig.data.serviceCodeId=$("#serviceCodeId").val();
		window['emTableConfig'].ajaxConfig.data.operator=$("#operator").val();
		window['emTableConfig'].ajaxConfig.data.sendType=$("#sendType").val();
		window['emTableConfig'].ajaxConfig.data.startTime=$("#startTime").val(); 
		window['emTableConfig'].ajaxConfig.data.endTime=$("#endTime").val();
		window['emTableConfig'].ajaxConfig.data.state=$("#state").val();
		window['emTableConfig'].ajaxConfig.data.title=$("#title").val();
		window['emTableConfig'].ajaxConfig.data.businessTypeId=$("#businessType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.saveType=$("#saveType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.contentTypeId=$("#contentType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.infoType=$("#infoType").val(); 
		emTable('emTableConfig');
		$("#batchNumber").val(window['emTableConfig'].ajaxConfig.data.batchNumber);
		$("#serviceCodeId").val(window['emTableConfig'].ajaxConfig.data.serviceCodeId);
		$("#operator").val(window['emTableConfig'].ajaxConfig.data.operator);
		$("#startTime").val(window['emTableConfig'].ajaxConfig.data.startTime);
		$("#endTime").val(window['emTableConfig'].ajaxConfig.data.endTime);
		$("#state").val(window['emTableConfig'].ajaxConfig.data.state);
		$("#title").val(window['emTableConfig'].ajaxConfig.data.title);
		$("#sendType").val(window['emTableConfig'].ajaxConfig.data.sendType);
		$("#businessType").val(window['emTableConfig'].ajaxConfig.data.businessTypeId);
		$("#saveType").val(window['emTableConfig'].ajaxConfig.data.saveType); 
		$("#contentType").val(window['emTableConfig'].ajaxConfig.data.contentTypeId);
		$("#infoType").val(window['emTableConfig'].ajaxConfig.data.infoType);
		
	}
}
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true,
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [ {
			label : '批次号',
			id : 'batchNumber',
			type: 'include'
		},{
			label : '服务号ID',
			id : 'serviceCodeId',
			type : 'input'
		},{
			isShow : true,
			label : '发送时间',
			id : 'startTime',
			type : 'date',
			dateFmt : 'yyyy-MM-dd HH:mm:ss',
			startDate : '%y-%M-%d 00:00:00',
			maxDateId : 'endTime' 
		},{
			label : '<span class="searchspan">至</span>',
			id : 'endTime',
			dateFmt : 'yyyy-MM-dd HH:mm:ss',
			startDate : '%y-%M-%d 23:59:59',
			type : 'date',
			minDateId : 'startTime' 
		},{
			label : '<span class="searchspan">发送状态</span>',
			id : 'state',
			type : 'select',
			options : [ {
				context : '全部',
				value : ''
			}, {
				context : '发送中',
				value : '1'
			}, {
				context : ' 发送到运营商',
				value : '2'
			}, {
				context : ' 发送成功',
				value : '3'
			}, {
				context : '发送失败',
				value : '4'
			}, {
				context : '发送超时',
				value : '5'
			}]
		},{
			label : '<span class="searchspan">运营商</span>',
			id : 'operator',
			type : 'select',
			options : [ {
				context : '全部',
				value : ''
			}, {
				context : '移动',
				value : 'CMCC'  
			}, {
				context : '联通',
				value : 'CUCC'  
			}, {
				context : '电信',
				value : 'CTCC'  
			}]
		},{
			label : '闪推标题',
			id : 'title',
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
			id : 'contentType',
			type :"include"
		},{
			label : '<span class="searchspan">信息类型</span>',
			id : 'infoType',
			type : 'select',
			options : [ {
				context : '全部',
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
			id : 'sendType',
			type : 'select',
			options : [ {
				context : '全部',
				value : ''
			}, {
				context : '接口',
				value : 1  
			}, {
				context : '页面',
				value : 2
			}]
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
		url : WEB_SERVER_PATH + '/fms/client/messagedetail/ajax/list',
		method : 'POST',
		data : {
			batchNumber:'#batchNumber',
			title:'#title',
			serviceCodeId:'#serviceCodeId',
			state:'#state',
			infoType:'#infoType',
			businessTypeId:'#businessType',
			saveType:'#saveType',
			contentTypeId:'#contentType',
			operator:'#operator',
			startTime:'#startTime',
			endTime:'#endTime',
			sendType:'#sendType'
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
			title : "批次号",
			width : "6%",
			align :'left',
			context : '@{batchNumber}'
		},{
			isShow : true,
			title : "闪推标题",
			width : "6%",
			context : '@{title}',
		},{
			isShow : true,
			title : "接收人",
			width : "6%",
			context : '@{mobile}',

		},{
			isShow : true,
			title : "闪推内容",
			width : "6%",
			context : "@{content}"
			
		},{
			isShow : true,
			title : "运营商",
			width : "6%",
			selectors : [ {
				isShow : true,
				term : "@{operatorCode}",
				select : [ {
					value : 'CMCC',
					context : "移动"
				}, {
					value : 'CUCC',
					context : "联通"
				}, {
					value : 'CTCC',
					context : "电信"
				}]
			} ]
			
		},{
			isShow : true,
			title : "提交方式",
			width : "6%",
			selectors : [ {
				isShow : true,
				term : "@{sendType}",
				select : [ {
					value : 1,
					context : "接口"
				}, {
					value : 2,
					context : "页面"
				}]
			} ]
			
		},{
			isShow : true,
			title : "业务类别",
			width : "6%",
			context :'@{businessType}'
			
		},{
			isShow : true,
			title : "保存类型",
			width : "6%",
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
			width : "6%",
			context :'@{contentType}'			
		},{
			isShow : true,
			title : "信息类型",
			width : "6%",
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
			title : "发送状态",
			width : "6%",
			selectors : [ {
				isShow : true,
				term : "@{state}",
				select : [{
					context : '发送中',
					value : '1'
				}, {
					context : ' 发送到运营商',
					value : '2'
				}, {
					context : ' 发送成功',
					value : '3'
				}, {
					context : '发送失败',
					value : '4'
				}, {
					context : '发送超时',
					value : '5'
				}]
			} ]
		},{
			isShow : true,
			title : "发送时间",
			width : "6%",
			context : "@{sendTime}"
			
		},{
			isShow : true,
			title : "状态报告返回时间",
			width : "6%",
			context : "@{channelReportTime}"
		}]
	}
}
emTable('emTableConfig');
function chooseType(){
	var saveType=$("#saveType").val();
	var busiId=$("#businessType option:selected").val();
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/client/messagedetail/ajax/getcontent',
		type : 'post',
		dataType : 'json',
		data : {
			saveType :saveType,
			busiId:busiId
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				var list=data.result;
				var html='<option value="">--请选择--</option>';
				for(var i in list){
					if(list[i].name!=null){
						html+='<option value='+list[i].id+'>'+list[i].name+'</option>';
					}
				}
				$("#contentType").html(html);
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