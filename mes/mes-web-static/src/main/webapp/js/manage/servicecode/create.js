function chooseCustomerName(event){
	event.preventDefault()
	$("#clientName-error").remove();
    var tipBorderLen=$(".tipBorder").length;
    if(tipBorderLen==0){
    	var html="";
    	html='<div id="innerDiv"></div>';
		$.fn.tipOpen({
			title : "选择客户",
			width : '660',
			height:'',
			concent :html,
			btn : [{
				label : '保存',
				onClickFunction : 'turnTrue()'
			}],
		});
		emTableConfigtwo={
				outerDivId : "innerDiv",
				pagesShow : true, 
				totalNumbersShow:true,
				searchConfig : {
					searchItems : [ {
						label : '',
						id : '',
						type : 'input'
					}],
					searchButton : true,
					resetButton : false
				},
				ajaxConfig : {
					url : WEB_SERVER_PATH + '/fms/servicecode/manage/listclient',
					method : 'POST',
					data : {
						clientName :'#clientName'
					},
					startType : "startNum",
					startParams : "start",
					limitParams : "limit",
					defaultLimit : 10,
					supportLimit : [ 10, 20, 50 ],
					result : {
						dataArray : "result.list",
						totalCount : "result.totalCount",
						totalPageNum : "result.totalPage",
						currentPageNum : "result.currentPageNum"
					}
				},
				tableConfig : {
					isNeedIndexRow : false,
					rowItems : [{
								title : "",
								width : "5%",
								context : "<input type='radio' name='myRadio' class='check' svalue='@{clientId}' ctype='@{type}'/>"
							},{
								title : "客户编号",
								width : "50%",
								context : "@{clientNumber}"
							},{
								title : "客户名称",
								width : "45%",
								context : "<div class='leftdiv'>@{clientName}</div>"
							}]
				}
			}
		$(".tipBorder").css("top","50px");
		emTable('emTableConfigtwo');
		$("#innerDiv_search_form").append('<div class="emay-search"><label></label><input type="text" id="clientName"  placeholder="搜索客户编号或者客户名称"/></div>');
    }
}
function turnTrue(){
	var len=$("input[name='myRadio']:checked").length;
	if(len==0){
		 $.fn.tipAlert("请选择客户!",1.5,0);
		 return false;
	}
	$('.check:checked').each(function(inx,ele){
		    var id=$(ele).attr("svalue");
			var custom = $(ele).parent().next().next().text();
			var customname = $(ele).parent().next().text();
			 $("#clientN").val(custom);
			 $("#clientId").val(id);
			 $("#clientNumber").val(customname);
			 $('.layer,.tipBorder').remove();
		});
	
}
//新增确定事件
function addTure(event){
	event.preventDefault();
	var serviceCode=$("#serviceCode").val();
	var clientId=$("#clientId").val();
	var clientName=$("#clientN").val();
	var remark=$("#remark").val();
	var businessType = $("#businessType").val();
	var saveType = $("#saveType").val();
	var businessTypeId = $("#businessTypeId").val();
	if(serviceCode.length>20){
		if($(".error1").length==0){
			var html='<label id="clientName-error" class="error1 error" for="clientName">字符长度不可大于20</label>';
			$("#serviceCode").after(html);	
		}
		return false;
	}else{
		$(".error1").remove()
	}
	if(clientName==""){
		if($(".error2").length==0){
			var html='<label id="clientName-error" class="error2 error" for="clientName">客户名称不可为空</label>';
			$("#clientN").after(html);	
		}
		return false;
	}else{
		$(".error2").remove()
	}
	if (businessType == '') {
		$.fn.tipAlert('请选择业务类型',1.5,0);
		return false;
	}
	if (saveType == '') {
		$.fn.tipAlert('请选择保存类型',1.5,0);
		return false;
	}
	if (businessTypeId == '') {
		$.fn.tipAlert('请选择内容类别',1.5,0);
		return false;
	}
	$.fn.tipLodding();
	$.ajax({
		url : WEB_SERVER_PATH + "/fms/servicecode/manage/ajax/save",
		type : 'post',
		dataType : 'json',
		data : {
			serviceCode : serviceCode,
			enterpriseId:clientId,
			remark:remark,
			businessTypeId: businessTypeId
		},
		success : function(data) {
			if (data.success) {
				console.log(data)
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert("添加成功",1.5,0);
				goPage("/fms/servicecode/manage");
				var limit=$('.limitSelect').val();
				emrefulsh("emTableConfig", 0, limit);
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
function removetip(){
	$("#plat-error").remove();
}
//function addCancel(){
//	goPage("/fms/servicecode/list");
//}
$(document).ready(function(){
	$("#selectUser").click(chooseCustomerName)
	$("#updateBtn").click(addTure)
})
