$(function(){
	getItems('sysbasesection');
});
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
			label : '省份',
			id : 'provinceCode',
			type : 'include'
		},{
			isShow : true,
			label : '运营商',
			id : 'operatorCode',
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
			isShow:hasAuth("OPER_SYS_SECTION_ADD")==true,
			label : '新增',
			id : 'addBtn',
			onClickFunction : 'addNumber()'
		},{
			label : '下载导入模板',
			id : '',
			onClickFunction : 'downTemplate()'
		},{
			isShow: hasAuth("OPER_SYS_SECTION_ADD")==true,
			label : '导入',
			id : 'import',
			onClickFunction : 'showImport()'
		}],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/sys/base/section/list',
		method : 'POST',
		data : {
			number : '#number',
			operatorCode : '#operatorCode',
			provinceCode : '#provinceCode'
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
		rowItems : [{
					isShow : true,
					title : "号码段",
					width : "10%",
					context : "@{number}"
				},{
					isShow : true,
					title : "运营商",
					width : "10%",
					context : "@{operatorCode}"
				},{
					isShow : true,
					title : "省份",
					width : "10%",
					context : "@{provinceName}"
				},{
					isShow : true,
					title : "城市",
					width : "10%",
					context : "@{city}"
				},{
					isShow : hasAuths("OPER_SYS_SECTION_UPDATE","OPER_SYS_SECTION_DELETE")==true,
					title : "操作",
					width : "10%",
					selectors : [{
						    isShow:hasAuth("OPER_SYS_SECTION_UPDATE")==true,
							term : "@{id}",
							select : [ {
								value : "@{id}",
								context : "<div class='btn_box rs' alt='修改' title='修改' onClick='updateInfo(@{id})'><i class='iCon grey-write'></i></div>"
							} ]
						},{
							isShow:hasAuth("OPER_SYS_SECTION_DELETE")==true,
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
function addNumber(){
	 window.location.href=WEB_SERVER_PATH + '/sys/base/section/toadd';
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
		url : WEB_SERVER_PATH + '/sys/base/section/delete',
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
function updateInfo(id){
	window.location.href=WEB_SERVER_PATH + '/sys/base/section/toupdate?id='+id;
	setItems('sysbasesection');
}
//下载导入模板
function downTemplate(){
	window.open(WEB_STATIC_PATH+"/template/sectionnumber.xlsx","_self");
}
//显示导入模板box
function showImport(){
	var addHtml = "<input type='file' id='fileInput' name='fileInput'/>";
	$.fn.tipOpen({
		title : "导入号码段文件 ",
		width : '325',
		height: '80',
		btn : [{
			label : '导入',
			onClickFunction : 'importFile(this)'
		}],
		concent : addHtml
	});
}
//导入文件
function importFile(obj){
	var importFile = $('#fileInput').val();
	var fileId = 'fileInput';
	if($("#fileInput").val() === ''){
		fileId = 'null';
		$.fn.tipAlert('请选择上传的文件',1.5,2);
		return;
	}else if(!importFile.endWith('.xlsx')){
		$.fn.tipAlert('请上传.xlsx格式的文件',1.5,2);
		return;
	}
	$(obj).parents('.tipBorder').hide();
	$('.layer').remove();
	$('.tipLoadding').tipLog('open');
	$.ajaxFileUpload({
		url :  WEB_SERVER_PATH + '/sys/base/section/importFile',
		secureuri : false,
		fileElementId : fileId,
		dataType : 'json',
		success : function(data) {
			$('.tipLoadding').tipLog('close');
			var html  = '<div id="tipMsg"></div>';
				html += '<div class="tipFoot">';
				html += '<button id="determine" class="tipBtn" type="button" onclick="determineImport()">确定</button>';
				html += '<button id="download" class="tipBtn" type="button" onclick="download()">下载失败详情</button>';
				html += '</div>';
				
			$.fn.tipOpen({
				title : '导入状态',
				width : '300',
				height : '',
				cancel : false,
				tipClose : false,
				concent : html
			});
			if(data.success){
				var data= data.result;
				downloadKeys=data.downloadKey;
				var msg = '<label>导入总条数：</label><span>'+data.sum+'条</span><br/><label>导入成功条数：</label><span>'+data.success+'条</span><br/><label>导入失败条数：</label><a href="javascript:void(0)" >'+data.fail+'条</a>';
				if(data.fail==undefined){
					var msg = '<label>导入总条数：</label><span>'+data.sum+'条</span><br/><label>导入成功条数：</label><span>'+data.success+'条</span><br/><label>导入失败条数：</label><a href="javascript:void(0)" >'+0+'条</a>';
					data.fail=0;
				}
				if(data.fail==0){
					$("#download").css({"background":"#ccc","border-color":"#ccc"});
					$("#download").removeAttr("onclick");
				}
				
				$('#tipMsg').html( msg);
				$('#tipMsg').css("margin-top","10px").css("text-align","center");
				emrefulsh('emTableConfig');
			}else{
				$('#tipMsg').css("text-align","center").css("margin-top","20px");
				$("#download").css({"background":"#ccc","border-color":"#ccc"});
				$("#download").removeAttr("onclick");
				$('#tipMsg').html( data.message );
			}
		},
		error : function() {
			$.fn.tipAlert('error',1.5,2);
		}
	});
}
//下载
function download(){
	window.location.href=WEB_SERVER_PATH + '/sys/base/section/exportExcel?downloadKey='+downloadKeys;
}
//导入确定事件
function determineImport(){
	$('.layer,.tipBorder').remove();
}

function hasAuths(a,b){
	return (hasAuth(a)||hasAuth(b));
}