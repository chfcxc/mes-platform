package cn.emay.eucp.data.service.system;

import java.util.List;
import java.util.Map;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.AuthLevelBindName;


public interface AuthLevelBindNameService {

	public Page<AuthLevelBindName> selectAuthLevelBindName(String desc, Integer start, Integer limit);
	
	public void saveAuthLevelBindName(String name,int authLevel);
	
	public AuthLevelBindName findById(Long id);
	
	public int updateAuthLevelBindName(Long id, String name);
	
	public  List<AuthLevelBindName> selectAuthLevel();
	
	public Map<String, Object>  findBindLevelById(Long id); 
}
