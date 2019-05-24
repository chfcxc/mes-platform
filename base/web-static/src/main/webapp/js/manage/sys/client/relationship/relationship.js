var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true, 
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [{
			label : '客户名称/编号',
			id : 'clientUserNameAndCode',
			type : 'input'
		},{
			label : '销售',
			id : 'saleMan',
			type : 'input'
		}],
		buttonItems : [ {
			isShow:hasAuth("OPER_SYS_CLIENT_RELATIONSHIP_ADD")==true,
			label : '调整选中',
			id : 'adjustSelectedBtn',
			onClickFunction : 'adjustSelected()'
		},{
			isShow:hasAuth("OPER_SYS_CLIENT_RELATIONSHIP_ADD")==true,
			label : '调整所有',
			id : 'adjustSelectedAllBtn',
			onClickFunction : 'adjustSelectedAll()'
		}],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/client/relationship/selectRelationship',
		method : 'POST',
		data : {
			clientUserNameAndCode :'#clientUserNameAndCode', // 客户名称/编号
			saleMan : '#saleMan' //销售
		},
		startType : "startNum",
		startParams : "start",
		limitParams : "limit",
		defaultLimit : 20,
		supportLimit : [ 20, 50, 100 ],
		result : {
			dataArray : "result.list",
			totalCount : "result.totalCount",
			totalPageNum : "result.totalPage",
			currentPageNum : "result.currentPageNum"
		}
	},
	tableConfig : {
		isNeedIndexRow : true,
		checkBoxConfig :{
			id : 'checkAll',
		    subCalss : 'check',
		    sValue : "@{id}"
		},
		rowItems : [{
					align:'left',
					title : "客户名称",
					width : "25%",
					context : "@{systemEnterpriseName}<input id='systemEnterpriseId@{id}' type='hidden' value='@{systemEnterpriseId}' />" 
				},{
					title : "客户编号",
					width : "10%",
					context : "@{systemEnterpriseCode}"
				},{
					title : "销售用户名",
					width : "20%",
					context : "@{systemUsername}"
				},{
					title : "销售姓名",
					width : "20%",
					context : "@{systemRealname}"
				},{
					title : "创建时间",
					width : "15%",
					context : "@{createTime}"
				}]
	}
}
emTable('emTableConfig');
$("#saleMan").attr("placeholder","姓名/用户名");
function getSelectTableRowValue(tableId){
	var arr = new Array();
	$("#"+tableId+" input[type='checkbox']:checked").each(function(index,obj){
		var id = $(obj).attr("svalue");
		if(id != void 0){
			arr.push(id);
		}
	});
	return arr;
}
function searchItem(array,searchValue){
	var result = true;
	for(var i=0;i < array.length; i ++){
		if(array[i] == searchValue){
			result =  false;
			break;
		}
	}
	return result;
}
//调整选中
var html = $('#warp').html();
$('#warp').remove();
function adjustSelected(){
	var idArrays=new Array();
	idArrays =  getSelectTableRowValue("outerId_table_table");	
	if(idArrays.length==0){
		$.fn.tipAlert("未选中记录.",1.5,0);
		return false;
	}
	 $.fn.tipOpen({
			title : '提示',
			width : '420',
			btn : [],
			cancel:false,
			concent : html
		});
	$("#isAll").val(1);
	$("#tips").html("提示：将选中的客户调整到其他销售名下");
}
function adjustBtn(){
	 var $formCheckOut = $('#addForm');
		var validateor=$formCheckOut.validate({
	    	rules:{
	    		username:{  //销售
					required:true
	    		}
	    	},
			messages:{
				username:{
					required:"指定销售不可为空"
				}
			},
			submitHandler: function() {
				var isAll='';
				if($("#isAll").val()=='1'){
					var idArrays=new Array();
					idArrays =  getSelectTableRowValue("outerId_table_table");	
					var enterpriseIdsArr = new Array();
					for(var i=0;i<idArrays.length;i++){
						var id = $("#systemEnterpriseId"+idArrays[i]).val();
						if(searchItem(enterpriseIdsArr,id)){
							enterpriseIdsArr.push(id); 
						}
					}
					var enterpriseIds=enterpriseIdsArr.join(",");
				}else if($("#isAll").val()=='0'){
					 isAll =$("#isAll").val(); 
				}
				var username = $("#usernameVal").val();
				var clientUserNameAndCode = $("#clientUserNameAndCode").val();
				var saleMan = $("#saleMan").val();
				$.fn.tipLodding();
					$.ajax({
						url : WEB_SERVER_PATH + '/sys/client/relationship/ajax/relationship',
						type : 'post',
						dataType : 'json',
						data : {
							isAll : isAll,
							enterpriseIds : enterpriseIds, //客户id
							saleMan : saleMan, // 销售
							clientUserNameAndCode : clientUserNameAndCode, // 客户名称/编号
							username : username //销售
						},
						success : function(data) {
							if (data.success) {
								$.fn.tipLoddingEnd(true);
								$.fn.tipAlert('调整成功',1,1);
								var limit=$('.limitSelect').val();
		        				emrefulsh("emTableConfig", 0, limit);
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
//人员列表
var emTableConfigtwo;
function chooseCustomerName(){
	var addhtml="";
	addhtml='<div id="innerDiv"></div>';
	$.fn.tipOpen({
		title : "选择销售",
		width : '660',
		height:'',
		concent :addhtml,
		secondary :true,
		btn : [{
			label : '确定',
			onClickFunction : 'chooseBtn()'
		}],
	});
	emTableConfigtwo={
			outerDivId : "innerDiv",
			pagesShow : true, 
			totalNumbersShow:true,
			searchConfig : {
				searchItems : [ {
					label : '销售',
					id : 'realname',
					type : 'input'
				}],

				searchButton : true,
				resetButton : false
			},
			ajaxConfig : {
				url : WEB_SERVER_PATH + '/sys/client/relationship/ajax/list',
				method : 'POST',
				data : {
					realname :'#realname'
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
							context : "<input type='radio' name='myRadio' class='check' svalue='@{id}'/>"
						},{
							title : "用户名",
							width : "30%",
							context : "@{username}"
						},{
							title : "姓名",
							width : "30%",
							context : "@{realname}"
						},{
							align:'left',
							title : "部门",
							width : "35%",
							context : "@{department}"
						}]
			}
		}
	$(".tipBorder").css("top","50px");
	emTable('emTableConfigtwo');
	$("#realname").attr("placeholder","姓名/用户名");
}
function chooseBtn(){
	var len=$("input[name='myRadio']:checked").length;
	if(len==0){
		 $.fn.tipAlert("请选择销售!",1.5,0);
		 return false;
	}
	$('.check:checked').each(function(inx,ele){
		    var id=$(ele).attr("svalue");
			var username = $(ele).parent().next().next().text();
			$("#usernameVal").val(id);
			 $("#username").val(username);
			 
	});
	 $( ".tipBorder:eq(1)").remove();
     $( ".layer:eq(1)").remove();
     len=$( ".tipBorder").length;
	  if(len>=2){
	        $( ".tipBorder:eq(1)").hide();
	        $( ".layer:eq(1)").hide();
	 }
}
function addCancel(){
	$.fn.tipShut();
}
//调整所有 0-所有
function adjustSelectedAll(){
	 $.fn.tipOpen({
			title : '提示',
			width : '410',
			btn : [],
			cancel:false,
			concent : html
		});
	 $("#isAll").val(0);
	 $("#tips").html("提示：将满足条件的所有客户调整到其他销售名下");
}

