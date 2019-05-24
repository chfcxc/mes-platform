<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="crumbs">
	> 规则配置
</temple:write>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/servicecode/create.css" type="text/css" />
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/servicecode/rule.css" type="text/css" />
	<script type="text/javascript">
	var id='${serviceCodeinfo.id}';
	var serviceCode='${serviceCodeinfo.serviceCode}';
	var ipConfiguration='${serviceCodeinfo.ipConfiguration}';
	var isNeedReport='${serviceCodeinfo.isNeedReport}';
	var serviceCodeChannel='${serviceCodeChannel}';
	/* var bingChannel = '${bingChannel}';
	console.log(bingChannel) */
	</script>
</temple:write>

<temple:write property="context">
	<div class="clear rule_div" id="">
	<form method="post" id="fullForm" >
		<input type="hidden"  class="serviceCode" /> 
 		<input type="hidden"  class="ruleId" /> 
		<div class="rule_title light_blue_bg">规则配置&nbsp;&nbsp;${serviceCodeinfo.serviceCode}&nbsp;（${serviceCodeinfo.id}）&nbsp;【${serviceCodeinfo.remark}】<span class="red" style="margin-left:40px;">服务号名称&nbsp;（服务号ID）&nbsp;【备注】</span></div>
		<div class="celue clear">
			<label style="padding: 10px 2%">路由策略</label>
			<div class="temple_list" id="templebox">
				<table class="channelWrap">
				   	<tbody>
			   			 <tr>
						     <td width="10%" class="titleWrap" width="16%">闪推通道<span class="red">（普通）</span></td>
						     <td width="40%">
						     	<div class="channelBox clear">
						     		<div class="channelBoxOne fl" style="margin-top:63px"></div>
						     		<div class="channelBoxTwo fl" id="commonchannel">
										<div class="emay-search" >
											<label >移动</label>
											<select id="commoncmcc" operator="CMCC" onchange="detailInfo(this)">
												<option value="">--请选择--</option>
												<c:forEach items="${cmccAllChannel}"  var="cmccAllChannel1" >
													<option value="${cmccAllChannel1.id}">${cmccAllChannel1.channelName}</option>
												</c:forEach>
											</select>
										</div>
										<p class="labelspan" style="display:none;"></p>
										<div class="emay-search">
											<label>联通</label>
											<select id="commoncucc" operator="CUCC" onchange="detailInfo(this)">
												<option value="">--请选择--</option>
												<c:forEach items="${cuccAllChannel}"  var="cuccAllChannel1" >
													<option value="${cuccAllChannel1.id}">${cuccAllChannel1.channelName}</option>
												</c:forEach>
											</select>
										</div>
										<p class="labelspan" style="display:none;"></p>
										<div class="emay-search">
											<label>电信</label>
											<select id="commonctcc" operator="CTCC" onchange="detailInfo(this)">
												<option value="">--请选择--</option>
												<c:forEach items="${ctccAllChannel}"  var="ctccAllChannel1" >
													<option value="${ctccAllChannel1.id}">${ctccAllChannel1.channelName}</option>
												</c:forEach>
											</select>
										</div>
										<p class="labelspan" style="display:none;"></p>
							     	</div>
						     	</div>
						     </td> 
					     </tr>
					     <tr>
						     <td width="10%" class="titleWrap" width="16%">闪推通道<span class="red">（个性）</span></td>
						     <td width="40%">
						     	<div class="channelBox clear">
						     		<div class="channelBoxOne fl" style="margin-top:63px"></div>
						     		<div class="channelBoxTwo fl" id="personchannel">
										<div class="emay-search" >
											<label >移动</label>
											<select id="personcmcc" operator="CMCC" onchange="detailInfo(this)">
												<option value="">--请选择--</option>
												<c:forEach items="${personChannelListCmcc}"  var="personChannelListCmcc1" >
													<option value="${personChannelListCmcc1.id}">${personChannelListCmcc1.channelName}</option>
												</c:forEach>
											</select>
										</div>
										<p class="labelspan" style="display:none;"></p>
										<div class="emay-search">
											<label>联通</label>
											<select id="personcucc" operator="CUCC" onchange="detailInfo(this)">
												<option value="">--请选择--</option>
												<c:forEach items="${personChannelListCucc}"  var="personChannelListCucc1" >
													<option value="${personChannelListCucc1.id}">${personChannelListCucc1.channelName}</option>
												</c:forEach>
											</select>
										</div>
										<p class="labelspan" style="display:none;"></p>
										<div class="emay-search">
											<label>电信</label>
											<select id="personctcc" operator="CTCC" onchange="detailInfo(this)">
												<option value="">--请选择--</option>
												<c:forEach items="${personChannelListCtcc}"  var="personChannelListCtcc1" >
													<option value="${personChannelListCtcc1.id}">${personChannelListCtcc1.channelName}</option>
												</c:forEach>
											</select>
										</div>
										<p class="labelspan" style="display:none;"></p>
							     	</div>
						     	</div>
						     </td> 
					     </tr>
					</tbody>
				</table>
			</div>
		</div>	
		<div class="celue clear isPushContent">
			<div class="celuePush clear">
				<label class="batch_title">是否需要状态报告</label>
				<div id="isNeedReport" class="time">
			        <input type="radio"  id="isNeedReportAuto"  name="isNeedReportRadio"  value="2"/> 自动获取<br />
					<input type="radio"  id="isNeedReportNo"  name="isNeedReportRadio"  value="1"/> 不需要状态报告
					
				</div>
			</div>
		</div>
		<div class="celue clear">
			<label class="batch_title">IP配置</label>
			<div class="time" id="pei_z">
				<input type="text" class="admo" id="requestIps" name="requestIps" />
				<div class="imgs" onclick="addIp()"></div>
				<!--<div class="remave" onclick="removeIp()"></div>-->
			</div>
		</div>
		<div class="tipFoot"><button type="button" class="tipBtn" id="saveBtn" onclick="saveConf()">确 定</button><button type="button" class="tipBtn tip-cancel" onclick="addCancel()">取 消</button></div>
	</form>
 	</div>	
</temple:write>
<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/servicecode/rule.js"></script>