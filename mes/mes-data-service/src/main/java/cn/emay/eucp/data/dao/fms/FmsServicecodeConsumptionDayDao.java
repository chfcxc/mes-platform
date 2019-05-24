package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.statices.FmsServicecodeConsumptionReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionDay;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionMonth;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsServicecodeConsumptionDayDao extends BaseSuperDao<FmsServicecodeConsumptionDay> {
	public void deleteByTime(String day);

	public void saveList(List<FmsServicecodeConsumptionDay> list);

	public List<FmsServicecodeConsumptionMonth> findReportMonth(Date lastMonthFirstDay, Date monthLastDay, int currentPage, int pageSize);

	Page<FmsServicecodeConsumptionReportDto> findPage(String enterpriseId, Long serviceCodeId, Date startTime, Date endTime, int start, int limit);

}
