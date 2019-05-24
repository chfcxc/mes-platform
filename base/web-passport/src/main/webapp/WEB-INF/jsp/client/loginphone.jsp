<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="head">
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
		<meta name="apple-mobile-web-app-capable" content="yes" />
		<!-- 删除苹果默认的工具栏和菜单栏 -->
		<meta name="apple-mobile-web-app-status-bar-style" content="black" />
		<!-- 设置苹果工具栏颜色 -->
		<meta name="format-detection" content="telephone=no, email=no" />
		<!--忽略页面中的数字识别为电话，忽略email识别 -->
		<link rel="stylesheet" href="${WEB_SERVER_PATH}/css/client/login_phone.css" type="text/css" />
	<title>EUCP-登录</title>
	<script type="text/javascript" src="${WEB_SERVER_PATH}/lib/md5.js"></script>
	<script>
			 var oHeight = $(window).height(); 
			$(window).resize(function(){
				if($(document).height() < oHeight){
				    $(".footerphone").hide();
				}else{
			
				    $(".footerphone").show();
				}
			}); 
	</script>
</temple:write>
<temple:write property="context">
	<div class="mainphone">
			<div class="headerphone">
				 <span>亿美 · 统一通信平台</span>
			</div>
			<div class="center">
				<!-- <div class="center_top"></div> -->
				<div class="center_bottom">
					<h2>账号登录   </h2>
					<div class="log_line"></div>
					<input type="hidden" value="${fromUrl}" id="fromUrlInput"/>
					<input type="hidden" value="${system}" id="systemInput"/>
					<div class="divleftphone inpdiv">
						<input type="text" placeholder="输入用户名" autocomplete="off" id="username">
					</div>
					<div class="divleftphone inpdiv">
						<input type="password" placeholder="输入密码" id="password">
					</div>
					<div class="divleftphone captchadiv">
						<input type="text" placeholder="输入验证码" id="captcha">
						<img class="mycaptchaimg captcha_a mycaptchaimgphone" src="${WEB_SERVER_PATH}/captcha?type=login">
					</div>
					<div class="errorsdiv submit_btn">
							<span id="errorspan"></span>
					</div>
					<div class="btnDivphone">
						<input type="button" value="登 录" class="logbtn loginbtn rn" id="submit">
					</div>
					<div class="download">
						<a href="${DOMAIN_PATH}/static/doc/index.html" title="文档中心"><img src="${DOMAIN_PATH}/static/img/client/doc.png"  style="vertical-align: middle;">&nbsp;文档中心</a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="http://www.emay.cn/article283.html" title="立即申请"><img src="${DOMAIN_PATH}/static/img/client/jump.png" style="width:20px;height:20px;vertical-align: middle;">&nbsp;立即申请</a>
					 </div>
					  <span style="display:block;color:red;margin:10px 30px;font-size:15px;">建议使用pc端登录</span>
				</div>
				<div class="footerphone">
					<p>Copyright©北京亿美软通科技有限公司</p>
				</div>
			</div>
	 <script type="text/javascript" src="${WEB_SERVER_PATH}/js/login.js"></script>
</temple:write>
<%@ include file="./temple/noauth/noAuthTemple.jsp"%>
<script>
	     $(function(){
	    	 $(".login_header").hide(); 
	    	/*  $(".login_center").hide(); */
	    	  $(".login_footer").hide(); 
	    	   $(".login_center").attr("style","height:auto;");
	    	   $(".login_center").removeClass("login_center");
	    })
</script>
