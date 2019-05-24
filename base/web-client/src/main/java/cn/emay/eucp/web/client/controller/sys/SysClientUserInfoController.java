package cn.emay.eucp.web.client.controller.sys;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.system.ClientUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.ClientUserOperLogService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 个人信息
 * 
 * @author Araneid
 *
 */
@RequestMapping("/sys/client/userinfo")
@Controller
public class SysClientUserInfoController {
	private static Logger log = Logger.getLogger(SysClientUserInfoController.class);
	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "clientUserOperLogService")
	private ClientUserOperLogService clientUserOperLogService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = WebUtils.getCurrentUser(request, response);
		user.setPassword("******");
		model.addAttribute("user", user);
		return "sys/account/accountinfo";
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/mp")
	public void modifyPassword(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		String password = RequestUtils.getParameter(request, "password");
		String newPassword = RequestUtils.getParameter(request, "newPassword");
		String verifyPassword = RequestUtils.getParameter(request, "verifyPassword");
		Result result = userService.updatePassword(user, password, newPassword, verifyPassword);
		if (result.getSuccess()) {
			String service = "系统服务";
			String module = "个人信息";
			String content = result.getResult().toString();
			log.info(content);
			clientUserOperLogService.save(service, module, user, content, ClientUserOperLog.OPERATE_MODIFY);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 修改名称
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/mrn")
	public void modifyRealName(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		String realName = RequestUtils.getParameter(request, "realName");
		Result result = userService.updateRealName(user, realName);
		if (result.getSuccess()) {
			String service = "系统服务";
			String module = "个人信息";
			String content = result.getResult().toString();
			log.info(content);
			clientUserOperLogService.save(service, module, user, content, ClientUserOperLog.OPERATE_MODIFY);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 修改手机号
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/mm")
	public void updateMobile(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		String mobile = RequestUtils.getParameter(request, "mobile");
		Result result = userService.updateMoblie(user, mobile);
		if (result.getSuccess()) {
			String service = "系统服务";
			String module = "个人信息";
			String content = result.getResult().toString();
			log.info(content);
			clientUserOperLogService.save(service, module, user, content, ClientUserOperLog.OPERATE_MODIFY);
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 修改邮箱
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/me")
	public void updateEmail(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		String email = RequestUtils.getParameter(request, "email");
		Result result = userService.updateEmail(user, email);
		if (result.getSuccess()) {
			String service = "系统服务";
			String module = "个人信息";
			String content = result.getResult().toString();
			log.info(content);
			clientUserOperLogService.save(service, module, user, content, ClientUserOperLog.OPERATE_MODIFY);
		}
		ResponseUtils.outputWithJson(response, result);
	}
}
