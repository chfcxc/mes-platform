//新增
$(function(){
	//加载角色
	   var html ='<div class="fl"><label class="item-label"><span class="xing">*</span>所属角色:</label></div><div class="checkR fl">'
			$.ajax({
			url:  WEB_SERVER_PATH + "/sys/client/administrate/findrolename",
			type:'post',
			dataType:'json',
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
	
})
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

var jues;
function openAddBox(){
	var $formCheckOut = $('#UserForm');
	var validator =$formCheckOut.validate({
			rules:{
				username:{ //用户名
					required:true,
					c_user_name:true
				},
				realname:{
					required:true,
					real_name:true
				},
				mobile:{
					required:true,
					phone:true
				},
				email:{
					required : false,
					userEmail : true
				}
			},
			messages:{
				username:{
					required:"请填写用户名"
				},
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
				url:  WEB_SERVER_PATH + "/sys/client/administrate/add?randomdata="+Date.parse(new Date()),
				type:'post',
				dataType:'json',
				data:{
					username : username,
					realname : realname,
					mobile : mobile,
					email : email,
					roleids :jues
				},
				success:function(data){
    				if (data.success) {
    					$.fn.tipLoddingEnd(true);
    					var password=data.result;
    					var html ="";
    					html = "<div>生成的随机6位密码是："+password+"</div>";
    					html+='<div class="tipFoot tipFootMp20">';
    					html+='<button onclick="seeJump()" type="button" class="tipBtn">关 闭</button>';
    					html+='</div>';
    					$.fn.tipOpen({
    						title : "密码提示",
    						width : '300',
    						concent :html,
    						btn : []				
    					});
    					/*$.fn.tipAlert('用户添加成功',1,1);
    					goPage('/sys/client/administrate');*/
    				}else {
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
function seeJump(){
	$.fn.tipShut();
	goPage('/sys/client/administrate');
}