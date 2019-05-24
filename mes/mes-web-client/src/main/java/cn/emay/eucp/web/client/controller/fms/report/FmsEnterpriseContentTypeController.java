package cn.emay.eucp.web.client.controller.fms.report;

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
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.dto.fms.statices.FmsEnterpriseContentTypeReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBusinessType;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.data.service.fms.FmsBusinessTypeService;
import cn.emay.eucp.data.service.fms.FmsEnterpriseContentTypeDayService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("FMS_CLINT_REPORT_CONTENTTYPE")
@RequestMapping("/fms/client/reportcontenttype")
@Controller
public class FmsEnterpriseContentTypeController {
	@Resource
	private FmsEnterpriseContentTypeDayService fmsEnterpriseContentTypeDayService;
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
		return "fms/report/contenttype";
	}

	@RequestMapping("ajax/list")
	public void findlist(HttpServletRequest request, HttpServletResponse response) {
		String rate = RequestUtils.getParameter(request, "rate");
		Long serviceCodeId = RequestUtils.getLongParameter(request, "serviceCodeId", 0L);
		Date startTime = RequestUtils.getDateParameter(request, "startTime", "yyyy-MM-dd HH:mm:ss", null);
		Date endTime = RequestUtils.getDateParameter(request, "endTime", "yyyy-MM-dd HH:mm:ss", null);
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		Long businessTypeId = RequestUtils.getLongParameter(request, "businessTypeId", 0L);
		Enterprise enterprise = WebUtils.getCurrentEnterprise(request, response);
		Page<FmsEnterpriseContentTypeReportDto> page = fmsEnterpriseContentTypeDayService.findPage(rate, businessTypeId, serviceCodeId, startTime, endTime, start, limit,
				String.valueOf(enterprise.getId()));
		Set<Long> set = new HashSet<Long>();
		Set<Long> businessId = new HashSet<Long>();
		Map<Long, String> map = new HashMap<Long, String>();
		if (page.getList() != null && page.getList().size() > 0) {
			for (FmsEnterpriseContentTypeReportDto dto : page.getList()) {
				set.add(dto.getEnterpriseId());
				businessId.add(dto.getBusinessTypeId());
			}
			List<FmsBusinessType> list = fmsBusinessTypeService.findByid(businessId);
			if (list != null) {
				for (FmsBusinessType fmsBusinessType : list) {
					map.put(fmsBusinessType.getId(), fmsBusinessType.getName());
				}
			}
		}
		if (page.getList() != null && page.getList().size() > 0) {
			for (FmsEnterpriseContentTypeReportDto dto : page.getList()) {
				dto.setBusinessTypeName(map.get(dto.getBusinessTypeId()));
			}
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

}
