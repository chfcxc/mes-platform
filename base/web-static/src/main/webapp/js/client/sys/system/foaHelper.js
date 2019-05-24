$(function(){
	tabContent(this,'1');
});
function tabContent(obj,types){
	$(obj).addClass("li-on").siblings().removeClass("li-on");
	$(obj).parent().siblings('.card-cen').hide().eq($(obj).index()).show();
	$("#typeVal").val(types);
	type_now = types;
	smsList(types);
}
//function tabContent(obj,types){
//	$(obj).addClass("li-on").siblings().removeClass("li-on");
//	$(obj).parent().siblings('.card-cen').hide().eq($(obj).index()).show();
//	$("#typeVal").val(types);
//	type_now = types;
//	if(type_now=='1'){
//		smsList();
//	}else if(type_now=='2'){
//		floatList();
//	}else{
//		mmsList();
//	}
//}
function smsList(types){
	var businessType=types;
	if(businessType==undefined){
		businessType=1;
	}
	$.ajax({
		url : WEB_SERVER_PATH + '/helper/ajax/listFqa/client',
		type : 'post',
		dataType : 'json',
		data : {
			businessType:businessType
		},
		success : function(data) {
			if (data.success) {
				var list=data.result;
				var html="";
				for(var i in list){
					var descProblem=list[i].descProblem; //问题
					var reply=list[i].reply; //回答
					var createTime=list[i].createTime; //时间
					html+='<div class="topBox">';
					html+='<div class="triangle fl"></div>';
					html+='<div class="questionText fl">'+descProblem+'</div>';
					html+='<div class="pusTime fr">'+createTime+'</div>';
					html+='<div class="clear"></div>';
					html+='</div>';
					html+='<div class="bottomBox">';
					html+='<div class="replayText">'+reply+'</div>';
					html+='</div>';
					html+='<div class="clear"></div>';
					html+='<div class="dashDiv"></div>';
				}
				if(businessType==1){
					$("#smsDiv").html(html);
				}else if(businessType==3){
					$("#floatDiv").html(html);
				}else if(businessType==4){
					$("#imsDiv").html(html);
				}else if(businessType==5){
					$("#voiceDiv").html(html);
				}else if(businessType==2){
					$("#mmsDiv").html(html);
				}else if(businessType==6){
					$("#jryxDiv").html(html);
				}else if(businessType==7) {
					$("#stDiv").html(html);
				}
			} else {
				$.fn.tipAlert(data.message,1.5,0);
			}
			resize();
		},
		error : function() {
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
}
function resize(){
	if($("body").height()>$(window).height()-110){
		$("#footer").css("position","static");
	}else{
		$("#footer").css("position","fixed");
    }
 }
