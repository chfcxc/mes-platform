<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<div class="header">
	<div class="container">
		<a id="logo" class="logo" href="${WEB_SERVER_PATH}/index">亿美 · 统一通信平台</a>
		<ul id="navList" class="nav_list">
			<c:forEach items="${TOKEN.tree.children}" var="moudle">
				<c:if test="${moudle.resource.resourceFor == LOCAL_SYSTEM }">
					<li class="sms_module nav_item 
						<c:if test="${ moudle.resource.resourceCode == CURRENT_MOUDLE_CODE }">
							active
						</c:if>">
						<a class="nav-primary j-arrow" href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}${moudle.resource.businessType}${moudle.resource.resourcePath}">${moudle.resource.resourceName}</a>
					</li>
				</c:if>
			</c:forEach>
		</ul>
		<div class="header_right">
			<dl>
				<dd>
					<div class="header_user">
						<span class="welcome">欢迎您：</span><span class="value_header">${CURRENT_USER_REALNAME}</span>
						<span class="nav-arrow"></span>
						<div class="clear"></div>
					</div>
					<ul>
						<li><a href="${WEB_SERVER_PATH}/sys/sale/userInfo">个人信息</a></li>
						<li><a href="${DOMAIN_PATH}/${LOCAL_SYSTEM.toLowerCase()}${moudle.resource.businessType}/export" title="我的导出">我的导出</a></li>
					</ul>
				</dd>
			</dl>
		</div>
		<div class="helpbox">
			<dl>
				<dd>
					<div class="header_user">
						<span class="welcome">帮助</span>
					</div>
					<ul>
						<li><a href="${WEB_SERVER_PATH}/helperSales/updateInfo" title="系统更新">系统更新</a></li>
						<li><a href="${WEB_SERVER_PATH}/helperSales/fqa" title="FQA">FQA</a></li>
					</ul>
				</dd>
			</dl>
		</div>
		<div class="logout"><a id="logout" title="退出" href="javascript:void(0)"></a></div>
	</div>
</div>
