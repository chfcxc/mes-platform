package cn.emay.eucp.web.manage.controller.sys.client;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.authenum.AuthLevelBindNameEnum;
import cn.emay.eucp.common.constant.EucpSystem;
import cn.emay.eucp.common.dto.auth.AuthTree;
import cn.emay.eucp.common.moudle.db.system.AuthLevelBindName;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.AuthLevelBindNameService;
import cn.emay.eucp.data.service.system.EnterpriseAuthService;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.data.service.system.ResourceService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 客户权限
 *
 */
@PageAuth("SYS_CLIENT_AUTH")
@RequestMapping("/sys/client/auth")
@Controller
public class ClientAuthController {

	@Resource(name = "enterpriseAuthService")
	private EnterpriseAuthService enterpriseAuthService;
	@Resource(name = "resourceService")
	private ResourceService resourceService;
	@Resource(name = "manageUserOperLogService")
	private ManageUserOperLogService manageUserOperLogService;
	
	@Resource(name="authLevelBindNameService")
	private AuthLevelBindNameService authLevelBindNameService;

	private Logger log = Logger.getLogger(ClientAuthController.class);

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/client/auth/infolist";
	}
	

	/**
	 * 获取列表
	 * new
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/list")
	private void getAuthLevelBindNameList(HttpServletRequest request, HttpServletResponse response) {
		String name = RequestUtils.getParameter(request, "authName");
		int start = RequestUtils.getIntParameter(request, "start", 0);
		int limit = RequestUtils.getIntParameter(request, "limit", 20);
		Page<AuthLevelBindName> page = authLevelBindNameService.selectAuthLevelBindName(name, start, limit);
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
	@RequestMapping("/to/add")
	public String add(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/client/auth/listAdd";
	}
	
	
	/**
	 * 修改页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/to/modify")
	public String modify(HttpServletRequest request, HttpServletResponse response, Model model) {
		Long id = RequestUtils.getLongParameter(request, "id", 0l);
		Map<String, Object> info = authLevelBindNameService.findBindLevelById(id);
		model.addAttribute("id", id);
		model.addAttribute("authBindName", info);
		return "sys/client/auth/listModify";
	}
	
	
	/**
	 * 新增客户权限
	 * new
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/add")
	public void  addAuthNameBind(HttpServletRequest request, HttpServletResponse response) {
		int authLevel=4;
		String name = RequestUtils.getParameter(request, "authName");//用户输入名称
		String menuParams = RequestUtils.getParameter(request, "menuParams");
		String columnParams = RequestUtils.getParameter(request, "columnParams");
		
		if(StringUtils.isEmpty(name)) {
			ResponseUtils.outputWithJson(response, Result.badResult("名称不能为空"));
			return;
		}
		
		if (StringUtils.isEmpty(menuParams)) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不能为空"));
			return;
		}
		
		List<AuthLevelBindName> selectAuthLevel = authLevelBindNameService.selectAuthLevel();
		if(null!=selectAuthLevel && selectAuthLevel.size()>0) {
			for (AuthLevelBindName authLevelBindName : selectAuthLevel) {
				if (name.equals(authLevelBindName.getAuthName())) {
					ResponseUtils.outputWithJson(response, Result.badResult("权限名称重复"));
					return;
				}
			}
		}
		
		if(name.equals(AuthLevelBindNameEnum.BASIC.getName())) {
			authLevel=AuthLevelBindNameEnum.BASIC.getIndex();
		}else if(name.equals(AuthLevelBindNameEnum.PROFESSIONAL.getName())) {
			authLevel=AuthLevelBindNameEnum.PROFESSIONAL.getIndex();
		}else if(name.equals(AuthLevelBindNameEnum.ADVANCED.getName())) {
			authLevel=AuthLevelBindNameEnum.ADVANCED.getIndex();
		}else if(name.equals(AuthLevelBindNameEnum.CUSTOM.getName())) {
			authLevel=AuthLevelBindNameEnum.CUSTOM.getIndex();
		}else {
			if(null!=selectAuthLevel && selectAuthLevel.size()>0) {
				authLevel=selectAuthLevel.get(0).getAuthLevel()+1;
			}
		}
		
		authLevelBindNameService.saveAuthLevelBindName(name, authLevel);
		
		Result result = enterpriseAuthService.modifyEnterpriseAuth(authLevel, menuParams, columnParams);
		if (result.getSuccess()) {
			User currentUser = WebUtils.getCurrentUser(request, response);
			String service = "公共服务";
			String module = "客户管理";
			String context = "新增客户权限,权限名称为{0},权限等级为{1}";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] {name,authLevel}), ManageUserOperLog.OPERATE_ADD);
			log.info("【公共服务>客户管理】-->用户:" + currentUser.getUsername() + "新增客户权限");
		}
		ResponseUtils.outputWithJson(response, result);
	}
	
	
	/**
	 * 修改客户权限
	 * new
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/update")
	public void  updateAuthNameBind(HttpServletRequest request, HttpServletResponse response) {
		String name = RequestUtils.getParameter(request, "authName");//用户输入名称
		int authLevel = RequestUtils.getIntParameter(request, "authLevel", 0);
		String menuParams = RequestUtils.getParameter(request, "menuParams");
		String columnParams = RequestUtils.getParameter(request, "columnParams");
		Long id=RequestUtils.getLongParameter(request, "id", 0l);
		
		if(id<=0){
			ResponseUtils.outputWithJson(response, Result.badResult("记录不存在"));
			return;
		}
		
		if (StringUtils.isEmpty(menuParams)) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不能为空"));
			return;
		}
		
		if (StringUtils.isEmpty(name)) {
			ResponseUtils.outputWithJson(response, Result.badResult("名称不能为空"));
			return;
		}
		
		List<AuthLevelBindName> selectAuthLevel = authLevelBindNameService.selectAuthLevel();
		if(null!=selectAuthLevel && selectAuthLevel.size()>0) {
			for (AuthLevelBindName authLevelBindName : selectAuthLevel) {
				if(id.intValue() != authLevelBindName.getId().intValue()) {
					if (name.equals(authLevelBindName.getAuthName())) {
						ResponseUtils.outputWithJson(response, Result.badResult("权限名称重复"));
						return;
					}
				}
			}
		}
		
		
		/*
		 * 更新权限等级名称
		 */
		authLevelBindNameService.updateAuthLevelBindName(id, name);
		
		Result result = enterpriseAuthService.modifyEnterpriseAuth(authLevel, menuParams, columnParams);
		if (result.getSuccess()) {
			User currentUser = WebUtils.getCurrentUser(request, response);
			String service = "公共服务";
			String module = "客户管理";
			String context = "修改客户权限,权限名称为{0},权限等级为{1}";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] {name,authLevel}), ManageUserOperLog.OPERATE_MODIFY);
			log.info("【公共服务>客户管理】-->用户:" + currentUser.getUsername() + "修改客户权限");
			
		}
		ResponseUtils.outputWithJson(response, result);
	}
	
	
	/**
	 * 获取菜单权限树
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/authTree")
	public void getMenuAuthTree(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		AuthTree authTree = resourceService.findMenuAuthTree(EucpSystem.客户系统.getCode());
		map.put("authTree", authTree);
		ResponseUtils.outputWithJson(response, Result.rightResult(map));
	}

	/**
	 * 客户权限信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/info")
	public void info(HttpServletRequest request, HttpServletResponse response) {
		int authLevel = RequestUtils.getIntParameter(request, "authLevel", -1);
		List<String> clientAuth = enterpriseAuthService.findEnterpriseAuth(authLevel, null);
		ResponseUtils.outputWithJson(response, Result.rightResult(clientAuth));
	}

	/**
	 * 修改客户权限
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax/modify")
	public void modify(HttpServletRequest request, HttpServletResponse response) {
		int authLevel = RequestUtils.getIntParameter(request, "authLevel", 0);
		String menuParams = RequestUtils.getParameter(request, "menuParams");
		String columnParams = RequestUtils.getParameter(request, "columnParams");
		/*if (authLevel < EnterpriseAuth.AUTH_LEVEL_BASIC || authLevel > EnterpriseAuth.AUTH_LEVEL_PREMIUM) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数错误"));
			return;
		}*/
		if (StringUtils.isEmpty(menuParams)) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不能为空"));
			return;
		}
		Result result = enterpriseAuthService.modifyEnterpriseAuth(authLevel, menuParams, columnParams);
		if (result.getSuccess()) {
			User currentUser = WebUtils.getCurrentUser(request, response);
			String service = "公共服务";
			String module = "客户管理";
			String context = "修改客户权限";
			manageUserOperLogService.saveLog(service, module, currentUser, MessageFormat.format(context, new Object[] {}), ManageUserOperLog.OPERATE_MODIFY);
			log.info("【公共服务>客户管理】-->用户:" + currentUser.getUsername() + "修改客户权限");
		}
		ResponseUtils.outputWithJson(response, result);
	}

}
