<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/client/contact/contact.css" type="text/css" />
	<script>
		var groupType='${groupType}';
		var downloadKey;
	</script>
</temple:write>
<temple:write property="context">
<!-- 新建操作和搜索操作start -->
	<div class="contactsSearch">
		<div class="fl">
			<c:if test="${OPER_SYS_CLIENT_CONTACT_GROUP_ADD == true}">
				<div class="emay-search"><input type="button" id="adduser" onclick="adduserBox(0)" value="新建个人组"></div>
			</c:if>
			<c:if test="${OPER_SYS_CLIENT_CONTACT_GROUP_ADD == true}">
				<div class="emay-search"><input type="button" id="addDepart" onclick="adduserBox(1)" value="新建共享组"></div>
			</c:if>
			<c:if test="${OPER_SYS_CLIENT_CONTACT_ADD == true}">
				<div class="emay-search"><input type="button" id="addimg" onclick="addimgBox()" value="新建联系人"></div>
			</c:if>
		</div>
		<div class="fl searchBoxfr searchBoxfr_new emay-search">
			<div class="searchBox searchBox_new">
				<input type="text" id="params" placeholder='搜索联系人' />
				<i class="searchBoxImg" title='搜索' onclick="searchInfo()"></i>
			</div>
		</div>
	</div>
<!-- 新建操作和搜索操作end -->	
	<ul class="card">
		<li class="li-on"  groupType="0" onclick="tabContent(this,'0')">个人组</li>
		<li groupType="1" onclick="tabContent(this,'1')" >共享组</li>
	</ul>
	<input id="typeVal" type="hidden" value="${groupType}" />
	<!-- 个人组 -->
	<div class="departBox" style="display:block;">
		<div class="contactsLeft fl contactsTree" >
			<div id="addGroupTree" class="ztree" ></div>
		</div>
		<div class="departBox_R fl">
			<div class="temple_box">
				<div class="conStrip-box"></div>
				<div id="outerId"></div>
		   	</div>
		</div>
	</div>
	<!-- 共享组 -->
	<div class="departBox">
		<div class="contactsLeft fl contactsTree" >
			   <div id="addGroupTreeShare" class="ztree" ></div>
		</div>
		<div class="departBox_R fl">
			<div class="temple_box">
			  <div class="conStrip-box-share"></div>
			  <div id="outerIdShare"></div>
		   	</div>
		</div>
	</div>
</temple:write>
<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/client/sys/contact/contact.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/client/sys/contact/contactList.js"></script>
