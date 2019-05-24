<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="crumbs">
	> 客户账号详情  > 修改子账号
</temple:write>
<temple:write property="head">
	 <link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/info/info.css" type="text/css" />
	 <script type="text/javascript">
		var parentId='${parentId}';
	</script>
</temple:write>
<temple:write property="context">
<div class="wrapBox">
<input type="hidden" value="${enterpriseId}" id="enterpriseId"/>
	<div id="platformlist">
		<c:forEach items="${userPlatformCodeAssignMap.bindingList}" var ="userPlatformCodeList">
			<input type="hidden" value="${userPlatformCodeList.id}" />
		</c:forEach>
	</div>
	<div id="userAssignlist">
		<c:forEach items="${userServiceCodeAssignMap.bindingList}" var ="userAssignList">
			<input type="hidden" value="${userAssignList.id}" />
		</c:forEach>
	</div>
	<div id="userRoleAssignlist">
		<c:forEach items="${userRoleAssignList}" var ="userRoleAssignList">
			<input type="hidden" value="${userRoleAssignList.roleId}" />
		</c:forEach>
	</div>
	<input type="hidden" value="${userId}" id="detailId"/>
	<input type="hidden" value="${parentId}" id="parentId"/>
	<div class="childDetail">
		<div><label>客户名称：</label><span>${clientName}</span></div>
		<div>
			<label><span class="xing">*</span>用户名：</label><input type="text" id="userName" value="${user.username}" class="modifych" disabled="disabled"/>
			<label><span class="xing">*</span>手机号：</label><input type="text" id="phone" value="${user.mobile}"/>
			<label><span class="xing">*</span>联系人：</label><input type="text" id="linkman" maxlength="10" value="${user.realname}"/>
			<label>邮箱：</label><input type="text" id="email" value="${user.email}"/>
		</div>
	</div>
	<div class="sBox">
		
		<div class="accountInformation childAcc">	
			<div class="aTitle"><span>角色权限设置：</span></div>
			<div class="manageAccount">
				<div class="temple_content" id="roleId">
					<c:forEach items="${roleList}" var ="roleList">
						<label title="${roleList.roleName}"><input value="${roleList.id}"  type="checkbox">${roleList.roleName}</label>
					</c:forEach>
				</div>
			</div>
		</div>
		<div class="tipFoot">
			<button type="button" class="tipBtn" id="saveBtn" onclick="ajaxModify()">更新</button>
			<%-- <button onclick="javascript:goPage('/sys/client/info/to/detail?id=${parentId}')" type="button" class="tipBtn tip-cancel">取 消</button> --%>
		</div>
		<div id="allbox"></div>
		<%-- <div class="fBox" id="smsbox" style="display:none;">
		<div class="accountInformation childAcc">
			<div class="aTitle"><!-- <span>关联短信服务号：</span> --></div>
			<div class="manageAccount">
				<div class="temple_content" id="smsServiceCode"></div>
			</div>
		</div>
		<c:if test="${type=='1'}">
			<div class="accountInformation childAcc">	
				<div class=""><span>关联平台代码：</span></div>
				<div class="manageAccount">
					<div class="temple_content relation" id="platformCode">
					<c:forEach items="${userPlatformCodeAssignMap.allEffectiveList}" var ="platformCode">
						<label><input value="${platformCode.id}" type="checkbox">${platformCode.platformCode}</label>
					</c:forEach>
					</div>
				</div>
			</div>
		</c:if>
		<div class="tipFoot smsTipFoot">
			<button type="button" class="tipBtn" id="saveBtn" onclick="smsAjaxModify()">更新</button>
		</div>
		</div>
	</div>
	<div class="fBox" id="flowbox" style="display:none;">
		<div class="accountInformation childAcc">	
			<div class="aTitle"><span>关联流量服务号：</span></div>
			<div class="manageAccount">
				<div class="temple_content" id="flowServiceCode"></div>
			</div>
		</div>
		<div class="tipFoot flowTipFoot">
			<button type="button" class="tipBtn" id="saveBtn" onclick="flowAjaxModify()">更新</button>
			<button onclick="javascript:goPage('/sys/client/info/to/detail?id=${id}')" type="button" class="tipBtn tip-cancel">取 消</button>
		</div>
	</div>
	<div class="fBox" id="imsbox" style="display:none;">
		<div class="accountInformation childAcc">	
			<div class="aTitle"><span>关联国际短信服务号：</span></div>
			<div class="manageAccount">
				<div class="temple_content" id="imsServiceCode"></div>
			</div>
		</div>
		<div class="tipFoot flowTipFoot">
			<button type="button" class="tipBtn" id="saveBtn" onclick="imsAjaxModify()">更新</button>
			<button onclick="javascript:goPage('/sys/client/info/to/detail?id=${id}')" type="button" class="tipBtn tip-cancel">取 消</button>
		</div>
	</div>
	<div class="fBox" id="voicebox" style="display:none;">
		<div class="accountInformation childAcc">	
			<div class="aTitle"><span>关联语音服务号：</span></div>
			<div class="manageAccount">
				<div class="temple_content" id="voiceServiceCode"></div>
			</div>
		</div>
		<div class="tipFoot flowTipFoot">
			<button type="button" class="tipBtn" id="saveBtn" onclick="voiceAjaxModify()">更新</button>
			<button onclick="javascript:goPage('/sys/client/info/to/detail?id=${id}')" type="button" class="tipBtn tip-cancel">取 消</button>
		</div>
	</div>--%> 
</div>
	<div class="tipFoot flowTipFoot centerbtn">
			<!-- <button type="button" class="tipBtn" id="saveBtn" onclick="flowAjaxModify()"></button> -->
			<button onclick="javascript:goPage('/sys/client/info/to/detail?id=${parentId}')" type="button" class="tipBtn tip-cancel">返回</button>
	</div>
</div>
</temple:write>

<%@ include file="../../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/client/info/modifyaccount.js"></script>
