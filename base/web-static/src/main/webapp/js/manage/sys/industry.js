//查询
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true,
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [{
			label : '行业',
			id : 'industry',
			type : 'input'
		}],
		buttonItems : [{
			isShow:hasAuth("OPER_SYS_BASE_INDUSTRY_ADD")==true,
			label : '新增',
			id : 'addBtn',
			onClickFunction : 'addParach()'
		}],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/base/industry/ajax/list',
		method : 'POST',
		data : {
			industry : '#industry'
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
			title : "行业分类",
			width : "40%",
			context : "@{industry}"
		},{
			width : "10%",
			title : "操作",
			
			selectors : [
				{
             		isShow : hasAuth("OPER_SYS_BASE_INDUSTRY_MODIFY")==true,
             		term : "@{id}",
             		select : [{
             			value:"@{id}",
             			context :  "<div class='btn_box rs' alt='修改' title='修改'><a href='javascript:void(0)' onclick='modifyIndustry(\"@{id}\",\"@{industry}\")'><i class='iCon grey-write'></i></a></div>&nbsp;"
             		}]		
             	}/*,{
             		isShow : hasAuth("OPER_SYS_BASE_INDUSTRY_DELETE")==true,
             		term : "@{id}",
             		select : [{
             			value:"@{id}",
             			context : "<div class='btn_box rs' alt='删除' title='删除'><a href='javascript:void(0)' onClick='delConfirm(\"@{id}\",\"@{industry}\")'><i class='iCon grey-delete'></i></a></div>&nbsp;"
             		}]		
             	}*/]
		}]
	}
}
emTable('emTableConfig');
//新增
function addParach(){
	var addHtml='<div>';
		addHtml+='<label>行业分类：</label><input type="text" id="industryadd" placeholder="30字，可以有特殊字符" /></div>'
			 $.fn.tipOpen({
				 title : "新增行业分类",// 弹框标题
				 width : '300',// 弹框内容宽度
				 btn : [ {
					 label : '确定',
					 onClickFunction : 'determineadd()'
				 }],//按钮是否显示
				 concent : addHtml//弹框内容
			 });	
}
function determineadd(){
	var industry=$("#industryadd").val();
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/base/industry/ajax/add",
		type:'post',
		dataType:'json',
		data:{
			industry : industry
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('新增成功',1,1);
				var limit=$('.limitSelect').val();
				emrefulsh("emTableConfig", 0, limit);
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message, 1.5, 0);
			}
		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
//修改
function modifyIndustry(id){
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/base/industry/ajax/info",
		type:'post',
		dataType:'json',
		data:{
			id : id
		},
		success:function(data){
			if (data.success) {
				var list=data.result;
				var addHtml='<div>';
				addHtml+='<label>行业分类：</label><input type="text" id="industrymodify" value='+list.industry+' /></div>'
					 $.fn.tipOpen({
						 title : "新增行业分类",// 弹框标题
						 width : '300',// 弹框内容宽度
						 btn : [ {
							 label : '确定',
							 onClickFunction : 'determineModify('+id+')'
						 }],//按钮是否显示
						 concent : addHtml//弹框内容
					 });
			} else {
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
function determineModify(id){
	var industry=$("#industrymodify").val();
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/base/industry/ajax/modify",
		type:'post',
		dataType:'json',
		data:{
			industry : industry,
			id : id
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('修改成功',1,1);
				var limit=$('.limitSelect').val();
				emrefulsh("emTableConfig", 0, limit);
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
//删除
function delConfirm(id,industry){
	 var addHtml ="";
	 addHtml += "<div class='delDiv'>确定要删除【"+industry+"】吗?</div>";
	 $.fn.tipOpen({
		 title : "删除确认",
		 width : '470',
		 btn : [ {
			 label : '确定',
			 onClickFunction : 'delConSave('+id+')'
		 }],
		 concent : addHtml
	 });
}
function delConSave(id){
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/base/industry/ajax/delete",
		type:'post',
		dataType:'json',
		data:{
			id : id
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('删除成功',1,1);
				var limit=$('.limitSelect').val();
				emrefulsh("emTableConfig", 0, limit);
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}