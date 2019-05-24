package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateDto;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateServiceCodeAssign;

public interface FmsTemplateServiceCodeAssignService {

	void addTemplateServiceCodeAssign(FmsTemplateServiceCodeAssign entity);

	void updateTemplateServiceCodeAssign(FmsTemplateServiceCodeAssign entity);

	Page<FmsTemplateDto> findTempLetPage(String title, String content, List<String> appId, Long businessTypeId, Integer saveType, Long contentTypeId, Integer messageType, Integer submitType,
			Integer auditState, Date startTime, Date endTime, int start, int limit, Integer templateType);

	void addTemplateServiceCodeAssignAndReport(FmsTemplate fmsTemplate, Boolean save, FmsTemplateServiceCodeAssign entity);

	void save(FmsTemplateServiceCodeAssign entity);

	List<FmsTemplateServiceCodeAssign> findByAudiState(Integer auditDoing, int start, int limit);

	void updateBatch(List<FmsTemplateServiceCodeAssign> updateList);

	List<FmsTemplateServiceCodeAssign> findByLastUpdateTime(Date date, int currentPage, int pageSize);

	List<FmsTemplateServiceCodeAssign> findNeedRepeatReoportList();

	List<FmsTemplateServiceCodeAssign> findByAppIdAndTemplateId(Long userId, Long serviceCodeId, String templateId);

}
