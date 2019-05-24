<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="head">
	<title>EUCP-管理系统-首页</title>
	<link rel="stylesheet"
		href="${WEB_STATIC_PATH}/css/manage/index/index.css" type="text/css" />
</temple:write>


<temple:write property="context">
	<!--首页内容 begin-->
	<div class="home-box">
		<div class="module-content">
			<div class="home-module">
			<c:forEach items="${TOKEN.tree.children}" var="moudle">
				<c:if test="${moudle.resource.resourceFor == LOCAL_SYSTEM }">
					<div class="wrap">
						<div class="wrapA">
							<a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}${moudle.resource.businessType}${moudle.resource.resourcePath}">
								<div class="module-left">${moudle.resource.resourceName}</div>
							</a>
						</div>
						<div class="module-right">
							<ul>
							<!--公共服务-->
							<c:if test="${ moudle.resource.resourceCode == 'SYS' }">
							<c:forEach items="${moudle.getChildren()}" var="nav">
							<c:forEach items="${nav.getChildren()}" var="page">
								<c:if test="${ page.resource.resourceCode == 'SYS_CLIENT_INFO' }">
								<li><a href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
								</c:if>
								<c:if test="${ page.resource.resourceCode == 'SYS_CLIENT_ROLE' }">
								<li><a href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
								</c:if>
	
								<c:if test="${ page.resource.resourceCode == 'SYS_CLIENT_USER' }">
								<li><a href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
								<li style="height:24px;"></li><li style="height:24px;"></li>
								</c:if>
								<c:if test="${ page.resource.resourceCode == 'SYS_WARN_CONFIG' }">
								<li><a href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
								</c:if>
						<%-- 	<li class=" <c:if test="${ page.resource.resourceCode == 'SYS_CLIENT_INFO' }"></c:if>">
								<a href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a>
							</li> --%>
							<%-- <li class=" <c:if test="${ page.resource.resourceCode == SYS_CLIENT_ROLE }"></c:if>">
								<a href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a>
							</li>
							<li style="height:24px;"></li>
							<li style="height:24px;"></li>
							<li class=" <c:if test="${ page.resource.resourceCode == SYS_CLIENT_USER }"></c:if>">
								<a href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a>
							</li>
							<li class=" <c:if test="${ page.resource.resourceCode == SYS_WARN_CONFIG }"></c:if>">
								<a href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a>
							</li> --%>
							</c:forEach>
							</c:forEach>
								<!-- <li><a onclick="authJump('/sys/client/info','SYS_CLIENT_INFO')">客户信息</a></li>
								<li><a onclick="authJump('/sys/client/role','SYS_CLIENT_ROLE')">客户角色</a></li>
								<li style="height:24px;"></li>
								<li style="height:24px;"></li>
								<li><a onclick="authJump('/sys/client/user','SYS_CLIENT_USER')">客户用户</a></li>
								<li><a onclick="authJump('/sys/warn/config','SYS_WARN_CONFIG')">预警配置</a></li> -->
							</c:if>
							<!--短信服务-->
							<c:if test="${ moudle.resource.resourceCode == 'SMS' }">
							<c:forEach items="${moudle.getChildren()}" var="nav">
							<c:forEach items="${nav.getChildren()}" var="page">
							<c:if test="${ page.resource.resourceCode == 'SMS_SERVICECODE_MANAGE' }">
							<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}sms${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
							</c:if>
							<c:if test="${ page.resource.resourceCode == 'SMS_ES_MESSAGE_RESULT' }">
							<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}sms${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
							</c:if>
							<c:if test="${ page.resource.resourceCode == 'SMS_MESSAGE_AUDITING' }">
							<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}sms${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
							</c:if>
							<c:if test="${ page.resource.resourceCode == 'SMS_BASE_WHITELIST' }">
							<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}sms${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
							</c:if>
							<c:if test="${ page.resource.resourceCode == 'SMS_BASE_BLACKLIST' }">
							<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}sms${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
							</c:if>
							<c:if test="${ page.resource.resourceCode == 'SMS_MESSAGE_ESMO' }">
							<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}sms${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
							</c:if>
							<c:if test="${ page.resource.resourceCode == 'SMS_INTERCEPTION_INFO' }">
							<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}sms${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
							</c:if>
							<c:if test="${ page.resource.resourceCode == 'SMS_ACCOUNTS_REDE_RECORD' }">
							<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}sms${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
							</c:if>
							<c:if test="${ page.resource.resourceCode == 'SMS_BASE_REDLIST' }">
							<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}sms${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
							</c:if>
							<c:if test="${ page.resource.resourceCode == 'SMS_MESSAGE_TIMER' }">
							<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}sms${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
							</c:if>
							<c:if test="${ page.resource.resourceCode == 'SMS_CHANNEL_MONITOR' }">
							<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}sms${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
							</c:if>
							<c:if test="${ page.resource.resourceCode == 'SMS_ACCOUNTS_SMS_ESRECORD' }">
							<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}sms${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
							</c:if>
							</c:forEach>
							</c:forEach>
					          <!--   <li><a onclick="authJump('/sms/servicecode/manage','SMS_SERVICECODE_MANAGE')">服务号</a></li>
					            <li><a onclick="authJump('/sms/message/result','SMS_MESSAGE_RESULT')">短信查询</a></li>
					            <li><a onclick="authJump('/sms/message/auditing','SMS_MESSAGE_AUDITING')">短信审核</a></li>
					            <li><a onclick="authJump('/sms/base/whitelist','SMS_BASE_WHITELIST')">通道白名单</a></li>
					            <li><a onclick="authJump('/sms/base/blacklist','SMS_BASE_BLACKLIST')">黑名单</a></li>
					            <li><a onclick="authJump('/sms/message/mo','SMS_MESSAGE_MO')">上行查询</a></li>
					            <li><a onclick="authJump('/sms/interception/info','SMS_INTERCEPTION_INFO')">短信拦截</a></li>
								<li><a onclick="authJump('/sms/accounts/rederecord','SMS_ACCOUNTS_REDE_RECORD')">充值扣费明细</a></li>
								<li><a onclick="authJump('/sms/base/redlist','SMS_BASE_REDLIST')">红名单</a></li>
								<li><a onclick="authJump('/sms/message/timer','SMS_MESSAGE_TIMER')">定时短信</a></li>
								<li><a onclick="authJump('/sms/channel/monitor','SMS_CHANNEL_MONITOR')">通道监控</a></li>
								<li><a onclick="authJump('/sms/accounts/smsrecord','SMS_ACCOUNTS_SMS_RECORD')">短信扣费明细</a></li> -->
							</c:if>
							<!--流量服务-->
							<c:if test="${ moudle.resource.resourceCode == 'FLOW' }">
								<c:forEach items="${moudle.getChildren()}" var="nav">
									<c:forEach items="${nav.getChildren()}" var="page">
										<c:if test="${ page.resource.resourceCode == 'FLOW_SERVICECODE' }">
										<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}flow${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
										</c:if>
										<c:if test="${ page.resource.resourceCode == 'FLOW_CHANNEL_INFO' }">
										<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}flow${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
										</c:if>
									</c:forEach>
								</c:forEach>
							</c:if>
							<!--国际短信-->
							<c:if test="${ moudle.resource.resourceCode == 'IMS' }">
								<c:forEach items="${moudle.getChildren()}" var="nav">
									<c:forEach items="${nav.getChildren()}" var="page">
										<c:if test="${ page.resource.resourceCode == 'IMS_SERVICECODE' }">
										<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}ims${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
										</c:if>
										<c:if test="${ page.resource.resourceCode == 'IMS_CHANNEL_LIST' }">
										<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}ims${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
										</c:if>
									</c:forEach>
								</c:forEach>
							</c:if>
							<!--国际短信-->
							<c:if test="${ moudle.resource.resourceCode == 'VOICE' }">
								<c:forEach items="${moudle.getChildren()}" var="nav">
									<c:forEach items="${nav.getChildren()}" var="page">
										<c:if test="${ page.resource.resourceCode == 'VOICE_MANAGE_SERVICECODE' }">
										<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}voice${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
										</c:if>
										<c:if test="${ page.resource.resourceCode == 'VOICE_MANAGE_CHANNEL' }">
										<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}voice${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
										</c:if>
									</c:forEach>
								</c:forEach>
							</c:if>
							</ul>
						</div>
					
					</div>
				</c:if>	
			</c:forEach>
			</div>
			<div class="clear"></div>
			<!--彩信服务-->
			<!--<div class="home-module">
                <a href=""><div class="module-left fl">彩信服务</div></a>
                <div class="module-right fl rs">
                    <ul>
                        <li><a href="">注册号管理</a></li>
                        <li><a href="">充值扣费查询</a></li>
                        <li><a href="">通道管理</a></li>
                        <li><a href="">定时短信管理</a></li>
                        <li><a href="">短信审核</a></li>
                        <li><a href="">白名单</a></li>
                        <li><a href="">签名报备</a></li>
                        <li><a href="">黑名单</a></li>
                        <li><a href="">上行查询</a></li>
                        <li><a href="">发送查询</a></li>
                        <li><a href="">注册号管理</a></li>
                        <li><a href="">充值扣费查询</a></li>
                        <li><a href="">通道管理</a></li>
                        <li><a href="">定时短信管理</a></li>
                        <li><a href="">短信审核</a></li>
                        <li><a href="">白名单</a></li>
                        <li><a href="">签名报备</a></li>
                        <li><a href="">黑名单</a></li>
                        <li><a href="">上行查询</a></li>
                        <li><a href="">发送查询</a></li>
                    </ul>
                </div>
                 <div class="clear"></div>
            </div>-->
			<!--流量服务-->
			<!--<div class="home-module">
                <a href=""><div class="module-left fl">流量服务</div></a>
                <div class="module-right fl rs">
                    <ul>
                        <li><a href="">注册号管理</a></li>
                        <li><a href="">充值扣费查询</a></li>
                        <li><a href="">通道管理</a></li>
                        <li><a href="">定时短信管理</a></li>
                        <li><a href="">短信审核</a></li>
                        <li><a href="">白名单</a></li>
                        <li><a href="">签名报备</a></li>
                        <li><a href="">黑名单</a></li>
                        <li><a href="">上行查询</a></li>
                        <li><a href="">发送查询</a></li>
                    </ul>
                </div>
                 <div class="clear"></div>
            </div>-->
			<!--公共服务-->
<%-- 			<div class="home-module">
				<a href="sys/base/info"><div class="module-left fl">公共服务</div></a>
				<div class="module-right fl rs">
			<!-- 		<ul>
						<li><a href="sys/client/info">客户信息</a></li>
						<li><a href="sys/client/role">客户角色</a></li>
						<li><a href="sys/client/user">客户用户</a></li>
						<li><a href="sys/warn/config">预警配置</a></li>
					</ul> -->
				</div>
				<div class="clear"></div>
			</div>
			<div class="clear"></div> --%>
		</div>
	</div>
	<!--首页内容 end-->
</temple:write>

<%@ include file="../temple/auth/simpleAuthTemple.jsp"%>
<script type="text/javascript">
	$(function() {
		var centerHeight = $(window).height() - $('.header').height()
				- $('#footer').height() - 9;
		$(".center_box").css("min-height", centerHeight);
		
	})
	comments();
</script>


