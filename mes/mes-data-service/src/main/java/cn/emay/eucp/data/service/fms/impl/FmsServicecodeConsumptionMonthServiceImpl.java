package cn.emay.eucp.data.service.fms.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionMonth;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionYear;
import cn.emay.eucp.data.dao.fms.FmsServicecodeConsumptionMonthDao;
import cn.emay.eucp.data.service.fms.FmsServicecodeConsumptionMonthService;

@Service("fmsServicecodeConsumptionMonthService")
public class FmsServicecodeConsumptionMonthServiceImpl implements FmsServicecodeConsumptionMonthService {
	@Resource
	private FmsServicecodeConsumptionMonthDao fmsServicecodeConsumptionMonthDao;

	@Override
	public Long countByServiceCode(List<Long> serviceCodeId, Date startTime, Date endTime) {
		return fmsServicecodeConsumptionMonthDao.countByServiceCode(serviceCodeId, startTime, endTime);
	}

	@Override
	public void deleteMonth(String month) {
		fmsServicecodeConsumptionMonthDao.deleteMonth(month);
	}

	@Override
	public void batchSave(List<FmsServicecodeConsumptionMonth> insertList) {
		fmsServicecodeConsumptionMonthDao.batchSave(insertList);
	}

	@Override
	public List<FmsServicecodeConsumptionYear> findReportYear(String year, int start, int limit) {
		return fmsServicecodeConsumptionMonthDao.findReportYear(year, start, limit);
	}

}
