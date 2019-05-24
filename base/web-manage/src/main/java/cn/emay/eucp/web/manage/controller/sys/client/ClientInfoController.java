package cn.emay.eucp.web.manage.controller.sys.client;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.common.encryption.Md5;
import cn.emay.eucp.common.constant.EucpSystem;
import cn.emay.eucp.common.constant.GlobalConstants;
import cn.emay.eucp.common.dto.user.UserDTO;
import cn.emay.eucp.common.moudle.db.system.AuthLevelBindName;
import cn.emay.eucp.common.moudle.db.system.Business;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.Role;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign;
import cn.emay.eucp.common.moudle.db.system.UserRoleAssign;
import cn.emay.eucp.data.service.system.AuthLevelBindNameService;
import cn.emay.eucp.data.service.system.BusinessService;
import cn.emay.eucp.data.service.system.EnterpriseService;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.data.service.system.RoleService;
import cn.emay.eucp.data.service.system.UserDepartmentAssignService;
import cn.emay.eucp.data.service.system.UserRoleAssignService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.util.PasswordUtils;
import cn.emay.eucp.util.RandomNumberUtils;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 客户信息管理
 *
 * @author Frank
 *
 */
@PageAuth("SYS_CLIENT_INFO")
@RequestMapping("/sys/client/info")
@Controller
public class ClientInfoController {

	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "roleService")
	private RoleService roleService;
	@Resource(name = "businessService")
	private BusinessService businessService;
	@Resource(name = "enterpriseService")
	private EnterpriseService enterpriseService;
	@Resource(name = "userRoleAssignService")
	private UserRoleAssignService userRoleAssignService;
	@Resource(name = "manageUserOperLogService")
	private ManageUserOperLogService manageUserOperLogService;
	@Resource(name = "authLevelBindNameService")
	private AuthLevelBindNameService authLevelBindNameService;
	@Resource(name = "userDepartmentAssignService")
	private UserDepartmentAssignService userDepartmentAssignService;

	private Logger log = Logger.getLogger(ClientInfoController.class);

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/client/info/info";
	}

	/**
	 * 列表
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		String clientName = RequestUtils.getParameter(request, "clientName");
		String linkman = RequestUtils.getParameter(request, "linkman");
		String mobile = RequestUtils.getParameter(request, "mobile");
		String clientNumber = RequestUtils.getParameter(request, "clientNumber");
		int start = RequestUtils.getIntParameter(request, "start", GlobalConstants.DEFAULT_PAGE_START);
		int limit = RequestUtils.getIntParameter(request, "limit", GlobalConstants.DEFAULT_PAGE_LIMIT);
		int viplevel = RequestUtils.getIntParameter(request, "viplevel", -1);
		Enterprise entity = new Enterprise();
		entity.setNameCn(clientName);
		entity.setLinkman(linkman);
		entity.setMobile(mobile);
		entity.setClientNumber(clientNumber);
		entity.setViplevel(viplevel);
		Page<Enterprise> page = enterpriseService.findPage(start, limit, entity);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

	/**
	 * 新增页面
	 *
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@OperationAuth("OPER_SYS_ADD_CLIENT")
	@RequestMapping("/to/add")
	public String add(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<AuthLevelBindName> authList = authLevelBindNameService.selectAuthLevel();
		Collections.reverse(authList);// 反转方便输出
		model.addAttribute("authNameList", authList);
		//所有业务模块
		List<Business> businessList = businessService.findAllBusiness();
		model.addAttribute("businessList", businessList);
		return "sys/client/info/add";
	}

	/**
	 * 新增
	 *
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_ADD_CLIENT")
	@RequestMapping("/ajax/add")
	public void addDo(HttpServletRequest request, HttpServletResponse response) {
		String clientNumber = RequestUtils.getParameter(request, "clientNumber");
		String type = RequestUtils.getParameter(request, "type");
		String clientName = RequestUtils.getParameter(request, "clientName");
		// String userName = RequestUtils.getParameter(request, "userName");
		String linkman = RequestUtils.getParameter(request, "linkman");
		String mobile = RequestUtils.getParameter(request, "mobile");
		String email = RequestUtils.getParameter(request, "email");
		int isVip = RequestUtils.getIntParameter(request, "isvip", 0);
		String telephone = RequestUtils.getParameter(request, "telephone");
		String address = RequestUtils.getParameter(request, "address");
		Long salesId = RequestUtils.getLongParameter(request, "salesId", 0l);
		int authority = RequestUtils.getIntParameter(request, "authority", 0);
		int viplevel = RequestUtils.getIntParameter(request, "viplevel", 0);
		String valueAddedService = RequestUtils.getParameter(request, "valueAddedService");// 增值服务[1-短链接简易版,2-短链接精确版]
		String serviceType = RequestUtils.getParameter(request, "serviceType");// 开通服务[1-短信,2-流量,3-国际短信]
		Date startTime = RequestUtils.getDateParameter(request, "startTime", "yyyy-MM-dd", null);// 短信客户端数据范围开始时间
		Date endTime = RequestUtils.getDateParameter(request, "endTime", "yyyy-MM-dd", null);// 短信客户端数据范围结束时间
		Result result = checkAddParameter(clientNumber, clientName, linkman, mobile, email, address, salesId, type, authority, valueAddedService, serviceType);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		result = enterpriseService.add(type, clientNumber, clientName, null, linkman, mobile, currentUser, email, isVip, telephone, address, salesId, authority, valueAddedService, viplevel,
				serviceType,startTime,endTime);
		if (result.getSuccess()) {
			String service = "公共服务";
			String module = "客户管理";
			String context = "新增客户:客户编号为{0},客户名称为{1}";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { clientNumber, clientName }), ManageUserOperLog.OPERATE_ADD);
			log.info("【公共服务>客户管理】-->用户:" + currentUser.getUsername() + "新增客户:客户编号为" + clientNumber + ",客户名称为" + clientName);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 校验新增参数
	 *
	 * @param clientNumber
	 * @param clientName
	 * @param userName
	 * @param mobile
	 * @param serviceType
	 * @return
	 */
	private Result checkAddParameter(String clientNumber, String clientName, String linkman, String mobile, String email, String address, Long salesId, String type, int authority,
			String valueAddedService, String serviceType) {
		if (StringUtils.isEmpty(clientNumber)) {
			return Result.badResult("客户编号不能为空");
		}
		// 校验正整数1-8位
		String regExp = "^\\+?[1-9]\\d{0,7}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(clientNumber);
		if (!m.matches()) {
			return Result.badResult("客户编号只能是1-8位正整数");
		}
		if (enterpriseService.isExist(clientNumber)) {
			return Result.badResult("客户编号已经存在");
		}

		if (StringUtils.isEmpty(clientName)) {
			return Result.badResult("客户名称不能为空");
		}
		if (clientName.length() > 50) {
			return Result.badResult("客户名称长度不能大于50个字符");
		}

		if (!StringUtils.isEmpty(linkman)) {
			if (linkman.length() > 10) {
				return Result.badResult("联系人长度不能超过10个字符");
			}
		}
		if (!StringUtils.isEmpty(mobile)) {
			if (!RegularUtils.checkMobileFormat(mobile)) {
				return Result.badResult("手机号码格式不正确");
			}
			// if (userService.countByMobile(0l, mobile).longValue() != 0l) {
			// return Result.badResult("手机号已经存在");
			// }
		}
		if (!RegularUtils.isEmpty(email)) {
			if (!RegularUtils.checkEmail(email)) {
				return Result.badResult("邮箱格式不正确");
			}
			// Long emailCount = userService.countByEmail(null, email);
			// if (emailCount > 0) {
			// return Result.badResult("邮箱已存在");
			// }
		}
		if (!StringUtils.isEmpty(address)) {
			if (address.length() > 100) {
				return Result.badResult("地址长度不能超过100个字符");
			}
		}
		if (salesId <= 0l) {
			return Result.badResult("请选择销售");
		}
		if (StringUtils.isEmpty(type)) {
			return Result.badResult("客户类型不能为空！");
		}
		/*
		 * if(authority<Enterprise.AUTHORITY_BASIC ||
		 * authority>Enterprise.AUTHORITY_PREMIUM){ return Result.badResult("客户权限不正确");
		 * }
		 */
		if (!StringUtils.isEmpty(valueAddedService)) {
			String[] vasArr = valueAddedService.split(",");
			for (String vas : vasArr) {
				if (!Enterprise.VALUE_ADDED_SERVICE_SIMPLE.equals(vas) && !Enterprise.VALUE_ADDED_SERVICE_ACCURATE.equals(vas)) {
					return Result.badResult("增值服务不正确");
				}
			}
		}
		if (!StringUtils.isEmpty(serviceType)) {
			//所有业务模块
			List<Business> businessList = businessService.findAllBusiness();
			List<String> businessIdList = new ArrayList<String>();
			for(Business business :businessList){
				businessIdList.add(business.getId().toString());
			}
			
			String[] vasArr1 = serviceType.split(",");
			for (String vas1 : vasArr1) {
				if (!businessIdList.contains(vas1)) {
					return Result.badResult("开通服务不正确");
				}
			}
		}
		return Result.rightResult();
	}

	/**
	 * 修改页面
	 *
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@OperationAuth("OPER_SYS_UPDATE_CLIENT")
	@RequestMapping("/to/modify")
	public String modify(HttpServletRequest request, HttpServletResponse response, Model model) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);
		Map<String, Object> enterprise = enterpriseService.findEnterpriseInfo(id);
		List<AuthLevelBindName> authList = authLevelBindNameService.selectAuthLevel();
		Collections.reverse(authList);// 反转方便输出
		model.addAttribute("enterprise", enterprise);
		model.addAttribute("authNameList", authList);
		//所有业务模块
		List<Business> businessList = businessService.findAllBusiness();
		model.addAttribute("businessList", businessList);
		return "sys/client/info/modify";
	}

	/**
	 * 修改
	 *
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_UPDATE_CLIENT")
	@RequestMapping("/ajax/modify")
	public void modifyDo(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);
		String clientName = RequestUtils.getParameter(request, "clientName");
		String type = RequestUtils.getParameter(request, "type");
		String linkman = RequestUtils.getParameter(request, "linkman");
		String mobile = RequestUtils.getParameter(request, "mobile");
		String email = RequestUtils.getParameter(request, "email");
		int isVip = RequestUtils.getIntParameter(request, "isvip", 0);
		String telephone = RequestUtils.getParameter(request, "telephone");
		String address = RequestUtils.getParameter(request, "address");
		Long salesId = RequestUtils.getLongParameter(request, "salesId", 0l);
		int authority = RequestUtils.getIntParameter(request, "authority", 0);
		int viplevel = RequestUtils.getIntParameter(request, "viplevel", 0);
		String serviceType = RequestUtils.getParameter(request, "serviceType");// 开通服务[1-短信,2-流量,3-国际短信]
		String valueAddedService = RequestUtils.getParameter(request, "valueAddedService");// 增值服务[1-短链接简易版,2-短链接精确版]
		Date startTime = RequestUtils.getDateParameter(request, "startTime","yyyy-MM-dd", null);// 短信客户端数据范围开始时间
		Date endTime = RequestUtils.getDateParameter(request, "endTime", "yyyy-MM-dd", null);// 短信客户端数据范围结束时间
		Result result = checkModifyParameter(id, clientName, mobile, email, linkman, address, salesId, type, authority, valueAddedService, serviceType);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		result = enterpriseService.modify(type, id, clientName, linkman, mobile, currentUser, email, isVip, telephone, address, salesId, authority, valueAddedService, viplevel, serviceType,startTime,endTime);
		if (result.getSuccess()) {
			String service = "公共服务";
			String module = "客户管理";
			String context = "修改客户:客户编号为{0},客户名称为{1}";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { result.getResult(), clientName }), ManageUserOperLog.OPERATE_MODIFY);
			log.info("【公共服务>客户管理】-->用户:" + currentUser.getUsername() + "修改客户:客户编号为" + result.getResult() + ",客户名称为" + clientName);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 校验修改参数
	 *
	 * @param id
	 * @param clientName
	 * @param mobile
	 * @param serviceType
	 * @return
	 */
	private Result checkModifyParameter(Long id, String clientName, String mobile, String email, String linkman, String address, Long salesId, String type, int authority, String valueAddedService,
			String serviceType) {
		if (id <= 0l) {
			return Result.badResult("参数错误");
		}
		if (StringUtils.isEmpty(clientName)) {
			return Result.badResult("客户名称不能为空");
		}
		if (clientName.length() > 50) {
			return Result.badResult("客户名称长度不能大于50个字符");
		}
		if (!StringUtils.isEmpty(linkman)) {
			if (linkman.length() > 10) {
				return Result.badResult("联系人长度不能超过10个字符");
			}
		}
		if (!RegularUtils.isEmpty(mobile)) {
			if (!RegularUtils.checkMobileFormat(mobile)) {
				return Result.badResult("手机号码格式不正确");
			}
		}

		if (!RegularUtils.isEmpty(email)) {
			if (!RegularUtils.checkEmail(email)) {
				return Result.badResult("邮箱格式不正确");
			}
		}
		if (!StringUtils.isEmpty(address)) {
			if (address.length() > 100) {
				return Result.badResult("地址长度不能超过100个字符");
			}
		}
		if (salesId <= 0l) {
			return Result.badResult("请选择销售");
		}
		if (StringUtils.isEmpty(type)) {
			return Result.badResult("客户类型不能为空！");
		}
		/*
		 * if(authority<Enterprise.AUTHORITY_BASIC ||
		 * authority>Enterprise.AUTHORITY_PREMIUM){ return Result.badResult("客户权限不正确");
		 * }
		 */
		if (!StringUtils.isEmpty(valueAddedService)) {
			String[] vasArr = valueAddedService.split(",");
			for (String vas : vasArr) {
				if (!Enterprise.VALUE_ADDED_SERVICE_SIMPLE.equals(vas) && !Enterprise.VALUE_ADDED_SERVICE_ACCURATE.equals(vas)) {
					return Result.badResult("增值服务不正确");
				}
			}
		}
		if (!StringUtils.isEmpty(serviceType)) {
			//所有业务模块
			List<Business> businessList = businessService.findAllBusiness();
			List<String> businessIdList = new ArrayList<String>();
			for(Business business :businessList){
				businessIdList.add(business.getId().toString());
			}
			
			String[] vasArr1 = serviceType.split(",");
			for (String vas1 : vasArr1) {
				if (!businessIdList.contains(vas1)) {
					return Result.badResult("开通服务不正确");
				}
			}
		}
		return Result.rightResult();
	}

	/**
	 * 查看客户账号详情页面
	 *
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@OperationAuth("OPER_SYS_ACCOUNT_CLIENT")
	@RequestMapping("/to/detail")
	public String findClientInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);// 企业id
		model.addAttribute("enterpriseId", id);
		return "sys/client/info/detail";
	}

	/**
	 * 查看客户账号详情--服务信息
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_ACCOUNT_CLIENT")
	@RequestMapping("/ajax/getBusinessList")
	public void getBusinessList(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);// 企业id
		Enterprise enterprise = enterpriseService.findById(id);
		List<Business> businessList = businessService.findUserOpenBusinessList(enterprise.getServiceType());
		ResponseUtils.outputWithJson(response, Result.rightResult(businessList));
	}

	/**
	 * 查看客户账号详情--账号信息
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_ACCOUNT_CLIENT")
	@RequestMapping("/ajax/accountInfo")
	public void findAccountInfo(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);// 企业id
		Map<String, Object> map = enterpriseService.findClientInfo(id);
		ResponseUtils.outputWithJson(response, Result.rightResult(map));
	}

	/**
	 * 创建子账号页面
	 *
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@OperationAuth("OPER_SYS_CLIENT_CREAT_ACCOUNT")
	@RequestMapping("/to/addAccount")
	public String addChildAccount(HttpServletRequest request, HttpServletResponse response, Model model) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);// 企业id
		String clientName = RequestUtils.getParameter(request, "clientName");
		model.addAttribute("id", id);
		try {
			model.addAttribute("clientName", URLDecoder.decode(clientName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.error("解码异常:", e);
		}
		Enterprise enterprise = enterpriseService.findById(id);
		model.addAttribute("type", enterprise.getType());
		// 角色
		List<Role> roleList = roleService.findAll(EucpSystem.客户系统.getCode());
		model.addAttribute("roleList", roleList);
		return "sys/client/info/addaccount";
	}

	/**
	 * 创建子账号--保存子账号基础信息
	 *
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_CLIENT_CREAT_ACCOUNT")
	@RequestMapping("/ajax/addAccount")
	public void addChildAccountDo(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);
		String userName = RequestUtils.getParameter(request, "userName");
		String roleIds = RequestUtils.getParameter(request, "roleIds");
		String mobile = RequestUtils.getParameter(request, "mobile");
		String email = RequestUtils.getParameter(request, "email");
		String linkman = RequestUtils.getParameter(request, "linkman");
		Result clientCount = userService.findCilentCount(id);
		if (!clientCount.getSuccess()) {
			ResponseUtils.outputWithJson(response, clientCount);
			return;
		}
		Result result = checkParameters(userName, mobile, email, linkman);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		if (StringUtils.isEmpty(roleIds)) {
			ResponseUtils.outputWithJson(response, Result.badResult("角色权限不能为空"));
			return;
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		result = addChildAccount(id, userName, mobile, email, linkman, roleIds, currentUser.getId());
		if (result.getSuccess()) {
			String service = "公共服务";
			String module = "客户管理";
			String context = "客户:{0}创建子账号:{1}";
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) result.getResult();
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { map.get("enterpriseName"), userName }), ManageUserOperLog.OPERATE_ADD);
			log.info("【公共服务>客户管理】-->用户:" + currentUser.getUsername() + "为客户:" + map.get("enterpriseName") + "创建子账号:" + userName);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 增加子账户
	 *
	 * @param id
	 * @param userName
	 * @param mobile
	 * @param email
	 * @param likename
	 * @param smsServiceCodeIds
	 * @param roleIds
	 * @param operatorUserId
	 * @param platformCodes
	 * @return
	 */
	private Result addChildAccount(Long id, String userName, String mobile, String email, String likename, String roleIds, Long operatorUserId) {
		Enterprise enterprise = enterpriseService.findById(id);
		if (null == enterprise) {
			return Result.badResult("客户信息不存在");
		}
		Object mangerAccount = userService.findMangerAccount(id);
		if (null == mangerAccount) {
			return Result.badResult("管理账号不存在");
		}
		if (!RegularUtils.isEmpty(userName)) {
			userName = userName.toLowerCase();
		}
		Object[] mangerAccountArray = (Object[]) mangerAccount;
		// 用户
		String randomPwd = RandomNumberUtils.getNumbersAndLettersRandom(6);
		String pwd = PasswordUtils.encrypt(Md5.md5(randomPwd.getBytes()));
		User user = new User(likename, userName, pwd, email, mobile, operatorUserId, User.USER_TYPE_CLIENT);
		userService.add(user);
		user = userService.findUserByUserName(userName);
		// 用户部门关联
		UserDepartmentAssign udAssign = new UserDepartmentAssign();
		udAssign.setDepartmentId(Long.valueOf(mangerAccountArray[1].toString()));// 默认同管理账号部门
		udAssign.setIdentity(UserDepartmentAssign.IDENTITY_EMPLOYEE);// 子账号
		udAssign.setUserId(user.getId());
		userDepartmentAssignService.add(udAssign);
		// 用户角色关联
		List<UserRoleAssign> uraList = new ArrayList<UserRoleAssign>();
		List<Long> roleIdList = new ArrayList<Long>();
		String[] roleIdArray = roleIds.split(",");
		for (String roleId : roleIdArray) {
			roleIdList.add(Long.valueOf(roleId));
			uraList.add(new UserRoleAssign(user.getId(), Long.valueOf(roleId)));
		}
		List<Role> roleList = roleService.findRoleByIds(roleIdList, EucpSystem.客户系统.getCode());
		if (null == roleList || roleList.size() != roleIdList.size()) {
			return Result.badResult("角色不正确");
		}
		userRoleAssignService.saveBatch(uraList);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("enterpriseName", enterprise.getNameCn());
		map.put("password", randomPwd);
		map.put("userId", user.getId());
		return Result.rightResult(map);
	}

	/**
	 * 校验用户名、密码
	 */
	private Result checkParameters(String userName, String mobile, String email, String likename) {
		if (StringUtils.isEmpty(userName)) {
			return Result.badResult("用户名不能为空");
		}
		if (!RegularUtils.checkUserName(userName.toLowerCase())) {
			return Result.badResult("用户名为6-16个小写字母与数字组合,并不能以数字开头");
		}
		if (userService.countByUserName(0l, userName.toLowerCase()).longValue() != 0l) {
			return Result.badResult("用户名已经存在");
		}
		if (RegularUtils.isEmpty(mobile)) {
			return Result.badResult("手机号不能为空");
		}
		if (!RegularUtils.checkMobileFormat(mobile)) {
			return Result.badResult("请输入正确的手机号");
		}
		// if (userService.countByMobile(0l, mobile).longValue() != 0l) {
		// return Result.badResult("手机号已经存在");
		// }
		if (!RegularUtils.isEmpty(email)) {
			if (!RegularUtils.checkEmail(email)) {
				return Result.badResult("请输入正确的邮箱");
			}
			// Long emailCount = userService.countByEmail(null, email);
			// if (emailCount > 0) {
			// return Result.badResult("邮箱已存在");
			// }
		}
		if (RegularUtils.isEmpty(likename)) {
			return Result.badResult("联系人不能为空");
		}
		if (likename.length() > 10) {
			return Result.badResult("联系人为10位字符");
		}
		if (!RegularUtils.checkString(likename)) {
			return Result.badResult("联系人不不正确");
		}
		return Result.rightResult();
	}

	/**
	 * 修改子账号页面
	 *
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@OperationAuth("OPER_SYS_UPDATE__CLIENT_ACCOUNT")
	@RequestMapping("/to/modifyAccount")
	public String modifyChildAccount(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		String clientName = RequestUtils.getParameter(request, "clientName");
		Long parentId = RequestUtils.getLongParameter(request, "parentId", 0l);
		model.addAttribute("userId", userId);
		model.addAttribute("parentId", parentId);
		try {
			model.addAttribute("clientName", URLDecoder.decode(clientName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.error("解码异常:", e);
		}
		User user = userService.findUserById(userId);
		Boolean isClientUser = user.getUserType().equals(User.USER_TYPE_CLIENT);
		Boolean isMangerAccount = userService.isMangerAccount(userId);
		if (!isClientUser || isMangerAccount) {
			redirectAttributes.addFlashAttribute("msg", "参数错误");
			return "redirect:" + WebUtils.getLocalAddress(request) + "/error";
		}
		model.addAttribute("user", user);
		// 角色
		List<Role> roleList = roleService.findAll(EucpSystem.客户系统.getCode());
		model.addAttribute("roleList", roleList);

		// 平台代码
		long enterpriseId = userService.findenterpriseIdbyUser(userId);
		Enterprise enterprise = enterpriseService.findById(enterpriseId);
		model.addAttribute("type", enterprise.getType());
		// 用户关联角色
		List<UserRoleAssign> userRoleAssignList = userRoleAssignService.findByUserId(userId);
		model.addAttribute("userRoleAssignList", userRoleAssignList);
		model.addAttribute("enterpriseId", enterpriseId);
		return "sys/client/info/modifyaccount";
	}

	/**
	 * 修改子账号
	 *
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_UPDATE__CLIENT_ACCOUNT")
	@RequestMapping("/ajax/modifyAccount")
	public void modifyChildAccountDo(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		String roleIds = RequestUtils.getParameter(request, "roleIds");
		String mobile = RequestUtils.getParameter(request, "mobile");
		String email = RequestUtils.getParameter(request, "email");
		String linkman = RequestUtils.getParameter(request, "linkman");
		if (StringUtils.isEmpty(roleIds)) {
			ResponseUtils.outputWithJson(response, Result.badResult("角色权限不能为空"));
			return;
		}
		Boolean isClientUser = userService.isClientUser(userId);
		if (!isClientUser) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		Boolean isMangerAccount = userService.isMangerAccount(userId);
		if (isMangerAccount) {
			ResponseUtils.outputWithJson(response, Result.badResult("管理账号不能修改"));
			return;
		}
		Result checkParameter = checkParameter(mobile, email, linkman, userId);
		if (!checkParameter.getSuccess()) {
			ResponseUtils.outputWithJson(response, checkParameter);
			return;
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result = modifyChildAccount(userId, mobile, email, linkman, roleIds, currentUser.getId());
		if (result.getSuccess()) {
			String service = "公共服务";
			String module = "客户管理";
			String context = "修改子账号:{0}";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { result.getResult() }), ManageUserOperLog.OPERATE_MODIFY);
			log.info("【公共服务>客户管理】-->用户:" + currentUser.getUsername() + "修改子账号:" + result.getResult());
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 *
	 * @param id
	 * @param mobile
	 * @param email
	 * @param likename
	 * @param smsServiceCodeIds
	 * @param roleIds
	 * @param operatorUserId
	 * @param platformCodes
	 * @return
	 */
	public Result modifyChildAccount(Long id, String mobile, String email, String likename, String roleIds, Long operatorUserId) {

		User user = userService.findUserById(id);
		if (null == user) {
			return Result.badResult("子账号不存在");
		}
		if (user.getState() == User.STATE_OFF) {
			return Result.badResult("子账号被停用,不能修改");
		}
		user.setMobile(mobile);
		user.setEmail(email);
		user.setRealname(likename);
		userService.update(user);
		// 角色
		List<UserRoleAssign> uraList = new ArrayList<UserRoleAssign>();
		List<Long> roleIdList = new ArrayList<Long>();
		String[] roleIdArray = roleIds.split(",");
		for (String roleId : roleIdArray) {
			roleIdList.add(Long.valueOf(roleId));
			uraList.add(new UserRoleAssign(user.getId(), Long.valueOf(roleId)));
		}
		List<Role> roleList = roleService.findRoleByIds(roleIdList, EucpSystem.客户系统.getCode());
		if (null == roleList || roleList.size() != roleIdList.size()) {
			return Result.badResult("角色不正确");
		}
		userRoleAssignService.deleteByUserId(id);
		userRoleAssignService.saveBatch(uraList);
		return Result.rightResult(user.getUsername());
	}

	private Result checkParameter(String mobile, String email, String likename, Long userId) {
		if (RegularUtils.isEmpty(mobile)) {
			return Result.badResult("手机号不能为空");
		}
		if (!RegularUtils.checkMobileFormat(mobile)) {
			return Result.badResult("请输入正确的手机号");
		}
		if (!RegularUtils.isEmpty(email)) {
			if (!RegularUtils.checkEmail(email)) {
				return Result.badResult("请输入正确的邮箱");
			}
		}
		if (RegularUtils.isEmpty(likename)) {
			return Result.badResult("联系人不能为空");
		}
		if (likename.length() > 10) {
			return Result.badResult("联系人为10位字符");
		}
		return Result.rightResult();
	}

	/**
	 * 查看子账号详情页面
	 *
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/to/childAccountInfo")
	public String childAccountInfo(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		User user = userService.findUserById(userId);
		Boolean isClientUser = user.getUserType().equals(User.USER_TYPE_CLIENT);
		Boolean isMangerAccount = userService.isMangerAccount(userId);
		if (!isClientUser || isMangerAccount) {
			redirectAttributes.addFlashAttribute("msg", "参数错误");
			return "redirect:" + WebUtils.getLocalAddress(request) + "/error";
		}
		String clientName = RequestUtils.getParameter(request, "clientName");

		// 平台代码
		// 角色
		List<Map<String, Object>> userRoleList = userRoleAssignService.findUserRole(userId, EucpSystem.客户系统.getCode());
		try {
			model.addAttribute("clientName", URLDecoder.decode(clientName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.error("解码异常:", e);
		}
		Long parentId = RequestUtils.getLongParameter(request, "parentId", 0l);
		model.addAttribute("parentId", parentId);
		model.addAttribute("user", user);
		model.addAttribute("userRoleList", userRoleList);
		long enterpriseId = userService.findenterpriseIdbyUser(userId);
		model.addAttribute("enterpriseId", enterpriseId);
		return "sys/client/info/childaccountinfo";
	}

	/**
	 * 启用用户
	 */
	@OperationAuth("OPER_SYS_UPDATE__CLIENT_ACCOUNT")
	@RequestMapping("/ajax/on")
	public void on(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		if (userId <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户不存在"));
			return;
		}
		Boolean isClientUser = userService.isClientUser(userId);
		if (!isClientUser) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		if (userId.longValue() == currentUser.getId().longValue()) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能启用自己"));
			return;
		}
		Result result = userService.modifyOn(userId, User.USER_TYPE_CLIENT);
		if (result.getSuccess()) {
			String service = "公共服务";
			String module = "客户管理";
			String context = "启用子账号:{0}";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { result.getResult() }), ManageUserOperLog.OPERATE_MODIFY);
			log.info("【公共服务>客户管理】-->用户:" + currentUser.getUsername() + "启用子账号:" + result.getResult());
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 停用用户
	 */
	@OperationAuth("OPER_SYS_UPDATE__CLIENT_ACCOUNT")
	@RequestMapping("/ajax/off")
	public void off(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		if (userId <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户不存在"));
			return;
		}
		Boolean isClientUser = userService.isClientUser(userId);
		if (!isClientUser) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		if (userId.longValue() == currentUser.getId().longValue()) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能停用自己"));
			return;
		}
		Result result = userService.modifyOff(userId, User.USER_TYPE_CLIENT);
		if (result.getSuccess()) {
			String service = "公共服务";
			String module = "客户管理";
			String context = "停用子账号:{0}";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { result.getResult() }), ManageUserOperLog.OPERATE_MODIFY);
			log.info("【公共服务>客户管理】-->用户:" + currentUser.getUsername() + "停用子账号:" + result.getResult());
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 重置密码(管理账号、子账号)
	 *
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@OperationAuth("OPER_SYS_UPDATE__CLIENT_ACCOUNT")
	@RequestMapping(value = "/ajax/reset")
	public void resetPassword(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		if (userId <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		Boolean isClientUser = userService.isClientUser(userId);
		if (!isClientUser) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		Result result = userService.updateClientPassword(userId);
		if (result.getSuccess()) {
			Map<String, Object> userMap = (Map<String, Object>) result.getResult();
			String service = "公共服务";
			String module = "客户管理";
			String context = "重置了账号:{0}的密码";
			User currentUser = WebUtils.getCurrentUser(request, response);
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { userMap.get("userName") }), ManageUserOperLog.OPERATE_MODIFY);
			log.info("【公共服务>客户管理】-->用户:" + currentUser.getUsername() + "重置了账号:" + userMap.get("userName") + "的密码");
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 销售人员
	 *
	 * @return
	 */
	@RequestMapping("/ajax/saleslist")
	public void salesPersonList(HttpServletRequest request, HttpServletResponse response) {
		String username = RequestUtils.getParameter(request, "username");
		int start = RequestUtils.getIntParameter(request, "start", GlobalConstants.DEFAULT_PAGE_START);
		int limit = RequestUtils.getIntParameter(request, "limit", GlobalConstants.DEFAULT_PAGE_LIMIT);
		Page<UserDTO> page = userService.findUserPageByRoleType(EucpSystem.销售系统.getCode(), User.USER_TYPE_EMAY, username, start, limit);
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

}
