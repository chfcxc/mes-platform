var emTableConfig = {
	outerDivId : "outerId",
	pagesShow : true,
	totalNumbersShow:true,
	searchConfig : {
		searchItems : [{
			isShow : true,
			label : '通道名称',
			id : 'channelName',
			type : 'input'
		},{
			isShow : true,
			label : '通道号',
			id : 'channelNumber',
			type : 'input'
		},{
			isShow : true,
			label : '通道状态',
			id : 'state',
			type : 'select',
			options : [ {
				context:'--请选择--',
				value:''
			},{
				context : '启用',
				value : '1'
			}, {
				context : '停用',
				value : '0'
			}]
		},{
			isShow : true,
			label : '业务类型',
			id : 'businessId',
			type :"include"
		}],
		buttonItems : [ {
//			isShow : hasAuth("VOICE_MANAGE_BLACKDICTIONARY_ADD")==true,
			label : '新增',
			id : 'addBtn',
			onClickFunction : 'addParach()'
		}],
		searchButton : true,
		resetButton : false
	},
	ajaxConfig : {
		url : WEB_SERVER_PATH + '/fms/channel/manage/ajax/list',
		method : 'POST',
		data : {
			channelName : '#channelName', //通道名称
			channelNumber : '#channelNumber', //通道号
			state : '#state', //通道状态
			businessId : '#businessId'
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
		rowItems : [
			{
				isShow : true,
				title : "通道名",
				width : "7%",
				context : "@{channelName}"
			},{
				isShow : true,
				title : "通道号",
				width : "7%",
				context : "@{channelNumber}"
			},{
				isShow : true,
				title : "运营商",
				width : "8%",
				selectors : [ {
					isShow : true,
					term : "@{providers}",
					select : [ {
						value : '1',
						context : "移动"
					}, {
						value : '2',
						context : "联通"
					}, {
						value : '3',
						context : "电信"
					}, {
						value : '1,2',
						context : "移动，联通"
					}, {
						value : '2,3',
						context : "联通，电信"
					}, {
						value : '1,3',
						context : "移动，电信"
					}, {
						value : '1,2,3',
						context : "移动，联通，电信"
					}]
				} ]
			},{
				isShow : true,
				title : "通道状态",
				width : "5%",
				selectors : [ {
					isShow : true,
					term : "@{state}",
					select : [ {
						value : 0,
						context : "停用"
					}, {
						value : 1,
						context : "启用"
					}]
				} ]
			},{
				isShow : true,
				title : "业务类型",
				width : "10%",
				context : "@{businessType}"
			},{
				isShow : true,
				title : "保存类型",
				width : "6%",
				selectors : [ {
					isShow : true,
					term : "@{saveType}",
					select : [ {
						value : 1,
						context : "可以保存"
					}, {
						value : 2,
						context : "不可保存"
					}]
				} ]
			},{
				isShow : true,
				title : "内容类别",
				width : "10%",
				context : "@{contentType}"
			},{
				isShow : true,
				title : "支持信息类型",
				width : "6%",
				selectors : [ {
					isShow : true,
					term : "@{templateType}",
					select : [ {
						value : '0',
						context : "普通"
					}, {
						value : '1',
						context : "个性"
					}, {
						value : '0,1',
						context : "个性，普通"
					}]
				} ]
			},{
				isShow: true,
				width : "10%",
				title : "操作",
				selectors : [{
//                    isShow : hasAuth("VOICE_UPDATE_CHANNEL_STATE")==true,
                    term : "@{state}",
                    select : [{
                        value : 1,
                        context :
                        	 "<div class='btn_box rs' alt='停用' title='停用' onClick='disable(\"@{id}\",\"@{channelName}\",\"@{state}\")'><i class='iCon grey-disable'></i></div>&nbsp;"
                    } ]
                },{
//                	isShow : hasAuth("VOICE_OPERATION_CHANNEL")==true,
                    term : "@{state}",
                    select : [{
                        value : 1,
                        context :
                        	 "<div class='btn_box rs' alt='修改' title='修改' onClick='dismodify()'><i class='iCon grey-write'></i></div>&nbsp;"
                    } ]
                },{
//                    isShow : hasAuth("VOICE_UPDATE_CHANNEL_STATE")==true,
                    term : "@{state}",
                    select : [{
                        value : 0,
                        context :
                        	"<div class='btn_box rs' alt='启用' title='启用' onClick='disable(\"@{id}\",\"@{channelName}\",\"@{state}\")'><i class='iCon grey-enable'></i></div>&nbsp;" 
                    } ]
                },{
//                	isShow : hasAuth("VOICE_OPERATION_CHANNEL")==true,
                    term : "@{state}",
                    select : [{
                        value : 0,
                        context :
                        	 "<div class='btn_box rs' alt='修改' title='修改' onClick='modify(\"@{id}\")'><i class='iCon grey-write'></i></div>&nbsp;"
                    } ]
                }]	
			}]
	}
}
emTable('emTableConfig');
/*新增线路*/
function addParach(){
	window.location.href=WEB_SERVER_PATH + '/fms/channel/manage/to/newchannel';
}
/*修改线路*/
function modify(id){
	window.location.href=WEB_SERVER_PATH + '/fms/channel/manage/to/modify?id='+id;
}
function dismodify() {
	$.fn.tipAlert('启用状态不可修改', 1.5, 0);
}

/*启用 停用*/
function disable(id,channelName,state){
	if(state==1){
		var addHtml ="";
	 	addHtml = "<div>确定要停用通道【"+channelName+"】?</div>";
		$.fn.tipOpen({
				title : "停用确认",
				width : '300',
				height : '27',
				concent :addHtml,
				btn : [ {
			 		label : '确定',
			 		onClickFunction : 'disEnaboff(\"'+id+'\")'
			 	}]				
			});
	}else if(state==0){
		var addHtml ="";
	 	addHtml = "<div>确定要启用通道【"+channelName+"】?</div>";
		$.fn.tipOpen({
				title : "启用确认",
				width : '300',
				height : '27',
				concent :addHtml,
				btn : [ {
			 		label : '确定',
			 		onClickFunction : 'disEnabon(\"'+id+'\")'
			 	}]				
		});
	}
}
function disEnabon(id){
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/fms/channel/manage/ajax/on",
		type:'post',
		dataType:'json',
		data:{
			id : id 
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				if(data.message == 'success'){
					$.fn.tipAlert('启用成功',1,1);
				}
				var limit=$('.limitSelect').val();
	        	emrefulsh("emTableConfig", 0, limit);
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message, 1.5, 0);
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}

function disEnaboff(id){
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/fms/channel/manage/ajax/off",
		type:'post',
		dataType:'json',
		data:{
			id : id 
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				if(data.message == 'success'){
					$.fn.tipAlert('停用成功',1,1);
				}
				var limit=$('.limitSelect').val();
	        	emrefulsh("emTableConfig", 0, limit);
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message, 1.5, 0);
			}
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
