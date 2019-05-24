<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="head">
<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/user/user.css" type="text/css" />
</temple:write>
<temple:write property="context">
	<div id="outerId"></div>
</temple:write>

<%@ include file="../../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/user/usermanage/usermanage.js"></script>