package cn.emay.channel.framework.api.http.query;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.emay.channel.framework.cache.FQueueCache;
import cn.emay.channel.framework.core.ChannelFrameWork;
import cn.emay.channel.framework.dto.Constants;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateChannelResponseDto;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

/** @author dejun
 * @version 创建时间：2019年5月6日 下午3:29:24 类说明 */
public class QueryAuditTask implements PeriodTask {

	private Logger logger = Logger.getLogger(QueryAuditTask.class);
	private static long period = 500L;

	@Override
	public TaskResult exec(Map<String, String> arg0) {
		QueryAudit queryAudit = (QueryAudit) ChannelFrameWork.getInstance().getchannel();
		Map<String, String> fmsChannelMap = ChannelFrameWork.getInstance().getFmsChannelParamMap();
		try {
			List<FmsTemplateChannelResponseDto> auditList = queryAudit.queryAudit();
			if (auditList != null && auditList.size() > 0) {
				for (FmsTemplateChannelResponseDto fmsTemplateChannelResponseDto : auditList) {
					String json = JsonHelper.toJsonString(fmsTemplateChannelResponseDto);
					FQueueCache.putValue(Constants.AUDITREPORTQUEUE, json);
				}
				period = 30L;
			} else {
				if (fmsChannelMap.containsKey("sleepAuditTime")) {
					try {
						period = Long.parseLong(fmsChannelMap.get("sleepAuditTime"));
					} catch (Exception e) {
						period = 3000L;
					}
				} else {
					period = 3000L;
				}
				return TaskResult.notDoBusinessResult();
			}
		} catch (Exception e) {
			logger.info(ChannelFrameWork.getInstance().getChannelId() + "QueryAuditTask,主动获取报备结果异常！");
			period = 5000L;
			return TaskResult.doBusinessFailResult();
		}
		return TaskResult.doBusinessSuccessResult();
	}

	@Override
	public long period() {
		return period;
	}

}
