package cn.emay.eucp.task.multiple.fms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.report.UpdateFmsDTO;
import cn.emay.eucp.data.service.fms.FmsMessageService;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;
import cn.emay.util.PropertiesUtil;

public class UpdateFmsInDbTask implements PeriodTask {

	private static final Logger logger = Logger.getLogger(UpdateFmsInDbTask.class);
	private static FmsMessageService fmsMessageService = BeanFactoryUtils.getBean(FmsMessageService.class);
	private static RedisClient redis = BeanFactoryUtils.getBean(RedisClient.class);
	private static int saveNum = PropertiesUtil.getIntProperty("once.update.db.num", "eucp.service.properties", 1000);

	@Override
	public long period() {
		long size = redis.llen(RedisConstants.FMS_UPDATE_DB_QUEUE);
		if (size > 0) {
			return 50;
		} else {
			return 500L;
		}
	}

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		List<UpdateFmsDTO> updateReportList = new ArrayList<UpdateFmsDTO>();
		List<UpdateFmsDTO> updateResponseList = new ArrayList<UpdateFmsDTO>();
		List<UpdateFmsDTO> rollBackList = new ArrayList<UpdateFmsDTO>();
		Set<String> imsIdSet = new HashSet<String>();
		Map<String, UpdateFmsDTO> responMap = new HashMap<String, UpdateFmsDTO>();
		try {
			for (int i = 0; i < saveNum; i++) {
				UpdateFmsDTO dto = redis.rpop(RedisConstants.FMS_UPDATE_DB_QUEUE, UpdateFmsDTO.class);
				if (null == dto) {
					break;
				}
				if (redis.hexists(RedisConstants.FMS_MESSAGE_HASH, dto.getBatchNo())) {// 短信批次Hash 没有说明已经入库了 可以进行更新
					rollBackList.add(dto);
				} else {
					if (dto.getReportTime() != null) {
						updateReportList.add(dto);
						imsIdSet.add(dto.getFmsId());
					} else {
						responMap.put(dto.getFmsId(), dto);
					}
				}
			}
			if (imsIdSet.size() > 0) {
				for (String s : imsIdSet) {
					responMap.remove(s);
				}
			}
			if (responMap.size() > 0) {
				updateResponseList.addAll(responMap.values());
			}
			if (updateResponseList.size() > 0) {
				fmsMessageService.updateFmsResponse(updateResponseList);
				updateResponseList.clear();
			}
			if (updateReportList.size() > 0) {
				fmsMessageService.updateFmsReport(updateReportList);
				updateReportList.clear();
			}

		} catch (Exception e) {
			logger.info("更新入库失败", e);
		} finally {
			if (updateResponseList.size() > 0) {
				rollBackList.addAll(updateResponseList);
			}
			if (updateReportList.size() > 0) {
				rollBackList.addAll(updateReportList);
			}
			if (rollBackList.size() > 0) {
				// logger.info("更新入库失败，数据回滚至队列");
				redis.lpush(RedisConstants.FMS_UPDATE_DB_QUEUE, -1, rollBackList.toArray());
				rollBackList.clear();
			}
		}
		return TaskResult.doBusinessSuccessResult();
	}
}
