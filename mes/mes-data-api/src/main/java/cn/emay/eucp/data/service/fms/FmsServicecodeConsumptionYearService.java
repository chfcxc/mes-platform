package cn.emay.eucp.data.service.fms;

import java.util.List;

import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionYear;

public interface FmsServicecodeConsumptionYearService {

	void delete(String year);

	void bachSave(List<FmsServicecodeConsumptionYear> insertList);

}
