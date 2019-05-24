package cn.emay.channel.framework.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

import cn.emay.channel.framework.core.ChannelFrameWork;
import cn.emay.channel.framework.dto.FmsHasResponse;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.constant.EucpFmsMessageState;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.report.FmsReportDTO;
import cn.emay.eucp.common.dto.report.UpdateFmsDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsMessage;
import cn.emay.jpp.utils.JppUtils;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;
import cn.emay.util.DateUtil;
import cn.emay.util.PropertiesUtil;

public class ClearReportTimeOutTask implements PeriodTask {

	private static final Logger logger = Logger.getLogger(ContrastReportTask.class);
	private RedisClient redis = JppUtils.getRedis("Redis");
	private static long period = 1000l;
	private int timeoutDay = PropertiesUtil.getIntProperty("timeout.report.day", "channel.properties", 2);

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		try {
			FmsChannel mmsChannel = ChannelFrameWork.getInstance().getFmsChannel();
			if (null == mmsChannel) {
				return TaskResult.doBusinessSuccessResult();
			}
			Map<String, List<FmsReportDTO>> customReportMap = new HashMap<String, List<FmsReportDTO>>();
			List<UpdateFmsDTO> updateList = new ArrayList<UpdateFmsDTO>();
			String nowTime = DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss");
			long endTime = System.currentTimeMillis() - (timeoutDay * 24 * 60 * 60 * 1000l);
			Set<String> timeOutSet = redis.zrangeByScore(RedisConstants.FMS_WAIT_REPORT_ZSET + mmsChannel.getId(), 0, endTime, 0, 1000);
			if (null != timeOutSet && timeOutSet.size() > 0) {
				List<String> RespMtKeys = new ArrayList<String>();
				RespMtKeys.addAll(timeOutSet);
				String[] timeOutMt = timeOutSet.toArray(new String[timeOutSet.size()]);
				List<String> listRespMt = redis.hmget(RedisConstants.FMS_WAIT_REPORT_HASH + mmsChannel.getId(), timeOutMt);
				if (null != listRespMt && listRespMt.size() > 0) {
					for (int i = 0; i < listRespMt.size(); i++) {
						String JsonRespMt = listRespMt.get(i);
						if (JsonRespMt != null && !JsonRespMt.equals("")) {
							logger.info("【ContrastReportServiceImpl】-->待比对状态报告信息：[" + JsonRespMt + "]");
							FmsHasResponse hasResponseDTO = JsonHelper.fromJson(new TypeToken<FmsHasResponse>() {
							}, JsonRespMt);
							String key = hasResponseDTO.getMobile() + "#" + hasResponseDTO.getOperatorFmsId();
							RespMtKeys.add(key);
							// 生成状态报告
							if (hasResponseDTO.isNeedReport()) {
								FmsReportDTO reportDTO = buildTimeoutReport(hasResponseDTO, nowTime);
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
							UpdateFmsDTO updateImsDTO = buildTimeoutUpdateData(hasResponseDTO, nowTime);
							updateList.add(updateImsDTO);
						}
					}
					if (RespMtKeys.size() > 0) {
						redis.hdel(RedisConstants.FMS_WAIT_REPORT_HASH + mmsChannel.getId(), RespMtKeys.toArray(new String[RespMtKeys.size()]));
						redis.zrem(RedisConstants.FMS_WAIT_REPORT_ZSET + mmsChannel.getId(), RespMtKeys.toArray(new String[RespMtKeys.size()]));
					}
					if (customReportMap.size() > 0) {
						for (Entry<String, List<FmsReportDTO>> entry : customReportMap.entrySet()) {
							redis.lpush(RedisConstants.FMS_REPORT_QUEUE + entry.getKey(), -1, entry.getValue().toArray());
						}
						customReportMap.clear();
					}
					if (updateList.size() > 0) {
						redis.lpush(RedisConstants.FMS_UPDATE_DB_QUEUE, -1, updateList.toArray());
						updateList.clear();
					}
				}
			}
			return TaskResult.doBusinessSuccessResult();
		} catch (Exception e) {
			logger.error("超时检测任务异常", e);
			return TaskResult.doBusinessFailResult();
		}
	}

	private FmsReportDTO buildTimeoutReport(FmsHasResponse hasResponseDTO, String time) {
		FmsReportDTO report = new FmsReportDTO();
		report.setAppId(hasResponseDTO.getAppId());
		report.setCustomFmsId(hasResponseDTO.getCustomFmsId());
		report.setDesc("超时");
		report.setFmsId(hasResponseDTO.getFmsId());
		report.setMobile(hasResponseDTO.getMobile());
		report.setReceiveTime(hasResponseDTO.getChannelServiceReceiveResponseTime());
		report.setSubmitTime(hasResponseDTO.getSubmitTime());
		report.setState("TIMEOUT");
		return report;
	}

	private UpdateFmsDTO buildTimeoutUpdateData(FmsHasResponse hasResponseDTO, String time) {
		UpdateFmsDTO dto = new UpdateFmsDTO(hasResponseDTO.getFmsId(), null, time, FmsMessage.STATE_TIMEOUT, EucpFmsMessageState.TIMEOUT.getCode(), EucpFmsMessageState.TIMEOUT.getCode(),
				hasResponseDTO.getOperatorFmsId(), hasResponseDTO.getBatchNo());
		dto.setChannelResponseTime(hasResponseDTO.getChannelResponseTime());
		return dto;
	}

	@Override
	public long period() {
		return period;
	}

}
