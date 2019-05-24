<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/client/reported/contentReport.css" type="text/css" />
</temple:write>
<temple:write property="context">
	<input id="typeVal" type="hidden" value="${groupType}" />
	<input type="hidden" id="serialNumberOrdinary"  value=""  />
	<input type="hidden" id="size"  value=""  />
	<div class="card-cen" style="display:block">
	<div class="sms-wrap">
		<div class="sms-left">
			<div class="sms-item">
				<label class="item-label">模板名称：</label>
				<input id="templateName" name="templateName"  />
			</div>
			<div class="bttomColor"></div>
			<div class="sms-item prelative-mobile">
				<div class="fielsBtn">
				 	<a onclick="addvar()" class="tipBtn tipBtnNew">添加变量</a>
				</div>
				<div class="tips"><span class="red">内容小于70字（含标点符号）</span></div>
				<div class="textarea-box">
					<label class="item-label lh100">请输入内容：</label>
					<textarea maxlength="1000"  id="sendContent" ></textarea>
					<div class="clear"></div>
					<div class="empty_box" onclick="emptyContent(this)"><i class="iCon grey-delete" title="清空"></i></div>
				</div>
			</div>
			<div class="sms-item">
				<label class="item-label">选择服务号：</label>
				<select class="fmsserviceCode fr" id="fmsserviceCode" onchange="changeServiceCode(this)">
					<option value="">请选择</option>
					<c:forEach items="${fmsServiceCodeList}" var="list">
						<option data-appid="${list.appId}" data-businesstype="${list.businessType}" data-savetype="${list.saveType}" data-contenttype="${list.contentType}" value="${list.id}">${list.serviceCode}</option>
					</c:forEach>
				</select>
				<p class="bringspan">
					<!-- <span>USSD</span>
					<span>不可保存</span>
					<span>金融地产</span> -->
				</p>
			</div>
			<p class="templateExample">模板内容，填写变量示例：尊敬的$ {xxx}您好：您已成功注册参与我们的$ {yyy}活动，连续刷卡满 8 周，可以获得拍立得一台。填写变量的信息是个性信息</p>
			<div class="bttomColor"></div>
			<div class="bottom-btn">
                <div id="sendBtn" onclick="reportTemplate()" class="btn-blue">报备</div>
            </div>
		</div>
		<div class="sms-right">
			<div class="phone">
				<div class="phone-top" id="simulation"></div>
				<div class="phone-bottom" >
					<p id="simulation-bottom"></p>
					<p id="warning" class="red"></p>
				</div>
			</div>
		</div>
		<div class="clear"></div>
		</div>
	</div>
</temple:write>
<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/client/reported/contentReport.js"></script>