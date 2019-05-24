<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="temple" uri="/temple"%>
<div class="crumbs">
	<i class="icon crumbs-icon"></i>
	<c:forEach items="${TOKEN.tree.getChildren()}" var="moudle">
		<c:if test="${ moudle.resource.resourceCode == CURRENT_MOUDLE_CODE }">
			<c:out value="${ moudle.resource.resourceName}"/>
			&gt;
			<c:forEach items="${moudle.getChildren()}" var="nav">
				<c:if test="${ nav.resource.resourceCode == CURRENT_NAV_CODE }">
					<c:out value="${ nav.resource.resourceName}"/>
					&gt;
					<c:forEach items="${nav.getChildren()}" var="page">
						<c:if test="${ page.resource.resourceCode == CURRENT_PAGE_CODE }">
							<a href="${WEB_SERVER_PATH}${ page.resource.resourcePath}">
								<c:out value="${ page.resource.resourceName}"/>
							</a>
							<temple:block property="crumbs" />
						</c:if>
					</c:forEach>
				</c:if>
			</c:forEach>
		</c:if>
	</c:forEach>
</div>
