// JavaScript Document
$(function(){
	// 引入左侧列表
	$("#smsLeft").load("sms-left.html");
	$("#voiceleft").load("voice-left.html");
	$("#widget").click(function(){
		$('html,body').animate({scrollTop: '0px'}, 800);
	})
})
$(window).scroll(function () {
		var scrollheight=$(document).scrollTop();
		$(".documentCenter div.dc_right").css("min-height","620px");
		if(scrollheight>400){
			$("#widget").css("display","block");
		}else{
			$("#widget").css("display","none");
		}
		//当滚动到一定距离，左侧菜单固定
		/*var bignav=$(".documentCenter div.dc_left");
		if(scrollheight > 400){
			$(".documentCenter div.dc_left").css({"position":"fixed","top":"0","z-index":"9999","max-height":"620px"});
		}else{ //当滚动距离小于430的时候执行下面的内容，也就是让左侧列表恢复原状
			$(".documentCenter div.dc_left").css({"position":"static"});
		}*/
});

//左侧效果
$(document).ready(function(){
	setTimeout(function() {
		var str = window.location.href;
		var i = str.lastIndexOf("/");
		var urlStr = str.slice(str.lastIndexOf("/",i) + 1, str.length-5);
		switch(urlStr) {
			case "sms":
				$(".sms").addClass("active").siblings("h2").removeClass("active");
				break;
			case "onlysms":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				break;
			case "statuscode":
				$(".statuscode").addClass("active").siblings("h2").removeClass("active");
				break;
			case "errorcode":
				$(".errorcode").addClass("active").siblings("h2").removeClass("active");
				break;
			case "download":
				$(".download").addClass("active").siblings("h2").removeClass("active");
				break;
			case "moresms_custom":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".moresms_custom").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "moresms":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".moresms").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "personalizedsms":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".personalizedsms").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "personalizedsmsall":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".personalizedsmsall").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "getpresentation":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".getpresentation").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "getupstream":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".getupstream").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "getbalance":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".getbalance").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "onlysms_or":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".onlysms_orUL").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".onlysms_or").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "personalizedsms_or":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".onlysms_orUL").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".personalizedsms_or").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "getpresentation_or":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".onlysms_orUL").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".getpresentation_or").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "getupstream_or":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".onlysms_orUL").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".getupstream_or").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "getbalance_or":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".onlysms_orUL").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".getbalance_or").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "pushUplink":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".pushUplinkUL").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".pushUplink").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "ordiPush":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".pushUplinkUL").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".ordiPush").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "response":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".pushUplinkUL").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".response").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "statusReport":
				$("#sms").show();
				$(".onlysms").addClass("active").siblings("h2").removeClass("active");
				$(".statusReportUL").addClass("twoactive").parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				break;
				
				//语音短信
			case "voice_statuscode":
				$(".voice_statuscode").addClass("active");
				break;
			case "voice_interfacestatusreport":
				$(".voice_interfacestatusreport").addClass("active").siblings("h2").removeClass("active");
				break;
			case "join_statuscode":
				$(".join_statuscode").addClass("active").siblings("h2").removeClass("active");
				break;
			case "join_Interfacestatuscode":
				$(".join_Interfacestatuscode").addClass("active").siblings("h2").removeClass("active");
				break;
				//通知接口
			case "voice_send":
				$("#voice").show();
				$(".voice_send").addClass("active").siblings("h2").removeClass("active");
				break;
			case "voice_getReport":
				$("#voice").show();
				$(".voicesend").addClass("active").siblings("h2").removeClass("active");
				$(".voice_getReport").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "voice_balance":
				$("#voice").show();
				$(".voicesend").addClass("active").siblings("h2").removeClass("active");
				$(".voice_balance").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "voice_pushstatus":
				$("#voice").show();
				$(".voicesend").addClass("active").siblings("h2").removeClass("active");
				$(".voice_pushstatus").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
				
				//验证码接口
			case "voice_sendVFCode":
				$("#voice").show();
				$(".voicesend").addClass("active").siblings("h2").removeClass("active");
				$(".sendVFCodeurl").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".voice_sendVFCode").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "voice_VFgetReport":
				$("#voice").show();
				$(".sendVFCodeurl").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".voice_VFgetReport").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "voice_VFbalance":
				$("#voice").show();
				$(".sendVFCodeurl").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".voice_VFbalance").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "voice_VFpushstatus":
				$("#voice").show();
				$(".sendVFCodeurl").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".voice_VFpushstatus").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
				//营销接口
			case "voice_sendCTCode":
				$("#voice").show();
				$(".voicesend").addClass("active").siblings("h2").removeClass("active");
				$(".sendCTCodeurl").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".voice_sendCTCode").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "voice_CTgetReport":
				$("#voice").show();
				$(".sendCTCodeurl").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".voice_CTgetReport").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "voice_CTbalance":
				$("#voice").show();
				$(".sendCTCodeurl").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".voice_CTbalance").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "voice_CTpushstatus":
				$("#voice").show();
				$(".sendCTCodeurl").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".voice_CTpushstatus").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
				//语音短信
			case "join_send":
				$("#voice").show();
				$(".voicesend").addClass("active").siblings("h2").removeClass("active");
				$(".joinsendurl").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".join_send").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "join_getstatus":
				$("#voice").show();
				$(".joinsendurl").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".join_getstatus").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
			case "join_balance":
				$("#voice").show();
				$(".joinsendurl").addClass("twoactive").siblings('ul').show().parent().siblings().find("a").removeClass("twoactive").siblings('ul').hide();
				$(".join_balance").addClass("threeactive").parent().siblings().find("a").removeClass("threeactive");
				break;
		}
	}, 100)
	
})