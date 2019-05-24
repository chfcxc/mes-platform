$(function(){
	// 问题反馈QQ
	var QQheight= $(window).height()/2-50;
	$("#consultationQQ").css("position","fixed");
	$("#consultationQQ").css("top",QQheight + 'px');

})

function problem(){
	var html='<form id="problemForm">';
	html+='<dl class="alert-cen ">';			
	html+='<dt class="item">';
	html+='<label class="item-label">反馈内容 ：</label>';
	html+='<textarea class="item-textarea" id="feedbackContent" name="feedbackContent"  placeholder="描述您的问题……" maxlength="200"></textarea>';
	html+='</dt>';
	html+='<dt class="item">';
	html+='<label class="item-label">您的邮箱 ：</label>';
	html+='<input type="text"  class="item-text" id="yourMail" name="yourMail"  placeholder="有效的邮箱地址"/>';
	html+='</dt>';
	html+='<dt class="item">';
	html+='<label class="item-label">手机号 ：</label>';
	html+='<input type="text" class="item-text" id="phone" name="phone"  placeholder="输入手机号"/>';
	html+='</dt>';
	html+='<dt class="item">';
	html+='<label class="item-label">QQ ：</label>';
	html+='<input type="text" class="item-text" id="QQ" name="QQ"  placeholder="输入QQ号"/>';
	html+='</dt>';
	html+='</dl>';
	html+='<div class="tipFoot">';
	html+='<div id="serverperson"><span>400-779-7255</span></div>';
	html+='<button onclick="problemSave()" type="submit" class="tipBtn" id="saveBtn">提交反馈</button>';
	html+='</div>';
	html+='</form>';
	$.fn.tipOpen({
		title : "问题反馈",//弹框标题
		width : '500',//弹框内容宽度
		btn : [],//按钮是否显示
		cancel:false,
		concent : html//弹框内容
	});

}

function problemSave(){
	var $formCheckOut = $('#problemForm');
	var validator =$formCheckOut.validate({
			rules:{
				feedbackContent:{ 
					required:true,
					maxlength:200
				},
				yourMail:{
					required:false,
					//problemEmail:true
				},
				phone:{
					required:false,
					//phone:true
				},
				QQ:{
					required:false,
				//	QQ:true
				}
			},
			messages:{
				feedbackContent:{ 
					required:"请填写反馈内容",
					maxlength :$.validator.format("反馈内容不能超过200字")            
				}
			},
		submitHandler: function() {
			var content = $("#feedbackContent").val();
			var email = $("#yourMail").val();
		    var mobile = $("#phone").val();
			var qq = $("#QQ").val();
			$.fn.tipLodding();
	     	$.ajax({
				url:  DOMAIN_PATH + "/client/sys/client/feedback/save?randomdata="+Date.parse(new Date()),
				type:'post',
				dataType:'json',
				data:{
					content  : content ,
					email  : email ,
					mobile  : mobile ,
					qq  : qq 
				},
				success:function(data){
    				if (data.success) {
    					$.fn.tipLoddingEnd(true);
    					$.fn.tipAlert('操作成功',1,1);
    					window.location.reload();
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



