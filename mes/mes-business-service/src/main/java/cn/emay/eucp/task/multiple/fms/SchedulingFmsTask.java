package cn.emay.eucp.task.multiple.fms;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.common.utils.StringUtils;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

/**
 * 
 * @项目名称：eucp-fms-business-service  @类描述：页面发送调度任务，兼容定时发送 @创建人：Adorkable  
 * @创建时间：2019年5月8日 下午3:58:38   @修改人：Adorkable  
 * @修改时间：2019年5月8日 下午3:58:38   @修改备注：
 */
public class SchedulingFmsTask implements PeriodTask {
	private Logger logger = Logger.getLogger(SchedulingFmsTask.class);
	private RedisClient redis = BeanFactoryUtils.getBean(RedisClient.class);

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		long time = System.currentTimeMillis();
		int offset = 0;
		for (;;) {
			Set<String> serialNumbers = redis.zrangeByScore(RedisConstants.FMS_SCHEDULING_QUEUE, 0, time, offset, 500);
			if (serialNumbers == null || serialNumbers.size() == 0) {
				break;
			}
			redis.lpush(RedisConstants.FMS_PAGESEND_SERIALNUMBER_QUEUE, -1, serialNumbers.toArray());
			logger.info("调度流水号：" + StringUtils.join(serialNumbers, ","));
			offset += 500;
		}
		return TaskResult.doBusinessSuccessResult();
	}

	@Override
	public long period() {
		return 1000;
	}
}
