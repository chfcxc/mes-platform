package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateDto;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateServiceCodeAssign;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsTemplateServiceCodeAssignDao extends BaseSuperDao<FmsTemplateServiceCodeAssign> {

	Page<FmsTemplateDto> findTempLetPage(String title, String content, List<String> appId, Long businessTypeId, Integer saveType, Long contentTypeId, Integer messageType, Integer submitType,
			Integer auditState, Date startTime, Date endTime, int start, int limit, Integer templateType);

	List<FmsTemplateServiceCodeAssign> findByAudiState(Integer auditState, int currentPage, int pageSize);

	List<FmsTemplateServiceCodeAssign> findByLastUpdateTime(Date date, int currentPage, int pageSize);

	List<FmsTemplateServiceCodeAssign> findNeedRepeatReoportList();

	List<FmsTemplateServiceCodeAssign> findByAppIdAndTemplate(String appId, String templateId);

}
