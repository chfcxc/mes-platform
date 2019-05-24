package cn.emay.eucp.data.dao.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Industry;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.IndustryDao;
import cn.emay.eucp.util.RegularCheckUtils;

@Repository
public class IndustryDaoImpl extends PojoDaoImpl<Industry> implements IndustryDao {
	
	@Override
	public Page<Industry> findPage (String industry,int start,int limit){
		List<Object> parameters = new ArrayList<Object>();
		String sql = "select * from system_industry s where s.is_delete = 0";
		if(!StringUtils.isEmpty(industry)) {
			sql += " and s.industry like ?";
			parameters.add("%"+RegularCheckUtils.specialCodeEscape(industry)+"%");
		}
		return this.findSqlForPageForMysql(Industry.class, sql, parameters, start, limit);
	}

	@Override
	public Industry getIndustry(String industry,Long id) {
		Map<String, Object> params = new HashMap<String,Object>();
		String hql = "from Industry where industry =:industry";
		params.put("industry", industry);
		if(id != null && id.longValue() > 0l) {
			hql += " and id != :id";
			params.put("id", id);
		}
		return this.getUniqueResult(Industry.class, hql, params);
	}
	
	@Override
	public List<Industry> findAllIndustry(){
		List<Object> parameters = new ArrayList<Object>();
		String sql = "select s.id,s.industry from system_industry s where s.is_delete = 0 order by s.industry asc";
		return this.findSqlForListObj(Industry.class, sql, parameters);
	}

	@Override
	public List<Industry> findList(int currentPage, int pageSize, Date lastUpdateTime) {
		String sql = "select s.id,s.industry from system_industry s where s.is_delete = 0";
		List<Object> parameters = new ArrayList<Object>();
		if (null != lastUpdateTime) {
			sql += " and a.last_update_time>=?";
			parameters.add(lastUpdateTime);
		}
		return this.findSqlForListForMysql(Industry.class, sql, parameters, currentPage, pageSize);
	}
}
