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
		window['emTableConfig'].ajaxConfig.data.serviceCodeId=$("#serviceCodeId").val();
		window['emTableConfig'].ajaxConfig.data.startTime=$("#startTime").val(); 
		window['emTableConfig'].ajaxConfig.data.endTime=$("#endTime").val(); 
		window['emTableConfig'].ajaxConfig.data.title=$("#title").val();
		window['emTableConfig'].ajaxConfig.data.businessType=$("#businessType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.saveType=$("#saveType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.contentTypeId=$("#businessTypeId option:selected").val();
		window['emTableConfig'].ajaxConfig.data.messageType=$("#messageType option:selected").val(); 
		window['emTableConfig'].ajaxConfig.data.submitType=$("#submitType option:selected").val(); 
		emTable('emTableConfig');
		$("#serviceCodeId").val(window['emTableConfig'].ajaxConfig.data.serviceCodeId); 
		$("#startTime").val(window['emTableConfig'].ajaxConfig.data.startTime);
		$("#endTime").val(window['emTableConfig'].ajaxConfig.data.endTime); 
		$("#title").val(window['emTableConfig'].ajaxConfig.data.title); 
		$("#businessType").val(window['emTableConfig'].ajaxConfig.data.businessType);
		$("#saveType").val(window['emTableConfig'].ajaxConfig.data.saveType); 
		$("#businessTypeId").val(window['emTableConfig'].ajaxConfig.data.contentTypeId); 
		$("#messageType").val(window['emTableConfig'].ajaxConfig.data.messageType); 
		$("#submitType").val(window['emTableConfig'].ajaxConfig.data.submitType);
	}
}


var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true,
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [ {
			label : '服务号ID',
			id : 'serviceCodeId',
			type : 'input'
		},{
	    	  isShow : true,
	          label : '提交时间',
	          id : 'startTime',
	          type : 'date',// 时间，
		      dateFmt:'yyyy-MM-dd HH:mm:ss',
		      startDate:'%y-%M-%d 00:00:00',
		      maxDateId : 'endTime'   
		},{
			label : '&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;&nbsp;',
			id : 'endTime',
			type : 'date',// 时间，
	        dateFmt:'yyyy-MM-dd HH:mm:ss',
	        startDate:'%y-%M-%d 23:59:59',
	        minDateId : 'startTime'
		 },{
			label : '闪推标题',
			id : 'title',
			type : 'input'
		},{
			isShow : true,
			label : '业务类型',
			id : 'businessType',
			type :"include"
		},{
			isShow : true,
			label : '<span class="searchspan">保存类型</span>',
			id : 'saveType',
			type : 'include'
		},{
			isShow : true,
			label : '内容类别',
			id : 'businessTypeId',
			type :"include"
		},{
			label : '<span class="searchspan">信息类型</span>',
			id : 'messageType',
			type : 'select',
			options : [ {
				context : '--请选择--',
				value : ''
			}, {
				context : '普通',
				value : '0'  
			}, {
				context : '个性',
				value : '1'
			}]
		},{
			label : '<span class="searchspan">提交方式</span>',
			id : 'submitType',
			type : 'select',
			options : [ {
				context : '--请选择--',
				value : ''
			}, {
				context : '接口',
				value : '1'  
			}, {
				context : '页面',
				value : '2'
			}]
		}
		],
		buttonItems : [ {
			isShow: true,
			label : '查询',
			id : 'addBtn',
			onClickFunction : 'showItem()'
		}],
		searchButton : false,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/fms/reported/audit/list',
		method : 'POST',
		data : {
			title:'#title',
			serviceCodeId:'#serviceCodeId',
			saveType:'#saveType',
			messageType:'#messageType',
			submitType:'#submitType',
			startTime:'#startTime',
			endTime:'#endTime',
			contentTypeId:'#businessTypeId'
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
			title : "服务号ID",
			width : "5%",
			context : "@{serviceCodeId}"
		},{
			isShow : true,
			title : "闪推标题",
			width : "5%",
			align:'left',
			context : "@{templateName}" //'<a href="jacascript:;">@{templateName}</a>'
		},{
			isShow : true,
			title : "报备内容",
			width : "8%",
			align:'left',
			context : '@{content}',
		},{
			isShow : true,
			title : "业务类型",
			width : "5%",
			align:'left',
			context : '@{businessType}'
		},{
			isShow : true,
			title : "保存类型",
			width : "5%",
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
			title : "内容类别",
			width : "6%",
			align:'left',
			context : '@{contentType}'
		},{
			isShow : true,
			title : "信息类型",
			width : "3%",
			selectors : [ {
				isShow : true,
				term : "@{templateType}",
				select : [ {
					value : 0,
					context : "普通"
				}, {
					value : 1,
					context : "个性"
				}]
			} ]
		},{
			isShow : true,
			title : "提交方式",
			width : "3%",
			selectors : [ {
				isShow : true,
				term : "@{sendType}",
				select : [ {
					value : 2,
					context : "页面"
				}, {
					value : 1,
					context : "接口"
				}]
			} ]
		},{
			isShow : true,
			title : "提交时间",
			width : "8%",
			context : "@{submitTime}"
			
		},{
			isShow : true,
			title : "报备通道",
			width : "7%",
			align:'left',
			context : '<div><p>移动：@{cmccChannelName}</p><p>联通：@{cuccChannelName}</p><p>电信：@{ctccChannelName}</p></div>',
		},{
			isShow : true,
			title : "模板ID",
			width : "7%",
			align:'left',
			context : '<div><p>移动：@{cmccTemplateId}</p><p>联通：@{cuccTemplateId}</p><p>电信：@{ctccTemplateId}</p></div>',
		},{
			isShow : true,
			title : "报备类型",
			width : "6%",
			align:'left',
			context : '<div class="tablereport"><p>移动：@{cmccAuditStateDesc}</p><p>联通：@{cuccAuditStateDesc}</p><p>电信：@{ctccAuditStateDesc}</p></div>',
		},{
			width : "5%",
			title : "操作",
			selectors : [
				{
					isShow : true, //hasAuth("OPER_SMS_SERVICECODE_PRICE_UPDATE")==true
					term : "@{templateId}",
					select : [ {
						value :"@{templateId}",
						context :  "<div class='btn_box rs' alt='回填模板ID' title='回填模板ID' onClick='backWriteId(\"@{cmccReportType}\",\"@{cuccReportType}\",\"@{ctccReportType}\",\"@{templateId}\",\"@{cmccChannelId}\",\"@{cuccChannelId}\",\"@{ctccChannelId}\")'><i class='iCon grey-back'></i></div>&nbsp;"
					} ]
				},{
					isShow : true, //hasAuth("OPER_SMS_SERVICECODE_PRICE_UPDATE")==true
					term : "@{templateId}",
					select : [ {
						value :"@{templateId}",
						context :  "<div class='btn_box rs' alt='重新报备' title='重新报备' onClick='backreport(\"@{cmccAuditState}\",\"@{cuccAuditState}\",\"@{ctccAuditState}\",\"@{templateId}\",\"@{cmccChannelId}\",\"@{cuccChannelId}\",\"@{ctccChannelId}\")'><i class='iCon grey-reset'></i></div>&nbsp;"
					} ]
				}]
		}]
	}
}
emTable('emTableConfig');

//回填模板ID
function backWriteId(cmccReportType,cuccReportType,ctccReportType,templateId,cmccChannelId,cuccChannelId,ctccChannelId){
	var html ="";
	if (cmccReportType == 1 || cmccReportType == 3) {
		html += `
			<div style="display:block;">
			   <div class="item">
		            <label class="item-label">移动：&nbsp;&nbsp;</label>
		            <input type="hidden" data-code="CMCC" value="`+cmccChannelId+`" />
		            <select id="cmccBack" class="backselect" name="cmccBack" onclick="chooseState(this)">
						<option value="">--请选择--</option>
			        	<option value="2">通过</option>
			        	<option value="3">拒绝</option>
			        	<option value="4">不支持</option>
				   </select>
		       </div>
		       <div class="item" style="display:none;">
		            <label class="item-label">回填模板ID</label>
		            <input type="text" style="width:100px;" id="cmccTemplateId" name="cmccTemplateId" value="" class="item-text" />
		       </div>
		    </div>
		`
	}
	if (cuccReportType == 1 || cuccReportType == 3) {
		html += `
			<div style="display:block;">
		       <div class="item">
		            <label class="item-label">联通：&nbsp;&nbsp;</label>
		            <input type="hidden" data-code="CUCC" value="`+cuccChannelId+`" />
		            <select id="cuccBack" class="backselect" name="cuccBack" onclick="chooseState(this)">
						<option value="">--请选择--</option>
			        	<option value="2">通过</option>
			        	<option value="3">拒绝</option>
			        	<option value="4">不支持</option>
				   </select>
		       </div>
		       <div class="item" style="display:none;">
		            <label class="item-label">回填模板ID</label>
		            <input type="text" style="width:100px;" id="cuccTemplateId" name="cuccTemplateId" value="" class="item-text" />
		       </div>
		    </div>
		`
	}
	if (ctccReportType == 1 || ctccReportType == 3) {
		html += `
			 <div style="display:block;">
		       <div class="item">
		            <label class="item-label">电信：&nbsp;&nbsp;</label>
		            <input type="hidden" data-code="CTCC" value="`+ctccChannelId+`" />
		            <select id="ctccBack" class="backselect" name="ctccBack" onclick="chooseState(this)">
						<option value="">--请选择--</option>
			        	<option value="2">通过</option>
			        	<option value="3">拒绝</option>
			        	<option value="4">不支持</option>
				   </select>
		       </div>
		       <div class="item" style="display:none;">
		            <label class="item-label">回填模板ID</label>
		            <input type="text" style="width:100px;" id="ctccTemplateId" name="ctccTemplateId" value="" class="item-text" />
		       </div>
		    </div>
		`
	}
	
	if (html != "") {
		$.fn.tipOpen({
			title : "回填模板ID",
			width : '450',
			concent :html,
			btn : [ {
		 		label : '确定',
		 		onClickFunction : 'backWriteIdTure(\"'+templateId+'\",\"'+cmccChannelId+'\",\"'+cuccChannelId+'\",\"'+ctccChannelId+'\")'
		 	}]				
		});
	} else {
		$.fn.tipAlert('暂无需要回填模板ID的数据',1.5,0);
	}
}

// 选择回填状态
function chooseState(obj) {
	if ($(obj).val() == 2) {
		$(obj).parent().next().show()
	} else {
		$(obj).parent().next().hide()
	}
}

//回填确定事件
function backWriteIdTure(templateId, cmccChannelId, cuccChannelId, ctccChannelId){
	var reportData = '';
	// CMCC,channelTemplateId,channelId,state#CUCC,channelTemplateId,channelId,state#CTCC,channelTemplateId,channelId,state
//	reportData = 'CMCC,'+$("#cmccTemplateId").val()+','+cmccChannelId+','+$("#cmccBack").val()+'#CUCC,'+$("#cuccTemplateId").val()+','+cuccChannelId+','+$("#cuccBack").val()+'#CTCC,'+$("#ctccTemplateId").val()+','+ctccChannelId+','+$("#ctccBack").val()
	$('.backselect').each(function(i,ele){
		reportData += $(ele).siblings('input').data('code')+','+$(ele).parent().next().find('input').val()+','+$(ele).siblings('input').val()+','+$(ele).val()+'#'
	})
	reportData = reportData.substring(0,reportData.length-1)
	$.fn.tipLodding();
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/reported/audit/reportoffline',
		type : 'post',
		dataType : 'json',
		data : {
			templateId: templateId,
			reportData:reportData
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('回填成功',1,1);
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

// 重新报备
function backreport(cmccAuditState,cuccAuditState,ctccAuditState,templateId, cmccChannelId, cuccChannelId, ctccChannelId) {
	var html = "";
	if (cmccAuditState == 7) {
		html += `
			<div style="display:block;">
		       <div class="item">
		            <label class="item-label">移动：</label>
		            <input type="hidden" data-code="CMCC" value="`+cmccChannelId+`" />
		            <a href="javascript:;" style="border: 1px solid #ccc; padding: 5px 15px; border-radius:5px;text-decoration: underline;" onclick="backTemplateTure(this,\'`+templateId+`\')">重新报备</a>
		       </div>
		    </div>
		`
	}
	if (cuccAuditState == 7) {
		html += `
			<div style="display:block;">
		       <div class="item">
		            <label class="item-label">联通：</label>
		            <input type="hidden" data-code="CUCC" value="`+cuccChannelId+`" />
		            <a href="javascript:;" style="border: 1px solid #ccc; padding: 5px 15px; border-radius:5px;text-decoration: underline;" onclick="backTemplateTure(this,\'`+templateId+`\')">重新报备</a>
		       </div>
		    </div>
		`
	}
	if (ctccAuditState == 7) {
		html += `
			<div style="display:block;">
		       <div class="item">
		            <label class="item-label">电信：</label>
		            <input type="hidden" data-code="CTCC" value="`+ctccChannelId+`" />
		            <a href="javascript:;" style="border: 1px solid #ccc; padding: 5px 15px; border-radius:5px;text-decoration: underline;" onclick="backTemplateTure(this,\'`+templateId+`\')">重新报备</a>
		       </div>
		    </div>
		`
	}
	if (html != "") {
		$.fn.tipOpen({
			title : "重新报备",
			width : '260',
			concent :html,
			btn : false			
		});
	} else {
		$.fn.tipAlert('暂无需要重新报备的数据',1.5,0);
	}
}
//重新报备确定
function backTemplateTure(obj,templateId) {
	var operatorCode = $(obj).siblings('input').data('code');
	var channelId = $(obj).siblings('input').val();
	$.fn.tipLodding();
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/reported/audit/reportonline',
		type : 'post',
		dataType : 'json',
		data : {
			templateId: templateId,
			operatorCode:operatorCode,
			channelId:channelId
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('重新报备成功',1,1);
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
	})
}
