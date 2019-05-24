package cn.emay.eucp.web.client.controller.fms.servicecode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.data.service.fms.FmsBusinessTypeService;
import cn.emay.eucp.data.service.fms.FmsServiceCodeService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("FMS_CLINT_SERVICECODE_MANAGE")
@RequestMapping("/fms/client/servicecode")
@Controller
public class FmsServiceCodeController {
	@Resource
	private FmsServiceCodeService fmsServiceCodeService;
	@Resource
	private FmsBusinessTypeService fmsBusinessTypeService;

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
		return "fms/servicecode/list";
	}

	@RequestMapping("ajax/list")
	public void findlist(HttpServletRequest request, HttpServletResponse response) {
		String appId = RequestUtils.getParameter(request, "appId");
		String serviceCode = RequestUtils.getParameter(request, "serviceCode");
		/*
		 * int savetype = RequestUtils.getIntParameter(request, "savetype", 0); String contenttype = RequestUtils.getParameter(request, "contenttype"); String businessType =
		 * RequestUtils.getParameter(request, "businessType");
		 */
		Long businessTypeId = RequestUtils.getLongParameter(request, "businessTypeId", 0L);
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 0);
		Enterprise enterprise = WebUtils.getCurrentEnterprise(request, response);
		Page<FmsServiceCodeDto> page = fmsServiceCodeService.findClientList(appId, serviceCode, businessTypeId, start, limit, enterprise.getId());
		ResponseUtils.outputWithJson(response, Result.rightResult(page));

	}

	@RequestMapping("ajax/info")
	public void findid(HttpServletRequest request, HttpServletResponse response) {
		long id = RequestUtils.getLongParameter(request, "id", 0L);
		FmsServiceCode servicecode = fmsServiceCodeService.findById(id);
		if (null == servicecode) {
			ResponseUtils.outputWithJson(response, Result.badResult("此服务号不存在"));
			return;
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(servicecode));
	}

	@RequestMapping("ajax/update")
	public void update(HttpServletRequest request, HttpServletResponse response) {
		long id = RequestUtils.getLongParameter(request, "id", 0L);
		String serviceCode = RequestUtils.getParameter(request, "serviceCode");
		FmsServiceCode servicecode = fmsServiceCodeService.findById(id);
		if (null == serviceCode) {
			ResponseUtils.outputWithJson(response, Result.badResult("此服务号不存在"));
			return;
		}
		servicecode.setServiceCode(serviceCode);
		fmsServiceCodeService.update(servicecode);
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}
}
