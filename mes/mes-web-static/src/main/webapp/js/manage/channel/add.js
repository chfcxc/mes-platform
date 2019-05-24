function cancelText(){
	$.fn.tipShut();
}

function showreportType(obj) {
	if ($(obj).val() == 1) {
		$("#reportType").parent().show();
	} else {
		$("#reportType").parent().hide();
	}
}

//新增通道
function sanAddTure(){
	var $formCheckOut = $('#tempForm');
	var validateor=$formCheckOut.validate({
    	rules:{
    		channelName:{ 
				required:true
			},
			channelNumber:{ 
				required:true
			},
			sendSpeed:{//发送速度
				required:true,
			},
			cmccLimit:{
				required:true,
			},
			cuccLimit:{
				required:true,
			},
			ctccLimit:{
				required:true,
			}
		},
		messages:{
			channelName:{
				required:"请输入通道名"
			},
			channelNumber:{
				required:"请输入通道号"
			},
			sendSpeed:{//发送速度
				required:"请输入发送速度"
			},
			cmccLimit:{
				required:"请输入移动通道字数限制"
			},
			cuccLimit:{
				required:"请输入联通通道字数限制"
			},
			ctccLimit:{
				required:"请输入电信通道字数限制"
			}
		},
    	 submitHandler: function() {
	        var pobj = {};
	        var providers=''; 
	        var str='';
	        var messagestr = "";
        	var templateType = "";
        	$('input[type=text]').each(function(i,ele){
        		pobj[$(ele).attr('name')] = $(ele).val()
        	})
        	pobj["businessTypeId"]=$('#businessTypeId').val();
        	pobj["isNeedReport"]=$('#isNeedReport').val();
        	pobj["reportType"]=$('#reportType').val();
        	
        	$("#operator").find('input').each(function(i,obj){
        		if($(obj).prop('checked') == true){
        			str += $(obj).val() +','
        		}
        	})
        	$("#templateType").find('input').each(function(i,obj){
        		if($(obj).prop('checked') == true){
        			messagestr += $(obj).val() +','
        		}
        	})
        	if ($('#businessType').val() == '') {
        		$.fn.tipAlert('请选择业务类型', 1.5, 0);
        		return false;
        	}
        	if ($('#saveType').val() == '') {
        		$.fn.tipAlert('请选择保存类型', 1.5, 0);
        		return false;
        	}
        	if ($('#businessTypeId').val() == '') {
        		$.fn.tipAlert('内容类别不能为空', 1.5, 0);
        		return false;
        	}
        	if (str == ''){
        		$.fn.tipAlert('请选择允许发送的运营商', 1.5, 0);
        		return false;
        	}
        	if (messagestr == ''){
        		$.fn.tipAlert('请选择允许支持的信息类型', 1.5, 0);
        		return false;
        	}
        	if($('#cmccLimit').val() > 70 || $('#cuccLimit').val() > 70 || $('#ctccLimit').val() > 70) {
        		$.fn.tipAlert('通道字数限制不能超过70', 1.5, 0);
        		return false;
        	}
	        providers=str.substring(0,str.length-1);
	        templateType=messagestr.substring(0,messagestr.length-1);
	        pobj["providers"]=providers;
	        pobj["templateType"]=templateType;
	        var channelParams=''; // 第三方通道参数 a#b#c,
        	var flag=false;
        	$("#fmsChannel .item").each(function(index,element){
        		if($(element).find("input.inpVal").val()==''){
        			$.fn.tipAlert("自定义属性值不可为空", 1.5, 0);
        			flag=true;
        			return false;
        		}else{
        			channelParams+=''+$(element).find("label.item-label .propertyName").text()+'#'+$(element).find("input.item-text").attr("id")+'#'+$(element).find("input.item-text").val()+',';
        		}
        	});
        	if(flag){
        		return false;
        	}
	        $.fn.tipLodding();
             $.ajax({
         		url : WEB_SERVER_PATH + '/fms/channel/manage/ajax/addChannel', 
         		type : "post",
         		dataType : "json",
         		data : {
         			params:JSON.stringify(pobj),
         			channelParams : channelParams
         		},
         		success:function(data) {
         			if (data.success) {
         				console.log(data)
     					$.fn.tipLoddingEnd(true);
     					$.fn.tipAlert('第三方通道配置添加成功',1,1);
     					goPage("/fms/channel/manage");
     				} else {
     					$.fn.tipLoddingEnd(false);
     					$.fn.tipAlert(data.message, 1.5, 0);
     					validateor.resetForm();
     				}
         		},
         		error:function() {
         			$.fn.tipLoddingEnd(false);
        			$.fn.tipAlert('系统异常',1.5,2);
         		}
         	});
        	 
        }
    });

}
//添加自定义属性
var addfiledArr=[]; //存储字段标识的id
var addfilename=[];
function addFieldBtn(){
    var $addFormFiledOUt = $('#addFormFiled');
    var validateor=$addFormFiledOUt.validate({
   	rules:{     
   		filedName:{ 
				required:true,
				maxlength:30
			},
			fieldIdent:{ 
				required:true,
				maxlength:30
			},
			fieldVal:{ 
				required:true
			}
		},
		messages:{
			filedName:{
				required:"请输入字段名",
				maxlength :$.validator.format("字段名长度不可超过30")
			},
			fieldIdent:{
				required:"请输入字段标识",
				maxlength :$.validator.format("字段标识长度不可超过30")
			},
			fieldVal:{ 
				required:"请输入字段值"
			}
		},
       submitHandler: function() {
    	   var filedName=$("#filedName").val(),
    	   		fieldIdent=$("#fieldIdent").val(),
    	   		fieldVal=$("#fieldVal").val();
	    	if($.inArray(filedName,addfilename)!==-1){
    			$.fn.tipAlert("字段名已存在", 1.5, 0);
    			return false;
    		}
	    	if($.inArray(fieldIdent,addfiledArr)!==-1){
    			$.fn.tipAlert("字段标识已存在", 1.5, 0);
    			return false;
    		}else{
    			$.fn.tipShut();
    	       	$("#thirdChannel").append("<div class='item' ><label class='item-label'><span class='xing'>*</span>"+
    	       			"<span class='propertyName'>"+filedName+"</span></label>" +
    	       			"<input type='text' id="+fieldIdent+"  class='item-text inpVal'  value="+fieldVal+"  name='fieldVal' maxLength='512'  />"+
    	       			"<div class='remave' onclick='removeuser(this)'></div></div>");
    	}
	    		addfilename.push(filedName);
    	       	addfiledArr.push(fieldIdent);
       }
		
   });
	 }
function removeuser(obj){
	$(obj).parent().remove();
	var fileName=$(obj).parent().find(".propertyName").html();
	var ident=$(obj).parent().find("input[type='text']").attr("id");
	addfiledArr.splice($.inArray(ident,addfiledArr),1); 
	addfilename.splice($.inArray(fileName,addfilename),1);
}
//第三方添加字段
var html='';
html+='<form id="addFormFiled">';
html+='<div class="item">';
html+='<label class="item-label"><span class="xing">*</span>字段名</label><input type="text" id="filedName" name="filedName" class="item-text"  />';
html+='</div>';
html+='<div class="item">';
html+=' <label class="item-label"><span class="xing">*</span>字段标识</label>';
html+='<input type="text" id="fieldIdent" name="fieldIdent" class="item-text"  />';
html+='</div>';
html+='<div class="item">';
html+='<label class="item-label"><span class="xing">*</span>值</label>';
html+='<input type="text" id="fieldVal" name="fieldVal" class="item-text"  maxLength="512"  />';
html+='</div>';
html+='<div class="tipFoot">';
html+='<button id="addBtnAttr"  class="tipBtn" type="submit" onclick="addFieldBtn()" >添加字段</button>';
html+='<button id="cancelBtnAttr"  class="tipBtn" type="button" onclick="cancelText()">取消</button>';
html+='</div></form>';
/*var addHtml = $('#addBox').html();
$("#addBox").remove();*/
function addField(){
	 $.fn.tipOpen({
			title : "自定义属性",
			width : '400',
			btn : [],
			cancel:false,
			concent : html
		})
}