//号段新增
function addTure(){
	var $formCheckOut = $('#fullForm');
    var validateor=$formCheckOut.validate({
    	rules:{     
    		numberInput:{ 
				required:true,
				sectionnumber:true
			},
			provinceInput:{   
			  required:true 
			}
    	},
		messages:{
			numberInput:{
				required:"请输入7位数字",
				maxlength :$.validator.format("请输入以1开头的7位数字")
			},
			provinceInput:{
				required:"请选择省份", 
				maxlength :$.validator.format("省份不可为空")
			}
		},
		submitHandler: function() {
			var number = $("#numberInput").val(); //号段数
			var provinceCode = $("#provinceInput").val();//省份编码
			var operatorCode = $("#companyInput option:selected").val();//运营商
			var city = $("#cityInput").val(); //城市
			$("#areaCodeInput").val(provinceCode); 
			$.fn.tipLodding();
        	$.ajax({
        		url : WEB_SERVER_PATH + '/sys/base/section/addsn?randomdata='+Date.parse(new Date()),
        		type : 'post',
        		dataType : 'json',
        		data : {
        			number : number, //号码段
        			provinceCode : provinceCode,//省份编码
        			operatorCode : operatorCode,//运营商
        			city : city//城市
        		},
        		success : function(data) {
        			if (data.success) {
        				$.fn.tipLoddingEnd(true);
        				$.fn.tipAlert('精准号段添加成功',1,1);
        				goPage("/sys/base/section");
        				emrefulsh('emTableConfig');
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

function addCancel(){
	goPage("/sys/base/section");
}