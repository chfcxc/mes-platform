package cn.emay.eucp.data.service.fms;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeParam;

public interface FmsServiceCodeParamService {

	Map<String, String> findbyAppid(List<String> list);

	List<FmsServiceCodeParam> findByLastUpdateTime(Date date, int currentPage, int pageSize);

}
