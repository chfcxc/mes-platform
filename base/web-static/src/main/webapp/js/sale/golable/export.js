$(function(){
	$(".emtable_search_form").remove();
    $("#footer").css("margin-top","60px");
    $("body").css("background","#ffffff");
    $(".center_box").css("min-height","520px");
})
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, 
	searchConfig : {
		searchItems : [ ],
		searchButton : false,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/export/list',
		method : 'POST',
		data : {},
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
		rowItems : [
				{
					isShow : true,
					title : "文件名",
					width : "20%",
					context : "<div style='text-align:left;margin-left:20px;'><span class='fileback'></span>@{fileName}</div>"
				},{
					isShow : true,
					title : "导出时间",
					width : "20%",
					context : "@{createTime}"
				},{
					isShow : true,
					title : "状态",
					width : "20%",
					selectors : [ {
						isShow : true,
						term : "@{state}",
						select : [ {
							value : 0,
							context : "文件待生成"
						}, {
							value : 1,
							context : "文件生成中"
						
						}, {
							value : 2,
							context : "文件生成完成"
						
						},{
							value : 3,
							context : "文件生成异常"
						
						}]	
					}]
				},{
					isShow : true,
					title : "操作",
					width : "20%",
					selectors : [{
						isShow : true,
						term : "@{state}",
						select : [ {
							value : 0,
							context : "<span style='color:#ccc' >下载</span>"
						}, {
							value : 1,
							context : "<span style='color:#ccc' >下载</span>"
						
						}, {
							value : 2,
							context : "<span class='xiazai' onclick='downloadfile(@{id})' >下载</span>"
						
						},{
							value : 3,
							context : "<span style='color:#ccc' >下载</span>"
						}]	
					}]	
				}]
	}
}
emTable('emTableConfig');

function downloadfile(id){
	window.location.href=WEB_SERVER_PATH + "/export/download?id="+id;	
}