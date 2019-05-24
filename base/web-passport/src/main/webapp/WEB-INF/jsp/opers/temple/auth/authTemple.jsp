<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="temple" uri="/temple"%>
<!DOCTYPE HTML>
<html>
<head>
	<%@ include file="base.jsp"%>
</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="main">
		<div class="main_right">
			<%@ include file="crumbs.jsp"%>
			<div id="contextThis"></div>
			<%@ include file="footer.jsp"%>
		</div>
	</div>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/js/opers/systemDefault.js"></script>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/js/opers/authcommon.js"></script>
</body>
</html>