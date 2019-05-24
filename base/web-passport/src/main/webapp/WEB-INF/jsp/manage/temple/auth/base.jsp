<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="temple" uri="/temple"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1" />
<link type="image/x-icon" rel="shortcut icon" href="${WEB_STATIC_PATH}/img/favicon.ico">
<link type="image/x-icon" rel="bookmark" href="${WEB_STATIC_PATH}/img/favicon.ico">
<link type="image/x-icon" rel="icon" href="${WEB_STATIC_PATH}/img/favicon.ico">
<link type="text/css" href="${WEB_STATIC_PATH}/css/manage/common.css" rel="stylesheet" />
<link type="text/css" href="${WEB_STATIC_PATH}/css/manage/validate.css" rel="stylesheet" />
<link type="text/css" href="${WEB_STATIC_PATH}/lib/tip/tipCss.css" rel="stylesheet" />
<link type="text/css" href="${WEB_STATIC_PATH}/lib/emTable/emTable.css" rel="stylesheet" />
<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/validate/validate.expand.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/validate/validate.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/tip/jQuery.tipbox.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/emTable/emTable.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/ajaxfileupload.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/date/WdatePicker.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/cookie/jquery.cookie.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/cookie/storage.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/Highcharts/highcharts.js"></script>
<link type="text/css" href="${WEB_STATIC_PATH}/lib/easyui/themes/default/easyui.css" rel="stylesheet" />
<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript">
	function hasAuth(authCode){
		if(AUTHS[authCode] == null || AUTHS[authCode] == ""){
			return false;
		}else{
			return AUTHS[authCode];
		}
	}
</script>