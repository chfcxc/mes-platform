package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.statices.FmsServicecodeConsumptionReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionYear;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsServicecodeConsumptionYearDao extends BaseSuperDao<FmsServicecodeConsumptionYear> {
	public void delete(String year);

	public void bachSave(List<FmsServicecodeConsumptionYear> insertList);

	public Page<FmsServicecodeConsumptionReportDto> findPage(String enterpriseId, Long serviceCodeId, Date startTime, Date endTime, int start, int limit);
}
