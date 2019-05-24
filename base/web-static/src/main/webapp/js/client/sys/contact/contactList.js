//新建个人组和共享组
function adduserBox(groupType){
	var html='<div class="tipcen" id="warp">';
		html+='<div class="clear"  id="add">';
		html+='<form method="post" id="addForm">';
		html+='<div class="item"><label class="item-label">';
		html+='<span class="xing">*</span>组名：</label>';
		html+='<input type="text" maxlength="20" name="groupName" class="item-text" id="groupName"></div>';
		html+='<div class="tipFoot">';
		html+='<button onclick="addGroup('+groupType+')" id="saveBtn" class="tipBtn" type="submit">确 定</button>';
		html+='<button class="tipBtn tip-cancel" type="button" onclick="addCancel()">取 消</button>';
		html+='</div></form></div></div>';
		if(groupType==0){
			$.fn.tipOpen({
				title : "新建个人组",
				width : '400',
				btn : [],
				cancel:false,
				concent : html
			});
		}else{
			$.fn.tipOpen({
				title : "新建共享组",
				width : '400',
				btn : [],
				cancel:false,
				concent : html
			});
		}
			
}
function addCancel(){
	$.fn.tipShut();
}
//新建个人组和共享组保存
function addGroup(groupType){
	var $formCheckOut = $('#addForm');
	var validateor=$formCheckOut.validate({
    	rules:{
    		groupName:{
    			required:true,
    			groupNameExg:true //组名不能包含特殊字符
    		}
    	},
		messages:{
			groupName:{
				required:"组名不可为空 "
			}
		},
		submitHandler: function() {
			var groupName = $("#groupName").val();
			$.fn.tipLodding();
				$.ajax({
					url : WEB_SERVER_PATH + "/sys/client/contact/ajax/addGroup",
					type : 'post',
					dataType : 'json',
					data : {
						groupType : groupType,
						groupName : groupName
					},
					success : function(data) {
						if (data.success) {
							$.fn.tipLoddingEnd(true);
							$.fn.tipAlert('组名添加成功',1,1);
							showTree();
							if(groupType==0){
								loadConcats(0,groupType,this); 
							}else if(groupType==1){
								loadConcatsTwo(0,groupType,this); 
							}
						} else {
							$.fn.tipLoddingEnd(false);
							$.fn.tipAlert(data.message,1.5,0);
						}
					},
					error : function() {
						$.fn.tipLoddingEnd(false);
						validateor.resetForm();
						$.fn.tipAlert('系统异常',1.5,2);
					}
				});
        }
    });
}
//编辑组名
function editName(obj){
	if($(obj).parent().prev().find('input').hasClass('disabledInput')){
		$(obj).parent().prev().find('input').removeClass('disabledInput').removeAttr('disabled');
	}else{
		$(obj).parent().prev().find('input').addClass('disabledInput').attr('disabled','disabled');
	}	
}
//修改个人组和公开组
function keepName(id,obj){
	var name = $(obj).val();
	var reg = /^([\u4E00-\u9FA5]|\w)*$/;
	if(!reg.test(name)){
		$.fn.tipAlert('组名不能包含特殊字符',1.5,0);
		return false;
	}
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/contact/ajax/modifyGroup",
		type:'post',
		data:{
			id : id,
			groupName : name
		},
		dataType:'json',
		success:function(data){
				if(data.success){
					$.fn.tipLoddingEnd(true);
					$.fn.tipAlert("编辑成功",1,1);
					$(obj).addClass('disabledInput').attr('disabled','disabled');
					showTree();
					var groupType=$("#typeVal").val(); //个人和共享类型
					if(groupType==0){
						loadConcats(0,groupType,this); 
					}else if(groupType==1){
						loadConcatsTwo(0,groupType,this); 
					}
				}else{
					$.fn.tipAlert(data.message,1.5,0);
				}
			},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
//删除组名操作
function deleteDepartment(id){
	$.fn.tipOpen({
		title : "删除",//弹框标题
		width : '400',//弹框内容宽度
		height: '50',
		btn : [{
			label : '确定',
			onClickFunction : 'deleDepar('+id+')'
		}],//按钮是否显示
		concent : '<div style="font-size:14px;text-align:center;">确定要删除当前部门以及部门所包含的成员?</div>'//弹框内容
	});
}
function deleDepar(id){
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/contact/ajax/deleteGroup",
		type:'post',
		data:{
			id : id
		},
		dataType:'json',
		success:function(data){	
			if(data.success){
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('删除成功',1,1);
				showTree();
				var groupType=$("#typeVal").val(); //个人和共享类型
				if(groupType==0){
					loadConcats(0,groupType,this); 
				}else if(groupType==1){
					loadConcatsTwo(0,groupType,this); 
				}
			}else{
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
//新建联系人
function addimgBox(){
	var html='<div class="tipcen">';
	html+='<input type="hidden" id="isAddMember" value="false" />';
	html+='<dl class="alert-cen">';
	html+='<dt ><label><font class="xing">*</font>姓名：</label><input type="text" value="" id="username" style="width:300px"/></dt>';
	html+='<dt ><label><font class="xing">*</font>手机号：</label><input type="text" value="" id="mobile" style="width:300px"/></dt>';
	html+='<dt ><label>邮箱：</label><input type="text" value="" id="email" style="width:300px"/></dt>';
	html+='<dt ><label>QQ：</label><input type="text" value="" id="qq" style="width:300px"/></dt>';
	html+='<dt class="groupType"><label><font class="xing">*</font>组类别：</label><input type="text" id="groupType" typeVal="" disabled ="disabled" /><span onclick="showUl()"><i></i></span><ul id="groupTypeul" style="display:none;"><li valType="0" onclick="showDefault(0)">个人组</li><li valType="1" onclick="showDefault(1)">共享组</li></ul></dt>';
	html+='<dt class="subGroup"><label style="float:left;"><font class="xing">*</font>所属组：<input type="hidden" id="gruopHidden" value="0" /></label><div class="simSelect-title" onclick="userList()"><input type="text" value="" id="department" disabled="disabled" /><span class="display:inline-block;float:left;"><i></i></span><div></dt>'
	html+='<dt><label class="fl">　</label><div class="simSelect-cen simSele"></div></dt>';
	html+='</dl>';
	html+='<table id="roleTable" style="width:100%">';
	html+='</table>';
	html+='<div class="redact" onclick="redact()" style="min-width:100px;"><span><center>更多编辑</center><span></div>';
	html+='<div class="conceal" >';
	html+='<dl class="alert-cen" style="display:none;" id="alertre">';
	html+='<dt ><label>生日：</label><input type="text" onfocus="WdatePicker()" value="" id="birthday" style="width:300px"/></dt>';
	html+='<dt ><label>单位：</label><input type="text" value="" id="company" style="width:300px"  maxLength="20"/></dt>';
	html+='<dt ><label>职位：</label><input type="text" value="" id="position" style="width:300px"  maxLength="50"/></dt>';
	html+='<dt ><label>公司地址：</label><input type="text" value="" id="companyAddress" style="width:300px"  maxLength="100"/></dt>';
	html+='</dl>';
	html+='<div>';
	$.fn.tipOpen({
		title : '新建联系人',//弹框标题
		width : '500',//弹框内容宽度
		btn : [{
			label : '确定',
			onClickFunction : 'addDepartmenty()'
		}],//按钮是否显示
		concent : html//弹框内容
	});
	showDefault(0);
			
}
//更多编辑
function redact(){
	$("#alertre").toggle();	
}
//组类别下拉框
function showUl(){
	var disval=$("#groupTypeul").css("display");
	if(disval=="none"){
		$("#groupTypeul").css("display","block");
	}else{
		$("#groupTypeul").css("display","none");
	}
}
function showDefault(num){
	if(num==0){
		$("#department").val("");
		$("#groupType").attr("typeVal","0");
		$("#groupType").val('个人组');
	}else if(num==1){
		$("#department").val("");
		$("#groupType").attr("typeVal","1");
		$("#groupType").val('共享组');
	}
	$("#groupTypeul").css("display","none");
	showTreeList();
}
//所属组下拉框
function userList(){
	$('.simSelect-cen').slideToggle(300);
}

function redact(){
	$("#alertre").toggle();	
}
//加载所属组 --个人和共享
function showTreeList(){
	var groupType;
	var gruopHidden = $("#gruopHidden").val();
	if(gruopHidden == 0){
		 groupType=$("#groupType").attr("typeval"); 
	}else{
		groupType=$("#typeVal").val(); 
	}
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/contact/ajax/listOwnGroup",
		type:'post',
		data:{
			groupType : groupType
		},
		dataType:'json',
		success:function(data){	
				var userHtml="";
				var usergroupTreeResult=data.result;
				if(gruopHidden == 0){
					if(usergroupTreeResult.length>0){
						$("#department").val("");
						$("#department").attr("_id","");
						$("#department").val(usergroupTreeResult[0].groupName);
						$("#department").attr("_id",""+usergroupTreeResult[0].id+"");
					}
				}
				for(var i  in usergroupTreeResult){
					var groupName= usergroupTreeResult[i].groupName;
					var parentID = usergroupTreeResult[i].id;
					userHtml += '<dl><dt id="'+parentID+'">';
					userHtml += '<span onclick="loadConcatsList('+parentID+',this)">'+groupName+'</span>';
					userHtml += '</dt>';
					userHtml += '</dl>';
				}
			var html = '<div class="simSelect-text ">'+userHtml+'</div>';
			html += '<div class="simSelect-foot"><button class="btn btn-blue" onclick="built(this)" type="button">确定</button></div>';
			$('.simSelect-cen').html(html);
		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
function loadConcatsList(id,obj){
	var text=$(obj).text();
	$('#department').attr('_id',id);
	$('#department').val(text);
	$("span[class='xing']").removeClass("xing");
	$(obj).addClass("xing");
	var groupType = $("#typeVal").val();
	if(groupType ==0){
		loadConcats(id,groupType,obj);
	}else{
		loadConcatsTwo(id,groupType,obj);
	}
}
function built(obj){
	var department = $("#department").val();	
	if(department == null || department ==""){
		$.fn.tipAlert('所属组不可为空',1.5,0);
		return false;
	}	
	$('.simSelect-cen').slideUp(300);
}

//增加联系人
function addDepartmenty(){
	var isAddMember = $("#isAddMember").val();
	var username = $('#username').val();
	var mobile = $('#mobile').val();
	var email = $('#email').val();
	var qq = $('#qq').val();
	var groupType = $("#groupType").attr("typeval");//组类别
	var parentId = $('#department').attr('_id'); //所属组
	var birthday =$('#birthday').val();
	var company =$('#company').val();
	var position = $('#position').val();
	var companyAddress =$('#companyAddress').val();
	if(username == ""){
		$.fn.tipAlert('姓名不能为空',1.5,0);
		return false;
	}
	var reg = /^[\u4E00-\u9FA5A-Za-z\s.]{0,50}$/;
	if(!reg.test(username)){
		$.fn.tipAlert('姓名支持中文、英文、空格 .最多50个',1.5,0);
		return false;
	}
	if(mobile == ""){
		$.fn.tipAlert('手机号不能为空',1.5,0);
		return false;
	}
	var partten = /^1[3,4,5,6,7,8,9]\d{9}$/;
    if(!partten.test(mobile)){
    	$.fn.tipAlert('手机号码不正确',1.5,0);
        return false;
    }
    //邮箱匹配到中文  
    var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[\u4e00-\u9fa5a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if(email != ""){
    	if(!reg.test(email)){
    		$.fn.tipAlert("Email格式不正确 ",1.5,0);
    		return false;
    	}
	}
    var qqExg =/^[1-9]\d{4,10}$/;
    if(qq !=""){
    	if(!qqExg.test(qq)){
    		$.fn.tipAlert('请输入正确的QQ号',1.5,0);
    		return false;
    	}
    }
	var department = $("#department").val();	
	if(department == null || department ==""){
		$.fn.tipAlert("所属组不能为空",1.5,0);
		return false;
	}
    if(company != ''){
    	if(!validate.loginName(company)){
        	$.fn.tipAlert("单位不能包含特殊符号",1.5,0);
    		return false;
        }
    }
    if(position != ''){
    	if(!validate.loginName(position)){
        	$.fn.tipAlert("职位不能包含特殊符号",1.5,0);
    		return false;
        }
    }
    $.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/contact/ajax/addContact",
		type:'post',
		data:{
			isAddMember : isAddMember,
			realName : username,  //姓名
			mobile : mobile, //手机号
			email : email, //邮箱
			qq : qq, //QQ 
			groupType : groupType, // 组类型[0-个人组,1-共享组]
			groupId : parentId, //所属组
			birthday : birthday, //生日
			company : company,//单位
			position : position, //职位
			companyAddress : companyAddress //公司地址
		},
		dataType:'json',
		success:function(data){
			if(data.success){
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('联系人添加成功',1.5,1);
				window.location.reload();
			}else{
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}						
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
//修改联系人
function editVerb(assignId){
	var html='<div class="tipcen">';
	html+='<dl class="alert-cen">';
	html+='<dt ><label><font class="xing">*</font>姓名：</label><input type="text" value="" id="username" style="width:300px"/></dt>';
	html+='<dt ><label><font class="xing">*</font>手机号：</label><input type="text" value="" id="mobile" style="width:300px"/></dt>';
	html+='<dt ><label>邮箱：</label><input type="text" value="" id="email" style="width:300px"/></dt>';
	html+='<dt ><label>QQ：</label><input type="text" value="" id="qq" style="width:300px"/></dt>';
	html+='<dt ><label>组类别：</label><input id="groupTypeInput" disabled="disabled" />';
	html+='<dt class="subGroup" onclick="showTreeList()"><label style="float:left;"><font class="xing">*</font>所属组：<input type="hidden" id="gruopHidden" value="1" /></label><div class="simSelect-title" onclick="userList()"><input type="text" value="" id="department" disabled="disabled" /><span class="display:inline-block;float:left;"><i></i></span><div></dt>'
	html+='<dt><label class="fl">　</label><div class="simSelect-cen simSele"></div></dt>';
	html+='</dl>';
	html+='<table id="roleTable" style="width:100%">';
	html+='</table>';
	html+='<div class="redact" onclick="redact()" style="min-width:100px;"><span><center>更多编辑</center><span></div>';
	html+='<div class="conceal" >';
	html+='<dl class="alert-cen" style="display:none;" id="alertre">';
	html+='<dt ><label>生日：</label><input type="text" onfocus="WdatePicker()" value="" id="birthday" style="width:300px"/></dt>';
	html+='<dt ><label>单位：</label><input type="text" value="" id="company" style="width:300px"  maxLength="20"/></dt>';
	html+='<dt ><label>职位：</label><input type="text" value="" id="position" style="width:300px"  maxLength="50"/></dt>';
	html+='<dt ><label>公司地址：</label><input type="text" value="" id="companyAddress" style="width:300px" maxLength="100"/></dt>';
	html+='</dl>';
	html+='<div>';
	$.fn.tipOpen({
		title :'修改联系人',//弹框标题
		width : '500',//弹框内容宽度
		btn : [{
			label :'确定',
			onClickFunction : 'modifyContact('+assignId+')'
		}],//按钮是否显示
		concent : html//弹框内容
	});
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/contact/ajax/info",
		type:'post',
		dataType:'json',
		data : {
			assignId : assignId
		},
		success:function(data){	
			if(data.success){
				var info = data.result;
				$("#username").val(info.realName);
				$("#mobile").val(info.mobile);
				$("#email").val(info.email);
				$("#qq").val(info.qq);
				var groupType = $("#typeVal").val();
				if(groupType==0){
					$("#groupTypeInput").val("个人组");
				}else{
					$("#groupTypeInput").val("共享组");
				}
				$("#department").val(info.groupName);
				$("#department").attr("_id",info.groupId);
				if(null!=info.birthday && ""!=info.birthday){
					$("#birthday").val(info.birthday.substring(0,10));
				}
				$("#company").val(info.company);
				$("#position").val(info.position);
				$("#companyAddress").val(info.companyAddress);
			}else{
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
//修改联系人保存
function modifyContact(assignId){
	var username = $('#username').val();
	var mobile = $('#mobile').val();
	var email = $('#email').val();
	var qq = $('#qq').val();
	var groupType = $("#typeVal").val();//组类别
	var parentId = $('#department').attr('_id'); //所属组
	var birthday =$('#birthday').val();
	var company =$('#company').val();
	var position = $('#position').val();
	var companyAddress =$('#companyAddress').val();
	if(username == ""){
		$.fn.tipAlert('姓名不能为空',1.5,0);
		return false;
	}
	var reg = /^[\u4E00-\u9FA5A-Za-z\s.]{0,50}$/;
	if(!reg.test(username)){
		$.fn.tipAlert('姓名支持中文、英文、空格 .最多50个',1.5,0);
		return false;
	}
	if(mobile == ""){
		$.fn.tipAlert('手机号不能为空',1.5,0);
		return false;
	}
	var partten = /^1[3,4,5,6,7,8,9]\d{9}$/;
    if(!partten.test(mobile)){
    	$.fn.tipAlert('手机号码不正确',1.5,0);
        return false;
    }
    //邮箱匹配到中文  
    var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[\u4e00-\u9fa5a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if(email != ""){
    	if(!reg.test(email)){
    		$.fn.tipAlert("Email格式不正确 ",1.5,0);
    		return false;
    	}
	}
    var qqExg =/^[1-9]\d{4,10}$/;
    if(qq !=""){
    	if(!qqExg.test(qq)){
    		$.fn.tipAlert('请输入正确的QQ号',1.5,0);
    		return false;
    	}
    }
	var department = $("#department").val();	
	if(department == null || department ==""){
		$.fn.tipAlert("所属组不能为空",1.5,0);
		return false;
	}
    if(company != ''){
    	if(!validate.loginName(company)){
        	$.fn.tipAlert("单位不能包含特殊符号",1.5,0);
    		return false;
        }
    }
    if(position != ''){
    	if(!validate.loginName(position)){
        	$.fn.tipAlert("职位不能包含特殊符号",1.5,0);
    		return false;
        }
    }
    $.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/contact/ajax/modifyContact",
		type:'post',
		data:{
			assignId : assignId,
			realName : username,  //姓名
			mobile : mobile, //手机号
			email : email, //邮箱
			qq : qq, //QQ 
			groupId : parentId, //所属组
			birthday : birthday, //生日
			company : company,//单位
			position : position, //职位
			companyAddress : companyAddress //公司地址
		},
		dataType:'json',
		success:function(data){
			if(data.success){
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('联系人修改成功',1.5,1);
				var groupType=$("#typeVal").val(); //个人和共享类型
				if(groupType==0){
					$(".card li[grouptype='"+groupType+"']").addClass('li-on').siblings().removeClass('li-on');
					$(".card li[grouptype='"+groupType+"']").parent().siblings('.departBox').hide().eq(0).show();
					emTable("emTableConfig"); 
					$("#addGroupTree ul li div").removeClass("xing");
					$("#addGroupTree ul li").each(function(index,obj){
						if($(obj).attr("id") == parentId){
							$(obj).find("div").addClass("xing");
						}
					});
				}else{
					$(".card li[grouptype='"+groupType+"']").addClass('li-on').siblings().removeClass('li-on');
					$(".card li[grouptype='"+groupType+"']").parent().siblings('.departBox').hide().eq(1).show();
					emTable("emTableConfigTwo");
					$("#addGroupTreeShare ul li div").removeClass("xing");
					$("#addGroupTreeShare ul li").each(function(index,obj){
						if($(obj).attr("id") == parentId){
							$(obj).find("div").addClass("xing");
						}
					});
				}
			}else{
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}						
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
//查看联系人详情
function check(assignId){
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/contact/ajax/info",
		type: 'post',
		dataType:'json',
		data:{
			assignId:assignId			
		},
		success:function(data){
			if(!data.success){
				$.fn.tipAlert(data.message,1.5,0);
				return;
			}
			var userResult=data.result;
			var username=userResult.realName;
			var mobile=userResult.mobile;
			var email=userResult.email;
			var qq=userResult.qq;
			var groupType = $("#typeVal").val();
			var groupName=userResult.groupName;
			var birthday=userResult.birthday;
			var company=userResult.company;
			var position=userResult.position;
			var companyAddress=userResult.companyAddress;	
			
			var html='<div class="tipcen">';
			html+='<dl class="alert-cen">';
			html+='<dt><label>姓名：</label><input type="text" value="'+username+'" id="usernames" disabled="disabled" style="border:0px" /></dt>';
			html+='<dt><label>手机号：</label><input type="text" value="'+mobile+'" id="mobile" disabled="disabled" style="border:0px" /></dt>';
			html+='<dt><label>邮箱：</label><input type="text" value="'+(email==null || email==""? "" :email)+'" id="email" disabled="disabled" style="border:0px" /></dt>';
			html+='<dt><label>QQ：</label><input type="text" value="'+(qq==null || qq==""? "" :qq)+'" id="qq" disabled="disabled" style="border:0px" /></dt>';
			if(groupType==0){
				html+='<dt><label>组类别：</label><input type="text" value="个人组"  disabled="disabled" style="border:0px" /></dt>';
			}else{
				html+='<dt><label>组类别：</label><input type="text" value="共享组"  disabled="disabled" style="border:0px" /></dt>';
			}
			html+='<dt><label>所属组：</label><input type="text" value="'+(groupName==null || groupName=="" ? "" : groupName)+'" id="fullPathName" disabled="disabled" style="border:0px" /></dt>';
			html+='<dt><label>生日：</label><input type="text" value="'+(birthday==null || birthday=="" ? "": birthday.substring(0,10))+'" id="birthday" disabled="disabled" style="border:0px" /></dt>';
			html+='<dt><label>单位：</label><input type="text" value="'+(company==null || company=="" ? "":company)+'" id="company" disabled="disabled" style="border:0px" /></dt>';
			html+='<dt><label>职位：</label><input type="text" value="'+(position==null || position=="" ? "":position)+'" id="position" disabled="disabled" style="border:0px" /></dt>';
			html+='<dt><label>公司地址：</label><input type="text" value="'+(companyAddress==null || companyAddress=="" ? "" : companyAddress)+'" id="companyAddress" disabled="disabled" title="'+(companyAddress==null || companyAddress=="" ? "" : companyAddress)+'" style="border:0px;display:inline-block;width:360px;" /></dt>';
			html+='</dl>';
			html+='</div>';
			$.fn.tipOpen({
				title : '查看联系人详情',//弹框标题
				width : '500',//弹框内容宽度
				btn : [],//按钮不显示
				concent : html//弹框内容
			});	
		},error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
		
	});
	
}

//删除联系人
function deleteDepartments(assignId,realName){
	$.fn.tipOpen({
		title : '删除确认',//弹框标题
		width : '300',//弹框内容宽度
		height: '40',
		btn : [{
			label : '确定',
			onClickFunction : 'deleDepars('+assignId+')'
		}],//按钮是否显示
		concent : '<div style="font-size:14px;text-align:center;">确定要删除['+realName+']成员?</div>'//弹框内容
	});	
}
function deleDepars(assignId){
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/contact/ajax/deleteContact",
		type:'post',
		data:{
			assignId : assignId
		},
		dataType:'json',
		success:function(data){	
			if(data.success){
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('删除成功',1,1);
				var groupType=$("#typeVal").val(); //个人和共享类型
				if(groupType==0){
					$(".card li[groupType='"+groupType+"']").addClass('li-on').siblings().removeClass('li-on');
					$(".card li[groupType='"+groupType+"']").parent().siblings('.departBox').hide().eq(0).show();
					emTable("emTableConfig"); 
					var number = $("#outerId_page_div font").text();
					$("#num").text(number);
				}else{
					$(".card li[groupType='"+groupType+"']").addClass('li-on').siblings().removeClass('li-on');
					$(".card li[groupType='"+groupType+"']").parent().siblings('.departBox').hide().eq(1).show();
					emTable("emTableConfigTwo");
					var number = $("#outerIdShare_page_div font").text();
					$("#numShare").text(number);
				}
			}else{
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*添加成员*/
function usermenberboxs(deptId){
	var groupType = $("#typeVal").val();
	var html='<div class="tipcen">';
	html+='<dl class="alert-cen">';
	html+='<input type="hidden" id="isAddMember" value="true" />';
	html+='<dt ><label><font class="xing">*</font>姓名：</label><input type="text" value="" id="username" style="width:300px"/></dt>';
	html+='<dt ><label><font class="xing">*</font>手机号：</label><input type="text" value="" id="mobile" style="width:300px"/></dt>';
	html+='<dt ><label>邮箱：</label><input type="text" value="" id="email" style="width:300px"/></dt>';
	html+='<dt ><label>QQ：</label><input type="text" value="" id="qq" style="width:300px"/></dt>';
	html+='</dl>';
	html+='<table id="roleTable" style="width:100%">';
	html+='</table>';
	html+='<div class="redact" onclick="redact()" style="min-width:100px;"><span><center>更多编辑</center><span></div>';
	html+='<div class="conceal" >';
	html+='<dl class="alert-cen" style="display:none;" id="alertre">';
	html+='<dt ><label>生日：</label><input type="text" onfocus="WdatePicker()" value="" id="birthday" style="width:300px"/></dt>';
	html+='<dt ><label>单位：</label><input type="text" value="" id="company" style="width:300px" maxLength="20" /></dt>';
	html+='<dt ><label>职位：</label><input type="text" value="" id="position" style="width:300px"  maxLength="50" /></dt>';
	html+='<dt ><label>公司地址：</label><input type="text" value="" id="companyAddress" style="width:300px"  maxLength="100" /></dt>';
	html+='</dl>';
	html+='<div>';
	$.fn.tipOpen({
		title :'添加成员',//弹框标题
		width : '500',//弹框内容宽度
		btn : [{
			label :'确定',
			onClickFunction : 'addUsermenber('+deptId+','+groupType+')'
		}],//按钮是否显示
		concent : html//弹框内容
	});	
}
function addUsermenber(deptId,groupType){
	var isAddMember = $("#isAddMember").val();
	var username = $('#username').val();
	var mobile = $('#mobile').val();
	var email = $('#email').val();
	var qq = $('#qq').val();
	var birthday =$('#birthday').val();
	var company =$('#company').val();
	var position = $('#position').val();
	var companyAddress =$('#companyAddress').val();
	if(username == ""){
		$.fn.tipAlert('姓名不能为空',1.5,0);
		return false;
	}
	var reg = /^[\u4E00-\u9FA5A-Za-z\s.]{0,50}$/;
	if(!reg.test(username)){
		$.fn.tipAlert('姓名支持中文、英文、空格 .最多50个',1.5,0);
		return false;
	}
	if(mobile == ""){
		$.fn.tipAlert('手机号不能为空',1.5,0);
		return false;
	}
	var partten = /^1[3,4,5,6,7,8,9]\d{9}$/;
    if(!partten.test(mobile)){
    	$.fn.tipAlert('手机号码不正确',1.5,0);
        return false;
    }
    //邮箱匹配到中文  
    var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[\u4e00-\u9fa5a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if(email != ""){
    	if(!reg.test(email)){
    		$.fn.tipAlert("Email格式不正确 ",1.5,0);
    		return false;
    	}
	}
    var qqExg =/^[1-9]\d{4,10}$/;
    if(qq !=""){
    	if(!qqExg.test(qq)){
    		$.fn.tipAlert('请输入正确的QQ号',1.5,0);
    		return false;
    	}
    }
    if(company != ''){
    	if(!validate.loginName(company)){
        	$.fn.tipAlert("单位不能包含特殊符号",1.5,0);
    		return false;
        }
    }
    if(position != ''){
    	if(!validate.loginName(position)){
        	$.fn.tipAlert("职位不能包含特殊符号",1.5,0);
    		return false;
        }
    }
    $.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/contact/ajax/addContact",
		type:'post',
		data:{
			isAddMember : isAddMember,
			groupType : groupType,
			groupId : deptId,
			realName : username,  //姓名
			mobile : mobile, //手机号
			email : email, //邮箱
			qq : qq, //QQ 
			birthday : birthday, //生日
			company : company,//单位
			position : position, //职位
			companyAddress : companyAddress //公司地址
		},
		dataType:'json',
		success:function(data){
			if(data.success){
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('成员添加成功',1.5,1);
				if(groupType==0){
					$(".card li[groupType='"+groupType+"']").addClass('li-on').siblings().removeClass('li-on');
					$(".card li[groupType='"+groupType+"']").parent().siblings('.departBox').hide().eq(0).show();
					emTable("emTableConfig"); 
					var number = $("#outerId_page_div font").text();
					$("#num").text(number);
				}else{
					$(".card li[groupType='"+groupType+"']").addClass('li-on').siblings().removeClass('li-on');
					$(".card li[groupType='"+groupType+"']").parent().siblings('.departBox').hide().eq(1).show();
					emTable("emTableConfigTwo");
					var number = $("#outerIdShare_page_div font").text();
					$("#numShare").text(number);
				}
			}else{
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}						
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}

//下载导入模板
function downTemplate(){
	window.open(WEB_STATIC_PATH+"/template/contact.xlsx","_self");
}
//导入
function showImport(id){
	var addHtml = "<input type='file' id='fileInput' name='fileInput'/><div class='tipPrompt red'></div>";
	$.fn.tipOpen({
		title : '导入',
		width : '325',
		height: '80',
		btn : [{
			label : '导入',
			onClickFunction : 'importFile(this,'+id+')'
		}],//按钮是否显示
		concent : addHtml//弹框内容
		
	});
}
//导入文件
function importFile(obj,id){
	var importFile = $('#fileInput').val();
	var fileId = 'fileInput';
	if($("#fileInput").val() === ''){
		fileId = 'null';
		$.fn.tipAlert('请选择上传的文件',1.5,2);
		return;
	}
	var groupType=$("#typeVal").val(); //个人和共享类型
	$(obj).parents('.tipBorder').hide();
	$('.layer').remove();
	$('.tipLoadding').tipLog('open');
	$.ajaxFileUpload({
		url :  WEB_SERVER_PATH + '/sys/client/contact/importContact?groupId='+id,
		secureuri : false,
		fileElementId : fileId,//文件上传控件的id属性 
		dataType : 'json',	
		data:{},
		success : function(data){	
			$('.tipLoadding').tipLog('close');
			var html  = '<div id="tipMsg"></div>';
			html += '<div class="tipFoot" style="margin-top:50px;">';
			html += '<button id="determine" class="tipBtn" type="button" onclick="determineImport()">确定</button>';
			html += '<button id="download" class="tipBtn" type="button" onclick="downloads()">下载失败详情</button>';
			html += '</div>';
			$.fn.tipOpen({
				title : '导入状态',//弹框标题
				width : '300',//弹框内容宽度
				cancel : false,
				tipClose : false,
				concent : html//弹框内容
			});
			if(data.success){
				var data  = data.result;
				downloadKey = data.downloadKey;
				var msg = '<label>导入总条数：</label><span>'+data.sum+'条</span><br/><label>导入成功条数：</label><span>'+data.success+'条</span><br/><label>导入失败条数：</label><a href="javascript:void(0)" >'+data.fail+'条</a><br/>';
				if(data.fail==undefined){
					var msg = '<label>导入总条数：</label><span>'+data.sum+'条</span><br/><label>导入成功条数：</label><span>'+data.success+'条</span><br/><label>导入失败条数：</label><a href="javascript:void(0)" >'+0+'条</a><br/>';
					data.fail=0;
				}
				if(data.fail==0){
					$("#download").css({"background":"#ccc","border-color":"#ccc"});
					$("#download").removeAttr("onclick");
				}
				$('#tipMsg').html(msg);
				$('#tipMsg').css("margin-top","10px").css("text-align","center");
				
				
				
				if(groupType==0){
					$(".card li[groupType='"+groupType+"']").addClass('li-on').siblings().removeClass('li-on');
					$(".card li[groupType='"+groupType+"']").parent().siblings('.card-cen').hide().eq(0).show();
					emrefulsh("emTableConfig");
					showTree(id);
					
				}else if(groupType==1){
					$(".card li[groupType='"+groupType+"']").addClass('li-on').siblings().removeClass('li-on');
					$(".card li[groupType='"+groupType+"']").parent().siblings('.card-cen').hide().eq(1).show();
					emrefulsh("emTableConfigTwo");
					showTree(id);
				}
				
				
			}else{
				$('#tipMsg').css("text-align","center").css("margin-top","10px");
				$("#download").css({"background":"#ccc","border-color":"#ccc"});
				$("#download").removeAttr("onclick");
				$('#tipMsg').html( data.message );
			}
		},
		error : function() {
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
}
function determineImport(){
	$('.layer,.tipBorder').remove();
}

//下载
function downloads(){
	window.location.href=WEB_SERVER_PATH + '/sys/client/contact/exportExcel?downloadKey='+downloadKey;
}

var params = "";
function checkSearchValue(params){
	if(params == '搜索联系人'){
		params='';
	}
	return params;
}

//导出
function exportExcel(groupId){
	params =  $('#params').val();
	params = checkSearchValue(params);
	$("#exportForm").remove();
	var formHtml = "<form id='exportForm' action='"+WEB_SERVER_PATH+"/sys/client/contact/exportContact' method='post'>"
		formHtml +="<input type='hidden' value='"+groupId+"' name='groupId'/>";
		formHtml +="<input type='hidden' value='"+params+"' name='params'/>";
		formHtml += "</form>";
	$("body").append(formHtml);
	$("#exportForm").submit();
}
//联系人搜索
function searchInfo(){
	var groupType = $(".card .li-on").attr("groupType");
	var url = WEB_SERVER_PATH  + '/sys/client/contact/ajax/listContact?groupId=0&groupType='+groupType;
	$.ajax({
		url: url,
		type:'post',
		dataType:'json',
		data : {
			mobiles : $("#params").val()
		},
		success:function(data){	
			if(data.success){
				if(groupType ==0){
					$("#addGroupTree ul li div.editdiv").removeClass("xing");
					$("#addGroupTree ul li div.editdiv").eq(0).addClass("xing");
					window['emTableConfig'].tableConfig.rowItems[0].isShow=true;
					window['emTableConfig'].ajaxConfig.url = url;
					emrefulsh("emTableConfig");
					$(".conStrip-box").html("");
					var isEditHtml ='';
					isEditHtml+='<div class="conStrip-title">';
					isEditHtml+='<div class="fl" gId="0"  gtype="0" id="gDiv">';
					isEditHtml+='<i class="iCon lgtBlue-folder"></i>';
					isEditHtml+='<input type="text"  class="disabledInput" disabled="disabled" value="全部" />';
					isEditHtml+='</div>';
					isEditHtml+='</div>';
					isEditHtml+='<div class="conStrip-nav emtable_search_form">';
					var number = $("#outerId_page_div font").text();
					isEditHtml+='<div class="emay-search"><label>成员(<span id="num">'+number+'</span>)</label></div>';
					isEditHtml+='</div>';
					$(".conStrip-box").append(isEditHtml);
				}else{
					$("#addGroupTreeShare ul li div.editdiv").removeClass("xing");
					$("#addGroupTreeShare ul li div.editdiv").eq(0).addClass("xing");
					window['emTableConfigTwo'].tableConfig.rowItems[0].isShow=true;
					window['emTableConfigTwo'].ajaxConfig.url = url;
					emrefulsh("emTableConfigTwo");
					$(".conStrip-box-share").html("");
					var isEditHtml ='';
					isEditHtml+='<div class="conStrip-title">';
					isEditHtml+='<div class="fl" gId="0"  gtype="1" id="gDiv">';
					isEditHtml+='<i class="iCon lgtBlue-folder"></i>';
					isEditHtml+='<input type="text"  class="disabledInput" disabled="disabled" value="全部">';
					isEditHtml+='</div>';
					isEditHtml+='</div>';
					isEditHtml+='<div class="conStrip-nav emtable_search_form">';
					var number = $("#outerIdShare_page_div font").text();
					isEditHtml+='<div class="emay-search"><label>成员(<span id="numShare">'+number+'</span>)</label></div>';
					isEditHtml+='</div>';
					$(".conStrip-box-share").append(isEditHtml);
				}
			}else{
				$.fn.tipAlert(data.message,1,2);
			}
		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
}