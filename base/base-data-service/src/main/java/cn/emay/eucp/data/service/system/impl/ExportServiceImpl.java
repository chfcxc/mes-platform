package cn.emay.eucp.data.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Export;
import cn.emay.eucp.data.dao.system.ExportDao;
import cn.emay.eucp.data.service.system.ExportService;
import cn.emay.util.DateUtil;

@Service("exportService")
public class ExportServiceImpl implements ExportService {
	@Resource
	private ExportDao exportDao;

	@Override
	public void saveSmsExport(Long userId, String module, String queryCriteria, String exportFileName, String systemFor, String businessType) {
		Export entity = new Export();
		entity.setUserId(userId);
		entity.setModule(module);
		entity.setQueryCriteria(queryCriteria);
		entity.setState(Export.STATE_WAITING_GENERATION);
		entity.setBusinessType(businessType);
		entity.setSystemFor(systemFor);
		Date date = new Date();
		String fileName = exportFileName + DateUtil.toString(date, "yyyyMMddHHmmss") + "_" + userId + ".zip";
		entity.setCreateTime(date);
		entity.setFileName(fileName);
		exportDao.save(entity);
	}

	@Override
	public Export findSmsExport(String systemFor, String businessType) {
		return exportDao.findSmsExport(systemFor, businessType);
	}

	@Override
	public void update(Export entity) {
		exportDao.update(entity);
	}

	@Override
	public Page<Export> findPage(int start, int limit, Long userId, String systemFor, String businessType) {
		Page<Export> page = exportDao.findPage(start, limit, userId, systemFor, businessType);
		List<Export> list = page.getList();
		for (Export export : list) {
			export.setPath(null);
		}
		return page;
	}

	@Override
	public Export findById(Long id) {
		return exportDao.findById(id);
	}

	@Override
	public List<Export> findSmsExportBeforeDate(Date date, String systemFor, String businessType) {
		return exportDao.findSmsExportBeforeDate(date, systemFor, businessType);
	}

	@Override
	public void deleteBatch(List<Export> entities) {
		exportDao.deleteBatchByPKid(entities);
	}

	@Override
	public Integer updateBatchSmsExport(Date date, int state, String systemFor, String businessType) {
		return exportDao.updateBatchSmsExport(date, state, systemFor, businessType);
	}

}
