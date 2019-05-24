function showItem(){
	var flag = false;
	if ($("#businessType").val() == "" && $("#saveType").val() == "" && $("#businessTypeId").val() == "") {
		flag = true;
	} else if ($("#businessType").val() != "" && $("#saveType").val() != "" && $("#businessTypeId").val() != ""){
		flag = true;
	} else {
		flag = false;
	}
	if (!flag) {
		$.fn.tipAlert('请统一选择业务类型，保存类型，内容类别',1.5,0);
	} else {
		window['emTableConfig'].ajaxConfig.data.serviceCode=$("#serviceCode").val();
		window['emTableConfig'].ajaxConfig.data.enterpriseNumber=$("#enterpriseNumber").val(); 
		window['emTableConfig'].ajaxConfig.data.enterpriseName=$("#enterpriseName").val(); 
		window['emTableConfig'].ajaxConfig.data.appId=$("#appId").val();
		window['emTableConfig'].ajaxConfig.data.serviceCodeId=$("#serviceCodeId").val();
		window['emTableConfig'].ajaxConfig.data.state=$("#state option:selected").val();
		window['emTableConfig'].ajaxConfig.data.businessType=$("#businessType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.saveType=$("#saveType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.businessTypeId=$("#businessTypeId option:selected").val();
		emTable('emTableConfig');
		$("#serviceCode").val(window['emTableConfig'].ajaxConfig.data.serviceCode); 
		$("#enterpriseNumber").val(window['emTableConfig'].ajaxConfig.data.enterpriseNumber);
		$("#enterpriseName").val(window['emTableConfig'].ajaxConfig.data.enterpriseName); 
		$("#appId").val(window['emTableConfig'].ajaxConfig.data.appId); 
		$("#serviceCodeId").val(window['emTableConfig'].ajaxConfig.data.serviceCodeId); 
		$("#businessType").val(window['emTableConfig'].ajaxConfig.data.businessType);
		$("#saveType").val(window['emTableConfig'].ajaxConfig.data.saveType); 
		$("#businessTypeId").val(window['emTableConfig'].ajaxConfig.data.businessTypeId); 
		$("#state").val(window['emTableConfig'].ajaxConfig.data.state); 
	}
}


var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, 
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [{
			isShow : true,
			label : '服务号名称',
			id : 'serviceCode',
			type : 'input'
		},{
			isShow : true,
			label : '客户编号',
			id : 'enterpriseNumber',
			type : 'input'
		},{
			isShow : true,
			label : '客户名称',
			id : 'enterpriseName',
			type : 'input'
		},{
			isShow : true,
			label : 'APPID',
			id : 'appId',
			type : 'input'
		},{
			isShow : true,
			label : '状态',
			id : 'state',
			type : 'select',
			options : [ {
				context:'--请选择--',
				value:''
			},{
				context : '启用',
				value : '0'
			}, {
				context : '停用',
				value : '1'
			}]
		},{
			isShow : true,
			label : '服务号ID',
			id : 'serviceCodeId',
			type : 'input'
		},{
			isShow : true,
			label : '业务类型',
			id : 'businessType',
			type :"include"
		},{
			isShow : true,
			label : '保存类型',
			id : 'saveType',
			type : 'include'
		},{
			isShow : true,
			label : '内容类型',
			id : 'businessTypeId',
			type :"include"
		}],
		buttonItems : [{
			isShow: true,
			label : '查询',
			id : 'addBtn',
			onClickFunction : 'showItem()'
		},{
//			isShow: hasAuth("VOICE_SERVICECODE_ADD")==true,
			label : '生成服务号',
			id : 'addBtn',
			onClickFunction : 'createNum()'
		}],
		searchButton : false,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/fms/servicecode/manage/ajax/list',
		method : 'POST',
		data : {
			appId :'#appId',
			serviceCode : '#serviceCode',
			serviceCodeId:'#serviceCodeId',
			businessTypeId :'#businessTypeId',
			enterpriseName : '#enterpriseName',
			enterpriseNumber:'#enterpriseNumber',
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
			title : "客户编号",
			width : "6%",
			context : "@{enterpriseNumber}"
		},{
			isShow : true,
			align :'left',
			title : "客户名称",
			width : "6%",
			context : "@{enterpriseName}"
		},{
			isShow : true,
			title : "服务号Id",
			width : "5%",
			context : "@{id}"
		},{
			isShow : true,
			title : "服务号名称",
			width : "6%",
			context : "@{serviceCode}"
		},{
			isShow : true,
			title : "保存类型",
			width : "6%",
			selectors : [ {
				isShow : true,
				term : "@{saveType}",
				select : [ {
					value : 1,
					context : "可以保存"
				}, {
					value : 2,
					context : "不可保存"
				}]
			} ]
		},{
			isShow : true,
			title : "业务类型",
			width : "6%",
			context : "@{businessType}"
		},,{
			isShow : true,
			title : "内容类别",
			width : "6%",
			context : "@{contentType}"
		},{
			isShow : true,
			title : "APPID",
			width : "10%",
			align :'left',
			context : "@{appId}"
		},{
			isShow : true,
			title : "秘钥",
			width : "10%",
			align :'left',
			context : "@{secretKey}"
		},{
			isShow : true,
			title : "状态",
			width : "5%",
			selectors : [ {
				isShow : true,
				term : "@{state}",
				select : [ {
					value : 0,
					context : "启用"
				}, {
					value : 1,
					context : "停用"
				}]
			} ]
		},{
	 		isShow:true, // hasAuths("VOICE_RULECONF","VOICE_START_STOP","VOICE_SECRETKEY")==true
			width : "10%",
			title : "操作",
			selectors : [
                {
                    isShow : hasAuth("VOICE_START_STOP")==true,
                    term : "@{state}",
                    select : [{
                        value : 0,
                        context :
                            "<div class='btn_box rs' alt='停用' title='停用' onClick='disable(\"@{id}\",\"@{state}\",\"@{serviceCode}\")'><i class='iCon grey-disable'></i></div>&nbsp;"
                    } ]
                },{
                    isShow : hasAuth("VOICE_SECRETKEY")==true,
                    term : "@{state}",
                    select : [{
                        value : 0,
                        context :
                            "<div class='btn_box rs' alt='重置秘钥' title='重置秘钥' onClick='seeDetails(\"@{id}\",\"@{secretKey}\")'><i class='iCon grey-reset'></i></div>&nbsp;"                            } ]
                },{
                    isShow : hasAuth("VOICE_RULECONF")==true,
                    term : "@{state}",
                    select : [{
                        value : 0,
                        context :
                            "<div class='btn_box rs' alt='规则配置' title='规则配置' onClick='disConfirm(\"@{id}\")'><i class='iCon grey-ruleConfigurate'></i></div>&nbsp;"
                    }]
                },{
                    isShow : hasAuth("VOICE_START_STOP")==true,
                    term : "@{state}",
                    select : [{
                        value : 1,
                        context :
                            "<div class='btn_box rs' alt='启用' title='启用' onClick='disable(\"@{id}\",\"@{state}\",\"@{serviceCode}\")'><i class='iCon grey-enable'></i></div>&nbsp;"
                    } ]
                },{
                    isShow : hasAuth("VOICE_SECRETKEY")==true,
                    term : "@{state}",
                    select : [{
                        value : 1,
                        context :
                            "<div class='btn_box rs' alt='重置秘钥' title='重置秘钥' onClick='seeDetails(\"@{id}\",\"@{secretKey}\")'><i class='iCon grey-reset'></i></div>&nbsp;"                            } ]
                },{
                    isShow : hasAuth("VOICE_RULECONF")==true,
                    term : "@{state}",
                    select : [{
                        value : 1,
                        context :
                            "<div class='btn_box rs' alt='规则配置' title='规则配置' onClick='delConfirm(\"@{id}\")'><i class='iCon grey-ruleConfigurate'></i></div>&nbsp;"
                    }]
                }]
		}]
	}
}
emTable('emTableConfig');
function hasAuths(a,b,c){
    return (hasAuth(a)||hasAuth(b)||hasAuth(c));
}

/*生成服务号*/
function createNum(){
	window.location.href=WEB_SERVER_PATH + '/fms/servicecode/manage/to/create';
}
/*规则配置*/
function delConfirm(id,appId){
	window.location.href=WEB_SERVER_PATH + '/fms/servicecode/manage/to/rule?id='+id;
}
function disConfirm() {
	$.fn.tipAlert('启用状态下不能进行规则配置', 1.5, 0);
}
/*重置秘钥*/
function seeDetails(id,secretKey){
	var html ="";
	html += "<div style='height:40px; line-height:40px;'>确定要重置秘钥【"+secretKey+"】吗?</div>";
	html += "<label><span class='xing'>*</span>重置秘钥原因：</label>" 
	html += "<textarea id='reason' style='width:280px;height:60px;resize:none' maxlength='100'></textarea>";
	$.fn.tipOpen({
			title : "重置秘钥确认",
			width : '400',
			height : '120',
			concent :html,
			btn : [ {
		 		label : '确定',
		 		onClickFunction : 'resetSecretKey(\"'+id+'\")'
		 	}]				
		});
}
function resetSecretKey(id){
	var reason=$("#reason").val();
	if(reason==null || reason==""){
		$.fn.tipAlert("请输入重置秘钥原因！", 1.5, 0);
		return false;
	}
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/fms/servicecode/manage/reset",
		type:'post',
		dataType:'json',
		data:{
			id : id,
			reason : reason
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('重置秘钥成功',1,1);
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

/*启用 停用*/
function disable(id,state,serviceCode){
	if(state==0){
		var addHtml ="";
	 	addHtml = "<div>确定要停用服务号【"+serviceCode+"】?</div>";
		$.fn.tipOpen({
				title : "停用确认",
				width : '300',
				height : '27',
				concent :addHtml,
				btn : [ {
			 		label : '确定',
			 		onClickFunction : 'disEnaboff(\"'+id+'\")'
			 	}]				
			});
	}else if(state==1){
		var addHtml ="";
	 	addHtml = "<div>确定要启用服务号【"+serviceCode+"】?</div>";
		$.fn.tipOpen({
				title : "启用确认",
				width : '300',
				height : '27',
				concent :addHtml,
				btn : [ {
			 		label : '确定',
			 		onClickFunction : 'disEnabon(\"'+id+'\")'
			 	}]				
		});
	}
}
function disEnabon(id){
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/fms/servicecode/manage/ajax/on",
		type:'post',
		dataType:'json',
		data:{
			id : id 
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				if(data.message == 'success'){
					$.fn.tipAlert('启用成功',1,1);
				}
				var limit=$('.limitSelect').val();
	        	emrefulsh("emTableConfig", 0, limit);
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message, 1.5, 0);
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}

function disEnaboff(id){
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/fms/servicecode/manage/ajax/off",
		type:'post',
		dataType:'json',
		data:{
			id : id 
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				if(data.message == 'success'){
					$.fn.tipAlert('停用成功',1,1);
				}
				var limit=$('.limitSelect').val();
	        	emrefulsh("emTableConfig", 0, limit);
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message, 1.5, 0);
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}

