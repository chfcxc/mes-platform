<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="crumbs">
	> 修改License
</temple:write>
<temple:write property="head">
<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/license/license.css" type="text/css" />
</temple:write>
<temple:write property="context">
<div class="form_box">
	<input type="hidden" value="${license.id}" id="clintId" />
	<form id="LicenseForm" method="post">
		<div class="item">
			<label class="item-label"><span class="xing">*</span>客户名称：</label> 
			<input type="hidden" id="systemEnterpriseId"  value='${license.systemEnterpriseId}'/>
			<input type="text" name="agentAbbr" class="item-text" onfocus="chooseCustomerName()" id="agentAbbr" value='${license.nameCn}' />
		</div>
         <div class="item">
			<label class="item-label"><span class="xing">*</span>客户联系人：</label> 
			<input type="text" disabled="disabled" name="linkman" class="item-text" id="linkman" value='${license.linkman}'>
		</div>
		<div class="item">
			<label class="item-label"><span class="xing">*</span>联系方式：</label> 
			<input type="text"  disabled="disabled" name="mobile" class="item-text" id="mobile" value='${license.mobile}'>
		</div>
         <div class="item">
             <label class="item-label">产品：</label>
             <select name="product" class="item-select" id="product" >
	  		 	<c:choose>
				   <c:when test="${license.product=='EMAS'}">
				        <option value="EMAS" selected="selected">EMAS</option>
				   </c:when>
				</c:choose>	
             </select>
         </div>
	    <div class="item">
            <label class="item-label">版本 :</label>
            <input type="text" id="version" name="version" class="item-text" value='${license.version}' />
          </div>
         <div class="item">
             <label class="item-label"><span class="xing">*</span>生效时间：</label>
             <input type="text" id="beginTime" class="item-text" name="beginTime" readonly="readonly"  value='<fmt:formatDate value="${license.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
         </div>
         <div class="item setTime">
         	 <label class="item-label"><span class="xing">*</span>失效时间：</label>
             <input type="text" id="endTime" class="item-text" name="endTime" readonly="readonly" value='<fmt:formatDate value="${license.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' />
         </div>
          <div class="item" >
            <label class="item-label"><span class="xing">*</span>MAC地址：</label>
            <input type="text" id="mac" name="mac" class="item-text" value='${license.mac}' />
         </div>
         <div id="serviceType"><input type="hidden" value="${license.serviceType}"/></div>
         <div class="item pl35 " id="serviceType">
         	 <label><span class="xing">*</span>短彩信服务：&nbsp;&nbsp;</label>
             <label><input type="checkbox" id="serviceType1" name="serviceType" value="1">短信服务</label> 
             <label><input type="checkbox" id="serviceType2" name="serviceType" value="2">彩信服务</label> 
         </div>
         <div class="tipFoot">
           <button onclick="openAddBox()" type="submit" class="tipBtn" id="saveBtn">生 成</button>
           <button onclick="javascript:goPage('/sys/license/recode')" type="button" class="tipBtn tip-cancel">取 消</button>
         </div>
	</form>
</div>
 <script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/license/modify.js"></script>
 <script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/license/common.js"></script>
</temple:write>

<%@ include file="../../temple/auth/authTemple.jsp"%>
