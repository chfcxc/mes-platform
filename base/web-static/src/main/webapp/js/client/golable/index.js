$(function(){
//	authJump();
	// 问题反馈QQ
	var QQheight= $(window).height()/2-50;
	$("#consultationQQ").css("position","fixed");
	$("#consultationQQ").css("top",QQheight + 'px');
	var arrQQ=["http://wpa.qq.com/msgrd?v=3&uin=1439990843&site=qq&menu=yes",
"http://wpa.qq.com/msgrd?v=3&uin=81997906&site=qq&menu=yes",
"tencent://message/?uin=2222010006&amp;Site=edu.dataguru.cn&amp;Menu=yes",
"tencent://message/?uin=7211066&amp;Site=edu.dataguru.cn&amp;Menu=yes",
"tencent://message/?uin=7211066&amp;Site=edu.dataguru.cn&amp;Menu=yes"
];
	var a=parseInt(arrQQ.length*Math.random()) //获取数组下标的随机数
	arrQQ[a]; //就是要抽取数组中 的随机数字
	var serviceModules=$("#serviceModules").val();
	var arr=serviceModules.split(",");
//	var htmlsendProfile="";
	var number=0;
	for(var j=0;j<arr.length;j++){
		$(".productService li a").each(function(i,ele){
			var zhi=$(ele).attr("state");
			var id=$(ele).attr("ID");
			var name=$(ele).attr("name");
			if(zhi==0){
				$(".productService li a[state='"+zhi+"']").parent().removeClass("notOpen");
				$(".productService li a[state='"+zhi+"'] span").text("已开通");
				$(".productService li a[state='"+zhi+"']").removeAttr("onmouseover");
				$(".productService li a[state='"+zhi+"']").removeAttr("onmouseout");
				$(".productService li a[state='"+zhi+"']").attr("href","javascript:void(0)");
			}else{
				$(ele).attr("href",arrQQ[a]);
			}
			/*if(id==1){
				if(zhi==0){
					getSmsInfo();
					var smsCount=$("#smsCount").val();
//					alert(smsCount)
					htmlsendProfile+='<li><label>短信发送</label><input type="text" value="'+smsCount+'" disabled="disabled" class="greencolor"/></li>';
				}else{
					htmlsendProfile+='';
				}
			}else if(id==2){
				if(zhi==0){
					getSmsInfo();
					var flowCount=$("#flowCount").val();
					htmlsendProfile+='<li><label>流量发送</label><input type="text" value="'+flowCount+'" disabled="disabled" class="orangecolor" /></li>';
				}else{
					htmlsendProfile+='';
				}
			}else if(id==3){
				if(zhi==0){
					getSmsInfo();
					var imsCount=$("#imsCount").val();
					htmlsendProfile+='<li><label style="width:100px;">国际短信发送</label><input type="text" value="'+imsCount+'" disabled="disabled" class="orangecolor" /></li>';
				}else{
					htmlsendProfile+='';
				}
			}else if(id==4){
				if(zhi==0){
					getSmsInfo();
					var voiceCount=$("#voiceCount").val();
					htmlsendProfile+='<li><label style="width:100px;">语音发送</label><input type="text" value="'+voiceCount+'" disabled="disabled" class="orangecolor" /></li>';
				}else{
					htmlsendProfile+='';
				}
			}*/
			if(zhi==0){
				number++;
			}	
		})
		$("#openService").val(number+'个')
	}
//	$(".sendProfile ul").html(htmlsendProfile);
	
	$("#voiceservicebox .serbox .bdip").each(function(z,elez){
		var ips=$(elez).val();
		var arrip=ips.split(",");
		var voicehtml='<a class="" onclick="moreIpVoice(this)" href="javascript:void(0);" >更多</a>';
		voicehtml+='<input type="hidden" value='+ips+' />';
		if(ips!=null && arrip.length > 1){
			$(elez).val(arrip[0]+'...');
			$(elez).after(voicehtml);
		}
	})
	/*绑定ip*/
	$(".serbox .bdip").each(function(q,ele){
		var ips=$(ele).val();
		var arrip=ips.split(",");
		var html='<a class="" onclick="moreIp(this)" href="javascript:void(0);" >更多</a>';
			html+='<input type="hidden" value='+ips+' />';
		if(ips!=null && arrip.length > 1){
			$(ele).val(arrip[0]+'...');
			$(ele).after(html);
		}
	})
	
	
	/*企业信息样式*/
	var conpanyNamelen=$(".companyName textarea").val().length;
	if(conpanyNamelen > 46){
		$(".companyName textarea").css("height","74px");
		$(".companyName").css("height","60px")
	}
	comments();
	
})
function moreIpVoice(obj){
	var serviceCodeinput=$(obj).parent().parent().find(".serviceCodeinput").val();
	var ips=$(obj).next().val();
	var arrip=ips.split(",");
	var html='<div class="iptitle"><label>APPID：</label><span>'+serviceCodeinput+'</span></div><div class="iptitle"><label>绑定IP：</label></div><ul class="ipul">';
	for(var i=0;i<arrip.length;i++){
		html+='<li>'+arrip[i]+'</li>';
	}
	html+='</ul>';
	$.fn.tipOpen({
		title : "绑定IP",//弹框标题
		width : '500',//弹框内容宽度
		btn : [],//按钮是否显示
		cancel:false,
		concent : html//弹框内容
	});
	
}

function addstyle(obj){
	$(obj).addClass('active');
}
function removestyle(obj){
	$(obj).removeClass('active');
}
function problem(){
	var html='<form id="problemForm">';
	html+='<dl class="alert-cen ">';			
	html+='<dt class="item">';
	html+='<label class="item-label">反馈内容 ：</label>';
	html+='<textarea class="item-textarea" id="feedbackContent" name="feedbackContent"  placeholder="描述您的问题……" maxlength="200"></textarea>';
	html+='</dt>';
	html+='<dt class="item">';
	html+='<label class="item-label">您的邮箱 ：</label>';
	html+='<input type="text"  class="item-text" id="yourMail" name="yourMail"  placeholder="有效的邮箱地址"/>';
	html+='</dt>';
	html+='<dt class="item">';
	html+='<label class="item-label">手机号 ：</label>';
	html+='<input type="text" class="item-text" id="phone" name="phone"  placeholder="输入手机号"/>';
	html+='</dt>';
	html+='<dt class="item">';
	html+='<label class="item-label">QQ ：</label>';
	html+='<input type="text" class="item-text" id="QQ" name="QQ"  placeholder="输入QQ号"/>';
	html+='</dt>';
	html+='</dl>';
	html+='<div class="tipFoot">';
	html+='<div id="serverperson"><span>400-779-7255</span></div>';
	html+='<button onclick="problemSave()" type="submit" class="tipBtn" id="saveBtn">提交反馈</button>';
	html+='</div>';
	html+='</form>';
	$.fn.tipOpen({
		title : "问题反馈",//弹框标题
		width : '500',//弹框内容宽度
		btn : [],//按钮是否显示
		cancel:false,
		concent : html//弹框内容
	});

}

function problemSave(){
	var $formCheckOut = $('#problemForm');
	var validator =$formCheckOut.validate({
			rules:{
				feedbackContent:{ 
					required:true,
					maxlength:200
				},
				yourMail:{
					required:false,
					//problemEmail:true
				},
				phone:{
					required:false,
					//phone:true
				},
				QQ:{
					required:false,
					//QQ:true
				}
			},
			messages:{
				feedbackContent:{ 
					required:"请填写反馈内容",
					maxlength :$.validator.format("反馈内容不能超过200字")            
				}
			},
		submitHandler: function() {
			var content = $("#feedbackContent").val();
			var email = $("#yourMail").val();
		    var mobile = $("#phone").val();
			var qq = $("#QQ").val();
	     	$.ajax({
				url:  DOMAIN_PATH + "/client/sys/client/feedback/save?randomdata="+Date.parse(new Date()),
				type:'post',
				dataType:'json',
				data:{
					content  : content ,
					email  : email ,
					mobile  : mobile ,
					qq  : qq 
				},
				success:function(data){
    				if (data.success) {
    					$.fn.tipAlert('操作成功',1,1);
    					window.location.reload();
    				}else {
    					$.fn.tipAlert(data.message, 1.5, 0);
    					validator.resetForm();
    				}
				},
				error:function(){
					$.fn.tipAlert('系统异常',1.5,0);
				}
			})
		}
	 })
}

// 获取已经开通的服务
var businessList = [];
function getBusinessList() {
	$.ajax({
		url:  WEB_SERVER_PATH + "/ajax/getBusinessList",
		type:'post',
		dataType:'json',
		data:{},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				businessList = data.result;
				getSmsInfo();
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
getBusinessList();

// 页面显示
function getSmsInfo(){
	var hrefPath = $('#hrefPath').val();
	for(var t=0; t<businessList.length;t++) {
		var serviceHtml = '';
		var htmlsendProfile="";
		htmlsendProfile+='<li><label style="width:100px;">'+businessList[t].businessNameSimple+'发送</label><input id="send'+businessList[t].businessCode+'" type="text" value="0" disabled="disabled" class="greencolor"/></li>';
		$(".sendProfile ul").append(htmlsendProfile);
		serviceHtml += '<div class="wrapBox clear allwrap"  id="'+businessList[t].businessCode+'servicebox">'+
						'<h2>'+businessList[t].businessNameSimple+'服务号</h2><a href="'+hrefPath+businessList[t].businessCode+'/'+businessList[t].businessCode+'/client/servicecode" class="bindService" >查看更多</a>'+
						'<ul class="serviceNumber clear" id="'+businessList[t].businessCode+'serviceNumber">'+	
							'</ul>'+
						'</div>'
		$('#allservicebox').append(serviceHtml);
		$.ajax({
			url:  WEB_SERVER_PATH + businessList[t].businessCode+"/basesupport/getServiceCodeInfo",
			type:'post',
			dataType:'json',
			async: false,
			success:function(data){
				if (data.success) {
					var smsdata=data.result.clientInfo;
					var count = data.result.clientInfo.count;
					var server=data.result.serviceCodeList;
					var serviceModules=smsdata.serviceModules;
					if (server.length > 0) {
						$("#send" + businessList[t].businessCode).val(smsdata.count)
						$("#serviceModules").val(serviceModules);
						var html='';
						for(var k=0;k<server.length;k++){
							html+='<li class='+businessList[t].businessCode+'>'+
							'<input type="hidden" value='+server[k].type+'/>'+
							'<div class="serbox">';
							for (var a in server[k]) {
								if(server[k][a].value== null || server[k][a].value==""){
									html+='<div class="'+server[k][a].key+'"><label>'+server[k][a].name+'</label><input class="bdip" type="text"  disabled="disabled" /></div>';
								}else{
									html+='<div class="'+server[k][a].key+'"><label>'+server[k][a].name+'</label><input class="ipvalue" type="hidden" value='+server[k][a].value+' /><input id="'+businessList[t].businessCode+server[k][a].key+'" type="text" value='+server[k][a].value+' disabled="disabled"/></div>';
								}
							}
							html+='</div>'+
							'</li>';
							var morehtml = '<a class="" onclick="moreIp(this)" href="javascript:void(0);" >更多</a>';
						}
						$("#"+businessList[t].businessCode+"serviceNumber").append(html);
						$('#'+businessList[t].businessCode+'ip').after(morehtml);
					} else {
						$("#"+businessList[t].businessCode+"servicebox").hide();
					}
				}else {
					$.fn.tipAlert(data.message, 1.5, 0);
				}
			},
			error:function(){
				$("#"+businessList[t].businessCode+"servicebox").hide();
			}
		})
	}
}
function moreIp(obj){
	var serviceCodeinput=$(obj).parent().parent().first().find(".ipvalue").val();
	var ipval = $(obj).parent().find('.ipvalue').val()
	var arrip=ipval.split(",");
	var html='<div class="iptitle"><label>服务号：</label><span>'+serviceCodeinput+'</span></div><div class="iptitle"><label>绑定IP：</label></div><ul class="ipul">';
		for(var i=0;i<arrip.length;i++){
			html+='<li>'+arrip[i]+'</li>';
		}
	html+='</ul>';
	$.fn.tipOpen({
		title : "绑定IP",//弹框标题
		width : '500',//弹框内容宽度
		btn : [],//按钮是否显示
		cancel:false,
		concent : html//弹框内容
	});
}

/*function authJump(){
	if(AUTHS["SYS_CLIENT_SERVICECODE"] == null){
		$("#servicebox .serviceNumber").hide();
		$("#servicebox .bindService").hide();
	}
	if(AUTHS["FLOW_SERVICECODE"] == null){
		$("#flowservicebox .serviceNumber").hide();
		$("#flowservicebox .bindService").hide();
	}
	if(AUTHS["IMS_CLIENT_SERVICECODE"] == null){
		$("#imsservicebox .serviceNumber").hide();
		$("#imsservicebox .bindService").hide();
	}
}*/
