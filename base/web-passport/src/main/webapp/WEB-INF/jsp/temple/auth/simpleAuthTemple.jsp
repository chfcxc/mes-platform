<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="temple" uri="/temple"%>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<c:set var="WEB_STATIC_PATH" value="${WEB_SERVER_PATH}"></c:set>
	<link type="image/x-icon" rel="shortcut icon" href="${WEB_STATIC_PATH}/img/client/favicon.ico">
	<link type="image/x-icon" rel="bookmark" href="${WEB_STATIC_PATH}/img/client/favicon.ico">
	<link type="image/x-icon" rel="icon" href="${WEB_STATIC_PATH}/img/client/favicon.ico">
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/jquery/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/js/authcommon.js"></script>
	<link type="text/css" href="${WEB_STATIC_PATH}/css/client/common.css" rel="stylesheet" />
	<link type="text/css" href="${WEB_STATIC_PATH}/css/client/validate.css" rel="stylesheet" />
	<link type="text/css" href="${WEB_STATIC_PATH}/css/client/tipCss.css" rel="stylesheet" />
	<link type="text/css" href="${WEB_STATIC_PATH}/css/client/emTable.css" rel="stylesheet" />
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/validate/jquery.validate.min.js"></script>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/validate/validate.expand.js"></script>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/validate/validate.js"></script>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/tip/jQuery.tipbox.js"></script>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/date/WdatePicker.js"></script>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/emTable/emTable.js"></script>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/cookie/jquery.cookie.js"></script>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/cookie/storage.js"></script>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/md5.js"></script>
	<link type="text/css" href="${WEB_STATIC_PATH}/lib/easyui/themes/default/easyui.css" rel="stylesheet" />
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript">
		var WEB_SERVER_PATH = '${WEB_SERVER_PATH}';
		var WEB_STATIC_PATH = '${WEB_STATIC_PATH}';

		var CURRENT_USER_ID = '${CURRENT_USER_ID}';
		var CURRENT_USER_USERNAME = '${CURRENT_USER_USERNAME}';
		var CURRENT_USER_REALNAME = '${CURRENT_USER_REALNAME}';
		
		${AUTH_SCRIPT}
		function hasAuth(authCode){
			if(AUTHS[authCode] == null){
				return false;
			}else{
				return true;
			}
		}
	</script>
	<temple:block property="head" />
</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="main">
		<temple:block property="context" />
	</div>
	<%@ include file="footer.jsp"%>
</body>
</html>