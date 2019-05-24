var emTableConfig = {
			outerDivId : "outerId",
			pagesShow : true, 
			searchConfig : {
				searchItems : [ {
					label : '',
					id : 'variableName',
					type : 'include'
				}],
				buttonItems : [ {
					label : '返回',
					id : 'returnPage',
					onClickFunction : 'setReturnPage('+parentId+')'
				}],
				searchButton : true,
				resetButton : false
			},
			ajaxConfig : {
				url:  WEB_SERVER_PATH + "/sys/user/department/ajax/childlist?deptId="+id,
				method : 'POST',
				data : {
					variableName : "#variableName"
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
								title : "用户名",
								width : "10%",
								align :'left',
								context : "@{username}"
							},{
								isShow : true,
								title : "姓名",
								width : "10%",
								align :'left',
								context : "@{realname}"
							},{
								isShow : true,
								title : "部门",
								width : "10%",
								align :'left',
								context : "@{department}"
							},{
								isShow : true,
								title : "上级部门",
								width : "10%",
								align :'left',
								context : "@{parentDepartment}"
							},{
								isShow : true,
								title : "手机号",
								width : "10%",
								context : "@{mobile}"
							}]
		 }
	}
	emTable('emTableConfig');


function setReturnPage(id){
	window.location.href=WEB_SERVER_PATH + '/sys/user/department?depId='+id+'&parentId='+parentId+'&exedPath=' +exedPath+'&selectPath='+selectPath;;

}
