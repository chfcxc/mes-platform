package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.statices.FmsEnterpriseContentTypeReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeDay;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeMonth;

public interface FmsEnterpriseContentTypeDayService {
	public void deleteByTime(String day);

	public void saveList(List<FmsEnterpriseContentTypeDay> list);

	public List<FmsEnterpriseContentTypeMonth> findMonth(Date startTime, Date endTime, int currentPage, int pageSize);

	public Page<FmsEnterpriseContentTypeReportDto> findPage(String rate, Long businessTypeId, Long serviceCodeId, Date startTime, Date endTime, int start, int limit, String enterpriseIds);

}
