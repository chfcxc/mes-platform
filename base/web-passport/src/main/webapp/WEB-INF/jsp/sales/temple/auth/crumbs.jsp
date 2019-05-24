<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="temple" uri="/temple"%>
<div class="crumbs" id="crumbsThis">
	<i class="icon crumbs-icon"></i>
	<span id = "crumbsmoudle"></span>
	<span id = "crumbsnav"></span>
	<a id = "crumbspageurl">
		<span id = "crumbspage"></span>
	</a>
	<span id = "crumbssupport"></span>
	<div class="headerRightBox">
		<div class="header_right">
			<dl>
				<dd>
					<div class="header_user">
						<span class="welcome">欢迎您：</span><span class="value_header" id="usernameFor"></span>
						<span class="nav-arrow"></span>
						<div class="clear"></div>
					</div>
					<ul>
						<li><a href="${BUS_DOMAIN_PATH}/${BUS_SYSTEM.toLowerCase()}/sys/sale/userInfo">个人信息</a></li>
						<li><a href="${BUS_DOMAIN_PATH}/${BUS_SYSTEM.toLowerCase()}/export" title="我的导出">我的导出</a></li>
					</ul>
				</dd>
			</dl>
		</div>
		<div class="helpbox">
			<dl>
				<dd>
					<div class="header_user">
						<span class="welcome">帮助</span>
						<span class="nav-arrow"></span>
					</div>
					<ul>
						<li><a href="${BUS_DOMAIN_PATH}/${BUS_SYSTEM.toLowerCase()}/helperSales/updateInfo" title="系统更新">系统更新</a></li>
						<li><a href="${BUS_DOMAIN_PATH}/${BUS_SYSTEM.toLowerCase()}/helperSales/fqa" title="FQA">FQA</a></li>
					</ul>
				</dd>
			</dl>
		</div>
		<div class="logout"><a id="logout" title="退出" href="javascript:void(0)"></a></div>
	</div>
</div>
