package cn.emay.eucp.web.manage.controller.sys.auth;

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
import cn.emay.eucp.common.constant.EucpSystem;
import cn.emay.eucp.common.dto.auth.AuthTree;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.Role;
import cn.emay.eucp.common.moudle.db.system.RoleResourceAssign;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.data.service.system.ResourceService;
import cn.emay.eucp.data.service.system.RoleResourceAssignService;
import cn.emay.eucp.data.service.system.RoleService;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 角色管理
 * 
 * @author Frank
 *
 */
@PageAuth("SYS_AUTH_ROLE")
@RequestMapping("/sys/auth/role")
@Controller
public class RoleController {

	private static Logger log = Logger.getLogger(RoleController.class);
	
	@Resource(name = "roleService")
	private RoleService roleService;

	@Resource(name = "roleResourceAssignService")
	private RoleResourceAssignService roleResourceAssignService;

	@Resource(name = "resourceService")
	private ResourceService resourceService;

	@Resource(name = "manageUserOperLogService")
	private ManageUserOperLogService manageUserOperLogService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		String system = RequestUtils.getParameter(request, "system");
		if(EucpSystem.findNameByCode(system) == null ){
			system = EucpSystem.管理系统.getCode();
		}
		request.setAttribute("system", system);
		return "sys/auth/role";
	}
	
	@OperationAuth("OPER_SYS_USER_ROLE_ADD")
	@RequestMapping("/add")
	public String add(HttpServletRequest request, HttpServletResponse response, Model model) {
		String system = RequestUtils.getParameter(request, "system");
		if(EucpSystem.findNameByCode(system) == null ){
			system = EucpSystem.管理系统.getCode();
		}
		request.setAttribute("system", system);
		return "sys/auth/add";
	}
	
	@OperationAuth("OPER_SYS_USER_ROLE_UPDATE")
	@RequestMapping("/modify")
	public String modify(HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		Long roleId = RequestUtils.getLongParameter(request, "roleId", 0l);
		request.setAttribute("roleId", roleId);
		Role role = roleService.findRoleById(roleId);
		if (role == null) {
			redirectAttributes.addFlashAttribute("msg", "角色不存在");
			return "redirect:" + WebUtils.getLocalAddress(request) + "/error";
		}
		request.setAttribute("system", role.getRoleType());
		return "sys/auth/modify";
	}
	
	/**
	 * 所有权限
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/getTree")
	public void getTree(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		String roleType = RequestUtils.getParameter(request, "roleType");
		if(roleType == null){
			ResponseUtils.outputWithJson(response, Result.rightResult(map));
			return;
		}
		AuthTree dto = resourceService.findSystemAuthTree(roleType);
		map.put("allauth", dto);
		ResponseUtils.outputWithJson(response, Result.rightResult(map));
	}
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/ajax/list")
	public void rolelist(HttpServletRequest request, HttpServletResponse response) {
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String roleName = RequestUtils.getParameter(request, "roleName");
		String roleType = RequestUtils.getParameter(request, "roleType");
		Page<Role> rolepage = roleService.findRolePage(start, limit, roleName,roleType);
		ResponseUtils.outputWithJson(response, Result.rightResult(rolepage));
	}
	
	/**
	 * 删除角色
	 */
	@OperationAuth("OPER_SYS_USER_ROLE_DELETE")
	@RequestMapping("/ajax/delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		Long roleId = RequestUtils.getLongParameter(request, "roleId", 0l);
		if (roleId <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("角色不存在"));
			return;
		}
		if (roleId == 1l) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作系统管理员"));
			return;
		}
		if (roleId == 2l) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作客户管理员"));
			return;
		}
		if (roleId == 3l) {
			ResponseUtils.outputWithJson(response, Result.badResult("不能操作销售管理员"));
			return;
		}
		Result result = roleService.deleteRoleById(roleId);
		User currentUser = WebUtils.getCurrentUser(request, response);
		if (result.getSuccess()) {
			String service = "公共服务";
			String context = "删除角色:{0}";
			String module = "角色管理";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] {result.getResult() }), ManageUserOperLog.OPERATE_DELETE);
			log.info("用户:"+currentUser.getUsername()+"删除角色,角色名称为:"+result.getResult());
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 添加角色
	 */
	@OperationAuth("OPER_SYS_USER_ROLE_ADD")
	@RequestMapping("/ajax/add")
	public void add(HttpServletRequest request, HttpServletResponse response) {
		String auths = RequestUtils.getParameter(request, "auths");
		String roleName = RequestUtils.getParameter(request, "roleName");
		String remark = RequestUtils.getParameter(request, "remark");
		String errorMsg=checkData(roleName,auths,remark,0l);
		if(!StringUtils.isEmpty(errorMsg)){
			ResponseUtils.outputWithJson(response, Result.badResult(errorMsg));
			return;
		}
		String roleType=RequestUtils.getParameter(request, "roleType");
		Result result = roleService.addRole(roleName, auths, remark,roleType);
		User currentUser = WebUtils.getCurrentUser(request, response);
		if (result.getSuccess()) {
			String service = "公共服务";
			String context = "添加角色:{0}";
			String module = "角色管理";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] {roleName}), ManageUserOperLog.OPERATE_ADD);
			log.info("用户:"+currentUser.getUsername()+"添加角色,角色名称为:"+roleName);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 修改角色
	 */
	@OperationAuth("OPER_SYS_USER_ROLE_UPDATE")
	@RequestMapping("/ajax/modify")
	public void modify(HttpServletRequest request, HttpServletResponse response) {
		String auths = RequestUtils.getParameter(request, "auths");
		String roleName = RequestUtils.getParameter(request, "roleName");
		String remark = RequestUtils.getParameter(request, "remark");
		Long roleId = RequestUtils.getLongParameter(request, "roleId", 0l);
		if (roleId <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("角色不存在"));
			return;
		}
		if (roleId == 1l) {
			ResponseUtils.outputWithJson(response, Result.badResult("系统管理员不允许修改"));
			return;
		}
		String errorMsg=checkData(roleName,auths,remark,roleId);
		if(!StringUtils.isEmpty(errorMsg)){
			ResponseUtils.outputWithJson(response, Result.badResult(errorMsg));
			return;
		}
		Result result = roleService.modifyRole(roleId, roleName, auths, remark);
		User currentUser = WebUtils.getCurrentUser(request, response);
		if (result.getSuccess()) {
			String service = "公共服务";
			String context = "修改角色:{0}";
			String module = "角色管理";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] { roleName }), ManageUserOperLog.OPERATE_MODIFY);
			log.info("用户:"+currentUser.getUsername()+"修改角色,角色名称为:"+roleName);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 角色所有权限
	 */
	@RequestMapping("/ajax/roleauth")
	public void userauth(HttpServletRequest request, HttpServletResponse response) {
		Long roleId = RequestUtils.getLongParameter(request, "roleId", 0l);
		if (roleId <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("角色不存在"));
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Role role = roleService.findById(roleId);
		if (role == null) {
			ResponseUtils.outputWithJson(response, Result.badResult("角色不存在"));
			return;
		}
		List<RoleResourceAssign> list = roleResourceAssignService.findAllAuthByRoleId(roleId);
		map.put("role", role);
		map.put("list", list);
		ResponseUtils.outputWithJson(response, Result.rightResult(map));
	}
	
	private String checkData(String roleName,String auths,String remark,Long roleId){
		String errorMsg = "";
		if (StringUtils.isEmpty(roleName)) {
			errorMsg = "角色名不能为空";
			return errorMsg;
		}
		
		if (StringUtils.isEmpty(auths)) {
			errorMsg = "权限不能为空";
			return errorMsg;
		}
		
		if (roleName.length() > 20) {
			errorMsg = "角色名称长度不可超过20个字符";
			return errorMsg;
		}
		
		if (!StringUtils.isEmpty(remark)&&remark.length() > 50) {
			errorMsg = "角色描述不能超过50个字符";
			return errorMsg;
		}
		
		if (!RegularUtils.notExistSpecial(roleName)) {
			errorMsg = "角色名称不合法";
			return errorMsg;
		}
		Long count = roleService.countNumberByRoleName(roleName, roleId);
		// 角色名称唯一校验
		if (count > 0) {
			errorMsg = "角色名已存在";
			return errorMsg;
		}
		return errorMsg;
	}

}
