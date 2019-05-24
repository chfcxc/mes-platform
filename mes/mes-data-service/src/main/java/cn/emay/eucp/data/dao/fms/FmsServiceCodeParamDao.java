package cn.emay.eucp.data.dao.fms;

import java.util.Date;
import java.util.List;

import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeParam;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface FmsServiceCodeParamDao extends BaseSuperDao<FmsServiceCodeParam> {

	List<FmsServiceCodeParam> findByAppid(List<String> list);

	List<FmsServiceCodeParam> findByLastUpdateTime(Date date, int currentPage, int pageSize);

	void deletebyAppid(String appId);

	FmsServiceCodeParam findbyAppId(String appId);

}
