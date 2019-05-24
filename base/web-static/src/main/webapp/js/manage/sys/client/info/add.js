function addTure(){
	var $formCheckOut = $('#infoForm');
    var validateor=$formCheckOut.validate({
    	rules:{    
    		clientNumber:{
    			required:true,
    			cNumber:true
    		},
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
			clientNumber:{
				required:"客户编号不能为空"
    		},
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
				required:"销售不能为空",
			}
			
		},
		submitHandler: function() {
			var clientNumber=$("#clientNumber").val();
			var clientName=$("#clientName").val();
			var type=$("#clientType").val();
			var authority=$("#authority").val();
			var linkman=$("#linkman").val();
			var mobile=$("#mobile").val();
			var salesId=$("#saleId").val();
			var email=$("#email").val();
			var address=$("#address").val();
			var viplevel =$("#viplevel").val();
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
        		url : WEB_SERVER_PATH + '/sys/client/info/ajax/add?randomdata='+Date.parse(new Date()),
        		type : 'post',
        		dataType : 'json',
        		data : {
        			clientNumber : clientNumber,
        			isvip : isvip,
        			clientName : clientName , 
        			type : type,
        			authority:authority,
        			valueAddedService:valueAddedService,
        			linkman : linkman ,
        			mobile  : mobile,
        			salesId : salesId,
        			email : email,
        			address : address,
        			viplevel:viplevel,
        			telephone : telephone,
        			serviceType : serviceType,
        			startTime : startDate,
        			endTime : endDate
        		},
        		success : function(data) {
        			if (data.success) {
        				$.fn.tipLoddingEnd(true);
        				var data=data.result;
        				var html ="";
        				html = "<div>生成的管理账号:"+data.userName+",密码:"+data.password+"</div>";
        				html+='<div class="tipFoot tipFootMp20">';
        				html+='<button onclick="repage()" type="button" class="tipBtn">确 定</button>';
        				html+='</div>';
        				$.fn.tipOpen({
        					title : "生成管理账号密码",
        					width : '300',
        					concent :html,
        					btn : []				
        				});
        			} else {
        				$.fn.tipLoddingEnd(false);
        				validateor.resetForm();
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
function repage(){
	goPage('/sys/client/info');
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
		/*var saleId=$(obj).next().val();
		setTimeout(function(){
			if(saleId != null || saleId != ""){
				$(".emtable_table_table input[type='radio']").each(function(i,ele){
					var id=$(ele).attr("svalue");
					if(id==saleId){
						$(ele).attr("checked",true);
					}
				})  
			}
		},300);*/
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