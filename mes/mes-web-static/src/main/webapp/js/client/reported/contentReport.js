//获取光标位置
(function ($) { 
    $.fn.extend({ 
        insertAtCaret: function (myValue) { 
            var $t = $(this)[0]; 
            if (document.selection) { 
                this.focus(); 
                sel = document.selection.createRange(); 
                sel.text = myValue; 
                this.focus(); 
            } else 
                if ($t.selectionStart || $t.selectionStart == '0'){ 
                    var startPos = $t.selectionStart; 
                    var endPos = $t.selectionEnd; 
                    var scrollTop = $t.scrollTop; 
                    $t.value = $t.value.substring(0, startPos) + myValue + $t.value.substring(endPos,$t.value.length); 
                    this.focus(); 
                    $t.selectionStart = startPos + myValue.length;
                    $t.selectionEnd = startPos + myValue.length; 
                    $t.scrollTop = scrollTop; 
                } else { 
                    this.value += myValue; 
                    this.focus(); 
                } 
        } 
    }) 
})(jQuery); 
$(function(){
	$('#sendContent').keyup(function(){
		sendContent();
	});
	$('#sendContent').keydown(function(){
		sendContent();
	});
	$('#sendContent').blur(function(){
		sendContent();
	});
});

//清空--内容
function emptyContent(obj){
	$(obj).parent().find("textarea").val("");
	$('#simulation,#simulation-bottom,#warning').html('');
}

//添加变量
function addvar(){
	var html = "";
	html = `
		<div class="sms-item">
			<label class="item-label" style="width:130px;">请输入变量名称：</label>
			<input id="varname" name="varname"  />
		</div>
	`
	$.fn.tipOpen({
		title : "添加变量",
		width : '300',
		height:'',
		concent :html,
		btn : [{
			label : '确定',
			onClickFunction : 'addvarTrue()'
		}],
	});
}
//添加变量确定
function addvarTrue() {
	var varname = $("#varname").val();
	$('#sendContent').insertAtCaret('${'+varname+'}');
	sendContent();
	$('.tipBorder').remove();
	$('.layer').remove();
}

// 内容同步右侧手机
function sendContent() {
	var sizeleng;
	var content=$("#sendContent").val();
	sizeleng = content.length;
	if(content!=''){
		$("#simulation").html(content.replace(/</g,'&lt;'));
	if(content.length>70){
		$.fn.tipAlert('内容超过70字不能发送',1.5,0);
		$("#warning").text('内容超过70字不能发送');
	}else{
		$("#warning").text('');
	}
	$("#simulation-bottom").html(sizeleng+'/'+Math.ceil(sizeleng/67)+'　　'+'字/条');
	}else{
		$("#simulation").html('');
		$("#simulation-bottom").html('');
		$("#warning").text('');
	}		
}

// 选择服务号带出信息
function changeServiceCode(obj) {
	$('.bringspan').html('')
	var html = '';
	var str = '';
	$(obj).find('option').each(function(i,ele){
		if ($(ele).prop('selected') == true && $(ele).val() != '') {
			if ($(ele).data('savetype') == 1) {
				str = '可以保存'
			} else if ($(ele).data('savetype') == 2) {
				str = '不可保存'
			}
			html = '<span>'+$(ele).data('businesstype')+'</span><span>'+str+'</span><span>'+$(ele).data('contenttype')+'</span>'
		}
	})
	$('.bringspan').html(html)
}

//报备模板
function reportTemplate() {
	var content = $('#sendContent').val();
	content = content.replace(/"/g, "@EMAY34PAGE@");
	content = content.replace(/'/g, "@EMAY39PAGE@");
	var templateName = $("#templateName").val();
	var appId = '';
	$('#fmsserviceCode').find('option').each(function(i,ele){
		if ($(ele).prop('selected') == true && $(ele).val() != '') {
			appId = $(ele).data('appid');
		}
	})
	if (templateName == '') {
		$.fn.tipAlert('模板名称不能为空',1.5,0);
		return false;
	}
	if (content == '') {
		$.fn.tipAlert('报备内容不能为空',1.5,0);
		return false;
	}
	if($("#warning").text() != ''){
		$.fn.tipAlert('报备内容不能超过70字',1.5,2);
		return false;
	}
	if (appId == '') {
		$.fn.tipAlert('请选择服务号',1.5,0);
		return false;
	}
	
	$.fn.tipLodding();
    $.ajax({
		url : WEB_SERVER_PATH + '/fms/client/reportedcontent/reportTemplate', 
		type : "post",
		dataType : "json",
		data : {
			templateName:templateName,
			content : content,
			appId : appId
		},
		success:function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('报备成功',1,1);
				window.location.href=WEB_SERVER_PATH + '/fms/client/reportedquery';
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message, 1.5, 0);
			}
		},
		error:function() {
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
}
