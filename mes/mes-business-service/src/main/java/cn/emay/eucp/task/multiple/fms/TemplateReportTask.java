package cn.emay.eucp.task.multiple.fms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.cache.GlobalConstant;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateReportResult;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateWaitingReportDto;
import cn.emay.eucp.common.dto.fms.template.ReportFmsTemplateDto;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateChannelReport;
import cn.emay.eucp.data.service.fms.FmsTemplateChannelReportService;
import cn.emay.eucp.task.multiple.constant.CacheConstant;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

/**
 * 
* @项目名称：eucp-fms-business-service 
* @类描述：   模板报备预处理任务
* @创建人：dinghaijiao   
* @创建时间：2019年5月7日 下午3:55:21   
* @修改人：dinghaijiao   
* @修改时间：2019年5月7日 下午3:55:21   
* @修改备注：
 */
public class TemplateReportTask implements PeriodTask {

	private static final Logger logger = Logger.getLogger(TemplateReportTask.class);
	private static RedisClient redis = BeanFactoryUtils.getBean(RedisClient.class);
	private static FmsTemplateChannelReportService fmsTemplateChannelReportService = BeanFactoryUtils.getBean(FmsTemplateChannelReportService.class);

	@Override
	public long period() {
		long size = redis.llen(RedisConstants.FMS_TEMPLATE_WAITING_REPORT_QUEUE);
		if (size > 0) {
			return 50;
		} else {
			return 500L;
		}
	}

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		long start = System.currentTimeMillis();
		FmsTemplateWaitingReportDto fmsTemplateWaitingReportDto = redis.rpop(RedisConstants.FMS_TEMPLATE_WAITING_REPORT_QUEUE, FmsTemplateWaitingReportDto.class);
		if (null == fmsTemplateWaitingReportDto) {
			return TaskResult.notDoBusinessResult();
		}
		String templateId = fmsTemplateWaitingReportDto.getTemplateId();
		Long channelId = fmsTemplateWaitingReportDto.getChannelId();
		List<String> rollBack = new ArrayList<String>();
		try {
			FmsChannel channel = CacheConstant.channelMap.get(channelId);
			int state = FmsTemplate.REPORT_SUBMIT;
			if (channel.getIsNeedReport() != null && channel.getIsNeedReport().intValue() == 0) {// 如果该通道不需要报备，报备状态为成功
				state = FmsTemplate.REPORT_OK;
			} else if (channel.getReportType().intValue() == 2 || channel.getReportType().intValue() == 3) {// 如果该通道需要报备，且是线下报备，则状态为报备中
				state = FmsTemplate.REPORT_DOIND;
			}
			List<String> supportOperators = getChannelOperator(channel);

			boolean ishas = true;
			List<FmsTemplateChannelReport> fmsTemplateChannelReportList = new ArrayList<FmsTemplateChannelReport>();
			for (String operatorCode : GlobalConstant.OPERATOR_CODES) {
				if (!supportOperators.contains(operatorCode)) {
					continue;
				}

				boolean notExistResult = saveReportInRedis(templateId, channelId, operatorCode, state);
				if (notExistResult) {// 之前该模板未在这个通道上报备过
					rollBack.add(templateId + "," + channelId + "," + operatorCode);
					// 入库
					buildTemplateChannelReport(fmsTemplateChannelReportList, templateId, channelId, operatorCode, state);
					ishas = false;
				}
			}
			if (ishas) {
				logger.info("模板报备任务执行成功，模板id:" + fmsTemplateWaitingReportDto.getTemplateId() + " ,通道id: " + channelId + " 报备已经提交过 ,不再报备");
				return TaskResult.doBusinessSuccessResult();
			}

			if (fmsTemplateChannelReportList.size() > 0) {
				fmsTemplateChannelReportService.saveBatch(fmsTemplateChannelReportList);
			}
			if (state == FmsTemplate.REPORT_SUBMIT) {// 状态为提交时，则需要提交到通道报备
				ReportFmsTemplateDto reportFmsTemplateDto = new ReportFmsTemplateDto(fmsTemplateWaitingReportDto.getTemplateId(), fmsTemplateWaitingReportDto.getTemplateType(),
						fmsTemplateWaitingReportDto.getContent(), fmsTemplateWaitingReportDto.getVariable());
				redis.lpush(RedisConstants.CHANNEL_REPORT_TEMPLATE_QUEUE + channelId, reportFmsTemplateDto, -1);
			}
			logger.info("模板报备任务执行成功，模板id:" + fmsTemplateWaitingReportDto.getTemplateId() + " ,通道id: " + channelId + " ,耗时：" + (System.currentTimeMillis() - start) + "ms");
		} catch (Exception e) {
			logger.error("模板报备任务执行失败，模板id:" + fmsTemplateWaitingReportDto.getTemplateId() + " ,通道id: " + channelId, e);
			// 回滚
			redis.lpush(RedisConstants.FMS_TEMPLATE_WAITING_REPORT_QUEUE, 0, fmsTemplateWaitingReportDto);
			if (rollBack.size() > 0) {
				for (String field : rollBack) {
					redis.hdel(RedisConstants.FMS_TEMPLATEID_CHANNELID_HASH, field);
				}
			}
		}
		return TaskResult.doBusinessSuccessResult();
	}

	private List<String> getChannelOperator(FmsChannel channel) {
		List<String> supportOperators = new ArrayList<String>();
		String providers = channel.getProviders();
		String[] proveiderArray = providers.split(",");
		for (String provider : proveiderArray) {
			if (provider.equals("1")) {
				supportOperators.add("CMCC");
			} else if (provider.equals("2")) {
				supportOperators.add("CUCC");
			} else if (provider.equals("3")) {
				supportOperators.add("CTCC");
			}
		}
		return supportOperators;

	}

	/**
	 * 
	 * @Title: saveReportInRedis
	 * @Description: 将模板通道报备关系入redis
	 * @param @param
	 *            templateId
	 * @param @param
	 *            channelId
	 * @param @param
	 *            operatorCode
	 * @param @return
	 * @return boolean true:入redis成功（之前没有这个数据）；false:入redis失败（该模板通道关系已经入库了）
	 * @throws TODO
	 */
	private boolean saveReportInRedis(String templateId, Long channelId, String operatorCode, Integer state) {
		String field = templateId + "," + channelId + "," + operatorCode;
		FmsTemplateReportResult result = new FmsTemplateReportResult();
		result.setState(state);
		return redis.hsetnx(RedisConstants.FMS_TEMPLATEID_CHANNELID_HASH, field, result, 0);
	}

	/**
	 * 
	 * @Title: buildTemplateChannelReport
	 * @Description: 模板通道关系入库
	 * @param @param
	 *            fmsTemplateChannelReportList
	 * @param @param
	 *            templateId
	 * @param @param
	 *            channelId
	 * @param @param
	 *            operatorCode
	 * @return void
	 * @throws TODO
	 */
	private void buildTemplateChannelReport(List<FmsTemplateChannelReport> fmsTemplateChannelReportList, String templateId, Long channelId, String operatorCode, Integer state) {
		FmsTemplateChannelReport fmsTemplateChannelReport = new FmsTemplateChannelReport();
		fmsTemplateChannelReport.setChannelId(channelId);
		fmsTemplateChannelReport.setCreateTime(new Date());
		fmsTemplateChannelReport.setOperatorCode(operatorCode);
		fmsTemplateChannelReport.setState(state);
		fmsTemplateChannelReport.setTemplateId(templateId);
		if (state != FmsTemplate.REPORT_DOIND) {
			fmsTemplateChannelReport.setAuditTime(new Date());
		}
		fmsTemplateChannelReportList.add(fmsTemplateChannelReport);
	}

}
