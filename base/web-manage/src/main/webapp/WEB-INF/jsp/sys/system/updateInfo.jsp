<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/system/updateInfo.css" type="text/css" />
</temple:write>
<temple:write property="context">
	<div id="outerId"></div>
	<input type="text" id="oldcontent" onchange="translateContent(this)" />
	<input type="hidden" id="content" />
	<select id="versionSearch"></select>
	<!--FQA新增修改 -->
	<div class="tipcen disno" id="warp">
		<div class="clear"  id="add">
			<form method="post" id="addForm">
				<input type="hidden" id="systemId" />
				<div class="item">
					<label class="item-label"><span class="xing">*</span>版本号：</label>
					<input type="text" id="version"  class="item-text" name="version" maxlength="20" />
				</div><br />
				<div class="item">
		              <label class="item-label"><span class="xing">*</span>发布时间：</label>
		              <input type="text" id="pubTime" class="item-text" name="pubTime"  />
		        </div><br />
				<div class="item"><label class="item-label">
					<span class="xing">*</span>所属系统：</label>
					<select class="item-select" id="subSystem" name="subSystem" onclick="getBusinessType(this)">
                   	    <!-- <option value="">---请选择---</option>   -->
                        <option value="1">管理系统</option>
                        <option value="2">销售系统</option>
                        <option value="3">客户系统</option>
                    </select>
				</div><br />
				<div class="item"><label class="item-label">
					<span class="xing">*</span>业务类型：</label>
					<select class="item-select" id="businessType" name="businessType" >
                   	     <!--<option value="">---请选择---</option>  -->  
                        <option value="1">短信</option>
                        <option value="2">彩信</option>
                        <option value="3">流量</option>
                        <option value="4">国际短信</option>
                        <option value="5">语音</option> 
                        <option value="6">金融</option> 
                    </select>
				</div>
				<div class="item hh130">
					<label class="item-label"><span class="xing">*</span>更新内容：</label>
					<textarea id="updateInfo" class="textareas" maxlength="1000" name="updateInfo"></textarea>
				</div>
				<div class="tipFoot">
					<button onclick="addTure()" id="saveBtn" class="tipBtn" type="submit">确 定</button>
				    <button class="tipBtn tip-cancel" type="button" onclick="addCancel()">取 消</button>
			    </div>
			</form>
		</div>
	</div>
</temple:write>
<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/system/updateInfo.js"></script>