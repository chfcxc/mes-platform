package cn.emay.eucp.web.client.controller.sys;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.constant.GlobalConstants;
import cn.emay.eucp.common.moudle.db.system.Business;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign;
import cn.emay.eucp.data.service.system.BusinessService;
import cn.emay.eucp.data.service.system.ClientUserOperLogService;
import cn.emay.eucp.data.service.system.EnterpriseService;
import cn.emay.eucp.data.service.system.UserDepartmentAssignService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.auth.annotation.OperationAuth;
import cn.emay.eucp.web.common.auth.annotation.PageAuth;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 账号与特服号绑定
 * 
 * @author Araneid
 * 
 */
@PageAuth("SYS_CLIENT_ACCOUNT")
@RequestMapping("/sys/client/account")
@Controller
public class SysServiceCodeUserController {

	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "businessService")
	private BusinessService businessService;
	@Resource(name = "enterpriseService")
	private EnterpriseService enterpriseService;
	@Resource(name = "clientUserOperLogService")
	private ClientUserOperLogService clientUserOperLogService;
	@Resource(name = "userDepartmentAssignService")
	private UserDepartmentAssignService userDepartmentAssignService;
	// private Logger log = Logger.getLogger(SysServiceCodeUserController.class);

	/**
	 * 客户端特服号管理页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "sys/bindservice/bindInfo";
	}

	/**
	 * 列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		String userName = RequestUtils.getParameter(request, "userName");
		int start = RequestUtils.getIntParameter(request, "start", GlobalConstants.DEFAULT_PAGE_START);
		int limit = RequestUtils.getIntParameter(request, "limit", GlobalConstants.DEFAULT_PAGE_LIMIT);
		User user = WebUtils.getCurrentUser(request, response);
		Page<Map<String, Object>> page = findServiceCodeBindingPageClient(start, limit, userName, user.getId());
		ResponseUtils.outputWithJson(response, Result.rightResult(page));
	}

	/**
	 * 查看用户绑定特服号详情页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/to/cb/{userId}")
	@OperationAuth("SYS_CLIENT_ACCOUNT_CHECK")
	public String toInfo(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable("userId") Long userId) {
		model.addAttribute("userId", userId);
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			return "redirect:" + WebUtils.getLocalAddress(request) + "/error";
		}
		String type = userService.findenterpriseTypebyUserId(userId);
		model.addAttribute("type", type);
		return "sys/bindservice/checkbind";
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
	 * 查看用户已经绑定的流量服务号
	 * 
	 * @param request
	 * @param response
	 */
	/*TODO@RequestMapping("/ajax/findBindFlowServiceCode")
	@OperationAuth("SYS_CLIENT_ACCOUNT_CHECK")
	public void findBindFlowServiceCode(HttpServletRequest request, HttpServletResponse response) {
		User currentUser = WebUtils.getCurrentUser(request, response);
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		List<SimpleFlowServiceCodeDTO> dtoList = flowServiceCodeService.findByAssignUserId(userId);
		ResponseUtils.outputWithJson(response, Result.rightResult(dtoList));
	}*/
	
	/**
	 * 查看用户已经绑定的国际短信服务号
	 * 
	 * @param request
	 * @param response
	 */
	/*TODO@RequestMapping("/ajax/findBindImsServiceCode")
	@OperationAuth("SYS_CLIENT_ACCOUNT_CHECK")
	public void findBindImsServiceCode(HttpServletRequest request, HttpServletResponse response) {
		User currentUser = WebUtils.getCurrentUser(request, response);
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		List<ImsServiceCode> dtoList = imsUserServiceCodeAssignService.findByAssignUserId(userId);
		ResponseUtils.outputWithJson(response, Result.rightResult(dtoList));
	}*/
	
	/**
	 * 查看用户已经绑定的語音短信服务号
	 * 
	 * @param request
	 * @param response
	 */
	/*TODO@RequestMapping("/ajax/findBindVoiceServiceCode")
	@OperationAuth("SYS_CLIENT_ACCOUNT_CHECK")
	public void findBindVoiceServiceCode(HttpServletRequest request, HttpServletResponse response) {
		User currentUser = WebUtils.getCurrentUser(request, response);
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		List<VoiceServiceCode> dtoList = voiceServiceCodeService.findByAssignUserId(userId);
		ResponseUtils.outputWithJson(response, Result.rightResult(dtoList));
	}*/

	/**
	 * 跳转到特服号进行绑定页面
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/to/usb/{userId}/{userName}")
	@OperationAuth("SYS_CLIENT_ACCOUNT_BINDING")
	public String toBinding(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable("userId") Long userId, @PathVariable("userName") String userName) {
		model.addAttribute("userId", userId);
		model.addAttribute("userName", userName);
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			return "redirect:" + WebUtils.getLocalAddress(request) + "/error";
		}
		String type = userService.findenterpriseTypebyUserId(userId);
		model.addAttribute("type", type);
		return "sys/bindservice/servicebind";
	}

	/**
	 * 绑定流量服务号
	 * 
	 * @param request
	 * @param response
	 */
	/*TODO@RequestMapping("/ajax/findFlowServiceCode")
	@OperationAuth("SYS_CLIENT_ACCOUNT_BINDING")
	public void findFlowServiceCode(HttpServletRequest request, HttpServletResponse response) {
		User currentUser = WebUtils.getCurrentUser(request, response);
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		long enterpriseId = userService.findenterpriseIdbyUser(userId);
		if (enterpriseId == 0l) {
			ResponseUtils.outputWithJson(response, Result.badResult("参数不正确"));
			return;
		}
		Map<String, Object> map = flowUserSCAssignService.findBinding(userId, enterpriseId);
		ResponseUtils.outputWithJson(response, Result.rightResult(map));
	}*/
	
	/**
	 * 绑定国际短信服务号
	 * 
	 * @param request
	 * @param response
	 */
	/*TODO@RequestMapping("/ajax/findImsServiceCode")
	@OperationAuth("SYS_CLIENT_ACCOUNT_BINDING")
	public void findImsServiceCode(HttpServletRequest request, HttpServletResponse response) {
		User currentUser = WebUtils.getCurrentUser(request, response);
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		Map<String, Object> map = imsUserServiceCodeAssignService.findBinding(userId, (Long)result2.getResult());
		ResponseUtils.outputWithJson(response, Result.rightResult(map));
	}*/
	
	/**
	 * 绑定语音短信服务号
	 * 
	 * @param request
	 * @param response
	 */
	/*TODO@RequestMapping("/ajax/findVoiceServiceCode")
	@OperationAuth("SYS_CLIENT_ACCOUNT_BINDING")
	public void findVoiceServiceCode(HttpServletRequest request, HttpServletResponse response) {
		User currentUser = WebUtils.getCurrentUser(request, response);
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		long enterpriseId = userService.findenterpriseIdbyUser(userId);
		Map<String, Object> map = voiceServiceCodeService.findBinding(userId, enterpriseId);
		ResponseUtils.outputWithJson(response, Result.rightResult(map));
	}*/
	


	/**
	 * 用户绑定流量服务号操作
	 * 
	 * @param request
	 * @param response
	 */
	/*TODO@RequestMapping("/ajax/bindFlowServiceCode")
	@OperationAuth("SYS_CLIENT_ACCOUNT_BINDING")
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
	@OperationAuth("SYS_CLIENT_ACCOUNT_BINDING")
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
	@OperationAuth("SYS_CLIENT_ACCOUNT_BINDING")
	public void bindVoiceServiceCode(HttpServletRequest request, HttpServletResponse response) {
		Long userId = RequestUtils.getLongParameter(request, "userId", 0l);
		String voiceServiceCodeIds = RequestUtils.getParameter(request, "voiceServiceCodeIds", "");
		User currentUser = WebUtils.getCurrentUser(request, response);
		Result result2 = userService.findenterpriseId(userId, currentUser.getId());
		if (!result2.getSuccess()) {
			ResponseUtils.outputWithJson(response, result2);
			return;
		}
		Long enterpriseId = userService.findenterpriseIdbyUser(userId);
		Result result = voiceServiceCodeService.modifyVoiceUserSCAssign(userId, enterpriseId, voiceServiceCodeIds);
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
	@OperationAuth("SYS_CLIENT_ACCOUNT_BINDING")
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
	@OperationAuth("SYS_CLIENT_ACCOUNT_BINDING")
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
	@OperationAuth("SYS_CLIENT_ACCOUNT_BINDING")
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

	public Page<Map<String, Object>> findServiceCodeBindingPageClient(int start, int limit, String userName, Long userId) {
		Page<Map<String, Object>> page = null;
		if (!RegularUtils.isEmpty(userName)) {
			userName = userName.toLowerCase();
		}
		Long enterpriseId = userService.findenterpriseIdbyUser(userId);
		UserDepartmentAssign userDepartmentAssign = userDepartmentAssignService.findByUserId(userId);
		// Long serviceCodeNum =
		// smsServiceCodeService.countByEnterpriseId(enterpriseId);
		// if (serviceCodeNum > 0) {
		Long clientUserId = null;
		if (UserDepartmentAssign.IDENTITY_EMPLOYEE == userDepartmentAssign.getIdentity()) {
			clientUserId = userId;
		}
		page = userService.findServiceCodeBindingPageClient(start, limit, userName, clientUserId, enterpriseId);
		// } else {
		// page = new Page<Map<String, Object>>();
		// page.setList(new ArrayList<Map<String, Object>>());
		// page.setTotalCount(0);
		// page.setTotalPage(0);
		// page.setStart(start);
		// page.setLimit(limit);
		// }
		return page;
	}

	/**
	 * 客户端特服号绑定 判断当前查看的用户与登录用户是不属于同一企业
	 * 
	 * @param currentUserId
	 * @param userId
	 * @return
	 */
	// private Result checkUser(Long currentUserId, Long userId) {
	// // 判断当前查看的用户与登录用户是不属于同一企业
	// List<User> list = userService.findEnterpriseUsers(currentUserId);
	// Map<Long, User> map = new HashMap<Long, User>();
	// for (User user : list) {
	// map.put(user.getId(), user);
	// }
	// if (!map.containsKey(userId)) {
	// return Result.badResult("数据错误");
	// }
	// return Result.rightResult();
	// }



}
