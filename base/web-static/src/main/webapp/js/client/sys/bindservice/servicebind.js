$(function() {
	// 获取已经开通的服务
	var businessList = [];
	function getBusinessList() {
		$.ajax({
			url:  WEB_SERVER_PATH + "/ajax/getBusinessList",
			type:'post',
			dataType:'json',
			data:{},
			success:function(data){
				if (data.success) {
					$.fn.tipLoddingEnd(true);
					businessList = data.result;
					bindService();
				}
			},
			error:function(){
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert('系统异常',1.5,0);
			}
		});
	}
	getBusinessList();
	
	//	页面显示
	function bindService() {
		var userName = $('#username').val();
		for (var t=0;t<businessList.length;t++) {
			var html = '';
			html+='<input  id="modifInpVal" type="hidden" />'+
				'<input  id="modifInpValPlat" type="hidden" />'+
					'<dl class="alert-cen">'+
						'<dd class="authority">'+
							'<div class="role-title role-on light_grey_bg smstitle" onclick="role(this)"><i></i>'+businessList[t].businessNameSimple+'账号绑定<input type="hidden" />&nbsp;&nbsp;（'+userName+'）</div>'+
							'<div class="greyLine"></div>'+
							'<div class="role_box" id="serviceCodeBox">'+
								'<div class="role-add" id="role-add" onclick="showDatas(\''
								+businessList[t].businessCode+'\',\''+businessList[t].businessNameSimple+'\')">'+
									'<div class="yellow_add fl"></div>'+
									'<div class="serviceCode_bind fl">绑定'+businessList[t].businessNameSimple+'服务号</div>'+
									'<div class="clear"></div>'+
								'</div>'+
								'<div class="role-cen" >'+
									'<div class="roleAdd" id="'+businessList[t].businessCode+'roleAdd"></div>'+
								'</div>'+
							'</div>'+
						'</dd>'+
					'</dl>'
			$('#modifyRoleForm').append(html);
			$.ajax({
				url : WEB_SERVER_PATH +businessList[t].businessCode+ '/basesupport/account/ajax/getBindServiceCode',
				type : 'post',
				dataType : 'json',
				data : {
					userId : userId
				},
				success : function(data) {
					if (data.success) {
						var dataResult = data.result;
						var dataCode = data.result.code;
						// 绑定服务号
						var bindingList = dataResult.bindingList;
						var serviceCodeHtml = '';
						if (bindingList != "") {
							for (i in bindingList) {
								var serviceCode = bindingList[i].serviceCode;
								var id = bindingList[i].id;
								serviceCodeHtml += '<div class="roleNavFind">';
								serviceCodeHtml += '<label><input class="checkboxdo" type="hidden" value="'
										+ serviceCode
										+ '" />'
										+ serviceCode
										+ '<div class="deleDiv" onclick="delServiceCode(this,\''+dataCode+'\','+ id + ')"></div></label>';
								serviceCodeHtml += '</div>';
							}
						} else {
							serviceCodeHtml += '<label class="nodata">暂无数据</label>';
						}
						$("#"+dataCode+"roleAdd").html(serviceCodeHtml);
						$("#modifInpVal").val(userId);
						// 绑定平台
						if (dataResult.type == 1) {
							var plathtml = '';
							plathtml+='<div class="role_box"  id="platFormCodeBox">'+
										'<div class="role-add" onclick="showDatasPlat(\''+dataCode+'\')">'+
											'<div class="yellow_add fl"></div>'+
											'<div class="serviceCode_bind fl"> 绑定平台 </div>'+
											'<div class="clear"></div>'+
										'</div>'+
										'<div class="role-cen" >'+
											'<div class="roleAdd"></div>'+
										'</div>'+
									'</div>'
						$('#serviceCodeBox').before(plathtml);
							var bindPlatformCodelist = dataResult.bindPlatformCodelist;
							var platCodeHtml = '';
							if (bindPlatformCodelist != "") {
								for (i in bindPlatformCodelist) {
									var platformCode = bindPlatformCodelist[i].platformCode;
									platCodeHtml += '<div class="roleNavFind">';
									platCodeHtml += '<label><input class="checkboxdo" type="hidden" value="'
											+ platformCode
											+ '" />'
											+ platformCode
											+ '<div class="deleDiv" onclick="delServiceCodePlat(this,\''+dataCode+'\')"></div></label>';
									platCodeHtml += '</div>';
								}
							} else {
								platCodeHtml += '<label class="nodata">暂无数据</label>';
							}
							$("#platFormCodeBox .roleAdd").html(platCodeHtml);
						} else {
							$("#platFormCodeBox").hide();
						}

					} else {
						$.fn.tipAlert(data.message, 1.5, 0);
						$(".role-add").attr("onclick", "");
					}
				}

			});
		}
	}
});

String.prototype.endWith = function(endStr) {
	var d = this.length - endStr.length;
	return (d >= 0 && this.lastIndexOf(endStr) == d)
};
// 三角箭头
function role(obj) {
	if ($(obj).hasClass('role-on')) {
		$(obj).removeClass('role-on')
		$(obj).parent().find(".role_box").slideUp();
	} else {
		$(obj).addClass('role-on')
		$(obj).parent().find(".role_box").slideDown();
	}
}
// 绑定服务号弹框
function showDatas(bCode, bName) {
	var html = '<dt class="ofh"><div class="choci-box"><div class="choice-top" ><div class="choice-sclcted choice-sclctedxin"><input type="text" id="choiceSelected" value="" /><div onclick="choiceSel(\''+bCode+'\')" class="searchBoxImglxr" title="搜索服务号"></div></div></div>';
	html += '<div class="choice-left" ><div class="choice-title">服务号</div>';
	html += '<div id="seeNoBlingsDetails"></div>';
	html += '</div><div class="clear"></div></div>';
	html += '<div class="choice-right">';
	html += '<div class="choice-title">已添加</div>';
	html += '<div id="seeAllBlingsDetails">';
	html += '</div>';
	html += '</div><div class="clear"></div></dt>';
	html += '</dl>';
	$.fn.tipOpen({
		title : "绑定"+bName+"服务号 ",
		width : '440',
		height : '265',
		btn : [ {
			label : '绑定',
			onClickFunction : 'addChoice(\"'+bCode+'\")'
		} ],
		concent : html
	});
	choiceServiceCode(bCode, bName);
}
// 搜索服务号 弹框
function choiceSel(bCode) {
	var searchVal = $("#choiceSelected").val();
	var result = getServiceCode($("#choiceSelected").val());
	if (searchVal != "") {
		if (result != "") {
			var arrs = result.split(",");
			$(".noBoundBox ul li").remove();
			$(".noBoundBox ul")
					.append(
							'<li  onclick="changeColorNew(this)" ondblclick="rightPriority(this)">');
			$(".noBoundBox ul li").html(
					'<input type="hidden" value="' + arrs[1] + '"/>' + arrs[0]);
		} else {
			$(".noBoundBox ul li").remove();
			$(".noBoundBox ul").append("<li></li>");
			$(".noBoundBox ul li").html("暂无数据");
		}
	} else {
		searchItem(bCode);
	}
}
// 服务号为空，搜索事件
function searchItem(bCode) {
	$.ajax({
				url : WEB_SERVER_PATH + bCode+ '/basesupport/account/ajax/getServiceCode',
				type : 'post',
				data : {
					userId : userId
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						// 未绑定
						var dataResult = data.result.notBoundList; // 左侧未绑定的服务号
						var seeNotBoundListHtml = '<div class="noBoundBox"><ul>';
						if (dataResult != "") {
							hash = {};
							for (i in dataResult) {
								var id = dataResult[i].id;
								var serviceCode = dataResult[i].serviceCode;
								if (serviceCode.endWith(")")) {
									hashall[serviceCode.substring(0,
											serviceCode.indexOf("("))] = serviceCode;
								}
								hash[serviceCode] = serviceCode + "," + id;
								seeNotBoundListHtml += '<li  onclick="changeColorNew(this)" ondblclick="rightPriority(this)"><input type="hidden" value="'
										+ id + '"/>' + serviceCode + '</li>';
							}
							for ( var i in lefthash) {
								if (hash[lefthash[i].split(",")[0]] == undefined) {
									seeNotBoundListHtml += '<li  onclick="changeColorNew(this)" ondblclick="rightPriority(this)"><input type="hidden" value="'
											+ id
											+ '"/>'
											+ lefthash[i].split(",")[0]
											+ '</li>';
								}
							}
						} else {
							seeNotBoundListHtml += '<li class="alignCenter">暂无数据<input type="hidden" value=""></li>';
						}
						seeNotBoundListHtml += '</ul></div>';
						$("#seeNoBlingsDetails").html(seeNotBoundListHtml);
						$("#modifInpVal").val(userId);
					} else {
						$.fn.tipAlert(data.message, 1.5, 0);
					}
				},
				error : function() {
					$.fn.tipAlert('系统异常', 1.5, 0);
				}
			});
}
var hash = {};
var hashall = {};
var hashPlat = {}; // 平台
function getServiceCode(a) {
	var search = a;
	if (!a.endWith(")")) {
		if (hashall[search] != undefined) {
			search = hashall[a];
		}
	}
	if (hash[search] != undefined) {
		return hash[search];
	}
	if (hashPlat[search] != undefined) {
		return hashPlat[search];
	}
	if (lefthash[search] != undefined) {
		return lefthash[search];
	}
	if (lefthashPlat[search] != undefined) {
		return lefthashPlat[search];
	}
	return "";
}



function choiceServiceCode(code, name) {
	var serviceCode = $('#choiceSelected').val();
	$.ajax({
		url : WEB_SERVER_PATH + code +'/basesupport/account/ajax/getServiceCode',
		type : 'post',
		data : {
			userId : userId
//			serviceCode : serviceCode
		},
		dataType : 'json',
		success : function(data) {
			if (data.success) {
				// 未绑定
				var dataResult = data.result.notBoundList; // 左侧未绑定的服务号
				var seeNotBoundListHtml = '<div class="noBoundBox"><ul>';
				if (dataResult != "") {
					hash = {};
					hashall = {};
					lefthash = {};
					righthash = {};
					for (i in dataResult) {
						var id = dataResult[i].id;
						var serviceCode = dataResult[i].serviceCode;
						if (serviceCode.endWith(")")) {
							hashall[serviceCode.substring(0,
									serviceCode.indexOf("("))] = serviceCode;
						}
						hash[serviceCode] = serviceCode + "," + id;
						seeNotBoundListHtml += '<li  onclick="changeColorNew(this)" ondblclick="rightPriority(this)"><input type="hidden" value="'
								+ id + '"/>' + serviceCode + '</li>';
					}
				} else {
					seeNotBoundListHtml += '<li class="alignCenter">暂无数据<input type="hidden" value=""></li>';
				}
				seeNotBoundListHtml += '</ul></div>';
				// 已添加
				var smsList = data.result.bindingList;
				var seeAllBlingsHtml = '<div class="bindingBox"><ul>';
				if (smsList != "") {
					for (i in smsList) {
						var id = smsList[i].id;
						var serviceCode = smsList[i].serviceCode;
						seeAllBlingsHtml += '<li  onclick="changeColor(this)" class="bindTrBg" ondblclick="leftPriority(this)"><input type="hidden" value="'
								+ id + '"/>' + serviceCode + '</li>';
					}
				} else {
					seeAllBlingsHtml += '<li class="alignCenter">暂无数据<input type="hidden" value=""></li>';
				}
				seeAllBlingsHtml += '</ul></div>';
				$("#seeAllBlingsDetails").html(seeAllBlingsHtml);
				$("#seeNoBlingsDetails").html(seeNotBoundListHtml);
				$("#modifInpVal").val(userId);
			} else {
				$.fn.tipAlert(data.message, 1.5, 0);
			}
		},
		error : function() {
			$.fn.tipAlert(name + '服务号未开启', 1.5, 0);
		}
	});
}
function changeColor(obj) {
	$("#seeAllBlingsDetails ul li").removeClass("onGreen");
	$(obj).addClass("onGreen");
}
function changeColorNew(obj) {
	$(obj).addClass("onGreen").siblings().removeClass("onGreen");
}
var lefthash = {};
var righthash = {};
// 向左移动
function leftPriority() {
	if ($(".noBoundBox ul li").text() == "暂无数据") {
		$(".noBoundBox ul").html("");
	}
	$("#seeAllBlingsDetails ul li").each(
			function(index, element) {
				var a = $(element).text();
				if ($(element).hasClass("onGreen")) {
					if (hash[a] == undefined && lefthash[a] == undefined) {
						$("#seeNoBlingsDetails ul").prepend($(element));
						hashall[a.substring(0, a.indexOf("("))] = a;
						if (righthash[a] != undefined) {
							delete righthash[a];
						}
						lefthash[a] = a
								+ ","
								+ $(element).parent().find(
										"input[type='hidden']").val();
						$(element).removeClass("onGreen");
						$(element).removeAttr("onclick", "changeColor(this)")
								.attr("onclick", "changeColorNew(this)").attr(
										"ondblclick", "rightPriority(this)");
					} else {
						$(element).remove();
						delete righthash[a];
						return;
					}
				}
			});
}
// 向右移动
function rightPriority(obj) {
	if ($(".bindingBox ul li").text() == "暂无数据") {
		$(".bindingBox ul").html("");
	}
	$("#seeNoBlingsDetails ul li").each(
			function(index, element) {
				if ($(element).hasClass("onGreen")) {
					var del = $(element).text()
					if (righthash[del] != undefined) {
						$.fn.tipAlert('服务号已存在已添加列表', 1.5, 0);
						return;
					}
					$("#seeAllBlingsDetails ul").prepend($(element));
					delete hash[del];
					delete hashall[del.substring(0, del.indexOf("("))];
					delete lefthash[del];
					righthash[del] = del;
					$(element).removeClass("onGreen");
					$(element).removeAttr("onclick", "changeColorNew(this)")
							.attr("onclick", "changeColor(this)").attr(
									"ondblclick", "leftPriority(this)");
				}
			});
}
// 确定
function addChoice(addcode) {
	var serviceCodeIds = '';
	var html = '';
	$("#seeAllBlingsDetails ul li").each(function(index, obj) {
		serviceCodeIds += $(obj).find("input[type=hidden]").val() + ",";
	});
	serviceCodeIds = serviceCodeIds.substring(0,
			serviceCodeIds.length - 1);
	if (serviceCodeIds != "") {
		$.fn.tipLodding();
		$.ajax({
			url : WEB_SERVER_PATH + addcode+'/basesupport/account/ajax/bindServiceCode',
			type : 'post',
			data : {
				userId : userId,
				serviceCodeIds : serviceCodeIds
			},
			dataType : 'json',
			success : function(data) {
				if (data.success) {
					$.fn.tipLoddingEnd(true);
					$.fn.tipAlert('绑定成功', 1, 1);
					$.fn.tipShut();
					window.location.reload();
				} else {
					$.fn.tipLoddingEnd(false);
					$.fn.tipAlert(data.message, 1.5, 0);
				}
			},
			error : function() {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert('系统异常', 1.5, 2);
			}
		});
	} else {
		$.fn.tipAlert('请添加服务号！', 1.5, 0);
		return;
	}
}
/** ************************************************绑定平台*********************************************************************** */
// 绑定平台弹框
function showDatasPlat(dCode) {
	var platHtml = '<dt class="ofh"><div class="choci-box"><div class="choice-top" >';
	platHtml += '<div class="choice-sclcted choice-sclctedxin"><input type="text" id="choiceSelectedPlat" value="" />';
	platHtml += '<div onclick="choiceSelPlat(\''+dCode+'\')" class="searchBoxImglxr" title="搜索平台"></div></div></div>';
	platHtml += '<div class="choice-left"><div class="choice-title">平台代码</div>';
	platHtml += '<div id="seeNoBlingsDetailsPlat"></div>';
	platHtml += '</div><div class="clear"></div></div>';
	platHtml += '<div class="choice-right">';
	platHtml += '<div class="choice-title">已添加</div>';
	platHtml += '<div id="seeAllBlingsDetailsPlat">';
	platHtml += '</div>';
	platHtml += '</div><div class="clear"></div></dt>';
	platHtml += '</dl>';
	$.fn.tipOpen({
		title : "绑定平台",
		width : '440',
		height : '265',
		btn : [ {
			label : '绑定',
			onClickFunction : 'addChoicePlat(\"'+dCode+'\")'
		} ],
		concent : platHtml
	});
	choiceServiceCodePlat(dCode);
}
// 搜索平台按钮
function choiceSelPlat(dCode) {
	var searchVal = $("#choiceSelectedPlat").val().toUpperCase();
	var result = getServiceCode($("#choiceSelectedPlat").val().toUpperCase());
	if (searchVal != "") {
		if (result != "") {
			var arrs = result.split(",");
			$(".noBoundBox ul li").remove();
			$(".noBoundBox ul")
					.append(
							'<li  onclick="changeColorNew(this)" ondblclick="rightPriorityPlat(this)">');
			$(".noBoundBox ul li").html(
					'<input type="hidden" value="' + arrs[0] + '"/>' + arrs[0]);
		} else {
			$(".noBoundBox ul li").remove();
			$(".noBoundBox ul").append("<li></li>");
			$(".noBoundBox ul li").html("暂无数据");
		}
	} else {
		searchItemPlat(dCode);
	}
}
// 平台代码为空，搜索事件
function searchItemPlat(dCode) {
	$.ajax({
				url : WEB_SERVER_PATH +dCode+'/basesupport/account/ajax/getPlatformCode',
				type : 'post',
				data : {
					userId : userId
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						// 未绑定
						var dataResult = data.result.notBoundList; // 左侧未绑定平台
						var seeNotBoundListHtmlPlat = '<div class="noBoundBox"><ul>';
						if (dataResult != "") {
							hashPlat = {};
							for (i in dataResult) {
								var id = dataResult[i].id;
								var platformCode = dataResult[i].platformCode;
								hashPlat[platformCode] = platformCode + ","
										+ id;
								seeNotBoundListHtmlPlat += '<li  onclick="changeColorNew(this)" ondblclick="rightPriorityPlat(this)"><input type="hidden" value="'
										+ platformCode
										+ '"/>'
										+ platformCode
										+ '</li>';
							}
							for ( var i in lefthashPlat) {
								if (hashPlat[lefthashPlat[i].split(",")[0]] == undefined) {
									seeNotBoundListHtmlPlat += '<li  onclick="changeColorNew(this)" ondblclick="rightPriorityPlat(this)"><input type="hidden" value="'
											+ platformCode
											+ '"/>'
											+ lefthashPlat[i].split(",")[0]
											+ '</li>';
								}
							}
						} else {
							seeNotBoundListHtmlPlat += '<li class="alignCenter">暂无数据<input type="hidden" value=""></li>';
						}
						seeNotBoundListHtmlPlat += '</ul></div>';
						$("#seeNoBlingsDetailsPlat").html(
								seeNotBoundListHtmlPlat);
						$("#modifInpValPlat").val(userId);
					} else {
						$.fn.tipAlert(data.message, 1.5, 0);
					}
				},
				error : function() {
					$.fn.tipAlert('系统异常', 1.5, 0);
				}
			});
}
// 查询所有平台
function choiceServiceCodePlat(dCode) {
	var platformCode = $('#choiceSelectedPlat').val();
		$.ajax({
				url : WEB_SERVER_PATH + dCode+'/basesupport/account/ajax/getPlatformCode',
				type : 'post',
				data : {
					userId : userId,
					platformCode : platformCode
				},
				dataType : 'json',
				success : function(data) {
					console.log(data.result)
					if (data.success) {
						// 未绑定
						var dataResult = data.result.notBoundList; // 左侧未绑定的服务号
						var seeNotBoundListHtmlPlat = '<div class="noBoundBox"><ul>';
						if (dataResult != "") {
							hashPlat = {};
							lefthashPlat = {};
							righthashPlat = {};
							for (i in dataResult) {
								var id = dataResult[i].id;
								var platformCode = dataResult[i].platformCode;
								hashPlat[platformCode] = platformCode + ","
										+ id;
								seeNotBoundListHtmlPlat += '<li  onclick="changeColorNew(this)" ondblclick="rightPriorityPlat(this)"><input type="hidden" value="'
										+ platformCode
										+ '"/>'
										+ platformCode
										+ '</li>';
							}
						} else {
							seeNotBoundListHtmlPlat += '<li class="alignCenter">暂无数据<input type="hidden" value=""></li>';
						}
						seeNotBoundListHtmlPlat += '</ul></div>';
						// 已添加
						var bindingList = data.result.bindingList;
						var seeAllBlingsHtmlPlat = '<div class="bindingBox"><ul>';
						if (bindingList != "") {
							for (i in bindingList) {
								var id = bindingList[i].id;
								var platformCode = bindingList[i].platformCode;
								seeAllBlingsHtmlPlat += '<li  onclick="changeColor(this)" class="bindTrBg" ondblclick="leftPriorityPlat(this)"><input type="hidden" value="'
										+ platformCode
										+ '"/>'
										+ platformCode
										+ '</li>';
							}
						} else {
							seeAllBlingsHtmlPlat += '<li class="alignCenter">暂无数据<input type="hidden" value=""></li>';
						}
						seeAllBlingsHtmlPlat += '</ul></div>';
						$("#seeAllBlingsDetailsPlat")
								.html(seeAllBlingsHtmlPlat);
						$("#seeNoBlingsDetailsPlat").html(
								seeNotBoundListHtmlPlat);
						$("#modifInpValPlat").val(userId);
					} else {
						$.fn.tipAlert(data.message, 1.5, 0);
					}
				},
				error : function() {
					$.fn.tipAlert('系统异常', 1.5, 0);
				}
			});
}
var lefthashPlat = {};
var righthashPlat = {};
// 向左移动
function leftPriorityPlat() {
	if ($(".noBoundBox ul li").text() == "暂无数据") {
		$(".noBoundBox ul").html("");
	}
	$("#seeAllBlingsDetailsPlat ul li").each(
			function(index, element) {
				var a = $(element).text();
				if ($(element).hasClass("onGreen")) {
					if (hashPlat[a] == undefined
							&& lefthashPlat[a] == undefined) {
						$("#seeNoBlingsDetailsPlat ul").prepend($(element));
						if (righthashPlat[a] != undefined) {
							delete righthashPlat[a];
						}
						lefthashPlat[a] = a
								+ ","
								+ $(element).parent().find(
										"input[type='hidden']").val();
						$(element).removeClass("onGreen");
						$(element).removeAttr("onclick", "changeColor(this)")
								.attr("onclick", "changeColorNew(this)")
								.attr("ondblclick", "rightPriorityPlat(this)");
					} else {
						$(element).remove();
						delete righthashPlat[a];
						return;
					}
				}
			});
}
// 向右移动
function rightPriorityPlat(obj) {
	if ($(".bindingBox ul li").text() == "暂无数据") {
		$(".bindingBox ul").html("");
	}
	$("#seeNoBlingsDetailsPlat ul li").each(
			function(index, element) {
				if ($(element).hasClass("onGreen")) {
					var del = $(element).text();
					if (righthashPlat[del] != undefined) {
						$.fn.tipAlert('平台代码已存在已添加列表', 1.5, 0);
						return;
					}
					$("#seeAllBlingsDetailsPlat ul").prepend($(element));
					delete hashPlat[del];
					delete lefthashPlat[del];
					righthashPlat[del] = del;
					$(element).removeClass("onGreen");
					$(element).removeAttr("onclick", "changeColorNew(this)")
							.attr("onclick", "changeColor(this)").attr(
									"ondblclick", "leftPriorityPlat(this)");
				}
			});
}
// 确定
function addChoicePlat(dCode) {
	var platformCodes = '';
	var html = '';
	$("#seeAllBlingsDetailsPlat ul li").each(function(index, obj) {
		platformCodes += $(obj).find("input[type=hidden]").val() + ",";
	});
	platformCodes = platformCodes.substring(0, platformCodes.length - 1);
	if (platformCodes != "") {
		$.fn.tipLodding();
		$.ajax({
			url : WEB_SERVER_PATH + dCode+'/basesupport/account/ajax/bindPlatformCode',
			type : 'post',
			data : {
				userId : userId,
				platformCodes : platformCodes
			},
			dataType : 'json',
			success : function(data) {
				if (data.success) {
					$.fn.tipLoddingEnd(true);
					$.fn.tipAlert('绑定成功', 1, 1);
					$.fn.tipShut();
					window.location.reload();
				} else {
					$.fn.tipLoddingEnd(false);
					$.fn.tipAlert(data.message, 1.5, 0);
				}
			},
			error : function() {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert('系统异常', 1.5, 2);
			}
		});
	} else {
		$.fn.tipAlert('请添加平台代码！', 1.5, 0);
		return;
	}
}
/** ************************************************删除服务号*********************************************************************** */
function delServiceCode(obj,delcode, id) {
	var serviceCode = $(obj).parent().text();
	
	var addHtml = "";
	addHtml = "<div>确定要解绑服务号[" + serviceCode + "]?</div>";
	$.fn.tipOpen({
		title : "解绑确认",
		width : '300',
		height : '27',
		btn : [ {
			label : '确定',
			onClickFunction : 'del(\"' + id + '\", \"' + delcode + '\")'
		} ],
		concent : addHtml
	});
}
function del(id, delcode) {
	var userId = $("#modifInpVal").val();
	$.fn.tipLodding();
	$.ajax({
		url : WEB_SERVER_PATH +delcode+'/basesupport/account/ajax/deleteServiceCode',
		type : 'post',
		dataType : 'json',
		data : {
			userId : userId,
			serviceCodeId : id
		},
		success : function(data) {
			console.log(data)
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert("服务号解绑成功", 1.5, 1);
				window.location.reload();
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message, 3, 0);
			}
		},
		error : function() {
			$.fn.tipAlert('系统异常', 1.5, 2);
		}
	});
}
/** ************************************************删除平台*********************************************************************** */
function delServiceCodePlat(obj, dCode) {
	var platformCode = $(obj).parent().find("input[type='hidden']").val();// 绑定平台
	var addHtml = "";
	addHtml = "<div>确定要解绑平台[" + platformCode + "]?</div>";
	$.fn.tipOpen({
		title : "解绑确认",
		width : '300',
		height : '27',
		btn : [ {
			label : '确定',
			onClickFunction : 'delPlatformCode(\"' + platformCode + '\",\"' + dCode + '\")'
		} ],
		concent : addHtml
	});
}
function delPlatformCode(platformCode, dCode) {
	var userId = $("#modifInpVal").val();
	$.fn.tipLodding();
	$.ajax({
		url : WEB_SERVER_PATH + dCode + '/basesupport/account/ajax/deletePlatformCode',
		type : 'post',
		dataType : 'json',
		data : {
			userId : userId,
			platformCode : platformCode
		},
		success : function(data) {
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert("平台解绑成功", 1.5, 1);
				window.location.reload();
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message, 3, 0);
			}
		},
		error : function() {
			$.fn.tipAlert('系统异常', 1.5, 2);
		}
	});
}

// 取消
function addCancel(realname) {
	if (realname != null && realname != '') {
		goPage("/sys/client/administrate");
	} else {
		goPage("/sys/client/account");
	}
}

