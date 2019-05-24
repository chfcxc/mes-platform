package cn.emay.eucp.web.client.controller.fms.account;

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
import cn.emay.eucp.common.dto.fms.account.AccountDTO;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.data.service.fms.FmsAccountService;
import cn.emay.eucp.data.service.fms.FmsBusinessTypeService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("FMS_CLINT_ACCOUNT_QUERY")
@RequestMapping("/fms/client/account")
@Controller
public class FmsAccountController {
	@Resource
	private FmsAccountService fmsAccountService;
	@Resource
	private FmsBusinessTypeService fmsBusinessTypeService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<FmsBusinesTypeDto> list = fmsBusinessTypeService.findIds(null);
		Map<String, List<FmsBusinesTypeDto>> map = new HashMap<String, List<FmsBusinesTypeDto>>();
		for (FmsBusinesTypeDto fmsBusinesTypeDto : list) {
			if (map.isEmpty()) {
				List<FmsBusinesTypeDto> list2 = new ArrayList<FmsBusinesTypeDto>();
				list2.add(fmsBusinesTypeDto);
				map.put(fmsBusinesTypeDto.getParentName(), list2);
			} else {
				if (!map.containsKey(fmsBusinesTypeDto.getParentName())) {
					List<FmsBusinesTypeDto> list1 = new ArrayList<FmsBusinesTypeDto>();
					list1.add(fmsBusinesTypeDto);
					map.put(fmsBusinesTypeDto.getParentName(), list1);
				} else {
					String parentName = fmsBusinesTypeDto.getParentName();
					List<FmsBusinesTypeDto> list3 = map.get(parentName);
					list3.add(fmsBusinesTypeDto);
					map.put(parentName, list3);
				}

			}
		}
		model.addAttribute("map", map);
		return "fms/account/list";
	}

	@RequestMapping("ajax/list")
	public void findlist(HttpServletRequest request, HttpServletResponse response) {
		String appId = RequestUtils.getParameter(request, "appId");
		Long serviceCodeId = RequestUtils.getLongParameter(request, "serviceCodeId", 0L);
		Long businessTypeId = RequestUtils.getLongParameter(request, "businessTypeId", 0L);
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 0);
		Enterprise enterprise = WebUtils.getCurrentEnterprise(request, response);
		Page<AccountDTO> page = fmsAccountService.findClientList(appId, serviceCodeId, start, limit, businessTypeId, enterprise.getId());
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

}
