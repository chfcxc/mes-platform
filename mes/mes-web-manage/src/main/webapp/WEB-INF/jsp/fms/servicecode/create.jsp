<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="crumbs">
	> 生成服务号
</temple:write>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/servicecode/create.css" type="text/css" />
</temple:write>
<temple:write property="context">
	<div class="form_box" id="add">
		<form method="post" id="fullForm" class="createForm">
			<input id="addInpValue" type="hidden" />
			<div class="item">
				<label class="item-label"><span class="xing">*</span>客户名称：</label>
				<input id="clientN" class="item-text" name="clientN" type="text" readonly="readonly"/>
				<input type="hidden" id="clientId"  >
				<button class="agentAbbr_btn tipBtn" id="selectUser">选择客户</button>
			</div>
			<div class="item">
				<label class="item-label">客户编号：</label> 
				<input id="clientNumber" readonly="readonly" class="item-text" name="clientNumber" type="text" />
			</div>
			<div class="item">
				<label class="item-label">服务号名称：</label> 
				<input id="serviceCode" class="item-text" name="serviceCode" type="text" />
			</div>
			<div class="item">
                 <label class="item-label">业务类型：</label>
                 <select id="businessType" name="businessType" class="item-text" style="width:213px;" onchange="chooseType()">
	                 <option value="">--请选择--</option>
					<c:forEach items="${map}" var="map">
						<option value="${map.key}">${map.key}</option>
					</c:forEach>
		   		</select>
            </div>
            <div class="item">
                 <label class="item-label">保存类型：</label>
                 <select id="saveType" name="saveType" class="item-text" style="width:213px;" onchange="chooseType()">
					<option value="">--请选择--</option>
			     	<option value="1">可以保存</option>
			     	<option value="2">不可保存</option>
				 </select>
            </div>
            <div class="item">
                 <label class="item-label">内容类别：</label>
                 <select id="businessTypeId" class="item-text" style="width:213px;" name="businessTypeId" onclick="changecontent(this)">
                 	<option value="">--请选择--</option>
					<c:forEach items="${map}" var="dataMap"> 
				       <c:forEach items="${dataMap.value}" var="value"> 
				       	<option style="display:none;" data-savetype="${value.saveType}" data-parentname="${value.parentName}" value="${value.id}">${value.name}</option>
				       </c:forEach>                 
				    </c:forEach>
				 </select>
            </div>
			<div class="item" style="height: 100px;">
				<label class="item-label">备注：</label>
				<textarea value="" id="remark" class="item-text remark"></textarea>
			</div>
			<div class="tipFoot">
				<button  id="updateBtn" class="tipBtn"
					type="submit">确  定</button>
				<button class="tipBtn tip-cancel" type="button"
					onclick="javascript:goPage('/fms/servicecode/manage');">取 消</button>
			</div>
		</form>
	</div>
</temple:write>
<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/servicecode/create.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/common/common.js"></script>

