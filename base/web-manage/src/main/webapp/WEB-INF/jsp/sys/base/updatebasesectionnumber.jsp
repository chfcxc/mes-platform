<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="crumbs">
	>修改	
</temple:write>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/baseinfo/basesectionnuber.css" type="text/css" />
</temple:write>
<temple:write property="context">

	<div class="clear form_box"  id="update">
		<form method="post" id="fullFormUpdate" >
			<input type="hidden" id="updateInpId"  value='${sectionNumber.id}' />
			<!-- <input type="hidden" id="updateInpValue" /> -->
			<div class="item mp30">
                 <label class="item-label"><span class="xing">*</span>号码段：</label>
                 <input type="text" id="numberInput"   class="item-text" name="numberInput" value='${sectionNumber.number}' />
           </div>
           <div class="item">
               <label class="item-label"><span class="xing">*</span>省份：</label>
               <select id="provinceInput" class="item-select" name="provinceInput">
                  <option value="">请选择</option>
					<c:forEach items="${list}" var="b">
					 <c:choose>
					       <c:when test="${sectionNumber.provinceCode==b.code}">
					           <option value="${b.code}" selected="selected">${b.name}</option>
					       </c:when>
					       <c:otherwise>
					           <option value="${b.code}">${b.name}</option>
					       </c:otherwise>
					</c:choose>
					</c:forEach>
               </select>
           </div>
           <div class="item">
               <label class="item-label"><span class="xing">*</span>运营商：</label>
               <select id="companyInput" class="item-select" name="companyInput">
					 <c:choose>
					       <c:when test="${sectionNumber.operatorCode=='CMCC'}">
					            <option value="CMCC" selected="selected">中国移动</option>
					            <option value="CUCC">中国联通</option>
								<option value="CTCC">中国电信</option>
					       </c:when>
					       <c:when test="${sectionNumber.operatorCode=='CUCC'}">
					       		<option value="CMCC" >中国移动</option>
					            <option value="CUCC" selected="selected">中国联通</option>
								<option value="CTCC">中国电信</option>
			      				
					       </c:when>
					       <c:otherwise>
					       		<option value="CMCC">中国移动</option>
					            <option value="CUCC">中国联通</option>
								 <option value="CTCC" selected="selected">中国电信</option>
					          
					       </c:otherwise>
					</c:choose>
               </select>
           </div>
           <div class="item">
                 <label class="item-label">城市：</label>
                 <input type="text" id="cityInput"  class="item-text" name="cityInput" value='${sectionNumber.city}' />
           </div>
           <div class="tipFoot">
                 <button onclick="updateTure()" id="updateBtn_S" class="tipBtn" type="submit">保 存</button>
                 <button class="tipBtn tip-cancel" type="button"  onclick="addCancel()">取 消</button>
            </div>	
		</form>
	</div>

</temple:write>

<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/baseinfo/fullsectionnumber/updatebasesection.js"></script>