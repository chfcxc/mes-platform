<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="crumbs">
	> 查看人员
</temple:write>
<temple:write property="head">
	<script>
		var id=${depId};
		var parentId="${parentId}";
		var exedPath="${exedPath}";
		var selectPath="${selectPath}";
	</script>
</temple:write>
<temple:write property="context">
	<input type="text" id="variableName" placeholder="搜索用户名、姓名、手机号" />
	<div id="outerId"></div>
</temple:write>
<%@ include file="../../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/user/department/seeperson.js"></script>