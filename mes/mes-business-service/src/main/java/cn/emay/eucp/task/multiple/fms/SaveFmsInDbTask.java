package cn.emay.eucp.task.multiple.fms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.moudle.db.fms.FmsMessage;
import cn.emay.eucp.data.service.fms.FmsMessageService;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.ConcurrentPeriodTask;
import cn.emay.util.PropertiesUtil;

public class SaveFmsInDbTask implements ConcurrentPeriodTask {

	private static final Logger logger = Logger.getLogger(SaveFmsInDbTask.class);
	private static FmsMessageService fmsMessageService = BeanFactoryUtils.getBean(FmsMessageService.class);
	private static RedisClient redis = BeanFactoryUtils.getBean(RedisClient.class);
	private static int saveNum = PropertiesUtil.getIntProperty("once.save.db.num", "eucp.service.properties", 1000);

	@Override
	public long period() {
		long size = redis.llen(RedisConstants.FMS_SAVE_DB_QUEUE);
		if (size > 0) {
			return 50;
		} else {
			return 500L;
		}
	}

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		// logger.info("saveImsInDbTask短信入库 执行");
		List<FmsMessage> list = new ArrayList<FmsMessage>();
		Set<String> rollbackSet = new HashSet<String>();
		try {
			for (;;) {
				String batchNo = redis.rpop(RedisConstants.FMS_SAVE_DB_QUEUE);
				if (StringUtils.isEmpty(batchNo)) {
					break;
				}
				rollbackSet.add(batchNo);
				FmsMessage[] fmss = redis.hget(RedisConstants.FMS_MESSAGE_HASH, batchNo, FmsMessage[].class);
				if (fmss != null && fmss.length > 0) {
					for (FmsMessage f : fmss) {
						list.add(f);
					}
				}
				if (list.size() >= saveNum) {
					break;
				}
			}
			if (list.size() > 0) {
				fmsMessageService.saveBatchByBeans(list);
				list.clear();
				redis.hdel(RedisConstants.FMS_MESSAGE_HASH, rollbackSet.toArray(new String[rollbackSet.size()]));
				rollbackSet.clear();
			}
		} catch (Exception e) {
			logger.info("数据入库失败，数据回滚", e);
			if (rollbackSet.size() > 0) {
				if (list.size() > 0) {
					redis.lpush(RedisConstants.FMS_SAVE_DB_QUEUE_FAIL, -1, rollbackSet.toArray());
				} else {
					redis.hdel(RedisConstants.FMS_MESSAGE_HASH, rollbackSet.toArray(new String[rollbackSet.size()]));
				}
				rollbackSet.clear();
			}
		}
		return TaskResult.doBusinessSuccessResult();
	}

	@Override
	public int needConcurrent(int alive, int min, int max) {
		long size = redis.llen(RedisConstants.FMS_SAVE_DB_QUEUE);
		Long num = size / 1000;
		if (num == 0) {
			return 1;
		}
		return num.intValue();
	}
}
