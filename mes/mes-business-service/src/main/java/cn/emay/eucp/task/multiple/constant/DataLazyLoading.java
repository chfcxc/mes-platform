package cn.emay.eucp.task.multiple.constant;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateRedisDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateReportResult;
import cn.emay.store.memory.MemoryMap;

public class DataLazyLoading {
	private MemoryMap vc;
	private RedisClient redis = BeanFactoryUtils.getBean(RedisClient.class);

	private int paramsCacheTime = 60;

	public DataLazyLoading(int paramsCacheTime, MemoryMap vc) {
		this.paramsCacheTime = paramsCacheTime;
		this.vc = vc;
		if (this.paramsCacheTime < 10) {
			this.paramsCacheTime = 10;
		}
	}

	public FmsTemplateRedisDto getTemplate(String templateId) {
		FmsTemplateRedisDto dto = vc.get(templateId, FmsTemplateRedisDto.class);
		if (null == dto) {
			dto = redis.hget(RedisConstants.FMS_TEMPLATE_HASH, templateId, FmsTemplateRedisDto.class);
			if (dto != null) {
				vc.set(templateId, dto, paramsCacheTime);
			} else {
				return null;
			}
		}
		return dto;
	}

	public FmsTemplateReportResult getFmsTemplateReportResult(String template) {
		FmsTemplateReportResult reportResult = vc.get(template, FmsTemplateReportResult.class);
		if (null == reportResult) {
			reportResult = redis.hget(RedisConstants.FMS_TEMPLATEID_CHANNELID_HASH, template, FmsTemplateReportResult.class);
			if (null != reportResult) {
				vc.set(template, reportResult, paramsCacheTime);
			} else {
				return null;
			}
		}
		return reportResult;

	}

}
