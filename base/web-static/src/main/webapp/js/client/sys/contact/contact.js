var groupType='0';
$(function(){
	showTree();
	loadConcats(0,groupType,this); //默认加载全部树
	if(groupType == null || groupType== ''){
		groupType = 0;
	}
	tabContent(this,groupType);
});
function tabContent(obj,groupType){
	$(obj).addClass("li-on").siblings().removeClass("li-on");
	$(obj).parent().siblings('.departBox').hide().eq($(obj).index()).show();
	$("#typeVal").val(groupType);
	showTree();
	if(groupType==0){
		loadConcats(0,groupType,this); 
	}else if(groupType==1){
		loadConcatsTwo(0,groupType,this); 
	}
}
//加载树
function showTree(contactId){
	var groupType=$("#typeVal").val(); //个人和共享类型
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/client/contact/ajax/listGroup",
		type:'post',
		data:{
			groupType : groupType
		},
		dataType:'json',
		success:function(data){	
			var html="";
			var treeResult = data.result;
			if(groupType==0){
				html +='<ul><li class="treeli"><div class="editdiv" onclick="loadConcats(0,'+groupType+',this)">全部</div></li></ul>';
				for(var i  in treeResult){
					var groupName = treeResult[i].groupName;
					var id = treeResult[i].id;
					var isShow = treeResult[i].isShow; //isShow权限控制
					html +='<ul><li id="'+id+'" class="treeli"><div class="editdiv" _isShow ="'+isShow+'" onclick="loadConcats('+id+','+groupType+',this,'+isShow+')">'+groupName+'</div></li>';
					html +='</ul>';
				}
				$("#addGroupTree").html(html);
				if(contactId){
					$("#addGroupTree ul li div").removeClass("xing");
					$("#addGroupTree ul li").each(function(index,obj){
						if($(obj).attr("id") == contactId){
							$(obj).find("div").addClass("xing");
						}
					});
				}
			}else{
				html +='<ul><li class="treeli"><div class="editdiv" onclick="loadConcatsTwo(0,'+groupType+',this)">全部</div></li></ul>';
				for(var i  in treeResult){
					var groupName = treeResult[i].groupName;
					var id = treeResult[i].id;
					var isShow = treeResult[i].isShow; //isShow权限控制
					html +='<ul><li id="'+id+'" class="treeli"><div class="editdiv"  _isShow ="'+isShow+'" onclick="loadConcatsTwo('+id+','+groupType+',this,'+isShow+')">'+groupName+'</div></li>';
					html +='</ul>';
				}
				$("#addGroupTreeShare").html(html);
				if(contactId){
					$("#addGroupTreeShare ul li div").removeClass("xing");
					$("#addGroupTreeShare ul li").each(function(index,obj){
						if($(obj).attr("id") == contactId){
							$(obj).find("div").addClass("xing");
						}
					});
				}

			}
			
		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
var emTableConfig;
var emTableConfigTwo;
/*********************************************************个人组*************************************************************/
function loadConcats(id,groupType,obj,isShow){
	$("#params").val("");
	var temp = {
			outerDivId : "outerId",
			pagesShow : true, //true 显示具体的分页数
			totalNumbersShow:true,
			searchConfig : {
				searchItems : [],
				buttonItems : [],
				searchButton : false,
				resetButton : false
			},
			ajaxConfig : {
				url:  WEB_SERVER_PATH  + '/sys/client/contact/ajax/listContact?groupId='+id+'&groupType='+groupType,
				method : 'POST',
				async:false,
				data : {
					params : '#params'
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
				rowItems : [{	isShow : false,
								title : "所属组",
								width : "20%",
								context : "@{groupName}"
							},{
								isShow : true,
								title : "姓名",
								width : "20%",
								align :'left',
								context : "@{realName}"
							},{
								isShow : true,
								title : "手机号",
								width : "15%",
								context : "@{mobile}"
							},{
								isShow : true,
								title : "邮箱",
								width : "15%",
								context : "@{email}"
							},{
								isShow : true,
								title : "QQ",
								width : "15%",
								context : '@{qq}'
							},{
								width : "10%",
								title : "操作",
								selectors : [{
									isShow : hasAuth("OPER_SYS_CLIENT_CONTACT_MODIFY")==true,
									term : "@{id}",
									select : [ {
										value : "@{id}",
										context : "<div onclick='editVerb(\"@{assignId}\")' title='修改'  class='btn_box rs'><i class='iCon grey-write'></i></div>&nbsp;"
									} ]
								},{
									term : "@{id}",
									select : [ {
										value : "@{id}",
										context : '<div onclick="check(\'@{assignId}\')" title="查看"  class="btn_box rs"><i class="iCon grey-see"></i></div>&nbsp;'
									} ]
								},{
									isShow : hasAuth("OPER_SYS_CLIENT_CONTACT_DELETE")==true,
									term : "@{id}",
									select : [ {
										value : "@{id}",
										context : '<div onclick="deleteDepartments(\'@{assignId}\',\'@{realName}\')" title="删除"  class="btn_box rs"><i class="iCon grey-delete"></i></div>&nbsp;'
									} ]
								}]

					        }]
		 }
	}
	emTableConfig = temp;
	emTable('emTableConfig');
	$("#params").val("");
	$(obj).addClass("xing").parent().parent().siblings().find("div.editdiv").removeClass("xing");
	$(".conStrip-box").html("");
	var type = $('.card').find('.li-on').attr('groupType');
	var name = $(obj).html();
	var isEditHtml ='';
	isEditHtml+='<div class="conStrip-title">';
	isEditHtml+='<div class="fl" gId='+id+'  gtype='+type+' id="gDiv">';
	isEditHtml+='<i class="iCon lgtBlue-folder"></i>';
	if(id !=0){
		isEditHtml+='<input type="text" maxLength="20" onblur="keepName('+id+',this)" class="disabledInput" disabled="disabled" value="'+name+'" />';
	}else{
		isEditHtml+='<input type="text"  class="disabledInput" disabled="disabled" value="全部" />';
	}
	isEditHtml+='</div>';
	if(id !=0){
		isEditHtml+='<div class="conStrip-title-right">';
		if(hasAuth("OPER_SYS_CLIENT_CONTACT_GROUP_MODIFY") && isShow == true){
			isEditHtml+='<div onclick="editName(this)" title="修改" class="btn_box rs"><i class="iCon grey-write"></i></div>';
		}
		if(hasAuth("OPER_SYS_CLIENT_CONTACT_GROUP_DELETE") && isShow == true){
			isEditHtml+='<div onclick="deleteDepartment('+id+')" title="删除" class="btn_box rs"><i class="iCon grey-delete"></i></div>';
		}
		isEditHtml+='</div>';
	}
	isEditHtml+='</div>';
	isEditHtml+='<div class="conStrip-nav emtable_search_form">';
	var number = $("#outerId_page_div font").text();
	isEditHtml+='<div class="emay-search"><label>成员(<span id="num">'+number+'</span>)</label></div>';
	if(id!=0){
		isEditHtml+='<div class="emay-search">';
		if(hasAuth("OPER_SYS_CLIENT_CONTACT_ADD") && isShow == true){
			isEditHtml+='<input type="button" id="addBtn" value="添加成员" onclick="usermenberboxs('+id+')">';
		}
		isEditHtml+='</div>';
		isEditHtml+='<div class="emay-search">';
		if(hasAuth("OPER_SYS_CLIENT_CONTACT_ADD") && isShow == true){
			isEditHtml+='<input type="button" id="" value="下载导入模版" onclick="downTemplate()">';
		}
		isEditHtml+='</div>';
		isEditHtml+='<div class="emay-search">';
		if(hasAuth("OPER_SYS_CLIENT_CONTACT_ADD") && isShow == true){
			isEditHtml+='<input type="button" id="import" value="导入" onclick="showImport('+id+')">';
		}
		isEditHtml+='</div>';
		isEditHtml+='<div class="emay-search">';
		isEditHtml+='<input type="button" id="exportBtn" value="导出" onclick="exportExcel('+id+')">';
		isEditHtml+='</div>';
	}
	isEditHtml+='</div>';
	$(".conStrip-box").append(isEditHtml);
}
/**********************************************共享组******************************************************/
function loadConcatsTwo(id,groupType,obj,isShow){
	$("#params").val("");
	var temp = {
			outerDivId : "outerIdShare",
			pagesShow : true, //true 显示具体的分页数
			totalNumbersShow:true,
			searchConfig : {
				searchItems : [],
				buttonItems : [],
				searchButton : false,
				resetButton : false
			},
			ajaxConfig : {
				url:  WEB_SERVER_PATH  + '/sys/client/contact/ajax/listContact?groupId='+id+'&groupType='+groupType,
				method : 'POST',
				async:false,
				data : {
					params : '#params'
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
				rowItems : [{	isShow : false,
								title : "所属组",
								width : "20%",
								context : "@{groupName}"
							},{
								isShow : true,
								title : "姓名",
								width : "20%",
								align :'left',
								context : "@{realName}"
							},{
								isShow : true,
								title : "手机号",
								width : "15%",
								context : "@{mobile}"
							},{
								isShow : true,
								title : "邮箱",
								width : "15%",
								context : "@{email}"
							},{
								isShow : true,
								title : "QQ",
								width : "15%",
								context : '@{qq}'
							},{
								width : "10%",
								title : "操作",
								selectors : [{
									isShow : hasAuth("OPER_SYS_CLIENT_CONTACT_MODIFY") == true,
									term : "@{isShow}",
									select : [ {
										value : true,
										context : "<div onclick='editVerb(\"@{assignId}\")' title='修改'  class='btn_box rs'><i class='iCon grey-write'></i></div>&nbsp;"
									} ]
								},{
									term : "@{id}",
									select : [ {
										value : "@{id}",
										context : '<div onclick="check(\'@{assignId}\')" title="查看"  class="btn_box rs"><i class="iCon grey-see"></i></div>&nbsp;'
									} ]
								},{
									isShow : hasAuth("OPER_SYS_CLIENT_CONTACT_DELETE") == true,
									term : "@{isShow}",
									select : [ {
										value : true,
										context : '<div onclick="deleteDepartments(\'@{assignId}\',\'@{realName}\')" title="删除"  class="btn_box rs"><i class="iCon grey-delete"></i></div>&nbsp;'
									} ]
								}]

					        }]
		 }
	}
	emTableConfigTwo = temp;
	emTable('emTableConfigTwo');
	$("#params").val("");
	$(obj).addClass("xing").parent().parent().siblings().find("div.editdiv").removeClass("xing");
	$(".conStrip-box-share").html("");
	var type = $('.card').find('.li-on').attr('groupType');
	var name = $(obj).html();
	var isEditHtml ='';
	isEditHtml+='<div class="conStrip-title">';
	isEditHtml+='<div class="fl" gId='+id+'  gtype='+type+' id="gDiv">';
	isEditHtml+='<i class="iCon lgtBlue-folder"></i>';
	if(id !=0){
		isEditHtml+='<input type="text" maxLength="20" onblur="keepName('+id+',this)" class="disabledInput" disabled="disabled" value="'+name+'">';
	}else{
		isEditHtml+='<input type="text"  class="disabledInput" disabled="disabled" value="全部">';
	}
	isEditHtml+='</div>';
	if(id !=0){
		isEditHtml+='<div class="conStrip-title-right">';
		if(hasAuth("OPER_SYS_CLIENT_CONTACT_GROUP_MODIFY") && isShow == true){
			isEditHtml+='<div onclick="editName(this)" title="修改" class="btn_box rs"><i class="iCon grey-write"></i></div>';
		}
		if(hasAuth("OPER_SYS_CLIENT_CONTACT_GROUP_DELETE") && isShow == true){
			isEditHtml+='<div onclick="deleteDepartment('+id+')" title="删除" class="btn_box rs"><i class="iCon grey-delete"></i></div>';
		}
		isEditHtml+='</div>';
	}
	
	isEditHtml+='</div>';
	isEditHtml+='<div class="conStrip-nav emtable_search_form">';
	var number = $("#outerIdShare_page_div font").text();
	isEditHtml+='<div class="emay-search"><label>成员(<span id="numShare">'+number+'</span>)</label></div>';
	if(id!=0){
		isEditHtml+='<div class="emay-search">';
		if(hasAuth("OPER_SYS_CLIENT_CONTACT_ADD") && isShow == true){
			isEditHtml+='<input type="button" id="addBtn" value="添加成员" onclick="usermenberboxs('+id+')">';
		}
		isEditHtml+='</div>';
		isEditHtml+='<div class="emay-search">';
		if(hasAuth("OPER_SYS_CLIENT_CONTACT_ADD") && isShow == true){
			isEditHtml+='<input type="button" id="" value="下载导入模版" onclick="downTemplate()">';
		}
		isEditHtml+='</div>';
		isEditHtml+='<div class="emay-search">';
		if(hasAuth("OPER_SYS_CLIENT_CONTACT_ADD") && isShow == true){
			isEditHtml+='<input type="button" id="import" value="导入" onclick="showImport('+id+')">';
		}
		isEditHtml+='</div>';
		isEditHtml+='<div class="emay-search">';
		isEditHtml+='<input type="button" id="exportBtn" value="导出" onclick="exportExcel('+id+')">';
		isEditHtml+='</div>';
	}
	isEditHtml+='</div>';
	$(".conStrip-box-share").append(isEditHtml);
}
