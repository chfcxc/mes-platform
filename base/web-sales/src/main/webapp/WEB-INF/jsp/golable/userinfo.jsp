<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/sale/userinfo/userinfo.css" type="text/css" />
</temple:write>
<temple:write property="context">

<div class="personalInformation">
    <h2>个人信息</h2>
	<ul class="ulBox">
		<li><label>用户名：</label><span>${user.username}</span></li>
		<li><label>密&nbsp;&nbsp;&nbsp;&nbsp;码：</label><span>${user.password}</span><input value="修 改" onclick="modifypw()" type="button"></li>
		<li><label>姓&nbsp;&nbsp;&nbsp;&nbsp;名：</label><span>${user.realname}</span><input value="修 改" onclick="modifyrn('${user.realname}')" type="button" class="disno"></li>
		<li><label>手&nbsp;&nbsp;&nbsp;&nbsp;机：</label><span>${user.mobile}</span><input value="修 改" onclick="modifym('${user.mobile}')" type="button"></li>
		<li><label>邮&nbsp;&nbsp;&nbsp;&nbsp;箱：</label><span>${user.email}</span><input value="修 改" onclick="modifye('${user.email}')" type="button"></li>
	</ul>
</div>
<div id="changepassword">
<form method="post"  id="changepw" class="changepw">
   	<fieldset class="changepw_box">
       <div class="item">
           <label class="item-label"><span class="xing">*</span>当前密码:</label>
           <input id="oldPassWord" name="oldPassWord" class="item-text" maxlength="16" type="password">
       </div>
       <div class="item">
           <label class="item-label"><span class="xing">*</span>新密码:</label>
           <input id="newpass" name="newpass" class="item-text" maxlength="16" type="password">
           <span class="fontColor">&nbsp;&nbsp;密码由6－16位数字英文组成，区分大小写</span>
       </div>
       <div class="item">
           <label class="item-label"><span class="xing">*</span>确认新密码:</label>
           <input id="affirm" name="affirm" class="item-text" maxlength="16" type="password">
       </div>
        <div class="tipFoot">
           <button id="saveBtn" class="tipBtn" type="submit" onclick="modifyPword()">保 存</button>
           <button class="tipBtn tip-cancel" type="button" onclick="javascript:goPage('/sys/sale/userInfo');">取 消</button>
       </div>
      </fieldset>
</form>
</div>
 
</temple:write>

<%@ include file="../temple/auth/simpleAuthTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/sale/golable/userInfo.js"></script>