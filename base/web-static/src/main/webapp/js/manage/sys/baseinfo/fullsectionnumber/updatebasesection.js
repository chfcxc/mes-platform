//号段修改
function updateTure(){
	var $formCheckOut = $('#fullFormUpdate');
   var validateor=$formCheckOut.validate({
    	rules:{     
    		numberUpdate:{ 
				required:true,
				sectionnumber:true
			},
			provinceUpdate:{   
				required:true
			}
    	},
		messages:{
			numberUpdate:{
				required:"请输入正确的号段数",
				maxlength :$.validator.format("请输入正确的号段数")
			},
			provinceUpdate:{
				required:"请选择省份",
				maxlength :$.validator.format("省份不可为空")
			}
		},
		submitHandler: function() {
			var number = $("#numberInput").val(); //号段数
			var provinceCode = $("#provinceInput").val();
			var operatorCode = $("#companyInput").val();//运营商
			var city = $("#cityInput").val(); //城市
			$("#areaCodeInput").val(provinceCode); //地区编码
			var id=$("#fullFormUpdate input[type=hidden]").val();
			$.fn.tipLodding();
			$.ajax({
				url : WEB_SERVER_PATH + '/sys/base/section/updatesn?randomdata='+Date.parse(new Date()),
				type : 'post',
				dataType : 'json',
				data : {
					id :id,
					number :number, //号码段
        			provinceCode : provinceCode,//省份编码
        			operatorCode : operatorCode,//运营商
        			city : city//城市
				},
				success : function(data) {
					if (data.success) {
						$.fn.tipLoddingEnd(true);
						$.fn.tipAlert('精准号段修改成功',1,1);
						goPage("/sys/base/section");
						emrefulsh('emTableConfig');
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
function addCancel(){
	goPage("/sys/base/section");
}