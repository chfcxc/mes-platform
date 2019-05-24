function showItem(){
	var flag = false;
	if ($("#businessType").val() == "" && $("#saveType").val() == "" && $("#contentType").val() == "") {
		flag = true;
	} else if ($("#businessType").val() != "" && $("#saveType").val() != "" && $("#contentType").val() != ""){
		flag = true;
	} else {
		flag = false;
	}
	if (!flag) {
		$.fn.tipAlert('请统一选择业务类型，保存类型，内容类别',1.5,0);
	} else {
		window['emTableConfig'].ajaxConfig.data.businessTypeName=$("#businessType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.saveType=$("#saveType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.content=$("#contentType option:selected").val();
		emTable('emTableConfig');
		$("#businessType").val(window['emTableConfig'].ajaxConfig.data.businessTypeName);
		$("#saveType").val(window['emTableConfig'].ajaxConfig.data.saveType); 
		$("#contentType").val(window['emTableConfig'].ajaxConfig.data.content); 
	}
}
var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, 
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [{
			isShow : true,
			label : '业务类型',
			id : 'businessType',
			type :"include"
		},{
			//1不可保存2可以保存
			label : '<span class="searchspan">保存类型</span>',
			id : 'saveType',
			type :"include"
		},{
			isShow : true,
			label : '内容类型',
			id : 'contentType',
			type :"include"
		}],
		buttonItems : [{
			isShow: true,
			label : '查询',
			id : 'addBtn',
			onClickFunction : 'showItem()'
		},{
			isShow: true,
			label : '新增业务类型',
			id : 'addBtn',
			onClickFunction : 'addbusiness()'
		},{
			isShow: true,
			label : '新增内容类别',
			id : 'addBtn',
			onClickFunction : 'addcontant()'
		}],
		searchButton : false,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/fms/base/business/ajax/list',
		method : 'POST',
		data : {
			businessTypeName:'#businessType',
			saveType:'#saveType',
			content:'#contentType'
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
			title : "业务类型",
			width : "10%",
			context : "@{busiName}"
		},{
			isShow : true,
			title : "内容类型",
			width : "10%",
			context : "@{contentName}"
				
		},{
			isShow : true,
			title : "保存类型",
			width : "10%",
			selectors : [ {
				isShow : true,
				term : "@{saveType}",
				select : [ {
					value : 1,
					context : "可保存"
				}, {
					value : 2,
					context : "不可保存"
				}]
			} ]
		},{
			isShow : true,
			title : "操作",
			width : "20%",
			 context :
            	 "<div class='btn_box rs' alt='修改' title='修改' onClick='modify(\"@{contentId}\")'><i class='iCon grey-write'></i></div>&nbsp;"
//            	 "<div class='btn_box rs' alt='删除' title='删除' onClick='deletes(\"@{contentId}\")'><i class='iCon grey-delete'></i></div>"
		}]
	}
}
emTable('emTableConfig');
function addbusiness(){
	var html='<label><span class="xing">*</span>业务类型：</label><input type="text" id="businessTypeName" maxlength="20" />';
	$.fn.tipOpen({
		title : "新增操作",
		width : '300',
		height : '50',
		concent :html,
		btn : [ {
	 		label : '确定',
	 		onClickFunction : 'addconfir()'
	 	}]				
});
}
function addconfir(){
	var businessTypeName=$("#businessTypeName").val();
	if(businessTypeName=='' || businessTypeName==null){
		$.fn.tipAlert('业务类型不能为空',1,0);
		return false;
	}
	$.fn.tipLodding();
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/base/business/ajax/savebusi',
		type : 'post',
		dataType : 'json',
		data : {
			businessTypeName :businessTypeName
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('添加成功',1,1);
				window.location.reload();
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error : function() {
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
}
function addcontant(){
	var html='<div class="businesstype" ><label><span class="xing">*</span>业务类型：</label>';
		html+='<select id="businessTypeName">'+$('#businessType').html()+'</select>';
		html+='</div>';
		html+='<div class="businesstype" ><label><span class="xing">*</span>保存类型：</label><select id="saveTypeadd"><option value="1">可保存</option><option value="2">不可保存</option></select></div>';
		html+='<div class="businesstype" ><label><span class="xing">*</span> 内容类别：</label><input type="text" id="contentName" maxlength="20" /></div>';
	$.fn.tipOpen({
		title : "新增操作",
		width : '300',
		height : '150',
		concent :html,
		btn : [ {
	 		label : '确定',
	 		onClickFunction : 'addcontantdeir()'
	 	}]				
});
}

function addcontantdeir(){
	var businessTypeName =$("#businessTypeName option:selected").text()
	var busiId =$("#businessTypeName").val();
	var saveType=$("#saveTypeadd").val();
	var contentName=$("#contentName").val();
	if(busiId=='' || busiId==null){
		$.fn.tipAlert('请选择业务类型',1,0);
		return false;
	}
	if(contentName=='' || contentName==null){
		$.fn.tipAlert('请添加内容类别',1,0);
		return false;
	}
	$.fn.tipLodding();
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/base/business/ajax/savecontent',
		type : 'post',
		dataType : 'json',
		data : {
			busiId : busiId,
			businessTypeName :businessTypeName,
			saveType :saveType,
			contentName :contentName
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('添加成功',1,1);
				var limit=$('.limitSelect').val();
	        	emrefulsh("emTableConfig", 0, limit);
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error : function() {
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
}
function modify(id){
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/base/business/ajax/findid',
		type : 'post',
		dataType : 'json',
		data : {
			contentId:id
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				var date=data.result;
				var parentId=date[0].parentId;
				var name=date[0].name;
				var saveType=date[0].saveType;
				var html='<div class="businesstype" ><label><span class="xing">*</span>业务类型：</label>';
					html+='<select id="businessTypeName" disabled="disabled" >'+$('#businessType').html()+'</select>';
					html+='</div>';
					html+='<div class="businesstype" ><label><span class="xing">*</span>保存类型：</label><select id="saveTypemodify" ><option value="1">可保存</option><option value="2">不可保存</option></select></div>';
					html+='<div class="businesstype" ><label><span class="xing">*</span> 内容类别：</label><input type="text" id="contentName" maxlength="20" /></div>';
				$.fn.tipOpen({
					title : "修改操作",
					width : '300',
					height : '150',
					concent :html,
					btn : [ {
				 		label : '确定',
				 		onClickFunction : 'modefydeir('+id+')'
				 	}]				
				});
				$('#businessTypeName option').each(function(i,obj){
					if($(obj).val()==parentId){
						$(obj).attr("selected",true);
					}
				})
				$('#saveTypemodify option').each(function(i,obj){
					if($(obj).val()==saveType){
						$(obj).attr("selected",true);
					}
				})
				$("#saveTypemodify").attr('disabled',true);
				$("#contentName").val(name);
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error : function() {
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
	
}
function modefydeir(id){
	var businessTypeName =$("#businessTypeName option:selected").text()
//	var busiId =$("#businessTypeName").val();
	var saveType=$("#saveTypemodify").val();
	var contentName=$("#contentName").val();
	if(contentName=='' || contentName==null){
		$.fn.tipAlert('请添加内容类别',1,0);
		return false;
	}
	$.fn.tipLodding();
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/base/business/ajax/update',
		type : 'post',
		dataType : 'json',
		data : {
			contentId:id,
			businessTypeName :businessTypeName,
			saveType :saveType,
			contentName :contentName
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('添加成功',1,1);
				var limit=$('.limitSelect').val();
	        	emrefulsh("emTableConfig", 0, limit);
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error : function() {
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
}

function deletes(id){
	var html='<p>确定删除此类别么</p>';
	$.fn.tipOpen({
		title : "删除操作",
		width : '300',
		height : '50',
		concent :html,
		btn : [ {
	 		label : '确定',
	 		onClickFunction : 'deletesfir('+id+')'
	 	}]				
});
}
function deletesfir(id){
	$.fn.tipLodding();
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/base/business/ajax/delete',
		type : 'post',
		dataType : 'json',
		data : {
			contentId :id
		},
		success : function(data) {
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
		error : function() {
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
}
$(function(){
	$("#saveType").find('option').each(function(i,ele){
		if($(ele).val() != "") {
			$(ele).hide();
		}
	})
})
function chooseType(){
	var saveType=$("#saveType").val();
	var busiId=$("#businessType option:selected").val();
	$("#saveType").find('option').each(function(i,ele){
		$(ele).show();
	})
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/base/business/ajax/getcontent',
		type : 'post',
		dataType : 'json',
		data : {
			saveType :saveType,
			busiId:busiId
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				var list=data.result;
				var html='<option value="">--请选择--</option>';
				for(var i in list){
					if(list[i].name!=null){
						html+='<option value='+list[i].id+'>'+list[i].name+'</option>';
					}
				}
				$("#contentType").html(html);
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error : function() {
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
}