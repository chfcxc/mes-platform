package cn.emay.eucp.data.service.system.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.AuthLevelBindName;
import cn.emay.eucp.data.dao.system.AuthLevelBindNameDao;
import cn.emay.eucp.data.service.system.AuthLevelBindNameService;

@Service("authLevelBindNameService")
public class AuthLevelBindNameServiceImpl implements AuthLevelBindNameService {
	@Resource
	private AuthLevelBindNameDao authLevelBindNameDao;
	
	

	@Override
	public Page<AuthLevelBindName> selectAuthLevelBindName(String name, Integer start, Integer limit) {
		return authLevelBindNameDao.selectAuthLevelBindName(name, start, limit);
	}

	@Override
	public void saveAuthLevelBindName(String authName, int authLevel) {
		AuthLevelBindName  bindNameEntiy=new AuthLevelBindName();
		bindNameEntiy.setAuthLevel(authLevel);
		bindNameEntiy.setAuthName(authName);
		bindNameEntiy.setCreateTime(new Date());
		this.authLevelBindNameDao.save(bindNameEntiy);
	}

	@Override
	public AuthLevelBindName findById(Long id) {
		return this.authLevelBindNameDao.findById(id);
	}

	@Override
	public int updateAuthLevelBindName(Long id, String name) {
		return this.authLevelBindNameDao.updateAuthLevelBindName(id, name);
	}

	
	public  List<AuthLevelBindName> selectAuthLevel(){
		return  this.authLevelBindNameDao.selectAuthLevel();
	}

	@Override
	public Map<String, Object> findBindLevelById(Long id) {
		Map<String,Object>  map=new HashMap<String,Object>();
		AuthLevelBindName info = this.authLevelBindNameDao.findById(id);
		if(null!=info) {
			map.put("authName", info.getAuthName());
			map.put("authLevel", info.getAuthLevel());
		}
		return map;
	}
	
}
