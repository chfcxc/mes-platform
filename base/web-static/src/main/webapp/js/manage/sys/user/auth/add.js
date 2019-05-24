
//新增角色
$(function(){
	var html='<div class="clear role_div" id="role_div">';
	html +='<form method="post" id="addRoleForm" class="">';
	html +='<dl class="alert-cen">';
	html +='<dt class="item"><label class="item-label">所属系统 :</label> &nbsp;&nbsp;<span  >'+getSystemName(SYSTEM_NOW)+'</span></dt>';
	html +='<dt class="item itemNew"><label class="item-label"><span class="xing">*</span>角色名称 :</label><input type="text" id="addroleName" value="" class="item-text" name="addroleName" maxlength="20"  /></dt><div class="clear"></div>';
	html +='<dt class="item" style="margin-top:15px;"><label class="item-label">角色描述 :</label><textarea maxlength="50" name="remark" id="remark" ></textarea></dt>';
	html +='<dd><div class="b_gradient role_auth">角色权限</div></dd>';
	html +='<dd class="authority"></dd>';
	html += '</dl>';
	html+='<div class="tipFoot">';
    html+='<button type="submit" class="tipBtn" id="saveBtn" onclick="ajaxAdd()">保 存</button>';
    html+='<button onclick="addCancel()" type="button" class="tipBtn tip-cancel">取 消</button></div>';
	html +='</form></div>';
	$("#addRolebox").html(html);
	typeChange();
});

function typeChange(){
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/auth/role/ajax/getTree",
		type:'post',
		dataType:'json',
		data : {
			roleType : SYSTEM_NOW
		},
		success:function(data){	
			$(".authority").html("");
			var httt = "";
			var auth = data.result.allauth;
			if(auth == null){
				return;
			}
			var ngvs = auth.ngvs;
			for(var i in ngvs){
				var ngv = ngvs[i]; 
				var ngvId = ngv.id;
				var ngvName = ngv.resourceName; 
				httt +='<div class="role-title role-on light_blue_bg" onclick="role(this)"><i></i>'+ngvName+'<input type="hidden" id="'+ngvId+'" /></div>';
				httt +='<div class="role-cen" style="display:block">';
				var pagesNew=ngv.resource;	
				for(var j in pagesNew){
					var page = pagesNew[j];
					var pageIdSecond = page.id;
					var pageNameSecond = page.resourceName;
					
					httt += "<div class='roleAdd'>";
					httt += "<div class='roleNav'><label><input type='checkbox' onclick='roleNav(this)' class='checkboxdo' id='"+pageIdSecond +"' stype='page' />"+pageNameSecond+"</label></div>" ;
					var pages=page.resource;
					for(var k in pages){
						var lastPages=pages[k];
						var resourceName=pages[k].resourceName;
						var resourceId=pages[k].id;
						httt+='<div class="roleNavFind clear">';
						httt+='<label><input type="checkbox" class="checkboxdo" onclick="checkedParent(this,'+resourceId+')"  stype="page" id="'+resourceId+'" stype="page">'+resourceName+'</label><div class="threebox">';
						var aa=lastPages.resource; 
						for(var m in aa){
							var operName=aa[m].resourceName; 
							var operId=aa[m].id; 
							httt += '<div class="roleNavFind-cen">';
							httt +='<label><input type="checkbox"  parentid="'+resourceId+'"  id="'+operId+'" stype="page" class="checkboxdo" onclick="checkedPage(this,'+resourceId+')"/>'+operName+'</label>';
							httt +='</div>';
						}
						httt+='</div></div>';						
					}
					httt +=	"</div>";
				}
				httt += "</div>";
			}
			$(".authority").html(httt);
		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}


//新增确定
function ajaxAdd(){
	if($("#remark").val()==''|| $("#remark").val()==undefined || $("#remark").val()==null){
		$("#remark").val("");
	}
	var $formCheckOut = $('#addRoleForm');
  $formCheckOut.validate({
  	rules:{     
  		addroleName:{ 
  			required:true,
				allroleName:true
			},
			remark:{
				maxlength:50
			}
  	},
		messages:{
			addroleName:{
				required:"角色名称不可为空"
			},
			remark:{
				rangelength:$.validator.format("角色描述不可超过50个字符")
			},
		},
		submitHandler: function() {
			var name = $('#addroleName').val();
			var remark=$("#remark").val();
			var ps = '';
			var os = '';
			$('.authority').find('input[type=checkbox]:checked').each(function(index,element){
				var row = $(element).attr('id');
				var type = $(element).attr('stype');
				if(row.length!==3){
					if(type == "page"){
						ps = ps + row + ",";
					}else{
						os = os + row + ",";
					}
				}
			});
			if(!validate.required(ps)){
				$.fn.tipAlert("角色页面权限不允许为空",1.5,0);
				return false;
			}
			tmp = ps;
			$.fn.tipLodding();
      	$.ajax({
      		url:  WEB_SERVER_PATH + '/sys/auth/role/ajax/add?randomdata='+Date.parse(new Date()),
      		type : 'post',
      		dataType : 'json',
      		data : {
      			auths : tmp,
      			roleName : name,
      			remark : remark,
      			roleType : SYSTEM_NOW
      		},
      		success : function(data) {
      			if (data.success) {
      				$.fn.tipLoddingEnd(true);
      				$.fn.tipAlert('角色添加成功',1.5,1);
      				addCancel();
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
      }
  });
}
