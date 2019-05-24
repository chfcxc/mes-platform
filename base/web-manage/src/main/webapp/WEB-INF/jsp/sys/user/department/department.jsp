<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="head">
<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/department/department.css" type="text/css" />
<link rel="stylesheet" href="${WEB_STATIC_PATH}/lib/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script>
		var deptId='${depId}';
	</script>
</temple:write>
<temple:write property="context">
<div class="departBox">
	<div class="contactsLeft fl" >
		   <ul id="addGroupTree" class="ztree" ></ul>
	</div>
	<div class="departBox_R fl">
		<div class="temple_box">
		  <div id="outerId"></div>
	   	</div>
	</div>
	<input type="text" id="departmentName" placeholder="搜索部门" />
	<br />
	<input id="expantNodePathId"  type="hidden" value="${exedPath}" style="width: 350px;">
	<br />
	<input id="selecNodePathId"   type="hidden" value="${selectPath}" style="width: 350px;">
</div>
</temple:write>
<%@ include file="../../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/user/department/department.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/ztree/js/jquery.ztree.all.js"></script>
