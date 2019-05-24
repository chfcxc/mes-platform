package cn.emay.eucp.data.service.fms;

import java.util.List;

import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeYear;

public interface FmsEnterpriseContentTypeYearService {
	public void deleteYear(String year);

	public void saveList(List<FmsEnterpriseContentTypeYear> list);

}
