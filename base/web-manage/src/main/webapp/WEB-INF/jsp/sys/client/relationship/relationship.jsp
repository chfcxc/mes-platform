<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/relationship/relationship.css" type="text/css" />
</temple:write>
<temple:write property="context">
	<input type="hidden" id="isAll" value="1" /><div id="outerId"></div>
	<!-- 调整选中 -->
	<div class="tipcen disno" id="warp">
		<div class="clear"  id="add">
			<form method="post" id="addForm">
				<div class="item xing" id="tips">提示：将选中的客户调整到其他销售名下</div>
				<div class="usernamebox">
					<div class="item">
						<label class="item-label"><span class="xing">*</span>指定销售：</label>
						<input type="hidden" id="usernameVal" />
						<input type="text" name="username" class="item-text" id="username" readonly="readonly" />
					</div>
					<div class="tipFoot chooseBtn">
						<button type="button" class="tipBtn" onclick="chooseCustomerName()">选 择</button>
					</div>
					<div class="clear"></div>
				</div>
				<div class="tipFoot">
					<button onclick="adjustBtn()" id="saveBtn" class="tipBtn" type="submit">确 定</button>
				    <button class="tipBtn tip-cancel" type="button" onclick="addCancel()">取 消</button>
			    </div>
			</form>
		</div>
	</div>
</temple:write>
<%@ include file="../../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/client/relationship/relationship.js"></script>