<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="crumbs">
	> 客户账号详情  > 查看子账号
</temple:write>
<temple:write property="head">
	 <link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/info/info.css" type="text/css" />
</temple:write>
 <script type="text/javascript">
		var parentId='${parentId}';
	</script>
<temple:write property="context">
<div class="wrapBox">
	<input type="hidden" value="${user.id}" id="detailId"/>
	<input type="hidden" value="${enterpriseId}" id="enterpriseId"/>
	<div class="childDetail">
		<div><label>客户名称：</label><span>${clientName}</span></div>
		<div>
			<label><span class="xing">*</span>用户名：</label><input type="text" id="userName" value="${user.username}" class="modifych" disabled="disabled"/>
			<label><span class="xing">*</span>手机号：</label><input type="text" id="phone" value="${user.mobile}" class="modifych" disabled="disabled"/>
			<label><span class="xing">*</span>联系人：</label><input type="text" id="linkman" maxlength="10" value="${user.realname}" class="modifych" disabled="disabled"/>
			<label>邮箱：</label><input type="text" id="email" value="${user.email}" class="modifych" disabled="disabled"/>
		</div>
	</div>
	<div class="accountInformation childAcc">	
		<div class="aTitle"><span>角色权限设置：</span></div>
		<div class="manageAccount">
			<div class="temple_content" id="roleId">
				<c:forEach items="${userRoleList}" var ="userRoleList">
					<label><input value="${userRoleList.roleId}"  type="checkbox" style="visibility: hidden;">${userRoleList.roleName}</label>
				</c:forEach>
			</div>
		</div>
	</div>
	<div id="allbox"></div>
		<%-- <div class="accountInformation childAcc" id="smsbox" style="display:none;">	
			<div class="aTitle"><span>关联短信服务号：</span></div>
			<div class="manageAccount">
				<div class="temple_content" id="smsServiceCode">
					<!-- <div class="sTitle"><span>短信服务</span></div> -->
					<c:forEach items="${userServiceCodeList}" var ="userServiceCodeList">
						<label><input value="${userServiceCodeList.id}" type="checkbox" style="visibility: hidden;">${userServiceCodeList.serviceCode}</label>
					</c:forEach>
				</div>
			</div>
		</div>
		<c:if test="${type=='1'}">
			<div class="accountInformation childAcc">	
				<div class=""><span>关联平台代码：</span></div>
				<div class="manageAccount">
					<div class="temple_content relation" id="platformCode">
					<c:forEach items="${smsPlatformCodelist}" var ="smsPlatformCodelist">
						<label><input value="${smsPlatformCodelist.id}" type="checkbox" style="visibility: hidden;">${smsPlatformCodelist.platformCode}</label>
					</c:forEach>
					</div>
				</div>
			</div>
		</c:if>
	<div class="accountInformation childAcc" id="flowbox" style="display:none;">	
		<div class="aTitle"><span>关联流量服务号：</span></div>
		<div class="manageAccount">
			<div class="temple_content" id="flowServiceCode">
				<!-- <div class="sTitle"><span>短信服务</span></div> -->
				<c:forEach items="${userServiceCodeList}" var ="userServiceCodeList">
					<label><input value="${userServiceCodeList.id}" type="checkbox" style="visibility: hidden;">${userServiceCodeList.serviceCode}</label>
				</c:forEach>
			</div>
		</div>
	</div>
	<div class="accountInformation childAcc" id="imsbox" style="display:none;">	
		<div class="aTitle"><span>国际短信服务号：</span></div>
		<div class="manageAccount">
			<div class="temple_content" id="imsServiceCode">
				<c:forEach items="${userServiceCodeList}" var ="userServiceCodeList">
					<label><input value="${userServiceCodeList.id}" type="checkbox" style="visibility: hidden;">${userServiceCodeList.serviceCode}</label>
				</c:forEach>
			</div>
		</div>
	</div>
	<div class="accountInformation childAcc" id="voicesbox" style="display:none;">	
		<div class="aTitle"><span>关联语音服务号：</span></div>
		<div class="manageAccount">
			<div class="temple_content" id="voiceServiceCode">
				<c:forEach items="${userServiceCodeList}" var ="userServiceCodeList">
					<label><input value="${userServiceCodeList.id}" type="checkbox" style="visibility: hidden;">${userServiceCodeList.serviceCode}</label>
				</c:forEach>
			</div>
		</div>
	</div> --%>
</div>
	<div class="tipFoot centerbtn">
		<button onclick="javascript:goPage('/sys/client/info/to/detail?id=${parentId}')" type="button" class="tipBtn tip-cancel">返 回</button>
	</div>
</temple:write>

<%@ include file="../../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/client/info/childyaccount.js"></script>
