<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="crumbs">
	> 修改用户	
</temple:write>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/client/user/user.css" type="text/css" />
	<script type="text/javascript">
		var id='${id}';
		var identity='{identity}';
	</script>
</temple:write>
<temple:write property="context">
<div class="form_box">
	<form id="modifyUserForm" >
	<input type="hidden" value="${id}"/>
   	  	<div class="item firstitem">
            <label class="item-label"><span class="xing">*</span>用户名:</label>
            <input type="text" id="username" name="username" class="item-text" disabled="disabled"/>
         </div>
         <div class="item">
            <label class="item-label"><span class="xing">*</span>姓名:</label>
            <input type="text" id="realname" name="realname" class="item-text"  />
         </div>
	    <div class="item">
            <label class="item-label"><span class="xing">*</span>手机号:</label>
            <input type="text" id="mobile" name="mobile" class="item-text"  />
          </div>
          <div class="item">
            <label class="item-label">邮箱:</label>
            <input type="text" id="email" name="email" class="item-text"  />
          </div>
          <div class="item qux" id="jues">
          		<!-- <div class="checkL fl"><label class="item-label"><span class="xing">*</span>所属角色:</label></div>
          		<div class="checkR fl"><label><input class="check" type="checkbox" value="1" name="checkbox_role">系统管理员</label></div> -->
          </div> 
          <div class="tipFoot">
            <button onclick="modifyAddBox(${id})" type="submit" class="tipBtn" id="saveBtn">保 存</button>
            <button onclick="javascript:goPage('/sys/client/administrate')" type="button" class="tipBtn tip-cancel">取 消</button>
          </div>
	</form>
</div>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/client/sys/user/modify.js"></script>
</temple:write>
<%@ include file="../../temple/auth/authTemple.jsp"%>
