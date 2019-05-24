package cn.emay.eucp.data.dao.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.emay.eucp.common.moudle.db.system.EnterpriseAuth;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.EnterpriseAuthDao;

@Repository
public class EnterpriseAuthDaoImpl extends PojoDaoImpl<EnterpriseAuth> implements EnterpriseAuthDao {
	
	@Override
	public List<String> findEnterpriseAuth(Integer authLevel,String authType){
		Map<String,Object> params=new HashMap<String,Object>();
		String hql="select authCode from EnterpriseAuth where 1=1";
		if(authLevel!=null && authLevel.intValue()>-1){
			hql+=" and authLevel=:authLevel";
			params.put("authLevel", authLevel);
		}
		if(!StringUtils.isEmpty(authType)){
			hql+=" and authType=:authType";
			params.put("authType", authType);
		}
		return this.getListResult(String.class, hql, params);
	}
	
	@Override
	public void saveBatchEnterpriseAuth(int authLevel,List<EnterpriseAuth> enterpriseAuthList){
		List<Object[]> authList = new ArrayList<Object[]>();
		for (EnterpriseAuth ea : enterpriseAuthList) {
			authList.add(new Object[] { ea.getAuthCode(), ea.getParentAuthCode(), ea.getAuthType(),authLevel,ea.getForService()});
		}
		String sql = "insert into system_enterprise_auth (auth_code,parent_auth_code,auth_type,auth_level,for_service) values(?,?,?,?,?)";
		this.getJdbcTemplate().batchUpdate(sql, authList);
	}
	
	@Override
	public void deleteEnterAuthByProperty(String fieldName, Object value) {
		this.deleteByProperty(fieldName, value);
	}
}
