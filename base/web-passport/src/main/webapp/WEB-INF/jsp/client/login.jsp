<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="head">
	<title>EUCP-登录</title>
	<script type="text/javascript" src="${WEB_SERVER_PATH}/lib/md5.js"></script>
	<style type="text/css">
		body {
			background:url(${WEB_STATIC_PATH}/img/client/clientLoginBg.png) 1366px 768px #88c0fb;
			background-position:center center;
			background-size:cover;
		}
	</style>
	<c:set var="WEB_STATIC_PATH" value="${pageContext.request.contextPath}"></c:set>
</temple:write>

<temple:write property="context">
	<div class="login_left fl">
		<img src="${WEB_STATIC_PATH}/img/client/login_img2.png" />
	</div>
	<div class="login_right fadeInDown animated fl rs">
		<div class="login-center">
			<input type="hidden" value="${fromUrl}" id="fromUrlInput"/>
			<input type="hidden" value="${system}" id="systemInput"/>
			<div class="divleft inpdiv">
				<input type="text" placeholder="用户名" autocomplete="off" id="username">
			</div>
			<div class="divleft inpdiv">
				<input type="password" placeholder="密码" id="password">
			</div>
			<div class="divleft captchadiv">
				<input type="text" placeholder="验证码" id="captcha"> 
				<img class="mycaptchaimg captcha_a" src="${WEB_SERVER_PATH}/captcha?type=login">
			</div>
			<div class="errorsdiv submit_btn">
				<span id="errorspan"></span>
			</div>
			<div class="btnDiv">
				<input type="button" value="登 录" class="logbtn loginbtn rn" id="submit">
			</div>
			<div class="download">
				<a href="http://www.emay.cn/article283.html" title="立即申请"><img src="${DOMAIN_PATH}/static/img/client/jump.png" style="width:18px;height:18px;"/>&nbsp;立即申请</a>&nbsp;&nbsp;&nbsp;
				<a href="${DOMAIN_PATH}/static/doc/index.html" title="文档中心"><img src="${DOMAIN_PATH}/static/img/client/doc.png" />&nbsp;文档中心</a>
				</div>
		</div>
	</div>
	 <script type="text/javascript" src="${WEB_SERVER_PATH}/js/login.js"></script>
</temple:write>

<%@ include file="./temple/noauth/noAuthTemple.jsp"%>
