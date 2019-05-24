$(function(){
	   var html ='<div class="fl"><label class="item-label"><span class="xing">*</span>所属角色&nbsp;:</label></div><div class="checkR fl">'
			$.ajax({
			url:  WEB_SERVER_PATH + "/sys/user/manage/list",
			type:'post',
			dataType:'json',
			success:function(data){
				if(data.success){
					var data = data.result;
					var manager=data.manager;
					var opers=data.opers;
					var sales=data.sales;
					var manhtml="<div><h4>管理系统</h4>";
					for(var i  in manager){
						var roleId = manager[i].id;
						var roleName = manager[i].roleName;
						manhtml +='<label><input class="check" type="checkbox" value="'+roleId+'" name="checkbox_role">&nbsp;'+roleName+'&nbsp;</label>';
					}
					manhtml+="</div><div class='clear'></div>";
					var opershtml="<div><h4>运维系统</h4>";
					for(var i  in opers){
						var roleId = opers[i].id;
						var roleName = opers[i].roleName;
						opershtml +='<label><input class="check" type="checkbox" value="'+roleId+'" name="checkbox_role">&nbsp;'+roleName+'&nbsp;</label>';					
					}
					opershtml+="</div><div class='clear'></div>";
					var salehtml="<div><h4>销售系统</h4>";
					for(var i  in sales){
						var roleId = sales[i].id;
						var roleName = sales[i].roleName;
						salehtml +='<label><input class="check" type="checkbox" value="'+roleId+'" name="checkbox_role">&nbsp;'+roleName+'&nbsp;</label>';					
					}
					salehtml+="</div>";
					html+=manhtml+opershtml+salehtml+'</div>';
					$("#jues").html(html);
					redate();
				}
				
			},
			error:function(){
				$.fn.tipAlert('系统异常',1,2);
			}	
	   }) 
	
})
function redate(){
	 $.ajax({
	 		url : WEB_SERVER_PATH + "/sys/user/manage/ajax/detail",
	 		type : 'post',
	 		dataType : 'json',
	 		data : {
	 			userId : uid
	 		},
	 		success : function(data) {
	 			if (data.success) {
	 				var data = data.result;
	 				var departmentName=data.department.departmentName;
	 				var departmentId=data.department.id;
	 				var username=data.user.username;
	 				var realname=data.user.realname;
	 				var mobile=data.user.mobile;
	 				var email=data.user.email;
	 				var roles=data.user.roles;
	 				var identity=data.user.identity;
	 			    var userId = data.user.id;
	 			    if(identity==1){
	 			    	$("#identity").attr("checked",true);
	 			    }else if(identity==2){
	 			    	$("#identity").attr("checked",false);
	 			    }
	 			    $("#modifyUserForm input[type=hidden]").val(userId);
					$("#username").val(username); //不需要验证
					$("#realname").val(realname);
					$("#department").val(departmentName);
					$("#mobile").val(mobile);
					$("#email").val(email);
					$("#department").attr("_id",departmentId);
					
					$("#jues .checkR input[type=checkbox]").each(function(i,ele){
						var id = $(ele).val();
						if($.inArray(Number(id), roles)!==-1){
							$("#jues .checkR input[value="+id+"]").attr("checked",true);
						}
					});
	 			}else{
	 				$.fn.tipAlert(data.message, 1.5, 0);
	 			}
	 		},
	 		error:function(){
				$.fn.tipAlert('系统异常',1.5,0);
			}
		});
}

function departmen(){
	var id=0;
	$('.simSelect-cen').slideToggle(300);
	var treeHtml="";	
		$.ajax({
			url:  WEB_SERVER_PATH + "/sys/user/manage/ajax/getTree?id="+id,
			type:'post',
			dataType:'json',
			success:function(data){					
				var treeResult = data.result;
				var treehtml="";
				for(var i  in treeResult){
					var id = treeResult[i].id;
					var name=treeResult[i].departmentName;
					treeHtml+='<dl>';
					treeHtml+='<dd><i class="treeI" onclick="ajaxDrop('+id+',this,1)"></i><span  onclick="ajaxDrop1('+id+',this,1)">' + name +'</span><div></div></dd>';
					treeHtml+='</dl>';
				}
				var html = '<div class="simSelect-text ">'+treeHtml+'</div>';
				html += '<div class="simSelect-foot"><button class="btn btn-blue" onclick="confirm(this)" type="button">确定</button></div>';

				$('.simSelect-cen').html(html);
			},
			error:function(){
				$.fn.tipAlert('系统异常',1.5,0);
			}
		});
}
function ajaxDrop(id,obj,listFalse){
	$("#addGroupTree").find("span").removeClass("red");
	$(obj).parent().siblings('dl').find('dd').hide();
	var parentObj = $(obj).parent();
	if(id==0){
	 return;
	}
	if($(parentObj).hasClass('turn')){
		$(parentObj).removeClass('turn');
		$(parentObj).find('div').hide();
		return;
	}else{
		$(parentObj).addClass('turn');
		$(parentObj).find('div').show();
	}
	if(listFalse == 2){
		$(obj).next().addClass('red');
	}
	if(listFalse == 1){
		if($(obj).hasClass('red')){
			$(obj).removeClass('red');
			$('#department').val('');
			$('#department').attr('_ID','');
		}else{
			$('#department').val($(obj).text());
			$('#department').attr('_ID',id);
			$('dt,span').removeClass('red');
			$(obj).addClass('red');
		}
	}
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/user/department/ajax/showChildrenNode",
		type:'post',
		data:{
			id : id
		},
		dataType:'json',
		success:function(data){
			var treeResult = data.result;
			var html ='';
			for(var i  in treeResult){
				var name = treeResult[i].departmentName;
				var id = treeResult[i].id;
				html += '<dl>';
				html += '<dd id="'+id+'"><i class="treeI" onclick="ajaxDrop('+id+',this,'+listFalse+')"></i><span onclick="ajaxDrop1('+id+',this,'+listFalse+')">'+name+'</span><div></div></dd>';
				html += '</dl>';
			}
			$(obj).next().next().html(html);

		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
function ajaxDrop1(id,obj,listFalse){
	if(id==0){
		if($(obj).hasClass('red')){
			$(obj).removeClass('red');
			$(obj).parent().parent().siblings('dl').hide();
		}else{
			$(obj).addClass('red');
			$(obj).parent().parent().siblings('dl').show();
		}
	}
	$("#addGroupTree").find("span").removeClass("red");
	$(obj).parent().siblings('dl').find('dd').hide();
	var parentObj = $(obj).parent();
	if($(parentObj).hasClass('turn')){
		$(parentObj).removeClass('turn');
		$(parentObj).find('div').hide();
		return;
	}else{
		$(parentObj).addClass('turn');
		$(parentObj).find('div').show();
	}
	if(listFalse == 2){
		$(obj).addClass('red');

	}
	if(listFalse == 1){
		if($(obj).hasClass('red')){
			$(obj).removeClass('red');
			$('#department').val('');
			$('#department').attr('_ID','');
		}else{
			$('#department').val($(obj).text());
			$('#department').attr('_ID',id);
			$('dt,span').removeClass('red');
			$(obj).addClass('red');
		}
	}
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/user/department/ajax/showChildrenNode",
		type:'post',
		data:{
			id : id
		},
		dataType:'json',
		success:function(data){
			var treeResult = data.result;
			var html ='';
			for(var i  in treeResult){
				var name = treeResult[i].departmentName;
				var id = treeResult[i].id;
				html += '<dl>';
				html += '<dd id="'+id+'" ><i class="treeI" onclick="ajaxDrop('+id+',this,'+listFalse+')"></i><span onclick="ajaxDrop1('+id+',this,'+listFalse+')">'+name+'</span><div></div></dd>';
				html += '</dl>';

			}
			$(obj).next().html(html);

		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
function confirm(obj){
	var department = $("#department").val();
	$('.simSelect-cen').slideUp(300);
}
function chooseNode(id,obj){
	if($(obj).hasClass('red')){
		$(obj).removeClass('red');
		$('#department').val('');
		$('#department').attr('_ID','');
	}else{
		$('#department').val($(obj).text());
		$('#department').attr('_ID',id);
		$('dt,span').removeClass('red');
		$(obj).addClass('red');
	}
}
var jues="";
function modifyAddBox(){
		var departmentaa= $("#department").attr("_id");
		var $modifyUserForm = $('#modifyUserForm');
		var validator=$modifyUserForm.validate({
			rules:{
				username:{ //用户名
					required:true,
					user_name:true
				},
				realname:{
					required:true,
					real_name:true
				},
				department:{
					required:true
				},
				mobile:{
					required:true,
					phone:true
				},email:{
					required:true,
					userEmail:true
				}
			},
			messages:{
				username:{
					required:"请填写用户名"
				},
				realname:{
					required:"请填写姓名"
				},
				department:{
					required:"请选择所在部门"
				},
				mobile:{
					required:"请填写手机号"
				},
				email:{
					required:"请填写邮箱",
					userEmail:"请输入正确的邮箱"
				}
			},
			submitHandler: function() {
				var username = $("#username").val();
				var realname = $("#realname").val();
				var departmentId=$(".simSelect-title input[type=text]").attr("_id");
			    var mobile = $("#mobile").val();
				var email = $("#email").val();
				var identity;
				if($("#identity").is(':checked')) {
					identity=1;
				}else{
					identity=2;
				}
				var userid=$("#modifyUserForm input[type=hidden]").val();
				$("#jues .checkR input:checkbox:checked").each(function(i, obj) {
					if (jues != "" && null != jues) {
						jues = jues + "," + $(obj).val();
					} else {
						jues = $(obj).val();
					}
				});
				var strArr=jues.split(",");
				$.unique(strArr);
				var juesnew="";
				for(var i=0;i<strArr.length; i++){
					juesnew+=strArr[i]+",";
				}
				juesnew=juesnew.substring(0,juesnew.length-1);
				$.fn.tipLodding();
		     	$.ajax({
					url:  WEB_SERVER_PATH + "/sys/user/manage/ajax/modify?randomdata="+Date.parse(new Date()),
					type:'post',
					dataType:'json',
					data:{
						username: username,
						realname :realname,
						departmentId :departmentId,
						mobile :mobile,
						email :email,
						identity:identity,
						roles  :juesnew,
						userId :userid
					},
					success:function(data){
	    				if (data.success) {
	    					$.fn.tipLoddingEnd(true);
	    					$.fn.tipAlert('用户修改成功',1,1);
	    					goPage('/sys/user/manage');
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