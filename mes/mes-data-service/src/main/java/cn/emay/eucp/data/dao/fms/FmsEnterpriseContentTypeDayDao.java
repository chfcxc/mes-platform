package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.statices.FmsEnterpriseContentTypeReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeDay;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeMonth;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsEnterpriseContentTypeDayDao extends BaseSuperDao<FmsEnterpriseContentTypeDay> {
	public void deleteByTime(String day);

	public void saveList(List<FmsEnterpriseContentTypeDay> list);

	public List<FmsEnterpriseContentTypeMonth> findMonth(Date startTime, Date endTime, int currentPage, int pageSize);

	public Page<FmsEnterpriseContentTypeReportDto> findPage(Long businessTypeId, Long serviceCodeId, Date startTime, Date endTime, int start, int limit, String enterpriseIds);

}
