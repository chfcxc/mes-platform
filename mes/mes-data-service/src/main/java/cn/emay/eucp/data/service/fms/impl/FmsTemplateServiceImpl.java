package cn.emay.eucp.data.service.fms.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.eucp.common.cache.GlobalConstant;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateRedisDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateWaitingReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsBusinessType;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateServiceCodeAssign;
import cn.emay.eucp.common.util.TemplateIdUtil;
import cn.emay.eucp.data.dao.fms.FmsBusinessTypeDao;
import cn.emay.eucp.data.dao.fms.FmsServiceCodeChannelDao;
import cn.emay.eucp.data.dao.fms.FmsServiceCodeDao;
import cn.emay.eucp.data.dao.fms.FmsTemplateChannelReportDao;
import cn.emay.eucp.data.dao.fms.FmsTemplateDao;
import cn.emay.eucp.data.dao.fms.FmsTemplateServiceCodeAssignDao;
import cn.emay.eucp.data.service.fms.FmsTemplateService;

@Service("fmsTemplateService")
public class FmsTemplateServiceImpl implements FmsTemplateService {

	@Resource
	private RedisClient redis;
	@Resource
	private FmsTemplateDao fmsTemplateDao;
	@Resource
	private FmsTemplateServiceCodeAssignDao fmsTemplateServiceCodeAssignDao;
	@Resource
	private FmsServiceCodeDao fmsServiceCodeDao;
	@Resource
	private FmsBusinessTypeDao fmsBusinessTypeDao;
	@Resource
	private FmsTemplateChannelReportDao fmsTemplateChannelReportDao;
	@Resource
	private FmsServiceCodeChannelDao fmsServiceCodeChannelDao;

	@Override
	public void addTemplate(String appId, String templateName, String content, String variable, long userId) {
		templateReport(appId, templateName, content, variable, userId, 0);
	}

	/** 报备模板
	 * 
	 * @param appId
	 * @param remoteIp
	 * @param content
	 * @param variable
	 * @param i
	 * @return */
	private void templateReport(String appId, String templateName, String content, String variable, long userId, int i) {
		String templateId = null;
		int templateType = (variable == null || variable.isEmpty()) ? 0 : 1;
		try {
			templateId = TemplateIdUtil.getTemplateId(content, templateType, i);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// set 模板主数据
		FmsTemplateRedisDto template = new FmsTemplateRedisDto();
		template.setContent(content);
		template.setVariable(variable);
		boolean flag = redis.hsetnx(RedisConstants.FMS_TEMPLATE_HASH, templateId, template, -1);
		if (!flag) {
			FmsTemplateRedisDto oldTemplate = redis.hget(RedisConstants.FMS_TEMPLATE_HASH, templateId, FmsTemplateRedisDto.class);
			if (!oldTemplate.getContent().equals(content)) {
				templateReport(appId, templateName, content, variable, userId, i + 1);
				return;
			}
		} else {
			// 模板主数据入库
			FmsTemplate entity = new FmsTemplate();
			entity.setUserId(userId);
			entity.setContent(content);
			entity.setVariable(variable);
			entity.setCreateTime(new Date());
			entity.setSubmitTime(new Date());
			entity.setId(templateId);
			entity.setTemplateType(templateType);
			fmsTemplateDao.save(entity);

		}
		// appId与模板关系入库
		FmsServiceCode serviceCode = fmsServiceCodeDao.findByserviceCode(appId);
		FmsBusinessType fmsBusinessType = fmsBusinessTypeDao.findById(serviceCode.getBusinessTypeId());
		FmsTemplateServiceCodeAssign tscag = new FmsTemplateServiceCodeAssign();
		tscag.setAppId(appId);
		tscag.setCreateTime(new Date());
		tscag.setSendType(FmsTemplateServiceCodeAssign.SEND_TYPE_PAGE);
		tscag.setTemplateId(templateId);
		tscag.setAuditState(FmsTemplateServiceCodeAssign.AUDIT_DOING);
		tscag.setTemplateName(templateName);
		tscag.setUserId(userId);
		tscag.setBusinessTypeId(fmsBusinessType.getParentId());
		tscag.setContentTypeId(fmsBusinessType.getId());
		tscag.setSaveType(fmsBusinessType.getSaveType());
		redis.hset(RedisConstants.FMS_APPID_TEMPLATEID_HASH, appId + "_" + templateId, 1, -1);
		// 模板与通道报备状态 如果已存在，不入库，如果没有存在，那么入库
		Set<Long> channelIds = new TreeSet<Long>();
		boolean noChannel = true;
		for (String operatorCode : GlobalConstant.OPERATOR_CODES) {
			FmsServiceCodeChannel asgn = fmsServiceCodeChannelDao.findFmsServiceCodeChannel(appId, templateType, operatorCode);
			if (asgn == null) {
				continue;
			}
			noChannel = false;
			Long channelId = asgn.getChannelId();
			channelIds.add(channelId);
		}
		if (noChannel) {
			tscag.setAuditState(FmsTemplateServiceCodeAssign.AUDIT_COMPLETE);
		}
		fmsTemplateServiceCodeAssignDao.save(tscag);
		for (Long channelId : channelIds) {// 入待报备队列
			FmsTemplateWaitingReportDto fmsTemplateWaitingReportDto = new FmsTemplateWaitingReportDto();
			fmsTemplateWaitingReportDto.setChannelId(channelId);
			fmsTemplateWaitingReportDto.setTemplateId(templateId);
			fmsTemplateWaitingReportDto.setTemplateType(templateType);
			fmsTemplateWaitingReportDto.setContent(content);
			fmsTemplateWaitingReportDto.setVariable(variable);
			redis.lpush(RedisConstants.FMS_TEMPLATE_WAITING_REPORT_QUEUE, 0, fmsTemplateWaitingReportDto);
		}
		return;
	}

	@Override
	public void save(FmsTemplate template) {
		fmsTemplateDao.save(template);
	}

	@Override
	public List<FmsTemplate> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		return fmsTemplateDao.findByLastUpdateTime(date, currentPage, pageSize);
	}
}
