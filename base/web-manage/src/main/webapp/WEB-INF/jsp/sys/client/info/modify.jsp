<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="crumbs">
	> 修改客户
</temple:write>
<temple:write property="head">
	 <link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/info/info.css" type="text/css" /> 	
	 <script>
	 	var stateTime='${enterprise.startClientSelectTime}';
	 	var endTime='${enterprise.endClientSelectTime}';
	 	stateTime=stateTime.substring(0,stateTime.length-10);
	 	endTime=endTime.substring(0,endTime.length-10);
	 </script>
</temple:write>
<temple:write property="context">
	<div class="clear form_box"  id="modify">
	<form method="post" id="infoForm" >
		<input type="hidden" value="${enterprise.id}" id="clintId"/>
		<div class="item">
             <label class="item-label"><span class="xing">*</span>客户编号：</label>
             <input type="text" id="clientNumber" class="item-text noMi" name="clientNumber" value="${enterprise.clientNumber}" disabled="disabled" />
        </div>
		<div class="item">
              <label class="item-label"><span class="xing">*</span>客户名称：</label>
              <input type="text" id="nameCn"   class="item-text" name="nameCn" value="${enterprise.nameCn}" maxlength="50"/>
        </div>
        <div class="item">
             <label class="item-label"><span class="xing">*</span>客户类型：</label>
             <select id="clientType" class="item-select" name="clientType" value="${enterprise.type}">
			 	<option value="0" <c:if test="${'0' eq enterprise.type}">selected</c:if> >普通客户</option>
				<option value="1" <c:if test="${'1' eq enterprise.type}">selected</c:if> >代理商</option>
			 </select>
        </div>
        <div class="item">
             <label class="item-label"><span class="xing">*</span>客户级别：</label>
             <select id="viplevel" class="item-select" name=""viplevel"">
				<option value="0" <c:if test="${'0' eq enterprise.viplevel}">selected</c:if>>普通客户</option>
				<option value="1" <c:if test="${'1' eq enterprise.viplevel}">selected</c:if>>大客户</option>
				<option value="2" <c:if test="${'2' eq enterprise.viplevel}">selected</c:if>>vip客户</option>
			 </select>
        </div>
         <div class="item">
             <label class="item-label"><span class="xing">*</span>客户权限：</label>
             <select id="authority" class="item-select" name="authority" value="${enterprise.authority}">
				<c:forEach items="${authNameList}" var="a">
					<option value="${a.authLevel}" <c:if test="${a.authLevel == enterprise.authority}">selected</c:if> >${a.authName}</option>
				</c:forEach>
			 </select>
        </div>
        <div class="item">
        <input type="hidden" id="valueAddedServiceInput" value="${enterprise.valueAddedService}"/>
             <label class="item-label">增值服务：</label>
             <div id="valueAddedService">
              	<input type="checkbox" value="1" />&nbsp;短链接简易版
              	<input type="checkbox" value="2" />&nbsp;短链接精确版
             </div>
        </div>
        <div class="item">
             <label class="item-label">短信客户端数据范围：</label>
             <input id="startDate" class="Wdate endinput" type="text" onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd',maxDate:$('#endDate').val()})"  /> -- 
			<input id="endDate" class="Wdate endinput" type="text" onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd',minDate:$('#startDate').val()})" />
        </div>
        <div class="item" >
              <label class="item-label">联系人：</label>
              <input type="text" id="linkman" class="item-text" name="linkman" value="${enterprise.linkman}" maxLength="10"/>
        </div>
        <div class="item" >
              <label class="item-label">手机号：</label>
              <input type="text" id="mobile" class="item-text" name="mobile" value="${enterprise.mobile}"/>
        </div>
        <div class="item" >
              <label class="item-label"><span class="xing">*</span>销售：</label>
               <input type="text" id="salename" class="item-text" name="salename" value="${enterprise.realname}" readonly="readonly"/>&nbsp;&nbsp;
              <button onclick="chooseSale(this)"  class="tipBtn blue" type="button">选择</button>
               <input type="hidden" id="saleId" value="${enterprise.salesId}" />
        </div>
        <input type="hidden" id="telephone" value="${enterprise.telephone}"/>
        <div class="item telephoneItem" >
              <label class="item-label">电话：</label>
              <input type="text" id="telephone1" class="item-text" name="telephone" maxlength="5" placeholder="区号"/><div>-</div>
              <input type="text" id="telephone2" class="item-text" name="telephone" maxlength="8" placeholder="电话"/><div>-</div>
              <input type="text" id="telephone3" class="item-text" name="telephone" maxlength="6" placeholder="分机"/>
        </div>
        <div class="item" >
              <label class="item-label">邮箱：</label>
              <input type="text" id="email"   class="item-text" name="email" value="${enterprise.email}"/>
        </div>
        <div class="item" >
              <label class="item-label">公司地址：</label>
              <input type="text" id="address" value="${enterprise.address}" class="item-text" name="address" maxlength="100"/>
        </div>
        <div class="item">
        <input type="hidden" id="serviceTypeInput" value="${enterprise.serviceType}"/>
            <label class="item-label"><span class="xing">*</span>开通服务：</label>
             <div id="serviceType">
             	<c:forEach items="${businessList}" var="a">
             		<input type="checkbox"   value="${a.id}" />&nbsp;${a.businessName}
             	</c:forEach>
              	<!-- <input type="checkbox"   value="1" />&nbsp;短信服务
              	<input type="checkbox"   value="2" />&nbsp;流量服务
              	<input type="checkbox"   value="3" />&nbsp;国际短信服务
              	<input type="checkbox"   value="4" />&nbsp;语音服务 -->
             </div>
        </div>
        <div class="tipFoot">
               <button onclick="modifyTure()" id="updateBtn" class="tipBtn" type="submit">保 存</button>
               <button class="tipBtn tip-cancel" type="button" onclick="javascript:goPage('/sys/client/info')">取 消</button>
        </div>	
	</form>
</div>

</temple:write>
<%@ include file="../../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/client/info/modify.js"></script>
