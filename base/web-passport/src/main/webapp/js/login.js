$(function(){
	$(".login_center").height($(window).height()-$(".login_header").height()-$(".login_footer").height()-2);
	$(".login_right").css("margin-top",($(".login_center").height()-380)/2);
	$(".login_left").css("margin-top",($(".login_center").height()-380)/2);
	$(".login_footer").remove();
});
$("#username").focus();
$("#submit").click($submmit);
$("#captcha").keydown(function(event) {
	if (event.keyCode == 13) {
		$submmit();
	}
});
$(".captcha_a").click(refulshCaptcha);
function $submmit() {
	var username = $("#username").val();
	var password = $("#password").val();
	var captcha = $("#captcha").val();
	var fromUrl=$("#fromUrlInput").val();
	var system=$("#systemInput").val();
	if (!username) {
		errorShow("请输入用户名");
		return;
	}
	if (!password) {
		errorShow("请输入密码");
		return;
	}
	password = hex_md5(password);
	if (!captcha) {
		errorShow("请输入验证码");
		return;
	}

	$.ajax({
		url : WEB_SERVER_PATH + "/login",
		type : 'post',
		dataType : 'json',
		data : {
			username : username,
			password : password,
			captcha : captcha,
			fromUrl:fromUrl,
			system:system
		},
		success : function(data) {
			if (data.success == true) {
				location.href = data.result;
			} else {
				refulshCaptcha();
				errorShow(data.message);
			}
		},
		error : function() {
			refulshCaptcha();
			errorShow("系统错误");
		}
	});
}
function refulshCaptcha() {
	$(".mycaptchaimg").attr("src",WEB_SERVER_PATH + '/captcha?type=login&_=' + new Date().getTime());
}
function errorShow(error) {
	$(".errorsdiv").show();
	$("#errorspan").html(error);
	$("#errorspan").css("color", "red");
}
