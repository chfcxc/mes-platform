<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="temple" uri="/temple"%>

${simplehtml}


<script type="text/javascript" id="scriptFor">
	
	var WEB_SERVER_PATH = '${WEB_SERVER_PATH}';
	var WEB_STATIC_PATH = '${WEB_STATIC_PATH}';
	var PASSPORT_PATH = '${PASSPORT_PATH}';
	var LOCAL_SYSTEM = '${LOCAL_SYSTEM}';
	var DOMAIN_PATH = '${DOMAIN_PATH}';

	var CURRENT_USER_ID = '${CURRENT_USER_ID}';
	var CURRENT_USER_USERNAME = '${CURRENT_USER_USERNAME}';
	var CURRENT_USER_REALNAME = '${CURRENT_USER_REALNAME}';
	var CURRENT_MOUDLE_CODE = '${CURRENT_MOUDLE_CODE}';
	${AUTH_SCRIPT}
	var enterpriseId = '${enterpriseId}';

</script>

<div id="headFor">
	<temple:block property="head" />
</div>

<title id="titleFor">${CURRENT_PAGE_NAME == null ? 'EUCP' : CURRENT_PAGE_NAME}</title>

<div id="contextFor">
	<temple:block property="context" />
</div>
