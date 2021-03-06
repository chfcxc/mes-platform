<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/baseinfo/basesectionnuber.css" type="text/css" />
</temple:write>
<temple:write property="context">
	<div id="outerId"></div>
	<div class="clear disno"  id="add">
		<form method="post" id="baseForm">
			<input type="hidden" id="baseId" />
			<div class="item">
                  <label class="item-label"><span class="xing">*</span>号码段：</label>
                  <input type="text" id="numberInput"  class="item-text" name="numberInput"  />
            </div>
			
			<div class="item">
                <label class="item-label"><span class="xing">*</span>运营商：</label>
                <select id="companyInput" class="item-select" name="companyInput">
                    <option value="">---请选择---</option>
                    <option value="CMCC">中国移动</option>
					<option value="CUCC">中国联通</option>
					<option value="CTCC">中国电信</option>
                </select>
            </div>
            <div class="tipFoot">
                  <button onclick="addTure()" id="saveBtn" class="tipBtn" type="submit">保 存</button>
                  <button class="tipBtn tip-cancel" type="button" onclick="cancelText()">取 消</button>
             </div>
		</form>
	</div>
	
	<div class="clear disno"  id="update">
		<form method="post" id="updateForm">
			<input type="hidden" id="baseIdupdate" />
			<div class="item">
                  <label class="item-label"><span class="xing">*</span>号码段：</label>
                  <input type="text" id="numberUpdate"   class="item-text" name="numberUpdate"  />
            </div>
			
			<div class="item">
                <label class="item-label"><span class="xing">*</span>运营商：</label>
                <select id="companyUpdate" class="item-select" name="companyUpdate">
                    <option value="">---请选择---</option>
                    <option value="CMCC">中国移动</option>
					<option value="CUCC">中国联通</option>
					<option value="CTCC">中国电信</option>
                </select>
            </div>
            <div class="tipFoot">
                  <button onclick="updateTure()" id="updateBtn" class="tipBtn" type="submit">保 存</button>
                  <button class="tipBtn tip-cancel" type="button" onclick="cancelText()">取 消</button>
             </div>	
		</form>
	</div>

	
</temple:write>

<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/baseinfo/fullsectionnumber/basesectionnuber.js"></script>