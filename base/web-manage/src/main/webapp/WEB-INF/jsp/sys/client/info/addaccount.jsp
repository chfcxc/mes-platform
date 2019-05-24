<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="crumbs">
	> <a href="detail?id=${id}">客户账号</a>  > 创建子账号
</temple:write>
<temple:write property="head">
	 <link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/info/info.css" type="text/css" />
</temple:write>
<temple:write property="context">
<div class="wrapBox baseDetail">
	<input type="hidden" value="${id}" id="detailId"/>
	<input type="hidden" id="userId"/>
	<div class="childDetail">
		<div><label>客户名称：</label><span>${clientName}</span></div>
		<div>
			<label><span class="xing">*</span>用户名：</label><input type="text" id="userName" maxlength="16"/>
			<label><span class="xing">*</span>手机号：</label><input type="text" id="phone"/>
			<label><span class="xing">*</span>联系人：</label><input type="text" id="linkman" maxlength="10"/>
			<label>邮箱：</label><input type="text" id="email"/>
		</div>
	</div>
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
		<button type="button" class="tipBtn" id="saveBtn" onclick="ajaxAdd()">保 存</button>
		<%-- <button onclick="javascript:goPage('/sys/client/info/to/detail?id=${id}')" type="button" class="tipBtn tip-cancel">取 消</button> --%>
	</div>
</div>
<div class="wrapBox" id="wrapBox">
	<%-- <div class="sBox" id="smsbox" style="display:none;">
		<div class="accountInformation childAcc">	
			<div class="aTitle system-on" onclick="collect(this)"><span>关联短信服务号：</span><i></i></div>
			<div class="manageAccount">
				<div class="temple_content" id="smsServiceCode"></div>
			</div>
		</div>
		<div id="smsplatCode"></div>
		<c:if test="${type=='1'}">
			<div class="accountInformation childAcc">	
				<div class=""><span>关联平台代码：</span></div>
				<div class="manageAccount">
					<div class="temple_content relation" id="platformCode">
					<c:forEach items="${platformCodeList}" var ="platformCodeList">
						<label><input value="${platformCodeList.id}" type="checkbox">${platformCodeList.platformCode}</label>
					</c:forEach>
					</div>
				</div>
			</div>
		</c:if>
		<div class="tipFoot secand smsTipFoot">
			<button type="button" class="tipBtn" id="saveBtn" onclick="smsAjaxAdd()">保 存</button>
		</div>
	</div>
	<div class="fBox" id="flowbox" style="display:none;">
		<div class="accountInformation childAcc">	
			<div class="aTitle system-on" onclick="collect(this)"><span>关联流量服务号：</span><i></i></div>
			<div class="manageAccount">
				<div class="temple_content" id="flowServiceCode"></div>
			</div>
		</div>
		<div class="tipFoot secand flowTipFoot">
			<button type="button" class="tipBtn" id="saveBtn" onclick="flowAjaxAdd()">保 存</button>
			<button onclick="javascript:goPage('/sys/client/info/to/detail?id=${id}')" type="button" class="tipBtn tip-cancel">取 消</button>
		</div>
	</div>	
	<div class="fBox" id="imsbox" style="display:none;">
		<div class="accountInformation childAcc">	
			<div class="aTitle system-on" onclick="collect(this)"><span>关联国际短信服务号：</span><i></i></div>
			<div class="manageAccount">
				<div class="temple_content" id="imsServiceCode"></div>
			</div>
		</div>
		<div class="tipFoot secand flowTipFoot">
			<button type="button" class="tipBtn" id="saveBtn" onclick="imsAjaxAdd()">保 存</button>
			<button onclick="javascript:goPage('/sys/client/info/to/detail?id=${id}')" type="button" class="tipBtn tip-cancel">取 消</button>
		</div>
	</div>	
	<div class="fBox" id="voicebox" style="display:none;">
		<div class="accountInformation childAcc">	
			<div class="aTitle system-on" onclick="collect(this)"><span>关联语音服务号：</span><i></i></div>
			<div class="manageAccount">
				<div class="temple_content" id="voiceServiceCode"></div>
			</div>
		</div>
		<div class="tipFoot secand flowTipFoot">
			<button type="button" class="tipBtn" id="saveBtn" onclick="voiceAjaxAdd()">保 存</button>
		</div>
	</div> --%>
</div>
</temple:write>
<%@ include file="../../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/client/info/addaccount.js"></script>
