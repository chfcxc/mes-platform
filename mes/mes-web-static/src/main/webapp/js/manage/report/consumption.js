var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, 
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [{
			label : '统计周期',
			id : 'rate',
			type : 'select',
			options : [{
				context : '按日',
				value : 'DAY'  
			},{
				context : '按月',
				value : 'MONTH'
			},{
				context : '按年',
				value : 'YEAR'
			}]
		},{
			label : '服务号Id',
			id : 'serviceCodeId',
			type : 'input'
		},{
			label : '所属企业',
			id : 'nameCn',
			type : 'input'
		},{
			label : '开始时间',
           id : 'startTime',
           type : 'date',
           dateFmt:'yyyy-MM-dd HH:mm:ss',
           startDate:'%y-%M-%d 00:00:00',
           maxDateId : 'endTime'
       },{
           label : '至',
           id : 'endTime',
           dateFmt:'yyyy-MM-dd HH:mm:ss',
           startDate:'%y-%M-%d 23:59:59',
           type : 'date',
           minDateId : 'startTime'
       }],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/fms/report/consumption/ajax/list',
		method : 'POST',
		data : {
			rate : '#rate',
			serviceCodeId:'#serviceCodeId',
			nameCn:'#nameCn',
			startTime : '#startTime',
			endTime : '#endTime'
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
					title : "统计日期",
					width : "15%",/*
					selectors : [ {
						isShow : true,
						term : "@{isRed}",
						select : [ {
							value : false,
							context : "@{statisticsDateStr}"
						}, {
							value : true,
							context : "<span style='color:red;'>@{statisticsDateStr}</span>"
						
						}]	
					}]*/
					context : "<span style='color:red;'>@{reportTimeStr}</span>"
					
				},{
					isShow : true,
					title : "服务号（服务号Id）",
					width : "15%",
					context : "@{serviceCode}(@{servicecodeId})"
				},{
					isShow : true,
					title : "所属企业",
					width : "15%",
					context : "@{enterpriseName}"
				},{
					isShow : true,
					title : "提交总数（条）",
					width : "9%",
					context : '<div><p>总数：@{totalNumber}</p><p>移动：@{cmccNumber}</p><p>联通：@{cuccNumber}</p><p>电信：@{ctccNumber}</p>'
				},{
					isShow : true,
					title : "成功总数（条）",
					width : "9%",
					context : '<div><p>总数：@{successNumber}</p><p>移动：@{cmccSuccessNumber}</p><p>联通：@{cuccSuccessNumber}</p><p>电信：@{ctccSuccessNumber}</p>'
				},{
					isShow : true,
					title : "失败总数（条）",
					width : "9%",
					context : '<div><p>总数：@{failNumber}</p><p>移动：@{cmccFailNumber}</p><p>联通：@{cuccFailNumber}</p><p>电信：@{ctccFailNumber}</p>'
				},{
					isShow : true,
					title : "超时总数（条）",
					width : "9%",
					context : '<div><p>总数：@{timeoutNumber}</p><p>移动：@{cmccTimeoutNumber}</p><p>联通：@{cuccTimeoutNumber}</p><p>电信：@{ctccTimeoutNumber}</p>'
				}]
	}
}
emTable('emTableConfig');