function showItem(){
	var flag = false;
	if ($("#businessType").val() == "" && $("#saveType").val() == "" && $("#businessTypeId").val() == "") {
		flag = true;
	} else if ($("#businessType").val() != "" && $("#saveType").val() != "" && $("#businessTypeId").val() != ""){
		flag = true;
	} else {
		flag = false;
	}
	if (!flag) {
		$.fn.tipAlert('请统一选择业务类型，保存类型，内容类别',1.5,0);
	} else {
		window['emTableConfig'].ajaxConfig.data.serviceCodeId=$("#serviceCodeId").val();
		window['emTableConfig'].ajaxConfig.data.operationType=$("#operationType").val(); 
		window['emTableConfig'].ajaxConfig.data.startTime=$("#startTime").val(); 
		window['emTableConfig'].ajaxConfig.data.endTime=$("#endTime").val();
		window['emTableConfig'].ajaxConfig.data.businessType=$("#businessType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.saveType=$("#saveType option:selected").val();
		window['emTableConfig'].ajaxConfig.data.businessTypeId=$("#businessTypeId option:selected").val();
		emTable('emTableConfig');
		$("#serviceCodeId").val(window['emTableConfig'].ajaxConfig.data.serviceCodeId);
		$("#operationType").val(window['emTableConfig'].ajaxConfig.data.operationType);
		$("#startTime").val(window['emTableConfig'].ajaxConfig.data.startTime);
		$("#endTime").val(window['emTableConfig'].ajaxConfig.data.endTime);
		$("#businessType").val(window['emTableConfig'].ajaxConfig.data.businessType);
		$("#saveType").val(window['emTableConfig'].ajaxConfig.data.saveType); 
		$("#businessTypeId").val(window['emTableConfig'].ajaxConfig.data.businessTypeId);
	}
}
//充值扣费明细
var emTableConfig = {
		outerDivId : "outerId",
		pagesShow : true,
		totalNumbersShow:true,
		searchConfig : {
			searchItems : [ {
				isShow : true,
				label : '服务号ID',
				id : 'serviceCodeId',
				type : 'input'
			},{
				isShow : true,
				label : '操作类型',
				id : 'operationType',
				type : 'select',
				options : [ {
					context:'全部',
					value:''
				}, {
					context : '充值',
					value : '0'
				},{
					context : '扣费',
					value : '1'
				}, {
					context : '补款',
					value : '2'
				}]
			}, {
					label : '提交时间',
					id : 'startTime',
					type : 'date',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					startDate : '%y-%M-%d 00:00:00',
					maxDateId : 'endTime'
				}, {
					label : '至',
					id : 'endTime',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					startDate : '%y-%M-%d 23:59:59',
					type : 'date',
					minDateId : 'startTime'
				},{
				isShow : true,
				label : '业务类型',
				id : 'businessType',
				type :"include"
			},{
				isShow : true,
				label : '保存类型',
				id : 'saveType',
				type : 'include'
			},{
				isShow : true,
				label : '内容类型',
				id : 'businessTypeId',
				type :"include"
			}],
			 buttonItems : [{
					isShow: true,
					label : '查询',
					id : 'addBtn',
					onClickFunction : 'showItem()'
				}],
			searchButton : false,
			resetButton : false
		},
		ajaxConfig : {
			url : WEB_SERVER_PATH + '/fms/account/detail/ajax/list',
			method : 'POST',
			data : {
				serviceCodeId:'#serviceCodeId',
				operationType:'#operationType',
				saveType:'#saveType',
				businessType:'#businessType',
				businessTypeId:'#businessTypeId',
				startTime:'#startTime',
				endTime:'#endTime'
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
					title : "客户编号",
					width : "8%",
					context : "@{clientNumber}"
					},{
						isShow : true,
						title : "客户名称",
						width : "10%",
						align :'left',
						context : "<div class='ellipsBox'  style='width:200px;'><span class='ellips' style='width:200px;' title='@{nameCn}' >@{nameCn}</span></div>"
					},{
						isShow : true,
						title : "服务号ID",
						width : "8%",
						context : "@{serviceCodeId}"
					},{
						isShow : true,
						title : "业务类别",
						width : "8%",
						context :'@{businessType}'
					},{
						isShow : true,
						title : "保存类型",
						width : "8%",
						selectors : [ {
							isShow : true,
							term : "@{saveType}",
							select : [ {
								value : 1,
								context : "可保存"
							}, {
								value : 2,
								context : "不可保存"
							}]
						} ]
					},{
						isShow : true,
						title : "内容类别",
						width : "8%",
						context :'@{contentType}'
					},{
							isShow : true,
							title : "变动条数",
							width : "8%",
							align :'left',
							selectors : [ {
								isShow : true,
								term : "@{operationType}",
								select : [ {
									value : 0,
									context : "<span style='color:green;'>+@{changeNumber}</span>"
								}, {
									value : 1,
									context : "<span style='color:red;'>-@{changeNumber}</span>"
								
								},{
									value : 2,
									context : "<span style='color:green;'>+@{changeNumber}</span>"
								}]	
							}]
						},{
						isShow : true,
						title : "剩余条数",
						align :'left',
						width : "8%",
						context : "@{remainingNumber}"
					},{
						isShow : true,
						title : "操作类型",
						width : "8%",
						align :'left',
						selectors : [ {
							isShow : true,
							term : "@{operationType}",
							select : [ {
								value : 0,
								context : "充值"
							}, {
								value : 1,
								context : "扣费"
							}, {
								value : 2,
								context : "补款"
							}]
						} ]
					},{
						isShow : true,
						title : "备注",
						width : "8%",
						align :'left',
						context : "<div class='ellipsBox'  style='width:80px;'><span class='ellips' style='width:80px;' title='@{remark}'>@{remark}</span></div>"
					},{
						isShow : true,
						title : "操作人",
						align :'left',
						width : "8%",
						context : "@{userName}"
					},{
						isShow : true,
						title : "操作时间",
						width : "8%",
						context : "@{operationTime}"
					}]
		}
	}
emTable('emTableConfig');
