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
$(function() {
	var minheight = $(window).height() - $('.header').height()- $('#footer').height() - 9 - 30;
	$('.main').css('height', minheight + 'px');
	var cenheight=minheight-$("#changepw").height();
	$("#changepw").css("padding-top",cenheight/2+ 'px');

});
// 登出
function logout(){
	var fromUrl=$("#fromUrlInput").val();
	var system=$("#systemInput").val();
	$.ajax({
		url : WEB_SERVER_PATH + "/logout",
		type : 'post',
		dataType : 'json',
		success : function(data) {
			location.href = WEB_SERVER_PATH + "/toLogin?system="+system+"&fromUrl="+fromUrl;
		},
		error : function() {
			location.href = WEB_SERVER_PATH + "/toLogin?system="+system+"&fromUrl="+fromUrl;
		}
	});
}


//修改密码 确定事件
/*function modifyPword(){
	var $formCheckOut = $('#changepw');
	var validator = $formCheckOut.validate({
    	rules:{
			oldPassWord:{ 
				required:true
			},
			spass:{   
				required:true,
				rangelength:[6,16],
				passwordTrue: "#password",
				newPassReg:true
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
				rangelength:$.validator.format("密码必须是6-16位"),
				passwordTrue:"新密码与旧密码不能一样"
			},
			spass2:{
				required:"请再次输入密码",
				equalTo:"两次密码必须一致" //表示和id="spass"的值相同
			}
		},
        submitHandler: function() {
        	var password = $("#password").val();
        	var newpass = $("#newpass").val();
        	var affirm = $("#affirm").val();
        	var fromUrl=$("#fromUrlInput").val();
        	var system=$("#systemInput").val();
        	password = hex_md5(password);
        	newpass = hex_md5(newpass);
        	 $.ajax({
        		url : WEB_SERVER_PATH + '/changePassword',
        		type : "post",
        		dataType : "json",
        		data : {
        			password : password,
        			newpassword : newpass,
        			fromUrl:fromUrl,
        			system:system
        		},
        		error:function() {
        			$.fn.tipAlert("系统异常", 1.5, 0);
        		},
        		success:function(data) {
        			if (data.success) {
        				$.fn.tipAlert("修改密码成功,请重新登录!",3,1);
        				logout();
        			} else {
        				$.fn.tipAlert(data.message,3,0);
        			}
        		}
        	});
        	 
        }
    });
	
}*/
function modifyPword(){
	var oldPassWord=$("#oldPassWord").val();
	var newpass = $("#newpass").val();
	var affirm = $("#affirm").val();
	var fromUrl=$("#fromUrlInput").val();
	var system=$("#systemInput").val();
	if(newpass == "" || newpass == null){
		$.fn.tipAlert("请输入新密码！",1.5,0);
		return;
	}
	var lower = ['q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m','Q','W','E','R','T','Y','U','I','O','P','A','S','D','F','G','H','J','K','L','Z','X','C','V','B','N','M'];
	var number = ['1','2','3','4','5','6','7','8','9','0'];
	var total= ['q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m','Q','W','E','R','T','Y','U','I','O','P','A','S','D','F','G','H','J','K','L','Z','X','C','V','B','N','M','1','2','3','4','5','6','7','8','9','0'];
	var a = 0;
	var b = 0;	
	var nep = newpass.split("");
		if (nep.length <=16 && nep.length >= 6){
			for(var i = 0; i < nep.length; i++){
				var pass = nep[i];
				var bk = false;
				for(var t in lower){
					if(pass == lower[t]){
						a=1;	
						bk = true;
						continue;						
					}
				}
				if(bk){
					continue;	
				}
				
				for(var t in number){
					if(pass == number[t]){
						b=1;
						bk = true;
						continue;
					}
				}
				if(bk){
					continue;	
				}
				var ok = false;
				for(var t in total){
					if(pass == total[t]){
						ok = true;
						continue;	
					}
				}
				if(!ok){
					$.fn.tipAlert('不支持特殊字符',1.5,0);
					return;
				}
				
			}
			if(a ==1 && b == 1 ){
				
			}else{
				$.fn.tipAlert('密码请用字母和数字组成！',1.5,0);
				return;
			}
		}else{
			$.fn.tipAlert('密码必须是6-16',1.5,0);
			return;
		}

	if(affirm == "" || affirm == null){
		$.fn.tipAlert("请再次输入新密码！",1.5,0);
		return;
	}
	if(!(newpass == affirm) ){
		$.fn.tipAlert("两次新密码不一致!",1.5,0);
		return;
	}
	oldPassWord = hex_md5(oldPassWord);
	newpass = hex_md5(newpass);
	
	$.ajax({
		url : WEB_SERVER_PATH + "/changePassword",
		type : "post",
		dataType : "json",
		data : {
			password : oldPassWord,
			newpassword : newpass,
			fromUrl:fromUrl,
			system:system
		},
		success:function(data) {
			if (data.success) {
				$.fn.tipAlert("修改密码成功,请重新登录!",3,0);
				setTimeout(function(){logout();},1500);
			} else {
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error:function() {
			$.fn.tipAlert("系统异常", 1.5, 0);
		}
	});
	
}

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

function goPage(url){
	window.location.href=url;
}