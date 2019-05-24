<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<div class="main-left">
	<div class="left_nav">
		<c:forEach items="${TOKEN.tree.getChildren()}" var="moudle">
			<c:if test="${moudle.resource.resourceFor == LOCAL_SYSTEM }">
				<c:if
					test="${ moudle.resource.resourceCode == CURRENT_MOUDLE_CODE }">
					<div class="left_nav_shouye">
						<span class="nav-ico ${ moudle.resource.resourceIcon}"></span><a>${ moudle.resource.resourceName}</a><i
							onclick="foldBox(this)"></i>
					</div>
					<c:forEach items="${moudle.getChildren()}" var="nav">
						<h2 class="left_nav_module">
							<span class="nav-ico ${nav.resource.resourceIcon}"></span>${ nav.resource.resourceName}<i></i>
						</h2>
						<ul class="left_nav_menu">
							<c:forEach items="${nav.getChildren()}" var="page">
								<li
									class=" <c:if test="${ page.resource.resourceCode == CURRENT_PAGE_CODE }">left_nav_high</c:if>">
									<i></i> <a
									href="${WEB_SERVER_PATH}${page.resource.resourcePath}"
									onclick="sessionStorage.clear()">${ page.resource.resourceName}</a>
								</li>
							</c:forEach>
						</ul>
					</c:forEach>
				</c:if>
			</c:if>
		</c:forEach>
	</div>
	<div class="open_box">
		<div class='open_nav' onclick="unfoldBox(this)"></div>
	</div>
</div>
<div class="tipLoadding disno" id="loaddingTip">
	<div class="tc">
		<img src="${WEB_STATIC_PATH}/img/loadding.gif">
	</div>
</div>