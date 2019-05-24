String.prototype.startWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length)
		return false;
	if (this.substr(0, str.length) == str)
		return true;
	else
		return false;
	return true;
}

String.prototype.endWith = function(str) {
	if (str == null || str == "" || this.length == 0
			|| str.length > this.length)
		return false;
	if (this.substring(this.length - str.length) == str)
		return true;
	else
		return false;
	return true;
}

$.ajaxSetup({
	complete : function(xhr, status) {
		var str = xhr.responseText;
		try {
			if (str.startWith('<')) {
				str = str.substring(str.indexOf('>') + 1, str.length);
			}
			if (str.endWith('>')) {
				str = str.substring(0, str.lastIndexOf('<'));
			}
			var res = eval('(' + str + ')');
			if (res == undefined || res.code == undefined) {
				return;
			}
			if (res.code == '-222') {
				$.fn.tipAlert('离开太久，系统已经退出，请重新登录', 1.5, 0);
				setTimeout(function() {
					location.href = WEB_SERVER_PATH + "/";
				}, 2000);
			}
			if (res.code == '-333') {
				$.fn.tipAlert(res.message, 1.5, 0);
				setTimeout(function() {
					location.href = WEB_SERVER_PATH + "/";
				}, 2000);
			}
		} catch (err) {
		}
	}
})

$(function() {
	$(".nav_list_div").css('height', $(window).height()-$(".clientLogo").height() + 'px');
	$("#contextThis").css('min-height', $(window).height()-$("#footer").height()-122+ 'px');
	// 一级菜单的显示隐藏
	$("#navList").mouseenter(function(){
	  $("#navList").find('a span.j-arrow').show();
	  $(".nav_list_div,.open_box,.clientLogo,.nav_list_div .nav_list li,.container").stop().animate({width:'200px'},"fast",'linear');
	  var mainleftwidth = $(".main-left").width();
	  //logo
	  $(".clientLogo #logo").addClass("disno").css({"background-size":"49px 40px","background-position":"10px 6px","display":"none"});
	  $(".clientLogo h2").show();
	  $(".container h2 a").css("margin-left","16px");
	  if($(".left_nav").css("display")=='none'){
		  $(".main_right").stop().delay(100).fadeIn('slow').animate({marginLeft : "160px"}, 100, "linear");
	  }
	});
	$("#navList").mouseleave(function(){
	  $("#navList").find('a span.j-arrow').hide();
	  $(".nav_list_div,.open_box,.nav_list_div .nav_list li").stop().animate({width:'40px'},'fast','linear');
	  var mainleftwidth = $(".main-left").width();
	 
	  if($(".left_nav").css("display")=='none'){
		  $(".container,.clientLogo,.open_box").animate({width:'40px'},'fast','linear');
		  $(".clientLogo #logo").removeClass("disno").css({"background-size":"30px 24px","background-position":"4px 18px","display":"block"});
		  $(".clientLogo h2").hide();	
		  $(".main_right").stop().delay(100).fadeIn('slow').animate({marginLeft : "0px"}, 100, "linear");
	  }else{
		  $(".container,.clientLogo,.open_box").animate({width:'200px'},"fast",'linear');
		  $(".clientLogo #logo").addClass("disno").css({"background-size":"49px 40px","background-position":"10px 6px","display":"none"});
		  $(".clientLogo h2").show();
		  $(".container h2 a").css("margin-left","16px");
		  $(".main_right").stop().delay(100).fadeIn('slow').animate({marginLeft : "0px"}, 100, "linear");
	  }
	
	});
//	$("#navList li:first-child").click(function(){
//		$(".nav_list li.nav_item a span:last-child").css('dsiplay','inline-block');
//	})
	// 固定左侧在浏览器
	var navH = $(".container").offset().top;
	$(window).scroll(function(){
		var scroH = $(this).scrollTop();
		if(scroH>navH){
			$(".container").css({"position":"fixed","top":0,"left":0});
			$(".open_box,.nav_list_div,.left_nav").css({"position":"fixed"});
		}else if(scroH<navH){
			$(".container").css({"position":"relative","top":0,"left":0});
			$(".open_box,.nav_list_div,.left_nav").css({"position":"absolute"});
		}
	})
	// main的最小高度 右侧高度判断
	var minheight = $(window).height() - $('.header').height()- $('#footer').height() - 9;
	$('.main').css('min-height', minheight + 'px');
	var rightheight = $('.main_right').height();
	var maxheight = $('.main').height();
	if (rightheight >= minheight) {
		$('.main_right').css('min-height', maxheight + 'px');
	}
	// 头部功能模块菜单
	$(".nav_item").click(function() {
		var i = $(this).index();
		$(".nav_item").removeClass("active");
		$(this).addClass("active").siblings().removeClass("active");
	});
	//左侧菜单高亮
	$(".left_nav li").each(function(index,obj){
		var thisCode = $(obj).attr("code");
		if(thisCode == CURRENT_PAGE_CODE){
			$(obj).parent().parent().find("li").removeClass("left_nav_high");
			$(obj).addClass("left_nav_high").siblings().removeClass("left_nav_high");
		}
	});
	// 左侧功能模块菜单
	$(".left_nav_menu li").each(function(index, element) {
		if ($(element).hasClass('left_nav_high')) {
			$(element).parent().show();
			$(element).parent().prev().addClass('left_nav_module-on');
		}
	});
	$(".left_nav_module").click(function() {
		var $this = $(this);
		if (!$this.hasClass("left_nav_module-on")) {
			$(".left_nav_menu").slideUp(600);
			$(".left_nav_module").removeClass("left_nav_module-on");
			if (0 != $this.next(".left_nav_menu").length) {
				$this.next(".left_nav_menu").slideDown(600);
			}
			$this.addClass("left_nav_module-on");
		} else {
			$this.removeClass("left_nav_module-on");
			$this.next(".left_nav_menu").slideUp(600);
		}
	});
	$("#logout").click(logout);
	
	//首页样式
	if($(".wrapA").length>0){
		$(".wrapA").eq(1).find(".module-left").css("color","#FEA720");
		$(".wrapA").eq(1).find("a").css("background-image",'url(' + WEB_STATIC_PATH + '/img/home-icon-bg-2.png)');
		$(".wrapA").eq(0).find(".module-left").css("color","#087BFF");
	}
	/*问题反馈*/
	var QQheight= $(window).height()/2-50;
	$("#consultationQQ").css("position","fixed");
	$("#consultationQQ").css("top",QQheight + 'px');
	/*QQ随机*/
//	var arrQQ=["http://wpa.qq.com/msgrd?v=3&uin=1439990843&site=qq&menu=yes",
//	           "http://wpa.qq.com/msgrd?v=3&uin=81997906&site=qq&menu=yes",
//	           "tencent://message/?uin=2222010006&amp;Site=edu.dataguru.cn&amp;Menu=yes",
//	           "tencent://message/?uin=620068241&amp;Site=edu.dataguru.cn&amp;Menu=yes",
//	           "tencent://message/?uin=621068241&amp;Site=edu.dataguru.cn&amp;Menu=yes"
//	           ];
//	var a=parseInt(arrQQ.length*Math.random()) //获取数组下标的随机数
//	arrQQ[a]; //就是要抽取数组中 的随机数字
//	$("#consultationQQ .QQ a").attr("href",arrQQ[a]);
	//左侧高度
	var mainLeftHH=$(window).height()-$(".clientLogo").height();
	$(".left_nav").css("height",mainLeftHH);
	if($.cookie("EUDOEIW83")=='IE873'){
		foldBox($(".left_nav .left_nav_shouye i"));
	}else if($.cookie("EUDOEIW83")=='P-0)'){
		unfoldBox($(".main-left .open_nav"));
	}
	// 重置联想查询的样式
	//$(".textbox .textbox-text").parent().parent().css("margin-top","7px");
	$(".textbox:eq(0)").remove();
});
//客服咨询
function customerService() {
 	$.ajax({
 		url:DOMAIN_PATH+"/"+LOCAL_SYSTEM.toLowerCase()+ "/sys/client/feedback/customerService",
		type:'post',
		dataType:'json',
		success:function(data){
			if (data.success) {
				var qqs=data.result;
				if(qqs.endWith(',')){
					qqs=qqs.substring(0,qqs.length-1);
				}
				var arrQQ=qqs.split(",");
				var a=parseInt(arrQQ.length*Math.random()) //获取数组下标的随机数
				window.location.href="tencent://message/?uin="+arrQQ[a]+"&amp;Site=edu.dataguru.cn&amp;Menu=yes";
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

//展开
function foldBox(obj){
	$(obj).parent().parent().parent().find(".left_nav").hide();
	$(obj).parent().parent().parent().find(".open_nav").show();
	$(obj).parent().parent().parent().find(".commonSeverice_small_nav,.sms_small_nav").show();
	$(obj).parent().parent().parent().parent().find(".main-left").animate({width:'40px'},"fast",'linear');
	$(".open_nav").css({"background" : 'url(' + PASSPORT_PATH+ '/img/icon_greywhite.png) no-repeat center center'});
	$(obj).parent().parent().parent().parent().parent().find(".main_right").hide();
	$(obj).parent().parent().parent().parent().parent().find(".main_right").css({"margin-left" : "50px"});
	$(obj).parent().parent().parent().parent().parent().find(".main_right").stop().delay(100).fadeIn('slow').animate({marginLeft : "50px"}, 100, "linear");
	
	$(".open_box").show();
	$(".nav_list_div").css({"top":"100px"});
	//报表文字的距离
	foldBoxreport("outerId_search_form");
	foldBoxreport("outerIdTwo_search_form");
	$.cookie("EUDOEIW83","IE873",{expires:7,path:"/"});
//	$(".nav_list").hide();

	//header
	$(".navbar-side").css({"width":"40px"});
	//logo 
	$(".container,.clientLogo").css({"width":"40px"});
	$(".clientLogo #logo").removeClass("disno").css({"background-size":"30px 24px","background-position":"4px 18px","display":"block"});
	$(".clientLogo h2").hide();	
	//收缩扩展图标
	$(".open_box").css({"width":"40px"});
	//right 动画
	$(".main_right").stop().delay(100).fadeIn('slow').animate({marginLeft : "0px"}, 100, "linear");
}
//报表文字的距离
function foldBoxreport(str){
	var marleft=parseInt($(".main_right").css("margin-left"));
	 setTimeout(function(){
		 var heightval=parseInt($("#"+str+"").height());
		 if(marleft==50 && heightval < 90){
			$(".datay").css("top","78px");
			$(".popupDiv").css("top","212px");
		 }else if(marleft==50 && heightval > 90){
			 $(".datay").css("top","128px");
			 $(".popupDiv").css("top","260px");
		 }
	 },200);
}
//收缩
function unfoldBox(obj){
	$(obj).css("background-image",'url(' + WEB_STATIC_PATH + '/img/icon.jpg)');
	$(obj).parent().parent().find(".left_nav").show();
	$(obj).hide();
	$(obj).parent().find(".commonSeverice_small_nav,.sms_small_nav").hide();
	$(obj).parent().parent().parent().find(".main-left").animate({width:'160px'},"fast",'linear');
	$(obj).parent().parent().parent().parent().find(".main_right").hide();
	$(obj).parent().parent().parent().parent().find(".main_right").css({"margin-left" : "210px","min-width" : "990px"});
	$(obj).parent().parent().parent().parent().find(".main_right").stop().delay(100).fadeIn('slow').animate({marginLeft : "210px"}, 100, "linear");
	
	$(".open_box").hide();
	$(".nav_list_div").css({"top":"60px"});
	//报表文字的距离
	unfoldBoxreport("outerId_search_form");
	unfoldBoxreport("outerIdTwo_search_form");
	$.cookie("EUDOEIW83","P-0)",{expires:7,path:"/"});
//	$(".nav_list").show();
	//header
	$(".navbar-side").css({"width":"200px"});
	//logo 
	$(".container,.clientLogo").css({"width":"200px"});
	$(".clientLogo #logo").addClass("disno").css({"background-size":"49px 40px","background-position":"10px 6px","display":"none"});
	$(".clientLogo h2").show();
	//收缩扩展图标
	$(".open_box").css({"width":"200px"});
	//right 动画
	$(".main_right").stop().delay(100).fadeIn('slow').animate({marginLeft : "0px"}, 100, "linear");
}
function unfoldBoxreport(str){
	var marleft=parseInt($(".main_right").css("margin-left"));
	 setTimeout(function(){
		 var heightval=parseInt($("#"+str+"").height());
		 if(marleft==210 && heightval > 90){
			$(".datay").css("top","126px");
			 $(".popupDiv").css("top","260px");
		 }else if(marleft==210 && heightval < 90){
			$(".datay").css("top","80px");
			$(".popupDiv").css("top","212px");
		 }
	 },200);
}
// 登出
function logout(){
	$.ajax({
		url : PASSPORT_PATH + "/logout",
		type : 'post',
		dataType : 'json',
		success : function(data) {
			location.href = WEB_SERVER_PATH + "/";
		},
		error : function() {
			location.href = WEB_SERVER_PATH + "/";
		}
	});
}

/*//修改密码 确定事件
function modifyPassword(){
	var password = $("#password").val();
	var newpass = $("#newpass").val();
	var affirm = $("#affirm").val();
	var $formCheckOut = $('#changepw');
    $formCheckOut.validate({
    	rules:{     //配置验证规则，key就是被验证的dom对象，value就是调用验证的方法(也是json格式)
			oldPassWord:{ //oldPassWord是name名
				required:true
			},
			spass:{   
				required:true,
				rangelength:[6,16],
				passwordTrue: "#password"
			},
			spass2:{
				required:true,
				equalTo:'#newpass' 
			}
			
		},
		messages:{
			oldPassWord:{
				required:"请输入旧密码"
			},
			spass:{
				required:"请输入新密码",
				rangelength:$.validator.format("密码长度为6-16位字母数字"),
				passwordTrue:"新密码与旧密码不能一样"
			},
			spass2:{
				required:"请再次输入密码",
				equalTo:"两次密码必须一致" //表示和id="spass"的值相同
			}
		},
        submitHandler: function() {
        	password = hex_md5(password);
        	newpass = hex_md5(newpass);
        	 $.ajax({
        		url : WEB_SERVER_PATH + '/changePassword',
        		type : "post",
        		dataType : "json",
        		data : {
        			password : password,
        			newpassword : newpass
        		},
        		error:function() {
        			$.fn.tipAlert("系统异常", 1.5, 0);
        		},
        		success:function(data) {
        			if (data.success) {
        				$.fn.tipAlert("修改密码成功,请重新登陆!",1.5,1);
        				logout();
        				//window.location.reload();
        			} else {
        				$.fn.tipAlert(data.message,1.5,0);
        			}
        		}
        	});
        	 
        }
    });
	
}*/
function timelateryyyyMMddHHmmss(minuteslater){
	var date = new Date().getTime();
	if(minuteslater){
		date = date + (minuteslater * 60 * 1000);
		date = new Date(date);
	}
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    var strDate = date.getDate();
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var hour =  date.getHours();
    if (hour >= 0 && hour <= 9) {
    	hour = "0" + hour;
    }
    var minutes =  date.getMinutes();
    if (minutes >= 0 && minutes <= 9) {
    	minutes = "0" + minutes;
    }
    var sec = date.getSeconds();
    if (sec >= 0 && sec <= 9) {
    	sec = "0" + sec;
    }
    return date.getFullYear() + seperator1 + month + seperator1 + strDate + " " + hour + seperator2 + minutes + seperator2 + sec;
}


function modifyPw(){
	var url=window.location.href;
	window.location.href=PASSPORT_PATH + '/toChangePassword?fromUrl='+url+'&system='+LOCAL_SYSTEM;
}
function goPage(url){
	window.location.href=WEB_SERVER_PATH + url;
}
function authJump(url,authCode){
	if(AUTHS[authCode] == null){
		$.fn.tipAlert("请联系管理员开通权限",1.5,0);
		return false;
	}else{
		window.location.href=WEB_SERVER_PATH + url;
		return true;
	}
}
//页面增加是否显示总数复选框
function isShowTotal(){
	$("#outerId_search_form").append("<br/><br/><div style='float:left'>&nbsp;&nbsp;"+
			 "<input id='showTotleInput'name='showTotleInput' type='checkbox' onclick='showTotalNumbers()'>&nbsp;是否显示总量&nbsp;<div>");
}
function showTotalNumbers(){
	if($("#showTotleInput").is(":checked")){
		emTableConfig.totalNumbersShow = true;
		$("#showTotleInput").val(0);
	}else{
		emTableConfig.totalNumbersShow = false;
		$("#showTotleInput").val(1);
	}
	emrefulsh('emTableConfig');
}
//获取当前时间
function getnowt() {
	var nowtime = new Date()
    var year = nowtime.getFullYear();
    var month = padleft0(nowtime.getMonth() + 1);
    var day = padleft0(nowtime.getDate());
    var hour = padleft0(nowtime.getHours());
    var minute = padleft0(nowtime.getMinutes());
    var second = padleft0(nowtime.getSeconds());
    var millisecond = nowtime.getMilliseconds(); millisecond = millisecond.toString().length == 1 ? "00" + millisecond : millisecond.toString().length == 2 ? "0" + millisecond : millisecond;
    return year + "-" + month + "-" + day + " ";

}
/*获取3天之前的时间*/
function getThreeBeforeTime() {
	var timestamp = new Date().getTime();
	var day3=259200000;
	var tm=timestamp-day3;
	var nowtime=new Date(tm);
    var year = nowtime.getFullYear();
    var month = padleft0(nowtime.getMonth() + 1);
    var day = padleft0(nowtime.getDate());
    var hour = padleft0(nowtime.getHours());
    var minute = padleft0(nowtime.getMinutes());
    var second = padleft0(nowtime.getSeconds());
    var millisecond = nowtime.getMilliseconds(); millisecond = millisecond.toString().length == 1 ? "00" + millisecond : millisecond.toString().length == 2 ? "0" + millisecond : millisecond;
return year + "-" + month + "-" + day  ;

}
//报表默认三天时间
function defaultTimeReport(str,type){
	if(type==1){
		$("#outerIdTwo_search_form #"+str+"").val(getThreeBeforeTime());
		window['emTableConfigTwo'].ajaxConfig.data.startTime=getThreeBeforeTime();
		emTable('emTableConfigTwo');
		$("#outerIdTwo_search_form #"+str+"").val(getThreeBeforeTime());
	}else{
		$("#outerId_search_form #"+str+"").val(getThreeBeforeTime());
		window['emTableConfig'].ajaxConfig.data.startTime=getThreeBeforeTime();
		emTable('emTableConfig');
		$("#outerId_search_form #"+str+"").val(getThreeBeforeTime());
	}
	
}
//获取当前年月日
function getnowymd() {
	var nowtime = new Date()
    var year = nowtime.getFullYear();
    var month = padleft0(nowtime.getMonth() + 1);
    var day = padleft0(nowtime.getDate());
    return year + "-" + month + "-" + day + " " + "23:59:59";

}
/*获取90天之前的时间*/
function getnowtime() {
	var timestamp = new Date().getTime();
	var day90=7776000000;
	var tm=timestamp-day90;
	var nowtime=new Date(tm);
    var year = nowtime.getFullYear();
    var month = padleft0(nowtime.getMonth() + 1);
    var day = padleft0(nowtime.getDate());
    var hour = padleft0(nowtime.getHours());
    var minute = padleft0(nowtime.getMinutes());
    var second = padleft0(nowtime.getSeconds());
    var millisecond = nowtime.getMilliseconds(); millisecond = millisecond.toString().length == 1 ? "00" + millisecond : millisecond.toString().length == 2 ? "0" + millisecond : millisecond;
return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second ;

}
/*获取7天之前的时间*/
function getSevendaysTime() {
	var timestamp = new Date().getTime();
	var day7=518400000;
	var tm=timestamp-day7;
	var nowtime=new Date(tm);
    var year = nowtime.getFullYear();
    var month = padleft0(nowtime.getMonth() + 1);
    var day = padleft0(nowtime.getDate());
    var hour = padleft0(nowtime.getHours());
    var minute = padleft0(nowtime.getMinutes());
    var second = padleft0(nowtime.getSeconds());
    var millisecond = nowtime.getMilliseconds(); millisecond = millisecond.toString().length == 1 ? "00" + millisecond : millisecond.toString().length == 2 ? "0" + millisecond : millisecond;
    return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second ;

}
//补齐两位数
function padleft0(obj) {
    return obj.toString().replace(/^[0-9]{1}$/, "0" + obj);
}
//点击查看短信详情信息
function seeDetails(obj){
	var text=$(obj).parent().parent().text();
	$.fn.tipOpen({
		title : '详情',
		width : '500',
		cancel : [{
			label : '关闭',
			onClickFunction : 'seeClose()'
		}],
		tipClose : false,
		concent : '<div class="seeDetails">'+cleanXss(text)+'</div>'
	});
}
//xss转义
function cleanXss(obj){
	obj = obj.replace(/</g, "&lt;");
	obj = obj.replace(/'/g, "&#39;");
	return obj; 
}
function seeClose(){
	$.fn.tipShut();
}
/*获取15天之前的时间*/
function getFifteenthDaysTime() {
	var timestamp = new Date().getTime();
	var day15=1209600000;
	var tm=timestamp-day15;
	var nowtime=new Date(tm);
    var year = nowtime.getFullYear();
    var month = padleft0(nowtime.getMonth() + 1);
    var day = padleft0(nowtime.getDate());
    var hour = padleft0(nowtime.getHours());
    var minute = padleft0(nowtime.getMinutes());
    var second = padleft0(nowtime.getSeconds());
    var millisecond = nowtime.getMilliseconds(); millisecond = millisecond.toString().length == 1 ? "00" + millisecond : millisecond.toString().length == 2 ? "0" + millisecond : millisecond;
    return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second ;

}
/**************时间控件完善***********************/
function getSeventhBeforeDaysTime() {
	var timestamp = new Date().getTime();
	var day7=518400000;  
	var tm=timestamp+day7;
	var nowtime=new Date(tm);
	return formatTimeString(nowtime);
}
/*获取90天之前的时间*/
function getNowBeforeTime(){
	var timestamp = new Date().getTime();
	var day90=7776000000;
	var tm=timestamp-day90;
	var nowtime=new Date(tm);
	return formatTimeString(nowtime);
}
function formatTimeString(time){
	var nowtime = time;
	var year = nowtime.getFullYear();
    var month = padleft0(nowtime.getMonth() + 1);
    var day = padleft0(nowtime.getDate());
    var hour = padleft0(nowtime.getHours());
    var minute = padleft0(nowtime.getMinutes());
    var second = padleft0(nowtime.getSeconds());
    var millisecond = nowtime.getMilliseconds(); millisecond = millisecond.toString().length == 1 ? "00" + millisecond : millisecond.toString().length == 2 ? "0" + millisecond : millisecond;
    return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second ;
}
//初始化格式化时间
function nowTimeLocaleString(){
	var now = new Date().toLocaleString();
	if(now.indexOf("上") === -1){
		now = now.substring(now.indexOf("午")+1);
		var arrE = now.split(":");
		for(var i=0;i<arrE.length;i++){
			arrE[i].length<2 && (arrE[i] = 12 + (+arrE[i]));
		}
		now = arrE.join(":");
	}else{
		now = now.substring(now.indexOf("午")+1);
		var arrA = now.split(":");
		for(var i=0;i<arrA.length;i++){
			arrA[i].length<2 && (arrA[i] = "0"+ arrA[i]);
		}
		now = arrA.join(":");
	}
	return now;
}
function formatTime(time){
	var nowtime = time;
	var year = nowtime.getFullYear();
    var month = padleft0(nowtime.getMonth() + 1);
    var day = padleft0(nowtime.getDate());
    return year + "-" + month + "-" + day + " " + "23:59:59";
}
//时间区间控件修改
function timeControl(){
	//当前时间后7天timeString
	function section(string){
		var timestamp = new Date(string).getTime();
		var now = new Date().getTime(); 
		var day7=518400000;
		var tm=timestamp+day7;
		tm >= now && (tm=now);
		var nowtime=new Date(tm);
		return formatTime(nowtime);
	}
	$(".main_right").on("blur","#startDateSearch",function(){
		var end = new Date($("#endDateSearch").val()).getTime();
		var start = new Date($(this).val()).getTime();
		(end > new Date(section($(this).val())).getTime() || end < start) && ($("#endDateSearch").val(section($(this).val())))
	})
	$(".main_right").on("focus","#endDateSearch",function(){
		var date = section($("#startDateSearch").val());
		var obj = {firstDayOfWeek:1,
				readOnly:true,
				minDate:'#F{$dp.$D(\'startDateSearch\')}',
				dateFmt:'yyyy-MM-dd HH:mm:ss',
				startDate:'%y-%M-%d 23:59:59'
		};
		obj["maxDate"] = date;
		WdatePicker(obj)
	})
}

function comments(){
	 $.ajax({
		url : WEB_SERVER_PATH + '/helper/ajax/updateInfoFirst/client',
		type : "post",
		dataType : "json",
		success:function(data) {
			if (data.success) {
				var date=data.result;
				if(date!=null && date!=undefined){
				var html='<label>新版本：'+date.versionInfo+'</label><br>';
				html+='<label>业务类型：'+date.businessNameSimple+'</label><br>';
				html+='<label>更新内容：</label><br>';
				html+='<textarea class="comments" disabled="disabled">'+date.updateInfo+'</textarea>';
				$.fn.tipLoddingEnd(true);
				
					$.fn.tipOpen({
						title : '新功能发布通知',
						width : '500',
						cancel : [{
							label : '关闭',
							onClickFunction : 'seeClose()'
						}],
						tipClose : false,
						concent : html
					});
				}
				} else {
					$.fn.tipLoddingEnd(false);
					$.fn.tipAlert(data.message, 1.5, 0);
				}
		},
		error:function() {
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert("系统异常", 1.5, 2);
		}
	});
	
}
