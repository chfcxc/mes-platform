<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="temple" uri="/temple"%>
<temple:write property="head">
	<link rel="stylesheet" href="${WEB_STATIC_PATH}/css/manage/systeminfo/systeminfo.css" type="text/css" />
</temple:write>
<temple:write property="context">
<div class="sysbox">
	<h2>系统配置</h2>
	<table>
		<thead>
			<tr>
				<th>名称</th>
				<th>说明</th>
				<temple:auth property="OPER_SYS_BASE_INFO_UPDATE"><th>操作</th></temple:auth>
			</tr>
		</thead>
		<tbody id="mainTab" >
		</tbody>
	</table>
</div>
</temple:write>

<%@ include file="../../temple/auth/authTemple.jsp"%>
<script type="text/javascript" src="${WEB_STATIC_PATH}/js/manage/sys/baseinfo/systeminfo/systeminfo.js"></script>



