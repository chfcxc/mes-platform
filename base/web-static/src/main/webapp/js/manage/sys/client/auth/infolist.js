var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true,
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [ {
			isShow : true,
			label : '权限名称',
			id : 'authName',
			type : 'input'
		} ],
		buttonItems : [ {
			isShow : true,
			label : '新增权限',
			id : 'addBtn',
			onClickFunction : 'addauth()'
		}],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/client/auth/ajax/list',
		method : 'POST',
		data : {
			authName : '#authName'
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
					title : "权限名称",
					width : "40%",
					context : '@{authName}'
				},{
					isShow :true,
					title : "操作",
					width : "45%",
					selectors : [ {
							isShow:true,
							term : "@{id}",
							select : [ {
								value : "@{id}",
								context : "<div class='btn_box rs' alt='修改' title='修改' onClick='modify(@{id})'><i class='iCon grey-write'></i></div>"
							} ]
						} ]
				}]
	}
}

emTable('emTableConfig');
function addauth(){
	window.location.href=WEB_SERVER_PATH + '/sys/client/auth/to/add';//跳转到新增页面
}
function modify(id){
	window.location.href=WEB_SERVER_PATH + '/sys/client/auth/to/modify?id='+id;//跳转到修改页面
}