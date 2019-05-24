<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="head">
	 <link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/golable/export.css" type="text/css" /> 
</temple:write>
<temple:write property="context">
	<div class="wrapBox">
		<h2>我的导出</h2>
		<div id="outerId"></div>
	</div>
</temple:write>
<%@ include file="../temple/auth/simpleAuthTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/golable/export.js"></script>
