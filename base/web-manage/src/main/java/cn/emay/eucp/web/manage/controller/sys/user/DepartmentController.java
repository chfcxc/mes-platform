package cn.emay.eucp.web.manage.controller.sys.user;

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
import cn.emay.eucp.common.dto.department.DepartmentDTO;
import cn.emay.eucp.common.dto.user.UserDTO;
import cn.emay.eucp.common.moudle.db.system.Department;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.DepartmentService;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.data.service.system.UserDepartmentAssignService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 部门管理
 *
 * @author Frank
 *
 */
@PageAuth("SYS_USER_DEPARTMENT")
@RequestMapping("/sys/user/department")
@Controller
public class DepartmentController {

	private static Logger log = Logger.getLogger(DepartmentController.class);

	@Resource(name = "departmentService")
	private DepartmentService departmentService;

	@Resource(name = "userDepartmentAssignService")
	private UserDepartmentAssignService userDepartmentAssignService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "manageUserOperLogService")
	private ManageUserOperLogService manageUserOperLogService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		Long depId=RequestUtils.getLongParameter(request, "depId", 0l);
		String exedPath = RequestUtils.getParameter(request, "exedPath","0");
		String selectPath = RequestUtils.getParameter(request, "selectPath","0");
		request.setAttribute("depId", depId);
		request.setAttribute("exedPath", exedPath);
		request.setAttribute("selectPath", selectPath);
		Result result = check(depId);
		if (!result.getSuccess()) {
			redirectAttributes.addFlashAttribute("msg", result.getMessage());
			return "redirect:" + WebUtils.getLocalAddress(request) + "/error";
		}
		return "sys/user/department/department";
	}
	@RequestMapping("/tree")
	public String tree(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/user/department/tree";
	}
	@OperationAuth("OPER_SYS_DEPARTMENT_DETAIL")
	@RequestMapping("/see")
	public String see(HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		Long depId=RequestUtils.getLongParameter(request, "depId", 0l);
		Long parentId=RequestUtils.getLongParameter(request, "parentId", 0l);
		String exedPath = RequestUtils.getParameter(request, "exedPath","0");
		String selectPath = RequestUtils.getParameter(request, "selectPath","0");
		request.setAttribute("exedPath", exedPath);
		request.setAttribute("selectPath", selectPath);
		request.setAttribute("depId", depId);
		request.setAttribute("parentId", parentId);
		Result result = check(depId);
		Result result2 = check(parentId);
		if (!result.getSuccess()) {
			redirectAttributes.addFlashAttribute("msg", result.getMessage());
			return "redirect:" + WebUtils.getLocalAddress(request) + "/error";
		}
		if (!result2.getSuccess()) {
			redirectAttributes.addFlashAttribute("msg", result2.getMessage());
			return "redirect:" + WebUtils.getLocalAddress(request) + "/error";
		}
		return "sys/user/department/see";
	}

	public Result check(Long depId) {
		if (depId>0l) {
			Department department = departmentService.findDepartmentById(depId);
			if (null==department) {
				return Result.badResult("部门不存在");
			}
			if(!department.getEnterpriseId().equals(Department.MANAGE_ENTERPRISEID)){
				return Result.badResult("部门不存在");
			}
		}
		return Result.rightResult();
	}
	/**
	 * 部门树形
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/getTree")
	public void getTree(HttpServletRequest request, HttpServletResponse response) {
		List<Department> list = departmentService.findByParentId(0l,Department.MANAGE_ENTERPRISEID);
		ResponseUtils.outputWithJson(response, Result.rightResult(list));
	}
	@RequestMapping("/ajax/getDeptTree")
	public void getDeptTree(HttpServletRequest request, HttpServletResponse response) {
		String nodeId = request.getParameter("id");
		if( null == nodeId || "".equals(nodeId)){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id", "0");
			map.put("name", "全部");
			map.put("isParent", true);
			map.put("iconSkin", "pIcon01");
			ResponseUtils.outputWithJson(response, map);
			return;
		}else{
			Long lid = Long.parseLong(nodeId);
			List<Map<String, Object>> departTree = departmentService.getDepartTree(lid,Department.MANAGE_ENTERPRISEID);
			ResponseUtils.outputWithJson(response, departTree);
		}
	}

	/**
	 * 添加部门
	 *
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_DEPARTMENT_ADD")
	@RequestMapping(value = "/ajax/add")
	public void add(HttpServletRequest request, HttpServletResponse response) {
		String departmentName = RequestUtils.getParameter(request, "name");
		Department department = new Department();
		String errorMsg = checkInfo(departmentName, null);
		if (!StringUtils.isEmpty(errorMsg)) {
			ResponseUtils.outputWithJson(response, Result.badResult(errorMsg));
			return;
		}
		Long parentId = RequestUtils.getLongParameter(request, "parentId", -1l);
		if(parentId==-1l){
			ResponseUtils.outputWithJson(response, Result.badResult("上级部门不存在"));
			return;
		}
		if (parentId == 0l) {
			department.setFullPath(0 + ",");
		} else {
			Department dept = departmentService.findDepartmentById(parentId);
			if (dept == null) {
				ResponseUtils.outputWithJson(response, Result.badResult("上级部门不存在"));
				return;
			}
			department.setFullPath(dept.getFullPath() + parentId + ",");// 全路径
		}
		department.setParentDepartmentId(parentId);
		department.setDepartmentName(departmentName);
		department.setIsDelete(false);
		department.setEnterpriseId(Department.MANAGE_ENTERPRISEID);//系统用户没有所属企业
		Result result = departmentService.addDepartment(department);
		ResponseUtils.outputWithJson(response, Result.rightResult());
		if (result.getSuccess()) {
			String service = "公共服务";
			String context = "添加部门:{0}";
			String module = "部门管理";
			User currentUser = WebUtils.getCurrentUser(request, response);
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] {departmentName }), ManageUserOperLog.OPERATE_ADD);
			log.info("用户:"+currentUser.getUsername()+"添加部门,部门名称为:"+departmentName);
		}
	}

	/**
	 * 删除部门
	 *
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_DEPARTMENT_DELETE")
	@RequestMapping(value = "/ajax/delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) {
			long id = RequestUtils.getLongParameter(request, "id", 0l);
			if (id <= 0l) {
				ResponseUtils.outputWithJson(response, Result.badResult("部门不存在"));
				return;
			}
			User currentUser = WebUtils.getCurrentUser(request, response);
			Department department = departmentService.findDepartmentById(id);
			if (null==department) {
				ResponseUtils.outputWithJson(response, Result.badResult("部门不存在"));
				return;
			}
			if(!department.getEnterpriseId().equals(Department.MANAGE_ENTERPRISEID)){
				ResponseUtils.outputWithJson(response, Result.badResult("部门不存在"));
				return;
			}
			Long depCount = departmentService.findCountByParentId(id);
			if (depCount>0) {
				ResponseUtils.outputWithJson(response, Result.badResult("该部门下有子部门，请先删除子部门"));
				return;
			}
			Long userCount = userDepartmentAssignService.findByDepId(id);
			if (userCount> 0) {
				ResponseUtils.outputWithJson(response, Result.badResult("该部门下有用户存在，不能删除"));
				return;
			}
			Result result = departmentService.deleteDepartment(id);
			if (result.getSuccess()) {
				String service = "公共服务";
				String context = "删除部门:{0}";
				String module = "部门管理";
				manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] {result.getResult() }),ManageUserOperLog.OPERATE_DELETE);
				log.info("用户:"+currentUser.getUsername()+"删除部门,部门名称为:"+result.getResult());
			}
			ResponseUtils.outputWithJson(response, Result.rightResult());
	}

	/**
	 * 部门详细信息
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/detail")
	public void detail(HttpServletRequest request, HttpServletResponse response) {
		Long deptId = RequestUtils.getLongParameter(request, "departmentId", 0l);
		if (deptId <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("请选择部门"));
			return;
		}
		Department department = departmentService.findDepartmentById(deptId);
		if (department == null) {
			ResponseUtils.outputWithJson(response, Result.badResult("部门不存在"));
			return;
		}
		if(!department.getEnterpriseId().equals(Department.MANAGE_ENTERPRISEID)){
			ResponseUtils.outputWithJson(response, Result.badResult("部门不存在"));
			return;
		}
		Department parentDepartment=null;
		if(department.getParentDepartmentId()!=0l){
			parentDepartment = departmentService.findDepartmentById(department.getParentDepartmentId());
			if (parentDepartment == null) {
				ResponseUtils.outputWithJson(response, Result.badResult("该部门信息有误"));
				return;
			}
		}
		String departmentName="";
		if(department.getParentDepartmentId()!=0l){
			departmentName=parentDepartment.getDepartmentName();
		}
		DepartmentDTO dto = new DepartmentDTO(department, departmentName);
		ResponseUtils.outputWithJson(response, Result.rightResult(dto));
	}

	/**
	 * 修改部门名称
	 *
	 * @param request
	 * @param response
	 */
	
	@OperationAuth("OPER_SYS_DEPARTMENT_UPDATE")
	@RequestMapping(value = "/ajax/modify")
	public void modify(HttpServletRequest request, HttpServletResponse response) {
		String errorMsg = "";
		User currentUser = WebUtils.getCurrentUser(request, response);
		long id = RequestUtils.getLongParameter(request, "id", 0l);
		String name = RequestUtils.getParameter(request, "name");
		Department dept = departmentService.findDepartmentById(id);
		if (dept == null) {
			errorMsg = "该部门不存在";
			ResponseUtils.outputWithJson(response, Result.badResult(errorMsg));
			return;
		}
		if(!dept.getEnterpriseId().equals(Department.MANAGE_ENTERPRISEID)){
			ResponseUtils.outputWithJson(response, Result.badResult("部门不存在"));
			return;
		}
		errorMsg = checkInfo(name, id);
		if (!StringUtils.isEmpty(errorMsg)) {
			ResponseUtils.outputWithJson(response, Result.badResult(errorMsg));
			return;
		}
		dept.setDepartmentName(name);
		Result result = departmentService.modifyDepartment(dept);
		if (result.getSuccess()) {
			String service = "公共服务";
			String context = "修改部门:{0}";
			String module = "部门管理";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] {name}), ManageUserOperLog.OPERATE_MODIFY);
			log.info("用户:"+currentUser.getUsername()+"修改部门,部门名称为:"+name);
		}
		ResponseUtils.outputWithJson(response, Result.rightResult());
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
		List<Department> childrenNode = departmentService.findByParentId(id,Department.MANAGE_ENTERPRISEID);
		ResponseUtils.outputWithJson(response, Result.rightResult(childrenNode));
	}

	/**
	 * 部门下部门
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/find")
	public void findDepartment(HttpServletRequest request, HttpServletResponse response) {
		long id = RequestUtils.getLongParameter(request, "id", 0l);
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		String departmentName = RequestUtils.getParameter(request, "departmentName");
		if (id < 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("请选择部门"));
			return;
		}
		Page<DepartmentDTO> list = departmentService.findDepartmentByLikeName(id, departmentName,Department.MANAGE_ENTERPRISEID,start, limit);
		ResponseUtils.outputWithJson(response, Result.rightResult(list));
	}

	/**
	 * 部门员工列表
	 *
	 * @param request
	 * @param response
	 */
	@OperationAuth("OPER_SYS_DEPARTMENT_DETAIL")
	@RequestMapping(value = "/ajax/childlist")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		long deptId = RequestUtils.getLongParameter(request, "deptId", 0l);
		String variableName = RequestUtils.getParameter(request, "variableName");
		if (deptId <= 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("请选择部门"));
			return;
		}
		Department dept = departmentService.findDepartmentById(deptId);
		if (dept == null) {
			ResponseUtils.outputWithJson(response, Result.badResult("部门不存在"));
			return;
		}
		if(!dept.getEnterpriseId().equals(Department.MANAGE_ENTERPRISEID)){
			ResponseUtils.outputWithJson(response, Result.badResult("部门不存在"));
			return;
		}
		Page<UserDTO> userList = userService.findBycondition(variableName, deptId, start, limit);
		ResponseUtils.outputWithJson(response, Result.rightResult(userList));
	}

	/**
	 * 校验信息
	 *
	 * @param request
	 * @param department
	 * @return
	 */
	public String checkInfo(String name, Long id) {
		String errorMsg = "";
		if (StringUtils.isEmpty(name)) {
			errorMsg = "部门名称不能为空";
			return errorMsg;
		}
		if (name.length() > 20) {
			errorMsg = "部门名称长度不能超过20个字符";
			return errorMsg;
		}
		if (!RegularUtils.checkString(name)) {
			errorMsg = "请输入中文和英文";
			return errorMsg;
		}
		if (!RegularUtils.notExistSpecial(name)) {
			errorMsg = "名称不能包含特殊字符";
			return errorMsg;
		}
		Long count = departmentService.findDepartmentByName(name, id);
		if (count > 0l) {
			errorMsg = "部门名称已存在";
			return errorMsg;
		}
		return errorMsg;
	}
}
