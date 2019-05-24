//查询
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, //true 显示具体的分页数
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [ {
			label : '操作用户',
			id : 'username',
			type : 'input'
		},{
	           isShow : true,
	           label : '操作时间',
	           id : 'startDate',
	           type : 'date',// 时间，
	           dateFmt:'yyyy-MM-dd HH:mm:ss',
	 	       startDate:'%y-%M-%d 00:00:00',
	 	       minDate: getnowtime(),
	           maxDateId : 'endDate'
	       },{
	           isShow : true,
	           label : '至&nbsp;&nbsp;&nbsp;',
	           id : 'endDate',
	           dateFmt:'yyyy-MM-dd HH:mm:ss',
		       startDate:'%y-%M-%d 23:59:59',
	           type : 'date',
	           minDateId : 'startDate',
	           maxDate : getnowymd()
	       },{
			label : '内&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;容 ',
			id : 'content',
			type : 'input'
		}],
		
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/client/log/findall',
		method : 'POST',
		data : {
			username : '#username', 
			startDate : '#startDate',
			endDate:'#endDate',
			content : '#content'
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
				title : "操作内容",
				width : "36%",
				align :'left',
				context : '<div class="ellipsBox"  style="width:450px;text-align:left;"><span class="ellips" style="width:450px;" title="点击查看详情" onclick="seeDetails(this)" ><a style="text-decoration:none;">@{content}</a></span></div>',
				},{
					isShow : true,
					title : "服务模块",
					width : "10%",
					context : "@{service}"
				},{
					isShow : true,
					title : "功能模块",
					width : "10%",
					context : "@{module}"
				},{
					isShow : true,
					title : "操作用户",
					width : "14%",
					align :'left',
					context : "@{username}"
				},{
					isShow : true,
					title : "操作时间",
					width : "20%",
					context : "@{operTime}"
				}]
	}
}
emTable('emTableConfig');
