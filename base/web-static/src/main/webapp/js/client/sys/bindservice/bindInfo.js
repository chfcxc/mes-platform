$(function(){
	getItems('clientAccount');
});
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, 
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [ {
			label : '',
			id : 'userName',
			type : 'include'
		}],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/client/account/list',
		method : 'POST',
		data : {
			userName : '#userName'
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
					title : "帐号",
					width : "10%",
					align :'left',
					context : "@{userName}"
				},{
					isShow : true,
					title : "帐号类型",
					width : "10%",
					selectors : [ {
						isShow : true,
						term : "@{identity}",
						select : [ {
							value : 1,
							context : "管理帐号"
						}, {
							value : 2,
							context : "普通帐号"
						}]
					} ]
				},{
					isShow : true,
					title : "状态",
					width : "10%",
					selectors : [ {
						isShow : true,
						term : "@{state}",
						select : [ {
							value : 1,
							context : "停用"
						}, {
							value : 2,
							context : "启用"
						}, {
							value : 3,
							context : "锁定"
						}]
					} ]
				},{
					isShow: hasAuth("SYS_CLIENT_ACCOUNT_CHECK")==true,
					title : "查看绑定",
					width : "10%",
					context : "<span><a href='javascript:void(0)' onClick='seeContext(\"@{userId}\")'>查看</a></span>"
				},{
					isShow: hasAuth('SYS_CLIENT_ACCOUNT_BINDING')==true,
					width : "10%",
					title : "操作",
					selectors : [{
						isShow : true,
						term : "@{identity}",
						select : [
						{
							value : [{
								yValue:2,
								value:"@{state}",
								cValue:1,
								context : "<span style='color:#818181'>帐号绑定</span>"
							}] 
						},{
							value : 1,
							context : "<span style='color:#818181'>已自动绑定</span>"
						},{
							value : 2,
							context : "<span><a href='javascript:void(0)' onClick='seeAppIdBindings(\"@{userId}\",\"@{userName}\")'>帐号绑定</a></span>"
						}]
					}]	
				}]
	}
}
emTable('emTableConfig');
//查看
function seeContext(userId){
	 window.location.href=WEB_SERVER_PATH + '/sys/client/account/to/cb/'+userId;
	 setItems('clientAccount');
}
//查看绑定服务号
function seeAppIdBindings(userId,userName){
	 window.location.href=WEB_SERVER_PATH + '/sys/client/account/to/usb/'+userId+'/'+userName;
	 setItems('clientAccount');
}