package cn.emay.eucp.data.service.fms;

import java.util.List;

import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeMonth;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeYear;

public interface FmsEnterpriseContentTypeMonthService {
	public void deleteMonth(String month);

	public void saveList(List<FmsEnterpriseContentTypeMonth> list);

	public List<FmsEnterpriseContentTypeYear> findYear(String year, int start, int limit);

}
