package cn.emay.eucp.web.client.controller.fms.report;

import java.util.Date;
import java.util.HashSet;
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
import cn.emay.eucp.common.dto.fms.statices.FmsServicecodeConsumptionReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.data.service.fms.FmsServiceCodeService;
import cn.emay.eucp.data.service.fms.FmsServicecodeConsumptionDayService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("FMS_CLINT_REPORT_CONSUMPTION")
@RequestMapping("/fms/client/reportconsumption")
@Controller
public class FmsServicecodeConsumptionController {
	@Resource
	private FmsServicecodeConsumptionDayService fmsServicecodeConsumptionDayService;
	@Resource
	private FmsServiceCodeService fmsServiceCodeService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "fms/report/consumption";
	}

	@RequestMapping("ajax/list")
	public void findlist(HttpServletRequest request, HttpServletResponse response) {
		String rate = RequestUtils.getParameter(request, "rate");
		Long serviceCodeId = RequestUtils.getLongParameter(request, "serviceCodeId", 0L);
		Date startTime = RequestUtils.getDateParameter(request, "startTime", "yyyy-MM-dd HH:mm:ss", null);
		Date endTime = RequestUtils.getDateParameter(request, "endTime", "yyyy-MM-dd HH:mm:ss", null);
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String enterpriseIds = "";
		Enterprise enterprise = WebUtils.getCurrentEnterprise(request, response);
		enterpriseIds = String.valueOf(enterprise.getId());
		Page<FmsServicecodeConsumptionReportDto> page = fmsServicecodeConsumptionDayService.findPage(rate, enterpriseIds, serviceCodeId, startTime, endTime, start, limit);
		Set<Long> set = new HashSet<Long>();
		if (page.getList() != null && page.getList().size() > 0) {
			for (FmsServicecodeConsumptionReportDto dto : page.getList()) {
				if (null != dto.getServicecodeId()) {
					set.add(dto.getServicecodeId());
				}
			}
		}
		Map<Long, FmsServiceCode> map2 = fmsServiceCodeService.findbyIds(set);
		if (page.getList() != null && page.getList().size() > 0) {
			for (FmsServicecodeConsumptionReportDto dto : page.getList()) {
				FmsServiceCode serviceCode = map2.get(dto.getServicecodeId());
				if (null != serviceCode) {
					dto.setServiceCode(map2.get(dto.getServicecodeId()).getServiceCode());
				}
			}
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(page));

	}
}
