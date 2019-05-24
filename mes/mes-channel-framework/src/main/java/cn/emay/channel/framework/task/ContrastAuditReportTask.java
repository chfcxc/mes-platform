package cn.emay.channel.framework.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

import cn.emay.channel.framework.cache.FQueueCache;
import cn.emay.channel.framework.core.ChannelFrameWork;
import cn.emay.channel.framework.dto.Constants;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateChannelReportDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateChannelResponseDto;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.jpp.utils.JppUtils;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

public class ContrastAuditReportTask implements PeriodTask {
	private static final Logger logger = Logger.getLogger(ContrastAuditReportTask.class);
	private RedisClient redis = JppUtils.getRedis("Redis");
	private static long period = 50L;

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		try {
			Map<String, FmsTemplateChannelResponseDto> reportMap = new HashMap<String, FmsTemplateChannelResponseDto>();
			List<FmsTemplateChannelReportDto> updateList = new ArrayList<>();
			FmsChannel fmsChannel = ChannelFrameWork.getInstance().getchannel().getFmsChannel();
			if (null == fmsChannel || fmsChannel.getState() != FmsChannel.CHANNEL_STATE_ON) {
				return TaskResult.doBusinessSuccessResult();
			}
			List<String> respMtKeys = new ArrayList<String>();
			for (int i = 0; i < 200; i++) {
				String jsonReport = FQueueCache.getValue(Constants.AUDITREPORTQUEUE);
				if (jsonReport != null && !jsonReport.equals("")) {
					FmsTemplateChannelResponseDto report = JsonHelper.fromJson(FmsTemplateChannelResponseDto.class, jsonReport);
					if (report.getCmccTemplateId() != null) {
						String key = "CMCC" + "#" + report.getCmccTemplateId();
						reportMap.put(key, report);
					}
					if (report.getCuccTemplateId() != null) {
						String key = "CUCC" + "#" + report.getCuccTemplateId();
						reportMap.put(key, report);
					}
					if (report.getCtccTemplateId() != null) {
						String key = "CTCC" + "#" + report.getCtccTemplateId();
						reportMap.put(key, report);
					}
					logger.info("【ContrastAuditReportTask】-->对比报备状态报告信息：[" + JsonHelper.toJsonString(report) + "]");
				} else {
					break;
				}
			}
			if (reportMap.size() > 0 && fmsChannel.getReportType() != 1 && fmsChannel.getReportType() != 3) {
				Set<String> keys = reportMap.keySet();
				List<String> respMtList = redis.hmget(RedisConstants.FMS_WAIT_AUDIT_REPORT_HASH + fmsChannel.getId(), keys.toArray(new String[keys.size()]));
				if (null != respMtList && respMtList.size() > 0) {
					logger.debug("【ContrastAuditReportTask】-->取出状态报告[" + reportMap.size() + "]条;");
					for (int i = 0; i < respMtList.size(); i++) {
						String jsonRespMt = respMtList.get(i);
						if (jsonRespMt != null && !jsonRespMt.equals("")) {
							logger.info("【ContrastAuditReportTask】-->待比对状态报告信息：[" + jsonRespMt + "]");
							FmsTemplateChannelReportDto hasResponseDTO = JsonHelper.fromJson(new TypeToken<FmsTemplateChannelReportDto>() {
							}, jsonRespMt);
							if (hasResponseDTO.getChannelTemplateId() != null) {
								String key = hasResponseDTO.getOperatorCode() + "#" + hasResponseDTO.getChannelTemplateId();
								respMtKeys.add(key);
								FmsTemplateChannelResponseDto channelReport = reportMap.remove(key);
								// 生成更新数据
								List<FmsTemplateChannelReportDto> updateFmsDTO = buildUpdateFms(channelReport, fmsChannel, hasResponseDTO);
								for (FmsTemplateChannelReportDto dto : updateFmsDTO) {
									updateList.add(dto);
								}
							}

						}
					}
					if (respMtKeys.size() > 0) {
						redis.hdel(RedisConstants.FMS_WAIT_AUDIT_REPORT_HASH + fmsChannel.getId(), respMtKeys.toArray(new String[respMtKeys.size()]));
						redis.zrem(RedisConstants.FMS_WAIT_AUDIT_REPORT_ZSET + fmsChannel.getId(), respMtKeys.toArray(new String[respMtKeys.size()]));
					}
					// 存储更新状态数据
					if (updateList.size() > 0) {
						redis.lpush(RedisConstants.CHANNEL_RESPONSE_TEMPLATE_QUEUE, -1, updateList.toArray());
					}
				}
				period = 50L;
			} else {
				period = 500L;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaskResult.doBusinessSuccessResult();
	}

	@Override
	public long period() {
		return period;
	}

	private List<FmsTemplateChannelReportDto> buildUpdateFms(FmsTemplateChannelResponseDto auditDTO, FmsChannel fmsChannel, FmsTemplateChannelReportDto hasResponseDTO) {
		List<FmsTemplateChannelReportDto> list = new ArrayList<>();
		String operator = hasResponseDTO.getOperatorCode();
		if ("CMCC".equals(operator)) {
			FmsTemplateChannelReportDto fmsTemplateChannelReportDto2 = new FmsTemplateChannelReportDto();
			fmsTemplateChannelReportDto2.setChannelId(fmsChannel.getId());
			fmsTemplateChannelReportDto2.setChannelTemplateId(auditDTO.getCmccTemplateId());
			fmsTemplateChannelReportDto2.setOperatorCode("CMCC");
			fmsTemplateChannelReportDto2.setState(auditDTO.getCmccState());
			fmsTemplateChannelReportDto2.setAuditTime(new Date());
			fmsTemplateChannelReportDto2.setTemplateId(hasResponseDTO.getTemplateId());
			list.add(fmsTemplateChannelReportDto2);
		} else if ("CUCC".equals(operator)) {
			FmsTemplateChannelReportDto fmsTemplateChannelReportDto3 = new FmsTemplateChannelReportDto();
			fmsTemplateChannelReportDto3.setChannelId(fmsChannel.getId());
			fmsTemplateChannelReportDto3.setChannelTemplateId(auditDTO.getCuccTemplateId());
			fmsTemplateChannelReportDto3.setOperatorCode("CUCC");
			fmsTemplateChannelReportDto3.setState(auditDTO.getCuccState());
			fmsTemplateChannelReportDto3.setTemplateId(hasResponseDTO.getTemplateId());
			fmsTemplateChannelReportDto3.setAuditTime(new Date());
			list.add(fmsTemplateChannelReportDto3);
		} else if ("CTCC".equals(operator)) {
			FmsTemplateChannelReportDto fmsTemplateChannelReportDto1 = new FmsTemplateChannelReportDto();
			fmsTemplateChannelReportDto1.setChannelId(fmsChannel.getId());
			fmsTemplateChannelReportDto1.setChannelTemplateId(auditDTO.getCtccTemplateId());
			fmsTemplateChannelReportDto1.setOperatorCode("CTCC");
			fmsTemplateChannelReportDto1.setState(auditDTO.getCtccState());
			fmsTemplateChannelReportDto1.setTemplateId(hasResponseDTO.getTemplateId());
			fmsTemplateChannelReportDto1.setAuditTime(new Date());
			list.add(fmsTemplateChannelReportDto1);
		}
		return list;
	}
}
