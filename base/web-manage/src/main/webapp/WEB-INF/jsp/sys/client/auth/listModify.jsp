<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/clientAuth/clientAuth.css" type="text/css" />
	<script type="text/javascript">
		var authName = '${authBindName.authName}';
		var authLevel = '${authBindName.authLevel}';
		var id = '${id}';
	</script>
</temple:write>
<temple:write property="context">
	<div class="role_div">
		<label><span class="xing">*</span>权限名称：</label>
		<input id="authName" type="text" value="${authBindName.authName}"  />
		<input id="typesss" type="hidden" value="${authBindName.authLevel}" />
	</div>

<div id="clientAuthBox"></div>
</temple:write>
<%@ include file="../../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/client/auth/treeauth.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/client/auth/listModify.js"></script>