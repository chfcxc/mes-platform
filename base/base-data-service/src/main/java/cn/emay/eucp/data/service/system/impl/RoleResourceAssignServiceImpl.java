package cn.emay.eucp.data.service.system.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.eucp.common.moudle.db.system.RoleResourceAssign;
import cn.emay.eucp.data.dao.system.RoleResourceAssignDao;
import cn.emay.eucp.data.service.system.RoleResourceAssignService;

/**
 * cn.emay.eucp.common.moudle.db.system.RoleResourceAssign Service implement
 * 
 * @author frank
 */
@Service("roleResourceAssignService")
public class RoleResourceAssignServiceImpl implements RoleResourceAssignService {

	@Resource
	private RoleResourceAssignDao roleResourceAssignDao;

	@Override
	public void saveRoleResourceAssign(RoleResourceAssign roleResourceAssign) {
		roleResourceAssignDao.save(roleResourceAssign);
	}

	@Override
	public void saveBatch(Long roleId, String auths) {
		String[] aus = auths.split(",");
		List<RoleResourceAssign> list = new ArrayList<RoleResourceAssign>();
		for (String au : aus) {
			RoleResourceAssign ra = new RoleResourceAssign();
			ra.setRoleId(roleId);
			ra.setResourceId(Long.valueOf(au));
			list.add(ra);
		}
		roleResourceAssignDao.saveBatch(list);
	}

	@Override
	public void deleteBatch(Long roleId) {
		roleResourceAssignDao.deleteBatch(roleId);
	}

	@Override
	public List<RoleResourceAssign> findAllAuthByRoleId(Long roleId) {
		return roleResourceAssignDao.findAllAuthByRoleId(roleId);
	}

}