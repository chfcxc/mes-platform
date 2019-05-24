//获取光标位置
(function ($) { 
    $.fn.extend({ 
        insertAtCaret: function (myValue) { 
            var $t = $(this)[0]; 
            if (document.selection) { 
                this.focus(); 
                sel = document.selection.createRange(); 
                sel.text = myValue; 
                this.focus(); 
            } else 
                if ($t.selectionStart || $t.selectionStart == '0'){ 
                    var startPos = $t.selectionStart; 
                    var endPos = $t.selectionEnd; 
                    var scrollTop = $t.scrollTop; 
                    $t.value = $t.value.substring(0, startPos) + myValue + $t.value.substring(endPos,$t.value.length); 
                    this.focus(); 
                    $t.selectionStart = startPos + myValue.length;
                    $t.selectionEnd = startPos + myValue.length; 
                    $t.scrollTop = scrollTop; 
                } else { 
                    this.value += myValue; 
                    this.focus(); 
                } 
        } 
    }) 
})(jQuery); 
$(function(){
	$('.card li').eq(0).click(function(){
		$(this).addClass('li-on').siblings().removeClass('li-on');
		$(this).parent().siblings('.card-cen').hide().eq(0).show();
	});
	$('.card li').eq(1).on('click', function(){ 
		$(this).addClass('li-on').siblings().removeClass('li-on');
		$(this).parent().siblings('.card-cen').hide().eq(1).show();
	}); 
});
	function gettemplate(){
		var typehtml='<input type="hidden" id="templateType" />'
			$("#templateTypeBox").html(typehtml)
		if($(".li-on").text()=='个性发送'){
			$("#templateType").val('1');
		}else{
			$("#templateType").val('0');
		}
		var html="";
		html='<div id="innerDiv"></div>';
		$.fn.tipOpen({
			title : "选择模板",
			width : '760',
			height:'',
			concent :html,
			btn : [{
				label : '保存',
				onClickFunction : 'turnTrue()'
			}],
		});
		 emTableConfigtwo={
				outerDivId : "innerDiv",
				pagesShow : true, 
				totalNumbersShow:true,
				searchConfig : {
					searchItems : [ {
						label : '模板名称',
						id : 'templateNameInput',
						type : 'input'
					},{
						label : '模板内容',
						id : 'content',
						type : 'input'
					},{
						label : '',
						id : 'templateType',
						type : 'include'
					}],
					searchButton : true,
					resetButton : false
				},
				ajaxConfig : {
					url : WEB_SERVER_PATH + '/fms/client/messagesend/ajax/gettemplate',
					method : 'POST',
					data : {
						templateName:'#templateNameInput',
						content :'#content',
						templateType:'#templateType'
					},
					startType : "startNum",
					startParams : "start",
					limitParams : "limit",
					defaultLimit : 10,
					supportLimit : [ 10, 20, 50 ],
					result : {
						dataArray : "result.list",
						totalCount : "result.totalCount",
						totalPageNum : "result.totalPage",
						currentPageNum : "result.currentPageNum"
					}
				},
				tableConfig: {
					isNeedIndexRow : false,
					rowItems : [{
								title : "",
								width : "5%",
								context : "<input type='radio'  name='savlue' serviceCodeId='@{serviceCodeId}' contentTypeId='@{contentTypeId}' appId='@{appId}' content='@{content}' templateId='@{templateId}' templateName='@{templateName}' serviceCode='@{serviceCode}' remark='@{remark}' class='check' saveType='@{saveType}' businessType='@{businessType}' contentType='@{contentType}' ctccAuditState=@{ctccAuditState} cuccAuditState='@{cuccAuditState}' cmccAuditState='@{cmccAuditState}' />"
							},{
								title : "模板名称",
								width : "10%",
								context: "@{templateName}"
							},{
								title : "业务类型",
								width : "10%",
								context : "@{businessType}"
							},{
								title : "内容类型",
								width : "10%",
								context : "@{contentType}"
							},{
								title : "模板内容",
								width : "40%",
								context : "@{content}"
							}]
				}
			}
		emTable('emTableConfigtwo');
}
function turnTrue(){
	var servicecode=$("input[type='radio']:checked").attr('servicecode');
	var businessType=$("input[type='radio']:checked").attr('businesstype');
	var CmccAuditState=$("input[type='radio']:checked").attr('cmccAuditState');
	var CuccAuditState=$("input[type='radio']:checked").attr('cuccAuditState');
	var CtccAuditState=$("input[type='radio']:checked").attr('ctccAuditState');
	var saveType=$("input[type='radio']:checked").attr('saveType');
	var contentType=$("input[type='radio']:checked").attr('contenttype');
	var content=$("input[type='radio']:checked").attr('content');
	var appId=$("input[type='radio']:checked").attr('appId');
	var templateName=$("input[type='radio']:checked").attr('templateName');
	var templateId=$("input[type='radio']:checked").attr('templateId');
	var contentTypeId=$("input[type='radio']:checked").attr('contentTypeId');
	var serviceCodeId=$("input[type='radio']:checked").attr('serviceCodeId');
	if($("input[type='radio']").is(':checked')==false) {
		$.fn.tipAlert("请选择模板",1.5,0);
		return false;
	}
	if(content!="" || content!=null){
		var html='<div style="width:100%;height:150px;">'+content+'</div><div class="phone-topBtn"><a>关闭</a><a>保存</a></div>';
		$("#simulation").html(html);
		$("#simulation-bottom").text('字数：'+content.length+'');
	}
	var Ps='';
	if($(".li-on").text()=='个性发送'){
		var Ps='Ps';
	}
	$('#serviceCode'+Ps).text(servicecode);
	$('#businessType'+Ps).text(businessType);
	$('#contenttype'+Ps).text(contentType);
	
	$("#templateName"+Ps).val(templateName);
	$("#templateName"+Ps).next('span').html(content);
	$("#appId"+Ps).val(appId);
	$("#templateId"+Ps).val(templateId);
	$("#templateContent"+Ps).val(content);
	$("#contentTypeId"+Ps).val(contentTypeId);
	$("#serviceCodeId"+Ps).val(serviceCodeId);
	var savecont='';
	if(saveType=1){
		savecont='可保存';
	}else if(saveType=2){
		savecont='不可保存';
	}
	$('#saveType'+Ps).text(savecont);
	AuditState(CmccAuditState,'Cmcc',Ps);
	AuditState(CuccAuditState,'Cucc',Ps);
	AuditState(CtccAuditState,'Ctcc',Ps);
}
function AuditState(state,operator,Ps){
	var AuditStatecont='';
	if(state==0){
		AuditStatecont='提交报备';
	}else if(state==1){
		AuditStatecont='报备中';
	}else if(state==2){
		AuditStatecont='报备成功';
	}else if(state==3){
		AuditStatecont='报备失败';
	}else if(state==4){
		AuditStatecont='不支持';
	}else if(state==5){
		AuditStatecont='不支持';
	}else if(state==6){
		AuditStatecont='未配置通道';
	}else if(state==7){
		AuditStatecont='未报备';
	}
	$("#operator"+operator+Ps).text(AuditStatecont);
	$('.tipBorder').remove();
	$('.layer').remove();
}
//定时发送
//function chooseSendMehod(){
//	if($("#delayedSending").is(":checked")){
//		delayedSending(this);
//		$("#delayedTime").show();
//	}else{
//		$("#delayedTime").hide();
//		$("#timerTimeRule").val("");
//	}
//}
//function chooseSendMehodPer(){
//	if($("#delayedSending-personal").is(":checked")){
//		delayedSendingPersonal(this);
//		$("#personal-delayedTime").show();
//	}else{
//		$("#personal-delayedTime").hide();
//		$("#personal-timerTimeRule").val("");
//	}
//}
//个性群发
//function chooseSendMehodBatch(){
//	if($("#delayedSending-batch").is(":checked")){
//		delayedSendingBatch(this);
//		$("#batch-delayedTime").show();
//	}else{
//		$("#batch-delayedTime").hide();
//		$("#batch-timerTimeRule").val("");
//	}
//}
////普通-定时发送
//function delayedSending(obj){
//	var later10date = timelateryyyyMMddHHmmss(minTimer);
//	$("#timerTimeRule").attr("onfocus","WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\',maxDate:getnowtimes(),minDate:\""+later10date+"\"})");
//}
////批量--定时发送
//function delayedSendingPersonal(obj){
//	var later10date = timelateryyyyMMddHHmmss(minTimer);
//	$("#personal-timerTimeRule").attr("onfocus","WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\',maxDate:getnowtimes(),minDate:\""+later10date+"\"})");
//}
////个性-定时发送
//function delayedSendingBatch(obj){
//	var later10date = timelateryyyyMMddHHmmss(minTimer);
//	$("#batch-timerTimeRule").attr("onfocus","WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\',maxDate:getnowtimes(),minDate:\""+later10date+"\"})");
//}
//function docheckTime(timerTimeRule){
//	timerTimeRule=timerTimeRule.replace(/-/g,"/");
//	var start = new Date(timerTimeRule).getTime();
//    var end = new Date().getTime();
//	if (start-end <= minTimer*60*1000) {
//		$.fn.tipAlert("发送时间应晚于当前时间"+minTimer+"分钟.",1.5,0);
//		return false;
//	}else{
//		return true;
//	}
//}
//function timelateryyyyMMddHHmmss(minuteslater){
//	var date = new Date().getTime();
//	if(minuteslater){
//		date = date + (minuteslater * 60 * 1000);
//		date = new Date(date);
//	}
//    var seperator1 = "-";
//    var seperator2 = ":";
//    var month = date.getMonth() + 1;
//    if (month >= 1 && month <= 9) {
//        month = "0" + month;
//    }
//    var strDate = date.getDate();
//    if (strDate >= 0 && strDate <= 9) {
//        strDate = "0" + strDate;
//    }
//    var hour =  date.getHours();
//    if (hour >= 0 && hour <= 9) {
//    	hour = "0" + hour;
//    }
//    var minutes =  date.getMinutes();
//    if (minutes >= 0 && minutes <= 9) {
//    	minutes = "0" + minutes;
//    }
//    var sec = date.getSeconds();
//    if (sec >= 0 && sec <= 9) {
//    	sec = "0" + sec;
//    }
//    return date.getFullYear() + seperator1 + month + seperator1 + strDate + " " + hour + seperator2 + minutes + seperator2 + sec;
//}
///*获取3个月的时间*/
//function getnowtimes() {
//	var timestamp = new Date().getTime();
//	var day30=7776000000;//3个月的毫秒数
//	var tm=timestamp+day30;
//	var nowtime=new Date(tm);
//    var year = nowtime.getFullYear();
//    var month = padleft0(nowtime.getMonth() + 1);
//    var day = padleft0(nowtime.getDate());
//    var hour = padleft0(nowtime.getHours());
//    var minute = padleft0(nowtime.getMinutes());
//    var second = padleft0(nowtime.getSeconds());
//    var millisecond = nowtime.getMilliseconds(); millisecond = millisecond.toString().length == 1 ? "00" + millisecond : millisecond.toString().length == 2 ? "0" + millisecond : millisecond;
//    return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second ;
//}
/*==============================普通发送begin=====================================*/
//手机号格式校验
function countMobile(){
	var mobiles = $("#mobiles").val();
	mobiles = mobiles.replace(/,|\n|\r|\s+|，/g,',');
	if(mobiles.lastIndexOf(",") == mobiles.length-1 || mobiles.lastIndexOf("，") == mobiles.length-1 || mobiles.lastIndexOf("\\s+") == mobiles.length-1 || mobiles.lastIndexOf("\\r") == mobiles.length-1){ 
		mobiles = mobiles.substr(0,mobiles.length-1);
	}
	var mobilesArray = mobiles.split(",");
	if(mobilesArray.length>0){
		//校验数据
		for(var i=0;i<mobilesArray.length;i++){
			if(!validate.notNull(mobilesArray[i])){
				continue;
			}
			if(!validate.phone(mobilesArray[i])){
				$.fn.tipAlert("手机号格式错误",1.5,0);
				return false;
			}
		}
		if(mobilesArray.length>1000){
 			$.fn.tipAlert("手机号码最多可以输入1000个",1.5,0);
 			return false;
		}
	}	
}
//手机号码去重
function unique(){
	var mobiles = $("#mobiles").val();
	mobiles = mobiles.replace(/,|\n|\r|\s+|，/g,',');
	if($("#mobiles").val()!=""){
		$("#mobiles").val(unique1(mobiles));
	}else{
		$.fn.tipAlert("请输入手机号码",1.5,0);
		return false;	
	}
}

function unique1(content) {
	var ss = content.split(",");
	var uq = {};
	var rq = [];
	for ( var i = 0; i < ss.length; i++) {
		if (!uq[ss[i]]) {
			uq[ss[i]] = true;
			rq.push(ss[i]);
		}
	}
	var result;
	result = rq.join(",");
	return result;
}
//清空--手机号、内容
function emptyContent(obj){
	$(obj).parent().find("textarea").val("");
	$('#simulation,#simulation-bottom,#warning').html('');
}
//上传 动作
$("#fileId").on("click",function(e){
	var ie = (navigator.appVersion.indexOf("MSIE")!=-1); //判断IE10以及以下
	var file = document.getElementById('fileId') ; 
    if( ie ){
    	var form=document.createElement('form');
        document.body.appendChild(form);
        var pos=file.nextSibling;
        form.appendChild(file);
        form.reset();
        pos.parentNode.insertBefore(file,pos);
        document.body.removeChild(form);
    }else{
        $("#fileId").val("");
    }
});
$("#fileIdPs").on('click',function(e){
	var ie = (navigator.appVersion.indexOf("MSIE")!=-1); //判断IE10以及以下
	var file = document.getElementById('fileIdPs') ; 
    if( ie ){
    	var form=document.createElement('form');
        document.body.appendChild(form);
        var pos=file.nextSibling;
        form.appendChild(file);
        form.reset();
        pos.parentNode.insertBefore(file,pos);
        document.body.removeChild(form);
    }else{
    	$("#fileIdPs").val("");
    }
});

$("#fileIdBatch").on('click',function(e){
	var ie = (navigator.appVersion.indexOf("MSIE")!=-1); //判断IE10以及以下
	var file = document.getElementById('fileIdBatch') ; 
    if( ie ){
    	var form=document.createElement('form');
        document.body.appendChild(form);
        var pos=file.nextSibling;
        form.appendChild(file);
        form.reset();
        pos.parentNode.insertBefore(file,pos);
        document.body.removeChild(form);
    }else{
    	$("#fileIdBatch").val("");
    }
});
$("#batchUploadBtn").on('click',function(e){
	var ie = (navigator.appVersion.indexOf("MSIE")!=-1); //判断IE10以及以下
	var file = document.getElementById('fileIdBatch') ; 
    if( ie ){
    	var form=document.createElement('form');
        document.body.appendChild(form);
        var pos=file.nextSibling;
        form.appendChild(file);
        form.reset();
        pos.parentNode.insertBefore(file,pos);
        document.body.removeChild(form);
    }else{
    	$("#fileIdBatch").val("");
    }
});
function btfile(obj,type){	
	console.log(type);
	var inputval=$(obj).val().toLowerCase();
	if(inputval == ''){
		return false;
	}
	if(!inputval=="" &&!inputval.endWith('.txt') && !inputval.endWith('.xlsx') && !inputval.endWith('.xls') && !inputval.endWith('.rar') && !inputval.endWith('.zip') && !inputval.endWith('.tar.gz') ){
		$.fn.tipAlert('不支持的文件格式，请重新上传',1,2);
		return false;
	}
	var st = inputval.split("/");
	var stt = st[st.length - 1];
	var si = stt.split("\\");
	var sii = si[si.length - 1]; 
	$(obj).parent().parent().find('input.uPload-text').val(sii);
	$(obj).removeAttr("onchange");
	$(obj).attr("onchange",'btfile(this,type)');
	if(type==1){
		$("#fileIdName").text($("#fileId").val());
	}else{
		$("#fileIdNamePs").text($("#fileIdPs").val());
	}
	
}
//模版下载
function templateDown(){
	window.open(WEB_STATIC_PATH+"/template/commonSend.xlsx","_self");
}
/*function sendContent() {
	var sizeleng;
	var content=$("#sendContent").val();
	var sign = $("#serviceCodeSign").val();
	if(sign!=""){
		lastContent = '【'+sign+'】' + content;
	}else{
		lastContent = content;
	}
	sizeleng = lastContent.length;
	if(content!=''){
		$("#simulation").html(lastContent.replace(/</g,'&lt;'));
		if(content.length>1000){
			$.fn.tipAlert('内容超过1000字不能发送',1.5,0);
			$("#warning").text('内容超过1000字不能发送');
		}
		else{
			$("#warning").text('');
		}
		if(sizeleng<=70){
			$("#simulation-bottom").html(sizeleng+'/'+Math.ceil(sizeleng/70)+'　　'+'字/条');
		}else{
			$("#simulation-bottom").html(sizeleng+'/'+Math.ceil(sizeleng/67)+'　　'+'字/条');
		}
	}else{
		$("#simulation").html('');
		$("#simulation-bottom").html('');
		$("#warning").text('');
	}		
}*/
//发送短信
function sendMessage(){
	var fileId = 'fileId';
	if($('#fileId').val() === ''){
		fileId = 'null';
	}
//	var sendContent = $('#sendContent').val();
//	sendContent = sendContent.replace(/"/g, "@EMAY34PAGE@");
//	sendContent = sendContent.replace(/'/g, "@EMAY39PAGE@");
//	sendContent = sendContent.replace(/\s+/g,' ');
	var mobiles=$('#mobiles').val().replace(/,|\n|\r|\s+|，/g,',');
	if(mobiles =="" && $('#fileId').val()==""){
		$.fn.tipAlert('请输入手机号或选择上传文件',1.5,0);
		return false;
	}
	countMobile();//手机号验证
	var templateName=$("#templateName").val();
	var appId=$("#appId").val();
	var templateId=$("#templateId").val();
	var content=$("#templateContent").val();
	var contentTypeId=$("#contentTypeId").val();
	var serviceCodeId=$("#serviceCodeId").val();
	if(templateName=="" || templateName==null){
		$.fn.tipAlert('请选择报备模板',1.5,0);
		return false;
	}
//	var sendTime=$('#timerTimeRule').val();
//	if($("#delayedSending").is(":checked")){
//		if(sendTime == ''){
//			$.fn.tipAlert('具体时间不能为空！',1.5,0);
//			return false;
//		}
//	}
//	if(""!=sendTime){
//		if(!docheckTime(sendTime)){
//			return false;
//		}
//	}
	var addHtml ="";
 	addHtml = '<div>是否确认发送该短信？</div>';
	$.fn.tipOpen({
			title : "确认",
			width : '300',
			height : '27',
			concent :addHtml,
			cancel:false,
			btn : [ {
		 		label : '确定',
		 		onClickFunction : 'reSend(\"'+fileId+'\",\"'+templateName+'\",\"'+mobiles+'\",\"'+templateId+'\",\"'+content+'\",\"'+appId+'\",\"'+contentTypeId+'\",\"'+serviceCodeId+'\")' 
		 	},{
		 		label : '取消',
		 		onClickFunction : 'cancel()'
		 	}]
	});
}
function reSend(fileId,templateName,mobiles,templateId,content,appId,contentTypeId,serviceCodeId){
//	var sendContent = $("#sendContent").val();
//	sendContent = sendContent.replace(/"/g, "@EMAY34PAGE@");
//	sendContent = sendContent.replace(/'/g, "@EMAY39PAGE@");
//	sendContent = sendContent.replace(/\s+/g,' ');
	$.fn.tipLodding();
	$.ajaxFileUpload({
		url : WEB_SERVER_PATH + '/fms/client/messagesend/send',
		secureuri : false,
		fileElementId : fileId,
		type:'post',
		data:{
			title : templateName,
			mobiles : mobiles,
			templateId : templateId,
			content : content,
			serviceCodeId:serviceCodeId,
			contentTypeId:contentTypeId,
			appId :appId
		},
		dataType:'json',
		success:function(data){
			$.fn.tipLoddingEnd(false);
			if(data.success){				
				$(".tipBorder").eq(0).remove();
				$(".layer").eq(0).remove();
				var txt = '<div class="tipBox"><div class="tipImg"></div><div class="tipText">';
				if($("#sendnow").is(":checked")){
					txt+='<dl><dt>短信设置成功</dt>';
				}
//				else if($("#delayedSending").is(":checked")){
//					txt+='<dl><dt>定时短信设置成功</dt>';
//				}
				txt += '<dd>此短信已保存至批次查询页面<a onclick="jumpPage(1)">查看发送状态</a></dd>';
				txt += '</dl></div></div>';
				$.fn.tipOpen({
					title : '提示',
					width : '350',
					height : '',
					tipClose : false,//禁止关闭按钮和点击罩层关闭(默认是true)
					btn : [{
						label : '继续写短信',
						onClickFunction : 'jumpSendStatePage()'
					}],
					cancel : false,
					concent : txt
				});
			}else{
				$.fn.tipAlert(data.message,1.5,0);
				return false;
			}
		},
		error:function(XMLHttpRequest){
			var a= XMLHttpRequest.responseXML.URL+"";
			if(a.indexOf('client/toLogin')>0){
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert('已超时，请重新登录',1.5,0);
				window.location.reload();
			}else{
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert('系统异常',1.5,0);
			}
		}
	});
}
//跳转
function jumpSendStatePage(){
	$.fn.tipShut();
	clearPt();
	$("#fileReloadPt").html("");
	var html='';
	html+='<div class="sms-item">';
	html+='<label class="item-label">导入号码文件：</label>';
	html+='<input type="text" name="fileId" class="uPload-text">';
	html+='<a id="file1" class="file" href="javascript:;">浏览...';
	html+='<input type="file" onchange="btfile(this,1)" name="fileId" id="fileId" class="input-file" title="上传文件">';
	html+='</a><button title="模板下载" onclick="templateDown()" class="tempatedown">模版下载</button></div>';
	$("#fileReloadPt").append(html);
}
//取消发送
//function clearPt(){
//	$('#mobiles,#sendContent').val('');
//	$('#simulation,#simulation-bottom,#warning,#sumTotal').html('');
//	$(".uPload-text").val("");
//	$('#sendnow').prop('checked',true);
//	$("#delayedTime").hide();
//	$("#timerTimeRule").val("");
//}

/*==============================个性群发begin=====================================*/
//模版下载
function batchTemplate(){
	window.open(WEB_STATIC_PATH+"/template/personalSend.xlsx","_self");
}
//个性上传
//function batchUpload(){
//	$("#bath-sendContent").val(""); //内容清空
//	var fileId = 'fileIdBatch';
//	if($('#fileIdBatch').parent().parent().find("input.uPload-text").val() === ''){
//		fileId = 'null';
//		$.fn.tipAlert('请选择上传号码文件',1.5,0);
//		return false;
//	}
//	$.fn.tipLoddingEnd(true);
//	$.ajaxFileUpload({
//		url: WEB_SERVER_PATH + '/sms/client/message/send/info/getTitle',
//		secureuri : false,
//		fileElementId : fileId,
//		type:'post',
//		dataType:'json',
//		success:function(data){
//			if(!data.success){
//				$.fn.tipLoddingEnd(false);
//				$.fn.tipAlert(data.message,1.5,0);
//			}else{
//				var html = '';
//				for(var i in data.result.titles){
//					html += '<span onclick="addText(this)" class="readbtns">'+data.result.titles[i]+'</span>';
//				}
//				$('#perName').html(html);
//				$("#serialNumber").val(data.result.serialNumber);//流水号
////				$('#fileIdBatch').parent().parent().find("input.uPload-text").val("");//清空已上传的号码文件
//			}
//		},
//		error:function(XMLHttpRequest){
//			var a= XMLHttpRequest.responseXML.URL+"";
//			if(a.indexOf('client/toLogin')>0){
//				$.fn.tipLoddingEnd(false);
//				$.fn.tipAlert('已超时，请重新登录',1.5,0);
//				window.location.reload();
//			}else{
//				$.fn.tipLoddingEnd(false);
//				$.fn.tipAlert('系统异常',1.5,0);
//			}
//		}
//	});
//}
//function getContentTitle(){
//	 document.getElementById('perName').style.display = ''; 
//}
//function addText(obj){
//	var text = $(obj).text();
//	text= '{#'+text+'#}';
//	 $("#bath-sendContent").insertAtCaret(text); 
//}
//个性发送短信
function sendMessagePs(){
	var fileId = 'fileIdPs';
	if($('#fileIdPs').val() === ''){
		fileId = 'null';
		$.fn.tipAlert('请选择上传文件',1.5,0);
		return false;
	}
	
	var templateName=$("#templateNamePs").val();
	var appId=$("#appIdPs").val();
	var templateId=$("#templateIdPs").val();
	var content=$("#templateContentPs").val();
	var contentTypeId=$("#contentTypeIdPs").val();
	var serviceCodeId=$("#serviceCodeIdPs").val();
	
	if(templateName=="" || templateName==null){
		$.fn.tipAlert('请选择报备模板',1.5,0);
		return false;
	}
	var addHtml ="";
 	addHtml = '<div>是否确认发送该短信？</div>';
	$.fn.tipOpen({
			title : "确认",
			width : '300',
			height : '27',
			concent :addHtml,
			cancel:false,
			btn : [ {
		 		label : '确定',
		 		onClickFunction : 'reSendPs(\"'+fileId+'\",\"'+templateName+'\",\"'+templateId+'\",\"'+content+'\",\"'+appId+'\",\"'+contentTypeId+'\",\"'+serviceCodeId+'\")' 
		 	},{
		 		label : '取消',
		 		onClickFunction : 'cancel()'
		 	}]
	});
}
function reSendPs(fileId,templateName,templateId,content,appId,contentTypeId,serviceCodeId){
	$.fn.tipLoddingEnd(true);
	$.fn.tipLodding();
	$.ajaxFileUpload({
		url:  WEB_SERVER_PATH + '/fms/client/messagesend/sendPersonal',
		secureuri : false,
		fileElementId : fileId,
		type:'post',
		data:{
			title : templateName,
			templateId : templateId,
			content : content,
			serviceCodeId:serviceCodeId,
			contentTypeId:contentTypeId,
			appId :appId
		},
		dataType:'json',
		success:function(data){
			$.fn.tipLoddingEnd(false);
			if(data.success){
				$(".tipBorder").eq(0).remove();
				$(".layer").eq(0).remove();
				clearContent();
				var txt = '<div class="tipBox"><div class="tipImg"></div><div class="tipText">';
				if($("#batch-sendnow").is(":checked")){
					txt+='<dl><dt>短信设置成功</dt>';
				}else if($("#delayedSending-batch").is(":checked")){
					txt+='<dl><dt>定时短信设置成功</dt>';
				}
				txt += '<dd>此短信已保存至批次查询页面<a onclick="jumpPage(3)">查看发送状态</a></dd>';
				txt += '</dl></div></div>';
				$.fn.tipOpen({
					title : '提示',
					width : '350',
					height : '',
					btn : [{
							label : '继续写短信',
							onClickFunction : 'jumpSendStatePageThree()'
						}],
					cancel : false,
					concent : txt
				});
			}else{
				$.fn.tipAlert(data.message,1.5,0);
				return false;
			}
		},
		error:function(XMLHttpRequest){
			var a= XMLHttpRequest.responseXML.URL+"";
			if(a.indexOf('client/toLogin')>0){
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert('已超时，请重新登录',1.5,0);
				window.location.reload();
			}else{
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert('系统异常',1.5,0);
			}
		}
	});
}
function clearContent(){
//	clearPtBatch();
	$("#serialNumber").val("");
	$("#fileReloadBatch").html("");
	var html="";
	html+='<div class="sms-item mb20">';
	html+='<label class="item-label">导入号码文件：</label>';
	html+='<input type="text" name="fileIdBatch" class="uPload-text">';
	html+='<a id="file3" class="file" href="javascript:;">浏览...<input type="file" onchange="btfile(this,3)" name="fileIdBatch" id="fileIdBatch" class="input-file" title="上传模板文件">';
	html+='</a><button title="模版下载" onclick="batchTemplate()" class="tempatedown">模版下载</button><button class="tempatedown" onclick="batchUpload()" title="上传" id="batchUploadBtn">上传</button></div>';
	$("#fileReloadBatch").append(html);
}
function jumpSendStatePageThree(){
	$.fn.tipShut();
}
////取消发送
//function clearPtBatch(){
//	$('.uPload-text').val('');
//	$("#perName").html("");
//	$('#batch-sendnow').prop('checked',true);
//	$("#batch-delayedTime").hide();
//	$("#batch-timerTimeRule").val("");
//	$("#bath-sendContent").val("");
//}
//跳转到批次查询页面
function jumpPage(type){
	if(type==1){
		window.location.href=WEB_SERVER_PATH + '/fms/client/messagebatch?serviceCode='+$("#serviceCodePs").val();//普通发送-跳转到批次页面
	}else if(type==2){
		window.location.href=WEB_SERVER_PATH + '/fms/client/messagebatch?serviceCode='+$("#PerServiceCodeValueFild").val();//批量发送-跳转到批次页面
	}else if(type =3){
		window.location.href=WEB_SERVER_PATH + '/fms/client/messagebatch?serviceCode='+$("#serviceCodePs").val();//个性发送-跳转到批次页面
	}
}
function cancel(){
	$.fn.tipShut();
}
/*===========================服务号联想begin=====================================*/
////普通发送-初始化服务号
//function getServiceCodeId() {
//    $("#serviceCode").combobox({
//    	url : WEB_SERVER_PATH + '/getServiceCode',
//        valueField: 'serviceCode',
//        textField: 'note',
//        mode: 'remote',
//		hasDownArrow:false,
//	    onSelect: function (record) {
//	    	$('#serviceCodeValueFild').val(record.serviceCode);
//        } , onChange: function (n,o) {
//        	$('#serviceCodeValueFild').val($('#serviceCode').combobox('getValue'));
//        },onHidePanel: function(){
//        	getSign();
//        }
//    });
//    $("#serviceCode").combobox('textbox').bind('focus',function(){
//		 $("#serviceCode").combobox('showPanel');
//	 });
//}
//
//
////个性发送-初始化服务号
//function getBatchServiceCodeId() {
//    $("#batch-serviceCode").combobox({
//    	url : WEB_SERVER_PATH + '/getServiceCode',
//        valueField: 'serviceCode',
//        textField: 'note',
//        mode: 'remote',
//		hasDownArrow:false,
//		onSelect: function (record) {
//			$('#batchServiceCodeValueFild').val(record.serviceCode);
//        }, onChange: function (n,o) {
//        	$('#batchServiceCodeValueFild').val($('#batch-serviceCode').combobox('getValue'));
//        }
//    });
//    $("#batch-serviceCode").combobox('textbox').bind('focus',function(){
//		 $("#batch-serviceCode").combobox('showPanel');
//	 });
//}
//服务号--签名
//function getSign(){
//	$.ajax({
//		url:  WEB_SERVER_PATH + "/sms/client/message/send/info/ajax/getsign?serviceCode="+$("#serviceCodeValueFild").val(),
//		type:'post',
//		dataType:'json',
//		data:{},
//		success:function(data){
//			if (data.success) {
//				$.fn.tipLoddingEnd(true);
//				var dataResult = data.result;
//				if(data.result!=""){
//					var sign = dataResult[0].sign;
//					$("#serviceCodeSign").val(sign);
//				}else{
//					$("#serviceCodeSign").val("");
//				}
//			} else {
//				$("#serviceCodeSign").val("");
//			}
////			sendContent();
//		},
//		error:function(){
//			$.fn.tipLoddingEnd(false);
//			$.fn.tipAlert('系统异常',1.5,2);
//		}
//
//	});
//}
//
//// 更改模板
//function changeTem(obj) {
//	var id = $(obj).val();
//	$.ajax({
//		url:  WEB_SERVER_PATH + "/sms/client/message/send/info/selectTemplateById",
//		type:'post',
//		dataType:'json',
//		data:{
//			id:id
//		},
//		success:function(data){
//			if (data.success) {
//				$("#sendContent").val(data.result.content);
//			} else {
//				$.fn.tipAlert(data.message,1.5,0);
//			}
//		},
//		error:function(){
//			$.fn.tipLoddingEnd(false);
//			$.fn.tipAlert('系统异常',1.5,2);
//		}
//
//	});
//}
//
////更改个性模板
//function changegxTem(obj) {
//	var id = $(obj).val();
//	$.ajax({
//		url:  WEB_SERVER_PATH + "/sms/client/message/send/info/selectTemplateById",
//		type:'post',
//		dataType:'json',
//		data:{
//			id:id
//		},
//		success:function(data){
//			if (data.success) {
//				$("#bath-sendContent").val(data.result.content);
//			} else {
//				$.fn.tipAlert(data.message,1.5,0);
//			}
//		},
//		error:function(){
//			$.fn.tipLoddingEnd(false);
//			$.fn.tipAlert('系统异常',1.5,2);
//		}
//
//	});
//}
//
// tab切换时获取个性模板
//function getTemplate(obj) {
//	$("#smsgxTemplate").html("");
//	var type = $(obj).data("type");
//	$.ajax({
//		url:  WEB_SERVER_PATH + "/sms/client/message/send/info/selectTemplatesByType",
//		type:'post',
//		dataType:'json',
//		data:{
//			type:type
//		},
//		success:function(data){
//			if (data.success) {
//				var list = data.result.list;
//				var html = "";
//					html +='<option value="-1">插入短信模板</option>';
//				for(var i=0;i<list.length;i++) {
//					if(list[i].userId == 0){
//						html += '<option value="'+list[i].id+'">'+ '(通用)' +list[i].templateName+'</option>';
//					}else{
//						html += '<option value="'+list[i].id+'">'+list[i].templateName+'</option>';
//					}
//				}
//				$("#smsgxTemplate").append(html);
//			} else {
//				$.fn.tipAlert(data.message,1.5,0);
//			}
//		},
//		error:function(){
//			$.fn.tipLoddingEnd(false);
//			$.fn.tipAlert('系统异常',1.5,2);
//		}
//
//	});
//}
