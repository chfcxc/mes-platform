<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="crumbs">
	> 修改用户
</temple:write>
<temple:write property="head">
<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/user/user.css" type="text/css" />
<script type="text/javascript">
var uid='${userId}';
</script>
</temple:write>
<temple:write property="context">
<div class="form_box">
	<form id="modifyUserForm" method="post" class="" >
		<input type="hidden" value=""/>
   	  	<div class="item">
            <label class="item-label"><span class="xing">*</span>用户名 :</label>
            <input id="username"  maxlength="20" style="text-indent:1em;border:none;" disabled="disabled" ></input>
         </div>
         <div class="item">
            <label class="item-label"><span class="xing">*</span>姓名 :</label>
            <input type="text" id="realname" name="realname" class="item-text"  />
         </div>
		<div class="item simSelect simSelect-title" onclick="departmen()">
			<label class="fl item-label"><em class="xing">*</em>所属部门 :&nbsp;&nbsp;</label>
			<input type="text" name="department" class="department" style="width:200px;float:left;" id="department"  readonly="readonly" >
			<span ><i></i></span>
		</div>
		<div class="item"  style="min-height:1px;">
			<div class="simSelect-cen depar" style="display:none;">
				<div class="simSelect-text " >
					 <div style="height:300px;" id="addGroupTree" class="contactsTree">
	       			</div> 
				</div>
				<div class="simSelect-foot">
					<button type="button" onclick="confirm(this)" class="btn btn-blue">确定</button>
				</div>
		   </div>
	    </div>
	    <div class="item checkItem" >
            <label class="item-label">是否领导 :</label>
            <input class="item-text" type="checkbox" id="identity" name="identity" >
         </div>
	    <div class="item">
            <label class="item-label"><span class="xing">*</span>手机号 :</label>
            <input type="text" id="mobile" name="mobile" class="item-text"  />
          </div>
          <div class="item">
            <label class="item-label"><span class="xing">*</span>E-mail :</label>
            <input type="text" id="email" name="email" class="item-text"  />
          </div>
          <div class="item qux" id="jues">
          </div> 
          <div class="tipFoot">
            <button type="submit" class="tipBtn" id="saveBtnde" onclick="modifyAddBox()" >保 存</button>
            <button onclick="javascript:goPage('/sys/user/manage')" type="button" class="tipBtn tip-cancel">取 消</button>
          </div>
	</form>
</div>
</temple:write>
<%@ include file="../../../temple/auth/authTemple.jsp"%>
 <script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/user/usermanage/modify.js"></script>
