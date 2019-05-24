$(function(){
	$.fn.tipLodding();
	 $.ajax({
	 		url : WEB_SERVER_PATH + "/sys/client/administrate/findId",
	 		type : 'post',
	 		dataType : 'json',
	 		data : {
	 			id : id
	 		},
	 		success : function(data) {
	 			if (data.success) {
	 				$.fn.tipLoddingEnd(true);
	 				var data = data.result[0];
	 				var username=data.username;
	 				var realname=data.realname;
	 				var mobile=data.mobile;
	 				var email=data.email;
	 				var roles=data.roles;
	 			    var userId = data.id;
	 			    var roleName=data.roleName;
	 			    var remark=data.remark;
 					$("#username").val(username); //不需要验证
 					$("#realname").val(realname);
 					$("#mobile").val(mobile);
 					$("#email").val(email);
 					if(roleName!=""&&roleName!=null){
 						var arr=roleName.split(",");
 						var remarkArr=remark.split(",");
 						var html="";
 						for(i=0; i<arr.length; i++){
 							html+='<label onmouseover="showContent(this)" onmouseout="hideContent(this)"><input type="hidden" value="'+remarkArr[i]+'"/>'+arr[i]+'</label>';
 						}
 						$(".checkR").html(html);	 
 					}
	 			}else{
	 				$.fn.tipLoddingEnd(false);
	 				$.fn.tipAlert(data.message, 1.5, 0);
	 			}
	 		},
	 		error:function(){
	 			$.fn.tipLoddingEnd(false);
				$.fn.tipAlert('系统异常',1.5,0);
			}
		});
})
function showContent(obj){
	var content=$(obj).find("input").val();
	var contentlen=content.length;
	var html='<span class="shc">'+content+'</span>';
	if(content!="" && content!=null){
		$(obj).append(html);
	}
	if(contentlen >25){
		$(".shc").css("width","300px");
	}
}
function hideContent(obj){
	$(".shc").remove();
}