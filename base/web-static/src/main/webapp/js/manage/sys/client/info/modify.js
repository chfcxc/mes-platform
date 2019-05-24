$(function(){
    var telephone=$("#telephone").val();
    var arr=telephone.split("-");
    $("#telephone1").val(arr[0]);
    $("#telephone2").val(arr[1]);
    $("#telephone3").val(arr[2]);
    var valueAddedServiceInput=$("#valueAddedServiceInput").val();
    var arr=valueAddedServiceInput.split(",");
    $("#valueAddedService input[type=checkbox]").each(function(i,elent){
    	if($.inArray($(elent).val(),arr)!==-1){
    		$(elent).attr("checked","checked");
    	}
    })
    
    var serviceTypeInput=$("#serviceTypeInput").val();
    var arr1=serviceTypeInput.split(",");
    $("#serviceType input[type=checkbox]").each(function(i,elent){
    	if($.inArray($(elent).val(),arr1)!==-1){
    		$(elent).attr("checked","checked");
    	}
    })
    $("#startDate").val(stateTime);
 	$("#endDate").val(endTime);
});
function modifyTure(){
	var $formCheckOut = $('#infoForm');
    var validateor=$formCheckOut.validate({
    	rules:{
    		clientName :{ 
				required:true,
				maxlength:50
			},
			authority:{
				required:true,
			},
			viplevel:{
				required:true,
			},
			linkman:{
				required:false,
				real_name_new:true
			},
			mobile:{
				required:false,
				cMobile:true
			},
			salename:{
				required:true
			},
			email:{
				required:false
			}
    	},
		messages:{
    		clientName :{ 
				required:"客户名称不能为空",
				maxlength :$.validator.format("客户名称长度不可超过50个字符")
			},
			authority:{
				required:"请选择客户权限",
			},
			viplevel:{
				required:"请选择客户级别",
			},
			salename:{
				required:"销售不能为空"
			}
		},
		submitHandler: function() {
			var id=$("#clintId").val();
			var clientName=$("#nameCn").val();
			var type=$("#clientType").val();
			var authority=$("#authority").val();
			var linkman=$("#linkman").val();
			var mobile=$("#mobile").val();
			var salesId=$("#saleId").val();
			var email=$("#email").val();
			var address=$("#address").val();
			var viplevel=$("#viplevel").val();
			var telephone1=$("#telephone1").val();
			var telephone2=$("#telephone2").val();
			var telephone3=$("#telephone3").val();
			var startDate=$("#startDate").val();
			var endDate=$("#endDate").val();
			var telephone="";
			var reg=/^[0-9]*$/;	
			if (telephone1!=null && telephone1!="" && !reg.test(telephone1)) {
				$.fn.tipAlert("请输入数字", 1.5, 0);
				return false;
			}
			if (telephone2!=null && telephone2!="" && !reg.test(telephone2)) {
				$.fn.tipAlert("请输入数字", 1.5, 0);
				return false;
			}
			if (telephone3!=null && telephone3!="" && !reg.test(telephone3)) {
				$.fn.tipAlert("请输入数字", 1.5, 0);
				return false;
			}
			telephone=telephone1+"-"+telephone2+"-"+telephone3;
			var isvip="";
			if($("#isvip").is(":checked")){
				isvip=1;
			}else{
				isvip=0;
			}
			var len=$("#valueAddedService input[type=checkbox]:checked").length;
			if(len!=0){
				var valueAddedService="";
				$("#valueAddedService input[type=checkbox]:checked").each(function(j,ele){
					if(j==len-1){
						valueAddedService+=$(ele).val();
					}else{
						valueAddedService+=$(ele).val()+",";
					}
				})
			}
			
			var len1=$("#serviceType input[type=checkbox]:checked").length;
			if(len1==0){
				$.fn.tipAlert("请选择开通服务", 1.5, 0);
				return false;
			}
			if(len1!=0){
				var serviceType="";
				$("#serviceType input[type=checkbox]:checked").each(function(j,ele){
					if(j==len1-1){
						serviceType+=$(ele).val();
					}else{
						serviceType+=$(ele).val()+",";
					}
				})
			}
			$.fn.tipLodding();
        	$.ajax({
        		url : WEB_SERVER_PATH + '/sys/client/info/ajax/modify?randomdata='+Date.parse(new Date()),
        		type : 'post',
        		dataType : 'json',
        		data : {
        			id:id,
        			clientName :clientName, 
        			isvip : isvip,
        			type:type,
        			authority:authority,
        			valueAddedService:valueAddedService,
        			linkman :linkman,
        			mobile  :mobile,
        			salesId : salesId,
        			email:email,
        			address:address,
        			viplevel:viplevel,
        			telephone:telephone,
        			serviceType : serviceType,
        			startTime : startDate,
        			endTime : endDate
        		},
        		success : function(data) {
        			if (data.success) {
        				$.fn.tipLoddingEnd(true);
        				$.fn.tipAlert('客户修改成功',1,1);
        				goPage('/sys/client/info');
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

//选择销售
var emTableConfigtwo;
function chooseSale(obj){
    var tipBorderLen=$(".tipBorder").length;
    if(tipBorderLen==0){
    	var html="";
    	html='<div id="innerDiv"></div>';
		$.fn.tipOpen({
			title : "选择销售",
			width : '660',
			height:'',
			concent :html,
			btn : [{
				label : '确定',
				onClickFunction : 'turnTrue()'
			}],
		});
		emTableConfigtwo={
				outerDivId : "innerDiv",
				pagesShow : true, 
				totalNumbersShow:true,
				searchConfig : {
					searchItems : [  {
						label : '销售',
						id : 'username',
						type : 'input'
					} ],

					searchButton : true,
					resetButton : false
				},
				ajaxConfig : {
					url : WEB_SERVER_PATH + '/sys/client/info/ajax/saleslist',
					method : 'POST',
					data : {
						username :'#username'
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
								width : "10%",
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
								title : "部门",
								width : "30%",
								context : "<div class='leftdiv'>@{department}</div>"
							}]
				}
			}
		$(".tipBorder").css("top","50px");
		emTable('emTableConfigtwo');
		$("#username").attr("placeholder","姓名/用户名");
    }
}
function turnTrue(){
	var len=$("input[name='myRadio']:checked").length;
	if(len==0){
		 $.fn.tipAlert("请选择销售!",1.5,0);
		 return false;
	}
	$('.check:checked').each(function(inx,ele){
		    var id=$(ele).attr("svalue");
			var username = $(ele).parent().next().next().text();
			$("#salename").val(username);
			$("#saleId").val(id);
			$('.layer,.tipBorder').remove();
		});
}