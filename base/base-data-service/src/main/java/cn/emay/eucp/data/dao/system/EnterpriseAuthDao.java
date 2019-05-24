package cn.emay.eucp.data.dao.system;

import java.util.List;

import cn.emay.eucp.common.moudle.db.system.EnterpriseAuth;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface EnterpriseAuthDao extends BaseSuperDao<EnterpriseAuth> {

	List<String> findEnterpriseAuth(Integer authLevel, String authType);

	void saveBatchEnterpriseAuth(int authLevel, List<EnterpriseAuth> enterpriseAuthList);

	void deleteEnterAuthByProperty(String fieldName, Object value);

}
