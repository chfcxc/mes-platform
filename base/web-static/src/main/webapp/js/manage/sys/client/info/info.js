$(function(){
	getItems('sysclientinfo');
});
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, 
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [{
			isShow : true,
			label : '客户名称',
			id : 'clientName',
			type : 'input'
		},{
			isShow : true,
			label : '客户编号',
			id : 'clientNumber',
			type : 'input'
		},{
			isShow : true,
			label : '联&nbsp;&nbsp;系&nbsp;人',
			id : 'linkman',
			type : 'input'
		},{
			isShow : true,
			label : '手&nbsp;&nbsp;机&nbsp;号',
			id : 'mobile',
			type : 'input'
		}],
		buttonItems : [ {
		  isShow :hasAuth("OPER_SYS_ADD_CLIENT")==true,
			label : '新增',
			id : 'addBtn',
			onClickFunction : 'addInfo()'
		}],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/client/info/list', 
		method : 'POST',
		data : {
			clientNumber:'#clientNumber',
			clientName : '#clientName',
			linkman:'#linkman',
			mobile:'#mobile',
			viplevel:'#viplevel',
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
						title : "客户名称",
						width : "10%",
						align :'left',
						context : '<a href="javascript:void(0);" style=" text-decoration:none;" onclick="seeDetail(@{id})">@{nameCn}</a>'
					},{
						isShow : true,
						title : "客户类型",
						width : "6%",
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
						title : "客户级别",
						width : "6%",
						selectors : [ {
							isShow : true,
							term : "@{viplevel}",
							select : [ {
								value : 0,
								context : "普通客户"
							}, {
								value : 1,
								context : "大客户"
							},, {
								value : 2,
								context : "VIP客户"
							}]
						} ]
					},{
						isShow : true,
						title : "客户权限",
						width : "6%",
						context : "@{authName}"
					},{
						isShow : true,
						title : "增值服务",
						width : "6%",
						selectors : [ {
							isShow : true,
							term : "@{valueAddedService}",
							select : [ {
								value : 1,
								context : "短链接简易版"
							}, {
								value : 2,
								context : "短链接精确版"
							}, {
								value : "1,2",
								context : "短链接简易版、短链接精确版"
							}]
						} ]
					},{
						isShow : true,
						title : "客户编号",
						width : "8%",
						context : "@{clientNumber}"
					},{
						isShow : true,
						title : "联系人",
						width : "5%",
						align :'left',
						context : "@{linkman}"
					},{
						isShow : true,
						title : "所属销售",
						width : "6%",
						align :'left',
						context : "@{salesName}"
					},{
						isShow : true,
						title : "手机号",
						width : "8%",
						context : "@{mobile}"
					},{
						isShow : true,
						title : "电话",
						width : "8%",
						align :'left',
						context : "@{telephone}"
					},{
						isShow : true,
						title : "公司地址",
						width : "6%",
						align :'left',
						context : "@{address}"
					},{
						isShow : true,
						title : "邮箱",
						width : "6%",
						context : "@{email}"
					},{
						isShow : true,
						title : "创建时间",
						width : "10%",
						context : "@{createTime}"
					},{
						isShow : false,
						title : "是否VIP",
						width : "6%",
						selectors : [ {
							isShow : true,
							term : "@{isVip}",
							select : [ {
								value : true,
								context : "是"
							}, {
								value : false,
								context : "否"
							}]
						} ]
					},{
						isShow : hasAuth("OPER_SYS_UPDATE_CLIENT")==true,
						title : "操作",
						width : "4%",
						context :"<div class='btn_box rs' alt='编辑' title='编辑' onClick='modifyInfo(@{id})'><i class='iCon grey-write'></i></div>"
							 	
					}]
	}
}

emTable('emTableConfig');
function addInfo(){	
	window.location.href=WEB_SERVER_PATH + '/sys/client/info/to/add';//跳转到新增页面
}
function modifyInfo(id){	
	window.location.href=WEB_SERVER_PATH + '/sys/client/info/to/modify?id='+id;//跳转到修改页面
	setItems('sysclientinfo');
}
function seeDetail(id){	
	if(!hasAuth("OPER_SYS_ACCOUNT_CLIENT")){
		return;
	}
	window.location.href=WEB_SERVER_PATH + '/sys/client/info/to/detail?id='+id;//跳转到子客户页面
	setItems('sysclientinfo');
}
