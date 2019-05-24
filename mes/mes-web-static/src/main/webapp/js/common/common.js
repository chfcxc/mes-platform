$(function(){
	$("#saveType").find('option').each(function(i,ele){
		if($(ele).val() != "") {
			$(ele).hide();
		}
	})
})
//业务类型，保存类型，内容的选择
function chooseType() {
	$("#businessTypeId").find('option').each(function(i,ele){
		$(ele).show();
	});
	$("#saveType").find('option').each(function(i,ele){
		$(ele).show();
	})
	$("#businessTypeId").val('')
	var businessType = $("#businessType").val();
	var saveType = $("#saveType").val();
	$("#businessTypeId").find('option').each(function(i,ele){
		if ($(ele).data('parentname') == businessType && $(ele).data('savetype') == saveType) {
			$(ele).show();
		}else {
			$(ele).hide();
		}
	});
}

// 内容类别
function changecontent(obj){
//	var businessType = $("#businessType").val();
//	var saveType = $("#saveType").val();
//	if (businessType == '') {
//		$.fn.tipAlert('请先选择业务类型',1.5,0);
//		return false;
//	}else if (saveType == '') {
//		$.fn.tipAlert('请先选择保存类型',1.5,0);
//		return false;
//	}
}