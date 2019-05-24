//添加Ip
function addIp() {
	var len = $('#pei_z input').length;
	if (len < 10) {
		var ipVal = $(".imgs").prev("input").val();
		if (ipVal != "" && ipVal != null) {
			$('.imgs')
					.before(
							"<div class='remave only'  onclick='removeIp(this)'></div><input type='text' class='admo'/>");
			if (!$('.imgs').next().hasClass('remave')) {
				$('.imgs').after(
						"<div class='remave'  onclick='removeIp(this)'></div>")
			}
		} else {
			$.fn.tipAlert("请先添加前一个！", 1.5, 0);
		}
	}
	if (len == 10) {
		$.fn.tipAlert("IP最多为10个！", 1.5, 0);
	}
}
// 移除Ip
function removeIp(obj) {
	if ($('#pei_z input').length <= 2 && $(obj).next().val() === "") {
		$(obj).prev().remove();
		$('.remave').remove();
	}
	if ($(obj).hasClass("only")) {
		$(obj).prev().remove();
		$(obj).remove();
	} else {
		$(obj).prev().prev().prev().remove();
		$(obj).prev().prev().remove();
	}
	if ($('#pei_z input').length <= 1) {
		$('.remave').remove();
		return;
	}
}

// 获取指定字符的索引
function find(str,cha,num){
	var x=str.indexOf(cha);
	for(var i=0;i<num;i++){
	    x=str.indexOf(cha,x+1);
	}
	return x;
}
// 配置回显
function backSee() {
	if (serviceCodeChannel != undefined && serviceCodeChannel != "") {
		// 普通
		var ordinarystrindex = Number(find(serviceCodeChannel,'=',1) + 1);
		var ordinarystr = serviceCodeChannel.substring(ordinarystrindex)
		ordinarystr = ordinarystr.substring(0,ordinarystr.length-1)
		// 个性
		var perstrindex = Number(find(serviceCodeChannel,'=',0) + 1);
		var perstr = serviceCodeChannel.substring(perstrindex)
		perstr = perstr.substring(0,perstr.length-1)
		
		if (ordinarystr != "") {
			var arr = ordinarystr.split("#");
			for ( var i in arr) {
				var list = arr[i].split(",");
				if (list[0] == 'CMCC') {
					$("#commoncmcc option").each(function(index, item) {
						if ($(item).val() == list[1]) {
							$(item).attr("selected", true)
						}
					})
					continue;
				}
				if (list[0] == 'CUCC') {
					$("#commoncucc option").each(function(index, item) {
						if ($(item).val() == list[1]) {
							$(item).attr("selected", true)
						}
					})
					continue;
				}
				if (list[0] == 'CTCC') {
					$("#commonctcc option").each(function(index, item) {
						if ($(item).val() == list[1]) {
							$(item).attr("selected", true)
						}
					})
					continue;
				}
			}
		}
		
		if (perstr != "") {
			var perarr = perstr.split("#");
			for ( var i in perarr) {
				var perlist = perarr[i].split(",");
				if (perlist[0] == 'CMCC') {
					$("#personcmcc option").each(function(index, item) {
						if ($(item).val() == perlist[1]) {
							$(item).attr("selected", true)
						}
					})
					continue;
				}
				if (perlist[0] == 'CUCC') {
					$("#personcucc option").each(function(index, item) {
						if ($(item).val() == perlist[1]) {
							$(item).attr("selected", true)
						}
					})
					continue;
				}
				if (perlist[0] == 'CTCC') {
					$("#personctcc option").each(function(index, item) {
						if ($(item).val() == perlist[1]) {
							$(item).attr("selected", true)
						}
					})
					continue;
				}
			}
		}
	}

	// 是否需要返回报告
	isNeedReport == 1 ? ($("#isNeedReportNo").attr("checked", true))
			: ($("#isNeedReportAuto").attr("checked", true));
	// ip配置
	var ipConfigurations = ipConfiguration.split(",");
	var html = "";
	for (var i = 0; i < ipConfigurations.length; i++) {
		if (ipConfigurations[i] === "") {
			return;
		}
		if (i === ipConfigurations.length - 1) {
			if (ipConfigurations.length <= 1) {
				html += "<input type='text' value='"
						+ ipConfigurations[i]
						+ "' class='admo'/><div class='imgs' onclick='addIp()'></div>";
				continue;
			}
			html += "<input type='text' value='"
					+ ipConfigurations[i]
					+ "' class='admo'/><div class='imgs' onclick='addIp()'></div><div class='remave'  onclick='removeIp(this)'></div>";
		} else {
			html += "<input type='text' value='"
					+ ipConfigurations[i]
					+ "' class='admo'/><div class='remave only'  onclick='removeIp(this)'></div>";
		}
	}
	$('#pei_z').html(html);
}
backSee()
// 检索是否重复
function containsArray(arr, obj) {
	var i = arr.length;
	while (i--) {
		if (arr[i] === obj) {
			return true;
		}
	}
	return false;
}

// 选择通道带出详细信息
function detailInfo(obj) {
	var id = $(obj).val();
	if (id == '') {
		$(obj).parent().next('.labelspan').hide();
		return false;
	} else {
		$(obj).parent().next('.labelspan').show().html('')
	}
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/servicecode/manage/ajax/ruleinfo',
		type : 'post',
		dataType : 'json',
		async : false,
		data : {
			id : id
		},
		success : function(data) {
			if (data.success) {
				var html = '';
				var result = data.result;
				html = '<span class="bussinessType">'+result.businessType+'，</span><span>'+result.saveDesc+'，</span><span>'+result.contentType+'，</span><span>'+result.reportTypeDesc+'</span>'
				$(obj).parent().next('.labelspan').html(html)
			} else {
				$.fn.tipAlert(data.message, 1.5, 0);
			}
		},
		error : function() {
			$.fn.tipAlert('系统异常', 1.5, 2);
		}
	})
}


// 确定提交
function saveConf() {
	// IP request
	var requestIps = '';
	var ips = new Array();
	var ipCount = 0;
	$("#pei_z input[type='text']").each(function(i, ele) {
		if ($(ele).val() != "") {
			var ip = $(ele).val();
			var bool = containsArray(ips, ip);
			if (!bool) {
				ips.push($(ele).val());
			}
			++ipCount;
		}
	});
	if (ips.length < ipCount) {
		$.fn.tipAlert("填写的IP配置有重复", 1.5, 0);
		return;
	}
	ips.length > 0 && (requestIps = ips.join(','));
	// 是否需要状态报告
	var isNeedReport = $("#isNeedReport>input:checked").val();
	// 获取闪推通道
	var str = '';
	var perstr = '';
	$("#commonchannel div select").each(function() {
		if ($(this).val() != "") {
			str += $(this).attr("operator") + ',' + $(this).val() + '#';
		}
	});
	$("#personchannel div select").each(function() {
		if ($(this).val() != "") {
			perstr += $(this).attr("operator") + ',' + $(this).val() + '#';
		}
	});
	var serviceCodeChannel = str.substring(0, str.length - 1)
	var psersonServiceCodeChannel = perstr.substring(0, perstr.length - 1)
	$.ajax({
		url : WEB_SERVER_PATH + '/fms/servicecode/manage/ajax/saveInfo',
		type : 'post',
		dataType : 'json',
		async : false,
		data : {
			id : id,
			isNeedReport : isNeedReport,
			ipConfiguration : requestIps,
			serviceCodeChannel : serviceCodeChannel, // 普通通道
			psersonServiceCodeChannel : psersonServiceCodeChannel // 个性通道
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipAlert('服务号规则配置成功', 1.5, 1);
				goPage("/fms/servicecode/manage");
			} else {
				$.fn.tipAlert(data.message, 1.5, 0);
			}
		},
		error : function() {
			$.fn.tipAlert('系统异常', 1.5, 2);
		}
	})
}
// 取消配置
function addCancel() {
	goPage("/fms/servicecode/manage");
}
