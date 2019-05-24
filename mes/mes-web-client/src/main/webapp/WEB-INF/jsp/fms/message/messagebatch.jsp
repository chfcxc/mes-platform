<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="context">
	<div id="outerId"></div>
	<select id="businessType" name="busiList" style="width:160px;" onchange="chooseType()">
		<option value="" >--请选择--</option>
		<c:forEach items="${busiList}" var="busiList">
			<option value="${busiList.id}" >${busiList.name}</option>
		</c:forEach>
	</select>
	 <select id="saveType" name="saveType" onchange="chooseType()">
		<option value="">--请选择--</option>
     	<option value="1">可保存</option>
     	<option value="2">不可保存</option>
	</select>
	<select id="contentType" name="contentList" style="width:160px;">
		<option value="" >--请选择--</option>
		<%-- <c:forEach items="${contentList}" var="contentList">
			<option value="${contentList.id}" >${contentList.name}</option>
		</c:forEach> --%>
	</select>
</temple:write>
<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/client/message/messagebatch.js"></script>
