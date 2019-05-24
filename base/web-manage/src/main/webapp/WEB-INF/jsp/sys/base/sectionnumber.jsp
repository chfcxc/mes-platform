<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="head">
	<script>var downloadKeys;</script>
</temple:write>
 <temple:write property="context">
	<select id="provinceCode">
		<option value="">全部</option>
		<c:forEach items="${list}" var="a">
			<option value="${a.code}">${a.name}</option>
		</c:forEach>
	</select>
	<div id="outerId"></div>
</temple:write>
<%@ include file="../../temple/auth/authTemple.jsp"%>

<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/baseinfo/fullsectionnumber/fullsectionnumber.js"></script>