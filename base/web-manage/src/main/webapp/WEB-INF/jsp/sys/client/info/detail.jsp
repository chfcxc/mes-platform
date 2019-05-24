<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="crumbs">
	> 客户账号
</temple:write>
<temple:write property="head">
	 <link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/info/info.css" type="text/css" />
</temple:write>
<temple:write property="context">
<script>
		/* var enterpriseId='${enterpriseId}'; */
		var businessList='${businessList}';
</script>
<div class="wrapBox">
	<input type="hidden" value="" id="detailId"/>
	<input type="hidden" value="${enterpriseId}" id="enterpriseId"/>
	<input type="hidden" value="" id="manageAccountState"/>
	<h2><%-- <c:if test="${clientInfo.isVip == true}">【VIP】</c:if>  --%>客户名称：<span id="clientName"></span>（编号：<span id="clientNumber"></span>）</h2>
	<div class="accountInformation">
		<div class="aTitle">
			<span>账号信息</span>
			<%-- <temple:auth property="OPER_SYS_CLIENT_CREAT_ACCOUNT"><span class="createA" onclick="createAccount()">创建子账号</span></temple:auth> --%>
			<span class="createA" onclick="createAccount()">创建子账号</span>
		</div>
		<div class="manageAccount">
			<table class="emtable_table_table ">
				<thead>
					<tr>
						<th>用户名</th>
						<th>账号类型</th>
						<th>状态</th>
						<th>操作</th>
					    <%-- <temple:auth property="OPER_SYS_UPDATE__CLIENT_ACCOUNT"><th>操作</th></temple:auth> --%>
					</tr>
				</thead>
				<tbody id="manageAccountWrap"></tbody>
			</table>
		</div>
	
	</div>
<div id="smsFlowWrap">
	<!-- <div class="managingNumber" id="smsWrap">
	
	</div> -->
</div>
<div class="tipFoot">
		<button onclick="javascript:goPage('/sys/client/info')" type="button" class="tipBtn tip-cancel">返回</button>
	</div>
</div>
</temple:write>

<%@ include file="../../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/client/info/detail.js"></script>
