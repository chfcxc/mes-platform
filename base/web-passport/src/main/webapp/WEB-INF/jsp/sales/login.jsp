<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>

<temple:write property="head">
	<title>EUCP-登录</title>
	<script type="text/javascript" src="${WEB_SERVER_PATH}/lib/md5.js"></script>
	<style type="text/css">
		body {
			background: #fff;
		}
	</style>
</temple:write>

<temple:write property="context">
	<div class="login_left fl">
		<img src="${WEB_SERVER_PATH}/img/sale/saleLogin.png" />
	</div>
	<div class="login_right fadeInDown animated fl rs">
		<div class="login-center">
			<h2>帐号登录</h2>
			<div class="login_line"></div>
			<input type="hidden" value="${fromUrl}" id="fromUrlInput"/>
			<input type="hidden" value="${system}" id="systemInput"/>
			<div class="divleft inpdiv">
				<input type="text" placeholder="输入用户名" autocomplete="off" id="username">
			</div>
			<div class="divleft inpdiv">
				<input type="password" placeholder="输入密码" id="password">
			</div>
			<div class="divleft captchadiv">
				<input type="text" placeholder="输入验证码" id="captcha"> 
				<img class="mycaptchaimg captcha_a" src="${WEB_SERVER_PATH}/captcha?type=login">
			</div>
			<div class="errorsdiv submit_btn">
				<span id="errorspan"></span>
			</div>
			<div class="btnDiv">
				<input type="button" value="登 录" class="logbtn loginbtn rn" id="submit">
			</div>
		</div>
	</div>
	 <script type="text/javascript" src="${WEB_SERVER_PATH}/js/login.js"></script>
</temple:write>

<%@ include file="./temple/noauth/noAuthTemple.jsp"%>
