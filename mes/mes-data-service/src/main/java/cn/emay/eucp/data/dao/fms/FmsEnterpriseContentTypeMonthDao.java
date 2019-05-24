package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.statices.FmsEnterpriseContentTypeReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeMonth;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeYear;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsEnterpriseContentTypeMonthDao extends BaseSuperDao<FmsEnterpriseContentTypeMonth> {
	public void deleteMonth(String month);

	public void saveList(List<FmsEnterpriseContentTypeMonth> list);

	public List<FmsEnterpriseContentTypeYear> findYear(String year, int start, int limit);

	public Page<FmsEnterpriseContentTypeReportDto> findPage(Long businessTypeId, Long serviceCodeId, Date startTime, Date endTime, int start, int limit, String enterpriseIds);

}
