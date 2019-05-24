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
	<script type="text/javascript">
		var WEB_SERVER_PATH = '${WEB_SERVER_PATH}';
		var WEB_STATIC_PATH = '${WEB_STATIC_PATH}';
		var PASSPORT_PATH = '${PASSPORT_PATH}';
 		var LOCAL_SYSTEM = '${LOCAL_SYSTEM}';
 		var DOMAIN_PATH = '${DOMAIN_PATH}';
	</script>
	<link type="image/x-icon" rel="shortcut icon" href="${WEB_STATIC_PATH}/img/favicon.ico">
	<link type="image/x-icon" rel="bookmark" href="${WEB_STATIC_PATH}/img/favicon.ico">
	<link type="image/x-icon" rel="icon" href="${WEB_STATIC_PATH}/img/favicon.ico">
	<link type="text/css" href="${WEB_STATIC_PATH}/css/manage/common.css" rel="stylesheet" />
	<link type="text/css" href="${WEB_STATIC_PATH}/css/manage/noauthtemple.css" rel="stylesheet" />
	<link type="text/css" href="${WEB_STATIC_PATH}/css/manage/validate.css" rel="stylesheet" />
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/jquery/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/cookie/jquery.cookie.js"></script>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/cookie/storage.js"></script>
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/Highcharts/highcharts.js"></script>	
	<temple:block property="head" />
	<title>${CURRENT_PAGE_NAME == null ? 'EUCP' : CURRENT_PAGE_NAME}</title>
</head>
<body>
    <div class="login_header">
        <div class="login_main">亿美 · 统一通信平台</div>
    </div>
    <div class="login_center">
	   <temple:block property="context" />
        <div class="clear"></div>
    </div>
    <div class="login_footer">
    	<p>Copyright©北京亿美软通科技有限公司</p>
    </div>
   	<script type="text/javascript">
	   	$(function(){
	   		$(".login_center").height($(window).height()-$(".login_header").height()-$(".login_footer").height());
	   	});
	</script>
</body>
</html>