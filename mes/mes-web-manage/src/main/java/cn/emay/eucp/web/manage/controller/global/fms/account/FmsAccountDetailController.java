package cn.emay.eucp.web.manage.controller.global.fms.account;

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
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.account.AccountDetailDTO;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.fms.FmsAccountDetailService;
import cn.emay.eucp.data.service.fms.FmsBusinessTypeService;
import cn.emay.eucp.data.service.system.EnterpriseService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("FMS_ACCOUNT_DETAIL")
@RequestMapping("/fms/account/detail")
@Controller
public class FmsAccountDetailController {
	@Resource
	private EnterpriseService enterpriseService;
	@Resource
	private FmsAccountDetailService fmsAccountDetailService;
	@Resource
	private FmsBusinessTypeService fmsBusinessTypeService;
	@Resource
	private UserService userService;

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
		return "fms/account/detail";
	}

	@RequestMapping("ajax/list")
	public void findlist(HttpServletRequest request, HttpServletResponse response) {
		Long serviceCodeId = RequestUtils.getLongParameter(request, "serviceCodeId", 0L);
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 0);
		Long businessTypeId = RequestUtils.getLongParameter(request, "businessTypeId", 0L);
		int operationType = RequestUtils.getIntParameter(request, "operationType", -1);
		Date startTime = RequestUtils.getDateParameter(request, "startTime", "yyyy-MM-dd HH:mm:ss", null);
		Date endTime = RequestUtils.getDateParameter(request, "endTime", "yyyy-MM-dd HH:mm:ss", null);
		Page<AccountDetailDTO> page = fmsAccountDetailService.findlist(serviceCodeId, businessTypeId, start, limit, startTime, endTime, operationType);

		Set<Long> enterpriseIdList = new HashSet<Long>();
		Set<Long> userIdList = new HashSet<Long>();
		if (null != page.getList() && page.getList().size() > 0) {
			for (AccountDetailDTO dto : page.getList()) {
				enterpriseIdList.add(dto.getEnterpriseId());
				userIdList.add(dto.getUserId());
			}
		}
		List<Enterprise> findByIds = null;
		if (null != enterpriseIdList && enterpriseIdList.size() > 0) {
			findByIds = enterpriseService.findByIds(enterpriseIdList);
		}
		Map<Long, String> usermap = new HashMap<Long, String>();
		List<User> findUserByIds = userService.findUserByIds(userIdList);
		for (User user : findUserByIds) {
			usermap.put(user.getId(), user.getUsername());
		}
		Map<Long, Enterprise> map = new HashMap<Long, Enterprise>();
		if (null != findByIds) {
			for (Enterprise enterprise : findByIds) {
				map.put(enterprise.getId(), enterprise);
			}
		}

		if (null != page.getList() && page.getList().size() > 0) {
			for (AccountDetailDTO dto : page.getList()) {
				Enterprise enterprise = map.get(dto.getEnterpriseId());
				dto.setNameCn(enterprise.getNameCn());
				dto.setClientNumber(enterprise.getClientNumber());
				dto.setUserName(usermap.get(dto.getUserId()));
			}
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}
}
