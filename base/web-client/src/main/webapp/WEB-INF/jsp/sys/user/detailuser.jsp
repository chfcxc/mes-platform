<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="crumbs">
	> 查看用户	
</temple:write>
<temple:write property="head">
<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/client/user/user.css" type="text/css" />
	<script type="text/javascript">
		var id='${id}';
	</script>

</temple:write>
<temple:write property="context">
<div class="form_box">
	<form id="UserForm" class="detailuser">
   	  	<div class="item firstitem">
            <label class="item-label"><span class="xing">*</span>用户名 :</label>
            <input type="text" id="username" name="username" class="item-text" disabled="disabled"/>
         </div>
         <div class="item">
            <label class="item-label"><span class="xing">*</span>姓名 :</label>
            <input type="text" id="realname" name="realname" class="item-text"  disabled="disabled"/>
         </div>
	    <div class="item">
            <label class="item-label"><span class="xing">*</span>手机号 :</label>
            <input type="text" id="mobile" name="mobile" class="item-text"  disabled="disabled"/>
          </div>
          <div class="item">
            <label class="item-label">邮箱 :</label>
            <input type="text" id="email" name="email" class="item-text"  disabled="disabled"/>
          </div>
          <div class="item qux clear jueselabel" id="jues">
              <div class="checkL fl"><label class="item-label"><span class="xing">*</span>所属角色 :</label></div>
          	  <div class="checkR fl"></div>
          </div> 
          <div class="tipFoot">
            <button onclick="javascript:goPage('/sys/client/administrate')" type="button" class="tipBtn tip-cancel">返 回</button>
          </div>
	</form>
</div>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/client/sys/user/detailuser.js"></script>
</temple:write>
<%@ include file="../../temple/auth/authTemple.jsp"%>
