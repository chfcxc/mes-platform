package cn.emay.eucp.data.dao.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.user.RoleDTO;
import cn.emay.eucp.common.dto.user.SimpleUserDTO;
import cn.emay.eucp.common.dto.user.TurnOperatorUserDTO;
import cn.emay.eucp.common.dto.user.UserBindingDTO;
import cn.emay.eucp.common.dto.user.UserDTO;
import cn.emay.eucp.common.dto.user.UserRoleDTO;
import cn.emay.eucp.common.dto.user.WarningUserDTO;
import cn.emay.eucp.common.moudle.db.system.Role;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.UserDao;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.util.DateUtil;

/**
 * cn.emay.eucp.common.moudle.db.system.User Dao implement
 * 
 * @author frank
 */
@Repository
public class UserDaoImpl extends PojoDaoImpl<User> implements UserDao {

	@Override
	public User findUserByUserName(String username) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "from User where state != :deletestate and username=:username";
		param.put("deletestate", User.STATE_DELETE);
		param.put("username", username);
		return this.getUniqueResult(User.class, hql, param);
	}

	@Override
	public List<RoleDTO> findAllUserRoles(Long userId) {
		String hql = "select r from UserRoleAssign ura , Role r where ura.roleId = r.id and ura.userId = :userId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		List<Role> roles = this.getListResult(Role.class, hql, params);
		List<RoleDTO> roledtos = new ArrayList<RoleDTO>();
		for (Role role : roles) {
			roledtos.add(new RoleDTO(role));
		}
		return roledtos;
	}

	@Override
	public Long findenterpriseId(Long id) {
		List<Object> parames = new ArrayList<Object>();
		String sql = " SELECT ed.enterprise_id FROM system_user u INNER JOIN system_user_department_assign ud ON u.id = ud.system_user_id INNER JOIN system_enterprise_department ed ON ud.enterprise_department_id = ed.id where u.id=? ";
		parames.add(id);
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql, parames.toArray());
		if (queryForList.size() > 0) {
			Map<String, Object> map = queryForList.get(0);
			Long l = Long.valueOf(String.valueOf(map.get("enterprise_id")));
			return l;
		}
		return 0l;
	}

	@Override
	public Date findLastUpdateTime(Long userId) {
		String sql = "select last_update_time from system_user where id=?";
		return this.getJdbcTemplate().queryForObject(sql, new Object[] { userId }, Date.class);
	}

	@Override
	public User findUserManage(Long enterpriseId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select u from User u ,UserDepartmentAssign uda ,Department d where u.id = uda.userId and uda.departmentId = d.id and d.enterpriseId = :enterpriseId ";
		params.put("enterpriseId", enterpriseId);
		return this.getUniqueResult(User.class, hql, params);
	}

	@Override
	public UserDTO findUserDTOById(Long userId) {
		String sql = "select u.* , uda.identity from  system_user  u ,system_user_department_assign uda where u.id =uda.system_user_id and u.id = ? ";
		List<Object> list = new ArrayList<Object>();
		list.add(userId);
		return this.findSqlForObj(UserDTO.class, sql, list);
	}

	@Override
	public void udpateUdateState(List<Long> userIds, int state) {
		String sql = "UPDATE  `system_user` SET state =?  WHERE id =?";
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (Long ids : userIds) {
			Object[] params = new Object[] { state, ids };
			batchArgs.add(params);
		}
		jdbcTemplate.batchUpdate(sql, batchArgs);
	}

	@Override
	public Long countByUserName(Long userId, String username) {
		String hql = "select count(*) from User where state != :deletestate and username =:username";
		Map<String, Object> params = new HashMap<String, Object>();
		if (userId != null && userId.longValue() > 0L) {
			hql = hql + " and id !=:id";
			params.put("id", userId);
		}
		params.put("deletestate", User.STATE_DELETE);
		params.put("username", username);
		return (Long) super.getUniqueResult(hql, params);
	}

	@Override
	public Long countByMobile(Long userId, String mobile) {
		String hql = "select count(*) from User where state != :deletestate and mobile=:mobile";
		Map<String, Object> params = new HashMap<String, Object>();
		if ((userId != null) && (userId.longValue() > 0L)) {
			hql = hql + " and id !=:id";
			params.put("id", userId);
		}
		params.put("deletestate", User.STATE_DELETE);
		params.put("mobile", mobile);
		return (Long) super.getUniqueResult(hql, params);
	}

	@Override
	public Long countByEmail(Long userId, String email) {
		String hql = "select count(*) from User where state != :deletestate and email =:email";
		Map<String, Object> params = new HashMap<String, Object>();
		if ((userId != null) && (userId.longValue() > 0L)) {
			hql = hql + " and id !=:id";
			params.put("id", userId);
		}
		params.put("deletestate", User.STATE_DELETE);
		params.put("email", email);
		return (Long) super.getUniqueResult(hql, params);
	}

	@Override
	public Page<UserDTO> findUserPageByManager(String username, String realname, String mobile, int start, int limit) {
		String hql = "select u,d.departmentName,ude.identity from User u , UserDepartmentAssign ude , Department d where u.state != :deletestate ";
		hql += " and u.id = ude.userId and d.id = ude.departmentId ";
		Map<String, Object> param = new HashMap<String, Object>();
		if (username != null && username.trim().length() > 0) {
			hql += " and u.username = :username ";
			param.put("username", username.toLowerCase());
		}
		if (realname != null && realname.trim().length() > 0) {
			hql += " and u.realname = :realname ";
			param.put("realname", realname.toLowerCase());
		}
		if (mobile != null && mobile.trim().length() > 0) {
			hql += " and u.mobile = :mobile ";
			param.put("mobile", mobile);
		}
		hql += " and u.userType = :userType order by u.id desc ";
		param.put("userType", User.USER_TYPE_EMAY);
		param.put("deletestate", User.STATE_DELETE);
		Page<Object> page = this.getPageResult(hql, start, limit, param, Object.class);
		Page<UserDTO> pd = new Page<UserDTO>();
		pd.setNumByStartAndLimit(start, limit, page.getTotalCount());
		List<UserDTO> list = new ArrayList<UserDTO>();
		List<Long> ids = new ArrayList<Long>();
		for (Object o : page.getList()) {
			Object oo[] = (Object[]) o;
			User u = (User) oo[0];
			String depment = (String) oo[1];
			Integer identity = null == oo[2] ? 2 : Integer.valueOf(oo[2].toString());
			UserDTO dto = new UserDTO(u.getId(), u.getUsername(), u.getRealname(), u.getMobile(), u.getEmail(), u.getCreateTime(), u.getState(), null, depment, u.getUserType(), identity);
			list.add(dto);
			ids.add(u.getId());
		}
		if (ids.size() > 0) {
			String hlq1 = "select r.roleName , ur.userId from Role r , UserRoleAssign ur where r.id = ur.roleId and ur.userId in :userId ";
			Map<String, Object> param1 = new HashMap<String, Object>();
			param1.put("userId", ids);
			List<?> list1 = this.getListResult(hlq1, param1);
			Map<Long, String> rolenames = new HashMap<Long, String>();
			for (Object o : list1) {
				Object oo[] = (Object[]) o;
				String roleName = (String) oo[0];
				Long userId = (Long) oo[1];
				if (rolenames.containsKey(userId)) {
					rolenames.put(userId.longValue(), rolenames.get(userId) + "," + roleName);
				} else {
					rolenames.put(userId.longValue(), roleName);
				}
			}
			for (UserDTO o : list) {
				o.setRolename(rolenames.get(o.getId()));
			}
		}
		pd.setList(list);
		return pd;
	}

	/*-----------------------------------*/

	@Override
	public Page<User> findBycondition(String variableName, Long departmentId, int start, int limit) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "select u from User u,UserDepartmentAssign ud where u.id=ud.userId and u.state != :state and ud.departmentId=:departmentId";
		param.put("departmentId", departmentId);
		if (!StringUtils.isEmpty(variableName)) {
			hql += " and (u.username like :variableName or u.realname like :variableName or u.mobile like :variableName)";
			param.put("variableName", "%" + RegularUtils.specialCodeEscape(variableName) + "%");
		}
		param.put("state", User.STATE_DELETE);
		hql += " order by u.createTime desc ";
		Page<User> page = this.getPageResult(hql, start, limit, param, User.class);
		return page;
	}

	@Override
	public List<User> countByUserNames(List<String> list, String userType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select u from User u where  u.state != :deletestate and u.username in (:usernames) ";
		if (!StringUtils.isEmpty(userType)) {
			hql += " and u.userType=:userType";
			params.put("userType", userType);
		}
		hql += " GROUP BY u.username";
		params.put("deletestate", User.STATE_DELETE);
		params.put("usernames", list);
		return this.getListResult(User.class, hql, params);
	}

	@Override
	public List<User> countByRealNames(List<String> list, String userType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select u from User u where  u.state != :deletestate and u.realname in (:realname) ";
		if (!StringUtils.isEmpty(userType)) {
			hql += " and u.userType=:userType";
			params.put("userType", userType);
		}
		hql += " GROUP BY u.realname";
		params.put("deletestate", User.STATE_DELETE);
		params.put("realname", list);
		return this.getListResult(User.class, hql, params);
	}

	@Override
	public List<User> countByUserIds(List<Long> list, String roleType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select u from User u where  u.state != :deletestate and u.id in (:id) ";
		if (!StringUtils.isEmpty(roleType)) {
			hql += " and u.userType=:userType";
			params.put("userType", roleType);
		}
		params.put("deletestate", User.STATE_DELETE);
		params.put("id", list);
		return this.getListResult(User.class, hql, params);
	}

	@Override
	public List<?> findUserSByIds(Set<Long> idsSet) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select id, username, realname, mobile, email from  User where state = :state and  id in :ids";
		params.put("state", User.STATE_ON);
		if (idsSet.size() == 0) {
			return new ArrayList<Object[]>();
		}
		params.put("ids", idsSet);
		return this.getListResult(hql, params);
	}

	@Override
	public List<?> findEnterpriseUser(Long enterpriseId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select u.id,u.username,u.state,uda.identity from system_enterprise_department d,system_user_department_assign uda,system_user u"
				+ " where d.id=uda.enterprise_department_id and uda.system_user_id=u.id and d.enterprise_id=:enterpriseId and u.state!=:state";
		params.put("enterpriseId", enterpriseId);
		params.put("state", User.STATE_DELETE);
		return this.getPageListResultBySqlByHibernate(sql, 0, 0, params);
	}

	@Override
	public Object findMangerAccount(Long enterpriseId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select uda.userId as userId,d.id as departmentId from Department d,UserDepartmentAssign uda where d.id=uda.departmentId and d.enterpriseId=:enterpriseId and uda.identity=:identity";
		params.put("enterpriseId", enterpriseId);
		params.put("identity", UserDepartmentAssign.IDENTITY_LEADER);
		return this.getUniqueResult(hql, params);
	}

	@Override
	public void updateUser(Long id, String linkman, String mobile, Long operatorUserId, String email) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "update User set realname=:linkman,mobile=:mobile,operatorId=:operatorUserId,email=:email where id = :id ";
		params.put("id", id);
		params.put("linkman", linkman);
		params.put("mobile", mobile);
		params.put("operatorUserId", operatorUserId);
		params.put("email", email);
		this.execByHql(hql, params);
	}

	@Override
	public Page<UserDTO> findall(int start, int limit, String username, String realname, String mobile, Long enterpriseId, Long userId) {
		String sql = " SELECT u.*,ud.identity FROM system_user u INNER JOIN system_user_department_assign ud ON u.id = ud.system_user_id INNER JOIN system_enterprise_department ed ON ed.id = ud.enterprise_department_id where 1=1 ";
		List<Object> list = new ArrayList<Object>();
		if (!StringUtils.isEmpty(username)) {
			if (username.contains("%")) {
				username = username.replaceAll("%", "/%");
			}
			sql += " and u.username like ? ";
			list.add("%" + username + "%");
		}
		if (!StringUtils.isEmpty(userId)) {
			sql += " and u.id=? ";
			list.add(userId);
		}
		if (!StringUtils.isEmpty(realname)) {
			if (realname.contains("%")) {
				realname = realname.replaceAll("%", "/%");
			}
			sql += " and u.realname like ? ";
			list.add("%" + realname + "%");
		}
		if (!StringUtils.isEmpty(mobile)) {
			sql += " and u.mobile = ? ";
			list.add(mobile);
		}
		sql += " and ed.enterprise_id =? ";
		list.add(enterpriseId);
		sql += " and u.state != 0  order by u.id desc ";
		return findSqlForPageForMysql(UserDTO.class, sql, list, start, limit);
	}

	@Override
	public void updatestate(Long id, int state) {
		String hql = " update User set state =:state where id = :id ";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("state", state);
		param.put("id", id);
		this.execByHql(hql, param);
	}

	@Override
	public Boolean findmobile(Long id, String mobile) {
		String hql = "from User where mobile=:mobile and state !=0 ";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", mobile);
		if (id != null && id != 0l) {
			hql += " and id!=:id";
			map.put("id", id);
		}
		List<User> list = this.getListResult(User.class, hql, map);
		if (null != list && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean findemail(Long id, String email) {
		String hql = "from User where email =:email and state !=0";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", email);
		if (null != id && id != 0l) {
			hql += " and id !=:id";
			map.put("id", id);
		}
		List<User> list = this.getListResult(User.class, hql, map);
		if (null != list && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public User findBymobile(String mobile) {
		String hql = " from User where mobile =:mobile ";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", mobile);

		return (User) this.getUniqueResult(hql, map);
	}

	@Override
	public void updatepassword(String password, Long id, Date lastChangePasswordTime) {
		String hql = " update User set password =:password,lastChangePasswordTime=:lastChangePasswordTime where id=:id ";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("password", password);
		param.put("lastChangePasswordTime", null);
		param.put("id", id);
		this.execByHql(hql, param);

	}

	@Override
	public List<UserRoleDTO> finduserAndRole(Long id) {
		String sql = "select u.id,u.email,u.username,u.realname,u.mobile,r.role_name,r.id as roleid,r.remark from system_user u INNER JOIN system_user_role_assign sur on u.id=sur.system_user_id INNER JOIN system_role r on sur.system_role_id=r.id where r.role_type='CLIENT' and r.is_delete='0' and u.id=? ";
		List<Object> arrayList = new ArrayList<Object>();
		arrayList.add(id);
		return findSqlForListObj(UserRoleDTO.class, sql, arrayList);
	}

	@Override
	public List<UserDTO> findEnterpriseChild(Long enterpriseId) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select u.id,u.username,u.realname,u.mobile from system_enterprise_department d,system_user_department_assign uda,system_user u"
				+ " where d.id=uda.enterprise_department_id and uda.system_user_id=u.id and d.enterprise_id=? and u.state!=? and uda.identity=? ";
		params.add(enterpriseId);
		params.add(User.STATE_DELETE);
		params.add(UserDepartmentAssign.IDENTITY_EMPLOYEE);
		return this.findSqlForListObj(UserDTO.class, sql, params);
	}

	@Override
	public List<TurnOperatorUserDTO> findAllTurnOperatorUser(int currentPage, int pageSize, Date date, Boolean isDelete) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select u.id,u.username,u.realname,u.mobile,u.email,u.state from system_user u where 1 =1 ";
		if (null != date) {
			sql += " and  last_update_time >= ? ";
			params.add(date);
		}
		if (null != isDelete) {
			sql += " and  is_delete = ? ";
			params.add(isDelete);
		}
		return this.findSqlForListForMysql(TurnOperatorUserDTO.class, sql, params, currentPage, pageSize);
	}

	@Override
	public List<String> findEnterpriseUsers(Long currentUserId) {
		List<Object> parameters = new ArrayList<Object>();
		String sql = "select u.username from system_user u,	system_user_department_assign uda,	system_enterprise_department ed, system_enterprise e"
				+ " where 	u.id = uda.system_user_id and uda.enterprise_department_id = ed.id and ed.enterprise_id = e.id and e.id = " + "	(select e.id from "
				+ " system_user u,	system_user_department_assign uda,	system_enterprise_department ed, system_enterprise e"
				+ " where u.id = uda.system_user_id and uda.enterprise_department_id = ed.id	and ed.enterprise_id = e.id and u.id = ? )";
		parameters.add(currentUserId);
		return this.findSqlForSingleColumn(sql, parameters);
		//return this.findSqlForListObj(User.class, sql, parameters);
	}

	@Override
	public Boolean finduserName(Long id, String username) {
		String hql = "from User where username =:username and state !=0";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		if (null != id && id != 0l) {
			hql += " and id !=:id";
			map.put("id", id);
		}
		List<User> list = this.getListResult(User.class, hql, map);
		if (null != list && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public long findCilentCount(Long id) {

		String hql = "select count(u.id) from User u, UserDepartmentAssign uda ,Department d " + "where u.id = uda.userId and uda.departmentId = d.id and u.state != :state and d.enterpriseId = :id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("state", User.STATE_DELETE);
		params.put("id", id);
		return this.getUniqueResult(long.class, hql, params);
	}

	@Override
	public Page<UserDTO> findUserPage(String userName, String realName, String mobile, Integer accountType, Integer state, String userType, String clientName, String clientNumber, int start, int limit) {
		String sql = "select u.id,u.username,u.realname,u.mobile,u.email,u.create_time,u.state, group_concat(r.role_name) as rolename ,d.department_name,ud.identity,e.name_cn as clientName ,e.client_number "
				+ "from system_user u ,system_user_role_assign ur ,system_role r,system_enterprise_department d,system_user_department_assign ud,system_enterprise e "
				+ "where u.id = ur.system_user_id and ur.system_role_id = r.id " + "and u.id=ud.system_user_id and d.id=ud.enterprise_department_id and d.enterprise_id=e.id " + "and u.state !=0";
		List<Object> list = new ArrayList<Object>();
		if (!StringUtils.isEmpty(userName)) {
			sql += " and u.username like ? ";
			list.add("%" + RegularUtils.specialCodeEscape(userName) + "%");
		}
		if (!StringUtils.isEmpty(realName)) {
			sql += " and u.realname like ? ";
			list.add("%" + RegularUtils.specialCodeEscape(realName) + "%");
		}
		if (!StringUtils.isEmpty(mobile)) {
			sql += " and u.mobile like ? ";
			list.add("%" + RegularUtils.specialCodeEscape(mobile) + "%");
		}
		if (accountType > 0) {// 账号类型:0全部,1-领导,2-职员
			sql += " and ud.identity= ? ";
			list.add(accountType);
		}
		if (state > 0) {// 状态:0全部,1-停用，2-启用,3-锁定
			sql += " and u.state= ? ";
			list.add(state);
		}
		if (!StringUtils.isEmpty(clientName)) {
			sql += " and e.name_cn like ? ";
			list.add("%" + RegularUtils.specialCodeEscape(clientName) + "%");
		}

		if (!StringUtils.isEmpty(clientNumber)) {
			sql += " and e.client_number = ? ";
			list.add(RegularUtils.specialCodeEscape(clientNumber));
		}

		sql += " and u.user_type= ? ";
		list.add(userType);
		sql += " group by u.id ";
		sql += " order by u.create_time desc";
		return this.findSqlForPageForMysql(UserDTO.class, sql, list, start, limit);
	}

	@Override
	public List<WarningUserDTO> findUserSByIdsIgnoreState(Set<Long> idsSet) {
		if (idsSet.size() == 0) {
			return new ArrayList<WarningUserDTO>();
		}
		String sql = "SELECT id, username, realname, mobile, email, state FROM `system_user` WHERE id in (" + org.apache.commons.lang3.StringUtils.join(idsSet.toArray(), ",") + ") ";
		return this.findSqlForListObj(WarningUserDTO.class, sql, null);
	}

	@Override
	public UserDTO findClientUserById(Long id) {
		/*
		 * String sql= " SELECT u.id,u.email,u.create_time,u.state,group_concat(r.role_name) AS rolename FROM system_user u,system_user_role_assign ur,system_role r " +
		 * " WHERE u.id = ur.system_user_id AND ur.system_role_id = r.id AND u.state != 0 and u.id=? and r.role_type=? group by u.id" ;
		 */
		String sql = " SELECT u.id,u.username,u.realname,u.mobile,u.email,u.create_time,u.state,group_concat(r.role_name) AS rolename,d.department_name,ud.identity,e.name_cn AS clientName,e.client_number "
				+ " FROM system_user u,system_user_role_assign ur,system_role r,system_enterprise_department d,system_user_department_assign ud,system_enterprise e "
				+ " WHERE u.id = ur.system_user_id AND ur.system_role_id = r.id AND u.id = ud.system_user_id AND d.id = ud.enterprise_department_id AND d.enterprise_id = e.id "
				+ " AND u.state != 0 AND u.id =? and u.user_type=? group by u.id ";

		List<Object> list = new ArrayList<Object>();

		list.add(id);
		list.add(User.USER_TYPE_CLIENT);
		List<UserDTO> dtoList = this.findSqlForListObj(UserDTO.class, sql, list);
		if (null == dtoList || dtoList.size() == 0) {
			return null;
		}
		return dtoList.get(0);
	}

	@Override
	public List<String> findUserEndWithStr(String endStr) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select username from User where state != :state and username like :endStr";
		params.put("state", User.STATE_DELETE);
		params.put("endStr", "%" + endStr);
		return this.getListResult(String.class, hql, params);
	}

	@Override
	public boolean updateLastUpdateTimeById(String id) {
		boolean ret = false;
		String sql = "update system_user a set last_update_time = NOW() where a.id=" + id;
		int retNum = jdbcTemplate.update(sql);
		if (retNum >= 0) {
			ret = true;
		}
		return ret;
	}

	public List<User> findUserByRoleType(String roleType, String userType, Date date, String username) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select distinct u.* from system_user u,system_user_role_assign ur,system_role r where u.id=ur.system_user_id and ur.system_role_id=r.id "
				+ "and u.user_type=? and r.role_type=? and u.state!=? and u.state!=?");
		params.add(userType);
		params.add(roleType);
		params.add(User.STATE_DELETE);
		params.add(User.STATE_OFF);
		if (null != date) {
			sql.append(" and u.last_update_time>?");
			params.add(date);
		}
		if (!org.apache.commons.lang3.StringUtils.isEmpty(username)) {
			sql.append(" and u.username like ?");
			params.add("%" + RegularUtils.specialCodeEscape(username) + "%");
		}
		return this.findSqlForListObj(User.class, sql.toString(), params);
	}

	@Override
	public Page<UserDTO> findUserPageByRoleType(String roleType, String userType, String username, int start, int limit) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select distinct u.*,d.department_name as department from system_user u,system_user_role_assign ur,system_role r,system_enterprise_department d,"
				+ " system_user_department_assign ud where u.id=ur.system_user_id and ur.system_role_id=r.id and u.id=ud.system_user_id and d.id=ud.enterprise_department_id"
				+ " and u.user_type=? and r.role_type=? and u.state!=? and u.state!=?");
		params.add(userType);
		params.add(roleType);
		params.add(User.STATE_DELETE);
		params.add(User.STATE_OFF);
		if (!org.apache.commons.lang3.StringUtils.isEmpty(username)) {
			sql.append(" and (u.realname like ? or u.username like ?)");
			params.add("%" + RegularUtils.specialCodeEscape(username) + "%");
			params.add("%" + RegularUtils.specialCodeEscape(username) + "%");
		}
		sql.append(" order by u.create_time desc");
		return this.findSqlForPageForMysql(UserDTO.class, sql.toString(), params, start, limit);
	}

	@Override
	public List<Map<String, Object>> findUserIdByUsernameOrRealname(String UsernameOrRealname) {
		String sql = "SELECT * FROM SYSTEM_USER a WHERE a.username LIKE '%" + UsernameOrRealname + "%' OR a.realname LIKE '%" + UsernameOrRealname + "%'";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<User> findUserByUserType(String userType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from User u where u.state!=:state";
		params.put("state", User.STATE_DELETE);
		if (!StringUtils.isEmpty(userType)) {
			hql += " and u.userType=:userType";
			params.put("userType", userType);
		}
		return this.getListResult(User.class, hql, params);
	}

	@Override
	public List<User> findUserByLastUpdateTime(Date lastUpdateTime, String userType, int currentPage, int pageSize) {
		String sql = "select * from system_user where 1=1 ";
		List<Object> parameters = new ArrayList<Object>();
		if (null != lastUpdateTime) {
			sql += "and last_update_time>= ? ";
			parameters.add(DateUtil.toString(lastUpdateTime, "yyyy-MM-dd HH:mm:ss"));
		}
		if (!StringUtils.isEmpty(userType)) {
			sql += " and user_type= ? ";
			parameters.add(userType);
		}
		return this.findSqlForListForMysql(User.class, sql, parameters, currentPage, pageSize);
	}

	@Override
	public List<User> findByUserName(String userName) {
		if (RegularUtils.isEmpty(userName)) {
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from User u where u.username like :userName";
		params.put("userName", "%" + RegularUtils.specialCodeEscape(userName) + "%");
		return this.getListResult(User.class, hql, params);
	}

	@Override
	public List<User> findUserByIds(Set<Long> idsSet) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from  User where  id in :ids";
		if (idsSet.size() == 0) {
			return new ArrayList<User>();
		}
		params.put("ids", idsSet);
		return this.getListResult(User.class, hql, params);
	}

	@Override
	public Page<Map<String, Object>> findServiceCodeBindingPageClient(int start, int limit, String userName, Long userId, Long enterpriseId) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select u.username as userName, uda.identity as identity,u.id as userId ,u.state as state from system_enterprise e, system_enterprise_department d, system_user_department_assign uda, system_user u "
				+ " where e.id = d.enterprise_id and d.id = uda.enterprise_department_id and uda.system_user_id = u.id and u.state!= false and e.id = ? ";
		params.add(enterpriseId);
		if (!StringUtils.isEmpty(userName)) {
			sql += " and u.username = ? ";
			params.add(userName);
		}
		if (!StringUtils.isEmpty(userId)) {
			sql += " and u.id=?";
			params.add(userId);
		}
		sql += " order by u.create_time desc";
		return this.findSqlForPageMapForMysql(sql, params, start, limit);
	}

	// @Override
	// public List<Long> findUserBindServiceCodeClient(Long userId) {
	// List<Object> list = new ArrayList<Object>();
	// String sql = " select usca.service_code_id from system_user u, sms_service_code s,	sms_user_service_code_assign usca ";
	// sql += " where u.id=usca.system_user_id ";
	// sql += " and u.id = ?";
	// list.add(userId);
	// return jdbcTemplate.queryForList(sql, Long.class, list.toArray());
	// }

	@Override
	public List<Long> findEnterpriseBindServiceCode(Long userId) {
		List<Object> list = new ArrayList<Object>();
		String sql = "select ed.enterprise_id from system_user_department_assign uda,system_enterprise_department ed " + " where uda.enterprise_department_id = ed.id and uda.system_user_id = ?";
		list.add(userId);
		return jdbcTemplate.queryForList(sql, Long.class, list.toArray());
	}

	@Override
	public Page<UserBindingDTO> findUserInfoPage(int start, int limit, String userName, List<Long> userIdList) {
		List<Object> parameters = new ArrayList<Object>();
		String sql = "select e.name_cn,e.client_number,uda.identity,u.id as userId,u.username,u.state,e.type from system_enterprise e,system_enterprise_department d,system_user_department_assign uda,system_user u "
				+ "where e.id=d.enterprise_id and d.id=uda.enterprise_department_id and uda.system_user_id=u.id and u.state!=? ";
		parameters.add(User.STATE_DELETE);
		if (!StringUtils.isEmpty(userName)) {
			sql += " and u.username=?";
			parameters.add(userName);
		}
		if (userIdList != null && userIdList.size() > 0) {
			sql += " and u.id in(" + org.apache.commons.lang3.StringUtils.join(userIdList, ",") + ")";
		}
		sql += " order by u.create_time desc";
		return this.findSqlForPageForMysql(UserBindingDTO.class, sql, parameters, start, limit);
	}

	@Override
	public List<SimpleUserDTO> findByUserType(String userType) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select u.id,u.username,u.realname from system_user u where u.state!=?";
		params.add(User.STATE_DELETE);
		sql += " and u.user_type=?";
		params.add(User.USER_TYPE_EMAY);
		if (!StringUtils.isEmpty(userType)) {
			sql += " and u.realname like ?";
			params.add( "%"+RegularUtils.specialCodeEscape(userType) + "%");
		}
		sql += " order by u.username asc";
		return this.findSqlForListObj(SimpleUserDTO.class, sql, params);
	}
	
	@Override
	public List<User> findByRealNames(List<String> list, String userType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select u from User u where  u.state != :deletestate and u.realname in (:realname) ";
		if (!StringUtils.isEmpty(userType)) {
			hql += " and u.userType=:userType";
			params.put("userType", userType);
		}
		params.put("deletestate", User.STATE_DELETE);
		params.put("realname", list);
		return this.getListResult(User.class, hql, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> findUserIdbyEnterpriseId(Long enterpriseId) {
		String sql=" select u.id from system_user u, system_user_department_assign uda,system_enterprise_department ed " + 
				"where u.id=uda.system_user_id AND uda.enterprise_department_id=ed.id AND ed.enterprise_id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(enterpriseId);
		return (List<Long>) this.getListResultBySql(sql, params.toArray());
	}
}
