package cn.emay.eucp.data.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.emay.common.Result;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.db.Page;
import cn.emay.common.encryption.Md5;
import cn.emay.eucp.common.cache.CenterBaseRedisCache;
import cn.emay.eucp.common.constant.EucpSystem;
import cn.emay.eucp.common.dto.servicemodule.ServiceModuleDto;
import cn.emay.eucp.common.dto.user.ClientUserIndexDTO;
import cn.emay.eucp.common.dto.user.RoleDTO;
import cn.emay.eucp.common.dto.user.SimpleUserDTO;
import cn.emay.eucp.common.dto.user.TurnOperatorUserDTO;
import cn.emay.eucp.common.dto.user.UserBindingDTO;
import cn.emay.eucp.common.dto.user.UserDTO;
import cn.emay.eucp.common.dto.user.UserRoleDTO;
import cn.emay.eucp.common.dto.user.WarningUserDTO;
import cn.emay.eucp.common.moudle.db.system.Business;
import cn.emay.eucp.common.moudle.db.system.Department;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.Role;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign;
import cn.emay.eucp.common.moudle.db.system.UserRoleAssign;
import cn.emay.eucp.data.dao.system.BusinessDao;
import cn.emay.eucp.data.dao.system.DepartmentDao;
import cn.emay.eucp.data.dao.system.EnterpriseBindingSaleDao;
import cn.emay.eucp.data.dao.system.EnterpriseDao;
import cn.emay.eucp.data.dao.system.ResourceDao;
import cn.emay.eucp.data.dao.system.RoleDao;
import cn.emay.eucp.data.dao.system.UserDao;
import cn.emay.eucp.data.dao.system.UserDepartmentAssignDao;
import cn.emay.eucp.data.dao.system.UserRoleAssignDao;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.util.PasswordUtils;
import cn.emay.eucp.util.RandomNumberUtils;
import cn.emay.eucp.util.RegularUtils;

/**
 * cn.emay.eucp.common.moudle.db.system.User Service implement
 * 
 * @author frank
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;
	@Resource
	private UserRoleAssignDao userRoleAssignDao;
	@Resource
	private UserDepartmentAssignDao userDepartmentAssignDao;
	@Resource
	private DepartmentDao departmentDao;
	@Resource
	private RoleDao roleDao;
	@Resource
	private EnterpriseDao enterpriseDao;
	@Resource
	ResourceDao resourceDao;
	@Resource
	private RedisClient redis;
	@Resource
	EnterpriseBindingSaleDao enterpriseBindingSaleDao;
	@Resource
	BusinessDao businessDao;

	@Override
	public User findUserByUserName(String username) {
		if (!RegularUtils.isEmpty(username)) {
			username = username.toLowerCase();
		}
		return userDao.findUserByUserName(username);
	}

	@Override
	public List<RoleDTO> findAllUserRoles(Long userId) {
		return userDao.findAllUserRoles(userId);
	}

	@Override
	public Date findLastUpdateTime(Long id) {
		return userDao.findLastUpdateTime(id);
	}

	@Override
	public Result modifyOn(Long userId, String operUserType) {
		User user = this.findUserById(userId);
		if (user == null) {
			return Result.badResult("用户不存在");
		}
		if (user.getState().intValue() == User.STATE_DELETE) {
			return Result.badResult("已删除用户不能再次启用");
		}
		if (!user.getUserType().equals(operUserType)) {
			return Result.badResult("用户不存在");
		}
		if (User.USER_TYPE_CLIENT.equals(operUserType)) {
			Long enterpriseId = userDao.findenterpriseId(userId);
			if (null == enterpriseId || enterpriseId.longValue() == 0l) {
				return Result.badResult("参数错误");
			}
			// 查找企业管理员账户
			User userManage = userDao.findUserManage(enterpriseId);
			// 如果当前用户不是管理账户，且管理账户被停用
			if (userManage.getId().longValue() != user.getId().longValue() && userManage.getState().intValue() != User.STATE_ON) {
				return Result.badResult("主账号已被停用，无法进行操作");
			}
		}
		user.setState(User.STATE_ON);
		userDao.update(user);
		return Result.rightResult(user.getUsername());
	}

	@Override
	public User findUserById(Long userId) {
		return userDao.findById(userId);
	}

	@Override
	public Result deleteUserByUserId(Long userId, String operUserType) {
		User user = userDao.findById(userId);
		if (user == null) {
			return Result.badResult("用户不存在");
		}
		if (!user.getUserType().equals(operUserType)) {
			return Result.badResult("用户不存在");
		}
		if (user.getState() == User.STATE_DELETE) {
			return Result.badResult("用户已被删除,请勿重复操作");
		}
		if (User.USER_TYPE_CLIENT.equals(operUserType)) {
			Long enterpriseId = userDao.findenterpriseId(userId);
			if (null == enterpriseId || enterpriseId.longValue() == 0l) {
				return Result.badResult("参数错误");
			}
			// 查找企业管理员账户
			User userManage = userDao.findUserManage(enterpriseId);
			// 如果当前用户管理账户，不能被删除
			if (userManage.getId().longValue() == user.getId().longValue()) {
				return Result.badResult("客户管理账号不能被删除");
			}
		}
		user.setState(User.STATE_DELETE);
		userDao.update(user);
		userRoleAssignDao.deleteByUserId(userId);
		userDepartmentAssignDao.deleteDataByUserId(userId);
		List<Long> ids = enterpriseBindingSaleDao.getIdsBySaleId(userId.toString());
		if (null != ids && ids.size() > 0) {
			enterpriseBindingSaleDao.deleteByfieldName("systemUserId", userId);
			enterpriseBindingSaleDao.deleteSystemEnterpriseBindingSaleTop(org.apache.commons.lang3.StringUtils.join(ids.toArray(), ","));
		}
		return Result.rightResult(user.getUsername());
	}

	@Override
	public Result modifyOff(Long userId, String operUserType) {
		UserDTO dto = userDao.findUserDTOById(userId);
		if (dto == null) {
			return Result.badResult("用户不存在");
		}
		if (dto.getState().intValue() == User.STATE_DELETE) {
			return Result.badResult("已删除用户不能再次停用");
		}
		if (!dto.getUserType().equals(operUserType)) {
			return Result.badResult("用户不存在");
		}
		userDao.updatestate(dto.getId(), User.STATE_OFF);
		if (User.USER_TYPE_CLIENT.equals(operUserType)) {
			// 管理账号停用，则停用改企业下所有账号
			if (dto.getIdentity() == UserDTO.USER_IDENTITY_MANAGE) {
				Long enterpriseId = userDao.findenterpriseId(userId);
				if (null == enterpriseId || enterpriseId.longValue() == 0l) {
					return Result.badResult("参数错误");
				}
				List<?> enterpriseUserList = userDao.findEnterpriseUser(enterpriseId);
				List<Long> userIds = new ArrayList<Long>();
				for (Object object : enterpriseUserList) {
					Object[] obj = (Object[]) object;
					userIds.add(Long.valueOf(obj[0].toString()));
				}
				if (userIds.size() > 0) {
					userDao.udpateUdateState(userIds, User.STATE_OFF);
				}
			}
		}
		return Result.rightResult(dto.getUsername());
	}

	@Override
	public Long countByUserName(Long userId, String username) {
		return userDao.countByUserName(userId, username.toLowerCase());
	}

	@Override
	public Long countByMobile(Long userId, String mobile) {
		return userDao.countByMobile(userId, mobile);
	}

	@Override
	public Long countByEmail(Long userId, String email) {
		return userDao.countByEmail(userId, email);
	}

	@Override
	public Result addUser(String username, String realname, String password, String email, String mobile, String roles, Long departmentId, User currentUser, String userType, int identity) {
		List<UserRoleAssign> urs = new ArrayList<UserRoleAssign>();
		String message = this.getUserRoles(roles, urs);
		if (!StringUtils.isEmpty(message)) {
			return Result.badResult(message);
		}
		if (urs.size() == 0) {
			return Result.badResult("角色权限不能为空");
		}
		if (this.countByUserName(0l, username) > 0) {
			return Result.badResult("用户名已存在");
		}
		// if (this.countByMobile(0l, mobile) > 0) {
		// return Result.badResult("手机号已经存在");
		// }
		// if (this.countByEmail(0l, email) > 0) {
		// return Result.badResult("邮箱已经存在");
		// }
		Department department = departmentDao.findById(departmentId);
		if (department == null) {
			return Result.badResult("部门不存在");
		}
		if (urs.size() == 0) {
			return Result.badResult("权限不能为空");
		}
		User user = new User(realname, username.toLowerCase(), password, email, mobile, currentUser.getId(), userType);
		userDao.save(user);
		for (UserRoleAssign ur : urs) {
			ur.setUserId(user.getId());
		}
		userRoleAssignDao.saveBatch(urs);
		UserDepartmentAssign userDepartmentAssign = new UserDepartmentAssign();
		userDepartmentAssign.setUserId(user.getId());
		userDepartmentAssign.setDepartmentId(departmentId);
		userDepartmentAssign.setIdentity(identity);// 默认为普通账号，一个企业只能有一个主账号
		userDepartmentAssignDao.save(userDepartmentAssign);
		return Result.rightResult();
	}

	@Override
	public Result modifyUser(String username, String realname, String email, String mobile, String roles, Long userId, Long departmentId, int identity) {
		User user = this.findUserById(userId);
		if (user == null) {
			return Result.badResult("用户不存在");
		}
		// if (this.countByMobile(userId, mobile).longValue() > 0L) {
		// return Result.badResult("手机号已经存在");
		// }
		// if (!StringUtils.isEmpty(email) && this.countByEmail(userId,
		// email).longValue() > 0L) {
		// return Result.badResult("邮箱已经存在");
		// }
		List<UserRoleAssign> urs = new ArrayList<UserRoleAssign>();
		String message = this.getUserRoles(roles, urs);
		if (!StringUtils.isEmpty(message)) {
			return Result.badResult(message);
		}
		if (urs.size() == 0) {
			return Result.badResult("权限不能为空");
		}
		List<Long> ids = new ArrayList<Long>();
		for (UserRoleAssign ur : urs) {
			ur.setUserId(user.getId());
			ids.add(ur.getRoleId());
		}
		user.setUsername(username);
		user.setRealname(realname);
		user.setEmail(email);
		user.setMobile(mobile);
		userDao.update(user);
		UserDepartmentAssign userDepartmentAssign = new UserDepartmentAssign();
		userDepartmentAssign.setDepartmentId(departmentId);
		userDepartmentAssign.setUserId(userId);
		userDepartmentAssign.setIdentity(identity);
		if (user.getId().longValue() != 1) {
			userRoleAssignDao.deleteByUserId(user.getId());
			userRoleAssignDao.saveBatch(urs);
			userDepartmentAssignDao.deleteDataByUserId(userId);
			userDepartmentAssignDao.save(userDepartmentAssign);
			List<Role> roleList = roleDao.findRoleByIds(ids, EucpSystem.销售系统.getCode());
			if (null == roleList || roleList.size() == 0) {
				List<Long> saleIds = enterpriseBindingSaleDao.getIdsBySaleId(userId.toString());
				if (null != saleIds && saleIds.size() > 0) {
					enterpriseBindingSaleDao.deleteByfieldName("systemUserId", userId);
					enterpriseBindingSaleDao.deleteSystemEnterpriseBindingSaleTop(org.apache.commons.lang3.StringUtils.join(saleIds.toArray(), ","));
				}
			}
		}
		return Result.rightResult(user.getUsername());
	}

	@Override
	public Result updateResetUserPassword(Long userId) {
		User user = userDao.findById(userId);
		if (user == null) {
			return Result.badResult("用户不存在");
		}
		String randomPwd = RandomNumberUtils.getNumbersAndLettersRandom(6);
		String newEnPassword = PasswordUtils.encrypt(Md5.md5(randomPwd.getBytes()));
		user.setPassword(newEnPassword);
		user.setLastChangePasswordTime(null);
		userDao.update(user);
		redis.set(CenterBaseRedisCache.SYSTEM_USER_RESET_PASSWORD + userId, 1, 30 * 60);
		return Result.rightResult(randomPwd);
	}

	@Override
	public Result updateClientPassword(Long userId) {
		User user = userDao.findById(userId);
		if (user == null) {
			return Result.badResult("用户不存在");
		}
		String password = RandomNumberUtils.getNumbersAndLettersRandom(6);
		String newEnPassword = PasswordUtils.encrypt(Md5.md5(password.getBytes()));
		user.setPassword(newEnPassword);
		user.setLastChangePasswordTime(null);
		userDao.update(user);
		redis.set(CenterBaseRedisCache.SYSTEM_USER_RESET_PASSWORD + userId, 1, 30 * 60);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", user.getUsername());
		map.put("password", password);
		return Result.rightResult(map);
	}

	@Override
	public Page<UserDTO> findUserPageByManager(String username, String realname, String mobile, int start, int limit) {
		return userDao.findUserPageByManager(username, realname, mobile, start, limit);
	}

	/*-----------------------------------*/

	@Override
	public Result updateUserPassword(Long userId, String oldPassword, String newPassword) {
		User user = userDao.findById(userId);
		if (user == null) {
			return Result.badResult("用户不存在");
		}
		String enPassword = PasswordUtils.encrypt(oldPassword);
		if (!user.getPassword().equals(enPassword)) {
			return Result.badResult("原密码错误");
		}
		if (user.getPassword().equals(PasswordUtils.encrypt(newPassword))) {
			return Result.badResult("新密码不能与原密码重复");
		}
		String newEnPassword = PasswordUtils.encrypt(newPassword);
		user.setPassword(newEnPassword);
		user.setLastChangePasswordTime(new Date());
		userDao.update(user);
		return Result.rightResult(user.getUsername());
	}

	@Override
	public Result updateClientPassword(Long userId, String password) {
		User user = userDao.findById(userId);
		if (user == null) {
			return Result.badResult("用户不存在");
		}
		String newEnPassword = PasswordUtils.encrypt(password);
		user.setPassword(newEnPassword);
		user.setLastChangePasswordTime(new Date());
		userDao.update(user);
		return Result.rightResult(user.getUsername());
	}

	@Override
	public void updateLockUserByUsername(String username) {
		User user = userDao.findUserByUserName(username.toLowerCase());
		if (user == null) {
			return;
		}
		user.setState(User.STATE_LOCKING);
		userDao.update(user);
	}

	@Override
	public Page<UserDTO> findBycondition(String variableName, Long departmentId, int start, int limit) {
		Page<User> page = userDao.findBycondition(variableName, departmentId, start, limit);
		Page<UserDTO> pagedto = new Page<UserDTO>();
		List<UserDTO> listdto = new ArrayList<UserDTO>();
		Department department = departmentDao.findById(departmentId);
		Department parentDepartment = departmentDao.findById(department.getParentDepartmentId());
		for (User user : page.getList()) {
			UserDTO dto = new UserDTO(user, department, parentDepartment);
			listdto.add(dto);
		}
		pagedto.setList(listdto);
		pagedto.setCurrentPageNum(page.getCurrentPageNum());
		pagedto.setLimit(page.getLimit());
		pagedto.setStart(page.getStart());
		pagedto.setTotalCount(page.getTotalCount());
		pagedto.setTotalPage(page.getTotalPage());
		return pagedto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WarningUserDTO> findUserSByIds(Set<Long> idsSet) {
		List<Object[]> users = (List<Object[]>) userDao.findUserSByIds(idsSet);
		List<WarningUserDTO> userList = new ArrayList<WarningUserDTO>();
		for (Object[] user : users) {
			userList.add(new WarningUserDTO((Long) user[0], String.valueOf(user[1]), String.valueOf(user[2]), String.valueOf(user[3]), String.valueOf(user[4])));
		}
		return userList;
	}

	@Override
	public List<WarningUserDTO> findUserSByIdsIgnoreState(Set<Long> idsSet) {
		return userDao.findUserSByIdsIgnoreState(idsSet);
	}

	@Override
	public List<User> countByUserNames(List<String> list, String roleType) {
		return userDao.countByUserNames(list, roleType);
	}

	@Override
	public List<User> countByRealNames(List<String> list, String roleType) {
		return userDao.countByRealNames(list, roleType);
	}

	@Override
	public List<User> countByUserIds(List<Long> list, String roleType) {
		return userDao.countByUserIds(list, roleType);
	}

	@Override
	public Boolean isClientUser(Long userId) {
		User user = findUserById(userId);
		return user.getUserType().equals(User.USER_TYPE_CLIENT);
	}

	@Override
	public Boolean isMangerAccount(Long userId) {
		UserDepartmentAssign entity = userDepartmentAssignDao.findByUserId(userId);
		if (null != entity && entity.getIdentity().intValue() == UserDepartmentAssign.IDENTITY_LEADER) {
			return true;
		}
		return false;
	}

	@Override
	public Page<UserDTO> findall(int start, int limit, String username, String realname, String mobile, Long userId) {
		Long findenterpriseId = userDao.findenterpriseId(userId);
		if (!RegularUtils.isEmpty(username)) {
			username = username.toLowerCase();
		}
		UserDepartmentAssign userDepartmentAssign = userDepartmentAssignDao.findByUserId(userId);
		Long clientUserId = null;
		if (UserDepartmentAssign.IDENTITY_EMPLOYEE == userDepartmentAssign.getIdentity()) {
			clientUserId = userId;
		}
		Page<UserDTO> page = userDao.findall(start, limit, username, realname, mobile, findenterpriseId, clientUserId);
		return page;
	}

	@Override
	public void updatestate(Long id, int state) {
		userDao.updatestate(id, state);

	}

	@Override
	public Boolean findmobile(Long id, String mobile) {
		Boolean findmobile = userDao.findmobile(id, mobile);
		return findmobile;
	}

	@Override
	public Boolean findemail(Long id, String email) {
		Boolean findemail = userDao.findemail(id, email);
		return findemail;
	}

	@Override
	public void add(User user) {
		userDao.save(user);

	}

	@Override
	public User findBymobile(String mobile) {
		User user = userDao.findBymobile(mobile);
		return user;
	}

	@Override
	public void update(User user) {
		userDao.update(user);

	}

	@Override
	public void updatepassword(String password, Long id) {
		Date date = new Date();
		userDao.updatepassword(password, id, date);
		redis.set(CenterBaseRedisCache.SYSTEM_USER_RESET_PASSWORD + id, 1, 30 * 60);
	}

	@Override
	public List<UserRoleDTO> finduserAllRole(Long id) {
		List<UserRoleDTO> andRole = userDao.finduserAndRole(id);
		return andRole;
	}

	@Override
	public Result updatePassword(User user, String password, String newPassword, String verifyPassword) {
		Result result = this.checkModifyPassword(user, password, newPassword, verifyPassword);
		if (result.getSuccess()) {
			// checkModifyPassword(user, password, newPassword, verifyPassword);
			user.setPassword(PasswordUtils.encrypt(newPassword));
			userDao.update(user);
		}
		return result;
	}

	@Override
	public Result updateRealName(User user, String realName) {
		Result result = checkModifyRealName(user, realName);
		if (result.getSuccess()) {
			user.setRealname(realName);
			userDao.update(user);
		}
		return result;
	}

	@Override
	public Result updateMoblie(User user, String mobile) {
		Result result = checkModifyMobile(user, mobile);
		if (result.getSuccess()) {
			user.setMobile(mobile);
			userDao.update(user);
		}
		return result;
	}

	@Override
	public Result updateEmail(User user, String email) {
		Result result = checkModifyEmail(user, email);
		if (result.getSuccess()) {
			user.setEmail(email);
			userDao.update(user);
		}
		return result;
	}

	@Override
	public ClientUserIndexDTO findByUserId(User user) {
		ClientUserIndexDTO clientUserIndexDTO = new ClientUserIndexDTO();
		clientUserIndexDTO.setId(user.getId());
		clientUserIndexDTO.setUsername(user.getUsername());
		clientUserIndexDTO.setRealname(user.getRealname());
		clientUserIndexDTO.setMobile(user.getMobile());
		// 查询企业信息
		UserDepartmentAssign userDepartmentAssign = userDepartmentAssignDao.findByUserId(user.getId());
		Department department = departmentDao.findById(userDepartmentAssign.getDepartmentId());
		Enterprise enterprise = enterpriseDao.findById(department.getEnterpriseId());
		// 查询该企业下所有的服务号
		// List<SmsServiceCode> codeALLList =
		// smsServiceCodeDao.findClinetUserBindServiceCode(user.getId());
		// Boolean voiceService = false;
		// for (SmsServiceCode code : codeALLList) {
		// if (code.getIsOpenVoice()) {
		// voiceService = true;
		// break;
		// }
		// }
		// 查询所拥有的服务
		// List<?> serviceType =
		// userServiceCodeAssignDao.findServiceTypeByUserId(user.getId());
		// List<String> serviceTypeList = new ArrayList<String>();
		// if (StringUtils.isEmpty(serviceType.get(0)) && !voiceService) {
		// clientUserIndexDTO.setServiceCount(0);
		// } else {
		// String serviceModule = "0";
		// if (!StringUtils.isEmpty(serviceType.get(0))) {
		// serviceModule += serviceType.get(0).toString().trim();
		// }
		// if (voiceService) {
		// serviceModule += ",3";
		// }
		// clientUserIndexDTO.setServiceModules(serviceModule);
		// String[] ob = (serviceModule).split(",");
		// for (String o : ob) {
		// if (!StringUtils.isEmpty(o)) {
		// serviceTypeList.add(o);
		// }
		// }
		// clientUserIndexDTO.setServiceCount(serviceTypeList.size());
		// }
		// 查询系统所拥有的服务 没有地方获取,暂时这样
		List<ServiceModuleDto> moduleList = new ArrayList<ServiceModuleDto>();
		List<Business> businessList = businessDao.findAllBusiness();
		for (int i = 0; i < businessList.size(); i++) {
			Business business = businessList.get(i);
			moduleList.add(new ServiceModuleDto(business.getBusinessIndex() + "", business.getBusinessNameSimple() + "服务", 1));
		}
		/*
		 * moduleList.add(new ServiceModuleDto("1", "短信服务", 1)); moduleList.add(new
		 * ServiceModuleDto("2", "流量服务", 1)); moduleList.add(new ServiceModuleDto("3",
		 * "国际短信服务", 1)); moduleList.add(new ServiceModuleDto("4", "语音服务", 1));
		 */

		String serviceTypes = enterprise.getServiceType();
		String[] sts = serviceTypes.split(",");
		for (String st : sts) {
			for (ServiceModuleDto dto : moduleList) {
				if (dto.getId().equals(st)) {
					dto.setState(0);
				}
			}
		}
		clientUserIndexDTO.setServiceModulesList(moduleList);
		// 子账号信息
		List<UserDTO> childAccountList = new ArrayList<UserDTO>();
		if (userDepartmentAssign.getIdentity() == UserDepartmentAssign.IDENTITY_LEADER) {
			childAccountList = userDao.findEnterpriseChild(enterprise.getId());
		}
		clientUserIndexDTO.setChildCount(childAccountList.size());
		// 首页最多显示5个子账号
		List<UserDTO> childAccountIndexList = new ArrayList<UserDTO>();
		if (childAccountList.size() <= 5) {
			clientUserIndexDTO.setChildList(childAccountList);
		} else {
			for (int i = 0; i < 5; i++) {
				childAccountIndexList.add(childAccountList.get(i));
			}
			clientUserIndexDTO.setChildList(childAccountIndexList);
		}

		// 服务号信息
		// List<ClientServiceCodeDto> serviceCodeList = new
		// ArrayList<ClientServiceCodeDto>();
		Integer identity = 0;
		if (userDepartmentAssign.getIdentity() == UserDepartmentAssign.IDENTITY_LEADER) {
			identity = UserDepartmentAssign.IDENTITY_LEADER;
		} else {
			identity = UserDepartmentAssign.IDENTITY_EMPLOYEE;
		}
		clientUserIndexDTO.setIdentity(identity);
		clientUserIndexDTO.setEnterpriseId(enterprise.getId());
		clientUserIndexDTO.setEnterpriseName(enterprise.getNameCn());
		// // 需求首页最多显示6个服务号
		// int limit = 6;
		// List<?> accountServiceCodeList =
		// smsServiceCodeDao.findClientAccountServiceCode(enterprise.getId(), null,
		// identity, user.getId(), -1, 0, limit);
		// for (Object object : accountServiceCodeList) {
		// Object[] obj = (Object[]) object;
		// ClientServiceCodeDto clientServiceCodeDto = new ClientServiceCodeDto();
		// clientServiceCodeDto.setType(String.valueOf(obj[0]));
		// clientServiceCodeDto.setServiceCode(String.valueOf(obj[1]));
		// clientServiceCodeDto.setAppId(String.valueOf(obj[2]));
		// clientServiceCodeDto.setSecretKey(String.valueOf(obj[3]));
		// clientServiceCodeDto.setRequestIps(String.valueOf(obj[4]));
		// String appid = String.valueOf(obj[2]);
		// Long balance = redis.hget(CenterBaseRedisCache.CENTER_BALANCE_KEY, appid,
		// Long.class);
		// clientServiceCodeDto.setSmsBalance(balance == null ? 0l : (balance < 0l) ? 0l
		// : balance);
		// clientServiceCodeDto.setBalance(obj[6] == null ? 0l :
		// Long.valueOf(String.valueOf(obj[6])));
		// if (obj[8] != null && !StringUtils.isEmpty(obj[8].toString())) {// 服务号备注
		// clientServiceCodeDto.setServiceCode(String.valueOf(obj[1]) + "(" +
		// obj[8].toString() + ")");
		// }
		// serviceCodeList.add(clientServiceCodeDto);
		// }
		// clientUserIndexDTO.setServiceCodeList(serviceCodeList);
		// // 短信发送情况
		// List<String> serviceCodeListNew = new ArrayList<String>();
		// List<?> accountServiceCodeListNew =
		// smsServiceCodeDao.findClientAccountServiceCode(enterprise.getId(), null,
		// identity, user.getId(), UserServiceCodeAssign.TYPE_SMS, 0, 0);
		// for (Object object : accountServiceCodeListNew) {
		// Object[] obj = (Object[]) object;
		// serviceCodeListNew.add(null == obj[1] ? "" : String.valueOf(obj[1]));
		// }
		// if (serviceCodeListNew.size() <= 0) {
		// serviceCodeListNew.add("");
		// }
		// Date startTime = DateUtil.getNowMonthFirstDay();
		// Date endTime = DateUtil.getNowMonthLastDay();
		// Long smsCount = smsStatisticsDao.countByMonth(enterprise.getId(),
		// serviceCodeListNew, startTime, endTime);
		// Long voiceCount = smsReportVoiceClientDao.countByMonth(enterprise.getId(),
		// serviceCodeListNew, startTime, endTime);
		// clientUserIndexDTO.setSmsCount(null == smsCount ? 0l : smsCount);
		// clientUserIndexDTO.setVoiceCount(null == voiceCount ? 0l : voiceCount);
		return clientUserIndexDTO;
	}

	// @Override
	// public List<Map<String, Object>> ClientSmsSendStatistics(User user) {
	// List<Map<String, Object>> retList =
	// smsServiceCodeDao.findClientAccountServiceCodeForTop(null, user.getId(), 0,
	// 3).getList();
	// for (Map<String, Object> map : retList) {
	// String remark = map.get("remark") == null ? "" :
	// map.get("remark").toString();
	// if (null != remark && !"".equals(remark)) {
	// String serviceCode = map.get("service_code") == null ? "" :
	// map.get("service_code").toString();
	// map.put("service_code", serviceCode + "(" + remark + ")");
	// }
	// String appId = map.get("app_id") == null ? "" : map.get("app_id").toString();
	// if (null != appId && !"".equals(appId)) {
	// Long balance = redis.hget(CenterBaseRedisCache.CENTER_BALANCE_KEY, appId,
	// Long.class);
	// map.put("smsBalance", balance == null ? 0l : (balance < 0l) ? 0l : balance);
	// }
	// }
	// return retList;
	// ClientSmsSendStatisticsDTO clientUserIndexDTO = new
	// ClientSmsSendStatisticsDTO();
	// clientUserIndexDTO.setId(user.getId());
	// clientUserIndexDTO.setUsername(user.getUsername());
	// // 查询企业信息
	// // 需优化
	// UserDepartmentAssign userDepartmentAssign =
	// userDepartmentAssignDao.findByUserId(user.getId());
	// Department department =
	// departmentDao.findById(userDepartmentAssign.getDepartmentId());
	// Enterprise enterprise =
	// enterpriseDao.findById(department.getEnterpriseId());
	// // 服务号信息
	// List<ClientServiceCodeDto> serviceCodeList = new
	// ArrayList<ClientServiceCodeDto>();
	// Integer identity = 0;
	// if (userDepartmentAssign.getIdentity() ==
	// UserDepartmentAssign.IDENTITY_LEADER) {
	// identity = UserDepartmentAssign.IDENTITY_LEADER;
	// } else {
	// identity = UserDepartmentAssign.IDENTITY_EMPLOYEE;
	// }
	// clientUserIndexDTO.setIdentity(identity);
	// List<?> accountServiceCodeList =
	// smsServiceCodeDao.findClientAccountServiceCodeForTop(enterprise.getId(),
	// null, identity, user.getId(), UserServiceCodeAssign.TYPE_SMS, 0, 3);
	// for (Object object : accountServiceCodeList) {
	// Object[] obj = (Object[]) object;
	// ClientServiceCodeDto clientServiceCodeDto = new
	// ClientServiceCodeDto();
	// clientServiceCodeDto.setType(String.valueOf(obj[0]));
	// clientServiceCodeDto.setServiceCode(String.valueOf(obj[1]));
	// clientServiceCodeDto.setAppId(String.valueOf(obj[2]));
	// clientServiceCodeDto.setSecretKey(String.valueOf(obj[3]));
	// clientServiceCodeDto.setRequestIps(String.valueOf(obj[4]));
	// String appid = String.valueOf(obj[2]);
	// Long balance = redis.hget(CenterBaseRedisCache.CENTER_BALANCE_KEY, appid,
	// Long.class);
	// clientServiceCodeDto.setSmsBalance(balance == null ? 0l : (balance <
	// 0l) ? 0l : balance);
	// clientServiceCodeDto.setBalance(obj[6] == null ? 0l :
	// Long.valueOf(String.valueOf(obj[6])));
	// if (obj[8] != null && !StringUtils.isEmpty(obj[8].toString())) {//
	// // 服务号备注
	// clientServiceCodeDto.setServiceCode(String.valueOf(obj[1]) + "(" +
	// obj[8] + ")");
	// }
	// serviceCodeList.add(clientServiceCodeDto);
	// }
	// clientUserIndexDTO.setServiceCodeList(serviceCodeList);
	// return clientUserIndexDTO;
	// }

	/**
	 * 个人信息修改密码效验规则
	 * 
	 * @param user
	 * @param password
	 * @param newPassword
	 * @param verifyPassword
	 * @return
	 */
	private Result checkModifyPassword(User user, String password, String newPassword, String verifyPassword) {
		if (RegularUtils.isEmpty(password)) {
			return Result.badResult("当前密码不能为空");
		}
		if (RegularUtils.isEmpty(newPassword)) {
			return Result.badResult("新密码不能为空");
		}
		if (RegularUtils.isEmpty(verifyPassword)) {
			return Result.badResult("确认新密码不能为空");
		}
		if (!user.getPassword().equals(PasswordUtils.encrypt(password))) {
			return Result.badResult("当前密码不正确");
		}
		if (!newPassword.equals(verifyPassword)) {
			return Result.badResult("两次密码输入不匹配");
		}
		if (user.getPassword().equals(PasswordUtils.encrypt(newPassword))) {
			return Result.badResult("新密码不能与原密码重复");
		}
		return Result.rightResult(user.getUsername() + "修改密码成功");
	}

	/**
	 * 修改名称验证
	 * 
	 * @param user
	 * @param realName
	 * @return
	 */
	private Result checkModifyRealName(User user, String realName) {
		if (RegularUtils.isEmpty(realName)) {
			return Result.badResult("新姓名不能为空");
		}
		if (realName.length() > 10) {
			return Result.badResult("姓名不能超过10个字符");
		}
		if (!RegularUtils.notExistSpecial(realName)) {
			return Result.badResult("姓名不能包含特殊字符");
		}
		return Result.rightResult(user.getUsername() + "修改姓名成功");
	}

	/**
	 * 修改手机号
	 * 
	 * @param user
	 * @param mobile
	 * @return
	 */
	private Result checkModifyMobile(User user, String mobile) {
		if (RegularUtils.isEmpty(mobile)) {
			return Result.badResult("新手机号不能为空");
		}
		if (!RegularUtils.checkMobileFormat(mobile)) {
			return Result.badResult("请输入正确手机号");
		}
		if (null != user.getMobile() && mobile.endsWith(user.getMobile())) {
			return Result.badResult("新手机号不能与原手机号重复");
		}
		// User findUser = userDao.findBymobile(mobile);
		// if (null != findUser) {
		// return Result.badResult("手机号码重复");
		// }
		return Result.rightResult(user.getUsername() + "修改手机号成功");
	}

	/**
	 * 修改邮箱验证
	 * 
	 * @param user
	 * @param email
	 * @return
	 */
	private Result checkModifyEmail(User user, String email) {
		if (RegularUtils.isEmpty(email)) {
			return Result.badResult("新邮箱不能为空");
		}
		if (!RegularUtils.checkEmail(email)) {
			return Result.badResult("新邮箱格式不正确");
		}
		// Boolean emails = userDao.findemail(null, email);
		// if (emails) {
		// return Result.badResult("邮箱已被使用");
		// }
		return Result.rightResult(user.getUsername() + "修改邮箱成功");
	}

	@Override
	public Boolean findusername(Long id, String username) {
		Boolean boolean1 = userDao.finduserName(id, username);
		return boolean1;
	}

	@Override
	public Long findenterpriseIdbyUser(Long userId) {
		Long id = userDao.findenterpriseId(userId);
		if (null == id) {
			return 0l;
		}
		return id;
	}

	@Override
	public String findenterpriseTypebyUserId(Long userId) {
		Long id = userDao.findenterpriseId(userId);
		Enterprise enterprise = enterpriseDao.findById(id);
		if (null != enterprise) {
			return enterprise.getType();
		}
		return null;
	}

	@Override
	public Result findenterpriseId(Long userId, Long currentUserId) {
		Long findenterpriseId = userDao.findenterpriseId(currentUserId);// 登录用户
		Long enterpriseId = userDao.findenterpriseId(userId);// 操作的用户
		if ((enterpriseId == 0l) || (!enterpriseId.equals(findenterpriseId))) {
			return Result.badResult("操作的用户不是该企业的用户");
		}
		return Result.rightResult(enterpriseId);
	}

	@Override
	public Result findCilentCount(Long id) {
		long count = userDao.findCilentCount(id);
		if (count < 51) {
			return Result.rightResult();
		}
		return Result.badResult("子账户最多只能添加50个");
	}

	@Override
	public Page<UserDTO> findUserPage(String userName, String realName, String mobile, Integer accountType, Integer state, String userType, String clientName, String clientNumber, int start,
			int limit) {
		Page<UserDTO> page = userDao.findUserPage(userName, realName, mobile, accountType, state, userType, clientName, clientNumber, start, limit);
		return page;
	}

	@Override
	public Result addverify(String username, String realname, String mobile, String email, Long usernumber, String roleids) {
		if (StringUtils.isEmpty(username)) {
			return Result.badResult("用户名不能为空");
		}
		if (username.length() < 6 || username.length() > 16) {
			return Result.badResult("用户名长度为6-16个字符");
		}
		if (!RegularUtils.checkUserName(username)) {
			return Result.badResult("请输入英文字母和数字，首位不能为数字");
		}
		Boolean findusername = userDao.finduserName(null, username);
		if (findusername) {
			return Result.badResult("用户名已存在");
		}
		if (StringUtils.isEmpty(realname)) {
			return Result.badResult("名字不能为空");
		}
		if (!RegularUtils.checkString(realname)) {
			return Result.badResult("名字请输入中文或者英文");
		}
		if (realname.length() >= 10) {
			return Result.badResult("名字不能超过10个字符");
		}
		if (StringUtils.isEmpty(mobile)) {
			return Result.badResult("手机号不能为空");
		}
		// Boolean findmobile = userDao.findmobile(null, mobile);
		// if (findmobile) {
		// return Result.badResult("手机号已存在");
		// }
		if (!RegularUtils.checkMobile(mobile)) {
			return Result.badResult("请输入11位数字手机号");
		}
		if (!StringUtils.isEmpty(email)) {
			if (!RegularUtils.checkEmail(email)) {
				return Result.badResult("请正确输入邮箱");
			}
			// Boolean findemail = userDao.findemail(null, email);
			// if (findemail) {
			// return Result.badResult("邮箱已存在");
			// }
		}
		if (usernumber >= 50l) {
			return Result.badResult("最多创建50个子账户");
		}
		if (RegularUtils.isEmpty(roleids)) {
			return Result.badResult("角色不能为空");
		}
		String[] ids = roleids.split(",");
		List<Long> list = new ArrayList<Long>();
		for (String string : ids) {
			list.add(Long.valueOf(string));
		}
		List<Role> role = roleDao.findbyIds(list);
		for (Role role2 : role) {
			if (role2.getRoleType().equals("MANAGE")) {
				return Result.badResult("角色不是客户端角色");
			}
		}
		return Result.rightResult();
	}

	@Override
	public Result updateverify(String realname, String mobile, String email, Long id, String roleids) {
		if (StringUtils.isEmpty(realname)) {
			return Result.badResult("名字不能为空");
		}
		if (realname.length() >= 10) {
			return Result.badResult("名字长度不能超过10个字符");
		}
		if (!RegularUtils.checkString(realname)) {
			return Result.badResult("名字请输入中文或者英文");
		}
		if (StringUtils.isEmpty(mobile)) {
			return Result.badResult("手机号不能为空");
		}
		if (!RegularUtils.checkMobile(mobile)) {
			return Result.badResult("请输入11位数字手机号");
		}
		// Boolean findmobile = userDao.findmobile(id, mobile);
		// if (findmobile) {
		// return Result.badResult("手机号已存在");
		// }
		if (!StringUtils.isEmpty(email)) {
			if (!RegularUtils.checkEmail(email)) {
				return Result.badResult("请正确输入邮箱");
			}
			// Boolean findemail = userDao.findemail(id, email);
			// if (findemail) {
			// return Result.badResult("邮箱已存在");
			// }
		}
		if (RegularUtils.isEmpty(roleids)) {
			return Result.badResult("角色不能为空");
		}
		String[] ids = roleids.split(",");
		List<Long> list = new ArrayList<Long>();
		for (String string : ids) {
			list.add(Long.valueOf(string));
		}
		List<Role> role = roleDao.findbyIds(list);
		for (Role role2 : role) {
			if (role2.getRoleType().equals("MANAGE")) {
				return Result.badResult("角色不是客户端角色");
			}
		}
		return Result.rightResult();
	}

	@Override
	public UserDTO findClientUserById(Long id) {
		UserDTO userDTO = userDao.findClientUserById(id);
		return userDTO;
	}

	@Override
	public List<TurnOperatorUserDTO> findAllTurnOperatorUser(int currentPage, int pageSize, Date date, Boolean isDelete) {
		return userDao.findAllTurnOperatorUser(currentPage, pageSize, date, isDelete);
	}

	private String getUserRoles(String roleIds, List<UserRoleAssign> urs) {
		String message = "";
		String[] roleIdArray = roleIds.split(",");
		Set<Long> roleIdSet = new HashSet<Long>();
		for (String roleId : roleIdArray) {
			try {
				roleIdSet.add(Long.valueOf(roleId));
			} catch (Exception e) {

			}
		}
		Map<Long, Role> map = new HashMap<Long, Role>();
		List<Role> roles = roleDao.findAllNotDelete();
		for (Role role : roles) {
			map.put(role.getId(), role);
		}
		for (Long set : roleIdSet) {
			if (!map.containsKey(set)) {
				message = "数据错误";
			} else {
				UserRoleAssign ur = new UserRoleAssign();
				ur.setRoleId(set);
				urs.add(ur);
			}
		}
		return message;
	}

	@Override
	public Result addClientUser(String username, String realname, String mobile, String email, String roleids, Long operatorId) {
		User user = new User();
		user.setUsername(username.toLowerCase());
		user.setEmail(email);
		user.setMobile(mobile);
		user.setRealname(realname);
		user.setState(2);
		user.setCreateTime(new Date());
		user.setRemark(null);
		String randomPwd = RandomNumberUtils.getNumbersAndLettersRandom(6);
		user.setPassword(PasswordUtils.encrypt(Md5.md5(randomPwd.getBytes())));
		user.setUserType(User.USER_TYPE_CLIENT);
		user.setOperatorId(operatorId);
		userDao.save(user);
		String[] roleIdArr = roleids.split(",");
		List<UserRoleAssign> userRoleAssignList = new ArrayList<UserRoleAssign>();
		for (String roleId : roleIdArr) {
			UserRoleAssign userRoleAssign = new UserRoleAssign();
			userRoleAssign.setRoleId(Long.valueOf(roleId));
			userRoleAssign.setUserId(user.getId());
			userRoleAssignList.add(userRoleAssign);
		}
		userRoleAssignDao.saveBatch(userRoleAssignList);
		UserDepartmentAssign userDepartmentAssign = new UserDepartmentAssign();
		UserDepartmentAssign departmentAssign = userDepartmentAssignDao.findByUserId(operatorId);
		userDepartmentAssign.setUserId(user.getId());
		userDepartmentAssign.setDepartmentId(departmentAssign.getDepartmentId());
		userDepartmentAssign.setIdentity(UserDepartmentAssign.IDENTITY_EMPLOYEE);
		userDepartmentAssignDao.save(userDepartmentAssign);
		return Result.rightResult(randomPwd);
	}

	@Override
	public List<User> findUserByRoleType(String roleType, String userType, Date date, String username) {
		return userDao.findUserByRoleType(roleType, userType, date, username);
	}

	@Override
	public Page<UserDTO> findUserPageByRoleType(String roleType, String userType, String username, int start, int limit) {
		return userDao.findUserPageByRoleType(roleType, userType, username, start, limit);
	}

	@Override
	public List<User> findUserByUserType(String userType) {
		return userDao.findUserByUserType(userType);
	}

	@Override
	public List<User> findUserByLastUpdateTime(Date lastUpdateTime, String userType, int currentPage, int pageSize) {
		return userDao.findUserByLastUpdateTime(lastUpdateTime, userType, currentPage, pageSize);
	}

	@Override
	public Object findMangerAccount(Long enterpriseId) {
		Object object = userDao.findMangerAccount(enterpriseId);
		return object;
	}

	@Override
	public List<User> findByUserName(String userName) {
		return userDao.findByUserName(userName);
	}

	@Override
	public List<User> findUserByIds(Set<Long> idsSet) {
		return userDao.findUserByIds(idsSet);

	}

	@Override
	public boolean updateLastUpdateTimeById(String id) {
		return userDao.updateLastUpdateTimeById(id);
	}

	@Override
	public Page<Map<String, Object>> findServiceCodeBindingPageClient(int start, int limit, String userName, Long userId, Long enterpriseId) {
		return userDao.findServiceCodeBindingPageClient(start, limit, userName, userId, enterpriseId);
	}

	@Override
	public List<String> findEnterpriseUsers(Long currentUserId) {
		return userDao.findEnterpriseUsers(currentUserId);
	}

	// @Override
	// public List<Long> findUserBindServiceCodeClient(Long userId) {
	// return userDao.findUserBindServiceCodeClient(userId);
	// }

	@Override
	public List<Long> findEnterpriseBindServiceCode(Long userId) {
		return userDao.findEnterpriseBindServiceCode(userId);
	}

	@Override
	public User findById(Long id) {
		return userDao.findById(id);
	}

	@Override
	public Page<UserBindingDTO> findUserInfoPage(int start, int limit, String userName, List<Long> userIdList) {
		return userDao.findUserInfoPage(start, limit, userName, userIdList);
	}

	@Override
	public List<User> findall() {
		return userDao.findAll();
	}

	@Override
	public Result checkUserResetPassword(Long userId) {
		if (null != redis.get(CenterBaseRedisCache.SYSTEM_USER_RESET_PASSWORD + userId)) {
			redis.del(CenterBaseRedisCache.SYSTEM_USER_RESET_PASSWORD + userId);
			return Result.badResult();
		}
		return Result.rightResult();
	}

	@Override
	public List<SimpleUserDTO> findByUserType(String userType) {
		return userDao.findByUserType(userType);
	}

	@Override
	public List<User> findByRealNames(List<String> list, String userType) {
		return userDao.findByRealNames(list, userType);
	}

	@Override
	public List<Long> findUserIdbyEnterpriseId(Long enterpriseId) {
		return userDao.findUserIdbyEnterpriseId(enterpriseId);
	}

}
