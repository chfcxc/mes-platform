package cn.emay.eucp.web.manage.controller.global.fms.report;

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
import cn.emay.eucp.common.dto.fms.statices.FmsServicecodeConsumptionReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.data.service.fms.FmsServiceCodeService;
import cn.emay.eucp.data.service.fms.FmsServicecodeConsumptionDayService;
import cn.emay.eucp.data.service.system.EnterpriseService;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("FMS_REPORT_CONSUMPTION")
@RequestMapping("/fms/report/consumption")
@Controller
public class FmsServicecodeConsumptionController {
	@Resource
	private EnterpriseService enterpriseService;
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
		String nameCn = RequestUtils.getParameter(request, "nameCn");
		Date startTime = RequestUtils.getDateParameter(request, "startTime", "yyyy-MM-dd HH:mm:ss", null);
		Date endTime = RequestUtils.getDateParameter(request, "endTime", "yyyy-MM-dd HH:mm:ss", null);
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String enterpriseIds = "";
		if (nameCn != null) {
			List<Enterprise> list = enterpriseService.getEnterpriseByNameAndClientNumber(nameCn, null);
			if (list.size() == 0) {
				ResponseUtils.outputWithJson(response, Result.badResult("暂无数据"));
				return;
			}
			StringBuffer buffer = new StringBuffer();
			if (list.size() > 0) {
				for (Enterprise enterprise : list) {
					buffer.append(enterprise.getId()).append(",");
				}
			}
			enterpriseIds = buffer.toString().substring(0, buffer.length() - 1);
		}
		Page<FmsServicecodeConsumptionReportDto> page = fmsServicecodeConsumptionDayService.findPage(rate, enterpriseIds, serviceCodeId, startTime, endTime, start, limit);
		Set<Long> set = new HashSet<Long>();
		Set<Long> enterpeiseset = new HashSet<Long>();
		if (page.getList() != null && page.getList().size() > 0) {
			for (FmsServicecodeConsumptionReportDto dto : page.getList()) {
				if (null != dto.getServicecodeId()) {
					set.add(dto.getServicecodeId());
				}
			}
		}
		Map<Long, FmsServiceCode> map2 = fmsServiceCodeService.findbyIds(set);
		List<Enterprise> byIds = enterpriseService.findByIds(enterpeiseset);
		Map<Long, String> map = new HashMap<Long, String>();
		if (null != byIds) {
			for (Enterprise enterprise : byIds) {
				map.put(enterprise.getId(), enterprise.getNameCn());
			}
		}
		if (page.getList() != null && page.getList().size() > 0) {
			for (FmsServicecodeConsumptionReportDto dto : page.getList()) {
				FmsServiceCode serviceCode = map2.get(dto.getServicecodeId());
				if (null != serviceCode) {
					dto.setServiceCode(map2.get(dto.getServicecodeId()).getServiceCode());
				}
				dto.setEnterpriseName(map.get(dto.getEnterpriseId()));
			}
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(page));

	}
}
