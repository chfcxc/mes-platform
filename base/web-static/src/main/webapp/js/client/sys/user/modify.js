$(function(){
	 addjuese();
	 $.ajax({
	 		url : WEB_SERVER_PATH + "/sys/client/administrate/findId",
	 		type : 'post',
	 		dataType : 'json',
	 		async: false,
	 		data : {
	 			id : id
	 		},
	 		success : function(data) {
	 			if (data.success) {
	 				var data = data.result[0];
	 				var username=data.username;
	 				var realname=data.realname;
	 				var mobile=data.mobile;
	 				var email=data.email;
	 			    var userId = data.id;
	 			    var rid=data.rid.substring(0,data.rid.length-1).split(",");
	 			    //var rid=data.rid;
 					$("#username").val(username); //不需要验证
 					$("#realname").val(realname);
 					$("#mobile").val(mobile);
 					$("#email").val(email);
 					var arr=[];
 					$("#jues .checkR input[type=checkbox]").each(function(i,ele){
 						var id = $(ele).val();
 						arr.push(id); 								
 					});
 					for(j in rid){
						if($.inArray(rid[j], arr)!==-1){
 							$("#jues .checkR input[value="+rid[j]+"]").attr("checked",true);	
 						}	
					}	
 						 
	 			}else{
	 				$.fn.tipAlert(data.message, 1.5, 0);
	 			}
	 		},
	 		error:function(){
				$.fn.tipAlert('系统异常',1.5,0);
			}
		});
})


	
var jues;
function modifyAddBox(id){
	var $modifyUserForm = $('#modifyUserForm');
	var validator=$modifyUserForm.validate({
		rules:{
			
			realname:{
				required:true,
				real_name:true
			},
			mobile:{
				required:true,
				phone:true
			},
			email:{
				required:false,
				userEmail:true
			}
		},
		messages:{
			
			realname:{
				required:"请填写姓名"
			},
			mobile:{
				required:"请填写手机号",
			},
			email:{
				userEmail:"请输入正确的邮箱"
			}
		},
		submitHandler: function() {
			var username = $("#username").val();
			var realname = $("#realname").val();
		    var mobile = $("#mobile").val();
			var email = $("#email").val();
			$("#jues .checkR input:checkbox:checked").each(function(i, obj) {
				if (jues != "" && null != jues) {
					jues = jues + "," + $(obj).val();
				} else {
					jues = $(obj).val();
				}
			});
			$.fn.tipLodding();
	     	$.ajax({
				url:  WEB_SERVER_PATH + "/sys/client/administrate/update?randomdata="+Date.parse(new Date()),
				type:'post',
				dataType:'json',
				data:{
					username: username,
					realname :realname,
					mobile :mobile,
					email :email,
					roleids  :jues,
					id :id
				},
				success:function(data){
    				if (data.success) {
    					$.fn.tipLoddingEnd(true);
    					$.fn.tipAlert('用户修改成功',1,1);
    					goPage('/sys/client/administrate');
    					
    				} else {
    					$.fn.tipLoddingEnd(false);
    					$.fn.tipAlert(data.message, 1.5, 0);
    					validator.resetForm();
    				}
				},
				error:function(){
					$.fn.tipLoddingEnd(false);
					$.fn.tipAlert('系统异常',1.5,0);
				}
	     	})
		}
 })

	
}


//加载角色
function addjuese(){
	 var html ='<div class="fl"><label class="item-label"><span class="xing">*</span>所属角色:</label></div><div class="checkR fl">'
			$.ajax({
			url:  WEB_SERVER_PATH + "/sys/client/administrate/findrolename",
			type:'post',
			dataType:'json',
			async: false,
			success:function(data){
				var list = data.result;
				for(var i  in list){
					var roleId = list[i].id;
					var roleName = list[i].roleName;
					var remark=list[i].remark;
					html +='<label onmouseover="showContent(this)" onmouseout="hideContent(this)"><input class="check" type="checkbox" value="'+roleId+'" remarkval="'+remark+'" name="checkbox_role">&nbsp;'+roleName+'&nbsp;</label>';
				
				}
				html+='</div>';
				$("#jues").html(html);
			},
			error:function(){
				$.fn.tipAlert('系统异常',1,2);
			}	
	   }) 	
}  
function showContent(obj){
	var content=$(obj).find("input").attr("remarkval");
	var contentlen=content.length;
	var html='<span class="shc">'+content+'</span>';
	if(content!="" && content!=null && content!="null"){
		$(obj).append(html);
	}
	if(contentlen >25){
		$(".shc").css("width","300px");
	}
}
function hideContent(obj){
	$(".shc").remove();
}