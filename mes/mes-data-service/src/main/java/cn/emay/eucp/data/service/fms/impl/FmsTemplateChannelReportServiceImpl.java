package cn.emay.eucp.data.service.fms.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.eucp.common.dto.fms.template.UpdateTemplateAuditStateDto;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateChannelReport;
import cn.emay.eucp.data.dao.fms.FmsTemplateChannelReportDao;
import cn.emay.eucp.data.service.fms.FmsTemplateChannelReportService;

@Service("fmsTemplateChannelReportService")
public class FmsTemplateChannelReportServiceImpl implements FmsTemplateChannelReportService {
	@Resource
	private FmsTemplateChannelReportDao fmsTemplateChannelReportDao;

	@Override
	public FmsTemplateChannelReport findById(Long id) {
		return fmsTemplateChannelReportDao.findById(id);
	}

	@Override
	public void addFmsTemplateChannelReport(FmsTemplateChannelReport entity) {
		fmsTemplateChannelReportDao.save(entity);
	}

	@Override
	public void updateFmsTemplateChannelReport(FmsTemplateChannelReport entity) {
		fmsTemplateChannelReportDao.update(entity);
	}

	@Override
	public void updateAuditState(List<UpdateTemplateAuditStateDto> list, boolean isByOperatorCode) {
		fmsTemplateChannelReportDao.updateAuditState(list, isByOperatorCode);
	}

	@Override
	public List<FmsTemplateChannelReport> findByTemplateId(String templateId) {
		return fmsTemplateChannelReportDao.findByTemplateId(templateId);
	}

	@Override
	public List<FmsTemplateChannelReport> findByTemplateIdAndChannelId(String templateId, Long channelId) {
		return fmsTemplateChannelReportDao.findByTemplateIdAndChannelId(templateId, channelId);
	}

	@Override
	public FmsTemplateChannelReport findByParams(String templateId, Long channelId, String operatorCode) {
		return fmsTemplateChannelReportDao.findByParams(templateId, channelId, operatorCode);
	}

	@Override
	public void deleteByTemplateIdAndChannelId(String templateId, Long channelId) {
		fmsTemplateChannelReportDao.deleteByTemplateIdAndChannelId(templateId, channelId);
	}

	@Override
	public void saveBatch(List<FmsTemplateChannelReport> list) {
		fmsTemplateChannelReportDao.saveBatchByBeans(list);
	}

	@Override
	public List<FmsTemplateChannelReport> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		return fmsTemplateChannelReportDao.findByLastUpdateTime(date, currentPage, pageSize);
	}

	@Override
	public void save(FmsTemplateChannelReport fmsTemplateChannelReport) {
		fmsTemplateChannelReportDao.save(fmsTemplateChannelReport);
	}

}
