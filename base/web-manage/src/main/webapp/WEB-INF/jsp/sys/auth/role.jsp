<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>

<temple:write property="head">
	<script type="text/javascript">
		var SYSTEM_NOW = '${system}';
	</script>
</temple:write>


<temple:write property="context">
<ul class="card">
     <li id="card_MANAGE" onclick="tabContent(this,'MANAGE')">管理系统</li>
     <li id="card_CLIENT" onclick="tabContent(this,'CLIENT')">客户系统</li>
     <li id="card_OPERS" onclick="tabContent(this,'OPERS')">运维系统</li>
     <li id="card_SALES" onclick="tabContent(this,'SALES')">销售系统</li>
     <li id="card_AGENT" onclick="tabContent(this,'AGENT')">代理商系统</li>
 </ul>
<input id="typesss" type="hidden" value="${system}"/>
<div id="outerId"></div>
</temple:write>

<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/user/auth/treeauth.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/user/auth/role.js"></script>