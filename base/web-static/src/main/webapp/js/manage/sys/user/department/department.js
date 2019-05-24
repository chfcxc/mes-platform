$(function(){
	getItems('sysuserdepartment');
});
//加载树
var emTableConfig;
selectDept(deptId);
//showTree();
///////////////////////////////////////////////////////////////
function selectNode(id) {
	var treeObj = $.fn.zTree.getZTreeObj("addGroupTree");
	var node = treeObj.getNodeByParam("id", id, null);
	treeObj.selectNode(node);
}
function expendNodeByNodeId(id) {
	var treeObj = $.fn.zTree.getZTreeObj("addGroupTree");
	var node = treeObj.getNodeByParam("id", id, null);
	treeObj.expandNode(node, true, true, true);
}
function expendPath() {
	expendNodeByNodeId('0');
}

var setting = {
	view : {
		selectedMulti : false,
		showLine: false
	},
	async : {
		enable : true,
		url:  WEB_SERVER_PATH + "/sys/user/department/ajax/getDeptTree",
		autoParam : [ "id", "name=n", "level=lv" ],
		otherParam : {
			"otherParam" : "zTreeAsyncTest"
		}
	},
	callback : {
		beforeClick : beforeClick,
		beforeAsync : beforeAsync,
		onAsyncSuccess : onAsyncSuccess,
		onExpand : onExpandNode,
		beforeExpand: zTreeBeforeExpand
	}
};
function zTreeBeforeExpand(event, treeId, treeNode, msg){
}
function onExpandNode(event, treeId, treeNode, msg) {
	$('#selecNodePathId').val(''); //选择的结点
	nodeToPath(treeNode,'expantNodePathId');
}
function filter(treeId, parentNode, childNodes) {
	if (!childNodes)
		return null;
	for (var i = 0, l = childNodes.length; i < l; i++) {
		childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
	}
	return childNodes;
}
function beforeClick(treeId, treeNode) {
	$('#expantNodePathId').val(''); //选择的结点
	nodeToPath(treeNode,'selecNodePathId');
	selectDept(treeNode['id']);
}
function nodeToPath(treeNode,frmTxtid){
	var node = treeNode.getPath();
	var selectNodePathArray = new Array();
	for (var i = 0; i < node.length; i++) {
		selectNodePathArray.push(node[i]['id'])
	}
	$('#' + frmTxtid).val(selectNodePathArray.join(','));
}
function beforeAsync(treeId, treeNode) {
	return true;
}
function onAsyncSuccess(event, treeId, treeNode, msg) {
	var exedValue = $('#expantNodePathId').val(); //展开的结点
	var selectValue = $('#selecNodePathId').val(); //选择的结点
	var expandNodes = exedValue.split(',');
	var expendTarget = false;
	if ('' != selectValue) {
		expendTarget = true;  //是按那种方式展开的
		expandNodes = selectValue.split(','); //选择结点为空，就展开到上一次展开的结点
	}
	var level = 0;
	var currentExpNodeId = 0;
	if (treeNode != void (0)) {
		level = treeNode.level;
		var currentExpNodeId = expandNodes[level + 1];
		var childrens = treeNode.children;
		var carry = new Array();
		 for(var i =0; i<childrens.length; i++){
				var cnode = childrens[i];
				if(cnode['id'] ==currentExpNodeId ){
					var treeObj = $.fn.zTree.getZTreeObj("addGroupTree");
					//展开最后一个节点
					treeObj.expandNode(cnode, true, true, null);
					//选中最后一个节点
					if(currentExpNodeId == expandNodes[expandNodes.length - 1] ){
						treeObj.selectNode(cnode);
					}
				}
		}
	} else {
		expendNodeByNodeId(expandNodes[0]);
	}
}
$(document).ready(function() {
	$.fn.zTree.init($("#addGroupTree"), setting);
});
//重新加载树
function showTree(){
	var treeObj = $.fn.zTree.getZTreeObj("addGroupTree");
	treeObj.reAsyncChildNodes(null, "refresh");
}

function showTree1(){
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/user/department/ajax/getTree",
		type:'post',
		dataType:'json',
		success:function(data){
			$.fn.tipLoddingEnd(true);
			var treeResult = data.result;
			var html = '<dl><dt id="0" ><i class="treeI" ></i><span onclick="ajaxDrop1(0,this,1)">全部</span></dt></dl>';
			for(var i  in treeResult){
				var name = treeResult[i].departmentName;
				var id = treeResult[i].id;
				html += '<dl style="display:none;">';
				html += '<dd id="'+id+'"><i class="treeI" onclick="ajaxDrop('+id+',this,2)"></i><span  onclick="ajaxDrop1('+id+',this,2)">' + name +'</span><div></div></dd>';
				html += '</dl>';
			}
			$("#addGroupTree").html(html);
			$("#addGroupTree dl:eq(0) > dd").show();

			var parentID = $("#addGroupTree dl:eq(0) dt:eq(0)").attr('id');
		},
		error:function(){
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
//点击展开

function ajaxDrop(id,obj,listFalse){
	$("#addGroupTree").find("span").removeClass("red");
	$(obj).parent().siblings('dl').find('dd').hide();
	var parentObj = $(obj).parent();
	if(id==0){
	 return;
	}
	if($(parentObj).hasClass('turn')){
		$(parentObj).removeClass('turn');
		$(parentObj).find('div').hide();
		return;
	}else{
		$(parentObj).addClass('turn');
		$(parentObj).find('div').show();
	}
	if(listFalse == 2){
		$(obj).next().addClass('red');
	}
	if(listFalse == 1){
		if($(obj).hasClass('red')){
			$(obj).removeClass('red');
			$('#department').val('');
			$('#department').attr('_ID','');
		}else{
			$('#department').val($(obj).text());
			$('#department').attr('_ID',id);
			$('dt,span').removeClass('red');
			$(obj).addClass('red');
		}
	}
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/user/department/ajax/showChildrenNode",
		type:'post',
		data:{
			id : id
		},
		dataType:'json',
		success:function(data){
			var treeResult = data.result;
			var html ='';
			for(var i  in treeResult){
				var name = treeResult[i].departmentName;
				var id = treeResult[i].id;
				html += '<dl>';
				html += '<dd id="'+id+'"><i class="treeI" onclick="ajaxDrop('+id+',this,'+listFalse+')"></i><span onclick="ajaxDrop1('+id+',this,'+listFalse+')">'+name+'</span><div></div></dd>';
				html += '</dl>';
			}
			$(obj).next().next().html(html);

		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
function ajaxDrop1(id,obj,listFalse){
	if(id==0){
		if($(obj).hasClass('red')){
			$(obj).removeClass('red');
			$(obj).parent().parent().siblings('dl').hide();
		}else{
			$(obj).addClass('red');
			$(obj).parent().parent().siblings('dl').show();
		}
	}
	$("#addGroupTree").find("span").removeClass("red");
	$(obj).parent().siblings('dl').find('dd').hide();
	var parentObj = $(obj).parent();
	if($(parentObj).hasClass('turn')){
		$(parentObj).removeClass('turn');
		$(parentObj).find('div').hide();
		return;
	}else{
		$(parentObj).addClass('turn');
		$(parentObj).find('div').show();
	}
	if(listFalse == 2){
		$(obj).addClass('red');

	}
	if(listFalse == 1){
		if($(obj).hasClass('red')){
			$(obj).removeClass('red');
			$('#department').val('');
			$('#department').attr('_ID','');
		}else{
			$('#department').val($(obj).text());
			$('#department').attr('_ID',id);
			$('dt,span').removeClass('red');
			$(obj).addClass('red');
		}
	}

	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/user/department/ajax/showChildrenNode",
		type:'post',
		data:{
			id : id
		},
		dataType:'json',
		success:function(data){
			var treeResult = data.result;
			var html ='';
			for(var i  in treeResult){
				var name = treeResult[i].departmentName;
				var id = treeResult[i].id;
				html += '<dl>';
				html += '<dd id="'+id+'" ><i class="treeI" onclick="ajaxDrop('+id+',this,'+listFalse+')"></i><span onclick="ajaxDrop1('+id+',this,'+listFalse+')">'+name+'</span><div></div></dd>';
				html += '</dl>';

			}
			$(obj).next().html(html);

		},
		error:function(){
			$.fn.tipAlert('系统异常',1.5,0);
		}
	});
}
function departmen(){
	var id=0;
	$('.simSelect-cen').slideToggle(300);
	var treeHtml="";	
		$.ajax({
			url:  WEB_SERVER_PATH + "/sys/user/department/ajax/getTree?id="+id,
			type:'post',
			dataType:'json',
			success:function(data){					
				var treeResult = data.result;
				var treehtml="";
				for(var i  in treeResult){
					var id = treeResult[i].id;
					var name=treeResult[i].departmentName;
					treeHtml+='<dl>';
					treeHtml+='<dd><i class="treeI" onclick="ajaxDrop('+id+',this,1)"></i><span  onclick="ajaxDrop1('+id+',this,1)">' + name +'</span><div></div></dd>';
					treeHtml+='</dl>';
				}
				var html = '<div class="simSelect-text ">'+treeHtml+'</div>';
				html += '<div class="simSelect-foot"><button class="btn btn-blue" onclick="confirm(this)" type="button">确定</button></div>';

				$('.simSelect-cen').html(html);
			},
			error:function(){
				$.fn.tipAlert('系统异常',1.5,0);
			}
		});
}
function confirm(obj){
	var department = $("#department").val();
	$('.simSelect-cen').slideUp(300);
}
function chooseNode(id,obj){
	if($(obj).hasClass('red')){
		$(obj).removeClass('red');
		$('#department').val('');
		$('#department').attr('_ID','');
	}else{
		$('#department').val($(obj).text());
		$('#department').attr('_ID',id);
		$('dt,span').removeClass('red');
		$(obj).addClass('red');
	}
}

//新增部门
function addDepart(){
	var html='<form id="departForm">';
		html+='<dl class="alert-cen usertip">';
		html+='<dt class="item">';
		html+='<label><span class="xing">*</span>部门名称 :</label>';
		html+='<input type="text" maxlength="20" id="newDepartmentName" name="newDepartmentName" value="" style="width:200px;"/>';
		html+='</dt>';
		html+='<dt class="item">';
		html+='<label class="fl">上级部门 :</label>';
		html+='<div class="simSelect-title fl" onclick="departmen()" >';
		html+='<input type="text" style="width:200px;float:left;" id="department" name="department" value="" readonly="readonly"/>';
		html+='<span ><i></i></span>';
		html+='</div>';
		html+='</dt>';
		html+='<dt>';
		html+='<label class="fl"></label>';
		html+='<div class="simSelect-cen depar"></div>';
		html+='</dt>';
		html+='</dl>';
		html+='<div class="tipFoot">';
		html+='<button onclick="addDepartmenty()" type="submit" class="tipBtn" id="saveBtn">保 存</button>';
		html+='<button onclick="close()" type="button" class="tipBtn tip-cancel">取 消</button>';
		html+='</div>';
		html+='</form>';
		$.fn.tipOpen({
			title : "新建部门",//弹框标题
			width : '400',//弹框内容宽度
			btn : [],//按钮是否显示
			cancel:false,
			concent : html//弹框内容
		});

}
function close(){
	$(".tipBorder").remove();
	$(".layer").remove();
}

function addDepartmenty(){
	var $formCheckOut = $('#departForm');
   var validator = $formCheckOut.validate({
		rules:{
			newDepartmentName:{ //部门
				required:true,
				depName:true
			}
		},
		messages:{
			newDepartmentName:{
				required:"请填写部门名称"
			}
		},
	submitHandler: function() {
		   var parentId = $('#department').attr('_ID');
		   var depname=$('#newDepartmentName').val();
		   if(parentId==undefined){
			   parentId =0;
		   }
			$.fn.tipLodding();
			$.ajax({
				url:  WEB_SERVER_PATH + "/sys/user/department/ajax/add?randomdata="+Date.parse(new Date()),
				type:'post',
				data:{
					name : depname,
					parentId : parentId
				},
				dataType:'json',
				success:function(data){
					if(data.success){
						$.fn.tipLoddingEnd(true);
						$.fn.tipAlert('部门添加成功',1.5,1);
						var limit=$('.limitSelect').val();
        				emrefulsh("emTableConfig", 0, limit);
						showTree();
					}else{
						$.fn.tipLoddingEnd(false);
						validator.resetForm();
						$.fn.tipAlert(data.message,1.5,0);
					}
				},
				error:function(){
					$.fn.tipLoddingEnd(false);
					$.fn.tipAlert('系统异常',1.5,0);
				}
			})
	    }
     })
}
//修改部门
function modifyDepart(id){
	 $.ajax({
	 		url : WEB_SERVER_PATH + "/sys/user/department/ajax/detail",
	 		type : 'post',
	 		dataType : 'json',
	 		data : {
	 			departmentId : id
	 		},
	 		success : function(data) {
	 			if (data.success) {
	 				var departmentName=data.result.departmentName;
	 				var parentDepartment=data.result.parentDepartmentName;
	 				var id=data.result.id;
	 				var html='<form id="departFormM">';
	 				html+='<dl class="alert-cen usertip">';
	 				html+='<dt class="item">';
	 				html+='<label><span class="xing">*</span>部门名称 :</label>';
	 				html+='<input type="text" maxlength="20" id="newDepartmentNames" name="newDepartmentNames" value="'+departmentName+'" style="width:200px;"/>';
	 				html+='</dt>';
	 				html+='<dt class="item upitem">';
	 				html+='<label class="fl">上级部门 :</label>';
	 				html+='<div class="simSelect-title fl" >';
	 				html+='<input type="text" style="height:34px;width:200px;float:left;border:none;background:none;box-shadow: 0 1px 1px rgba(255,255,255,0) inset;" id="department" name="departments" value="'+parentDepartment+'" readonly="readonly" disabled="disabled"/>';
	 				html+='</div>';
	 				html+='</dt>';
	 				html+='<dt>';
	 				html+='<label class="fl"></label>';
	 				html+='<div class="simSelect-cen depar"></div>';
	 				html+='</dt>';
	 				html+='</dl>';
	 				html+='<div class="tipFoot">';
	 				html+='<button onclick="modifyDepartmenty('+id+')" type="submit" class="tipBtn" id="saveBtndep">保 存</button>';
	 				html+='<button onclick="close()" type="button" class="tipBtn tip-cancel">取 消</button>';
	 				html+='</div>';
	 				html+='</form>';
	 				$.fn.tipOpen({
	 					title : "修改部门",//弹框标题
	 					width : '400',//弹框内容宽度
	 					btn : [],//按钮是否显示
	 					cancel:false,
	 					concent : html//弹框内容
	 				});
	 				if(parentDepartment==""){
	 					$(".upitem").remove();
	 				}
	 			}else{
	 				$.fn.tipAlert(data.message, 1.5, 0);
	 			}
	 		},
	 		error:function(){
				$.fn.tipAlert('系统异常',1.5,0);
			}
		});

}
//修改部门 接口已改
function modifyDepartmenty(id){
	var $formCheckOut = $('#departFormM');
   var validator= $formCheckOut.validate({
		rules:{
			newDepartmentNames:{ //部门
				required:true,
				depName:true
			}
		},
		messages:{
			newDepartmentNames:{
				required:"请填写部门名称",
			}
		},
	submitHandler: function() {
			var ndname= $('#newDepartmentNames').val();
			$.fn.tipLodding();
			$.ajax({
				url:  WEB_SERVER_PATH + "/sys/user/department/ajax/modify?randomdata="+Date.parse(new Date()),
				type:'post',
				data:{
					name : ndname,
					id : id
				},
				dataType:'json',
				success:function(data){
					if(data.success){
						$.fn.tipLoddingEnd(true);
						$.fn.tipAlert('部门修改成功',1.5,1);
						var limit=$('.limitSelect').val();
        				emrefulsh("emTableConfig", 0, limit);
						showTree();
					}else{
						$.fn.tipLoddingEnd(false);
						$.fn.tipAlert(data.message, 1.5, 0);
						validator.resetForm();
					}
				},
				error:function(){
					$.fn.tipLoddingEnd(false);
					$.fn.tipAlert('系统异常',1.5,0);
				}
			})
	    }
     })
}
//删除部门
function delDepart(id,obj){
	var departname=$(obj).parent().parent().find("td:eq(1)").text();
	var addHtml ="";
	isShow:hasAuth("OPER_SYS_DEPARTMENT_DELETE")==true,
 	addHtml = "<div>确定要删除【"+departname+"】吗？</div>";
	$.fn.tipOpen({
			title : "删除确认",// 弹框标题
			width : '300',// 弹框内容宽度
			height : '27',
			concent :addHtml,//弹框内容
			btn : [ {
		 		label : '确定',
		 		onClickFunction : 'delDp('+id+',"'+departname+'")'
		 	}]
		});


}
function delDp(id,departname){
	$.fn.tipLodding();
	$.ajax({
		url:  WEB_SERVER_PATH + "/sys/user/department/ajax/delete",
		type:'post',
		dataType:'json',
		data:{
			id : id,
			departmentName : departname
		},
		success:function(data){
			if (data.success) {
				$.fn.tipLoddingEnd(true);
				$.fn.tipAlert('已删除',1,1);
				var limit=$('.limitSelect').val();
				emrefulsh("emTableConfig", 0, limit);
				showTree();
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

function selectDept(id){
	var temp = {
			outerDivId : "outerId",
			pagesShow : true, //true 显示具体的分页数
			totalNumbersShow:true,
			searchConfig : {
				searchItems : [{
					label : '',
					id : '',
					type : 'input'
				}, {
					label : '',
					id : 'departmentName',
					type : 'include'
				}],
				buttonItems : [{
					isShow:hasAuth("OPER_SYS_DEPARTMENT_ADD")==true,
					label : '新增',
					id : 'addBtn',
					onClickFunction : 'addDepart()'
				} ],
				searchButton : true,
				resetButton : false
			},
			ajaxConfig : {
				url:  WEB_SERVER_PATH + "/sys/user/department/ajax/find?id="+id,
				method : 'POST',
				data : {
					departmentName : "#departmentName"
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
								title : "部门名称",
								width : "10%",
								align :'left',
								context : "@{departmentName}"
							},{
								isShow : true,
								title : "上级部门",
								width : "10%",
								align :'left',
								context : "@{parentDepartmentName}"
							},{
								isShow : hasAuths("OPER_SYS_DEPARTMENT_DELETE","OPER_SYS_DEPARTMENT_UPDATE","OPER_SYS_DEPARTMENT_DETAIL"),
								width : "20%",
								title : "操作",
								selectors : [{
									isShow:hasAuth("OPER_SYS_DEPARTMENT_UPDATE")==true,
									term : "@{id}",
									select : [ {
										value : "@{id}",
										context : "<div onclick='modifyDepart(@{id})' title='编辑'  class='btn_box rs'><i class='iCon grey-write'></i></div>&nbsp;"
									} ]
								},{
									isShow:hasAuth("OPER_SYS_DEPARTMENT_DELETE")==true,
									term : "@{id}",
									select : [ {
										value : "@{id}",
										context : '<div onclick="delDepart(@{id},this)" title="删除"  class="btn_box rs"><i class="iCon grey-delete"></i></div>&nbsp;'
									} ]
								},{
									isShow:hasAuth("OPER_SYS_DEPARTMENT_DETAIL")==true,
									term : "@{id}",
									select : [ {
										value : "@{id}",
										context : '<div onclick="seePerson(@{id},@{getParentDepartmentId},this)" title="查看人员"  class="btn_box rs"><i class="iCon grey-seepeople"></i></div>&nbsp;'
									} ]
								} ]

					        }]
		 }
	}
	emTableConfig = temp;
	emTable('emTableConfig');

}
function seePerson(id,parentId){
	var exedPath = $('#expantNodePathId').val(); //展开的结点
	var selectPath = $('#selecNodePathId').val(); //选择的结点
	window.location.href=WEB_SERVER_PATH + '/sys/user/department/see?depId='+id+'&parentId='+parentId+'&exedPath=' +exedPath+'&selectPath='+selectPath;
	setItems('sysuserdepartment');
}
function hasAuths(a,b,c){
	return (hasAuth(a)||hasAuth(b)||hasAuth(c));
}