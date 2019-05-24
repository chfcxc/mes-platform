package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;

import cn.emay.eucp.common.dto.fms.template.UpdateTemplateAuditStateDto;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateChannelReport;

public interface FmsTemplateChannelReportService {

	FmsTemplateChannelReport findById(Long id);

	void addFmsTemplateChannelReport(FmsTemplateChannelReport entity);

	void updateFmsTemplateChannelReport(FmsTemplateChannelReport entity);

	void updateAuditState(List<UpdateTemplateAuditStateDto> list, boolean isByOperatorCode);

	List<FmsTemplateChannelReport> findByTemplateId(String templateId);

	List<FmsTemplateChannelReport> findByTemplateIdAndChannelId(String templateId, Long channelId);

	FmsTemplateChannelReport findByParams(String templateId, Long channelId, String operatorCode);

	void deleteByTemplateIdAndChannelId(String templateId, Long channelId);

	void saveBatch(List<FmsTemplateChannelReport> list);

	List<FmsTemplateChannelReport> findByLastUpdateTime(Date date, int currentPage, int pageSize);

	void save(FmsTemplateChannelReport fmsTemplateChannelReport);

}
