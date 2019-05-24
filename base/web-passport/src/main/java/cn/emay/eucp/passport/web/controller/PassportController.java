package cn.emay.eucp.passport.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.common.Result;
import cn.emay.common.captcha.CaptchaProducer;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.auth.Token;
import cn.emay.eucp.common.constant.EucpSystem;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.service.system.PassportService;
import cn.emay.eucp.data.service.system.UserService;
import cn.emay.eucp.web.common.WebUtils;
import cn.emay.eucp.web.common.constant.CommonConstants;
import cn.emay.util.DateUtil;
import cn.emay.util.RequestUtils;
import cn.emay.util.ResponseUtils;

@Controller
public class PassportController {

	@javax.annotation.Resource(name = "cptchaProducer")
	private CaptchaProducer cptchaProducer;

	@javax.annotation.Resource(name = "passportService")
	private PassportService passportService;
	@javax.annotation.Resource(name = "userService")
	private UserService userService;

	private Logger log = Logger.getLogger(PassportController.class);

	/**
	 * 图片验证码
	 */
	@RequestMapping("/captcha")
	public void captcha(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		String type = RequestUtils.getParameter(request, "type");
		String cacheKey = UUID.randomUUID().toString().replace("-", "");
		String cookieKey = CommonConstants.CAPTCHA_COOKIE_PER + type;
		Cookie cookie = new Cookie(cookieKey, cacheKey);
		cookie.setDomain(WebUtils.getLocalMainDomain(request));
		cookie.setPath("/");
		WebUtils.addCookie(request, response, cookie);
		cptchaProducer.create(response.getOutputStream(), cacheKey, 5 * 60);
	}

	/**
	 * 前往登录页，只允许同一一級域名的登录
	 */
	@RequestMapping(value = "/toLogin")
	public String toLogin(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		String fromUrl = RequestUtils.getParameter(request, "fromUrl");
		String system = RequestUtils.getParameter(request, "system");

		if (fromUrl == null || system == null) {
			return "redirect:/error?msg=无效访问";
		}
		if (EucpSystem.findNameByCode(system) == null) {
			return "redirect:/error?msg=无效访问";
		}
		String mydomain = WebUtils.getLocalMainDomain(request);
		if (!fromUrl.contains(mydomain)) {
			return "redirect:/error?msg=无效访问";
		}
		String tokenId = WebUtils.getCurrentTokenId(request);
		if (tokenId != null) {
			boolean isLogin = passportService.isCurrentLogin(tokenId, true);
			if (isLogin) {
				return "redirect:" + fromUrl;
			}
		}
		model.addAttribute("fromUrl", fromUrl);
		model.addAttribute("system", system);
		String toLogin = system.toLowerCase() + "/login";
		if ("CLIENT".equalsIgnoreCase(system) && isMobileDevice(request)) {
			toLogin = system.toLowerCase() + "/loginphone";
		}
		return toLogin;
	}

	/**
	 * 登录
	 */
	@RequestMapping(value = "/login")
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = RequestUtils.getParameter(request, "username");
		String password = RequestUtils.getParameter(request, "password");
		String fromUrl = RequestUtils.getParameter(request, "fromUrl");
		String system = RequestUtils.getParameter(request, "system");
		String captcha = RequestUtils.getParameter(request, "captcha");
		String capCode = null;
		String cookieKey = CommonConstants.CAPTCHA_COOKIE_PER + "login";
		Cookie capCodecookie = WebUtils.findCookie(request, cookieKey);
		if (capCodecookie != null) {
			capCode = capCodecookie.getValue();
		}
		if (captcha == null || captcha.trim().equals("")) {
			ResponseUtils.outputWithJson(response, Result.badResult(3, "请输入验证码", null));
			return;
		}
		if (capCode == null || capCode.trim().equals("")) {
			ResponseUtils.outputWithJson(response, Result.badResult(0, "无效访问", null));
			return;
		}
		if (username == null || username.trim().equals("")) {
			ResponseUtils.outputWithJson(response, Result.badResult(0, "请输入用户名", null));
			return;
		}
		if (password == null || password.trim().equals("")) {
			ResponseUtils.outputWithJson(response, Result.badResult(0, "请输入密码", null));
			return;
		}
		if (system == null || system.trim().equals("")) {
			ResponseUtils.outputWithJson(response, Result.badResult(0, "无效访问", null));
			return;
		}
		if (fromUrl == null || fromUrl.trim().equals("")) {
			ResponseUtils.outputWithJson(response, Result.badResult(0, "无效访问", null));
			return;
		}
		boolean isOK = cptchaProducer.checkAndDelete(capCode, captcha);
		if (!isOK) {
			ResponseUtils.outputWithJson(response, Result.badResult(0, "验证码错误", null));
			return;
		}
		Result result = passportService.login(username, password, fromUrl, system);
		if (result.getSuccess() == false) {
			ResponseUtils.outputWithJson(response, result);
			return;
		}
		String tokenId = (String) result.getResult();
		Cookie cookie = new Cookie(WebUtils.COOKIE_TOKEN_ID, tokenId);
		cookie.setDomain(WebUtils.getLocalMainDomain(request));
		cookie.setPath("/");
		WebUtils.addCookie(request, response, cookie);
		log.info("user : " + username + " login at " + DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss") + " , provide token : " + tokenId);
		ResponseUtils.outputWithJson(response, Result.rightResult(fromUrl));
	}

	/**
	 * 登出
	 */
	@RequestMapping(value = "/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		String tokenId = WebUtils.getCurrentTokenId(request);
		if (tokenId == null) {
			return;
		}
		WebUtils.deleteCookie(request, response, WebUtils.COOKIE_TOKEN_ID);
		passportService.logout(tokenId);
		ResponseUtils.outputWithJson(response, Result.rightResult());
	}

	/**
	 * 修改密码页面
	 */
	@RequestMapping(value = "/toChangePassword")
	public String toChangePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String system = RequestUtils.getParameter(request, "system");
		String fromUrl = RequestUtils.getParameter(request, "fromUrl");
		if (fromUrl == null || system == null) {
			return "redirect:/error?msg=无效访问";
		}
		if (EucpSystem.findNameByCode(system) == null) {
			return "redirect:/error?msg=无效访问";
		}
		String mydomain = WebUtils.getLocalMainDomain(request);
		if (!fromUrl.contains(mydomain)) {
			return "redirect:/error?msg=无效访问";
		}
		String tokenId = WebUtils.getCurrentTokenId(request);
		String tokenStr = passportService.getCurrentTokenStr(tokenId, true);
		Token token = JsonHelper.fromJson(Token.class, tokenStr);
		if (token == null) {
			return "/toLogin?system=" + system + "&fromUrl=" + fromUrl;
		}
		String count = "0";
		User user = token.getUser();
		if (user.getLastChangePasswordTime() != null) {
			count = "1";
		}
		request.setAttribute("count", count);
		return "changePassword";
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/changePassword")
	public void changePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String system = RequestUtils.getParameter(request, "system");
		String fromUrl = RequestUtils.getParameter(request, "fromUrl");
		if (EucpSystem.findNameByCode(system) == null) {
			ResponseUtils.outputWithJson(response, Result.badResult("未知请求"));
			return;
		}
		if (fromUrl == null || system == null) {
			ResponseUtils.outputWithJson(response, Result.badResult("未知请求"));
			return;
		}
		String mydomain = WebUtils.getLocalMainDomain(request);
		if (!fromUrl.contains(mydomain)) {
			ResponseUtils.outputWithJson(response, Result.badResult("未知请求"));
			return;
		}
		String password = RequestUtils.getParameter(request, "password");
		String newpassword = RequestUtils.getParameter(request, "newpassword");
		if (newpassword == null || password == null) {
			ResponseUtils.outputWithJson(response, Result.badResult("请输入密码"));
			return;
		}
		String tokenId = WebUtils.getCurrentTokenId(request);
		String tokenStr = passportService.getCurrentTokenStr(tokenId, true);
		Token token = JsonHelper.fromJson(Token.class, tokenStr);
		if (token == null) {
			WebUtils.sendNoLogin(request, response, system, fromUrl);
			return;
		}
		Result result = userService.updateUserPassword(token.getUser().getId(), password, newpassword);
		ResponseUtils.outputWithJson(response, result);
	}

	/**
	 * 错误页
	 */
	@RequestMapping("/error")
	public String error(HttpServletRequest request, HttpServletResponse response, Model model) {
		String msg = RequestUtils.getParameter(request, "msg");
		String fromUrl = RequestUtils.getParameter(request, "fromUrl");
		String system = RequestUtils.getParameter(request, "system");
		request.setAttribute("msg", msg);
		request.setAttribute("fromUrl", fromUrl);
		request.setAttribute("system", system);
		return "error";
	}

	/**
	 * 修改密码错误也
	 */
	@RequestMapping("/resterror")
	public String restError(HttpServletRequest request, HttpServletResponse response, Model model) {
		String msg = RequestUtils.getParameter(request, "msg");
		request.setAttribute("msg", msg);
		return "resterror";
	}

	@RequestMapping("/torelogin")
	public void torelogin(HttpServletRequest request, HttpServletResponse response, Model model) {
		String tokenId = WebUtils.getCurrentTokenId(request);
		if (tokenId == null) {
			return;
		}
		String fromUrl = RequestUtils.getParameter(request, "fromUrl");
		WebUtils.deleteCookie(request, response, WebUtils.COOKIE_TOKEN_ID);
		passportService.logout(tokenId);
		try {
			response.sendRedirect(fromUrl);
		} catch (IOException e) {
			log.error("发生异常！", e);
		}
	}

	public static boolean isMobileDevice(HttpServletRequest request) {
		String requestHeader = request.getHeader("user-agent");
		/**
		 * android : 所有android设备 mac os : iphone ipad windows phone:Nokia等windows系统的手机
		 */
		String[] deviceArray = new String[] { "android", "mac os", "windows phone" };
		if (requestHeader == null)
			return false;
		requestHeader = requestHeader.toLowerCase();
		for (int i = 0; i < deviceArray.length; i++) {
			if (requestHeader.indexOf(deviceArray[i]) > 0) {
				return true;
			}
		}
		return false;
	}
}
