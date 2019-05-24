package cn.emay.eucp.task.multiple.fms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.cache.GlobalConstant;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateReportResult;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateServiceCodeAssign;
import cn.emay.eucp.common.util.TemplateIdUtil;
import cn.emay.eucp.data.service.fms.FmsTemplateServiceCodeAssignService;
import cn.emay.eucp.task.multiple.constant.CacheConstant;
import cn.emay.eucp.task.multiple.util.StringUtil;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

/**
 * 
 * @项目名称：eucp-fms-business-service 
 * @类描述：   更新模板-服务号关联表中的审核状态
 * @创建人：Adorkable  
 * @创建时间：2019年5月9日 下午1:54:32  
 * @修改人：Adorkable  
 * @修改时间：2019年5月9日 下午1:54:32  
 * @修改备注：
 */
public class UpdateAuditStateTask implements PeriodTask {

	private static final Logger logger = Logger.getLogger(UpdateAuditStateTask.class);
	private static FmsTemplateServiceCodeAssignService fmsTemplateServiceCodeAssignService = BeanFactoryUtils.getBean(FmsTemplateServiceCodeAssignService.class);
	private static RedisClient redis = BeanFactoryUtils.getBean(RedisClient.class);
	private static int pageSize = 100;

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		long start = System.currentTimeMillis();
		Map<String, String> map = CacheConstant.serviceCodeChannelMap;
		/******** 将重新配置过通道的服务号，更新其模板审核状态为未审核 *********/
		List<FmsTemplateServiceCodeAssign> needRepeatReportlist = fmsTemplateServiceCodeAssignService.findNeedRepeatReoportList();
		List<FmsTemplateServiceCodeAssign> updateAudiList = new ArrayList<FmsTemplateServiceCodeAssign>();
		for (FmsTemplateServiceCodeAssign dto : needRepeatReportlist) {
			String appId = dto.getAppId();
			String templateId = dto.getTemplateId();
			int templateType = TemplateIdUtil.getTemplateType(templateId);
			boolean notAudit = checkAudi(appId, templateType, templateId, map);
			if (notAudit) {
				dto.setAuditState(FmsTemplateServiceCodeAssign.AUDIT_DOING);
				updateAudiList.add(dto);
			}
		}
		fmsTemplateServiceCodeAssignService.updateBatch(updateAudiList);

		/******** 将报备完成的更新为审核完成 *********/
		int currentPage = 1;
		int excuteNum = 0;
		int successNum = 0;

		while (true) {
			List<FmsTemplateServiceCodeAssign> list = fmsTemplateServiceCodeAssignService.findByAudiState(FmsTemplateServiceCodeAssign.AUDIT_DOING, currentPage, pageSize);
			if (list == null || list.isEmpty()) {
				break;
			}
			excuteNum += list.size();
			currentPage++;
			List<FmsTemplateServiceCodeAssign> updateList = new ArrayList<FmsTemplateServiceCodeAssign>();
			for (FmsTemplateServiceCodeAssign dto : list) {
				String appId = dto.getAppId();
				String templateId = dto.getTemplateId();
				int templateType = TemplateIdUtil.getTemplateType(templateId);
				boolean notAudit = checkAudi(appId, templateType, templateId, map);
				if (notAudit) {
					continue;
				}
				dto.setAuditState(FmsTemplateServiceCodeAssign.AUDIT_COMPLETE);
				updateList.add(dto);
			}
			successNum += updateList.size();
			fmsTemplateServiceCodeAssignService.updateBatch(updateList);
		}

		if (excuteNum == 0) {
			return TaskResult.notDoBusinessResult();
		}
		logger.info("更新模板-服务号关联表中的审核状态任务执行完成，一共查询" + excuteNum + "个 ,更新" + successNum + "个,耗时：" + (System.currentTimeMillis() - start) + "ms");
		return TaskResult.doBusinessSuccessResult();
	}

	@Override
	public long period() {
		return 1000L * 30;
	}

	private boolean checkAudi(String appId, int templateType, String templateId, Map<String, String> map) {
		boolean notAudit = false;
		for (String code : GlobalConstant.OPERATOR_CODES) {
			String key = appId + "," + templateType + "," + code;
			String channelId = map.get(key);
			if (StringUtil.isEmpty(channelId)) {// 该服务号通道未配置完成
				notAudit = true;
				break;
			}
			String field = templateId + "," + channelId + "," + code;
			FmsTemplateReportResult result = redis.hget(RedisConstants.FMS_TEMPLATEID_CHANNELID_HASH, field, FmsTemplateReportResult.class);
			if (result == null) {// 未存在状态
				notAudit = true;
				break;
			}
			int auditState = result.getState();
			if (auditState < FmsTemplate.REPORT_OK || auditState > FmsTemplate.NOT_SUPPORT) {
				notAudit = true;
				break;
			}
		}
		return notAudit;
	}

}
