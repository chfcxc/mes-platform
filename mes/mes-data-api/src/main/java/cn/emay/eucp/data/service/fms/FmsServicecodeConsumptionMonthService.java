package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;

import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionMonth;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionYear;

public interface FmsServicecodeConsumptionMonthService {
	public Long countByServiceCode(List<Long> serviceCodeId, Date startTime, Date endTime);

	public void deleteMonth(String month);

	public void batchSave(List<FmsServicecodeConsumptionMonth> insertList);

	public List<FmsServicecodeConsumptionYear> findReportYear(String year, int start, int limit);

}
