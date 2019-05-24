package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;

import cn.emay.eucp.common.moudle.db.fms.FmsTemplate;

public interface FmsTemplateService {

	void addTemplate(String appId, String templateName, String content, String variable, long userId);

	void save(FmsTemplate template);

	List<FmsTemplate> findByLastUpdateTime(Date date, int currentPage, int pageSize);

}
