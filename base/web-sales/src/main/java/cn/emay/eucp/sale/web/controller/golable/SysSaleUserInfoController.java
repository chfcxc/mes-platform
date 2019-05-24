package cn.emay.eucp.sale.web.controller.golable;

import java.text.MessageFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

/**
 * 个人信息
 * 
 */
@RequestMapping("/sys/sale/userInfo")
@Controller
public class SysSaleUserInfoController {
	
	private Logger log = Logger.getLogger(SysSaleUserInfoController.class);
	
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "manageUserOperLogService")
	private ManageUserOperLogService manageUserOperLogService;

	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = WebUtils.getCurrentUser(request, response);
		user.setPassword("******");// 隐藏密码
		model.addAttribute("user", user);
		return "golable/userinfo";
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/modifyPassword")
	public void modifyPassword(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		String password = RequestUtils.getParameter(request, "password");
		String newPassword = RequestUtils.getParameter(request, "newPassword");
		String verifyPassword = RequestUtils.getParameter(request, "verifyPassword");
		Result result = userService.updatePassword(user, password, newPassword, verifyPassword);
		if (result.getSuccess()) {
			String service = "销售系统";
			String module = "个人信息";
			String context = "修改密码";
			manageUserOperLogService.saveLog(service, module, user, MessageFormat.format(context, new Object[] {}), ManageUserOperLog.OPERATE_MODIFY);
			log.info("【销售系统】用户:" + user.getUsername() + "修改密码");
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 修改名称
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/modifyRealName")
	public void modifyRealName(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		String realName = RequestUtils.getParameter(request, "realName");
		Result result = userService.updateRealName(user, realName);
		if (result.getSuccess()) {
			String service = "销售系统";
			String module = "个人信息";
			String context = "修改姓名";
			manageUserOperLogService.saveLog(service, module, user, MessageFormat.format(context, new Object[] {}), ManageUserOperLog.OPERATE_MODIFY);
			log.info("【销售系统】用户:" + user.getUsername() + "修改姓名");
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 修改手机号
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/modifyMobile")
	public void modifyMobile(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		String mobile = RequestUtils.getParameter(request, "mobile");
		Result result = userService.updateMoblie(user, mobile);
		if (result.getSuccess()) {
			String service = "销售系统";
			String module = "个人信息";
			String context = "修改手机号";
			manageUserOperLogService.saveLog(service, module, user, MessageFormat.format(context, new Object[] {}), ManageUserOperLog.OPERATE_MODIFY);
			log.info("【销售系统】用户:" + user.getUsername() + "修改手机号");
		}
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 修改邮箱
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/modifyEmail")
	public void modifyEmail(HttpServletRequest request, HttpServletResponse response) {
		User user = WebUtils.getCurrentUser(request, response);
		String email = RequestUtils.getParameter(request, "email");
		Result result = userService.updateEmail(user, email);
		if (result.getSuccess()) {
			String service = "销售系统";
			String module = "个人信息";
			String context = "修改邮箱";
			manageUserOperLogService.saveLog(service, module, user, MessageFormat.format(context, new Object[] {}), ManageUserOperLog.OPERATE_MODIFY);
			log.info("【销售系统】用户:" + user.getUsername() + "修改邮箱");
		}
		ResponseUtils.outputWithJson(response, result);
	}
}
