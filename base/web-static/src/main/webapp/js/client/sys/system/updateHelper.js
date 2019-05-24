$(function(){
	$(".emtable_search_form").remove();
    $("#footer").css("margin-top","60px");
    $("body").css("background","#ffffff");
});
var emTableConfig = {
	outerDivId : "outerId",
	noneCount : true,
	searchConfig : {
		searchItems : [ ],
		searchButton : false,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/helper/ajax/listUpdateInfo/client',
		method : 'POST',
		data : {},
		async:false,
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
		isNeedIndexRow : false,
		rowItems : [{
				isShow : true,
				title : "版本",
				width : "15%",
				context : "@{versionInfo}"
			},{
				isShow : true,
				align:'left',
				specialChartTransform:'true',
				title : "更新内容",
				width : "60%",
				context : "<p>@{updateInfo}</p>"
			},{
				isShow : false,
				title : "业务类型",//业务类型 1:短信 2：彩信  3：流量 
				width : "10%",
				context : "@{businessNameSimple}"
			},{
				isShow : true,
				title : "更新时间",
				width : "15%",
				context : "@{pubTime}"
			}]
	}
}
emTable('emTableConfig');
$("#outerId_div .textsize").each(function(i, n){
    $(n).css("height", n.scrollHeight + "px");
});