<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="head">
	<title>EUCP-首页</title>
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/client/index/index.css" type="text/css" />
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/client/tipCss.css" type="text/css" />
</temple:write>
<temple:write property="context">
<div class="indexWrap">
	<div class="oneWrap clear">
		<div class="wrapBox account">
			<h2>账号信息</h2>
			<div class="accountBox clear" >
				<div class="userimg"></div>
				<ul>
					<c:choose>
						<c:when test="${clientUserIndexDTO.identity==1}">
							<li><span>${clientUserIndexDTO.username}<label>(管理账号)</label></span><!-- <a href="javascript:void(0);" onclick="javascript:goPage('/sys/client/userinfo');" authCode="jisis">修改</a> --></li>
						</c:when>
						<c:otherwise>
							<li><span>${clientUserIndexDTO.username}<label>(普通账号)</label></span><!-- <a href="javascript:void(0);" onclick="javascript:goPage('/sys/client/userinfo');" authCode="jisis">修改</a> --></li>
						</c:otherwise>
					</c:choose>
					<li><label>姓名：</label><input type="text" value="${clientUserIndexDTO.realname}" disabled="disabled"/></li>
					<li><label>手机号：</label><input type="text" value="${clientUserIndexDTO.mobile}" disabled="disabled"/></li>
				</ul>
			</div>
		</div>
		<div class="wrapBox enterprise">
		    <h2>企业信息</h2>
		    <ul>
				<%-- <li><label>企业名称</label><input type="text" value="${clientUserIndexDTO.enterpriseName}" disabled="disabled"/></li> --%>
				<li class="companyName"><label>企业名称</label><textarea disabled="disabled">${clientUserIndexDTO.enterpriseName}</textarea></li>
				<li><label>已开服务</label><input type="text"  disabled="disabled" id="openService"/></li>
				<c:choose>
					<c:when test="${clientUserIndexDTO.identity==1}">
						<li class="childlabel"><label>子账号</label><input type="text" value="${clientUserIndexDTO.childCount}个" disabled="disabled"/><span>(限50个)</span></li>
					</c:when>
					<c:otherwise>
						<li class="childlabel"></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
		<div class="wrapBox sendProfile">
			<!-- <input id="smsCount" type="hidden" /> 
			<input id="flowCount" type="hidden" value=""/>
			<input id="imsCount" type="hidden" value="" />
			<input id="voiceCount" type="hidden" value=""/> -->
			<h2>发送概况（当月）</h2>
			<ul>
				
			</ul>
		</div>
	</div>
	
	<div class="wrapBox clear allwrap" >
	    <input type="hidden" id="serviceModules" value="${clientUserIndexDTO.serviceModules}"/>
		<h2>产品服务</h2>
		<ul class="productService clear">
			<c:forEach items="${clientUserIndexDTO.serviceModulesList}" var ="sModulesList">
				<li class="notOpen"><a href="javascript:void(0);" onmouseover="addstyle(this)" onmouseout="removestyle(this)" ID="${sModulesList.id}"  state="${sModulesList.state}" name="${sModulesList.serviceModuleName}"><em>${sModulesList.serviceModuleName}</em><i></i><span>未开通</span></a></li>
			</c:forEach>
			<!-- <li class="notOpen"><a href="javascript:void(0);"  ID="0" onmouseover="addstyle(this)" onmouseout="removestyle(this)"><em>短信服务</em><i></i><span>未开通</span></a></li>
		    <li class="notOpen"><a href="javascript:void(0);" ID="1" onmouseover="addstyle(this)" onmouseout="removestyle(this)"><em>流量服务</em><i></i><span>未开通</span></a></li>
		    <li class="notOpen"><a href="javascript:void(0);" ID="2" onmouseover="addstyle(this)" onmouseout="removestyle(this)"><em>彩信服务</em><i></i><span>未开通</span></a></li>		     -->
		</ul>
	</div>
	<c:choose>
		<c:when test="${clientUserIndexDTO.identity==1}">
			<div class="wrapBox clear allwrap">
				<h2>子账号</h2>
				<a href="javascript:void(0);" class="moreChild" onclick="javascript:goPage('/sys/client/administrate');">更多子账号</a>
				<ul class="childAccount clear">
					<c:forEach items="${clientUserIndexDTO.childList}" var ="childList">
						<li>			
							<div><label>用户名</label><input type="text" value="${childList.username}" disabled="disabled"/></div>
							<div><label>姓名</label><input type="text" value="${childList.realname}" disabled="disabled"/></div>
							<div><label>手机号</label><input type="text" value="${childList.mobile}" disabled="disabled"/></div>
						</li>
					</c:forEach>
				</ul>
			</div>
		</c:when>
		<c:otherwise></c:otherwise>
	</c:choose>
	<div id="allservicebox"></div>
	<input type="hidden" id="hrefPath" value="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}"/>
	<%-- <div class="wrapBox clear allwrap"  id="smsservicebox" style="display:none">
		<h2>短信服务号</h2><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}sms/sys/client/servicecode" class="bindService" >查看更多</a>
		<ul class="serviceNumber clear" id="smsserviceNumber">
			
		</ul>
	</div>	 --%>
	<%-- <div class="wrapBox clear allwrap"  id="flowservicebox" style="display:none">
		<h2>流量服务号</h2><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}flow/flow/servicecode" class="bindService" >查看更多</a>
		<ul class="serviceNumber clear" id="flowserviceNumber">
				
		</ul>
	</div>
	<div class="wrapBox clear allwrap"  id="imsservicebox" style="display:none">
		<h2>国际短信服务号</h2><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}ims/ims/client/servicecode" class="bindService" >查看更多</a>
		<ul class="serviceNumber clear" id="imsserviceNumber">
				
		</ul>
	</div>
	<div class="wrapBox clear allwrap"  id="voiceservicebox" style="display:none">
		<h2>语音服务号</h2><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}voice/voice/client/servicecode" class="bindService" >查看更多</a>
		<ul class="serviceNumber clear" id="voiceserviceNumber">
		</ul>
	</div> --%>
	<div id="smsservice"></div>
	<div id="flowservice"></div>
	<div id="imsservice"></div>
	<div id="voiceservice"></div>
</div>

</temple:write>
<%@ include file="../temple/auth/simpleAuthTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/client/golable/index.js"></script>

