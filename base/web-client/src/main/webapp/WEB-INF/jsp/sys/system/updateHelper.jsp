<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="head">
	 <link rel="stylesheet" href="${WEB_STATIC_PATH}/css/client/system/updateHelper.css" type="text/css" /> 
</temple:write>
<temple:write property="context">
	<div class="wrapBox">
		<h2>系统更新记录</h2>
		<div id="outerId"></div>
	</div>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/js/client/sys/system/updateHelper.js"></script>
</temple:write>
<%@ include file="../../temple/auth/simpleAuthTemple.jsp"%>
