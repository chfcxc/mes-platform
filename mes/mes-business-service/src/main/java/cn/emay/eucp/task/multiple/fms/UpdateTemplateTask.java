package cn.emay.eucp.task.multiple.fms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateChannelReportDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateReportResult;
import cn.emay.eucp.common.dto.fms.template.UpdateTemplateAuditStateDto;
import cn.emay.eucp.data.service.fms.FmsTemplateChannelReportService;
import cn.emay.eucp.task.multiple.util.FmsUtil;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

/** 更新模板报备状态任务
 * 
 * @author dinghaijiao */
public class UpdateTemplateTask implements PeriodTask {

	private static final Logger logger = Logger.getLogger(UpdateTemplateTask.class);
	private static FmsTemplateChannelReportService fmsTemplateChannelReportService = BeanFactoryUtils.getBean(FmsTemplateChannelReportService.class);
	private static RedisClient redis = BeanFactoryUtils.getBean(RedisClient.class);
	private static int num = 100;

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		List<UpdateTemplateAuditStateDto> updateList = new ArrayList<UpdateTemplateAuditStateDto>();
		List<FmsTemplateChannelReportDto> rollBackList = new ArrayList<FmsTemplateChannelReportDto>();
		try {
			int i = 0;
			while (true) {
				FmsTemplateChannelReportDto updateDto = redis.rpop(RedisConstants.CHANNEL_RESPONSE_TEMPLATE_QUEUE, FmsTemplateChannelReportDto.class);
				if (updateDto == null) {
					break;
				}
				// 如果redis中的状态比更新dto中的状态大，那么不更新
				String field = updateDto.getTemplateId() + "," + updateDto.getChannelId() + "," + updateDto.getOperatorCode();
				FmsTemplateReportResult reportResult = redis.hget(RedisConstants.FMS_TEMPLATEID_CHANNELID_HASH, field, FmsTemplateReportResult.class);
				if (reportResult != null && reportResult.getState().intValue() >= updateDto.getState().intValue()) {
					continue;
				}
				updateList.add(FmsUtil.toUpdateTemplateDto(updateDto));
				rollBackList.add(updateDto);
				i++;
				if (i == num) {
					break;
				}
			}
			if (updateList != null && updateList.size() > 0) {
				// 更新库
				fmsTemplateChannelReportService.updateAuditState(updateList, true);
				// 更新redis
				for (UpdateTemplateAuditStateDto dto : updateList) {
					String field = dto.getTemplateId() + "," + dto.getChannelId() + "," + dto.getOperatorCode();
					FmsTemplateReportResult result = new FmsTemplateReportResult();
					result.setChannelTemplateId(dto.getChannelTemplateId());
					result.setState(dto.getAuditState());
					redis.hset(RedisConstants.FMS_TEMPLATEID_CHANNELID_HASH, field, result, 0);
				}
				logger.info("【UpdateTemplateTask】更新模板报备状态完成，共" + i + "个");
			}
		} catch (Exception e) {
			// 回滚
			if (!rollBackList.isEmpty()) {
				redis.lpush(RedisConstants.CHANNEL_RESPONSE_TEMPLATE_QUEUE, 0, rollBackList.toArray());
				logger.error("【UpdateTemplateTask】更新模板报备状态失败，回滚数据：" + rollBackList.size() + "个", e);
			} else {
				logger.error("【UpdateTemplateTask】更新模板报备状态失败", e);
			}
			return TaskResult.doBusinessFailResult();
		}
		return TaskResult.doBusinessSuccessResult();
	}

	@Override
	public long period() {
		long size = redis.llen(RedisConstants.CHANNEL_RESPONSE_TEMPLATE_QUEUE);
		if (size > 0) {
			return 50;
		} else {
			return 500L;
		}
	}

}
