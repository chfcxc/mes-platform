<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="crumbs">
	> 账号绑定
</temple:write>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/client/bindservice/bindInfo.css" type="text/css" />
	<script type="text/javascript">
		var userId='${userId}';
		var type='${type}';
	</script>
</temple:write>
<temple:write property="context">
	<div id="updateRolebox">
	<input type="hidden" id="username" value="${userName}"/>
		<form method="post" id="modifyRoleForm">
			<%-- <input  id="modifInpVal" type="hidden" />
			<input  id="modifInpValPlat" type="hidden" />
				<dl class="alert-cen">
					<dd class="authority">
						<div class="role-title role-on light_grey_bg smstitle" onclick="role(this)"><i></i><!-- 短信账号绑定 --><input type="hidden" />&nbsp;&nbsp;（${userName}）</div>
						<div class="greyLine"></div>
						<!-- 绑定平台 -->
						<c:if test="${type == 1}">
							<div class="role_box"  id="platFormCodeBox">
								<div class="role-add" onclick="showDatasPlat()">
									<div class="yellow_add fl"></div>
									<div class="serviceCode_bind fl"> 绑定平台 </div>
									<div class="clear"></div>
								</div>
								<div class="role-cen" >
									<div class="roleAdd"></div>
								</div>
							</div>
						</c:if>
						<!-- 绑定短信服务号 -->
						<div class="role_box" id="serviceCodeBox">
							<div class="role-add" id="role-add" onclick="showDatas(1)">
								<div class="yellow_add fl"></div>
								<div class="serviceCode_bind fl">绑定短信服务号</div>
								<div class="clear"></div>
							</div>
							<div class="role-cen" >
								<div class="roleAdd" id="smsroleAdd"></div>
							</div>
						</div>
					</dd>
				</dl>
			</c:if> --%>
		</form>
		<div class="tipFoot">
			<button onclick="addCancel('${realname}')" type="button" class="tipBtn tip-cancel">返 回</button>
		</div>
	</div>
</temple:write>

<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/client/sys/bindservice/servicebind.js"></script>