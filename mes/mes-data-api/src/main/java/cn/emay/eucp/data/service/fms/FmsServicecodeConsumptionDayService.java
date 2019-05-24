package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.statices.FmsServicecodeConsumptionReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionDay;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionMonth;

public interface FmsServicecodeConsumptionDayService {

	void deleteByTime(String day);

	void saveList(List<FmsServicecodeConsumptionDay> list);

	List<FmsServicecodeConsumptionMonth> findReportMonth(Date lastMonthFirstDay, Date monthLastDay, int currentPage, int pageSize);

	Page<FmsServicecodeConsumptionReportDto> findPage(String rate, String enterpriseId, Long serviceCodeId, Date startTime, Date endTime, int start, int limit);

}
