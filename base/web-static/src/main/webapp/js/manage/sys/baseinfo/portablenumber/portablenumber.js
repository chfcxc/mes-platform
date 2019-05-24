//查询
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, //true 显示具体的分页数
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [ {
			label : '手机号 ',
			id : 'mobile',
			type : 'input'
		}],
		
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/base/portablenumber/findall',
		method : 'POST',
		data : {
			mobile : '#mobile'//手机号
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
					title : "手机号",
					width : "30%",
					context : "@{mobile}"
				},{
					isShow : true,
					title : "运营商",
					width : "30%",
					selectors : [ {
						isShow : true,
						term : "@{operatorCode}",
						select : [ {
							value : "CMCC",
							context : "中国移动"
						},{
							value : "CUCC",
							context : "中国联通"
						},{
							value : "CTCC",
							context : "中国电信"
						}]
					} ]
						
				},{
					isShow : true,
					title : "最后更改时间",
					width : "40%",
					context : "@{lastUpdateTime}"
				}]
	}
}
emTable('emTableConfig');