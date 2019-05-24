package cn.emay.eucp.web.manage.controller.global.fms.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.business.FmsBusinesTypeDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateChannelReportDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateRedisDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateWaitingReportDto;
import cn.emay.eucp.common.dto.fms.template.UpdateTemplateAuditStateDto;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateChannelReport;
import cn.emay.eucp.common.util.FmsControllerUtil;
import cn.emay.eucp.common.util.TemplateIdUtil;
import cn.emay.eucp.data.service.fms.FmsBusinessTypeService;
import cn.emay.eucp.data.service.fms.FmsChannelService;
import cn.emay.eucp.data.service.fms.FmsServiceCodeService;
import cn.emay.eucp.data.service.fms.FmsTemplateChannelReportService;
import cn.emay.eucp.data.service.fms.FmsTemplateServiceCodeAssignService;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@PageAuth("FMS_REPORTED_AUDIT")
@RequestMapping("/fms/reported/audit")
@Controller
public class FmsTemplateReportManageController {

	@Resource
	private FmsTemplateServiceCodeAssignService fmsTemplateServiceCodeAssignService;
	@Resource
	private FmsServiceCodeService fmsServiceCodeService;
	@Resource
	private FmsTemplateChannelReportService fmsTemplateChannelReportService;
	@Resource
	private FmsChannelService fmsChannelService;
	@Resource
	private FmsBusinessTypeService fmsBusinessTypeService;
	@Resource
	private RedisClient redisClient;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		// List<FmsBusinessType> busiList = fmsBusinessTypeService.findBusiName(-1L);
		// model.addAttribute("busiList", busiList);
		// List<FmsBusinessType> contentList = fmsBusinessTypeService.findContent(-1L);
		// model.addAttribute("contentList", contentList);
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
		return "fms/reported/audit";
	}

	@RequestMapping("list")
	public void list(HttpServletRequest request, HttpServletResponse response, Model model) {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String appIdString = request.getParameter("serviceCodeId");
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
		Integer auditState = 0;
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		Date startTime = RequestUtils.getDateParameter(request, "startTime", "yyyy-MM-dd HH:mm:ss", null);
		Date endTime = RequestUtils.getDateParameter(request, "endTime", "yyyy-MM-dd HH:mm:ss", null);
		Long businessTypeId = RequestUtils.getLongParameter(request, "businessTypeId", 0L);
		Long contentTypeId = RequestUtils.getLongParameter(request, "contentTypeId", 0L);
		List<String> appIds = null;
		if (!StringUtils.isEmpty(appIdString)) {
			appIds = new ArrayList<String>();
			if (appIdString.contains("EUCP-EMY-")) {
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
						appIds.add(fmsServiceCode.getAppId());
					}else {
						appIds.add("-");
					}
				}
			}
		}
		Page<FmsTemplateDto> page = fmsTemplateServiceCodeAssignService.findTempLetPage(title, content, appIds, businessTypeId, saveType, contentTypeId, messageType, submitType, auditState,
				startTime, endTime, start, limit, -1);
		FmsControllerUtil.fillServiceCodeId(page, redisClient);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

	@RequestMapping("reportoffline")
	public void reportOffline(HttpServletRequest request, HttpServletResponse response, Model model) {
		String templateId = request.getParameter("templateId");
		String reportData = request.getParameter("reportData");
		if (StringUtils.isEmpty(templateId)) {
			ResponseUtils.outputWithJson(response, Result.badResult("模板id不能为空！"));
			return;
		}
		if (StringUtils.isEmpty(reportData)) {
			ResponseUtils.outputWithJson(response, Result.badResult("审核数据不能为空！"));
			return;
		}
		String[] reportOperators = reportData.split("#");
		if (null == reportOperators || reportOperators.length == 0 || reportOperators.length > 3) {
			ResponseUtils.outputWithJson(response, Result.badResult("审核数据错误！"));
			return;
		}
		List<UpdateTemplateAuditStateDto> updateTemplateAuditStateDtos = new ArrayList<UpdateTemplateAuditStateDto>();
		List<FmsTemplateChannelReportDto> fmsTemplateChannelReportDtos = new ArrayList<FmsTemplateChannelReportDto>();
		for (String reportOperator : reportOperators) {
			String[] reportChannelData = reportOperator.split(",");
			if (null != reportChannelData && reportChannelData.length == 4) {
				String operatorCode = reportChannelData[0];
				if (!"CMCC".equals(operatorCode) && !"CUCC".equals(operatorCode) && !"CTCC".equals(operatorCode)) {
					ResponseUtils.outputWithJson(response, Result.badResult("运营商类型错误！"));
					return;
				}
				String channelTemplateId = reportChannelData[1];
				// if (org.apache.commons.lang3.StringUtils.isEmpty(channelTemplateId)) {
				// ResponseUtils.outputWithJson(response, Result.badResult("通道模板id错误！"));
				// return;
				// }
				String channelIdString = reportChannelData[2];
				Long channelId = null;
				try {
					channelId = Long.parseLong(channelIdString);
				} catch (Exception e) {
				}
				if (channelId == null || channelId <= 0L) {
					ResponseUtils.outputWithJson(response, Result.badResult("通道id错误！"));
					return;
				}
				String reportStateString = reportChannelData[3];
				Integer reportState = null;
				try {
					reportState = Integer.parseInt(reportStateString);
				} catch (Exception e) {
					ResponseUtils.outputWithJson(response, Result.badResult("报备状态错误！"));
					return;
				}
				UpdateTemplateAuditStateDto updateTemplateAuditStateDto = new UpdateTemplateAuditStateDto();
				updateTemplateAuditStateDto.setAuditState(reportState);
				if (reportState == FmsTemplate.REPORT_OK) {
					if (StringUtils.isEmpty(reportData)) {
						ResponseUtils.outputWithJson(response, Result.badResult("通道模板id错误！"));
						return;
					}
				}
				updateTemplateAuditStateDto.setChannelTemplateId(channelTemplateId);
				updateTemplateAuditStateDto.setChannelId(channelId);
				updateTemplateAuditStateDto.setOperatorCode(operatorCode);
				updateTemplateAuditStateDtos.add(updateTemplateAuditStateDto);
				FmsTemplateChannelReportDto fmsTemplateChannelReportDto = new FmsTemplateChannelReportDto();
				fmsTemplateChannelReportDto.setAuditTime(new Date());
				fmsTemplateChannelReportDto.setChannelId(channelId);
				fmsTemplateChannelReportDto.setChannelTemplateId(channelTemplateId);
				fmsTemplateChannelReportDto.setOperatorCode(operatorCode);
				fmsTemplateChannelReportDto.setState(reportState);
				fmsTemplateChannelReportDto.setTemplateId(templateId);
				fmsTemplateChannelReportDtos.add(fmsTemplateChannelReportDto);
			} else {
				ResponseUtils.outputWithJson(response, Result.badResult("审核数据错误！"));
				return;
			}
		}
		if (!updateTemplateAuditStateDtos.isEmpty()) {
			fmsTemplateChannelReportService.updateAuditState(updateTemplateAuditStateDtos, true);
		}
		if (!fmsTemplateChannelReportDtos.isEmpty()) {
			redisClient.lpush(RedisConstants.CHANNEL_RESPONSE_TEMPLATE_QUEUE, -1, fmsTemplateChannelReportDtos.toArray());
		}
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

	@RequestMapping("reportonline")
	public void reportOnline(HttpServletRequest request, HttpServletResponse response, Model model) {
		String templateId = request.getParameter("templateId");
		String operatorCode = request.getParameter("operatorCode");
		Long channelId = RequestUtils.getLongParameter(request, "channelId", 0L);
		FmsChannel fmsChannel = fmsChannelService.findById(channelId);
		if (null == fmsChannel) {
			ResponseUtils.outputWithJson(response, Result.badResult("通道不存在错误"));
			return;
		}
		if (StringUtils.isEmpty(templateId)) {
			ResponseUtils.outputWithJson(response, Result.badResult("模板id错误"));
			return;
		}
		if (!"CMCC".equals(operatorCode) && !"CUCC".equals(operatorCode) && !"CTCC".equals(operatorCode)) {
			ResponseUtils.outputWithJson(response, Result.badResult("运营商错误"));
			return;
		}
		FmsTemplateChannelReport fmsTemplateChannelReport = fmsTemplateChannelReportService.findByParams(templateId, channelId, operatorCode);
		if (null != fmsTemplateChannelReport) {
			ResponseUtils.outputWithJson(response, Result.badResult("模板已报备"));
			return;
		}
		String operatorCodeString = fmsChannel.getProviders();
		String[] operators = operatorCodeString.split("#");
		List<FmsTemplateChannelReport> insertList = new ArrayList<FmsTemplateChannelReport>();
		for (String s : operators) {
			FmsTemplateChannelReport entity = new FmsTemplateChannelReport();
			entity.setChannelId(channelId);
			entity.setCreateTime(new Date());
			entity.setState(FmsTemplate.REPORT_SUBMIT);
			entity.setTemplateId(templateId);
			if ("1".equals(s)) {
				entity.setOperatorCode("CMCC");
			} else if ("2".equals(s)) {
				entity.setOperatorCode("CTCC");
			} else if ("3".equals(s)) {
				entity.setOperatorCode("CUCC");
			}
			insertList.add(entity);
		}
		FmsTemplateRedisDto fmsTemplateRedisDto = redisClient.hget(RedisConstants.FMS_TEMPLATE_HASH, templateId, FmsTemplateRedisDto.class);
		if (null == fmsTemplateRedisDto) {
			ResponseUtils.outputWithJson(response, Result.badResult("未找到模板"));
			return;
		}
		FmsTemplateWaitingReportDto fmsTemplateWaitingReportDto = new FmsTemplateWaitingReportDto();
		fmsTemplateWaitingReportDto.setChannelId(channelId);
		fmsTemplateWaitingReportDto.setContent(fmsTemplateRedisDto.getContent());
		fmsTemplateWaitingReportDto.setVariable(fmsTemplateRedisDto.getVariable());
		fmsTemplateWaitingReportDto.setTemplateType(TemplateIdUtil.getTemplateType(templateId));
		fmsTemplateWaitingReportDto.setTemplateId(templateId);
		redisClient.lpush(RedisConstants.FMS_TEMPLATE_WAITING_REPORT_QUEUE, -1, fmsTemplateWaitingReportDto);
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}
}
