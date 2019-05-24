package cn.emay.eucp.inter.framework.core;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.emay.eucp.common.cache.GlobalConstant;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.mt.FmsIdAndMobile;
import cn.emay.eucp.common.dto.fms.mt.SendFmsData;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateRedisDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateReportResult;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateSaveDto;
import cn.emay.eucp.common.dto.report.FmsReportDTO;
import cn.emay.eucp.common.dto.report.FmsReportReceiveRecordDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateServiceCodeAssign;
import cn.emay.eucp.common.support.OnlyIdGenerator;
import cn.emay.eucp.common.util.TemplateIdUtil;
import cn.emay.eucp.inter.framework.dto.CustomFmsIdAndMobile;
import cn.emay.eucp.inter.framework.dto.TemplateReportResult;
import cn.emay.eucp.inter.framework.dto.TemplateResultDto;
import cn.emay.eucp.inter.framework.dto.TemplateSatus;
import cn.emay.json.JsonHelper;
import cn.emay.utils.string.StringUtils;

/**
 * 闪推接口框架服务
 * 
 * @author dinghaijiao
 */
public class FmsInterfaceService {

	/** 根据APPID获取服务号 */
	public FmsServiceCodeDto getServiceCodeByAppId(String appId) {
		return FmsInterfaceFrameWork.getDataStore().getServiceCodeByAppId(appId);
	}

	/**
	 * 报备模板
	 * 
	 * @param appId
	 * @param remoteIp
	 * @param content
	 * @param variable
	 * @param i
	 * @return
	 */
	public TemplateReportResult templateReport(String appId, String content, String variable, int i) {
		String templateId = null;
		int templateType = (variable == null || variable.isEmpty()) ? 0 : 1;
		try {
			templateId = TemplateIdUtil.getTemplateId(content, templateType, i);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		TemplateReportResult result = new TemplateReportResult(templateId, variable);
		// set 模板主数据
		FmsTemplateRedisDto template = new FmsTemplateRedisDto();
		template.setContent(content);
		template.setVariable(variable);
		boolean flag = FmsInterfaceFrameWork.getRedis().hsetnx(RedisConstants.FMS_TEMPLATE_HASH, templateId, template, -1);
		if (!flag) {
			FmsTemplateRedisDto oldTemplate = FmsInterfaceFrameWork.getRedis().hget(RedisConstants.FMS_TEMPLATE_HASH, templateId, FmsTemplateRedisDto.class);
			if (!oldTemplate.getContent().equals(content)) {
				result = templateReport(appId, content, variable, i + 1);
				return result;
			}
		}
		boolean existflag = FmsInterfaceFrameWork.getRedis().hsetnx(RedisConstants.FMS_APPID_TEMPLATEID_HASH, appId + "_" + templateId, 1, -1);
		if (existflag) {
			FmsTemplateSaveDto templateSave = new FmsTemplateSaveDto();
			templateSave.setAppId(appId);
			templateSave.setTemplateId(templateId);
			templateSave.setSubmitTime(new Date());
			templateSave.setSendType(FmsTemplateServiceCodeAssign.SEND_TYPE_INTERFACE);
			templateSave.setVariable(variable);
			templateSave.setTemplateType(templateType);
			templateSave.setContent(content);
			templateSave.setSave(flag);
			FmsInterfaceFrameWork.getRedis().lpush(RedisConstants.FMS_TEMPLATE_SAVE_QUEUE, -1, templateSave);
		}
		return result;
	}

	/**
	 * 获取模板报备状态
	 * 
	 * @param appId
	 * @param templateId
	 * @return
	 */
	public TemplateResultDto getTemplateStatus(String appId, String templateId) {
		TemplateResultDto dto = new TemplateResultDto();
		TemplateSatus status = new TemplateSatus();
		int templateType = TemplateIdUtil.getTemplateType(templateId);
		Map<String, Long> channelMap = FmsInterfaceFrameWork.getDataStore().getChannelId(appId, templateType);
		for (String code : GlobalConstant.OPERATOR_CODES) {
			Long channelId = channelMap.get(code);
			Integer state = FmsTemplate.REPORT_DOIND;
			if (channelId == null) {
				state = FmsTemplate.NOT_SUPPORT;
			} else {
				FmsTemplateReportResult result = FmsInterfaceFrameWork.getDataStore().getTemplateStatus(templateId, channelId, code);
				state = result == null ? FmsTemplate.REPORT_DOIND : result.getState();
			}
			state = parseState(state);
			if ("CMCC".equals(code)) {
				status.setCmcc(state);
			} else if ("CUCC".equals(code)) {
				status.setCucc(state);
			} else {
				status.setCtcc(state);
			}
		}
		dto.setStatus(status);
		dto.setTemplateId(templateId);
		return dto;
	}

	private Integer parseState(int state) {
		if (state <= FmsTemplate.REPORT_DOIND) {
			return TemplateSatus.REPORT_DOIND;
		} else if (state >= FmsTemplate.NOT_SUPPORT) {
			return TemplateSatus.NOT_SUPPORT;
		} else if (state == FmsTemplate.REPORT_OK) {
			return TemplateSatus.REPORT_OK;
		} else if (state == FmsTemplate.REPORT_ERROR) {
			return TemplateSatus.REPORT_ERROR;
		}
		return state;
	}

	/** 发送单条 */
	public FmsIdAndMobile sendSingleFms(String appId, String templateId, String customFmsId, String remoteIp, String content, String mobile) {
		SendFmsData smsdata = new SendFmsData();
		smsdata.setAppId(appId);
		smsdata.setTemplateId(templateId);
		smsdata.setBatchMobileNumber(1);
		smsdata.setBatchNo(OnlyIdGenerator.genOblyBId(FmsInterfaceFrameWork.getInterfaceCode()));
		smsdata.setInterfaceServiceNo(FmsInterfaceFrameWork.getInterfaceCode());
		smsdata.setRemoteIp(remoteIp);
		smsdata.setSendType(SendFmsData.SEND_TYPE_INTERFACE);
		smsdata.setSubmitTime(new Date());
		smsdata.setTemplateType(TemplateIdUtil.getTemplateType(templateId));
		String fmsId = OnlyIdGenerator.genOnlyId(FmsInterfaceFrameWork.getInterfaceCode());
		FmsIdAndMobile dto = new FmsIdAndMobile(fmsId, customFmsId, mobile, StringUtils.isEmpty(content) ? null : content);
		smsdata.setFmsIdAndMobiles(new FmsIdAndMobile[] { dto });
		FmsInterfaceFrameWork.getDataStore().saveSmsDataByRedis(smsdata);
		return dto;
	}

	/** 发送批量 */
	public FmsIdAndMobile[] sendBatchFms(String appId, CustomFmsIdAndMobile[] mobiles, String remoteIp, String templateId) {
		SendFmsData smsdata = new SendFmsData();
		smsdata.setAppId(appId);
		smsdata.setBatchMobileNumber(mobiles.length);
		smsdata.setBatchNo(OnlyIdGenerator.genOblyBId(FmsInterfaceFrameWork.getInterfaceCode()));
		smsdata.setInterfaceServiceNo(FmsInterfaceFrameWork.getInterfaceCode());
		smsdata.setRemoteIp(remoteIp);
		smsdata.setSendType(SendFmsData.SEND_TYPE_INTERFACE);
		smsdata.setSubmitTime(new Date());
		smsdata.setTemplateId(templateId);
		smsdata.setTemplateType(TemplateIdUtil.getTemplateType(templateId));
		FmsIdAndMobile[] fmses = new FmsIdAndMobile[mobiles.length];
		int i = 0;
		for (CustomFmsIdAndMobile mobile : mobiles) {
			String fmsId = OnlyIdGenerator.genOnlyId(FmsInterfaceFrameWork.getInterfaceCode());
			fmses[i] = new FmsIdAndMobile(fmsId, mobile.getCustomFmsId(), mobile.getMobile(), StringUtils.isEmpty(mobile.getContent()) ? null : mobile.getContent());
			i++;
		}
		smsdata.setFmsIdAndMobiles(fmses);
		FmsInterfaceFrameWork.getDataStore().saveSmsDataByRedis(smsdata);
		return fmses;
	}

	/** 获取Report */
	public List<FmsReportDTO> getReport(String appId, String remoteIp, int number) {
		List<FmsReportDTO> receives = new ArrayList<FmsReportDTO>();
		for (int i = 0; i < number; i++) {
			FmsReportDTO dto = FmsInterfaceFrameWork.getDataStore().getReport(appId);
			if (null == dto) {
				break;
			}
			receives.add(dto);
		}
		return receives;
	}

	/**
	 * 状态报告回滚
	 * 
	 * @param reports
	 */
	public void rollBackReport(List<FmsReportDTO> reports) {
		FmsInterfaceFrameWork.getDataStore().rollBackReport(reports);
	}

	/**
	 * 保存状态报告获取记录
	 * 
	 * @param reports
	 */
	public void saveReportRecord(String appId, String remoteIp, String reportJson) {
		FmsReportReceiveRecordDTO dto = new FmsReportReceiveRecordDTO();
		dto.setAppId(appId);
		dto.setRemoteIp(remoteIp);
		dto.setCreateTime(new Date());
		dto.setReportJson(JsonHelper.toJsonString(reportJson));
		FmsInterfaceFrameWork.getDataStore().saveReportRecord(dto);
	}

	public String getServiceCodeByAppIdBalance(String cdkey) {
		return FmsInterfaceFrameWork.getDataStore().getServiceCodeByAppIdBalance(cdkey);
	}

}
