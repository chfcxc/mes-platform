<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/clientAuth/clientAuth.css" type="text/css" />
	<script type="text/javascript">
		var authLevel = '${system}';
	</script>
</temple:write>
<temple:write property="context">
<ul class="card">
     <li id="card_base" class="li-on"  onclick="tabContent(this,'0')">基础版</li>
     <li id="card_pro" onclick="tabContent(this,'1')">专业版</li>
     <li id="card_premium" onclick="tabContent(this,'2')">高级版</li>
 </ul>
<input id="typesss" type="hidden" value="${system}" />
<div id="clientAuthBox"></div>
</temple:write>
<%@ include file="../../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/client/auth/treeauth.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/client/auth/list.js"></script>