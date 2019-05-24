/* 中间高度 */
$(function(){
	var centerHeight= $(window).height() - $('.header').height()-$('#footer').height()-9;
	$(".center_box").height(centerHeight);
	var cenheight=$(".center_box").height()-$(".changepw").height();
	$(".changepw").css("padding-top",cenheight/2);
});


function modifyPword(){
	var oldPassWord=$("#oldPassWord").val();
	var newpass = $("#newpass").val();
	var affirm = $("#affirm").val();
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
			newpassword : newpass
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
