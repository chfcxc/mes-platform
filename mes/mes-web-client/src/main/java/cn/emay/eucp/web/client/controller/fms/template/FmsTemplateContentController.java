package cn.emay.eucp.web.client.controller.fms.template;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.eucp.common.cache.GlobalConstant;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.util.TemplateInfoParseUtil;
import cn.emay.eucp.data.service.fms.FmsBusinessTypeService;
import cn.emay.eucp.data.service.fms.FmsServiceCodeService;
import cn.emay.eucp.data.service.fms.FmsTemplateService;
import cn.emay.eucp.data.service.fms.FmsTemplateServiceCodeAssignService;
import cn.emay.eucp.data.service.fms.FmsUserServiceCodeAssignService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("FMS_CLINT_REPORTED_CONTENT")
@RequestMapping("/fms/client/reportedcontent")
@Controller
public class FmsTemplateContentController {
	@Resource
	private FmsTemplateServiceCodeAssignService fmsTemplateServiceCodeAssignService;
	@Resource
	private FmsServiceCodeService fmsServiceCodeService;
	@Resource
	private FmsUserServiceCodeAssignService fmsUserServiceCodeAssignService;
	@Resource
	private FmsTemplateService fmsTemplateService;
	@Resource
	private FmsBusinessTypeService fmsBusinessTypeService;
	@Resource
	private RedisClient redis;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		User currentUser = WebUtils.getCurrentUser(request, response);
		// Integer templateType = RequestUtils.getIntParameter(request, "templateType", 0);
		// List<FmsServiceCodeDto> result = new ArrayList<FmsServiceCodeDto>();
		List<FmsServiceCodeDto> fmsServiceCodeList = fmsServiceCodeService.findFmsServiceCodeDtoByUserId(currentUser.getId());
		// for (FmsServiceCodeDto dto : fmsServiceCodeList) {
		// String appId = dto.getAppId();
		// for (String operatorCode : GlobalConstant.OPERATOR_CODES) {
		// String field = appId + "," + templateType + "," + operatorCode;
		// Long channelId = redis.hget(RedisConstants.FMS_APPID_CHANNELID_HASH, field, Long.class);
		// if (channelId != null) {
		// result.add(dto);
		// break;
		// }
		// }
		//
		// }
		model.addAttribute("fmsServiceCodeList", fmsServiceCodeList);
		return "fms/reported/contentReport";
	}

	@RequestMapping("reportTemplate")
	public void reportTemplate(HttpServletRequest request, HttpServletResponse response, Model model) {
		User currentUser = WebUtils.getCurrentUser(request, response);
		String templateName = RequestUtils.getParameter(request, "templateName");
		String content = RequestUtils.getParameter(request, "content");
		String appId = RequestUtils.getParameter(request, "appId");
		// 校验
		if (templateName == null || templateName.isEmpty()) {
			ResponseUtils.outputWithJson(response, Result.badResult("模板名称不能为空"));
			return;
		}
		if (content == null || content.isEmpty()) {
			ResponseUtils.outputWithJson(response, Result.badResult("模板内容不能为空"));
			return;
		}
		if (content.length() > GlobalConstant.TEMPLATE_MAX_LENGTH) {
			ResponseUtils.outputWithJson(response, Result.badResult("模板内容不能超过70个字"));
			return;
		}
		List<String> varList = TemplateInfoParseUtil.getVarList(content);
		if (varList.size() > GlobalConstant.TEMPLATE_VAR_MAX_NUM) {
			ResponseUtils.outputWithJson(response, Result.badResult("模板变量不能超过三个"));
			return;
		}
		Set<String> set = new HashSet<String>();
		set.addAll(varList);
		if (varList.size() != set.size()) {
			ResponseUtils.outputWithJson(response, Result.badResult("模板变量重复"));
			return;
		}
		String variable = org.apache.commons.lang3.StringUtils.join(varList.toArray(), ",");
		// 保存
		fmsTemplateService.addTemplate(appId, templateName, content, variable, currentUser.getId());
		ResponseUtils.outputWithJson(response, Result.rightResult(variable));
		return;

	}

}
