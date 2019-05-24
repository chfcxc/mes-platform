package cn.emay.channel.framework.api.http.query;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.emay.channel.framework.cache.FQueueCache;
import cn.emay.channel.framework.core.ChannelFrameWork;
import cn.emay.channel.framework.dto.Constants;
import cn.emay.channel.framework.dto.FmsChannelReport;
import cn.emay.common.json.JsonHelper;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

public class QueryReportTask implements PeriodTask {

	private Logger logger = Logger.getLogger(QueryReportTask.class);
	private static long period = 500L;

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		QueryReport queryReport = (QueryReport) ChannelFrameWork.getInstance().getchannel();
		Map<String, String> fmsChannelMap = ChannelFrameWork.getInstance().getFmsChannelParamMap();
		try {
			List<FmsChannelReport> reportList = queryReport.query();
			if (reportList != null && reportList.size() > 0) {
				for (FmsChannelReport report : reportList) {
					String json = JsonHelper.toJsonString(report);
					FQueueCache.putValue(Constants.REPORTQUEUE, json);
				}
				period = 30L;
			} else {
				if (fmsChannelMap.containsKey("sleepTime")) {
					try {
						period = Long.parseLong(fmsChannelMap.get("sleepTime"));
					} catch (Exception e) {
						period = 3000L;
					}
				} else {
					period = 3000L;
				}
				return TaskResult.notDoBusinessResult();
			}
		} catch (Exception e) {
			logger.info(ChannelFrameWork.getInstance().getChannelId() + "QueryReportTask,主动获取状态报告异常！");
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
