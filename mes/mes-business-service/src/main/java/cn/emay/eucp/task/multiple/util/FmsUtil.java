package cn.emay.eucp.task.multiple.util;

import java.util.Date;
import java.util.Map;

import cn.emay.eucp.common.dto.fms.mt.FmsIdAndMobile;
import cn.emay.eucp.common.dto.fms.mt.FmsSendDto;
import cn.emay.eucp.common.dto.fms.mt.SendFmsData;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateChannelReportDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateRedisDto;
import cn.emay.eucp.common.dto.fms.template.UpdateTemplateAuditStateDto;
import cn.emay.eucp.common.dto.report.FmsReportDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsMessage;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeParam;
import cn.emay.eucp.task.multiple.dto.StrategyCheckDTO;

public class FmsUtil {

	public static UpdateTemplateAuditStateDto toUpdateTemplateDto(FmsTemplateChannelReportDto reportDto) {
		UpdateTemplateAuditStateDto updateDto = new UpdateTemplateAuditStateDto();
		updateDto.setAuditState(reportDto.getState());
		updateDto.setChannelId(reportDto.getChannelId());
		updateDto.setChannelTemplateId(reportDto.getChannelTemplateId());
		updateDto.setOperatorCode(reportDto.getOperatorCode());
		updateDto.setTemplateId(reportDto.getTemplateId());
		// TODO审核时间？
		return updateDto;
	}

	public static FmsMessage buildMessage(SendFmsData fmsData, FmsServiceCode serviceCode, FmsIdAndMobile dto, FmsTemplateRedisDto templateDto) {
		FmsMessage message = new FmsMessage();
		message.setId(dto.getFmsId());
		message.setCustomFmsId(dto.getCustomFmsId());
		message.setAppId(fmsData.getAppId());
		message.setMobile(dto.getMobile() == null ? "" : dto.getMobile().trim());
		message.setSubmitTime(fmsData.getSubmitTime());
		message.setEnterpriseId(serviceCode.getEnterpriseId());
		message.setAppId(serviceCode.getAppId());
		message.setTemplateId(fmsData.getTemplateId());
		message.setBatchNumber(fmsData.getSerialNumber());
		message.setSendType(fmsData.getSendType());
		message.setTemplateType(fmsData.getTemplateType());
		message.setContentTypeId(serviceCode.getBusinessTypeId());
		message.setServiceCodeId(serviceCode.getId());
		message.setState(FmsMessage.STATE_SENDING);
		message.setTitle(fmsData.getTitle());
		message.setSendValue(templateDto.getVariable());
		message.setSendTime(new Date());
		if (message.getTemplateType() == FmsMessage.TEMPLATE_TYPE_PERSON) {
			String content = StringUtil.getContent(templateDto.getContent(), dto.getContent());
			message.setContent(content);
		} else {
			message.setContent(templateDto.getContent());
		}
		return message;

	}

	public static StrategyCheckDTO buildStrategyDto(SendFmsData fmsData, FmsServiceCode serviceCode) {
		StrategyCheckDTO dto = new StrategyCheckDTO();
		dto.setServiceCodeId(serviceCode.getId());
		dto.setAppId(fmsData.getAppId());
		dto.setEnterpriseId(serviceCode.getEnterpriseId());
		return dto;
	}

	public static void setErrorMsg(FmsMessage fms, String fail, String stateDesc) {
		Date time = new Date();
		fms.setChannelReportState(fail);
		fms.setChannelReportDesc(stateDesc);
		fms.setChannelReportTime(time);
		fms.setChannelResponseTime(time);
		fms.setState(FmsMessage.STATE_FAIL);
	}

	public static FmsReportDTO buildReport(FmsMessage fms) {
		FmsReportDTO dto = new FmsReportDTO();
		dto.setAppId(fms.getAppId());
		dto.setFmsId(fms.getCustomFmsId());
		dto.setMobile(fms.getMobile());
		dto.setState(fms.getChannelReportDesc());
		if (null != fms.getChannelReportTime()) {
			dto.setReceiveTime(DateUtil.toString(fms.getChannelReportTime(), "yyyy-MM-dd HH:mm:ss"));
		} else {
			dto.setReceiveTime(DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		}
		return dto;
	}

	public static FmsSendDto buildFmsSendDto(FmsMessage fms, FmsServiceCodeParam fmsServiceCodeParam, String batchNo, Map<Long, String> fessagemap, String serialNumber, int channelIsNeedReport,
			FmsTemplateRedisDto templateDto, Map<String, String> mobileContentmap, String channelTemplateId) {
		FmsSendDto dto = new FmsSendDto();
		dto.setAppId(fms.getAppId());
		dto.setSubmitTime(DateUtil.toString(fms.getSubmitTime(), "yyyy-MM-dd HH:mm:ss"));
		dto.setMobile(fms.getMobile());
		int isNeedReport = fmsServiceCodeParam.getGetReportType() == null ? 1 : fmsServiceCodeParam.getGetReportType().intValue();
		dto.setIsNeedReport(false);
		if (fms.getSendType() == 1) {
			dto.setIsNeedReport(isNeedReport == 2 ? true : false);
		}
		dto.setFmsId(fms.getId());
		dto.setBatchNo(batchNo);
		dto.setMobile(fms.getMobile());
		dto.setContent(templateDto.getContent());
		dto.setCustomFmsId(fms.getCustomFmsId());
		String paramContent = mobileContentmap.get(fms.getMobile());
		dto.setParamContent(paramContent);
		if (channelIsNeedReport == FmsChannel.IS_NOT_NEED_REPORT) {
			dto.setAllContent(templateDto.getContent());
		} else {
			dto.setAllContent(fms.getContent());
		}
		dto.setChannelTemplateId(channelTemplateId);
		dto.setSerialNumber(serialNumber);
		dto.setTemplateId(fms.getTemplateId());
		return dto;
	}

}
