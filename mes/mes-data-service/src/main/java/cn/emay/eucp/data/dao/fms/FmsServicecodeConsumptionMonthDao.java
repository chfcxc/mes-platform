package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.statices.FmsServicecodeConsumptionReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionMonth;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionYear;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsServicecodeConsumptionMonthDao extends BaseSuperDao<FmsServicecodeConsumptionMonth> {
	public Long countByServiceCode(List<Long> serviceCodeId, Date startTime, Date endTime);

	public void deleteMonth(String month);

	public void batchSave(List<FmsServicecodeConsumptionMonth> insertList);

	public List<FmsServicecodeConsumptionYear> findReportYear(String year, int start, int limit);

	public Page<FmsServicecodeConsumptionReportDto> findPage(String enterpriseId, Long serviceCodeId, Date startTime, Date endTime, int start, int limit);
}
