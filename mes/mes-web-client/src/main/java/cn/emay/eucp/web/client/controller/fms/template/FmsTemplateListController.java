package cn.emay.eucp.web.client.controller.fms.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.util.FmsControllerUtil;
import cn.emay.eucp.data.service.fms.FmsBusinessTypeService;
import cn.emay.eucp.data.service.fms.FmsServiceCodeService;
import cn.emay.eucp.data.service.fms.FmsTemplateService;
import cn.emay.eucp.data.service.fms.FmsTemplateServiceCodeAssignService;
import cn.emay.eucp.data.service.fms.FmsUserServiceCodeAssignService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("FMS_CLINT_REPORTED_QUERY")
@RequestMapping("/fms/client/reportedquery")
@Controller
public class FmsTemplateListController {
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
	private RedisClient redisClient;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<FmsBusinesTypeDto> list = fmsBusinessTypeService.findIds(null);
		Map<String, List<FmsBusinesTypeDto>> map = new HashMap<String, List<FmsBusinesTypeDto>>();
		for (FmsBusinesTypeDto fmsBusinesTypeDto : list) {
			if (!map.isEmpty()) {
				if (!map.containsKey(fmsBusinesTypeDto.getParentName())) {
					List<FmsBusinesTypeDto> list2 = new ArrayList<FmsBusinesTypeDto>();
					list2.add(fmsBusinesTypeDto);
					map.put(fmsBusinesTypeDto.getParentName(), list2);
				} else {
					String parentName = fmsBusinesTypeDto.getParentName();
					List<FmsBusinesTypeDto> list3 = map.get(parentName);
					list3.add(fmsBusinesTypeDto);
					map.put(parentName, list3);
				}
			} else {
				List<FmsBusinesTypeDto> list2 = new ArrayList<FmsBusinesTypeDto>();
				list2.add(fmsBusinesTypeDto);
				map.put(fmsBusinesTypeDto.getParentName(), list2);
			}
		}
		model.addAttribute("map", map);
		return "fms/reported/reportedquery";
	}

	@RequestMapping("list")
	public void list(HttpServletRequest request, HttpServletResponse response, Model model) {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String appIdString = request.getParameter("appId");
		String saveTypeString = request.getParameter("saveType");
		Integer saveType = null;
		try {
			saveType = Integer.parseInt(saveTypeString);
		} catch (Exception e) {
		}
		Integer messageType = null;
		String messageTypesString = request.getParameter("messageType");
		try {
			messageType = Integer.parseInt(messageTypesString);
		} catch (Exception e) {
		}
		Integer submitType = null;
		String submitTypesString = request.getParameter("submitType");
		try {
			submitType = Integer.parseInt(submitTypesString);
		} catch (Exception e) {
		}
		Integer auditState = null;
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		Date startTime = RequestUtils.getDateParameter(request, "startTime", "yyyy-MM-dd HH:mm:ss", null);
		Date endTime = RequestUtils.getDateParameter(request, "endTime", "yyyy-MM-dd HH:mm:ss", null);
		Long businessTypeId = RequestUtils.getLongParameter(request, "businessTypeId", 0L);
		Long contentTypeId = RequestUtils.getLongParameter(request, "contentTypeId", 0L);
		User user = WebUtils.getCurrentUser(request, response);
		List<FmsServiceCode> serviceCodes = fmsUserServiceCodeAssignService.findByAssignUserId(user.getId());
		Set<String> appIdSet = new HashSet<String>();
		for (FmsServiceCode fmsServiceCode : serviceCodes) {
			appIdSet.add(fmsServiceCode.getAppId());
		}
		List<String> appIds = new ArrayList<String>();
		if (!StringUtils.isEmpty(appIdString)) {
			if (appIdString.contains("EUCP-EMY-")) {
				if (!appIds.contains(appIdString)) {
					ResponseUtils.outputWithJson(response, Result.badResult("暂无数据"));
					return;
				}
				appIds.add(appIdString);
			} else {
				Long serviceCodeId = null;
				try {
					serviceCodeId = Long.parseLong(appIdString);
				} catch (Exception e) {
				}
				if (null != serviceCodeId) {
					FmsServiceCode fmsServiceCode = fmsServiceCodeService.findById(serviceCodeId);
					if (null != fmsServiceCode) {
						if (!appIds.contains(appIdString)) {
							ResponseUtils.outputWithJson(response, Result.badResult("暂无数据"));
							return;
						}
						appIds.add(fmsServiceCode.getAppId());
					} else {
						ResponseUtils.outputWithJson(response, Result.badResult("暂无数据"));
						return;
					}
				} else {
					ResponseUtils.outputWithJson(response, Result.badResult("暂无数据"));
					return;
				}
			}
		} else {
			appIds.addAll(appIdSet);
		}
		Page<FmsTemplateDto> page = fmsTemplateServiceCodeAssignService.findTempLetPage(title, content, appIds, businessTypeId, saveType, contentTypeId, messageType, submitType, auditState,
				startTime, endTime, start, limit, -1);
		FmsControllerUtil.fillServiceCodeId(page, redisClient);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}
}
