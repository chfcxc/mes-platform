package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.eucp.common.dto.fms.template.UpdateTemplateAuditStateDto;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateChannelReport;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsTemplateChannelReportDao extends BaseSuperDao<FmsTemplateChannelReport> {

	public List<FmsTemplateChannelReport> findByTempLetIdAndChannelId(Map<String, Set<Long>> queryMap);

	void updateAuditState(List<UpdateTemplateAuditStateDto> list, boolean isByOperatorCode);

	List<FmsTemplateChannelReport> findByTemplateId(String templateId);

	List<FmsTemplateChannelReport> findByTemplateIdAndChannelId(String templateId, Long channelId);

	FmsTemplateChannelReport findByParams(String templateId, Long channelId, String operatorCode);

	void deleteByTemplateIdAndChannelId(String templateId, Long channelId);

	void saveBatchByBeans(List<FmsTemplateChannelReport> list);

	public List<FmsTemplateChannelReport> findByLastUpdateTime(Date date, int currentPage, int pageSize);
}
