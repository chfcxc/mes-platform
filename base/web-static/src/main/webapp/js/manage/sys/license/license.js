$(function(){
	getItems('license');
});
//查询
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, //true 显示具体的分页数
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [{
			label : '创建人',
			id : 'operatorName',
			type : 'input'
		},{
			label : '有效期',
			id : 'termOfValidity',
			type : 'input'
		},{
			label : 'mac地址',
			id : 'mac',
			type : 'input'
		},{
	           isShow : true,
	           label : '创建时间',
	           id : 'startDate',
	           type : 'date',// 时间，
	           dateFmt:'yyyy-MM-dd HH:mm:ss',
	 	       startDate:'%y-%M-%d 00:00:00',
	           maxDateId : 'endDate'
	       },{
	           isShow : true,
	           label : '至　',
	           id : 'endDate',
	           dateFmt:'yyyy-MM-dd HH:mm:ss',
		       startDate:'%y-%M-%d 23:59:59',
	           type : 'date',
	           minDateId : 'startDate'
	       }],
		buttonItems : [{
			isShow:hasAuth("SYS_SERIAL_NUMBER_ADD")==true,
			label : '生成license',
			id : 'addBtn',
			onClickFunction : 'addLicense()'
		} ],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/license/recode/ajax/list',
		method : 'POST',
		data : {
			operatorName : '#operatorName',
			termOfValidity : '#termOfValidity',
			mac : '#mac',
			startDate : '#startDate',
			endDate : '#endDate'
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
					title : "客户名称",
					width : "15%",
					align :'left',
					context : "@{nameCn}"
				},{
					isShow : true,
					title : "客户联系人",
					width : "9%",
					align :'left',
					context : "@{linkman}"
				},{
					isShow : true,
					title : "联系方式",
					width : "9%",
					context : "@{mobile}"
				},{
					isShow : true,
					title : "产品",
					width : "5%",
					context : "@{product}"
				},{
					isShow : true,
					title : "版本",
					width : "6%",
					context : "@{version}"
				},{
					isShow : true,
					align :'left',
					title : "mac地址",
					width : "20%",
					align :'left',
					context : "@{mac}"
				},{
					isShow : true,
					title : "有效期",
					width : "5%",
					context : "@{termOfValidity}"
				},{
					isShow : true,
					title : "创建人",
					width : "8%",
					align :'left',
					context : "@{operatorName}"
				},{
					isShow : true,
					title : "创建时间",
					width : "12%",
					context : "@{createTime}"
				},{
					isShow : hasAuths("SYS_SERIAL_NUMBER_UPDATE","SYS_SERIAL_NUMBER_DOWNLOAD")==true,
					width : "6%",
					title : "操作",
					selectors : [{
						isShow : hasAuth("SYS_SERIAL_NUMBER_UPDATE")==true,
						term : "@{id}",
						select : [{
							value : "@{id}",
							context : 
								 "<div class='btn_box rs' alt='修改' title='修改' onClick='modifyUser(@{id})'><i class='iCon grey-write'></i></div>&nbsp;"
					     }]
		            },{
						isShow : hasAuth("SYS_SERIAL_NUMBER_DOWNLOAD")==true,
						term : "@{id}",
						select : [{
							value : "@{id}",
							context : 
								 "<div class='btn_box rs' alt='下载' title='下载' onclick='download(@{id})'><i class='iCon grey-download'></i></div>&nbsp;"
					     }]
		            }]

			}]
				
		}
}
emTable('emTableConfig');

//新增
function addLicense(){
	window.location.href=WEB_SERVER_PATH + '/sys/license/recode/add';
}

//编辑
function modifyUser(id){
	window.location.href=WEB_SERVER_PATH + '/sys/license/recode/modify?id='+id;
	setItems('license');
}

//下载
function download(id){
	$("#downloadForm").remove();
	var formHtml = "<form id='downloadForm' action='"+WEB_SERVER_PATH+"/sys/license/recode/ajax/download' method='post'>";
		formHtml +="<input type='hidden' value='"+id+"' name='id'/>";
		formHtml += "</form>";
	$("body").append(formHtml);
	$("#downloadForm").submit();
}
function hasAuths(a,b){
	return (hasAuth(a)||hasAuth(b));
}
function seeClose(){
	$.fn.tipShut();
	emrefulsh('emTableConfig');
}
