package cn.emay.eucp.data.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.emay.common.Result;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.auth.ResourceTreeNode;
import cn.emay.eucp.common.auth.ResourceTreeNodeGenerater;
import cn.emay.eucp.common.auth.Token;
import cn.emay.eucp.common.constant.EucpSystem;
import cn.emay.eucp.common.dto.user.RoleDTO;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.EnterpriseAuth;
import cn.emay.eucp.common.moudle.db.system.Resource;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.AuthInterceptorService;
import cn.emay.eucp.data.service.system.BusinessService;
import cn.emay.eucp.data.service.system.EnterpriseAuthService;
import cn.emay.eucp.data.service.system.EnterpriseService;
import cn.emay.eucp.data.service.system.PassportService;
import cn.emay.eucp.data.service.system.ResourceService;
import cn.emay.eucp.data.service.system.UserService;

@Service("authInterceptorService")
public class AuthInterceptorServiceImpl implements AuthInterceptorService {

	@javax.annotation.Resource
	private UserService userService;
	@javax.annotation.Resource
	private ResourceService resourceService;
	@javax.annotation.Resource
	private EnterpriseService enterpriseService;
	@javax.annotation.Resource
	private EnterpriseAuthService enterpriseAuthService;
	@javax.annotation.Resource
	private BusinessService businessService;
	@javax.annotation.Resource
	private PassportService passportService;

	@Override
	public Result authErrorHandle(String tokenId, String passportPath, String localSystem, String fromUrl) {
		// 未登录，去登录页面
		String tokenStr = passportService.getCurrentTokenStr(tokenId, true);
		Token token = JsonHelper.fromJson(Token.class, tokenStr);
		if (token == null) {
			return Result.badResult("noLogin", "", null);
		}
		User user = userService.findById(token.getUser().getId());
		// 判断用户状态是否正常
		if (user == null || user.getState() == User.STATE_OFF || user.getState() == User.STATE_DELETE) {
			return Result.badResult("error", "您的账号已被锁定，请联系管理员解锁", null);
		}
		// 用户被管理员重置密码
		Result resetPasswordResult = userService.checkUserResetPassword(user.getId());
		if (!resetPasswordResult.getSuccess()) {
			return Result.badResult("restPassword", "您的密码已经被管理员重置，请重新登录", null);
		}
		// 第一次登陆，需要修改密码
		if (user.getLastChangePasswordTime() == null) {
			String redirectUrl = passportPath + "/toChangePassword?system=" + localSystem + "&fromUrl=" + fromUrl;
			return Result.badResult("redirect", redirectUrl, null);
		}

		boolean isHasLoginAuth = false;

		List<RoleDTO> rolelist = userService.findAllUserRoles(user.getId());
		for (RoleDTO dto : rolelist) {
			if (localSystem.equals(dto.getRoleType())) {
				isHasLoginAuth = true;
				break;
			}
		}

		if (!isHasLoginAuth) {
			String errorMsg = "noAuthToLogin&system=" + localSystem + "&fromUrl=" + fromUrl;
			return Result.badResult("noLoginAuth", errorMsg, null);
		}

		Map<String, Object> requestAttributeMap = new HashMap<String, Object>();
		requestAttributeMap.put("USER", user);

		return Result.rightResult(requestAttributeMap);
	}

	@Override
	public Result authHandle(Boolean isAjaxRequest, String tokenStr, User user, String[] pageAuthCode, String[] operationAuthCode, String contextPath, String requestUri,String localSystem,String businessType, String domainPath) {
		Token token = JsonHelper.fromJson(Token.class, tokenStr);
		Set<String> res = token.getResourceAuth();

		Map<String, Object> requestAttributeMap = new HashMap<String, Object>();
		Map<String, Boolean> authMap = new HashMap<String, Boolean>();
		// 拼接权限JS
		String script = "var AUTHS = {};\n";
					
		Enterprise enterprise = enterpriseService.findByUserId(user.getId());
		requestAttributeMap.put("ENTERPRISE", enterprise);

		// 客户用户权限处理
		Set<String> removeAuth = new HashSet<String>();
		if (User.USER_TYPE_CLIENT.equals(user.getUserType())) {
			List<Resource> authResources = token.getAllResourceAuth();
			// 判断是否是代理商，若不是代理商则移除代理商相关资源
			boolean isAgentEnterprise = false;
			if (null != enterprise && Enterprise.TYPE_AGENT.equals(enterprise.getType())) {
				isAgentEnterprise = true;
			}
			if (!isAgentEnterprise) {
				removeAgentResource(authResources, removeAuth);
			}

			if (enterprise != null) {
				// 匹配客户权限级别：基础版、专业版、高级版
				List<String> enterpriseAuths = enterpriseAuthService.findEnterpriseAuth(enterprise.getAuthority(), EnterpriseAuth.AUTH_TYPE_PAGE);
				this.compareClientAuth(authResources, enterpriseAuths, removeAuth);
				// 若客户没有选择增值服务，则客户端不显示增值服务相关菜单
				if (StringUtils.isEmpty(enterprise.getValueAddedService())) {
					this.removeValueAddedServiceResource(authResources, removeAuth);
				}else{
					// 短链接精确版才显示短链接明细页面
					if(enterprise.getValueAddedService().indexOf(Enterprise.VALUE_ADDED_SERVICE_ACCURATE) == -1){
						this.removeValueShortLinkResource(authResources, removeAuth);
					}
				}
				// 若客户没有开通服务，则客户端不显示该服务相关信息
				List<String> openServiceList = businessService.findBusinessCodeByIds(enterprise.getServiceType());
				this.removeNotOpenServiceResource(authResources, openServiceList, removeAuth);
			}
		}

		// 资源权限判断
		boolean isHasPageAuth = false;// 是否拥有页面权限
		if (pageAuthCode == null || pageAuthCode.length == 0) {
			isHasPageAuth = true;
		} else {
			for (String pc : pageAuthCode) {
				if (res.contains(pc) && !removeAuth.contains(pc)) {
					isHasPageAuth = true;
					break;
				}
			}
		}
		boolean isHasOperAuth = false;// 是否拥有操作权限
		if (operationAuthCode == null || operationAuthCode.length == 0) {
			isHasOperAuth = true;
		} else {
			for (String oc : operationAuthCode) {
				if (res.contains(oc) && !removeAuth.contains(oc)) {
					isHasOperAuth = true;
					break;
				}
			}
		}

		boolean isHasAuth = isHasPageAuth && isHasOperAuth;
		// 无权限，拒绝访问
		if (!isHasAuth) {
			return Result.badResult("error", "无权访问", null);
		}

		// 非AJAX请求，需要定位当前页面
		if (!isAjaxRequest) {
			String pagecode = "";
			String pageName = null;
			String pageUrl = null;
			String navcode = "";
			String navName = "";
			String moudlecode = "";
			String moudleName = "";
			if (!StringUtils.isEmpty(requestUri)) {
				ResourceTreeNode root = token.getTree();
				BK: for (ResourceTreeNode module : root.getChildren()) {
					if (null == module || null == module.getChildren()) {
						continue;
					}
					if(!module.getResource().getResourceFor().equalsIgnoreCase(localSystem)){
						continue;
					}
					for (ResourceTreeNode nav : module.getChildren()) {
						if (null == nav || null == nav.getChildren()) {
							continue;
						}
						for (ResourceTreeNode page : nav.getChildren()) {
							if (null == page || null == page.getResource()) {
								continue;
							}
							String realPage = contextPath + page.getResource().getResourcePath() + "/";
							if (requestUri.startsWith(realPage)) {
								moudlecode = module.getResource().getResourceCode();
								moudleName = module.getResource().getResourceName();
								navcode = nav.getResource().getResourceCode();
								pageName = page.getResource().getResourceName();
								pagecode = page.getResource().getResourceCode();
								pageUrl = page.getResource().getResourcePath();
								navName = nav.getResource().getResourceName();
								break BK;
							}
						}
					}
				}
			}

			requestAttributeMap.put("CURRENT_PAGE_CODE", pagecode);
			requestAttributeMap.put("CURRENT_PAGE_URL", pageUrl);
			requestAttributeMap.put("CURRENT_NAV_CODE", navcode);
			requestAttributeMap.put("CURRENT_MOUDLE_CODE", moudlecode);
			requestAttributeMap.put("CURRENT_PAGE_NAME", pageName);
			requestAttributeMap.put("CURRENT_NAV_NAME", navName);
			requestAttributeMap.put("CURRENT_MOUDLE_NAME", moudleName);

			for (String auth : res) {
				script += "\t\tAUTHS['" + auth + "'] = true;\n";
				authMap.put(auth, true);
			}
			// 客户用户权限处理
			if (User.USER_TYPE_CLIENT.equals(user.getUserType())) {
				// 拼接权限相关JS
				for (String enterAuth : removeAuth) {
					script += "\t\tAUTHS['" + enterAuth + "'] = false;\n";
					authMap.put(enterAuth, false);
				}
				// 客户权限
				List<String> enterpriseAuths = enterpriseAuthService.findEnterpriseAuth(enterprise.getAuthority(), EnterpriseAuth.AUTH_TYPE_COLUMNS);
				if (enterpriseAuths != null && enterpriseAuths.size() > 0) {
					for (String enterAuth : enterpriseAuths) {
						script += "\t\tAUTHS['" + enterAuth + "'] = true;\n";
						authMap.put(enterAuth, true);
					}
				}
				// 判断是否代理商用户
				boolean IS_AGENT = true;
				if (null == enterprise || Enterprise.TYPE_NORMAL.equals(enterprise.getType())) {
					IS_AGENT = false;
				}
				script += "\t\tvar IS_AGENT = '" + IS_AGENT + "';\n";
				requestAttributeMap.put("IS_AGENT", IS_AGENT);
			}
			requestAttributeMap.putAll(authMap);
		}
		
		script += "\t\tvar MOUDLES_URLS = {};\n";
		
		//重构顶级URL
		ResourceTreeNode root = token.getTree();
		List<Resource> systemResources = resourceService.findAll();
		List<Resource> authResources = new ArrayList<Resource>();
		genTopUlr(root, authMap, authResources);
		ResourceTreeNode newroot = ResourceTreeNodeGenerater.genAuthTree(systemResources, authResources);
		ResourceTreeNode[] modules = newroot.getChildren();
		for(ResourceTreeNode moudle : modules){
			if(moudle.getResource().getResourceFor().equalsIgnoreCase(localSystem)){
				script += "\t\tMOUDLES_URLS['" + moudle.getResource().getResourceCode() + "'] = '"+ domainPath+"/"+localSystem.toLowerCase()+moudle.getResource().getBusinessType()+moudle.getResource().getResourcePath()+"';\n";
				if(moudle.getResource().getBusinessType().equalsIgnoreCase(businessType)){
					requestAttributeMap.put("THIS_MOUDLE", moudle.getResource());
				}
			}
		}
		
		requestAttributeMap.put("AUTH_SCRIPT", script);
		
		return Result.rightResult(requestAttributeMap);
	}
	
	private void genTopUlr(ResourceTreeNode root , Map<String, Boolean> authMap,List<Resource> authResources){
		if(root == null){
			return;
		}
		if(root.getResource() != null){
			Boolean result = authMap.get(root.getResource().getResourceCode()) ;
			if(result != null && result == true){
				authResources.add(root.getResource());
			}
		}
		if(root.getChildren() != null && root.getChildren().length != 0){
			for(ResourceTreeNode node : root.getChildren()){
				genTopUlr(node, authMap, authResources);
			}
		}
	}

	/**
	 * 去掉代理商相关资源
	 */
	private void removeAgentResource(List<Resource> authResources, Set<String> removeAuth) {
		for (Resource resource : authResources) {
			if (Enterprise.TYPE_AGENT.equals(resource.getResourceForCustomer())) {
				removeAuth.add(resource.getResourceCode());
			}
		}
	}

	private void compareClientAuth(List<Resource> authResources, List<String> enterpriseAuths, Set<String> removeAuth) {
		// 判断客户端的页面资源是否有客户权限
		for (Resource resource : authResources) {
			if (EucpSystem.客户系统.getCode().equals(resource.getResourceFor()) && Resource.RESOURCE_TYPE_PAGE.equals(resource.getResourceType())
					&& ("sms".equals(resource.getBusinessType()) || StringUtils.isEmpty(resource.getBusinessType()))) {
				if (enterpriseAuths == null || !enterpriseAuths.contains(resource.getResourceCode())) {
					removeAuth.add(resource.getResourceCode());
				}
			}
		}
	}

	/**
	 * 移除客户端增值服务相关菜单
	 * 
	 * @param authResources
	 */
	private void removeValueAddedServiceResource(List<Resource> authResources, Set<String> removeAuth) {
		for (Resource resource : authResources) {
			if (EucpSystem.客户系统.getCode().equals(resource.getResourceFor()) && Resource.RESOURCE_TYPE_PAGE.equals(resource.getResourceType())) {// 客户端菜单
				if ("SMS_CLIENT_MESSAGE_SEND_SHORT_LINK".equals(resource.getResourceCode())) {// 短链短信发送菜单
					removeAuth.add(resource.getResourceCode());
				} else if ("SMS_CLIENT_SHORT_LINK_STATISTICS".equals(resource.getResourceCode())) {// 短链接点击率报表
					removeAuth.add(resource.getResourceCode());
				}else if ("SMS_CLIENT_SHORT_LINK".equals(resource.getResourceCode())) {// 短链接管理
					removeAuth.add(resource.getResourceCode());
				}else if ("SMS_CLIENT_SHORT_LINK_INFO".equals(resource.getResourceCode())) {// 短链接明细
					removeAuth.add(resource.getResourceCode());
				}
			}
		}
	}
	
	/**
	 * 移除客户端短链接明细相关菜单
	 * 
	 * @param authResources
	 */
	private void removeValueShortLinkResource(List<Resource> authResources, Set<String> removeAuth) {
		for (Resource resource : authResources) {
			if (EucpSystem.客户系统.getCode().equals(resource.getResourceFor()) && Resource.RESOURCE_TYPE_PAGE.equals(resource.getResourceType())) {// 客户端菜单
				if ("SMS_CLIENT_SHORT_LINK".equals(resource.getResourceCode())) {// 短链接管理
					removeAuth.add(resource.getResourceCode());
				}else if ("SMS_CLIENT_SHORT_LINK_INFO".equals(resource.getResourceCode())) {// 短链接明细
					removeAuth.add(resource.getResourceCode());
				}
			}
		}
	}
	

	/**
	 * 移除客户端未开通服务相关菜单
	 * 
	 * @param authResources
	 * @return
	 */
	private void removeNotOpenServiceResource(List<Resource> authResources, List<String> openServiceList, Set<String> removeAuth) {
		for (Resource resource : authResources) {
			if (EucpSystem.客户系统.getCode().equals(resource.getResourceFor())) {// 客户端
				if (openServiceList == null || openServiceList.isEmpty()) {
					if (!StringUtils.isEmpty(resource.getBusinessType())) {
						removeAuth.add(resource.getResourceCode());
					}
				} else {
					if (!StringUtils.isEmpty(resource.getBusinessType()) && !openServiceList.contains(resource.getBusinessType())) {
						removeAuth.add(resource.getResourceCode());
					}
				}
			}
		}
	}

}