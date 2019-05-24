<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="context">
	<div id="outerId"></div>
	<select id="businessId" name="busiList" style="width:160px;">
		<option value="" >--请选择--</option>
		<c:forEach items="${list}" var="busiList">
			<option value="${busiList.id}">${busiList.name}</option>
		</c:forEach>
	</select>  
</temple:write>
<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/channel/manage.js"></script>