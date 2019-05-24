<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="head">
	<%-- <link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/accounts/rede.css" type="text/css" /> --%>
	<script>
		var industry='${industry}';
		console.log(industry);
	</script>
</temple:write>
<temple:write property="context">
		<div id="outerId"></div>
</temple:write>

<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/industry.js"></script>