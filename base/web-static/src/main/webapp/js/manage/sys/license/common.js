//时间
$(function(){
	$("#beginTime").addClass("Wdate");
	$("#endTime").addClass("Wdate");
	$("#beginTime").attr("onfocus","WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\',startDate:\'%y-%M-%d 00:00:00\',maxDate:'#F{getInterceptEndTime()}'})");
	$("#endTime").attr("onfocus","WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\',startDate: \'%y-%M-%d 23:59:59\',minDate:'#F{getInterceptBeginTime()}'})");
});
function getInterceptEndTime(){
	return $("#endTime").val();
}
function getInterceptBeginTime(){
	return $("#beginTime").val();
}
//选择客户
var emTableConfigtwo;
function chooseCustomerName(){
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
				onClickFunction : 'saveClient()'
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
					url : WEB_SERVER_PATH + '/sys/license/recode/listClient',
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
								context : "<input type='radio' name='myRadio' class='check' svalue='@{clientId}' clinkman='@{linkman}' cmobile='@{mobile}'/>"
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
function saveClient(){
	var len=$("input[name='myRadio']:checked").length;
	if(len==0){
		 $.fn.tipAlert("请选择客户",1.5,0);
		 return false;
	}
	$('.check:checked').each(function(inx,ele){
		    var id=$(ele).attr("svalue");//客户id
			var custom = $(ele).parent().next().next().text(); //客户名称
			var linkman = $(ele).attr("clinkman"); //客户联系人
			var mobile = $(ele).attr("cmobile"); //联系方式
			$("#systemEnterpriseId").val(id); 
			 $("#agentAbbr").html( '<input type="hidden" id="'+id+'"  /><input id="agentAbbr"  class="item-text" name="agentAbbr" type="text" disabled="disabled"/>').val(custom);
			 $("#linkman").val(linkman);
			 $("#mobile").val(mobile);
			 $('.layer,.tipBorder').remove();
		});
}