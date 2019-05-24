package cn.emay.eucp.web.client.controller.sys;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.common.encryption.Md5;
import cn.emay.eucp.common.dto.user.UserDTO;
import cn.emay.eucp.common.dto.user.UserRoleDTO;
import cn.emay.eucp.common.moudle.db.system.Business;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.Role;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign;
import cn.emay.eucp.common.moudle.db.system.UserRoleAssign;
import cn.emay.eucp.data.service.system.BusinessService;
import cn.emay.eucp.data.service.system.ClientUserOperLogService;
import cn.emay.eucp.data.service.system.EnterpriseService;
import cn.emay.eucp.data.service.system.RoleService;
import cn.emay.eucp.data.service.system.UserDepartmentAssignService;
import cn.emay.eucp.data.service.system.UserRoleAssignService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.util.PasswordUtils;
import cn.emay.eucp.util.RandomNumberUtils;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;


/**
 * 用户信息
 * 
 * @author Shmily
 *
 */
@PageAuth("SYS_CLIENT_ADMINISTRA")
@RequestMapping("/sys/client/administrate")
@Controller
public class SysClientUserAdministrateController {
	private static Logger log = Logger.getLogger(SysClientUserAdministrateController.class);

	@Resource(name = "userService")
	private UserService userService;
	@Resource
	private RoleService roleService;
	@Resource(name = "enterpriseService")
	private EnterpriseService enterpriseService;
	@Resource
	private UserRoleAssignService userRoleAssignService;
	@Resource
	private ClientUserOperLogService clientUserOperLogService;
	@Resource
	private UserDepartmentAssignService userDepartmentAssignService;
	@Resource(name = "businessService")
	private BusinessService businessService;
	
	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		User currentUser = WebUtils.getCurrentUser(request, response);
		User user = userService.findUserById(currentUser.getId());
		model.addAttribute("user", user);
		return "sys/user/clientuser";
	}

	/**
	 *
	 * 分页查询全部
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("findall")
	public void findall(HttpServletRequest request, HttpServletResponse response, Model model) {
		int start = RequestUtils.getIntParameter(request, "start", 1);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String username = RequestUtils.getParameter(request, "username");
		String realname = RequestUtils.getParameter(request, "realname");
		String mobile = RequestUtils.getParameter(request, "mobile");
		User user = WebUtils.getCurrentUser(request, response);
		Page<UserDTO> page = userService.findall(start, limit, username, realname, mobile, user.getId());
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

	/**
	 *
	 * 停用 启用
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_ON_OFF")
	@RequestMapping("updatestate")
	public void updateState(HttpServletRequest request, HttpServletResponse response) {
		int state = RequestUtils.getIntParameter(request, "state", 0);
		long id = RequestUtils.getLongParameter(request, "userId", 0l);
		if (StringUtils.isEmpty(state)) {
			ResponseUtils.outputWithJson(response, Result.badResult("状态不能为空"));
			return;
		}
		if (id <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户不存在"));
			return;
		}
		User user = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(id, user.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		if (user.getId().equals(id)) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作自己"));
			return;
		}
		UserDepartmentAssign userDepartmentAssign = userDepartmentAssignService.findByUserId(id);
		if (userDepartmentAssign.getIdentity() == 1) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作系统管理员"));
			return;
		}
		userService.updatestate(id, state);
		User user2 = userService.findUserById(id);
		if (state == 1) {
			log.info("user : " + user.getUsername() + "停用, 用户名为： : " + user2.getUsername());
			clientUserOperLogService.save("系统服务", "用户管理", user, "停用用户,用户名为：" + user2.getUsername(), ManageUserOperLog.OPERATE_MODIFY);
		}
		if (state == 2) {
			log.info("user : " + user.getUsername() + "启用 , 用户名为： " + user2.getUsername());
			clientUserOperLogService.save("系统服务", "用户管理", user, "启用用户,用户名为：" + user2.getUsername(), ManageUserOperLog.OPERATE_MODIFY);
		}
		ResponseUtils.outputWithJson(response, Result.rightResult("操作成功"));
	}

	/*
	 * 
	 * 删除
	 */
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_DETELE")
	@RequestMapping("delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		int state = RequestUtils.getIntParameter(request, "state", 0);
		long id = RequestUtils.getLongParameter(request, "userId", 0l);
		if (StringUtils.isEmpty(state)) {
			ResponseUtils.outputWithJson(response, Result.badResult("状态不能为空"));
			return;
		}
		if (id <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户不存在"));
			return;
		}
		User user = WebUtils.getCurrentUser(request, response);
		if (user.getId().equals(id)) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作自己"));
			return;
		}
		UserDepartmentAssign userDepartmentAssign = userDepartmentAssignService.findByUserId(id);
		if (userDepartmentAssign.getIdentity() == 1) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作管理员"));
			return;
		}
		// // 判断是否为预警接收人
		// Boolean flag = smsChannelWarningConfigService.isWarningUser(id);
		// if (flag) {
		// ResponseUtils.outputWithJson(response, Result.badResult("用户为预警接收人员，不能删除"));
		// return;
		// }
		// flag = smsServiceCodeWarningReceiverService.isWarningUser(id);
		// if (flag) {
		// ResponseUtils.outputWithJson(response, Result.badResult("用户为预警接收人员，不能删除"));
		// return;
		// }
		//
		// flag = smsServiceCodeDefaultRuleService.isWarningUser(user.getUsername());
		// if (flag) {
		// ResponseUtils.outputWithJson(response,
		// Result.badResult("用户为服务号默认规则预警接收人员，不能删除"));
		// return;
		// }
		Result result2 = userService.findenterpriseId(id, user.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		User user2 = userService.findUserById(id);
		if (user2.getState() == User.STATE_DELETE) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户已被删除,请勿重复操作"));
			return;
		}
		userService.updatestate(id, state);
		log.info("user : " + user.getUsername() + "删除 ,用户名为: " + user2.getUsername());
		clientUserOperLogService.save("系统服务", "用户管理", user, "删除,用户名为：" + user2.getUsername(), ManageUserOperLog.OPERATE_DELETE);
		ResponseUtils.outputWithJson(response, Result.rightResult("操作成功"));
	}

	/**
	 *
	 * 查询 角色名
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("findrolename")
	public void findrolename(HttpServletRequest request, HttpServletResponse response) {
		List<Role> list = roleService.findAllname();
		ResponseUtils.outputWithJson(response, Result.rightResult(list));

	}

	/**
	 * 
	 * 增加跳转
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_ADD")
	@RequestMapping("toadd")
	public String toadd(HttpServletRequest request, HttpServletResponse response) {
		return "sys/user/adduser";

	}

	/**
	 *
	 * 增加
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_ADD")
	@RequestMapping("add")
	public void save(HttpServletRequest request, HttpServletResponse response) {
		String username = RequestUtils.getParameter(request, "username");
		String realname = RequestUtils.getParameter(request, "realname");
		String mobile = RequestUtils.getParameter(request, "mobile");
		String email = RequestUtils.getParameter(request, "email");
		String roleids = RequestUtils.getParameter(request, "roleids");
		User currentUser = WebUtils.getCurrentUser(request, response);
		Long usernumber = userDepartmentAssignService.findUsernumber(currentUser.getId());
		Result result = userService.addverify(username, realname, mobile, email, usernumber, roleids);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		result = userService.addClientUser(username, realname, mobile, email, roleids, currentUser.getId());
		log.info("user : " + currentUser.getUsername() + "增加用户名为：" + username);
		clientUserOperLogService.save("系统服务", "用户管理", currentUser, "增加用户,用户名为：" + username, ManageUserOperLog.OPERATE_ADD);
		ResponseUtils.outputWithJson(response, Result.rightResult(result.getResult()));
	}

	/**
	 * 修改跳转路径
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_UPDATE")
	@RequestMapping("toupdate/{id}")
	public String toupdate(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id) {
		request.setAttribute("id", id);
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(id, currentUser.getId());
		if (!result2.getSuccess()) {
			return "redirect:" + WebUtils.getLocalAddress(request) + "/error";
		}
		return "sys/user/updateuser";
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_UPDATE")
	@RequestMapping("update")
	public void update(HttpServletRequest request, HttpServletResponse response) {
		long id = RequestUtils.getLongParameter(request, "id", 0l);
		String realname = RequestUtils.getParameter(request, "realname");
		String mobile = RequestUtils.getParameter(request, "mobile");
		String email = RequestUtils.getParameter(request, "email");
		String roleids = RequestUtils.getParameter(request, "roleids");
		User currentUser = WebUtils.getCurrentUser(request, response);
		UserDepartmentAssign userDepartmentAssign = userDepartmentAssignService.findByUserId(id);
		if (userDepartmentAssign.getIdentity() == 1) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作管理员"));
			return;
		}
		Result result2 = userService.findenterpriseId(id, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		Result result = userService.updateverify(realname, mobile, email, id, roleids);
		if (!result.getSuccess()) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		User user = userService.findUserById(id);
		user.setRealname(realname);
		user.setEmail(email);
		user.setMobile(mobile);
		userService.update(user);
		List<Role> role = roleService.findByIds(roleids);
		userRoleAssignService.deleteByUserId(user.getId());
		for (Role role2 : role) {
			UserRoleAssign userRoleAssign2 = new UserRoleAssign();
			userRoleAssign2.setRoleId(role2.getId());
			userRoleAssign2.setUserId(user.getId());
			userRoleAssignService.add(userRoleAssign2);
		}
		User findBymobile = userService.findBymobile(mobile);
		log.info("user : " + currentUser.getUsername() + "修改用户为：" + user.getUsername());
		clientUserOperLogService.save("系统服务", "用户管理", currentUser, "修改用户,用户名为：" + findBymobile.getUsername(), ManageUserOperLog.OPERATE_MODIFY);
		ResponseUtils.outputWithJson(response, Result.rightResult("操作成功"));
	}

	/**
	 *
	 * 重置密码
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_RESET")
	@RequestMapping("updatepwd")
	public void updatePassword(HttpServletRequest request, HttpServletResponse response) {
		long id = RequestUtils.getLongParameter(request, "userId", 0l);
		User user = userService.findUserById(id);
		User currentUser = WebUtils.getCurrentUser(request, response);
		if (currentUser.getId().equals(id)) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作自己"));
			return;
		}
		Result result2 = userService.findenterpriseId(id, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}

		UserDepartmentAssign userDepartmentAssign = userDepartmentAssignService.findByUserId(id);
		if (userDepartmentAssign.getIdentity() == '1') {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作管理员"));
			return;
		}
		String randomPwd = RandomNumberUtils.getNumbersAndLettersRandom(6);
		String newpwd = PasswordUtils.encrypt(Md5.md5(randomPwd.getBytes()));
		userService.updatepassword(newpwd, id);
		log.info("user : " + currentUser.getUsername() + "重置密码,用户名为:" + user.getUsername());
		clientUserOperLogService.save("系统服务", "用户管理", currentUser, "重置密码,用户名为：" + user.getUsername(), ManageUserOperLog.OPERATE_MODIFY);
		ResponseUtils.outputWithJson(response, Result.rightResult(randomPwd));
	}

	/**
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("todetail/{id}")
	public String todetail(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id) {
		request.setAttribute("id", id);
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(id, currentUser.getId());
		if (!result2.getSuccess()) {
			return "redirect:" + WebUtils.getLocalAddress(request) + "/error";
		}
		return "sys/user/detailuser";
	}

	/**
	 * 查看详情
	 * 
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_DETAIL")
	@RequestMapping("findId")
	public void finduserandRole(HttpServletRequest request, HttpServletResponse response) {
		long id = RequestUtils.getLongParameter(request, "id", 0l);
		if (null == Long.valueOf(id)) {
			ResponseUtils.outputWithJson(response, Result.badResult("id不能为空"));
			return;
		}
		if (id <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("用户不存在"));
			return;
		}
		UserDepartmentAssign userDepartmentAssign = userDepartmentAssignService.findByUserId(id);
		if (userDepartmentAssign.getIdentity() == 1) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作管理员"));
			return;
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(id, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		List<UserRoleDTO> finduserAllRole = userService.finduserAllRole(id);
		List<UserRoleDTO> userRoleDTO = new ArrayList<UserRoleDTO>();
		if (finduserAllRole.isEmpty() || finduserAllRole.size() <= 0) {
			UserRoleDTO dto = new UserRoleDTO();
			User user = userService.findUserById(id);
			dto.setId(user.getId());
			dto.setRealname(user.getRealname());
			dto.setEmail(user.getEmail());
			dto.setMobile(user.getMobile());
			userRoleDTO.add(dto);
		} else {
			StringBuffer buffer1 = new StringBuffer();
			StringBuffer buffer2 = new StringBuffer();
			StringBuffer buffer3 = new StringBuffer();
			for (UserRoleDTO object : finduserAllRole) {
				if (!StringUtils.isEmpty(object.getRoleName())) {
					buffer1.append(object.getRoleName());
					buffer1.append(",");
					String remark = object.getRemark();
					if (StringUtils.isEmpty(remark)) {
						remark = "";
					}
					buffer3.append(remark);
					buffer3.append(",");
				}
				if (!StringUtils.isEmpty(object.getRoleid())) {
					buffer2.append(object.getRoleid());
					buffer2.append(",");
				}
			}
			UserRoleDTO dto = new UserRoleDTO();
			dto.setId(finduserAllRole.get(0).getId());
			dto.setRealname(finduserAllRole.get(0).getRealname());
			dto.setEmail(finduserAllRole.get(0).getEmail());
			dto.setMobile(finduserAllRole.get(0).getMobile());
			dto.setUsername(finduserAllRole.get(0).getUsername());
			dto.setRoleName(buffer1.toString());
			dto.setRid(buffer2.toString());
			dto.setRemark(buffer3.toString());
			userRoleDTO.add(dto);
		}
		ResponseUtils.outputWithJson(response, Result.rightResult(userRoleDTO));
	}
	/**************************************账号绑定相关方法begin*******************************************/
	/**
	 *
	 * 绑定帐号
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_BIND")
	@RequestMapping("account/{userId}")
	public String account(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") Long userId) {
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			return "redirect:" + WebUtils.getLocalAddress(request) + "/error";
		}
		request.setAttribute("userId", userId);
		User user = userService.findUserById(userId);
		String type = userService.findenterpriseTypebyUserId(userId);
		request.setAttribute("type", type);
		request.setAttribute("realname", user.getRealname());
		request.setAttribute("userName", user.getUsername());
		return "sys/user/servicebind";// 帐号绑定路径
	}
	
	/**
	 * 查看客户账号详情--已开通的服务信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/getBusinessList")
	public void getBusinessList(HttpServletRequest request, HttpServletResponse response) {
		if (!WebUtils.isLogin(request, response)) {
			ResponseUtils.outputWithJson(response, Result.badResult("请重新登录"));
		}
		User user = WebUtils.getCurrentUser(request, response);
		Enterprise enterprise = enterpriseService.findByUserId(user.getId());
		List<Business> businessList = businessService.findUserOpenBusinessList(enterprise.getServiceType());
		ResponseUtils.outputWithJson(response, Result.rightResult(businessList));
	}

	/**
	 * 绑定流量服务号
	 * 
	 * @param request
	 * @param response
	 */
	/*
	 * TODO@RequestMapping("/ajax/findFlowServiceCode")
	 * 
	 * @OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_BIND") public void
	 * findFlowServiceCode(HttpServletRequest request, HttpServletResponse response)
	 * { User currentUser = WebUtils.getCurrentUser(request, response); Long userId
	 * = RequestUtils.getLongParameter(request, "userId", 0l); Result result2 =
	 * userService.findenterpriseId(userId, currentUser.getId()); if
	 * (!result2.getSuccess()) { ResponseUtils.outputWithJson(response, result2);
	 * return; } long enterpriseId = userService.findenterpriseIdbyUser(userId); if
	 * (enterpriseId == 0l) { ResponseUtils.outputWithJson(response,
	 * Result.badResult("参数不正确")); return; } Map<String, Object> map =
	 * flowUserSCAssignService.findBinding(userId, enterpriseId);
	 * ResponseUtils.outputWithJson(response, Result.rightResult(map)); }
	 */

	/**
	 * 绑定国际短信服务号
	 * 
	 * @param request
	 * @param response
	 */
	/*
	 * TODO@RequestMapping("/ajax/findImsServiceCode")
	 * 
	 * @OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_BIND") public void
	 * findImsServiceCode(HttpServletRequest request, HttpServletResponse response)
	 * { User currentUser = WebUtils.getCurrentUser(request, response); Long userId
	 * = RequestUtils.getLongParameter(request, "userId", 0l); Result result2 =
	 * userService.findenterpriseId(userId, currentUser.getId()); if
	 * (!result2.getSuccess()) { ResponseUtils.outputWithJson(response, result2);
	 * return; } Map<String, Object> map =
	 * imsUserServiceCodeAssignService.findBinding(userId,
	 * (Long)result2.getResult()); ResponseUtils.outputWithJson(response,
	 * Result.rightResult(map)); }
	 */
	
	/**
	 * 绑定语音短信服务号
	 * 
	 * @param request
	 * @param response
	 */
	/*
	 * TODO@RequestMapping("/ajax/findVoiceServiceCode")
	 * 
	 * @OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_BIND") public void
	 * findVoiceServiceCode(HttpServletRequest request, HttpServletResponse
	 * response) { User currentUser = WebUtils.getCurrentUser(request, response);
	 * Long userId = RequestUtils.getLongParameter(request, "userId", 0l); Result
	 * result2 = userService.findenterpriseId(userId, currentUser.getId()); if
	 * (!result2.getSuccess()) { ResponseUtils.outputWithJson(response, result2);
	 * return; } long enterpriseId = userService.findenterpriseIdbyUser(userId);
	 * Map<String, Object> map = voiceServiceCodeService.findBinding(userId,
	 * enterpriseId); ResponseUtils.outputWithJson(response,
	 * Result.rightResult(map)); }
	 */
	
	/**
	 * 用户绑定流量服务号操作
	 * 
	 * @param request
	 * @param response
	 */
	/*TODO@RequestMapping("/ajax/bindFlowServiceCode")
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_BIND")
	public void modifyBindFlowServiceCode(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		String flowServiceCodeIds = RequestUtils.getParameter(request, "flowServiceCodeIds", "");
		if (RegularUtils.isEmpty(flowServiceCodeIds)) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		Long enterpriseId = userService.findenterpriseIdbyUser(userId);
		if (enterpriseId == null || enterpriseId.longValue() == 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		Result result = flowUserSCAssignService.modifyUserSCAssign(userId, enterpriseId, flowServiceCodeIds);
		if (result.getSuccess()) {
			User user = userService.findById(userId);
			// userService.updateLastUpdateTimeById(String.valueOf(userId.longValue()));
			String service = "系统服务";
			String module = "账号绑定";
			String context = "修改{0}的绑定流量服务号";
			clientUserOperLogService.save(service, module, currentUser, MessageFormat.format(context, new Object[] { user.getUsername() }), ClientUserOperLog.OPERATE_MODIFY);
			log.info("【系统服务>账号绑定】-->用户:" + currentUser.getUsername() + "为:" + result.getResult() + "修改绑定流量服务号");
		}
		ResponseUtils.outputWithJson(response, result);
	}*/

	/**
	 * 用户绑定国际短信服务号操作
	 * 
	 * @param request
	 * @param response
	 */
	/*TODO@RequestMapping("/ajax/bindImsServiceCode")
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_BIND")
	public void modifyBindImsServiceCode(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		String imsServiceCodeIds = RequestUtils.getParameter(request, "imsServiceCodeIds", "");
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		Result result = imsUserServiceCodeAssignService.modifyImsUserSCAssign(userId, (Long)result2.getResult(), imsServiceCodeIds);
		if (result.getSuccess()) {
			User user = userService.findById(userId);
			// userService.updateLastUpdateTimeById(String.valueOf(userId.longValue()));
			String service = "系统服务";
			String module = "账号绑定";
			String context = "修改{0}的绑定国际短信服务号";
			clientUserOperLogService.save(service, module, currentUser, MessageFormat.format(context, new Object[] { user.getUsername() }), ClientUserOperLog.OPERATE_MODIFY);
			log.info("【系统服务>账号绑定】-->用户:" + currentUser.getUsername() + "为:" + result.getResult() + "修改绑定国际短信服务号");
		}
		ResponseUtils.outputWithJson(response, result);
	}*/
	
	/**
	 * 用户绑定语音短信服务号操作
	 * 
	 * @param request
	 * @param response
	 */
	/*TODO@RequestMapping("/ajax/bindVoiceServiceCode")
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_BIND")
	public void bindVoiceServiceCode(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		String VoiceServiceCodeIds = RequestUtils.getParameter(request, "VoiceServiceCodeIds", "");
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		Long enterpriseId = userService.findenterpriseIdbyUser(userId);
		Result result = voiceServiceCodeService.modifyVoiceUserSCAssign(userId,enterpriseId, VoiceServiceCodeIds);
		if (result.getSuccess()) {
			User user = userService.findById(userId);
			// userService.updateLastUpdateTimeById(String.valueOf(userId.longValue()));
			String service = "系统服务";
			String module = "账号绑定";
			String context = "修改{0}的绑定语音服务号";
			clientUserOperLogService.save(service, module, currentUser, MessageFormat.format(context, new Object[] { user.getUsername() }), ClientUserOperLog.OPERATE_MODIFY);
			log.info("【系统服务>账号绑定】-->用户:" + currentUser.getUsername() + "为:" + result.getResult() + "修改绑定语音服务号");
		}
		ResponseUtils.outputWithJson(response, result);
	}*/
	


	/**
	 * 用户解除单个流量服务号的绑定关系
	 * 
	 * @param request
	 * @param response
	 */
	/*TODO@RequestMapping("/ajax/deleteFlowServiceCode")
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_BIND")
	public void deleteFlowServiceCode(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		Long flowServiceCodeId = RequestUtils.getLongParameter(request, "flowServiceCodeId", 0l);
		// 校验当前用户和解除绑定关系的用户是否属于同一客户
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		if (flowServiceCodeId == null || flowServiceCodeId.longValue() == 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		Result result = flowUserSCAssignService.deleteBind(userId, flowServiceCodeId);
		if (result.getSuccess()) {
			// userService.updateLastUpdateTimeById(String.valueOf(userId.longValue()));
			User user = userService.findUserById(userId);
			FlowServiceCode flowServiceCode = flowServiceCodeService.findById(flowServiceCodeId);
			String service = "系统服务";
			String module = "账号绑定";
			String context = "解除【" + user.getUsername() + "】关联的流量服务号:【" + flowServiceCode.getServiceCode() + "】";
			clientUserOperLogService.save(service, module, currentUser, context, ClientUserOperLog.OPERATE_MODIFY);
			log.info("【系统服务>账号绑定】-->" + context);
		}
		ResponseUtils.outputWithJson(response, result);
	}*/
	
	/**
	 * 用户解除单个国际短信服务号的绑定关系
	 * 
	 * @param request
	 * @param response
	 */
	/*TODO@RequestMapping("/ajax/deleteImsServiceCode")
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_BIND")
	public void deleteImsServiceCode(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		Long imsServiceCodeId = RequestUtils.getLongParameter(request, "imsServiceCodeId", 0l);
		// 校验当前用户和解除绑定关系的用户是否属于同一客户
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		if (imsServiceCodeId == null || imsServiceCodeId.longValue() == 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		Result result = imsUserServiceCodeAssignService.deleteImsBind(userId, imsServiceCodeId);
		if (result.getSuccess()) {
			// userService.updateLastUpdateTimeById(String.valueOf(userId.longValue()));
			User user = userService.findUserById(userId);
			ImsServiceCode imsServiceCode = imsServiceCodeService.findById(imsServiceCodeId);
			String service = "系统服务";
			String module = "账号绑定";
			String context = "解除【" + user.getUsername() + "】关联的国际短信服务号:【" + imsServiceCode.getServiceCode() + "】";
			clientUserOperLogService.save(service, module, currentUser, context, ClientUserOperLog.OPERATE_MODIFY);
			log.info("【系统服务>账号绑定】-->" + context);
		}
		ResponseUtils.outputWithJson(response, result);
	}*/
	
	/**
	 * 用户解除单个语音短信服务号的绑定关系
	 * 
	 * @param request
	 * @param response
	 */
	/*TODO@RequestMapping("/ajax/deleteVoiceServiceCode")
	@OperationAuth("OPER_SYS_CLIENT_ADMINISTRA_BIND")
	public void deleteVoiceServiceCode(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		Long voiceServiceCodeId = RequestUtils.getLongParameter(request, "voiceServiceCodeId", 0l);
		// 校验当前用户和解除绑定关系的用户是否属于同一客户
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		if (voiceServiceCodeId == null || voiceServiceCodeId.longValue() == 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}
		Result result = voiceServiceCodeService.deleteVoiceBind(userId, voiceServiceCodeId);
		if (result.getSuccess()) {
			// userService.updateLastUpdateTimeById(String.valueOf(userId.longValue()));
			User user = userService.findUserById(userId);
			VoiceServiceCode voiceServiceCode = voiceServiceCodeService.findById(voiceServiceCodeId);
			String service = "系统服务";
			String module = "账号绑定";
			String context = "解除【" + user.getUsername() + "】关联的语音服务号:【" + voiceServiceCode.getAppId() + "】";
			clientUserOperLogService.save(service, module, currentUser, context, ClientUserOperLog.OPERATE_MODIFY);
			log.info("【系统服务>账号绑定】-->" + context);
		}
		ResponseUtils.outputWithJson(response, result);
	}*/
	
	/**************************************账号绑定相关方法end*******************************************/
}
