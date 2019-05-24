package cn.emay.channel.framework.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.emay.channel.framework.core.ChannelFrameWork;
import cn.emay.common.Result;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateChannelReportDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateChannelResponseDto;
import cn.emay.eucp.common.dto.fms.template.ReportFmsTemplateDto;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.jpp.utils.JppUtils;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

/** 发送审核报备的任务，
 * 
 * @author dejun */
public class SendAuditTask implements PeriodTask {

	private RedisClient redis = JppUtils.getRedis("Redis");

	private long period = 1000L;// 30s

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		FmsChannel fmsChannel = ChannelFrameWork.getInstance().getFmsChannel();
		ReportFmsTemplateDto reportFmsTemplateDto = redis.rpop(RedisConstants.CHANNEL_REPORT_TEMPLATE_QUEUE + fmsChannel.getId(), ReportFmsTemplateDto.class);
		if (reportFmsTemplateDto != null) {
			Result response = ChannelFrameWork.getInstance().getchannel().sendAudit(reportFmsTemplateDto);
			Map<String, Double> zsetMap = new HashMap<String, Double>();
			Map<String, Object> hasResponseMap = new HashMap<String, Object>();
			List<FmsTemplateChannelReportDto> list = new ArrayList<>();
			FmsTemplateChannelResponseDto auditDTO = (FmsTemplateChannelResponseDto) response.getResult();
			FmsTemplateChannelReportDto fmsTemplateChannelReportDto1 = new FmsTemplateChannelReportDto();
			fmsTemplateChannelReportDto1.setChannelId(auditDTO.getChannelId());
			fmsTemplateChannelReportDto1.setChannelTemplateId(auditDTO.getCtccTemplateId());
			fmsTemplateChannelReportDto1.setOperatorCode("CTCC");
			fmsTemplateChannelReportDto1.setState(auditDTO.getCtccState());
			fmsTemplateChannelReportDto1.setTemplateId(auditDTO.getTemplateId());
			list.add(fmsTemplateChannelReportDto1);
			FmsTemplateChannelReportDto fmsTemplateChannelReportDto2 = new FmsTemplateChannelReportDto();
			fmsTemplateChannelReportDto2.setChannelId(auditDTO.getChannelId());
			fmsTemplateChannelReportDto2.setChannelTemplateId(auditDTO.getCmccTemplateId());
			fmsTemplateChannelReportDto2.setOperatorCode("CMCC");
			fmsTemplateChannelReportDto2.setState(auditDTO.getCmccState());
			fmsTemplateChannelReportDto2.setTemplateId(auditDTO.getTemplateId());
			list.add(fmsTemplateChannelReportDto2);
			FmsTemplateChannelReportDto fmsTemplateChannelReportDto3 = new FmsTemplateChannelReportDto();
			fmsTemplateChannelReportDto3.setChannelId(auditDTO.getChannelId());
			fmsTemplateChannelReportDto3.setChannelTemplateId(auditDTO.getCuccTemplateId());
			fmsTemplateChannelReportDto3.setOperatorCode("CUCC");
			fmsTemplateChannelReportDto3.setState(auditDTO.getCuccState());
			fmsTemplateChannelReportDto3.setTemplateId(auditDTO.getTemplateId());
			list.add(fmsTemplateChannelReportDto3);
			if (response.getSuccess()) {
				String key = "CMCC" + "#" + auditDTO.getCmccTemplateId();
				hasResponseMap.put(key, JsonHelper.toJsonString(fmsTemplateChannelReportDto2));
				zsetMap.put(key, Double.valueOf(System.currentTimeMillis()));
				String key1 = "CTCC" + "#" + auditDTO.getCtccTemplateId();
				hasResponseMap.put(key1, JsonHelper.toJsonString(fmsTemplateChannelReportDto1));
				zsetMap.put(key1, Double.valueOf(System.currentTimeMillis()));
				String key2 = "CUCC" + "#" + auditDTO.getCuccTemplateId();
				hasResponseMap.put(key2, JsonHelper.toJsonString(fmsTemplateChannelReportDto3));
				zsetMap.put(key2, Double.valueOf(System.currentTimeMillis()));
			}
			redis.lpush(RedisConstants.CHANNEL_RESPONSE_TEMPLATE_QUEUE, -1, list.toArray());
			if (zsetMap.size() > 0 && fmsChannel.getReportType() != 1) {
				redis.zadd(RedisConstants.FMS_WAIT_AUDIT_REPORT_ZSET + fmsChannel.getId(), zsetMap);
				zsetMap.clear();
			}
			if (hasResponseMap.size() > 0 && fmsChannel.getReportType() != 1) {// 线下报备不产生超时先不考虑
				redis.hmset(RedisConstants.FMS_WAIT_AUDIT_REPORT_HASH + fmsChannel.getId(), hasResponseMap, -1);
				hasResponseMap.clear();
			}
		} else {
			period = 2000L;
		}
		return TaskResult.doBusinessSuccessResult();
	}

	public int needConcurrent(int alive, int min, int max) {
		FmsChannel fmsChannel = ChannelFrameWork.getInstance().getFmsChannel();
		int length = redis.llen(RedisConstants.CHANNEL_REPORT_TEMPLATE_QUEUE + fmsChannel.getId()).intValue();
		if (length > max) {
			return max;
		} else {
			return length;
		}
	}

	@Override
	public long period() {
		return period;
	}

}
