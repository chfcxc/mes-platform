<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<temple:write property="head">
	<title>修改密码</title>
	<c:set var="WEB_STATIC_PATH" value="${pageContext.request.contextPath}"></c:set>
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/changepassword.css" type="text/css" />
	<script type="text/javascript" src="${WEB_STATIC_PATH}/lib/md5.js"></script>
</temple:write>

<temple:write property="context">
	<form method="post" id="changepw" class="changepw">
       	<fieldset  class="changepw_box">
           <legend class="pwtitle">修改密码</legend>
            <input type="hidden" value="${param.fromUrl}" id="fromUrlInput"/>
			<input type="hidden" value="${param.system}" id="systemInput"/>
         <!--   <div class="tipword"><span>提示： </span>首次登录需要修改密码！</div> -->
           <div class="item">
               <label class="item-label"><span class="xing">*</span>当前密码:</label>
               <input type="password" id="oldPassWord" name="oldPassWord" class="item-text" maxlength="16"/>
           </div>
           <div class="item">
               <label  class="item-label"><span class="xing">*</span>新密码:</label>
               <input type="password" id="newpass"  name="newpass" class="item-text"  maxlength="16"/>
               <span class="fontColor">密码由6－16位数字英文组成，区分大小写</span>
           </div>
           <div class="item">
               <label  class="item-label"><span class="xing">*</span>确认新密码:</label>
               <input type="password" id="affirm" name="affirm" class="item-text"   maxlength="16"/>
           </div>
            <div class="tipFoot">
               <button id="saveBtn" class="tipBtn" type="button" onclick="modifyPword()">保 存</button>
               <button class="tipBtn tip-cancel" type="button" onclick="javascript:goPage('${param.fromUrl}');">取 消</button>
           </div>
          </fieldset>
       </form> 
		<script type="text/javascript" src="${WEB_STATIC_PATH}/js/authcommon.js"></script>	
</temple:write>

<%@ include file="./temple/auth/simpleAuthTemple.jsp"%>