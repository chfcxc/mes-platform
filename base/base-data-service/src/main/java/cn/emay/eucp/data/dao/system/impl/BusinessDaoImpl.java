package cn.emay.eucp.data.dao.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.emay.eucp.common.moudle.db.system.Business;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.BusinessDao;

@Repository
public class BusinessDaoImpl extends PojoDaoImpl<Business> implements BusinessDao {

	@Override
	public List<Business> findAllBusiness() {
		String hql = "from Business order by businessIndex ";
		return this.getListResult(Business.class, hql);
	}
	
	@Override
	public List<Business> findByIds(List<Long> ids){
		Map<String, Object> params = new HashMap<String,Object>();
		String hql = "from Business where id in (:ids)";
		params.put("ids", ids);
		return this.getListResult(Business.class, hql, params);
	}

}
