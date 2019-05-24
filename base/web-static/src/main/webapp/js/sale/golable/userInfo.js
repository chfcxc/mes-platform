$(function(){
	$("body").css("background","#fff");
})
var addHtml = $('#changepassword').html();
$('#changepassword').remove();
function modifypw(){
	$.fn.tipOpen({
		title : "修改密码",
		width : '640',
		btn : [],
		concent : addHtml
	});
}

function modifyPword(){
	var $formCheckOut = $('#changepw');
	var validator = $formCheckOut.validate({
    	rules:{
    		oldPassWord:{ 
    			required:true
			},
			newpass:{   
				required:true,
				rangelength:[6,16],
				passwordTrue: "#password",
				newPassReg:true
			},
			affirm:{
				required:true,
				equalTo:'#newpass' 
			}
		},
		messages:{
			oldPassWord:{
				required:"请输入旧密码"
			},
			newpass:{
				required:"请输入新密码",
				rangelength:$.validator.format("密码必须是6-16位"),
				passwordTrue:"新密码与旧密码不能一样"
			},
			affirm:{
				required:"请再次输入密码",
				equalTo:"两次密码必须一致", //表示和id="spass"的值相同
			}
		},
        submitHandler: function() {
			var oldPassWord=$("#oldPassWord").val();
			var newpass = $("#newpass").val();
			var affirm = $("#affirm").val();
			oldPassWord = hex_md5(oldPassWord);
			newpass = hex_md5(newpass);
			affirm = hex_md5(affirm);
			$.ajax({
				url : WEB_SERVER_PATH + "/sys/userInfo/modifyPassword",
				type : "post",
				dataType : "json",
				data : {
					password : oldPassWord,
					newPassword : newpass,
					verifyPassword : affirm
				},
				success:function(data) {
					if (data.success) {
						$.fn.tipAlert("修改密码成功!",3,1);
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
    });
	
}

function modifyrn(vals){
	var html = "<div align='center'><span class='xing'>*</span><label>姓名：</label><input id='musername' type='text' value='"+vals+"'/></div>";
	$.fn.tipOpen({
			title : "提示",// 弹框标题
			width : '300',// 弹框内容宽度
			concent :html,//弹框内容
			btn : [ {
		 		label : '确定',
		 		onClickFunction : 'rnsave()'
		 	}]				
		});
	
}
function rnsave(){
   var musername=$("#musername").val();
   if(musername==""){ 
	   $.fn.tipAlert("姓名不能为空",1.5,0);
	   return false;
   }
   var re = /^[\u2E80-\u9FFFa-zA-Z]+$/;
	if (!re.test(musername)) {
		$.fn.tipAlert("请输入中文和英文", 1.5, 0);
		return false;
	}
   $.ajax({
		url : WEB_SERVER_PATH + '/sys/userInfo/modifyRealName',
		type : "post",
		dataType : "json",
		data : {
			realName : musername
		},      		
		success:function(data) {
			if (data.success) {
				$.fn.tipAlert("修改成功",1.5,1);
				window.location.reload();
			} else {
				$.fn.tipAlert(data.message,1.5,0);
				
			}
		},
		error:function() {
			$.fn.tipAlert("系统异常", 1.5, 0);
		},
	});

}
function modifym(vals){
	var html = "<div align='center'><span class='xing'>*</span><label>变更后手机号：</label><input id='mphone' type='text' value='"+vals+"'/></div>";
	$.fn.tipOpen({
			title : "提示",// 弹框标题
			width : '300',// 弹框内容宽度
			concent :html,//弹框内容
			btn : [ {
		 		label : '确定',
		 		onClickFunction : 'ymsave()'
		 	}]				
		});
	
}
function ymsave(){
	var phone=$("#mphone").val();
	   if(phone==""){ 
		   $.fn.tipAlert("手机号不能为空",1.5,0);
		   return false;
	   }
	   $.ajax({
			url : WEB_SERVER_PATH + '/sys/userInfo/modifyMobile',
			type : "post",
			dataType : "json",
			data : {
				mobile : phone
			},      		
			success:function(data) {
				if (data.success) {
					$.fn.tipAlert("修改成功",1.5,1);
					window.location.reload();
				} else {
					$.fn.tipAlert(data.message,1.5,0);
					
				}
			},
			error:function() {
				$.fn.tipAlert("系统异常", 1.5, 0);
			},
		});
	
}
function modifye(vals){
	var html = "<div align='center'><span class='xing'>*</span><label>变更后邮箱：</label><input id='memail' type='text' value='"+vals+"' style='display:inline-block; width:200px;'/></div>";
	$.fn.tipOpen({
			title : "提示",// 弹框标题
			width : '300',// 弹框内容宽度
			concent :html,//弹框内容
			btn : [ {
		 		label : '确定',
		 		onClickFunction : 'ysave()'
		 	}]				
		});
	
}
function ysave(){
	var memail=$("#memail").val();
	   if(memail==""){ 
		   $.fn.tipAlert("邮箱不能为空",1.5,0);
		   return false;
	   }
	   $.ajax({
			url : WEB_SERVER_PATH + '/sys/userInfo/modifyEmail',
			type : "post",
			dataType : "json",
			data : {
				email : memail
			},      		
			success:function(data) {
				if (data.success) {
					$.fn.tipAlert("修改成功",1.5,1);
					window.location.reload();
				} else {
					$.fn.tipAlert(data.message,1.5,0);	
				}
			},
			error:function() {
				$.fn.tipAlert("系统异常", 1.5, 0);
			},
		});
	
	
}