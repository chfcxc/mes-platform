<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="crumbs">
	> 新增	
</temple:write>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/channel/channel.css" type="text/css" />
</temple:write>
<temple:write property="context">
       <div class="content-box-conmmon">
           <form id="tempForm">
               <div class="tl">
               	   <div class="channel_basicNew" id="basicAttr">基本参数</div>
               	   <div class="senior-send">
                   <div class="item">
                         <label class="item-label"><span class="xing">*</span>通道名称</label>
                         <input type="text" id="channelName" name="channelName" class="item-text"   maxlength="30" />
                   </div>
                    <div class="item">
                         <label class="item-label"><span class="xing">*</span>通道号</label>
                         <input type="text" id="channelNumber" name="channelNumber" class="item-text"  maxlength="30"  />
                   </div>
                  <!--  <div class="item" style="display:none;">
	                       <label class="item-label"><span class="xing">*</span>通道状态</label>
	                       <select class="item-select" id="channelshate" name="channelshate">
	                           <option value="0">停用</option>
	                           <option value="1">启用</option>
	                       </select>
	                   </div> -->
	                    <div class="item">
	                         <label class="item-label"><span class="xing">*</span>发送速度</label>
	                         <input type="text" id="sendSpeed" name="sendSpeed" class="item-text" />
	                   </div>
	                   <div class="item">
	                         <label class="item-label"><span class="xing">*</span>业务类型&nbsp;&nbsp;</label>
	                        <select id="businessType" name="businessType" onchange="chooseType()">
	                        	<option value="">--请选择--</option>
								<c:forEach items="${map}" var="map">
									<option value="${map.key}">${map.key}</option>
								</c:forEach>
						   </select>
	                   </div>
	                   <div class="item">
	                         <label class="item-label"><span class="xing">*</span>保存类型&nbsp;&nbsp;</label>
	                        <select id="saveType" name="saveType" onchange="chooseType()">
								<option value="">--请选择--</option>
					        	<option value="1">可以保存</option>
					        	<option value="2">不可保存</option>
						   </select>
	                   </div>
	                   <div class="item">
	                         <label class="item-label"><span class="xing">*</span>内容类别&nbsp;&nbsp;</label>
	                        <select id="businessTypeId" name="businessTypeId" onclick="changecontent(this)">
	                        	<option value="">--请选择--</option>
								<c:forEach items="${map}" var="dataMap"> 
							        <c:forEach items="${dataMap.value}" var="value"> 
							        	<option style="display:none;" data-savetype="${value.saveType}" data-parentname="${value.parentName}" value="${value.id}">${value.name}</option>
							        </c:forEach>                 
							    </c:forEach>
						   </select>
	                   </div>
	                   
	                   <div class="item cmcuct" id="templateType">
	                       <label><span class="xing">*</span>允许支持信息类型&nbsp;&nbsp;</label> 
	                       <label><input type="checkbox" value="0"  name="common" id="common" />普通</label> 
	                       <label><input type="checkbox" value="1"  name="personal" id="personal"/>个性</label> 
	                   </div>
	                   <div class="item cmcuct" id="operator">
	                         <label class="item-label"><span class="xing">*</span>允许发送的运营商</label>
	                        &nbsp;&nbsp; <input type="checkbox" name="operatorCmcc"  value="1"  />&nbsp;&nbsp;移动
	                         			 <input type="checkbox" name="operatorCucc"  value="2" />&nbsp;&nbsp;联通
	                           			 <input type="checkbox" name="operatorCtcc"  value="3"  />&nbsp;&nbsp;电信
	                   </div>
	                   <div class="item">
	                         <label class="item-label"><span class="xing">*</span>移动通道字数限制</label>
	                         <input type="text" id="cmccLimit" name="cmccLimit" class="item-text" value="70" />
	                   </div>
	                   <div class="item">
	                         <label class="item-label"><span class="xing">*</span>联通通道字数限制</label>
	                         <input type="text" id="cuccLimit" name="cuccLimit" class="item-text" value="70" />
	                   </div>
	                   <div class="item">
	                         <label class="item-label"><span class="xing">*</span>电信通道字数限制</label>
	                         <input type="text" id="ctccLimit" name="ctccLimit" class="item-text" value="70" />
	                   </div>
	                   <div class="item">
	                       <label class="item-label"><span class="xing">*</span>是否需要报备</label>
	                       <select class="item-select" id="isNeedReport" name="isNeedReport" onchange="showreportType(this)">
	                           <option value="0">不需要报备</option>
	                           <option value="1">需要报备</option>
	                       </select>
	                   </div>
	                   <div class="item" style="display:none;">
	                       <label class="item-label"><span class="xing">*</span>报备类型</label>
	                       <select class="item-select" id="reportType" name="reportType" style="width:180px;">
	                           <option value="0">线上报备线上回模板ID</option>
	                           <option value="1">线上报备线下回模板ID</option>
	                           <option value="2">线下报备线上回模板ID</option>
	                           <option value="3">线下报备线下回模板ID</option>
	                       </select>
	                   </div>
                <div class="senior">
                	 <div class="channel_basicNew" id="protocolAttr">协议参数配置
						<div class="fielsBtn">
						 	<a class="tipBtn tipBtnNew" onclick="addField()">添加字段</a>
						</div> 
                	 </div>
                	 <div class="senior-send">
	                   <div id="fmsChannel">
		                   <div id="thirdChannel"></div>
	                   </div>
                	</div>
				</div>
               <div class="tipFoot">
                   <button  type="submit" class="tipBtn" id="saveBtn" onclick="sanAddTure()">保 存</button>
                   <button  onclick="javascript:goPage('/fms/channel/manage');" type="button" class="tipBtn tip-cancel">取 消</button>
              </div>
           </form>
       </div>
		 <!--  <div class="addBox" id="addBox">
	  		<form id="addFormFiled">
		       	<div class="item">
		            <label class="item-label"><span class="xing">*</span>字段名</label>
		            <input type="text" id="filedName" name="filedName" class="item-text"  />
		        </div>
		        <div class="item">
		            <label class="item-label"><span class="xing">*</span>字段标识</label>
		            <input type="text" id="fieldIdent" name="fieldIdent" class="item-text"  />
		        </div>
		        <div class="item">
		            <label class="item-label"><span class="xing">*</span>值</label>
		            <input type="text" id="fieldVal" name="fieldVal" class="item-text"  maxLength="512"  />
		        </div>
		      	<div class="tipFoot">
		      		<button id="addBtnAttr"  class="tipBtn" type="submit" onclick="addFieldBtn()" >添加字段</button>
		      		<button id="cancelBtnAttr"  class="tipBtn" type="button" onclick="cancelText()">取消</button>
		      	</div>
	   		</form>
		</div>  -->
</temple:write>

<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/channel/add.js"></script>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/common/common.js"></script>
