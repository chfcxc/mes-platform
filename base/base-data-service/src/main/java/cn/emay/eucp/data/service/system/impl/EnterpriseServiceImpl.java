package cn.emay.eucp.data.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.common.encryption.Md5;
import cn.emay.eucp.common.dto.enterprise.EnterpriseUserDTO;
import cn.emay.eucp.common.dto.user.UserDTO;
import cn.emay.eucp.common.moudle.db.system.Department;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.EnterpriseBindingSale;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign;
import cn.emay.eucp.common.moudle.db.system.UserRoleAssign;
import cn.emay.eucp.data.dao.system.DepartmentDao;
import cn.emay.eucp.data.dao.system.EnterpriseBindingSaleDao;
import cn.emay.eucp.data.dao.system.EnterpriseDao;
import cn.emay.eucp.data.dao.system.UserDao;
import cn.emay.eucp.data.dao.system.UserDepartmentAssignDao;
import cn.emay.eucp.data.dao.system.UserRoleAssignDao;
import cn.emay.eucp.data.service.system.EnterpriseService;
import cn.emay.eucp.util.PasswordUtils;
import cn.emay.eucp.util.PinYinUtils;
import cn.emay.eucp.util.RandomNumberUtils;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.util.DateUtil;

/**
 * cn.emay.eucp.common.moudle.db.system.Enterprise Service implement
 * 
 * @author frank
 */
@Service("enterpriseService")
public class EnterpriseServiceImpl implements EnterpriseService {

	@Resource
	private EnterpriseDao enterpriseDao;
	@Resource
	private DepartmentDao departmentDao;
	@Resource
	private UserDao userDao;
	@Resource
	private UserDepartmentAssignDao userDepartmentAssignDao;
	@Resource
	private UserRoleAssignDao userRoleAssignDao;
	@Resource
	private EnterpriseBindingSaleDao enterpriseBindingSaleDao;

	@Override
	public List<Enterprise> getEnterpriseForList() {
		return enterpriseDao.findAll();
	}

	@Override
	public Enterprise getEnterpriseById(Long id) {
		return enterpriseDao.findById(id);
	}

	@Override
	public List<Enterprise> findListByName(String enterpriseName) {
		return enterpriseDao.findListByName(enterpriseName);
	}

	@Override
	public Page<Enterprise> findPage(int start, int limit, Enterprise entity) {
		return enterpriseDao.findPage(start, limit, entity);
	}

	@Override
	public List<Enterprise> findListUseChannel(Long channelId) {
		return enterpriseDao.findListUseChannel(channelId);
	}

	@Override
	public Enterprise findByClientNumber(String clientNumber) {
		return enterpriseDao.findByClientNumber(clientNumber);
	}

	@Override
	public Result findEnterpriseByClientNumber(String clientNumber) {
		if (RegularUtils.isEmpty(clientNumber)) {
			return Result.badResult("客户编号不能为空");
		}
		if (!RegularUtils.checkIntegerSizeEight(clientNumber)) {
			return Result.badResult("输入位数不正确，客户编号只能是1-8位正整数");
		}
		Enterprise enterprise = enterpriseDao.findByClientNumber(clientNumber);
		if (null == enterprise) {
			return Result.badResult("客户编号错误");
		}
		return Result.rightResult(enterprise);
	}

	@Override
	public Enterprise findById(Long enterpriseId) {
		return enterpriseDao.findById(enterpriseId);
	}

	@Override
	public Boolean isExist(String clientNumber) {
		Enterprise entity = enterpriseDao.findByClientNumber(clientNumber);
		if (null == entity) {
			return false;
		}
		return true;
	}

	@Override
	public Result add(String type, String clientNumber, String clientName, String userName, String linkman, String mobile, User currentUser, String email, int isVip, String telephone, String address,
			Long salesId, int authority, String valueAddedService,int viplevel,String serviceType,Date startTime,Date endTime) {
		// 生成管理账号
		String chineseName = clientName;
		// 防止mrp接口操作人为空
		Long userId = null == currentUser ? null : currentUser.getId();
		String username = null == currentUser ? null : currentUser.getUsername();
		String firstSpell = PinYinUtils.getFirstSpell(chineseName);
		if (StringUtils.isEmpty(firstSpell)) {
			return Result.badResult("生成管理账号名称失败");
		}
		if (null != salesId) {
			User sales = userDao.findById(salesId);
			if (null != sales && sales.getState() == User.STATE_DELETE || null != sales && sales.getState() == User.STATE_OFF) {
				return Result.badResult("无法选择删除,停用的销售");
			}
		}
		firstSpell = firstSpell.toLowerCase();// 英文字母转为小写
		if (firstSpell.length() > 4) {
			firstSpell = firstSpell.substring(0, 4);
		}
		userName = firstSpell + clientNumber;// 管理账号名称
		// 查询可能与生成的主账号重复的用户
		List<String> userNameList = userDao.findUserEndWithStr(clientNumber);
		if (null != userNameList && userNameList.size() > 0) {
			for (int i = 0; i < 5; i++) {// 最多生成5次
				if (userNameList.contains(userName)) {// 如果生成的主账号已存在
					char c = (char) (int) (Math.random() * 26 + 97);// 随机生成a-z的字符
					userName = firstSpell + c + clientNumber;
				} else {
					break;
				}
			}
			if (userNameList.contains(userName)) {
				return Result.badResult("生成的管理账号已存在");
			}
		}
		if(null == startTime){//mrp接口创建企业
			startTime = new Date();
		}
		// 客户
		Enterprise enterprise = new Enterprise();
		enterprise.setType(type);
		enterprise.setClientNumber(clientNumber);
		enterprise.setCreateTime(new Date());
		enterprise.setIsDelete(false);
		enterprise.setLinkman(linkman);
		enterprise.setMobile(mobile);
		enterprise.setNameCn(clientName);
		enterprise.setOperatorId(userId);
		enterprise.setEmail(email);
		enterprise.setViplevel(viplevel);
		if (isVip > 0) {
			enterprise.setIsVip(true);
		} else {
			enterprise.setIsVip(false);
		}
		enterprise.setTelephone(telephone);
		enterprise.setAddress(address);
		enterprise.setAuthority(authority);
		enterprise.setValueAddedService(valueAddedService);
		enterprise.setViplevel(viplevel);
		enterprise.setServiceType(serviceType);
		enterprise.setStartClientSelectTime(startTime);
		enterprise.setEndClientSelectTime(endTime);
		enterpriseDao.save(enterprise);
		// 部门
		Department department = new Department();
		department.setDepartmentName("[" + clientNumber + "]" + clientName);
		department.setEnterpriseId(enterprise.getId());
		department.setFullPath(0 + ",");
		department.setIsDelete(false);
		department.setParentDepartmentId(0l);
		departmentDao.save(department);
		// 用户
		String password = RandomNumberUtils.getNumbersAndLettersRandom(6);// 密码:随机6位英文数字组合（区分大小写）
		String psw = PasswordUtils.encrypt(Md5.md5(password.getBytes()));
		User user = new User(linkman, userName, psw, email, mobile, userId, User.USER_TYPE_CLIENT);
		userDao.save(user);
		// 用户部门关联
		UserDepartmentAssign udAssign = new UserDepartmentAssign();
		udAssign.setDepartmentId(department.getId());
		udAssign.setIdentity(UserDepartmentAssign.IDENTITY_LEADER);// 主账户
		udAssign.setUserId(user.getId());
		userDepartmentAssignDao.save(udAssign);
		// 用户角色关联
		UserRoleAssign userRoleAssign = new UserRoleAssign();
		userRoleAssign.setRoleId(2l);// 客户管理员
		userRoleAssign.setUserId(user.getId());
		userRoleAssignDao.save(userRoleAssign);
		// 企业销售关联
		if (null != salesId) {
			List<Object[]> params = new ArrayList<Object[]>();
			params.add(new Object[] { String.valueOf(enterprise.getId()), String.valueOf(salesId), username, username });
			enterpriseBindingSaleDao.saveBatchEnterpriseBindingSale(params);
			userDao.updateLastUpdateTimeById(salesId.toString());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("password", password);
		map.put("userName", userName);
		map.put("enterId", enterprise.getId());
		return Result.rightResult(map);
	}

	@Override
	public Result modify(String type, Long id, String clientName, String linkman, String mobile, User currentUser, String email, int isVip, String telephone, String address, Long salesId, int authority, String valueAddedService,int viplevel,String serviceType,
			Date startTime,Date endTime) {
		// 客户
		Enterprise enterprise = enterpriseDao.findById(id);
		if (null == enterprise) {
			return Result.badResult("数据不存在");
		}
		// 客户关联的主账号、部门
		Object mangerAccount = userDao.findMangerAccount(id);
		if (null == mangerAccount) {
			return Result.badResult("主账号不存在");
		}
		User sales = userDao.findById(salesId);
		if (null != sales && sales.getState() == User.STATE_DELETE || null != sales && sales.getState() == User.STATE_OFF) {
			return Result.badResult("无法选择删除,停用的销售");
		}
		Object[] mangerAccountArray = (Object[]) mangerAccount;
		Long userId = Long.valueOf(mangerAccountArray[0].toString());
		Long departmentId = Long.valueOf(mangerAccountArray[1].toString());
		// Long count = userDao.countByMobile(userId, mobile);
		// if (count != 0l) {
		// return Result.badResult("手机号已存在");
		// }
		// if(!StringUtils.isEmpty(email)){
		// count = userDao.countByEmail(userId, email);
		// if(count!=0l){
		// return Result.badResult("邮箱已存在");
		// }
		// }
		// 更新数据
		enterprise.setNameCn(clientName);
		enterprise.setType(type);
		enterprise.setLinkman(linkman);
		enterprise.setMobile(mobile);
		enterprise.setOperatorId(currentUser.getId());
		enterprise.setEmail(email);
		if (isVip > 0) {
			enterprise.setIsVip(true);
		} else {
			enterprise.setIsVip(false);
		}
		enterprise.setTelephone(telephone);
		enterprise.setAddress(address);
		enterprise.setAuthority(authority);
		enterprise.setValueAddedService(valueAddedService);
		enterprise.setViplevel(viplevel);
		enterprise.setServiceType(serviceType);
		enterprise.setStartClientSelectTime(startTime);
		enterprise.setEndClientSelectTime(endTime);
		enterpriseDao.update(enterprise);
		departmentDao.updateDepartment(departmentId, enterprise.getClientNumber(), clientName);
		userDao.updateUser(userId, linkman, mobile, currentUser.getId(), email);
		EnterpriseBindingSale enterpriseBindingSale = enterpriseBindingSaleDao.findByfieldName("systemEnterpriseId", id);
		if (null == enterpriseBindingSale || salesId.intValue() != enterpriseBindingSale.getSystemUserId().intValue()) {
			if (null != enterpriseBindingSale) {
				enterpriseBindingSaleDao.deleteRelationships(enterpriseBindingSale.getId().toString());
				enterpriseBindingSaleDao.deleteSystemEnterpriseBindingSaleTop(enterpriseBindingSale.getId().toString());
			}
			EnterpriseBindingSale enterSale = new EnterpriseBindingSale();
			enterSale.setSystemEnterpriseId(enterprise.getId());
			enterSale.setSystemUserId(salesId);
			enterSale.setCreateTime(new Date());
			enterSale.setCreateMan(currentUser.getUsername());
			enterSale.setLastUpdateMan(currentUser.getUsername());
			enterpriseBindingSaleDao.save(enterSale);
			userDao.updateLastUpdateTimeById(salesId.toString());
		}
		return Result.rightResult(enterprise.getClientNumber());
	}

	@Override
	public Map<String, Object> findClientInfo(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Enterprise enterprise = enterpriseDao.findById(id);
		if (null != enterprise) {
			map.put("id", enterprise.getId());
			map.put("clientName", enterprise.getNameCn());
			map.put("clientNumber", enterprise.getClientNumber());
			map.put("isVip", enterprise.getIsVip());
			map.put("type", enterprise.getType());
			// 企业账户信息
			List<?> enterpriseUserList = userDao.findEnterpriseUser(enterprise.getId());
			List<Long> userIds = new ArrayList<Long>();
			UserDTO manageAccount = null;
			List<UserDTO> childAccountList = new ArrayList<UserDTO>();
			for (Object object : enterpriseUserList) {
				Object[] obj = (Object[]) object;
				if (Integer.parseInt(obj[3].toString()) == UserDepartmentAssign.IDENTITY_LEADER) {
					manageAccount = new UserDTO(Long.valueOf(obj[0].toString()), obj[1].toString(), Integer.parseInt(obj[2].toString()), Integer.parseInt(obj[3].toString()), "", User.USER_TYPE_CLIENT);
					userIds.add(Long.valueOf(obj[0].toString()));
				} else {
					childAccountList.add(new UserDTO(Long.valueOf(obj[0].toString()), obj[1].toString(), Integer.parseInt(obj[2].toString()), Integer.parseInt(obj[3].toString()), "",
							User.USER_TYPE_CLIENT));
					userIds.add(Long.valueOf(obj[0].toString()));
				}
			}

			map.put("manageAccount", manageAccount);
			map.put("childAccountList", childAccountList);
			map.put("userIds", userIds);
			// 管理账户关联特服号
			// List<Map<String, Object>> serviceCodeList = new
			// ArrayList<Map<String, Object>>();
			// // List<Long> serviceCodeIdList=new ArrayList<Long>();
			// List<?> accountServiceCodeList =
			// smsServiceCodeDao.findManageAccountServiceCode(enterprise.getId());
			// for (Object object : accountServiceCodeList) {
			// Object[] obj = (Object[]) object;
			// Map<String, Object> serviceCodeMap = new HashMap<String,
			// Object>();
			// serviceCodeMap.put("serviceTypeId", obj[0]);
			// serviceCodeMap.put("appId", obj[1]);
			// serviceCodeMap.put("serviceCode", obj[2]);
			// serviceCodeMap.put("secretKey", obj[3]);
			// serviceCodeMap.put("requestIps", obj[4]);
			// serviceCodeMap.put("state", obj[5]);
			// serviceCodeMap.put("serviceCodeId", obj[6]);
			// serviceCodeList.add(serviceCodeMap);
			// //
			// serviceCodeIdList.add(obj[6]==null?0l:Long.valueOf(obj[6].toString()));
			// }
			// //代理商分配服务号信息
			// Map<Long, SmsAgentServiceCode> smsAgentServiceCodeMap = new
			// HashMap<Long, SmsAgentServiceCode>();
			// if(serviceCodeIdList.size()>0){
			// List<SmsAgentServiceCode>
			// smsAgentServiceCodeList=smsAgentServiceCodeDao.findByServiceCodeIds(serviceCodeIdList);
			// for (SmsAgentServiceCode smsAgentServiceCode :
			// smsAgentServiceCodeList) {
			// smsAgentServiceCodeMap.put(smsAgentServiceCode.getServiceCodeId(),
			// smsAgentServiceCode);
			// }
			// }
			// //赋值
			// for(Map<String,Object> scmap:serviceCodeList){
			// Long serviceCodeId=0l;
			// if(!StringUtils.isEmpty(scmap.get("serviceCodeId"))){
			// serviceCodeId=Long.valueOf(scmap.get("serviceCodeId").toString());
			// }
			// SmsAgentServiceCode entity =
			// smsAgentServiceCodeMap.get(serviceCodeId);
			// if (entity == null) {
			// entity = new SmsAgentServiceCode();
			// }
			// scmap.put("nameCn", entity.getEnterpriseName());
			// scmap.put("linkman", entity.getLinkman());
			// scmap.put("mobile", entity.getMobile());
			// }
			// map.put("serviceCodeList", serviceCodeList);
		}
		return map;
	}

	@Override
	public Page<Map<String, Object>> findPage(int start, int limit, String clientName) {
		Map<String, Object> map = enterpriseDao.findPage(start, limit, clientName);
		List<?> list = (List<?>) map.get(Page.DATA_LIST);
		List<Map<String, Object>> clientInfoList = new ArrayList<Map<String, Object>>();
		for (Object object : list) {
			Object[] obj = (Object[]) object;
			Map<String, Object> clientInfoMap = new HashMap<String, Object>();
			clientInfoMap.put("clientId", obj[0]);
			clientInfoMap.put("clientName", obj[1]);
			clientInfoMap.put("clientNumber", obj[2]);
			clientInfoMap.put("type", obj[3]);
			clientInfoMap.put("linkman", obj[4]);
			clientInfoMap.put("mobile", obj[5]);
			clientInfoList.add(clientInfoMap);
		}
		// 分页信息
		Page<Map<String, Object>> page = new Page<Map<String, Object>>();
		page.setNumByStartAndLimit(start, limit, Integer.parseInt(String.valueOf(map.get(Page.TOTAL_COUNT))));
		page.setList(clientInfoList);
		return page;
	}

	@Override
	public Page<Map<String, Object>> findPage(int start, int limit, String clientName, String type) {
		Map<String, Object> map = enterpriseDao.findPage(start, limit, clientName, type);
		List<?> list = (List<?>) map.get(Page.DATA_LIST);
		List<Map<String, Object>> clientInfoList = new ArrayList<Map<String, Object>>();
		for (Object object : list) {
			Object[] obj = (Object[]) object;
			Map<String, Object> clientInfoMap = new HashMap<String, Object>();
			clientInfoMap.put("clientId", obj[0]);
			clientInfoMap.put("clientName", obj[1]);
			clientInfoMap.put("clientNumber", obj[2]);
			clientInfoMap.put("type", obj[3]);
			clientInfoMap.put("linkman", obj[4]);
			clientInfoMap.put("mobile", obj[5]);
			clientInfoList.add(clientInfoMap);
		}
		// 分页信息
		Page<Map<String, Object>> page = new Page<Map<String, Object>>();
		page.setNumByStartAndLimit(start, limit, Integer.parseInt(String.valueOf(map.get(Page.TOTAL_COUNT))));
		page.setList(clientInfoList);
		return page;
	}

	@Override
	public Enterprise isExistByName(String enterpriseName) {
		Enterprise enterprise = enterpriseDao.findeByName(enterpriseName);
		/*
		 * if(null!=enterprise){ return true; }
		 */
		return enterprise;
	}

	@Override
	public Boolean isExistByProperty(String fieldName, Object value) {
		Enterprise entity = enterpriseDao.findEnterpriseByProperty(fieldName, value);
		if (null == entity) {
			return false;
		}
		return true;
	}

	@Override
	public Map<String, Object> findEnterpriseInfo(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Object obj = enterpriseDao.findEnterpriseInfo(id);
		List<Map<String, Object>> list = enterpriseBindingSaleDao.findByEnterpriseId(id);
		if (null != obj) {
			Object[] objArray = (Object[]) obj;
			map.put("id", objArray[0]);
			map.put("nameCn", objArray[1]);
			map.put("clientNumber", objArray[2]);
			map.put("linkman", objArray[3]);
			map.put("mobile", objArray[4]);
			map.put("userName", objArray[5]);
			map.put("email", objArray[6]);
			map.put("isVip", objArray[7]);
			map.put("telephone", objArray[8]);
			map.put("address", objArray[9]);
			map.put("type", objArray[10]);
			map.put("authority", objArray[11]);
			map.put("valueAddedService", objArray[12]);
			map.put("viplevel", objArray[13]);
			map.put("serviceType", objArray[14]);
			map.put("startClientSelectTime", objArray[15]);
			map.put("endClientSelectTime", objArray[16]);
			map.put("startClientSelectTimeStr", null==objArray[15]?null:DateUtil.toString((Date) objArray[15], "yyyy-MM-dd"));
			map.put("endClientSelectTimeStr", null==objArray[16]?null:DateUtil.toString((Date) objArray[15], "yyyy-MM-dd"));
		}
		String salesId = "";
		String realname = "";
		if (list.size() > 0) {
			Map<String, Object> mapsales = list.get(0);
			salesId = mapsales.get("ID").toString();
			realname = mapsales.get("REALNAME").toString();
		}
		map.put("salesId", salesId);
		map.put("realname", realname);
		return map;
	}

	@Override
	public Enterprise findEnterpriseByName(String enterpriseName) {
		return enterpriseDao.findEnterpriseByName(enterpriseName);
	}

	@Override
	public Enterprise findEnterpriseByServiceCode(String serviceCode) {
		return enterpriseDao.findEnterpriseByServiceCode(serviceCode);
	}

	@Override
	public Result findEnterpriseByUserId(Long userId) {
		Enterprise enterprise = enterpriseDao.findEnterpriseByUserId(userId);
		if (null == enterprise) {
			return Result.badResult();
		}
		return Result.rightResult(enterprise);
	}

	@Override
	public boolean findemail(String email, Long id) {
		Enterprise enterprise = enterpriseDao.findEmail(email);
		if (null == enterprise) {
			return true;
		}
		if (enterprise.getId().longValue() == id.longValue()) {
			return true;
		}
		return false;
	}

	@Override
	public List<Enterprise> getEnterprises(Boolean isDelete) {
		return enterpriseDao.getEnterprises(isDelete);
	}

	@Override
	public List<Enterprise> findByLastUpdateTime(Date lastUpdateTime, int currentPage, int pageSize) {
		return enterpriseDao.findByLastUpdateTime(lastUpdateTime, currentPage, pageSize);
	}

	@Override
	public void batchUpdateBalance(List<Object[]> list) {
		enterpriseDao.batchUpdateBalance(list);
	}

	@Override
	public Enterprise findByUserId(Long userId) {
		return enterpriseDao.findEnterpriseByUserId(userId);
	}

	@Override
	public List<Enterprise> getEnterpriseByNameAndClientNumber(String Name, String clientNumber) {
		return enterpriseDao.getEnterpriseByNameAndClientNumber(Name, clientNumber);
	}

	@Override
	public List<Enterprise> getEnterprisesByType(String nameCn, String type) {
		return enterpriseDao.getEnterprisesByType(nameCn, type);
	}

	public List<Enterprise> findByIds(Set<Long> idset) {
		return enterpriseDao.findByids(idset);
	}

	@Override
	public List<Enterprise> findListByNameAndClientNumber(String enterpriseName, String clientNumber) {
		return enterpriseDao.findListByNameAndClientNumber(enterpriseName, clientNumber);
	}

	@Override
	public List<Long> findEnterpriseIdListByName(String enterpriseName) {
		List<Long> list = new ArrayList<Long>();
		List<Enterprise> enterpriseList = enterpriseDao.findListByName(enterpriseName);
		for (Enterprise enterprise : enterpriseList) {
			list.add(enterprise.getId());
		}
		return list;
	}

	@Override
	public List<EnterpriseUserDTO> findListByNameAndRealName(String enterpriseName, String saleManager, Set<Long> enterpriseIdSet) {
		return enterpriseDao.findListByNameAndRealName(enterpriseName, saleManager, enterpriseIdSet);
	}

	@Override
	public Page<Map<String, Object>> findPageByServiceType(int start, int limit, String enterpriseName, String voiceService) {
		Map<String, Object> map = enterpriseDao.findPageByServiceType(start, limit, enterpriseName,voiceService);
		List<?> list = (List<?>) map.get(Page.DATA_LIST);
		List<Map<String, Object>> clientInfoList = new ArrayList<Map<String, Object>>();
		for (Object object : list) {
			Object[] obj = (Object[]) object;
			Map<String, Object> clientInfoMap = new HashMap<String, Object>();
			clientInfoMap.put("clientId", obj[0]);
			clientInfoMap.put("clientName", obj[1]);
			clientInfoMap.put("clientNumber", obj[2]);
			clientInfoMap.put("type", obj[3]);
			clientInfoMap.put("linkman", obj[4]);
			clientInfoMap.put("mobile", obj[5]);
			clientInfoList.add(clientInfoMap);
		}
		// 分页信息
		Page<Map<String, Object>> page = new Page<Map<String, Object>>();
		page.setNumByStartAndLimit(start, limit, Integer.parseInt(String.valueOf(map.get(Page.TOTAL_COUNT))));
		page.setList(clientInfoList);
		return page;
	}
}
