package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.statices.FmsEnterpriseContentTypeReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeYear;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsEnterpriseContentTypeYearDao extends BaseSuperDao<FmsEnterpriseContentTypeYear> {
	public void deleteYear(String year);

	public void saveList(List<FmsEnterpriseContentTypeYear> list);

	public Page<FmsEnterpriseContentTypeReportDto> findPage(Long businessTypeId, Long serviceCodeId, Date startTime, Date endTime, int start, int limit, String enterpriseIds);

}
