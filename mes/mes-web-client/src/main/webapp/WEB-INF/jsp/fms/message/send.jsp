<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/client/message/send.css" type="text/css" />
</temple:write>
<temple:write property="context">
	<ul class="card">
		<li class="li-on">普通发送</li>
		<li>个性发送</li>
	</ul>
	<div class="sms-wrap card-cen" style="display:block">
		<div class="sms-left">
			<div class="shangchuan">
	                    <div class="uPloadBtn_box ">
		                    <div class="uPloadBtn_title ">
		                    	<button onclick="templateDown()" title="模板下载" href="javascript:void(0)" class="tempatedown">模板下载</button>
		                    	<div class="uPloadBtn uPloadBtnxin">
		                        	<label class="tempatedown" for="fileId" >上传文件</label>                           
		                       		<input type="file" title="上传文件" class="input-file" id="fileId" name="fileId"  onChange="btfile(this,1)"/>
	                        	</div>
	                        	<input type="tetx" class="inputspan"  id="inputspan" disabled="disabled " />
	                        	<span class="spantext">Excel小于5M &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		                    	<span id="fileIdName"></span>
		                    </div>
		                </div>
		             </div>
			<div class="sms-item prelative-mobile">
				<div class="fielsBtn">
				 	<a onclick="unique()" class="tipBtn tipBtnNew">号码去重</a>
				</div>
				<div class="textarea-box">
					<label class="item-label lh100">请输入手机号码：</label>
					<textarea onblur="countMobile()" id="mobiles" ></textarea>
					<div class="clear"></div>
					<div class="empty_box" onclick="emptyContent(this)"><i class="iCon grey-delete" title="清空"></i></div>
					<!-- <div class="tips">手机号支持<span class="red">换行、中英文逗号</span>分隔，建议手机号超过<span class="red">1000</span>个使用号码导入方式</div> -->
				</div>
			</div>
			<div class="bttomColor"></div>
			<div class="templatetabel">
				<div class="">
						<label class="item-label lh100">选择报备模板：</label>
						<input type="text" id="templateName" onclick="gettemplate()" />
				</div>
				<div id='templateTypeBox'></div>
				<div class="serviceCodeabox">
					<label>服务号：</label><span id="serviceCode"></span>
				</div>
				<table class="emtable_table_table">
					<thead>
						<tr><td>保存类型</td><td>业务类别</td><td>内容类别</td><td>移动</td><td>联通</td><td>电信</td></tr>
					</thead>
					<tbody>
						<tr><td id="saveType"></td><td id="businessType"></td><td id="contenttype"></td><td id="operatorCmcc"></td><td id="operatorCucc"></td><td id="operatorCtcc"></td></tr>
					</tbody>
				</table>
				<input type="hidden" id="appId" />
				<input type="hidden" id="templateId" />
				<input type="hidden" id="templateContent" />
				<input type="hidden" id="contentTypeId" />
				<input type="hidden" id="serviceCodeId" />
				
			</div>
			<div class="bottom-btn">
                <div id="sendBtn" onclick="sendMessage()" class="btn-blue">发送</div>
                <span class="pr20"></span>
                <!-- <div onclick="clearPt()" class="btn-blue">取消</div> -->
            </div>
		</div>
		<div class="sms-right">
			<div class="phone">
				<div class="phone-top" id="simulation"></div>
				<div class="phone-bottom" >
					<p id="simulation-bottom" id="templatebox"></p>
				</div>
			</div>
		</div>
		<div class="clear"></div>
		</div>
		
		<!-- 个性发送 -->
		<div class="sms-wrap card-cen">
		<div class="sms-left">
			<div class="shangchuan">
	                    <div class="uPloadBtn_box ">
		                    <div class="uPloadBtn_title ">
		                    	<button onclick="batchTemplate()" title="模板下载" href="javascript:void(0)" class="tempatedown">模板下载</button>
		                    	<div class="uPloadBtn uPloadBtnxin">
		                        	<label class="tempatedown" for="fileIdPs" >上传文件</label>                           
		                       		<input type="file" title="上传文件" class="input-file" id="fileIdPs" name="fileId"  onChange="btfile(this,3)"/>
	                        	</div>
	                        	<input type="tetx" class="inputspan"  id="inputspan" disabled="disabled " />
	                        	<span class="spantext">Excel小于5M &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		                   		<span id="fileIdNamePs"></span>
		                    </div>
		                </div>
		             </div>
			<!-- <div class="sms-item prelative-mobile">
				<div class="fielsBtn">
				 	<a onclick="unique()" class="tipBtn tipBtnNew">号码去重</a>
				</div>
				<div class="textarea-box">
					<label class="item-label lh100">请输入手机号码：</label>
					<textarea onblur="countMobile()" id="mobilesPs" ></textarea>
					<div class="clear"></div>
					<div class="empty_box" onclick="emptyContent(this)"><i class="iCon grey-delete" title="清空"></i></div>
					<div class="tips">手机号支持<span class="red">换行、中英文逗号</span>分隔，建议手机号超过<span class="red">1000</span>个使用号码导入方式</div>
				</div>
			</div> -->
			<div class="bttomColor"></div>
			<div class="templatetabel">
				<div class="">
						<label class="item-label lh100">选择报备模板：</label>
						<input type="text" id="templateNamePs" onclick="gettemplate()" />
						<span style="margin-left:20px;"></span>
				</div>
				<!-- <div class="">
					<label>模板备注：</label><span id="templateramk">..</span>
				</div> -->
				<div class="serviceCodeabox">
					<label>服务号：</label><span id="serviceCodePs"></span>
				</div>
				<table class="emtable_table_table">
					<thead>
						<tr><td>保存类型</td><td>业务类别</td><td>内容类别</td><td>移动</td><td>联通</td><td>电信</td></tr>
					</thead>
					<tbody>
						<tr><td id="saveTypePs"></td><td id="businessTypePs"></td><td id="contenttypePs"></td><td id="operatorCmccPs"></td><td id="operatorCuccPs"></td><td id="operatorCtccPs"></td></tr>
					</tbody>
				</table>
				<input type="hidden" id="appIdPs" />
				<input type="hidden" id="templateIdPs" />
				<input type="hidden" id="templateContentPs" />
				<input type="hidden" id="contentTypeIdPs" />
				<input type="hidden" id="serviceCodeIdPs" />
			</div>
			<div class="bottom-btn">
                <div id="sendBtn" onclick="sendMessagePs()" class="btn-blue">发送</div>
                <span class="pr20"></span>
               <!--  <div onclick="clearPt()" class="btn-blue">取消</div> -->
            </div>
		</div>
		<!-- <div class="sms-right">
			<div class="phone">
				<div class="phone-top" id="simulation"></div>
				<div class="phone-bottom" >
					<p id="simulation-bottom" id="templatebox"></p>
				</div>
			</div>
		</div> -->
		<div class="clear"></div>
		</div>
		
		

</temple:write>
<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/client/message/send.js"></script>