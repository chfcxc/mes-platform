<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="head">
	<title>EUCP-销售系统-首页</title>
	<link rel="stylesheet"
		href="${WEB_STATIC_PATH}/css/sale/index/index.css" type="text/css" />
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
									<c:if test="${ moudle.resource.resourceCode == 'SYSSALES' }">
										<c:forEach items="${moudle.getChildren()}" var="nav">
											<c:forEach items="${nav.getChildren()}" var="page">
												<c:if
													test="${ page.resource.resourceCode == 'SYS_CLIENT_INFO' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
												</c:if>
												<c:if
													test="${ page.resource.resourceCode == 'SYS_CLIENT_ROLE' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
												</c:if>

												<c:if
													test="${ page.resource.resourceCode == 'SYS_CLIENT_USER' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
													<li style="height: 24px;"></li>
													<li style="height: 24px;"></li>
												</c:if>
												<c:if
													test="${ page.resource.resourceCode == 'SYS_WARN_CONFIG' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
												</c:if>
											</c:forEach>
										</c:forEach>
									</c:if>
									<!--短信服务-->
									<c:if test="${ moudle.resource.resourceCode == 'SALES' }">
										<c:forEach items="${moudle.getChildren()}" var="nav">
											<c:forEach items="${nav.getChildren()}" var="page">
												<c:if
													test="${ page.resource.resourceCode == 'SMS_SERVICECODE_MANAGE' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
												</c:if>
												<c:if
													test="${ page.resource.resourceCode == 'SMS_MESSAGE_RESULT' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
												</c:if>
												<c:if
													test="${ page.resource.resourceCode == 'SMS_MESSAGE_AUDITING' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
												</c:if>
												<c:if
													test="${ page.resource.resourceCode == 'SMS_BASE_WHITELIST' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
												</c:if>
												<c:if
													test="${ page.resource.resourceCode == 'SMS_BASE_BLACKLIST' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
												</c:if>
												<c:if
													test="${ page.resource.resourceCode == 'SMS_MESSAGE_MO' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
												</c:if>
												<c:if
													test="${ page.resource.resourceCode == 'SMS_INTERCEPTION_INFO' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
												</c:if>
												<c:if
													test="${ page.resource.resourceCode == 'SMS_ACCOUNTS_REDE_RECORD' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
												</c:if>
												<c:if
													test="${ page.resource.resourceCode == 'SMS_BASE_REDLIST' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
												</c:if>
												<c:if
													test="${ page.resource.resourceCode == 'SMS_MESSAGE_TIMER' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
												</c:if>
												<c:if
													test="${ page.resource.resourceCode == 'SMS_CHANNEL_MONITOR' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
												</c:if>
												<c:if
													test="${ page.resource.resourceCode == 'SMS_ACCOUNTS_SMS_RECORD' }">
													<li><a
														href="${WEB_SERVER_PATH}${page.resource.resourcePath}">${ page.resource.resourceName}</a></li>
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
		comments()
	</script>


