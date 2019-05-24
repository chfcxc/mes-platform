<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="crumbs">
	> 查看详情
</temple:write>
<temple:write property="head">
<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/info/userdetail.css" type="text/css" />
	<script type="text/javascript">
		var id='${id}';
	</script>
</temple:write>
<temple:write property="context">
<div class="form_box">
	<form id="userdetail" >
		<div class="item">
			<label class="item-label">客户名称 :</label>
			<input id="clientName"  class="item-text" type="text">
		</div>
		<div class="item">
			<label class="item-label">客户编号 :</label>
			<input id="clientNumber"  class="item-text" type="text">
		</div>
		<div class="item">
			<label class="item-label">用户名 :</label>
			<input id="username"  class="item-text" type="text">
		</div>
		<div class="item">
			<label class="item-label">帐号类型 :</label>
			<input id="identity"  class="item-text" type="text">
		</div>
		<div class="item">
			<label class="item-label">姓名 :</label>
			<input id="realname"  class="item-text" type="text">
		</div>
		<div class="item">
			<label class="item-label">手机号 :</label>
			<input id="mobile"  class="item-text" type="text">
		</div>
		<div class="item">
			<label class="item-label">状态 :</label>
			<input id="state"  class="item-text" type="text">
		</div>
		<div class="item">
			<label class="item-label">邮箱 :</label>
			<input id="email"  class="item-text" type="text">
		</div>
		<div class="item">
			<label class="item-label">创建时间 :</label>
			<input id="timein"  class="item-text" type="text">
		</div>
		<div class="item authbox">
			<label class="item-label">角色 :</label>
			<div id="authinput"  class="item-text"></div>
		</div>
		<div class="tipFoot mb20">
			<button onclick="javascript:goPage('/sys/client/user')" type="button" class="tipBtn tip-cancel">返 回</button>
		</div>
	</form>
</div>
</temple:write>
<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/client/user/userdetail.js"></script>