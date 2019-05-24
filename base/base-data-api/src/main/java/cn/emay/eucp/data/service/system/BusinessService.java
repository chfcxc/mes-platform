package cn.emay.eucp.data.service.system;

import java.util.List;

import cn.emay.eucp.common.moudle.db.system.Business;

public interface BusinessService {

	List<Business> findAllBusiness();

	List<String> findBusinessCodeByIds(String ids);

	List<Business> findUserOpenBusinessList(String ids);
}
