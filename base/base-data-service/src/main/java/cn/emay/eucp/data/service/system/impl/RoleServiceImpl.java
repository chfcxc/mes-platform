package cn.emay.eucp.data.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Role;
import cn.emay.eucp.common.moudle.db.system.RoleResourceAssign;
import cn.emay.eucp.data.dao.system.ResourceDao;
import cn.emay.eucp.data.dao.system.RoleDao;
import cn.emay.eucp.data.dao.system.RoleResourceAssignDao;
import cn.emay.eucp.data.dao.system.UserRoleAssignDao;
import cn.emay.eucp.data.service.system.RoleService;

/**
 * cn.emay.eucp.common.moudle.db.system.Role Service implement
 * 
 * @author frank
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Resource
	private RoleDao roleDao;
	@Resource
	private UserRoleAssignDao userRoleAssignDao;
	@Resource
	private RoleResourceAssignDao roleResourceAssignDao;
	@Resource
	private ResourceDao resourceDao;

	@Override
	public List<Role> findAll(String roleType) {
		return roleDao.findAll(roleType);
	}

	@Override
	public Page<Role> findRolePage(int start, int limit, String roleName, String roleType) {
		return roleDao.findRolePage(start, limit, roleName, roleType);
	}

	@Override
	public Result deleteRoleById(Long roleId) {
		Role role = roleDao.findRoleById(roleId);
		if (role == null) {
			return Result.badResult("角色不存在");
		}
		long count = userRoleAssignDao.getNotDeleteUserCountByRole(roleId);
		if (count > 0) {
			return Result.badResult("角色正在被使用，不能删除");
		}
		role.setIsDelete(true);
		roleDao.update(role);
		roleResourceAssignDao.deleteBatch(roleId);
		return Result.rightResult(role.getRoleName());
	}

	@Override
	public Long countNumberByRoleName(String roleName, Long id) {
		return roleDao.countNumberByRoleName(roleName, id);
	}

	@Override
	public Result addRole(String roleName, String auths, String remark, String roleType) {
		// 根据角色类型查询与之相对应的资源
		List<cn.emay.eucp.common.moudle.db.system.Resource> resourceList = resourceDao.findAuthByType(null, roleType);
		Map<Long, cn.emay.eucp.common.moudle.db.system.Resource> map = new HashMap<Long, cn.emay.eucp.common.moudle.db.system.Resource>();
		for (cn.emay.eucp.common.moudle.db.system.Resource re : resourceList) {
			map.put(re.getId(), re);
		}
		String[] aus = auths.split(",");
		for (String au : aus) {
			if (!map.containsKey(Long.valueOf(au))) {
				return Result.badResult("权限错误");
			}
		}
		Role role = new Role();
		role.setCreateTime(new Date());
		role.setIsDelete(false);
		role.setRoleName(roleName);
		role.setRoleType(roleType);
		role.setRemark(remark);
		roleDao.save(role);
		List<RoleResourceAssign> list = new ArrayList<RoleResourceAssign>();
		for (String au : aus) {
			RoleResourceAssign ra = new RoleResourceAssign();
			ra.setRoleId(role.getId());
			ra.setResourceId(Long.valueOf(au));
			list.add(ra);
		}
		roleResourceAssignDao.saveBatch(list);
		return Result.rightResult();
	}

	@Override
	public Result modifyRole(Long roleId, String roleName, String auths, String remark) {
		Role role = this.findById(roleId);
		if (role == null || role.getIsDelete() == true) {
			return Result.badResult("角色不存在");
		}
		if (auths.trim().length() <= 0) {
			return Result.badResult("权限错误");
		}
		// 根据角色类型查询与之相对应的资源
		List<cn.emay.eucp.common.moudle.db.system.Resource> resourceList = resourceDao.findAuthByType(null, role.getRoleType());
		Map<Long, cn.emay.eucp.common.moudle.db.system.Resource> map = new HashMap<Long, cn.emay.eucp.common.moudle.db.system.Resource>();
		for (cn.emay.eucp.common.moudle.db.system.Resource re : resourceList) {
			map.put(re.getId(), re);
		}
		String[] aus = auths.split(",");
		for (String au : aus) {
			if (!map.containsKey(Long.valueOf(au))) {
				return Result.badResult("权限错误");
			}
		}
		roleResourceAssignDao.deleteBatch(roleId);
		if (roleName != null) {
			role.setRoleName(roleName);
		}
		role.setRemark(remark);
		roleDao.update(role);
		List<RoleResourceAssign> list = new ArrayList<RoleResourceAssign>();
		for (String au : aus) {
			RoleResourceAssign ra = new RoleResourceAssign();
			ra.setRoleId(roleId);
			ra.setResourceId(Long.valueOf(au));
			list.add(ra);
		}
		roleResourceAssignDao.saveBatch(list);
		return Result.rightResult();
	}

	@Override
	public Role findById(Long roleId) {
		return roleDao.findById(roleId);
	}

	@Override
	public List<Role> findAllname() {
		List<Role> list = roleDao.findAllname();
		return list;
	}

	@Override
	public List<Role> findByIds(String ids) {
		String[] split = ids.split(",");
		List<Long> list = new ArrayList<Long>();
		for (String roleid : split) {
			list.add(Long.valueOf(roleid));
		}
		List<Role> ids2 = roleDao.findbyIds(list);
		return ids2;
	}

	@Override
	public Role findRoleById(Long id) {
		return roleDao.findRoleById(id);
	}

	@Override
	public List<Role> findRoleByIds(List<Long> roleIdList, String code) {
		return roleDao.findRoleByIds(roleIdList, code);
	}

}