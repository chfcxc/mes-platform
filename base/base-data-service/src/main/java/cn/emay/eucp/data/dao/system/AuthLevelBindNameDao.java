package cn.emay.eucp.data.dao.system;

import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.AuthLevelBindName;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * 
 * @author gh
 */
public interface AuthLevelBindNameDao extends BaseSuperDao<AuthLevelBindName> {

	
	/**
	 * 获取分页数据信息
	 * @param desc
	 * @param start
	 * @param limit
	 * @return
	 */
	public Page<AuthLevelBindName> selectAuthLevelBindName(String name, Integer start, Integer limit);
	
	public int updateAuthLevelBindName(Long id, String name);
	
	public  List<AuthLevelBindName> selectAuthLevel();
}
