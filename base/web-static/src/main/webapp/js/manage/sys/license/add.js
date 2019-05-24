//新增确定事件
function openAddBox(){
	var $formCheckOut = $('#LicenseForm');
	var validateor=$formCheckOut.validate({
   	rules:{
   			agentAbbr:{
				required:true
			},
			beginTime:{
				required:true
			},
			endTime:{
				required:true		
			},
			mac:{
				required:true,
				macReg:true
			},
			serviceType:{
				required:true,
			}
    	},
		messages:{
			agentAbbr:{
				required:"客户名称不可为空"
			},
			beginTime:{
				required:"生效时间不可为空"
			},
			endTime:{
				required:"失效时间不可为空"
			},
			mac:{
				required:"MAC地址不可为空"
				
			},
			serviceType:{
				required:"短彩信服务不可为空"
			}
			
		},
		submitHandler: function() {
			var systemEnterpriseId=$("#systemEnterpriseId").val();
			var product = $("#product option:selected").val();
			var version = $("#version").val();
			var beginTime = $("#beginTime").val();
			var endTime = $("#endTime").val();
			var mac = $("#mac").val();
			var serviceType='';
			$("#serviceType input[type='checkbox']:checked").each(function(index,ele){
				serviceType+=$(ele).val()+","
			});
			serviceType=serviceType.substr(0,serviceType.length-1);
			$.fn.tipLodding();
        	$.ajax({
        		url : WEB_SERVER_PATH + '/sys/license/recode/ajax/add',
        		type : 'post',
        		dataType : 'json',
        		data : {
        			systemEnterpriseId : systemEnterpriseId,
        			product : product,
        			version : version,
        			beginTime : beginTime,
        			endTime : endTime,
        			mac : mac,
        			serviceType : serviceType
        		},
        		success : function(data) {
        			if (data.success) {
        				$.fn.tipLoddingEnd(true);
        				$.fn.tipAlert('生成License成功',1,1);
        				goPage("/sys/license/recode");
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
