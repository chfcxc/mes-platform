<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<header class="navbar-side">
	<div class="header">
		<div class="container">
			<div class="clientLogo">
				<a id="logo" class="logo disno" href="${BUS_DOMAIN_PATH}/${BUS_SYSTEM.toLowerCase()}/index"></a>
				<h2><a href="${BUS_DOMAIN_PATH}/${BUS_SYSTEM.toLowerCase()}/index">亿美 · 统一通信平台</a></h2>
			</div>
		</div>
	</div>
	<div class="open_box">
		<div class='open_nav' onclick="unfoldBox(this)"></div>
	</div>	
	<div class="nav_list_div">
		<ul id="navList" class="nav_list">
			<li class="sms_module nav_item" code = "INDEX">
				<a href="${BUS_DOMAIN_PATH}/${BUS_SYSTEM.toLowerCase()}/index">
				    <span class="nav-ico nav-ico-10"></span> 
                    <span class="nav-primary j-arrow">首页</span>
                  </a>
			</li>
			<c:forEach items="${TREE.children}" var="moudle">
					<li code = "${moudle.resource.resourceCode}"  class="sms_module nav_item">
						<a href="${BUS_DOMAIN_PATH}/${BUS_SYSTEM.toLowerCase()}${moudle.resource.businessType}${moudle.resource.resourcePath}" >
					        <span class="nav-ico ${ moudle.resource.resourceIcon}" ></span>
							<span class="nav-primary j-arrow">${moudle.resource.resourceName}</span>
					   </a>
					</li>
			</c:forEach>
		</ul>
	</div>
	<nav>
	<div class="main-left">
		<div class="left_nav">
			<c:forEach items="${TREE.getChildren()}" var="moudle">
				<c:if test="${ moudle.resource.businessType == BUS_TYPE }">
					<div class="left_nav_shouye">
						<span class="nav-ico ${ moudle.resource.resourceIcon}"></span><a>${ moudle.resource.resourceName}</a><i onclick="foldBox(this)"></i>
					</div>
					<c:forEach items="${moudle.getChildren()}" var="nav">
						<h2 class="left_nav_module" code="${nav.resource.resourceCode}">
							<span class="nav-ico ${nav.resource.resourceIcon}"></span>${ nav.resource.resourceName}<i></i>
						</h2>
						<ul class="left_nav_menu">
						<c:forEach items="${nav.getChildren()}" var="page">
							<li class="" code="${page.resource.resourceCode}">
								<i></i>
								<a href="${BUS_DOMAIN_PATH}/${BUS_SYSTEM.toLowerCase()}${page.resource.businessType}${page.resource.resourcePath}" onclick="sessionStorage.clear()">${ page.resource.resourceName}</a>
							</li>
						</c:forEach>
						</ul>
					</c:forEach>
					</c:if>
			</c:forEach>
		</div>
		<div class="open_box disno">
			<div class='open_nav' onclick="unfoldBox(this)"></div>
		</div>
	</div>
</nav>
</header>