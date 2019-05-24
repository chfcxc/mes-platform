package cn.emay.eucp.data.dao.system;

import java.util.List;

import cn.emay.eucp.common.moudle.db.system.Business;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface BusinessDao extends BaseSuperDao<Business> {

	List<Business> findAllBusiness();

	List<Business> findByIds(List<Long> ids);

}
