var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true,
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [ {
			label : '问题描述',
			id : 'oldcontent',
			type : 'include'
		},{
			isShow:false,
			label : '',
			id : 'content',
			type : 'include'
		}],
		buttonItems : [ {
			isShow : hasAuth("OPER_SYS_FQA_ADD")==true,
			label : '新增FQA',
			id : 'addBtn',
			onClickFunction : 'addParach()'
		}],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/system/fqa/ajax/list',
		method : 'POST',
		data : {
			descProblem : '#content'
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
					title : "业务类型",
					width : "10%",
					context : "@{businessNameSimple}"
				},{
					isShow : true,
					align:'left',
					specialChartTransform:'true',
					title : "问题描述",
					width : "25%",
					context : "@{descProblem}"
				},{
					isShow : true,
					align:'left',
					specialChartTransform:'true',
					title : "答复",
					width : "25%",
					context : "@{reply}"
				},{
					isShow : true,
					title : "时间",
					width : "15%",
					context : "@{createTime}"
				},{
					isShow:true,
					title : "操作",
					width : "10%",
					selectors : [{
						isShow : hasAuth("OPER_SYS_FQA_UPDATE")==true,
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

//新增
var html = $('#warp').html();
$('#warp').remove();
function addParach(){
	$.fn.tipOpen({
		title : "创建FQA",
		width : '700',
		btn : [],
		cancel:false,
		concent : html
	});
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
    		subSystem:{ 
				required:true
    		},
    		businessType:{
    			required:true
    		},
    		descProblem:{
    			required:true
    		},
    		reply:{
    			required:true
    		}
    	},
		messages:{
			subSystem:{
				required:"请选择所属系统 "
			},
			businessType:{
				required:"请选择业务类型"
			},
			descProblem:{
				required:"问题描述不可为空"
			},
			reply:{
				required:"答复不可为空"
			}
		},
		submitHandler: function() {
			var subSystem = $("#subSystem").val();
			var businessType =$("#businessType").val();
			var descProblem =$("#descProblem").val();
			descProblem = descProblem.replace(/"/g, "@EMAY34PAGE@");
			descProblem = descProblem.replace(/'/g, "@EMAY39PAGE@");
			descProblem = descProblem.replace(/</g, "@EMAY60PAGE@");
			descProblem = descProblem.replace(/>/g, "@EMAY62PAGE@");
			descProblem = descProblem.replace(/&/g, "@EMAY38PAGE@");
			descProblem = descProblem.replace(/`/g, "@EMAY96PAGE@");
			var reply =$("#reply").val();
			reply = reply.replace(/"/g, "@EMAY34PAGE@");
			reply = reply.replace(/'/g, "@EMAY39PAGE@");
			reply = reply.replace(/</g, "@EMAY60PAGE@");
			reply = reply.replace(/>/g, "@EMAY62PAGE@");
			reply = reply.replace(/&/g, "@EMAY38PAGE@");
			reply = reply.replace(/`/g, "@EMAY96PAGE@");
			$.fn.tipLodding();
				$.ajax({
					url : WEB_SERVER_PATH + '/sys/system/fqa/ajax/add',
					type : 'post',
					dataType : 'json',
					data : {
						subSystem : subSystem,
						businessType : businessType,
						descProblem : descProblem,
						reply : reply
					},
					success : function(data) {
						if (data.success) {
							$.fn.tipLoddingEnd(true);
							$.fn.tipAlert('FQA创建成功',1,1);
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
		url : WEB_SERVER_PATH + '/sys/system/fqa/ajax/info',
		type : 'post',
		dataType : 'json',
		data : {
			id : id
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipOpen({
					title : "修改FQA",
					width : '700',
					btn : [],
					cancel:false,
					concent : html
				});
				$("#saveBtn").attr("onclick","updateTrue()");
				var dataResult = data.result;
				$("#systemId").val(dataResult.id);
				$("#subSystem").val(dataResult.subSystem);
				$("#descProblem").val(dataResult.descProblem);
				$("#reply").val(dataResult.reply);
				//业务类型
				var busniessType = dataResult.businessType;
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
    		subSystem:{ 
				required:true
    		},
    		businessType:{
    			required:true
    		},
    		descProblem:{
    			required:true
    		},
    		reply:{
    			required:true
    		}
    	},
		messages:{
			subSystem:{
				required:"请选择所属系统 "
			},
			businessType:{
				required:"请选择业务类型"
			},
			descProblem:{
				required:"问题描述不可为空"
			},
			reply:{
				required:"答复不可为空"
			}
		},
		submitHandler: function() {
			var id=$("#systemId").val();
			var subSystem = $("#subSystem").val();
			var businessType =$("#businessType").val();
			var descProblem =$("#descProblem").val();
			descProblem = descProblem.replace(/"/g, "@EMAY34PAGE@");
			descProblem = descProblem.replace(/'/g, "@EMAY39PAGE@");
			descProblem = descProblem.replace(/</g, "@EMAY60PAGE@");
			descProblem = descProblem.replace(/>/g, "@EMAY62PAGE@");
			descProblem = descProblem.replace(/&/g, "@EMAY38PAGE@");
			descProblem = descProblem.replace(/`/g, "@EMAY96PAGE@");
			var reply =$("#reply").val();
			reply = reply.replace(/"/g, "@EMAY34PAGE@");
			reply = reply.replace(/'/g, "@EMAY39PAGE@");
			reply = reply.replace(/</g, "@EMAY60PAGE@");
			reply = reply.replace(/>/g, "@EMAY62PAGE@");
			reply = reply.replace(/&/g, "@EMAY38PAGE@");
			reply = reply.replace(/`/g, "@EMAY96PAGE@");
			$.fn.tipLodding();
				$.ajax({
					url : WEB_SERVER_PATH + '/sys/system/fqa/ajax/update',
					type : 'post',
					dataType : 'json',
					data : {
						id : id,
						subSystem : subSystem,
						businessType : businessType,
						descProblem : descProblem,
						reply : reply
					},
					success : function(data) {
						if (data.success) {
							$.fn.tipLoddingEnd(true);
							$.fn.tipAlert('FQA修改成功',1,1);
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