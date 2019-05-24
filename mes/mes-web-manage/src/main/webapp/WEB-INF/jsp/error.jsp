<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<temple:write property="head">
	<title>EUCP-错误</title>
	<style type="text/css">
		body{
		background:#fff;
		}
		.error_img {
			width: 100%;
			height: 380px;
			text-align: center;
			padding-top: 20px;
			position: relative;
		}
		.error_btn {
			position: absolute;
			top: 400px;
			left: 560px;
		}
		.error_btn input {
			border-radius: 5px;
			margin-right: 10px;
			line-height:24px;
		}
		.error_tips{
			text-align:center;
			position:absolute;
			z-index:9999;
			top:340px;
			left:34%;
		}
		.error_tips  p{
			font-size:20px;
		}
	</style>
</temple:write>

<temple:write property="context">
    <div class="login_center">
		<div class="error_img">
			<img src="${WEB_STATIC_PATH}/img/client/e_404.png" />
		</div>
		<div class="error_tips">
			<c:if test="${msg == null}">
				<p>您访问的页面不存在</p>
			</c:if>
			<c:if test="${msg != null}">
				<p>${msg}</p>
			</c:if>
		</div>
		<div class="error_btn">
			
			<a href="${WEB_SERVER_PATH}/index"><input type="button" value="返回首页"/></a>
            <a href="javascript:history.go(-1);"><input type="button" value="返回上一页"/> </a>
            
		</div>
		<div class="clear"></div>
	</div>
	<script type="text/javascript">
		$(".return_last").click(function(){
			history.back(-1);
		});
	</script>
</temple:write>

<%@ include file="./temple/noauth/noAuthTemple.jsp"%>
