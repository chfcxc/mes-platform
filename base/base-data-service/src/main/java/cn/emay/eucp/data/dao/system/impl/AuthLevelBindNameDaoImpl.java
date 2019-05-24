package cn.emay.eucp.data.dao.system.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.dubbo.common.utils.StringUtils;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.AuthLevelBindName;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.AuthLevelBindNameDao;
import cn.emay.eucp.util.RegularUtils;

/**
 * 
 * @author gh
 */
@Repository
public class AuthLevelBindNameDaoImpl extends PojoDaoImpl<AuthLevelBindName> implements AuthLevelBindNameDao {

	@Override
	public Page<AuthLevelBindName> selectAuthLevelBindName(String name, Integer start, Integer limit) {
		String hql = "from AuthLevelBindName  where 1=1";
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		if (!StringUtils.isEmpty(name)) {
			hql += " and authName like :name";
			params.put("name", "%" + RegularUtils.specialCodeEscape(name) + "%");
		}
		
		hql += " order by createTime desc";
		return  this.getPageResult(hql, start, limit, params, AuthLevelBindName.class);
	}

	@Override
	public int updateAuthLevelBindName(Long id, String name) {
			String sql = "update system_name_binding_level set auth_name='" + name + "'";
			sql += " where id=" + id + "";
			return jdbcTemplate.update(sql);
	}
	
	
	public  List<AuthLevelBindName> selectAuthLevel(){
		String hql = "from AuthLevelBindName where 1=1 order  by  authLevel  desc";
		return this.getListResult(AuthLevelBindName.class, hql);
	}
	
}
