$(function(){
	$("#userdetail input").attr("disabled",true);
	$.fn.tipLodding();
	$.ajax({
		url :  WEB_SERVER_PATH + "/sys/client/user/ajax/detail",
		type : 'post',
		dataType : 'json',
		data : {
			id : id
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				var createTime=data.result.createTime;
				var email=data.result.email;
				var rolename=data.result.rolename;
				var clientName=data.result.clientName;
				var clientNumber=data.result.clientNumber;
				var username=data.result.username;
				var identity=data.result.identity;
				var realname=data.result.realname;
				var mobile=data.result.mobile;
				var state=data.result.state;
				var arr=[];
				var html="";
				arr=rolename.split(",");
				for(var i=0;i<arr.length;i++){
					html+='<span>'+arr[i]+'</span>';
				}
				$("#clientName").val(clientName);
				$("#clientNumber").val(clientNumber);
				$("#username").val(username);
				if(identity==0){
					$("#identity").val("全部");
				}else if(identity==1){
					$("#identity").val("管理账号");
				}else if(identity==2){
					$("#identity").val("普通账号");
				}
				$("#realname").val(realname);
				$("#mobile").val(mobile);
				if(state==1){
					$("#state").val("停用");
				}else if(state==2){
					$("#state").val("启用");
				}else if(state==3){
					$("#state").val("锁定");
				}else if(state==0){
					$("#state").val("全部");
				}
				$("#authinput").html(html);
				$("#email").val(email);
				$("#timein").val(createTime);	
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error : function() {
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
})
