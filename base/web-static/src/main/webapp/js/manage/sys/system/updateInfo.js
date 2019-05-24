var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true,
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [ {
			isShow : true,
			label : '版本',
			id : 'versionSearch',
			type : 'include'
		},{
			label : '更新内容',
			id : 'oldcontent',
			type : 'include'
		},{
			isShow:false,
			label : '',
			id : 'content',
			type : 'include'
		}],
		buttonItems : [{
			isShow : hasAuth("OPER_SYS_UPDATE_INFO_ADD")==true,
			label : '新增升级记录',
			id : 'addBtn',
			onClickFunction : 'addParach()'
		}],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/system/updateInfo/ajax/list',
		method : 'POST',
		data : {
			version : '#versionSearch',
			updateInfo : '#content'
		},
		startType : "startNum",
		startParams : "start",
		limitParams : "limit", 
		defaultLimit : 20,
		supportLimit : [ 20, 50,100 ],
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
					title : "版本号",
					width : "10%",
					context : "@{versionInfo}"
				},{
					isShow : true,
					align:'left',
					specialChartTransform:'true',
					title : "更新内容",
					width : "40%",
					context : "<p>@{updateInfo}</p>"
				},{
					isShow : true,
					title : "业务类型",
					width : "10%",
					context : "@{businessNameSimple}"
				},{
					isShow : true, 
					title : "所属系统",// 所属系统  1:管理端  2：销售端 3：客户端  
					width : "10%",
					selectors : [ {
						isShow : true,
						term : "@{subSystem}",
						select : [ {
							context :'管理系统',
							value : '1'
						},{
							context :'销售系统',
							value : '2'
						},{
							context :'客户系统',
							value : '3'
						}]
					}]
				},{
					isShow : true,
					title : "版本发布时间",
					width : "15%",
					context : "@{pubTime}"
				},{
					isShow:true,
					title : "操作",
					width : "10%",
					selectors : [{
						isShow : hasAuth("OPER_SYS_UPDATE_INFO_UPDATE")==true,
						term : "@{id}",
						select : [ {
							value : "@{id}",
							context : "<div class='btn_box rs' alt='修改' title='修改' onClick='updateInfo(@{id})'><i class='iCon grey-write'></i></div>"
						} ]
					}]
				}]
	}
}

emTable('emTableConfig');
$(function(){
	versionList();
});
//加载版本号
function versionList(){
	$.ajax({
		url : WEB_SERVER_PATH + '/sys/system/updateInfo/ajax/versionList',
		type : 'post',
		dataType : 'json',
		data : {},
		success : function(data) {
			if (data.success) {
				var list=data.result;
				var html="<option value=''>全部</option>";
				for(var i in list){
					html+='<option>'+list[i]+'</option>';
				}
				$("#versionSearch").html(html);
			} else {
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error : function() {
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
}
//新增
var html = $('#warp').html();
$('#warp').remove();
function addParach(){
	$.fn.tipOpen({
		title : "新增升级记录",
		width : '700',
		btn : [],
		cancel:false,
		concent : html
	});
	$("#pubTime").addClass("Wdate");
    $("#pubTime").attr("onfocus","WdatePicker({dateFmt:\'yyyy-MM-dd\',startDate : '%y-%M-%d'})");
	$("#saveBtn").attr("onclick","addTure()");
}
function addCancel(){
	$.fn.tipShut();
}

//新增确定事件
function addTure(){
	var $formCheckOut = $('#addForm');
	var validateor=$formCheckOut.validate({
    	rules:{
    		version:{ 
				required:true
    		},
    		pubTime:{ 
				required:true
    		},
    		subSystem:{ 
				required:true
    		},
    		businessType:{
    			required:true
    		},
    		updateInfo:{
    			required:true
    		}
    	},
		messages:{
			version:{ 
				required:"版本号不可为空"
    		},
    		pubTime:{ 
				required:"发布时间不可为空"
    		},
			subSystem:{
				required:"请选择所属系统 "
			},
			businessType:{
				required:"请选择业务类型"
			},
			updateInfo:{
				required:"更新内容不可为空"
			}
		},
		submitHandler: function() {
			var version =$("#version").val();
			var pubTime =$("#pubTime").val();
			var subSystem = $("#subSystem").val();
			var businessType =$("#businessType").val();
			var updateInfo =$("#updateInfo").val();
			updateInfo = updateInfo.replace(/ /g,"&nbsp;").replace(/\r/g,"<br/>").replace(/\n/g,"<br/>"); 
			updateInfo = updateInfo.replace(/"/g, "@EMAY34PAGE@");
			updateInfo = updateInfo.replace(/'/g, "@EMAY39PAGE@");
			updateInfo = updateInfo.replace(/</g, "@EMAY60PAGE@");
			updateInfo = updateInfo.replace(/>/g, "@EMAY62PAGE@");
			updateInfo = updateInfo.replace(/&/g, "@EMAY38PAGE@");
			updateInfo = updateInfo.replace(/`/g, "@EMAY96PAGE@");
			$.fn.tipLodding();
				$.ajax({
					url : WEB_SERVER_PATH + '/sys/system/updateInfo/ajax/add',
					type : 'post',
					dataType : 'json',
					data : {
						version : version,
						pubTime : pubTime,
						subSystem : subSystem,
						businessType : businessType,
						updateInfo : updateInfo
					},
					success : function(data) {
						if (data.success) {
							$.fn.tipLoddingEnd(true);
							$.fn.tipAlert('新增升级记录成功',1,1);
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
//修改
function updateInfo(id){
	$.ajax({
		url : WEB_SERVER_PATH + '/sys/system/updateInfo/ajax/info',
		type : 'post',
		dataType : 'json',
		data : {
			id : id
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipOpen({
					title : "修改升级记录",
					width : '700',
					btn : [],
					cancel:false,
					concent : html
				});
				$("#pubTime").addClass("Wdate");
			    $("#pubTime").attr("onfocus","WdatePicker({dateFmt:\'yyyy-MM-dd\',startDate : '%y-%M-%d'})");
				$("#saveBtn").attr("onclick","updateTrue()");
				var dataResult = data.result;
				$("#systemId").val(dataResult.id);
				$("#version").val(dataResult.versionInfo);
				$("#pubTime").val(dataResult.pubTime);
				$("#subSystem").val(dataResult.subSystem);
				$("#updateInfo").val(dataResult.updateInfo);
				//业务类型
				var busniessType = dataResult.busniessType; 
				$("#businessType option").each(function(index,ele){
					if(busniessType == $(ele).val()){
						$(ele).attr("selected","selected");
					}
				});
			} else {
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error : function() {
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
}
//修改确定事件
function updateTrue(){
	var $formCheckOut = $('#addForm');
	var validateor=$formCheckOut.validate({
		rules:{
    		version:{ 
				required:true
    		},
    		pubTime:{ 
				required:true
    		},
    		subSystem:{ 
				required:true
    		},
    		businessType:{
    			required:true
    		},
    		updateInfo:{
    			required:true
    		}
    	},
		messages:{
			version:{ 
				required:"版本号不可为空"
    		},
    		pubTime:{ 
				required:"发布时间不可为空"
    		},
			subSystem:{
				required:"请选择所属系统 "
			},
			businessType:{
				required:"请选择业务类型"
			},
			updateInfo:{
				required:"更新内容不可为空"
			}
		},
		submitHandler: function() {
			var id=$("#systemId").val();
			var version =$("#version").val();
			var pubTime =$("#pubTime").val();
			var subSystem = $("#subSystem").val();
			var businessType =$("#businessType").val();
			var updateInfo =$("#updateInfo").val();
			updateInfo = updateInfo.replace(/ /g,"&nbsp;").replace(/\r/g,"<br/>").replace(/\n/g,"<br/>"); 
			updateInfo = updateInfo.replace(/"/g, "@EMAY34PAGE@");
			updateInfo = updateInfo.replace(/'/g, "@EMAY39PAGE@");
			updateInfo = updateInfo.replace(/</g, "@EMAY60PAGE@");
			updateInfo = updateInfo.replace(/>/g, "@EMAY62PAGE@");
			updateInfo = updateInfo.replace(/&/g, "@EMAY38PAGE@");
			updateInfo = updateInfo.replace(/`/g, "@EMAY96PAGE@");
			$.fn.tipLodding();
				$.ajax({
					url : WEB_SERVER_PATH + '/sys/system/updateInfo/ajax/update',
					type : 'post',
					dataType : 'json',
					data : {
						id : id,
						version : version,
						pubTime : pubTime,
						subSystem : subSystem,
						businessType : businessType,
						updateInfo : updateInfo
					},
					success : function(data) {
						if (data.success) {
							$.fn.tipLoddingEnd(true);
							$.fn.tipAlert('修改升级记录成功',1,1);
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

//内容转义
function translateContent(obj){
	var content = $(obj).val();
	content = content.replace(/"/g, "@EMAY34PAGE@");
	content = content.replace(/'/g, "@EMAY39PAGE@");
	content = content.replace(/</g, "@EMAY60PAGE@");
	content = content.replace(/>/g, "@EMAY62PAGE@");
	content = content.replace(/&/g, "@EMAY38PAGE@");
	content = content.replace(/`/g, "@EMAY96PAGE@");
	$("#content").val(content);
}
	

