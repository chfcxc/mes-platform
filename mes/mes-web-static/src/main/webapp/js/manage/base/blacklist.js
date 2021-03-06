 var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, 
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [{
			isShow : true,
			label : '手机号',
			id : 'mobile',
			type : 'input'
		}],
		buttonItems : [ {
//			isShow : hasAuth("VOICE_MANAGE_BLACKDICTIONARY_ADD")==true,
			label : '新增',
			id : 'addBtn',
			onClickFunction : 'addParach()'
		},{
//			isShow : hasAuth("VOICE_MANAGE_BLACKDICTIONARY_ADD")==true,
			label : '删除',
			id : 'addBtn',
			onClickFunction : 'bathdeleteInfo()'
		},{
//			isShow : hasAuth("VOICE_MANAGE_BLACKDICTIONARY_ADD")==true,
			label : '导入模版下载',
			id : '',
			onClickFunction : 'downTemplate()'
		},{
//			isShow : hasAuth("VOICE_MANAGE_BLACKDICTIONARY_ADD")==true,
			label : '导入',
			id : 'import',
			onClickFunction : 'showImport()'
		}],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/fms/base/blacklistglobal/ajax/list',
		method : 'POST',
		data : {
			mobile : '#mobile'
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
		isNeedIndexRow : false,
		checkBoxConfig :{
			id : 'checkAll',
		    subCalss : 'check',
		    sValue : "@{id}"
		},
		rowItems : [{
					isShow : true,
					title : "手机号",
					width : "10%",
					context : "@{mobile}"
				},{
					isShow : true,
					title : "创建原因",
					width : "18%",
					context : "@{remark}"
				},{
					isShow : true,
					title : "创建人",
					width : "18%",
					context : "@{userName}"
				},{
					isShow : true,
					title : "创建时间",
					width : "20%",
					context : "@{createTime}"
				},{
//					isShow : hasAuth("VOICE_MANAGE_BLACKDICTIONARY_DELETE")==true,
					title : "操作",
					width : "8%",
					selectors : [
				         {
							term : "@{id}",
							select : [ {
								value : "@{id}",
								context : "<div class='btn_box rs' alt='删除' title='删除' onClick='deleteInfo(this,\"@{id}\")'><i class='iCon grey-delete'></i></div>"
							} ]
						} ]
				}]
	}
}
emTable('emTableConfig');
//新增
function addParach(){
	var html='<div class="tipcen">';
	html+='<div class="clear"  id="add">';
	html+='<form method="post" id="addForm">';
	html+='<input type="hidden" id="id" value=""/>';
	html+='<div class="item"><label class="item-label"><span class="xing">*</span>手机号：</label>';
	html+='<input type="text" id="mobiles"   class="item-text" name="mobiles"  /> </div>';
	html+='<div class="item hh114">';
	html+='<label class="item-label">创建原因：</label>';
	html+='<textarea id="remark" class="item-text" name="remark" maxlength="50"></textarea>';
	html+='</div>';
	html+='<div class="tipFoot"><button onclick="addTure()" id="updateBtn" class="tipBtn" type="submit">确 定</button>';
    html+='<button class="tipBtn tip-cancel" type="button" onclick="addCancel()">取 消</button></div>'; 
	html+='</form></div></div>';
	$.fn.tipOpen({
		title : "添加黑名单",
		width : '400',
		btn : [],
		cancel:false,
		concent : html
	});
}
function addCancel(){
	$.fn.tipShut();
}
function addTure(){
	var $formCheckOut = $('#addForm');
	var validateor=$formCheckOut.validate({
    	rules:{     
    		mobiles:{ 
				required:true
			}
    	},
		messages:{
			mobiles:{
				required:"手机号不可为空"
			}
		},
		submitHandler: function() {
			var mobile=$("#mobiles").val();
			var remark=$("#remark").val();
			$.fn.tipLodding();
        	$.ajax({
        		url : WEB_SERVER_PATH + '/fms/base/blacklistglobal/ajax/save?randomdata='+Date.parse(new Date()),
        		type : 'post',
        		dataType : 'json',
        		data : {
        			mobile :mobile,
        			remark:remark
        		},
        		success : function(data) {
        			if (data.success) {
        				$.fn.tipLoddingEnd(true);
        				$.fn.tipAlert('黑名单添加成功',1,1);
        				var limit=$('.limitSelect').val();
        				emrefulsh("emTableConfig", 0, limit);
        			} else {
        				$.fn.tipLoddingEnd(false);
        				$.fn.tipAlert(data.message,1.5,0);
        			}
        		},
        		error : function() {
        			$.fn.tipLoddingEnd(false);
        			validateor.resetForm();
        			$.fn.tipAlert('系统异常',1.5,2);
        		}
        	});
        }
    });
}

//下载导入模板
function downTemplate(){
	window.open(WEB_STATIC_PATH+"/template/blacklistglobal.xlsx","_self");
}

//显示导入模板box
function showImport(){
	var addHtml = "<input type='file' id='fileInput' name='fileInput'/><div class='tipPrompt red'></div>";
	$.fn.tipOpen({
		title : "导入黑名单文件 ",
		width : '325',
		height: '80',
		btn : [{
			label : '导入',
			onClickFunction : 'importFile(this)'
		}],
		concent : addHtml

	});
}
//导入文件
function importFile(obj){
	var importFile = $('#fileInput').val();
	var fileId = 'fileInput';
	if($("#fileInput").val() === ''){
		fileId = 'null';
		$.fn.tipAlert('请选择上传的文件',1.5,2);
		return;
	}else if(!importFile.endWith('.xlsx')){
		$.fn.tipAlert('请上传.xlsx格式的文件',1.5,2);
		return;
	}
	$(obj).parents('.tipBorder').hide();
	$('.layer').remove();
	$('.tipLoadding').tipLog('open');
	$.ajaxFileUpload({
		url : WEB_SERVER_PATH + '/fms/base/blacklistglobal/ajax/upload',
		secureuri : false,
		fileElementId : fileId,
		dataType : 'json',
		success : function(data) {
			$('.tipLoadding').tipLog('close');
			var html  = '<div id="tipMsg"></div>';
				html += '<div class="tipFoot" style="margin-top:86px;">';
				html += '<button id="determine" class="tipBtn" type="button" onclick="determineImport()">确定</button>';
				html += '<button id="download" class="tipBtn" type="button" onclick="download()">下载失败详情</button>';
				html += '</div>';
			$.fn.tipOpen({
				title : '导入状态',
				width : '300',
				cancel : false,
				tipClose : false,
				concent : html
			});
			if(data.success){
				var data= data.result;
				downloadKeys=data.downloadKey;
				var msg = '<label>导入总条数：</label><span>'+data.sum+'条</span><br/><label>导入成功条数：</label><span>'+data.success+'条</span><br/><label>导入失败条数：</label><a href="javascript:void(0)" >'+data.fail+'条</a><label>导入重复条数：</label><span>'+data.repeated+'条</span>';
				if(data.fail==undefined){
					var msg = '<label>导入总条数：</label><span>'+data.sum+'条</span><br/><label>导入成功条数：</label><span>'+data.success+'条</span><br/><label>导入失败条数：</label><a href="javascript:void(0)" >'+0+'条</a>';
					data.fail=0;
				}
				if(data.fail==0){
					$("#download").css({"background":"#ccc","border-color":"#ccc"});
					$("#download").removeAttr("onclick");
				}
				
				$('#tipMsg').html( msg);
				$('#tipMsg').css("margin-top","10px").css("text-align","center");
				var limit=$('.limitSelect').val();
	        	emrefulsh("emTableConfig", 0, limit);
				
			}else{
				$('#tipMsg').css("text-align","center").css("margin-top","20px");
				$("#download").css({"background":"#ccc","border-color":"#ccc"});
				$("#download").removeAttr("onclick");
				$('#tipMsg').html( data.message );
			}
		},
		error : function() {
			$.fn.tipAlert('error',1.5,2);
		}
	});
}
function determineImport(){
	$('.layer,.tipBorder').remove();
}
//下载
function download(){
	window.location.href=WEB_SERVER_PATH + '/fms/base/blacklistglobal/ajax/exportexcel?downloadKey='+downloadKeys;
}
//删除黑名单
function deleteInfo(obj,id){
	var mobile=$(obj).parent().parent().find("td").eq(1).html();
	var addHtml = "<div>确定要删除黑名单【"+mobile+"】吗?</div>";
	$.fn.tipOpen({
		title : "删除确认",
		width : '300',
		height: '27',
		btn : [{
			label : '确定',
			onClickFunction : 'deleteDictionary('+mobile+')'
		}],
		concent : addHtml
	});
}
function deleteDictionary(mobile){
	$.fn.tipLodding();
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/base/blacklistglobal/ajax/deleteid',
		type : 'post',
		dataType : 'json',
		data : {
			mobile :mobile
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('已删除',1,1);
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
//删除黑名单
function bathdeleteInfo(){
	var addHtml = "<div>确定要删除黑名单吗?</div>";
	$.fn.tipOpen({
		title : "删除确认",
		width : '300',
		height: '27',
		btn : [{
			label : '确定',
			onClickFunction : 'bathdeleteDictionary()'
		}],
		concent : addHtml
	});
}
function bathdeleteDictionary(){
	var mobiles='';
	$("input[type='checkbox']:checked").each(function(i,obj){
			if($(obj).attr('svalue')!=undefined){
				mobiles+=$(obj).parent().next().text()+',';
			}
		});
	$.fn.tipLodding();
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/base/blacklistglobal/ajax/deleteids',
		type : 'post',
		dataType : 'json',
		data : {
			mobiles :mobiles
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('已删除',1,1);
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

