<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="context">
	<div id="outerId"></div>
	<select id="businessType" name="businessType" style="width: 160px;" onchange="chooseType()">
        <option value="">--请选择--</option>
		<c:forEach items="${map}" var="map">
			<option value="${map.key}">${map.key}</option>
		</c:forEach>
  	</select>
    <select id="saveType" name="saveType" onchange="chooseType()">
		<option value="">--请选择--</option>
     	<option value="1">可保存</option>
     	<option value="2">不可保存</option>
	</select>
    <select id="businessTypeId" name="businessTypeId">
    	<option value="">--请选择--</option>
		<c:forEach items="${map}" var="dataMap"> 
	        <c:forEach items="${dataMap.value}" var="value"> 
	        	<option style="display:none;" data-savetype="${value.saveType}" data-parentname="${value.parentName}" value="${value.id}">${value.name}</option>
	        </c:forEach>                 
    	</c:forEach>
  	</select>
</temple:write>
<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/client/servicecode/list.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/common/common.js"></script>
