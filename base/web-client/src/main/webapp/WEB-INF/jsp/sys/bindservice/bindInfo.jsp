<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/client/bindservice/bindInfo.css" type="text/css" />
</temple:write>
<temple:write property="context">
	<input type="text" id="userName" placeholder="输入帐号查询"  />
	<div id="outerId"></div>
</temple:write>

<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/client/sys/bindservice/bindInfo.js"></script>