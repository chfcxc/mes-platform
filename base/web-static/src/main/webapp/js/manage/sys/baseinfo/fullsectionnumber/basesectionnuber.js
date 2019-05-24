var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true,
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [ {
			isShow : true,
			label : '号码段',
			id : 'number',
			type : 'input'
		},{
			isShow : true,
			label : '运营商',
			id : 'companyCode',
			type : 'select',
			options : [ {
				context : '全部'
			}, {
				context : '中国移动',
				value : 'CMCC'
			}, {
				context : '中国联通',
				value : 'CUCC'
			}, {
				context : '中国电信',
				value : 'CTCC'
			}  ]
		}  ],
		buttonItems : [ {
			isShow : hasAuth("OPER_SYS_BASESECTION_ADD")==true,
			label : '新增',
			id : 'addBtn',
			onClickFunction : 'addNumber()'
		}],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/base/basesection/findlist',
		method : 'POST',
		data : {
			number : '#number',
			operatorCode : '#companyCode'
		},
		startType : "startNum",
		startParams : "start",
		limitParams : "limit", 
		defaultLimit : 20,
		supportLimit : [ 20, 50,100 ],
		result : {
			dataArray : "result.list",
			totalCount : "result.totalCount",
			totalPageNum : "result.totalPage",
			currentPageNum : "result.currentPageNum"
		}
	},
	tableConfig : {
		isNeedIndexRow : true,
		rowItems : [{
					isShow : true,
					title : "号码段",
					width : "20%",
					context : "@{number}"
				},{
					isShow : true,
					title : "运营商",
					width : "10%",
					context : "@{operatorCode}"
				},{
					isShow:hasAuths("OPER_SYS_BASESECTION_UPDATE","OPER_SYS_BASESECTION_DELETE")==true,
					title : "操作",
					width : "10%",
					selectors : [{
						isShow : hasAuth("OPER_SYS_BASESECTION_UPDATE")==true,
						term : "@{id}",
						select : [ {
							value : "@{id}",
							context : "<div class='btn_box rs' alt='修改' title='修改' onClick='updateInfo(@{id})'><i class='iCon grey-write'></i></div>"
						} ]
					},{
						isShow:hasAuth("OPER_SYS_BASESECTION_DELETE")==true,
						term : "@{id}",
						select : [ {
							value : "@{id}",
							context : "&nbsp;&nbsp;<div class='btn_box rs' alt='删除' title='删除' onClick='deleteInfo(@{id},@{number})'><i class='iCon grey-delete'></i></div>"
						} ]
					} ]
				}]
	}
}

emTable('emTableConfig');

//新增号码段
var addHtml = $('#add').html();
$('#add').remove();
function addNumber(){
	$.fn.tipOpen({
		title : "新增号段",
		width : '400',
		btn : [],
		concent : addHtml
	});
}
//号段新增
 function addTure(){
	var $formCheckOut = $('#baseForm');
	var validateor= $formCheckOut.validate({
    	rules:{     
    		numberInput:{ 
				required:true,
				basesectionnumber:true
			},
			companyInput:{   
				required:true
			}
    	},
		messages:{
			numberInput:{
				required:"请输入以1开头的3-5位数字"
			},
			companyInput:{
				required:"请选择运营商",
				maxlength :$.validator.format("运营商不可为空")
			}
		},
        submitHandler: function() {
        	$.fn.tipLodding();
        	$.ajax({
        		url : WEB_SERVER_PATH + '/sys/base/basesection/add?randomdata='+Date.parse(new Date()),
        		type : 'post',
        		dataType : 'json',
        		data : {
        			number :$("#numberInput").val(),
        			operatorCode : $("#companyInput").val(),
        		},
        		success : function(data) {
        			if (data.success) {
        				$.fn.tipLoddingEnd(true);
        				$.fn.tipAlert('基础号段添加成功',1,1);
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
 
//回显号码段数据
 var updateHtml = $('#update').html();
 $('#update').remove();
 function updateInfo(id){
 	$.ajax({
 		url:  WEB_SERVER_PATH + '/sys/base/basesection/findId',
 		type:'post',
 		dataType:'json',
 		data : {
 			id : id
 		},
 		success:function(data){	
 			if (data.success) {
 				$.fn.tipOpen({
 					title : "修改号段",
 					width : '400',
 					btn : [],
 					concent : updateHtml
 				});
 				var dataResult = data.result;
 				$("#numberUpdate").val(dataResult.number);
 				$("#updateForm input[type=hidden]").val(dataResult.id);
 				$("#companyUpdate").val(dataResult.operatorCode);
 			} else {
 				$.fn.tipAlert(data.message,1.5,0);
 			}
 		}
 	});
 }

//修改号码段
 function updateTure(){
	//校验规则    
	var $formCheckOutUpdate = $('#updateForm');
	var validateor=$formCheckOutUpdate.validate({
    	rules:{     
    		numberUpdate:{ 
				required:true,
				basesectionnumber:true,
				maxlength:5
			},
			companyUpdate:{   
				required:true
			}
    	},
		messages:{
			numberUpdate:{
				required:"请输入以1开头的3-5位数字",
				maxlength :$.validator.format("号段数不可超过5")
			},
			companyUpdate:{
				required:"请选择运营商",
				maxlength :$.validator.format("运营商不可为空")
			}
		},
        submitHandler: function() {
        	 var id=$("#updateForm input[type=hidden]").val();
	       	 var number = $("#numberUpdate").val();
	       	 var companyCode = $("#companyUpdate").val();
    		$.fn.tipLodding();
        	$.ajax({
        		url:  WEB_SERVER_PATH + '/sys/base/basesection/update?randomdata='+Date.parse(new Date()),
        		type:'post',
        		dataType:'json',
        		data : {
        			id :id,
        			number :number,
        			operatorCode : companyCode
        		},
        		success:function(data){	
        			if (data.success) {
        				$.fn.tipLoddingEnd(true);
        				$.fn.tipAlert('基础号段修改成功',1,1);
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

//删除号码段
 function deleteInfo(id,number){
 	var addHtml = "<div>确定要删除号段【"+number+"】吗?</div>";
 	$.fn.tipOpen({
 		title : "删除确认",
 		width : '300',
 		height: '27',
 		btn : [{
 			label : '确定',
 			onClickFunction : 'deletenumber('+id+')'
 		}],
 		concent : addHtml
 	});
 }

 function deletenumber(id){
 	$.fn.tipLodding();
 	$.ajax({
 		url : WEB_SERVER_PATH + '/sys/base/basesection/delete',
 		type : 'post',
 		dataType : 'json',
 		data : {
 			id :id
 		},
 		success : function(data) {
 			if (data.success) {
 				$.fn.tipLoddingEnd(true);
 				$.fn.tipAlert('已删除',1,1);
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
 //取消
 function cancelText(){
		$('.layer,.tipBorder').remove();
	}
 function hasAuths(a,b){
		return (hasAuth(a)||hasAuth(b));
	}