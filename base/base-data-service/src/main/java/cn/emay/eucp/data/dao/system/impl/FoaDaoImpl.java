package cn.emay.eucp.data.dao.system.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Foa;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.FoaDao;
import cn.emay.eucp.util.RegularUtils;

/**
 * 
 * @author gh
 */
@Repository
public class FoaDaoImpl extends PojoDaoImpl<Foa> implements FoaDao {

	@Override
	public List<Foa> findBySystemType(Integer subSystem,Integer businessType) {
		String hql = "from Foa  where 1=1";
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		if(null!=subSystem&&subSystem.intValue()>0) {
			hql = hql + " and subSystem=:subSystem ";
			params.put("subSystem", subSystem);
		}
		
		if(null!=subSystem&&subSystem.intValue()>0) {
			hql = hql + " and businessType=:businessType ";
			params.put("businessType", businessType);
		}
		
		hql += "order by createTime desc";
		return  this.getListResult(Foa.class, hql, params);
	}

	@Override
	public List<Foa> findAllList() {
		String hql = "from Foa where 1=1 order  by  createTime  desc";
		return this.getListResult(Foa.class, hql);
	}


	public int[] saveBatchFoa(List<Object[]> params) {
		String sql = "insert system_foa(sub_system,business_type,desc_problem,reply,create_time) values (?,?,?,?,now())";
		return this.jdbcTemplate.batchUpdate(sql, params);
	}

	@Override
	public List<Foa> findListByDescProblem(String desc) {
		String hql = "from Foa  where 1=1 ";
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		if (!StringUtils.isEmpty(desc)) {
			hql += " and descProblem like :descProblem ";
			params.put("descProblem", "%" + RegularUtils.specialCodeEscape(desc) + "%");
		}
		return   this.getListResult(Foa.class, hql, params);
	}
	
	
	public Page<Foa> selectFoa(String desc, Integer start, Integer limit) {
		String hql = "from Foa where 1=1 ";
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		if (desc != null) {
			hql += " and descProblem like :descProblem ";
			params.put("descProblem", "%" + RegularUtils.specialCodeEscape(desc) + "%");
		}
		hql += "order by id desc";
		return this.getPageResult(hql, start, limit, params, Foa.class);
	}
	
	public int updateFoa(Long id, Integer subSystem, Integer businessType, String descProblem, String reply) {
		String sql = "update system_foa set desc_problem='" + descProblem + "'";
		sql += ",sub_system='" + subSystem + "'";
		sql += ",business_type=" + businessType + ",reply='" + reply + "' where id=" + id + "";
		return jdbcTemplate.update(sql);
	}
}
