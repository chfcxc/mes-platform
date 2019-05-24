package cn.emay.eucp.web.manage.controller.sys.user;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.emay.eucp.common.dto.user.UserDTO;
import cn.emay.eucp.common.moudle.db.system.Department;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.Role;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign;
import cn.emay.eucp.common.moudle.db.system.UserRoleAssign;
import cn.emay.eucp.data.service.system.DepartmentService;
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
 * 用户管理
 * 
 * @author Frank
 * 
 */
@PageAuth("SYS_USER_MANAGE")
@RequestMapping("/sys/user/manage")
@Controller
public class UserController {

	private static Logger log = Logger.getLogger(UserController.class);

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "departmentService")
	private DepartmentService departmentService;

	@Resource(name = "userRoleAssignService")
	private UserRoleAssignService userRoleAssignService;

	@Resource(name = "userDepartmentAssignService")
	private UserDepartmentAssignService userDepartmentAssignService;

	@Resource(name = "manageUserOperLogService")
	private ManageUserOperLogService manageUserOperLogService;

	@Resource(name = "roleService")
	private RoleService roleService;

	// @Resource(name = "smsChannelWarningConfigService")
	// private SmsChannelWarningConfigService smsChannelWarningConfigService;
	//
	// @Resource(name = "smsServiceCodeWarningReceiverService")
	// private SmsServiceCodeWarningReceiverService
	// smsServiceCodeWarningReceiverService;
	//
	// @Resource(name = "smsServiceCodeDefaultRuleService")
	// private SmsServiceCodeDefaultRuleService smsServiceCodeDefaultRuleService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/user/manage/manage";
	}

	@OperationAuth("OPER_SYS_USER_ADD")
	@RequestMapping("/adduser")
	public String add(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/user/manage/adduser";
	}

	@OperationAuth("OPER_SYS_USER_UPDATE")
	@RequestMapping("/modify")
	public String modify(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		request.setAttribute("userId", userId);
		Result result = check(userId);
		if (!result.getSuccess()) {
			redirectAttributes.addFlashAttribute("msg", result.getMessage());
			return "redirect:" + WebUtils.getLocalAddress(request) + "/error";
		}
		return "sys/user/manage/modify";
	}

	/**
	 * 用户列表
	 */
	@RequestMapping("/ajax/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String username = RequestUtils.getParameter(request, "username");
		String realname = RequestUtils.getParameter(request, "realname");
		String mobile = RequestUtils.getParameter(request, "mobile");
		Page<UserDTO> userpage = userService.findUserPageByManager(username, realname, mobile, start, limit);
		ResponseUtils.outputWithJson(response, Result.rightResult(userpage));
	}

	/**
	 * 删除用户
	 */
	@OperationAuth("OPER_SYS_USER_DELETE")
	@RequestMapping("/ajax/delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		if (userId <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户不存在"));
			return;
		}
		if (userId == 1l) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作系统管理员"));
			return;
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		if (currentUser.getId().equals(userId)) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能删除自己"));
			return;
		}
		// 判断是否为通道预警接收人
		// Boolean flag = smsChannelWarningConfigService.isWarningUser(userId);
		// if (flag) {
		// ResponseUtils.outputWithJson(response, Result.badResult("用户为通道预警接收人员，不能删除"));
		// return;
		// }
		//
		// flag = smsServiceCodeWarningReceiverService.isWarningUser(userId);
		// if (flag) {
		// ResponseUtils.outputWithJson(response, Result.badResult("用户为预警接收人员，不能删除"));
		// return;
		// }
		// flag =
		// smsServiceCodeDefaultRuleService.isWarningUser(currentUser.getUsername());
		// if (flag) {
		// ResponseUtils.outputWithJson(response,
		// Result.badResult("用户为服务号默认规则预警接收人员，不能删除"));
		// return;
		// }
		Result result = userService.deleteUserByUserId(userId, User.USER_TYPE_EMAY);
		if (result.getSuccess()) {
			String service = "公共服务";
			String context = "删除用户:{0}";
			String module = "用户管理";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { result.getResult() }), ManageUserOperLog.OPERATE_DELETE);
			log.info("用户:" + currentUser.getUsername() + "删除用户,用户名称为:" + result.getResult());
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 启用用户
	 */
	@OperationAuth("OPER_SYS_USER_ON_OFF")
	@RequestMapping("/ajax/on")
	public void on(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		if (userId <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户不存在"));
			return;
		}
		if (userId == 1l) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作系统管理员"));
			return;
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		if (userId.longValue() == currentUser.getId().longValue()) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能启用自己"));
			return;
		}
		Result result = userService.modifyOn(userId, User.USER_TYPE_EMAY);
		if (result.getSuccess()) {
			String service = "公共服务";
			String context = "解锁用户:{0}";
			String module = "用户管理";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { result.getResult() }), ManageUserOperLog.OPERATE_MODIFY);
			log.info("用户:" + currentUser.getUsername() + "解锁用户,用户名称为:" + result.getResult());
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 停用用户
	 */
	@OperationAuth("OPER_SYS_USER_ON_OFF")
	@RequestMapping("/ajax/off")
	public void off(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		if (userId == 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户不存在"));
			return;
		}
		if (userId == 1l) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作系统初始化数据"));
			return;
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		if (userId.longValue() == currentUser.getId().longValue()) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能停用自己"));
			return;
		}
		Result result = userService.modifyOff(userId, User.USER_TYPE_EMAY);
		if (result.getSuccess()) {
			String service = "公共服务";
			String context = "锁定用户:{0}";
			String module = "用户管理";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { result.getResult() }), ManageUserOperLog.OPERATE_MODIFY);
			log.info("用户:" + currentUser.getUsername() + "锁定用户,用户名称为:" + result.getResult());
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 添加用户
	 */
	@OperationAuth("OPER_SYS_USER_ADD")
	@RequestMapping("/ajax/add")
	public void add(HttpServletRequest request, HttpServletResponse response) {
		String username = RequestUtils.getParameter(request, "username");
		String realname = RequestUtils.getParameter(request, "realname");
		String email = RequestUtils.getParameter(request, "email");
		String mobile = RequestUtils.getParameter(request, "mobile");
		Long department = RequestUtils.getLongParameter(request, "department", 0l);// 部门id
		String roles = RequestUtils.getParameter(request, "roles");
		Integer identity = RequestUtils.getIntParameter(request, "identity", UserDepartmentAssign.IDENTITY_EMPLOYEE);// 默认职员
		String errorMsg = checkUserRequired(username, realname, mobile, email, roles, department, 0l, identity);
		if (!StringUtils.isEmpty(errorMsg)) {
			ResponseUtils.outputWithJson(response, Result.badResult(errorMsg));
			return;
		}
		String randomPwd = RandomNumberUtils.getNumbersAndLettersRandom(6);
		String password = PasswordUtils.encrypt(Md5.md5(randomPwd.getBytes()));
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result = userService.addUser(username, realname, password, email, mobile, roles, department, currentUser, User.USER_TYPE_EMAY, identity);
		if (result.getSuccess()) {
			String service = "公共服务";
			String context = "添加用户:{0}";
			String module = "用户管理";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { username }), ManageUserOperLog.OPERATE_ADD);
			log.info("用户:" + currentUser.getUsername() + "添加用户,用户名称为:" + username);
			result.setResult(randomPwd);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 修改用户
	 */
	@OperationAuth("OPER_SYS_USER_UPDATE")
	@RequestMapping("/ajax/modify")
	public void modify(HttpServletRequest request, HttpServletResponse response) {
		String username = RequestUtils.getParameter(request, "username");
		String realname = RequestUtils.getParameter(request, "realname");
		String email = RequestUtils.getParameter(request, "email");
		String mobile = RequestUtils.getParameter(request, "mobile");
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		Long departmentId = RequestUtils.getLongParameter(request, "departmentId", 0l);// 部门id
		String roles = RequestUtils.getParameter(request, "roles");
		Integer identity = RequestUtils.getIntParameter(request, "identity", UserDepartmentAssign.IDENTITY_EMPLOYEE);// 默认职员
		if (userId <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户不存在"));
			return;
		}
		User user = userService.findUserById(userId);
		if (user == null) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户不存在"));
			return;
		}
		if (!user.getUserType().equals(User.USER_TYPE_EMAY)) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户不存在"));
			return;
		}
		if (userId == 1l) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作系统管理员"));
			return;
		}
		String errorMsg = checkUserRequired(username, realname, mobile, email, roles, departmentId, userId, identity);
		if (!StringUtils.isEmpty(errorMsg)) {
			ResponseUtils.outputWithJson(response, Result.badResult(errorMsg));
			return;
		}
		Result result = userService.modifyUser(username, realname, email, mobile, roles, userId, departmentId, identity);
		User currentUser = WebUtils.getCurrentUser(request, response);
		if (result.getSuccess()) {
			String service = "公共服务";
			String context = "修改用户{0}";
			String module = "用户管理";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { username }), ManageUserOperLog.OPERATE_MODIFY);
			log.info("用户:" + currentUser.getUsername() + "修改用户,用户名称为:" + username);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 用户详细信息
	 */
	@RequestMapping("/ajax/detail")
	public void userinfo(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		Result result = check(userId);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		List<UserRoleAssign> userRoles = userRoleAssignService.findByUserId(userId);
		UserDepartmentAssign userDepartmentAssign = userDepartmentAssignService.findByUserId(userId);
		if (null != userDepartmentAssign) {
			UserDTO dto = new UserDTO((User) result.getResult(), userRoles, userDepartmentAssign.getIdentity());
			Map<String, Object> map = new HashMap<String, Object>();
			if (null != userDepartmentAssign) {
				Department department = departmentService.findDepartmentById(userDepartmentAssign.getDepartmentId());
				map.put("department", department);
			}
			map.put("user", dto);// 用户
			map.put("roleList", userRoles);// 角色
			ResponseUtils.outputWithJson(response, Result.rightResult(map));
		} else {
			ResponseUtils.outputWithJson(response, Result.badResult("获取用户部门关系失败！"));
		}
	}

	/**
	 * 重置密码
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@OperationAuth("OPER_SYS_USER_UPDATE")
	@RequestMapping(value = "/ajax/reset")
	public void resetPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		if (userId <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户不存在"));
			return;
		}
		if (userId == 1l) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作系统管理员"));
			return;
		}
		User users = WebUtils.getCurrentUser(request, response);
		if (users.getId().equals(userId)) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能重置自己"));
			return;
		}
		User user = userService.findUserById(userId);
		if (user == null) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户不存在"));
			return;
		}
		if (!user.getUserType().equals(User.USER_TYPE_EMAY)) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户不存在"));
			return;
		}
		Result result = userService.updateResetUserPassword(userId);
		User currentUser = WebUtils.getCurrentUser(request, response);
		if (result.getSuccess()) {
			String service = "公共服务";
			String context = "重置密码的用户:{0}";
			String module = "用户管理";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { user.getUsername() }), ManageUserOperLog.OPERATE_MODIFY);
			log.info("用户:" + currentUser.getUsername() + "重置用户密码,用户名称为:" + user.getUsername());
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 角色列表，不分页
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/list")
	public void rolelist(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Role> manageList = roleService.findAll(EucpSystem.管理系统.getCode());
		List<Role> opersList = roleService.findAll(EucpSystem.运维系统.getCode());
		List<Role> salesList = roleService.findAll(EucpSystem.销售系统.getCode());
		map.put("manager", manageList);
		map.put("opers", opersList);
		map.put("sales", salesList);
		ResponseUtils.outputWithJson(response, Result.rightResult(map));
	}

	/**
	 * 部门树形
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/getTree")
	public void getTree(HttpServletRequest request, HttpServletResponse response) {
		List<Department> list = departmentService.findByParentId(0l, Department.MANAGE_ENTERPRISEID);
		ResponseUtils.outputWithJson(response, Result.rightResult(list));
	}

	/**
	 * 展开子节点
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/showChildrenNode")
	public void showChildrenNode(HttpServletRequest request, HttpServletResponse response) {
		long id = RequestUtils.getLongParameter(request, "id", 0l);
		if (id <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("请选择部门"));
			return;
		}
		List<Department> childrenNode = departmentService.findByParentId(id, Department.MANAGE_ENTERPRISEID);
		ResponseUtils.outputWithJson(response, Result.rightResult(childrenNode));
	}

	/**
	 * 校验用户信息
	 */
	private String checkUserRequired(String username, String nickname, String mobile, String email, String roles, Long department, Long userId, Integer identity) {
		String errorMsg = "";
		if (StringUtils.isEmpty(username)) {
			errorMsg = "用户名不能为空";
			return errorMsg;
		}
		if (username.length() < 4 || username.length() > 16) {
			errorMsg = "用户名长度为4-16个字符";
			return errorMsg;
		}
		if (!RegularUtils.notExistSpecial(username)) {
			errorMsg = "用户名不能包含特殊字符";
			return errorMsg;
		}
		username = username.toLowerCase();
		if (!RegularUtils.checkUserName(username)) {
			errorMsg = "请输入正确的用户名";
			return errorMsg;
		}
		if (StringUtils.isEmpty(nickname)) {
			errorMsg = "姓名不能为空";
			return errorMsg;
		}
		if (nickname.length() > 10) {
			errorMsg = "姓名不能超过10个字符";
			return errorMsg;
		}
		if (!RegularUtils.notExistSpecial(nickname)) {
			errorMsg = "姓名不能包含特殊字符";
			return errorMsg;
		}
		if (!RegularUtils.checkString(nickname)) {
			errorMsg = "请输入正确的姓名";
			return errorMsg;
		}
		if (StringUtils.isEmpty(email)) {
			errorMsg = "邮箱不能为空";
			return errorMsg;
		}
		if (!RegularUtils.checkEmail(email)) {
			errorMsg = "请输入正确的邮箱";
			return errorMsg;
		}
		if (!RegularUtils.checkMobileFormat(mobile)) {
			errorMsg = "手机号码格式不正确";
			return errorMsg;
		}
		if (StringUtils.isEmpty(roles)) {
			errorMsg = "角色不能为空";
			return errorMsg;
		}
		if (department <= 0) {
			errorMsg = "请选择部门";
			return errorMsg;
		}
		Department dep = departmentService.findDepartmentById(department);
		if (null == dep) {
			errorMsg = "部门不存在";
			return errorMsg;
		}
		if (!dep.getEnterpriseId().equals(Department.MANAGE_ENTERPRISEID)) {
			errorMsg = "部门不存在";
			return errorMsg;
		}
		Long userCount = userService.countByUserName(userId, username);
		if (userCount > 0) {
			errorMsg = "用户名已存在";
			return errorMsg;
		}
		if (UserDepartmentAssign.IDENTITY_LEADER != identity && UserDepartmentAssign.IDENTITY_EMPLOYEE != identity) {
			errorMsg = "用户身份错误";
			return errorMsg;
		}
		// Long mobileCount = userService.countByMobile(userId, mobile);
		// if (mobileCount > 0) {
		// errorMsg = "手机号已存在";
		// return errorMsg;
		// }
		// Long emailCount = userService.countByEmail(userId, email);
		// if (emailCount > 0) {
		// errorMsg = "邮箱已存在";
		// return errorMsg;
		// }
		return errorMsg;
	}

	private Result check(Long userId) {
		if (userId <= 0l) {
			return Result.badResult("用户不存在");
		}
		User user = userService.findUserById(userId);
		if (user == null) {
			return Result.badResult("用户不存在");
		}
		if (!user.getUserType().equals(User.USER_TYPE_EMAY)) {
			return Result.badResult("用户不存在");
		}
		return Result.rightResult(user);
	}

}
