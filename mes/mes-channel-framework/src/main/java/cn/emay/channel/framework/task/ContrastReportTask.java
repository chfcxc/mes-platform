package cn.emay.channel.framework.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

import cn.emay.channel.framework.cache.FQueueCache;
import cn.emay.channel.framework.core.ChannelFrameWork;
import cn.emay.channel.framework.dto.Constants;
import cn.emay.channel.framework.dto.FmsChannelReport;
import cn.emay.channel.framework.dto.FmsHasResponse;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.report.FmsReportDTO;
import cn.emay.eucp.common.dto.report.UpdateFmsDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsMessage;
import cn.emay.jpp.utils.JppUtils;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

public class ContrastReportTask implements PeriodTask {
	private static final Logger logger = Logger.getLogger(ContrastReportTask.class);
	private RedisClient redis = JppUtils.getRedis("Redis");
	private static long period = 50L;

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		try {
			Map<String, FmsChannelReport> reportMap = new HashMap<String, FmsChannelReport>();
			Map<String, List<FmsReportDTO>> customReportMap = new HashMap<String, List<FmsReportDTO>>();
			List<UpdateFmsDTO> updateList = new ArrayList<UpdateFmsDTO>();
			FmsChannel fmsChannel = ChannelFrameWork.getInstance().getchannel().getFmsChannel();
			if (null == fmsChannel || fmsChannel.getState() != FmsChannel.CHANNEL_STATE_ON) {
				return TaskResult.doBusinessSuccessResult();
			}
			List<String> respMtKeys = new ArrayList<String>();
			for (int i = 0; i < 200; i++) {
				String jsonReport = FQueueCache.getValue(Constants.REPORTQUEUE);
				if (jsonReport != null && !jsonReport.equals("")) {
					FmsChannelReport report = JsonHelper.fromJson(new TypeToken<FmsChannelReport>() {
					}, jsonReport);
					String key = report.getMobile() + "#" + report.getOperatorMessageId();
					reportMap.put(key, report);
					logger.info("【ContrastReportTask】-->对比状态报告信息：[" + JsonHelper.toJsonString(report.getOperatorMessageId()) + "]");
				} else {
					break;
				}
			}
			if (reportMap.size() > 0) {
				Set<String> keys = reportMap.keySet();
				List<String> respMtList = redis.hmget(RedisConstants.FMS_WAIT_REPORT_HASH + fmsChannel.getId(), keys.toArray(new String[keys.size()]));
				if (null != respMtList && respMtList.size() > 0) {
					logger.debug("【ContrastReportTask】-->取出状态报告[" + reportMap.size() + "]条;");
					for (int i = 0; i < respMtList.size(); i++) {
						String jsonRespMt = respMtList.get(i);
						if (jsonRespMt != null && !jsonRespMt.equals("")) {
							logger.info("【ContrastReportTask】-->待比对状态报告信息：[" + jsonRespMt + "]");
							FmsHasResponse hasResponseDTO = JsonHelper.fromJson(new TypeToken<FmsHasResponse>() {
							}, jsonRespMt);

							String key = hasResponseDTO.getMobile() + "#" + hasResponseDTO.getOperatorFmsId();
							respMtKeys.add(key);
							FmsChannelReport channelReport = reportMap.remove(key);
							// 生成状态报告
							if (hasResponseDTO.isNeedReport()) {
								FmsReportDTO reportDTO = buileReport(hasResponseDTO, channelReport);
								if (customReportMap.containsKey(hasResponseDTO.getAppId())) {
									List<FmsReportDTO> reports = customReportMap.get(hasResponseDTO.getAppId());
									reports.add(reportDTO);
								} else {
									List<FmsReportDTO> reports = new ArrayList<FmsReportDTO>();
									reports.add(reportDTO);
									customReportMap.put(hasResponseDTO.getAppId(), reports);
								}
							}
							// 生成更新数据
							UpdateFmsDTO updateFmsDTO = buildUpdateFms(hasResponseDTO, channelReport);
							updateList.add(updateFmsDTO);
						}
					}
					if (respMtKeys.size() > 0) {
						redis.hdel(RedisConstants.FMS_WAIT_REPORT_HASH + fmsChannel.getId(), respMtKeys.toArray(new String[respMtKeys.size()]));
						redis.zrem(RedisConstants.FMS_WAIT_REPORT_ZSET + fmsChannel.getId(), respMtKeys.toArray(new String[respMtKeys.size()]));
					}
					// 存储生成状态报告到状态报告队列
					if (customReportMap.size() > 0) {
						for (Entry<String, List<FmsReportDTO>> entry : customReportMap.entrySet()) {
							redis.lpush(RedisConstants.FMS_REPORT_QUEUE + entry.getKey(), -1, entry.getValue().toArray());
						}
					}
					// 存储更新状态数据
					if (updateList.size() > 0) {
						redis.lpush(RedisConstants.FMS_UPDATE_DB_QUEUE, -1, updateList.toArray());
					}
				}
				/** 状态报告未对比上的数据重新回滚至文件队列 */
				if (reportMap.size() > 0) {
					for (Entry<String, FmsChannelReport> entry : reportMap.entrySet()) {
						FmsChannelReport resultReport = entry.getValue();
						if (resultReport.getCompareTime() == 0L) {
							resultReport.setCompareTime(System.currentTimeMillis());
						}
						if (System.currentTimeMillis() - resultReport.getCompareTime() < Constants.REPEAT_COMPARISON_REPORT.longValue() * 1000L) {
							if (resultReport.getCompareTime() == 0L) {
								resultReport.setCompareTime(System.currentTimeMillis());
							}
							FQueueCache.putValue(Constants.REPORTQUEUE, JsonHelper.toJsonString(resultReport));
						}
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

	private FmsReportDTO buileReport(FmsHasResponse hasResponseDTO, FmsChannelReport channelReport) {
		FmsReportDTO report = new FmsReportDTO();
		report.setAppId(hasResponseDTO.getAppId());
		report.setCustomFmsId(hasResponseDTO.getCustomFmsId());
		report.setDesc(channelReport.getStateDesc());
		report.setFmsId(hasResponseDTO.getFmsId());
		report.setMobile(hasResponseDTO.getMobile());
		report.setReceiveTime(hasResponseDTO.getChannelServiceReceiveResponseTime());
		report.setSubmitTime(hasResponseDTO.getSubmitTime());
		report.setState(channelReport.getState());
		return report;
	}

	private UpdateFmsDTO buildUpdateFms(FmsHasResponse hasResponseDTO, FmsChannelReport channelReport) {
		int state = FmsMessage.STATE_DELIVRD;
		if (!"DELIVRD".equalsIgnoreCase(channelReport.getState())) {
			state = FmsMessage.STATE_FAIL;
		}
		UpdateFmsDTO dto = new UpdateFmsDTO(hasResponseDTO.getFmsId(), hasResponseDTO.getChannelResponseTime(), channelReport.getChannelReportTime(), state, channelReport.getState(),
				channelReport.getStateDesc(), hasResponseDTO.getOperatorFmsId(), hasResponseDTO.getBatchNo());
		return dto;
	}
}
