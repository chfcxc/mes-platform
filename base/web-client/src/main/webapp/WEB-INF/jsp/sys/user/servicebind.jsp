<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="crumbs">
	> 账号绑定
</temple:write>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/client/bindservice/bindInfo.css" type="text/css" />
	<script type="text/javascript">
		var userId='${userId}';
		var type='${type}';
	</script>
</temple:write>
<temple:write property="context">
	<div id="updateRolebox">
	<input type="hidden" id="username" value="${userName}"/>
		<form method="post" id="modifyRoleForm">	
		</form>
	</div>
	<div class="tipFoot">
			<button onclick="addCancel('${realname}')" type="button" class="tipBtn tip-cancel">返 回</button>
		</div>
</temple:write>

<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/client/sys/user/servicebind.js"></script>