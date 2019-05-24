<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="temple" uri="/temple"%>

${fullhtml}

<script type="text/javascript" id="scriptFor">
	
	var WEB_SERVER_PATH = '${WEB_SERVER_PATH}';
	var WEB_STATIC_PATH = '${WEB_STATIC_PATH}';
	var PASSPORT_PATH = '${PASSPORT_PATH}';
	var LOCAL_SYSTEM = '${LOCAL_SYSTEM}';
	var DOMAIN_PATH = '${DOMAIN_PATH}';
	var enterpriseId = '${enterpriseId}';
	
	var CURRENT_PAGE_CODE = '${CURRENT_PAGE_CODE}';
	var WEB_BUSINESS_CODE = '${WEB_BUSINESS_CODE}';
	
	var CURRENT_USER_ID = '${CURRENT_USER_ID}';
	var CURRENT_USER_USERNAME = '${CURRENT_USER_USERNAME}';
	var CURRENT_USER_REALNAME = '${CURRENT_USER_REALNAME}';
	var CURRENT_MOUDLE_CODE = '${CURRENT_MOUDLE_CODE}';
	
	
	${AUTH_SCRIPT}
	
</script>

<div id="headFor">
	<temple:block property="head" />
</div>

<title id="titleFor">${CURRENT_PAGE_NAME == null ? 'EUCP' : CURRENT_PAGE_NAME}</title>

<div id="crumbsForMoudle">
${CURRENT_MOUDLE_NAME}
</div>

<div id="crumbsForNav">
${CURRENT_NAV_NAME}
</div>

<div id="crumbsForPage">
${CURRENT_PAGE_NAME}
</div>

<div id="crumbsForPageUrl">
${WEB_SERVER_PATH}${CURRENT_PAGE_URL}
</div>

<div id="crumbsForSupport">
<temple:block property="crumbs" />
</div>

<div id="contextFor">
	<temple:block property="context" />
</div>
<style>
.promptBox{width:300px;height:200px;position: fixed;right: 5px;bottom:0px;z-index:9999;}
.promptTitle{width:100%;height:35px;background:#248dc1;color:#fff;line-height:35px;padding-left:30px;box-sizing: border-box;}
.promptcontent{width:100%;height:160px;text-algin:center;background:#fff;box-sizing: border-box;}
.promptcontent a{display:block;padding-top:60px;text-align: center;cursor:pointer;box-sizing: border-box;}
</style>
	<div class="promptBox" style="display:none" >
		<div class="promptTitle">消息提示</div>
		<div class="promptcontent">
			<a href="${WEB_SERVER_PATH}/voice/manage/auditing">您有未审核信息</a>
		</div>
	</div>
<script>
/*  $(function(){
	 aa();
	 setInterval(function(){aa();slDown();},300000);
});
function aa(){
	$.ajax({
		url : WEB_SERVER_PATH + '/voice/manage/auditremind/ajax/info',
		type : 'post',
		dataType : 'json',
		success : function(data) {
			if (data.success) {
				var type=data.result;
				if(type!='false'){
					$(".promptBox").slideDown(1000);
					slDown();
				}
			} else {
				$.fn.tipLoddingEnd(false);
				$.fn.tipAlert(data.message,1.5,0);
			}
		},
		error : function() {
			$.fn.tipLoddingEnd(false);
			$.fn.tipAlert('系统异常',1.5,2);
		}
	});
} */
function slDown(){
	setTimeout(function(){ $(".promptBox").remove()},5000);
}
</script>