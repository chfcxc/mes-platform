var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, 
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [{
				isShow : true,
				label : '客户编号/客户名称',
				id : 'clientUserNameAndCode',
				type : 'input'
			}],
		buttonItems : false,
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + 'sms/sys/sale/client/selectCientManage',
		method : 'POST',
		data : {
			clientUserNameAndCode : '#clientUserNameAndCode'
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
					title : "客户编号",
					width : "8%",
					align :'left', 
					context : "@{systemEnterpriseCode}"
				},{
					isShow : true,
					title : "客户名称",
					width : "28%",
					align :'left', 
					context : "@{systemEnterpriseName}"
				},{
					isShow : true,
					title : "客户类型",
					width : "8%",
					selectors : [ {
						isShow : true,
						term : "@{type}",
						select : [ {
							value : 0,
							context : "普通客户"
						}, {
							value : 1,
							context : "代理商"
						}]
					} ]
				},{
					isShow : true,
					title : "联系人",
					width : "10%",
					align :'left',
					context : "@{linkman}"
				},{
					isShow : true,
					title : "手机号",
					width : "15%",
					context : "@{mobile}"
				},{
					isShow : true,
					title : "服务号数量",
					width : "10%",
					context : "@{serviceCodeCount}"
				},{
					isShow : true,
					title : "销售",
					width : "10%",
					context : "@{systemRealname}"
				},{
					isShow : true,
					title : "操作",
					width : "6%",
					context : '<div class="btn_box rs" onclick="setTop(@{id})" title="置顶" alt="置顶"><i class="iCon grey-settop"></i></div>'
				}]
	}
}
emTable('emTableConfig');
function setTop(id){
	var clientUserNameAndCode=$("#clientUserNameAndCode").val();
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/sale/client/ajax/stickCientManage",
		type:'post',
		dataType:'json',
		data:{
			id : id
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert("置顶成功", 1, 1);
				$("#clientUserNameAndCode").val(clientUserNameAndCode);
				var limit=$('.limitSelect').val();
	        	emrefulsh("emTableConfig", 0, limit);
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message, 1.5, 0);
				$("#clientUserNameAndCode").val(clientUserNameAndCode);
				var limit=$('.limitSelect').val();
	        	emrefulsh("emTableConfig", 0, limit);
			} 
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
