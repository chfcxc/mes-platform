<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="crumbs">
	> 查看帐号绑定
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
		<%-- <c:if test="${IS_OPEN_SMS == true}">
			<dl class="alert-cen">
				<dd class="authority">
					<input type="hidden" />
					<!-- 绑定平台 -->
					<c:if test="${type == 1}">
						<div class="role_box check_rolebox" id="platFormCodeBox">
							<div class="role-title light_grey_bg mb30">绑定平台</div>
							<div class="role-cen width99" >
								<div class="roleAdd"></div>
							</div>
						</div>
					</c:if>
					<div class="role_box check_rolebox" id="serviceCodeBox">
						<div class="role-title light_grey_bg mb30">绑定短信服务号</div>
						<div class="role-cen width99" >
							<div class="roleAdd"></div>
						</div>
					</div>
				</dd>
			</dl>
		</c:if>
		<c:if test="${IS_OPEN_FLOW == true}">
			<dl class="alert-cen">
					<dd class="authority">
						<div class="role-title light_grey_bg mb30">绑定流量服务号<input type="hidden" /></div>
						<div class="role_box" id="flowserviceCodeBox">
							<div class="role-cen" >
								<div class="roleAdd" id="flowroleAdd"></div>
							</div>
						</div>
					</dd>
			</dl>
		</c:if>
		<c:if test="${IS_OPEN_IMS == true}">
			<dl class="alert-cen">
					<dd class="authority">
						<div class="role-title light_grey_bg mb30">绑定国际短信服务号<input type="hidden" /></div>
						<div class="role_box" id="imsserviceCodeBox">
							<div class="role-cen" >
								<div class="roleAdd" id="imsroleAdd"></div>
							</div>
						</div>
					</dd>
			</dl>
		</c:if>
		<c:if test="${IS_OPEN_VOICE == true}">
			<dl class="alert-cen">
					<dd class="authority">
						<div class="role-title light_grey_bg mb30">绑定语音服务号<input type="hidden" /></div>
						<div class="role_box" id="voiceserviceCodeBox">
							<div class="role-cen" >
								<div class="roleAdd" id="voiceroleAdd"></div>
							</div>
						</div>
					</dd>
			</dl>
		</c:if>  --%>
	</div>
	 <div class="tipFoot tipFootMt30">
            <button onclick="javascript:goPage('/sys/client/account');" type="button" class="tipBtn tip-cancel">返 回</button>
       </div>
</temple:write>

<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/client/sys/bindservice/checkbind.js"></script>