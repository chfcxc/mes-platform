$(function(){
	getItems('sysclientuser');
});
//查询
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, //true 显示具体的分页数
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [{
			label : '客户名称',
			id : 'clientName',
			type : 'input'
		},{
			label : '客户编号',
			id : 'clientNumber',
			type : 'input'
		},{
			label : '用&nbsp;&nbsp;户&nbsp;名',
			id : 'username',
			type : 'input'
		},{
			isShow : true,
			label : '账户类型',
			id : 'accountType',
			type : 'select',
			options : [ {
				context : '全部',
				value : '0'
			}, {
				context : '管理账号',
				value : '1'
			}, {
				context : '普通账号',
				value : '2'
			}]
		},{
			label : '姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名 ',
			id : 'realname',
			type : 'input'
		},{
			label : '手 &nbsp;机&nbsp;号 ',
			id : 'mobile',
			type : 'input'
		},{
			isShow : true,
			label : '状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态',
			id : 'state',
			type : 'select',
			options : [ {
				context : '全部',
				value : '0'
			}, {
				context : '停用',
				value : '1'
			}, {
				context : '启用',
				value : '2'
			}, {
				context : '锁定',
				value : '3'
			}]
		}],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/client/user/ajax/list',
		method : 'POST',
		data : {
			clientName: '#clientName',
			clientNumber:'#clientNumber',
			userName : '#username', //用户名
			realName : '#realname', //姓名
			mobile : '#mobile',//手机号
			accountType :'#accountType',
			state : '#state'
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
					width : "24%",
					align :'left',
					context : "@{clientName}"
						
				},{
					isShow : true,
					title : "客户编号",
					width : "8%",
					context : "@{clientNumber}"
				},{
					isShow : true,
					title : "用户名",
					width : "12%",
					align :'left',
					context : "@{username}"
				},{
					isShow : true,
					title : "帐号类型",
					width : "8%",
					selectors : [ {
						isShow : true,
						term : "@{identity}",
						select : [ {
							value : 0,
							context : "全部"
						}, {
							value : 1,
							context : "管理账号"
						}, {
							value : 2,
							context : "普通账号"
						}]	
					}]
				},{
					isShow : true,
					title : "姓名",
					width : "10%",
					align :'left',
					context : "@{realname}"
				},{
					isShow : true,
					title : "手机号",
					width : "8%",
					context : "@{mobile}"
				},{
					isShow : true,
					title : "状态",
					width : "8%",
					selectors : [ {
						isShow : true,
						term : "@{state}",
						select : [ {
							value : 0,
							context : "全部"
						}, {
							value : 1,
							context : "停用"
						}, {
							value : 2,
							context : "启用"
						}, {
							value : 3,
							context : "锁定"
						}]	
					}]
				},{
					isShow : true,
					title : "操作",
					width : "8%",
					context : '<div class="btn_box rs" alt="查看详情" title="查看详情" onclick="seeDetails(@{id})"><i class="iCon grey-see"></i></div>',
				}]
	}
}
emTable('emTableConfig');

function seeDetails(id){
	window.location.href=WEB_SERVER_PATH + '/sys/client/user/ajax/to/detail?id='+id;
	setItems('sysclientuser');
}
