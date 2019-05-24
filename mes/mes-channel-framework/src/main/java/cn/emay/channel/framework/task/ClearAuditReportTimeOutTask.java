package cn.emay.channel.framework.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

import cn.emay.channel.framework.core.ChannelFrameWork;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateChannelReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;
import cn.emay.jpp.utils.JppUtils;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;
import cn.emay.util.PropertiesUtil;

/** @项目名称：eucp-fms-channel-framework  @类描述：删除模板报备审核状态报告超时
 * 
 *                                      @创建人：dejun  
 * 
 * @创建时间：2019年5月15日 下午4:45:54   @修改人：dejun  
 * @修改时间：2019年5月15日 下午4:45:54   @修改备注： */
public class ClearAuditReportTimeOutTask implements PeriodTask {

	private static final Logger logger = Logger.getLogger(ClearAuditReportTimeOutTask.class);
	private Long period = 1000L;
	private RedisClient redis = JppUtils.getRedis("Redis");
	private int auditTimeOutDay = PropertiesUtil.getIntProperty("timeout.audit.day", "channel.properties", 5);

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		FmsChannel mmsChannel = ChannelFrameWork.getInstance().getFmsChannel();
		if (null == mmsChannel) {
			return TaskResult.doBusinessSuccessResult();
		}
		try {
			long endTime = System.currentTimeMillis() - (auditTimeOutDay * 24 * 60 * 60 * 1000L);
			Set<String> setString = redis.zrangeByScore(RedisConstants.FMS_WAIT_AUDIT_REPORT_ZSET + mmsChannel.getId(), 0, endTime, 0, 1000);
			if (null != setString && setString.size() > 0) {
				List<String> report = new ArrayList<>();
				report.addAll(setString);
				String[] reportArr = setString.toArray(new String[setString.size()]);
				List<String> list = redis.hmget(RedisConstants.FMS_WAIT_AUDIT_REPORT_HASH + mmsChannel.getId(), reportArr);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String auditDto = list.get(0);
						FmsTemplateChannelReportDto hasResponseDTO = JsonHelper.fromJson(new TypeToken<FmsTemplateChannelReportDto>() {
						}, auditDto);
						hasResponseDTO.setState(FmsTemplate.REPORT_TIMEOUT);
						redis.lpush(RedisConstants.CHANNEL_RESPONSE_TEMPLATE_QUEUE, -1, hasResponseDTO);
					}
				}
			}
		} catch (Exception e) {
			logger.info("报备审核超时任务异常", e);
			return TaskResult.doBusinessFailResult();
		}

		return TaskResult.doBusinessSuccessResult();
	}

	@Override
	public long period() {
		return period;
	}

}
