package cn.emay.eucp.task.multiple.fms;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.json.JsonHelper;
import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.cache.GlobalConstant;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateSaveDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateWaitingReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBusinessType;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateServiceCodeAssign;
import cn.emay.eucp.data.service.fms.FmsTemplateServiceCodeAssignService;
import cn.emay.eucp.task.multiple.constant.CacheConstant;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

/**
 * 
 * @项目名称：eucp-fms-business-service  
 * @类描述：  接口报备的模板入库任务 @创建人：dinghaijiao  
 * @创建时间：2019年5月7日 下午3:53:22   
 * @修改人：dinghaijiao  
 * @修改时间：2019年5月7日 下午3:53:22   
 * @修改备注：
 */
public class SaveTemplateTask implements PeriodTask {

	private static final Logger logger = Logger.getLogger(SaveTemplateTask.class);
	private static FmsTemplateServiceCodeAssignService fmsTemplateServiceCodeAssignService = BeanFactoryUtils.getBean(FmsTemplateServiceCodeAssignService.class);
	private static RedisClient redis = BeanFactoryUtils.getBean(RedisClient.class);

	@Override
	public long period() {
		long size = redis.llen(RedisConstants.FMS_TEMPLATE_SAVE_QUEUE);
		if (size > 0) {
			return 50;
		} else {
			return 500L;
		}
	}

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		long start = System.currentTimeMillis();
		FmsTemplateSaveDto fmsTemplateSaveDto = redis.rpop(RedisConstants.FMS_TEMPLATE_SAVE_QUEUE, FmsTemplateSaveDto.class);
		if (null != fmsTemplateSaveDto) {
			String appId = fmsTemplateSaveDto.getAppId();
			int sendType = fmsTemplateSaveDto.getSendType() == null ? FmsTemplateServiceCodeAssign.SEND_TYPE_INTERFACE : fmsTemplateSaveDto.getSendType();
			int templateType = fmsTemplateSaveDto.getTemplateType();
			String templateId = fmsTemplateSaveDto.getTemplateId();
			FmsServiceCode fmsServiceCode = CacheConstant.serviceCodeMap.get(appId);
			if (null == fmsServiceCode) {
				logger.error("服务号：" + appId + " 不存在--->" + JsonHelper.toJsonString(fmsTemplateSaveDto));
				return TaskResult.doBusinessFailResult();
			}
			Long contentTypeId = fmsServiceCode.getBusinessTypeId();
			FmsBusinessType fmsBusinessType = CacheConstant.businessMap.get(contentTypeId);
			Long businessTypeId = fmsBusinessType.getParentId();

			Map<String, String> serviceCodeChannelMap = CacheConstant.serviceCodeChannelMap;
			Set<Long> channelIds = new HashSet<Long>();
			boolean noChannel = true;// 判断服务号是否配置了通道
			for (String operatorCode : GlobalConstant.OPERATOR_CODES) {
				String key = appId + "," + templateType + "," + operatorCode;
				String channelId = serviceCodeChannelMap.get(key);
				if (null != channelId) {
					noChannel = false;
					channelIds.add(Long.parseLong(channelId));
				}
			}
			FmsTemplateServiceCodeAssign entity = new FmsTemplateServiceCodeAssign();
			entity.setAppId(appId);
			entity.setAuditState(FmsTemplateServiceCodeAssign.AUDIT_DOING);
			entity.setBusinessTypeId(businessTypeId);
			entity.setSaveType(fmsBusinessType.getSaveType());
			entity.setContentTypeId(contentTypeId);
			entity.setSendType(sendType);
			entity.setTemplateId(templateId);
			entity.setCreateTime(new Date());
			if (noChannel) {// 如果没有配置通道，状态直接为审核完成
				entity.setAuditState(FmsTemplateServiceCodeAssign.AUDIT_COMPLETE);
			}
			try {
				// 服务号与模板关系以及模板入库
				fmsTemplateServiceCodeAssignService.addTemplateServiceCodeAssignAndReport(parseTemplate(fmsTemplateSaveDto), fmsTemplateSaveDto.getSave(), entity);
				for (Long channelId : channelIds) {// 入待报备队列
					redis.lpush(RedisConstants.FMS_TEMPLATE_WAITING_REPORT_QUEUE, 0, parseWaitingReportDto(channelId, fmsTemplateSaveDto));
				}
				logger.info("报备模板数据入库完成，模板id: " + fmsTemplateSaveDto.getTemplateId() + " ,耗时： " + (System.currentTimeMillis() - start) + "ms");
			} catch (Exception e) {
				logger.error("报备模板数据入库失败，模板id：" + entity.getTemplateId(), e);
				// 回滚
				redis.lpush(RedisConstants.FMS_TEMPLATE_SAVE_QUEUE, 0, fmsTemplateSaveDto);
				return TaskResult.doBusinessFailResult();
			}
		} else {
			return TaskResult.notDoBusinessResult();
		}
		return TaskResult.doBusinessSuccessResult();
	}

	private FmsTemplate parseTemplate(FmsTemplateSaveDto fmsTemplateSaveDto) {
		FmsTemplate template = new FmsTemplate();
		template.setContent(fmsTemplateSaveDto.getContent());
		template.setSubmitTime(fmsTemplateSaveDto.getSubmitTime());
		template.setId(fmsTemplateSaveDto.getTemplateId());
		template.setTemplateType(fmsTemplateSaveDto.getTemplateType());
		template.setVariable(fmsTemplateSaveDto.getVariable());
		template.setCreateTime(new Date());
		return template;
	}

	private FmsTemplateWaitingReportDto parseWaitingReportDto(Long channelId, FmsTemplateSaveDto fmsTemplateSaveDto) {
		FmsTemplateWaitingReportDto dto = new FmsTemplateWaitingReportDto();
		dto.setChannelId(channelId);
		dto.setContent(fmsTemplateSaveDto.getContent());
		dto.setTemplateId(fmsTemplateSaveDto.getTemplateId());
		dto.setTemplateType(fmsTemplateSaveDto.getTemplateType());
		dto.setVariable(fmsTemplateSaveDto.getVariable());
		return dto;
	}
}
